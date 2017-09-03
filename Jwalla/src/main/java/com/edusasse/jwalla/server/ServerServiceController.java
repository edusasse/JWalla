package com.edusasse.jwalla.server;

import java.io.IOException;
import java.math.BigInteger;
import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.security.InvalidAlgorithmParameterException;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.HashMap;
import java.util.Iterator;
import java.util.concurrent.Semaphore;

import com.edusasse.jwalla.client.clients.CorbaClient;
import com.edusasse.jwalla.client.clients.RMIClient;
import com.edusasse.jwalla.client.clients.VirtualClient;
import com.edusasse.jwalla.client.clients.i_JwallaClient;
import com.edusasse.jwalla.client.clients.i_RemoteClient;
import com.edusasse.jwalla.client.service.Instruction;
import com.edusasse.jwalla.client.service.i_Service;
import com.edusasse.jwalla.engine.Constants;
import com.edusasse.jwalla.engine.JwallaController;
import com.edusasse.jwalla.rmi.applications.i_Communication;
import com.edusasse.jwalla.security.Crypter;
import com.edusasse.jwalla.security.Decrypter;
import com.edusasse.jwalla.security.MyKeyPairGenerator;
import com.edusasse.jwalla.util.XMLPreDefined;

public class ServerServiceController implements i_Communication, i_MasterServer {
	// Controla se esta estacao eh a servidora
	private boolean server;
	// Nome do servico
	private i_Service service;

	// ** CLIENTES **
	// Mapa dos clientes virtuais
	private HashMap<Long, VirtualClient> mapVirtClients;
	// Mapa dos clientes
	private HashMap<Long, i_RemoteClient> mapClients;

	// Controla o numero das IDs
	private long idCounter;
	// Chave privada do servico
	private PrivateKey privateKey;
	// Chave publica do servico
	private PublicKey publicKey;
	// public key big integer
	private final BigInteger[] publicKeyBigInt;

	public ServerServiceController(i_Service service, boolean isServer)
			throws AlreadyBoundException, IOException {
		this.service = service;
		this.server = isServer;
		this.mapVirtClients = new HashMap<Long, VirtualClient>();
		this.mapClients = new HashMap<Long, i_RemoteClient>();
		this.idCounter = 1;
		// Geracao das chaves
		KeyPair k = null;
		try {
			k = MyKeyPairGenerator.generateKeyPair();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidAlgorithmParameterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.privateKey = k.getPrivate();
		this.publicKey = k.getPublic();
		this.publicKeyBigInt = MyKeyPairGenerator.converPublicKey(this.publicKey);

	}

	// Avisa clientes de uma alteracao
	private boolean serverUpdateClients(final long id, Instruction instr) {
		// Server atualiza a si proprio
		JwallaController.getInstance().execInstruction(instr);

		// Caso o mapa tenha clientes registrados;
		if (!this.mapVirtClients.isEmpty()) {
			new MyThread(instr) {
				final Iterator<VirtualClient> i = mapVirtClients.values()
						.iterator();

				public void run() {
					// Para todos os clientes registrados..

					while (i.hasNext()) {
						VirtualClient vcx = i.next();
						// se nao for o cliente origem da instrucao
						if (vcx.getId() != id)
							// cria um novo processo que registra um instrucao..
							new MyThread(getInstruction(), vcx) {
								public void run() {
									try {
										// pois cada cliente para em momentos
										// diferentes
										System.out.println("Will update: "
												+ vc.getId());
										vc.addNewInstruction(getInstruction());
									} catch (Exception e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
								}
							}.start();
					}
				}
			}.start();
		}

		// Avisa os cliente linkados
		Iterator<i_RemoteClient> irmic = mapClients.values().iterator();
		while (irmic.hasNext()) {
			final i_RemoteClient rmic = irmic.next();
			if (rmic.getId() != id) {
				try {
					final i_Communication stub = rmic.getClientStub();
					if (stub != null)
						rmic.getClientStub().update(
								XMLPreDefined.convertInstructionToXML(instr));
				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				catch (RemoteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return false;
	}

	@Override
	// Cliente avisando que teve uma modificacao
	public String callServerUpdate(long id, String XMLInstruction) {
		if (!isServer())
			return XMLPreDefined.generateErrorXML(this.getServiceName(), 0,
					"Estacao nao servidora");

		Instruction instr = XMLPreDefined
				.convertXMLToInstruction(XMLInstruction);
		// objeto DO tem seu lock, e deve ser ativado
		final Semaphore sem = JwallaController.getInstance().getDOSemaphore(
				instr.getDOid());
		try {
			sem.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		final VirtualClient vc = this.mapVirtClients.get(id);
		if (vc != null) {
			if (vc.hasInstructionForDObject(instr.getDOid()))
				try {
					if (vc.getClientPK() != null)
						return XMLPreDefined.convertEcncryptedToXML(Crypter
								.encrypt(vc.getClientPK(),
										Constants.UPDATE_FIRST.getBytes()));
					else
						return Constants.UPDATE_FIRST;
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
		this.serverUpdateClients(id, instr);

		// Libera DObject
		sem.release();

		if (vc != null) {
			try {
				if (vc.getClientPK() != null)
					return XMLPreDefined
							.convertEcncryptedToXML(Crypter.encrypt(vc
									.getClientPK(), Constants.OK.getBytes()));
				else
					return Constants.OK;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();

			}
		}

		return Constants.OK;

	}

	// XML vindo do cliente em callRemoteUpdate
	private String getClientUpdateXML(long id, String xml) {
		if (this.mapVirtClients.get(id).getClientPK() != null)
			try {
				byte[][] x = XMLPreDefined.convertXMLToEcncrypted(xml);
				xml = Decrypter.decrypt(this.privateKey, x[0], x[1]);
			} catch (Exception e1) {
				XMLPreDefined.generateErrorXML(this.getServiceName(), 0,
						"Erro ao decriptar!");
			}

		Instruction instr = XMLPreDefined.convertXMLToInstruction(xml);
		// TODO adicionar instruction a lista e tratar
		return Constants.OK;
	}

	@Override
	// Retorna os dados que o cliente precisa atualizar
	public String getInstructionsForAVirtualClient(long clientCode) {
		// Pega cliente do mapa
		final VirtualClient rc = this.mapVirtClients.get(clientCode);
		String xmli = "";
		try {
			xmli = rc.getXMLListOfInstructions();
		} catch (InterruptedException e) {
			return XMLPreDefined.generateErrorXML(getServiceName(), 0, e
					.getMessage());
		}

		// Se cliente possuir chave
		if (rc.getClientPK() != null)
			try {
				return XMLPreDefined.convertEcncryptedToXML(Crypter.encrypt(rc
						.getClientPK(), xmli.getBytes()));
			} catch (Exception e) {
				XMLPreDefined.generateErrorXML(this.getServiceName(), 0,
						"Erro ao criptografar!");
			}
		return xmli;
	}

	// * ######### CRIACAO DE CONEXOES ######## */

	@Override
	// Cria conexao RMI
	public synchronized String createRemoteClientConnection(String xml) {
		// Cria um cliente
		i_RemoteClient rc = XMLPreDefined.getClientPropertiesObj(this.idCounter++, xml);
		// Interpreta o xml vindo do cliente << XML nao criptografado >>
		String ret = XMLPreDefined.getClientProperties(getServiceName(),(i_JwallaClient) rc, xml);
		
		i_RemoteClient rmic = (i_RemoteClient) rc;
		if (!ret.equals(Constants.OK))
			return ret; // Caso nao estiver OK, retorna string com xml
		// descrevendo o erro;
		if ( rmic.connectToMe(this.getService())) {
			System.out.println("Client connected! " + this.getServiceName());
			// Cliente OK, poe no mapa
			this.mapClients.put(rmic.getId(), rmic);

			return XMLPreDefined.generateRemoteClientConnectXML(this
					.getServiceName(), rmic.getId());
		} else
			return XMLPreDefined.generateErrorXML(this.getServiceName(), 0,
					"Nao foi possivel estabelecer conexao com o cliente");

	}

	@Override
	public synchronized String createVirtualConnectionClient(String xml) {
		// Cria um cliente
		VirtualClient vrc = null;
		// Interpreta o xml vindo do cliente << XML nao criptografado >>
		String ret = XMLPreDefined.getClientProperties(getServiceName(),
				(i_JwallaClient) vrc, xml);
		if (!ret.equals(Constants.OK))
			return ret; // Caso nao estiver OK, retorna string com xml
		// descrevendo o erro;

		// Cliente OK, poe no mapa
		this.mapVirtClients.put(vrc.getId(), vrc);
		// Retorna a chave publica se o cliente se conectou de modo seguro
		final boolean withPK = vrc.getClientPK() != null ? true : false;
		return XMLPreDefined.generateVirtualClientConnectXML(this
				.getServiceName(), vrc.getId(), withPK, this.publicKeyBigInt);

	}

	public String getServiceName() {
		return this.service.getName();
	}

	public PublicKey getPublicKey() {
		return publicKey;
	}

	public BigInteger[] getPublicKeyBigInt() {
		return publicKeyBigInt;
	}

	public boolean isServer() {
		return server;
	}

	public void setServer(boolean server) {
		this.server = server;
	}

	// Classe privada responsavel por destrinchar o cadastro de instrucoes
	private class MyThread extends Thread {
		Instruction instruction;
		VirtualClient vc;

		public MyThread(Instruction i) {
			this.instruction = i;
		}

		public MyThread(Instruction i, VirtualClient vc) {
			this.instruction = i;
			this.vc = vc;
		}

		public VirtualClient getVirtualClient() {
			return vc;
		}

		public Instruction getInstruction() {
			return instruction;
		}
	}

	@Override
	public boolean update(String XMLComands) {
		Instruction instr = XMLPreDefined.convertXMLToInstruction(XMLComands);
		System.out.println("Door: " + this.service.getDoor() + " Method:"
				+ instr.getGenericMethodString());
		return true;
	}

	@Override
	public String instanceSync() throws IllegalArgumentException,
			RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

	public i_Service getService() {
		return service;
	}
}

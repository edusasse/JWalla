package com.edusasse.jwalla.client.clients;

import org.omg.CORBA.ORB;
import org.omg.CORBA.ORBPackage.InvalidName;
import org.omg.CosNaming.NamingContextExt;
import org.omg.CosNaming.NamingContextExtHelper;
import org.omg.CosNaming.NamingContextPackage.CannotProceed;
import org.omg.CosNaming.NamingContextPackage.NotFound;

import com.edusasse.jwalla.client.service.i_Service;
import com.edusasse.jwalla.engine.Constants;
import com.edusasse.jwalla.rmi.applications.i_Communication;
import com.edusasse.jwalla.server.corba.ServerHelper;

public class CorbaClient implements i_RemoteClient, i_JwallaClient {
	// Id - identificacao do usuario no servico -
	private long id;
	// IP
	private String ip;
	// Porta
	private int door;
	// Maior sessao a qual o cliente faz parte atualmente
	private long maxSession;
	// Registro no servidor RMI do CLiente
	private i_Communication clientStub = null;
	
	public CorbaClient(long id) {
		this.id = id;
		this.maxSession = Constants.INITIAL_SESSION;
	}
	@Override
	public boolean connectToMe(i_Service s) {
		final String[] myArgs = new String[] { "–ORBInitialPort", "2000","–ORBInitialHost", s.getAddress()};
		try {
			// Cria e inicializa o ORB
			ORB orb = ORB.init(myArgs, null);

			// Obtem referencia para o servico de nomes
			org.omg.CORBA.Object objRef = orb.resolve_initial_references("NameService");
			NamingContextExt namecontextRef = NamingContextExtHelper.narrow(objRef);
 
			this.clientStub = ServerHelper.narrow(namecontextRef.resolve_str(s.getName()));
			System.out.println("Connected " + s.getName() + " at " + s.getAddress() + ":" + s.getDoor() );
		} catch (InvalidName e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NotFound e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CannotProceed e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (org.omg.CosNaming.NamingContextPackage.InvalidName e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;
	}

	@Override
	public i_Communication getClientStub() {
		if (this.clientStub == null)
			throw new NullPointerException("Stub nao iniciado!");
		return this.clientStub;
	}


	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	
	public int getDoor() {
		return door;
	}
	
	public void setDoor(int door) {
		this.door = door;
	}

	public long getMaxSession() {
		return maxSession;
	}

	public void setMaxSession(long maxSession) {
		this.maxSession = maxSession;
	}

	
	public long getId() {
		return id;
	}
	

	@Override
	public byte getKind() {
		return CORBA_CLIENT;
	}

}

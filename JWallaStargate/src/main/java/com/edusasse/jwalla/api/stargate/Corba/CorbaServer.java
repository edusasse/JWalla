package com.edusasse.jwalla.api.stargate.Corba;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import org.omg.CORBA.ORB;
import org.omg.CosNaming.NameComponent;
import org.omg.CosNaming.NamingContextExt;
import org.omg.CosNaming.NamingContextExtHelper;
import org.omg.PortableServer.POA;
import org.omg.PortableServer.POAHelper;

import com.edusasse.jwalla.client.clients.i_JwallaClient;
import com.edusasse.jwalla.client.service.i_Service;
import com.edusasse.jwalla.server.i_Server;
import com.edusasse.jwalla.server.corba.Server;
import com.edusasse.jwalla.server.corba.ServerHelper;
import com.edusasse.jwalla.server.corba.ServerPOA;
import com.edusasse.jwalla.util.CorbaUtils;
import com.edusasse.jwalla.util.XMLPreDefined;

public class CorbaServer extends ServerPOA  {
	private i_Server rmisrv;
	private i_Service serviceSG;
	private i_Service service;
	
	
	public CorbaServer(i_Service s1, i_Service service){
		this.service = s1;
		this.serviceSG = service;
		this.rmisrv = rmisrv;
		try {
		
		// Duto de conexao
		Registry registry2 = LocateRegistry.getRegistry(s1.getAddress(), s1.getDoor());
		
		// Pega a connexao com o server
		this.rmisrv = (i_Server) registry2.lookup(s1.getName());
		

		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NotBoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.startServer();
	}
	
	private final boolean isCorbaRunning() throws IOException {
		boolean isWindows = true;
		if (isWindows) {
			final String myReturn = CorbaUtils.convertStreamToString(Runtime
					.getRuntime().exec("TASKLIST /SVC").getInputStream());
			if (myReturn.toLowerCase().contains("orbd.exe"))
				return true;
		} else {
			// TODO linux
		}
		return false;
	}

	private final void startServer() {
		try {
			if (!isCorbaRunning())
				Runtime.getRuntime().exec("orbd -ORBInitialPort 900");

		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			// Cria e inicializa o ORB
			final ORB orb = ORB.init(new String[]{}, null);

			// Ativa o POA
			POA rootpoa = POAHelper.narrow(orb.resolve_initial_references("RootPOA"));
			rootpoa.the_POAManager().activate();

			// Pega a referência do servidor
			org.omg.CORBA.Object ref = rootpoa.servant_to_reference(this);
			Server href = ServerHelper.narrow(ref);

			// Obtém uma referência para o servidor de nomes
			org.omg.CORBA.Object objRef = orb.resolve_initial_references("NameService");
			NamingContextExt namecontextRef = NamingContextExtHelper.narrow(objRef);

			// Registra o servidor no servico de nomes
			NameComponent path[] = namecontextRef.to_name(this.getServiceName());
			namecontextRef.rebind(path, href);
			System.out.println("Started...");
			// Aguarda chamadas dos clientes
			new Thread() {
				public void run() {
					orb.run();					
				}
			};
		} catch (Exception e) {
			System.err.println("Error: " + e);
			e.printStackTrace(System.out);
		} finally {

		}

	}

	@Override
	public String callServerUpdate(long id, String XMLComands) {
		
		try {
			return this.rmisrv.callServerUpdate(id, XMLComands);
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public String createRemoteClientConnection(String xml) {
		// ** Solicitacao de conexai			
		// Gera soicitacao de Conexexao
		String xmlCliConn = XMLPreDefined.generateRemoteClientConnectXML(this.service.getName(), "10.1.1.48", this.service.getDoor(), i_JwallaClient.RMI_CLIENT);
		// Evia solicitacao
		try {
			return this.rmisrv.createRemoteClientConnection(xmlCliConn);
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
		
	}

	@Override
	public String getServiceName() {
		return this.serviceSG.getName();
	}

	@Override
	public String instanceSync() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean update(String XMLComands) {
		try {
			return this.rmisrv.update(XMLComands);
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	} 
}

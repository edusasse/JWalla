package com.edusasse.sample.calc.engine;

import java.io.IOException;
import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;

import org.omg.CORBA.ORB;
import org.omg.CORBA.ORBPackage.InvalidName;
import org.omg.CosNaming.NamingContextExt;
import org.omg.CosNaming.NamingContextExtHelper;
import org.omg.CosNaming.NamingContextPackage.CannotProceed;
import org.omg.CosNaming.NamingContextPackage.NotFound;

import com.edusasse.jwalla.client.service.Service;
import com.edusasse.jwalla.server.ServerServiceController;
import com.edusasse.jwalla.server.i_Server;
import com.edusasse.jwalla.server.corba.ServerHelper;

public class Main {

	/**
	 * @param args
	 */

	public Main() {
		this.criaAmbiente();
	}
	
	public void criaAmbiente() {
		try {
		 
			Service s1 = new Service("Test","Hello World!", "v1.0", "127.0.0.1",1024);
						
			//- ---- - -- ------- - -- -- ------ -- ---- -
			// RMI
			//- ---- - -- ------- - -- -- ------ -- ---- -
			/*
			// Duto de conexao
			Registry registry2 = LocateRegistry.getRegistry(s1.getAddress(), s1.getDoor());
			// Pega a connexao com o server
			i_Server stubRMICliServer = (i_Server) registry2.lookup(s1.getName());
			
			// Controladora cliente. Cria server do tipo cliente local
			RMIServer rmicli = new RMIServer(new ServerServiceController(s1, false));

			// ** Solicitacao de conexai			
			// Gera soicitacao de Conexexao
			String xmlCliConn = XMLPreDefined.generateRemoteClientConnectXML(s1.getName(), "127.0.0.1", s1.getDoor(), i_JwallaClient.RMI_CLIENT);
			// Evia solicitacao
			String xmlRCC = stubRMICliServer.createRemoteClientConnection(xmlCliConn);
			// Desmonta XML recebido
			final long id = XMLPreDefined.getRemoteClientConnectXMLId(s1.getName(),xmlRCC);
			// Exibe ID
			System.out.println(">> RMI: " + id);
			

			//- ---- - -- ------- - -- -- ------ -- ---- -
			// CORBA
			//- ---- - -- ------- - -- -- ------ -- ---- -
*/
			ServerServiceController ssc2 = new ServerServiceController(s1,false);
			
			//	CORBAServer corbacli = new CORBAServer(ssc2);
			
			 final String[] myArgs = new String[]{"–ORBInitialHost", "127.0.0.1"};
			 // Cria e inicializa o ORB
			 ORB orb = ORB.init(myArgs, null);
			 // Obtem referencia para o servico de nomes
			 org.omg.CORBA.Object objRef = orb.resolve_initial_references("NameService");
			 NamingContextExt namecontextRef = NamingContextExtHelper.narrow(objRef);
			 i_Server stubCorbaServer = ServerHelper.narrow(namecontextRef.resolve_str(s1.getName()));
			 System.out.println(stubCorbaServer.getServiceName());
			// ** Solicitacao de conexai			
			// Gera soicitacao de Conexexao
			/*String xmlCliConn2 = XMLPreDefined.generateRemoteClientConnectXML(s1.getName(), "127.0.0.1", s1.getDoor(), i_JwallaClient.CORBA_CLIENT);
			// Evia solicitacao
			String xmlRCC2 = stubCorbaServer.createRemoteClientConnection(xmlCliConn2);
			// Desmonta XML recebido
			final long id2 = XMLPreDefined.getRemoteClientConnectXMLId(s1.getName(),xmlRCC2);
			// Exibe ID
			System.out.println(">> CORBA: " + id2);
			*/
			 /*
			
			//- ---- - -- ------- - -- -- ------ -- ---- -
			// STARGATE - Corba
			//- ---- - -- ------- - -- -- ------ -- ---- -
			Service sg1 = new Service("SG:Test","Hello World!", "v1.0", "127.0.0.1",2000);
			sg1 = s1;
			ServerServiceController sscsg1 = new ServerServiceController(sg1,false);
			
			CORBAServer corbacliSG1 = new CORBAServer(sscsg1);
			
			final String[] myArgsSG1 = new String[]{"–ORBInitialPort","2000", "–ORBInitialHost", "localhost"};
			// Cria e inicializa o ORB
			ORB orb2 = ORB.init(myArgsSG1, null);
			// Obtem referencia para o servico de nomes
			org.omg.CORBA.Object objRefSG1 = orb2.resolve_initial_references("NameService");
			NamingContextExt namecontextRefSG1 = NamingContextExtHelper.narrow(objRefSG1);
			i_Server stubCorbaServerSG1 = ServerHelper.narrow(namecontextRefSG1.resolve_str(sg1.getName()));
				
			// ** Solicitacao de conexai			
			// Gera soicitacao de Conexexao
			String xmlCliConn3 = XMLPreDefined.generateRemoteClientConnectXML(sg1.getName(), "127.0.0.1", sg1.getDoor(), i_JwallaClient.CORBA_CLIENT);
			// Evia solicitacao
			String xmlRCCSG1 = stubCorbaServerSG1.createRemoteClientConnection(xmlCliConn3);
			// Desmonta XML recebido
			final long idSG1 = XMLPreDefined.getRemoteClientConnectXMLId(sg1.getName(),xmlRCCSG1);
			// Exibe ID
			System.out.println(">> Stargate[CORBA]: " + idSG1);
			
			
			*/
		} catch (RemoteException Re) {
			System.out.println(Re.getCause());
			System.out.println(Re.getMessage());
			Re.printStackTrace();
		} catch (AlreadyBoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
	}
	
	public static void main(String[] args) {
		new Main();

	}

}

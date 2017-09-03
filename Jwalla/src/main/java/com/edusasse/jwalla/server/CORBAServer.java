package com.edusasse.jwalla.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.omg.CORBA.ORB;
import org.omg.CosNaming.NameComponent;
import org.omg.CosNaming.NamingContextExt;
import org.omg.CosNaming.NamingContextExtHelper;
import org.omg.PortableServer.POA;
import org.omg.PortableServer.POAHelper;

import com.edusasse.jwalla.server.corba.Server;
import com.edusasse.jwalla.server.corba.ServerHelper;
import com.edusasse.jwalla.server.corba.ServerPOA;
import com.edusasse.jwalla.util.CorbaUtils;

public class CORBAServer extends ServerPOA implements i_Server {
	private ServerServiceController ssc;

	public CORBAServer(ServerServiceController ssc) {
		this.ssc = ssc;
		// Inicia o servidor
		startServer();

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
			final ORB orb = ORB.init(
					new String[] { "–ORBInitialPort", "2000" }, null);

			// Ativa o POA
			POA rootpoa = POAHelper.narrow(orb
					.resolve_initial_references("RootPOA"));
			rootpoa.the_POAManager().activate();

			// Pega a referência do servidor
			org.omg.CORBA.Object ref = rootpoa.servant_to_reference(this);
			Server href = ServerHelper.narrow(ref);

			// Obtém uma referência para o servidor de nomes
			org.omg.CORBA.Object objRef = orb
					.resolve_initial_references("NameService");
			NamingContextExt namecontextRef = NamingContextExtHelper
					.narrow(objRef);

			// Registra o servidor no servico de nomes
			NameComponent path[] = namecontextRef
					.to_name(this.getServiceName());
			namecontextRef.rebind(path, href);
			System.out.println("CORBA Started...");
			// Aguarda chamadas dos clientes
			new Thread() {
				public void run() {					 
					orb.run();					 
				}
			}.start();
			
		} catch (Exception e) {
			System.err.println("ERRO: " + e.getMessage());
		}

	}

	@Override
	public String callServerUpdate(long id, String XMLComands) {
		return ssc.callServerUpdate(id, XMLComands);
	}

	@Override
	public String getServiceName() {
		return ssc.getServiceName();
	}

	@Override
	public String instanceSync() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean update(String XMLComands) {
		if (this.ssc.isServer())
			return false;

		return this.ssc.update(XMLComands);
	}

	@Override
	public String createRemoteClientConnection(String xml) {
		return this.ssc.createRemoteClientConnection(xml);
	}

}

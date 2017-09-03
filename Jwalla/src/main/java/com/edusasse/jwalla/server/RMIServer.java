package com.edusasse.jwalla.server;

import java.io.IOException;
import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class RMIServer implements i_Server {
private ServerServiceController ssc;
private i_Server corba;
	
	public RMIServer(ServerServiceController ssc, i_Server corba) 
			throws AlreadyBoundException, IOException {
		this.ssc = ssc;
		this.corba = corba;

		// Inicia o servidor
		this.startServer();
	}

	public final void startServer() throws AlreadyBoundException, IOException {
		try {
			// Exporta o objeto remoto segunto interface
			i_Server stub = (i_Server) UnicastRemoteObject.exportObject(this, 0);

			// Liga o stub do objeto remoto no registro, e inicia o RMIRegistry
			// na porta 1000
			Registry registry = LocateRegistry.createRegistry(ssc.getService().getDoor());

			// Dá um nome pra ele no registro
			registry.bind(this.getServiceName(), stub);

		} catch (RemoteException Re) {
			throw Re;
		} catch (AlreadyBoundException ABe) {
			throw ABe;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public String callServerUpdate(long id, String XMLComands) {
		try {
			return corba.callServerUpdate(id, XMLComands);
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return XMLComands;
	}

	@Override
	public String getServiceName() {
		try {
			return corba.getServiceName();
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
	public String instanceSync() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean update(String XMLComands) throws IllegalArgumentException,RemoteException {
		if (this.ssc.isServer())
			return false;
		
		return this.corba.update(XMLComands);
	}

	@Override
	public String createRemoteClientConnection(String xml)
			throws IllegalArgumentException, RemoteException {
		return this.corba.createRemoteClientConnection(xml);
	}

 

}

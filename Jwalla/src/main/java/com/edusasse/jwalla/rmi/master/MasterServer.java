package com.edusasse.jwalla.rmi.master;

import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import java.rmi.RemoteException;
import java.rmi.AlreadyBoundException;
import java.rmi.server.UnicastRemoteObject;

import com.edusasse.jwalla.client.service.Service;
import com.edusasse.jwalla.client.service.i_Service;

import java.io.IOException;

public class MasterServer implements i_Master {

	public MasterServer() throws AlreadyBoundException, IOException {
		startServer();
	}

	public void startServer() throws AlreadyBoundException, IOException {
		try {
			// Exporta o objeto remoto segunto interface 
			i_Master stub = (i_Master) UnicastRemoteObject.exportObject(this, 0);

			// Liga o stub do objeto remoto no registro, e inicia o RMIRegistry na porta 1000
			Registry registry = LocateRegistry.createRegistry(1010);

			// Dá um nome pra ele no registro
			registry.bind("RMI Master Server", stub);
			
		} catch (RemoteException Re) {
			throw Re;
		} catch (AlreadyBoundException ABe) {
			throw ABe;
		} catch (IOException IOe) {
			throw IOe;
		} catch (Exception e){
			e.printStackTrace();
		}
	}

	@Override
	public String getServices() {
		return Controller.getInstance().getServices();
	}

	@Override
	public void registerService(Service s) {
		Controller.getInstance().addService(s);
	}

	@Override
	public void unregisterSerice(i_Service s)  {
		Controller.getInstance().removeService(s);
	}
 
	 

}
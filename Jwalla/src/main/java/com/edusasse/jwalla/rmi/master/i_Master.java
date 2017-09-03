package com.edusasse.jwalla.rmi.master;
import java.rmi.Remote;
import java.rmi.RemoteException;

import com.edusasse.jwalla.client.service.Service;
import com.edusasse.jwalla.client.service.i_Service;

public interface i_Master extends Remote {

	// Registra a aplicação no RMI Mestra; o retorno é um valor booleano que indica se o serviço foi aceito
	public void registerService(Service s) throws IllegalArgumentException, RemoteException;
	// Remove o servico
	public void unregisterSerice(i_Service s) throws IllegalArgumentException, RemoteException;
	// Retorna objeto contendo os serviços;
	public String getServices() throws RemoteException;
	
}

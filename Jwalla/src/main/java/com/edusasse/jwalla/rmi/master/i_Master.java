package com.edusasse.jwalla.rmi.master;
import java.rmi.Remote;
import java.rmi.RemoteException;

import com.edusasse.jwalla.client.service.Service;
import com.edusasse.jwalla.client.service.i_Service;

public interface i_Master extends Remote {

	// Registra a aplica��o no RMI Mestra; o retorno � um valor booleano que indica se o servi�o foi aceito
	public void registerService(Service s) throws IllegalArgumentException, RemoteException;
	// Remove o servico
	public void unregisterSerice(i_Service s) throws IllegalArgumentException, RemoteException;
	// Retorna objeto contendo os servi�os;
	public String getServices() throws RemoteException;
	
}

package com.edusasse.jwalla.rmi.applications;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface i_Communication extends Remote {
	// Indica a altera��o de uma propriedade no sistema do cliente, passando a sess�o e o comando; o retorno � um valor booleano que indica se a altera��o foi aceita
	public boolean update(String XMLComands)throws IllegalArgumentException, RemoteException;
	

}

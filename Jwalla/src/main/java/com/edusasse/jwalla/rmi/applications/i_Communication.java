package com.edusasse.jwalla.rmi.applications;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface i_Communication extends Remote {
	// Indica a alteração de uma propriedade no sistema do cliente, passando a sessão e o comando; o retorno é um valor booleano que indica se a alteração foi aceita
	public boolean update(String XMLComands)throws IllegalArgumentException, RemoteException;
	

}

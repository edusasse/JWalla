package com.edusasse.jwalla.server;

import java.rmi.RemoteException;

import com.edusasse.jwalla.rmi.applications.i_Communication;

public interface i_Server extends i_Communication {
	public String createRemoteClientConnection(String xml)throws IllegalArgumentException, RemoteException;
	public String callServerUpdate(long id, String XMLInstruction)throws IllegalArgumentException, RemoteException;
	public String getServiceName() throws IllegalArgumentException, RemoteException;
	public String instanceSync() throws IllegalArgumentException, RemoteException;

}

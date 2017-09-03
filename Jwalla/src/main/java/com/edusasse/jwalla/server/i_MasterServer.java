package com.edusasse.jwalla.server;

import java.rmi.RemoteException;

public interface i_MasterServer extends i_Server {
	public String createVirtualConnectionClient(String xml)throws IllegalArgumentException, RemoteException;
	public String getInstructionsForAVirtualClient(long clientCode)throws IllegalArgumentException, RemoteException;

}

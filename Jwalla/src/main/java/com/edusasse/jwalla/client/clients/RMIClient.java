package com.edusasse.jwalla.client.clients;

import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import com.edusasse.jwalla.client.service.i_Service;
import com.edusasse.jwalla.engine.Constants;
import com.edusasse.jwalla.rmi.applications.i_Communication;

public class RMIClient implements i_RemoteClient,i_JwallaClient, Remote {
	// Id - identificacao do usuario no servico -
	private long id;
	// IP
	private String ip;
	// Porta
	private int door;
	// Maior sessao a qual o cliente faz parte atualmente
	private long maxSession;
	// Registro no servidor RMI do CLiente
	private i_Communication clientStub = null;
	

	public RMIClient(long id) {
		this.id = id;
		this.maxSession = Constants.INITIAL_SESSION;
	}

	public boolean connectToMe(i_Service s) {
		try {
			final Registry registry = LocateRegistry.getRegistry(s.getAddress(), s.getDoor());
			this.clientStub = (i_Communication) registry.lookup(s.getName());
		} catch (AccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NotBoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;
	}

	@Override
	public i_Communication getClientStub() {
		
		return this.clientStub;
	}
	
	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	
	public int getDoor() {
		return door;
	}
	
	public void setDoor(int door) {
		this.door = door;
	}

	public long getMaxSession() {
		return maxSession;
	}

	public void setMaxSession(long maxSession) {
		this.maxSession = maxSession;
	}

	
	public long getId() {
		return id;
	}
	
	@Override
	public byte getKind() {
		return RMI_CLIENT;
	}

}

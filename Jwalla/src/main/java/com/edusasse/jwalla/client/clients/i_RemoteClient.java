package com.edusasse.jwalla.client.clients;

import com.edusasse.jwalla.client.service.i_Service;
import com.edusasse.jwalla.rmi.applications.i_Communication;

public interface i_RemoteClient {
	public boolean connectToMe(i_Service s);
	public String getIp();
	public void setIp(String ip);
	public int getDoor();
	public void setDoor(int door);
	public long getMaxSession();
	public void setMaxSession(long maxSession);
	public long getId();
	public i_Communication getClientStub();

}

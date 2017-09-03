package com.edusasse.jwalla.client.clients;

public interface i_JwallaClient {
	public final byte RMI_CLIENT =0;
	public final byte CORBA_CLIENT =1;
	public final byte VIRTUAL_CLIENT =2;
	
	public byte getKind();
}

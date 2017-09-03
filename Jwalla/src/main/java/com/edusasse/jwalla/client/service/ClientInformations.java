package com.edusasse.jwalla.client.service;

import java.math.BigInteger;
import java.security.PublicKey;

import com.edusasse.jwalla.security.MyKeyPairGenerator;

public class ClientInformations {
	private String serviceName;
	private long id;
	private PublicKey pk;
	
	public ClientInformations(String serviceName, long id, BigInteger[] pkBigInt){
		this.serviceName = serviceName;
		this.id = id;
		if (pkBigInt == null)
			this.pk = null;
		else 
			this.pk = MyKeyPairGenerator.readKeyFromFile(pkBigInt[0], pkBigInt[1]);
	}
	
	public long getId() {
		return id;
	}
	public String getServiceName() {
		return serviceName;
	}
	public PublicKey getPk() {
		return pk;
	}
}

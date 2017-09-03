package com.edusasse.jwalla.security;

import java.math.BigInteger;
import java.security.InvalidAlgorithmParameterException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAKeyGenParameterSpec;
import java.security.spec.RSAPublicKeySpec;

public class MyKeyPairGenerator {
	private static final int RSAKEYSIZE = 512;

	public static KeyPair generateKeyPair() throws NoSuchAlgorithmException,
			InvalidAlgorithmParameterException {

		KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
		kpg.initialize(new RSAKeyGenParameterSpec(RSAKEYSIZE,
				RSAKeyGenParameterSpec.F4));
		KeyPair kpr = kpg.generateKeyPair();
		return kpr;

	}
	

	public static PublicKey readKeyFromFile(BigInteger bi, BigInteger bi2){

		try {

			RSAPublicKeySpec keySpec = new RSAPublicKeySpec(bi, bi2);
			KeyFactory fact = KeyFactory.getInstance("RSA");
			PublicKey pubKey = fact.generatePublic(keySpec);
			return pubKey;
		} catch (Exception e) {
			throw new RuntimeException("Spurious serialisation error", e);
		}
	}
	
	public static BigInteger[] converPublicKey(PublicKey pk){
		KeyFactory fact = null;
		try {
			fact = KeyFactory.getInstance("RSA");
			
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		RSAPublicKeySpec pub = null;
		try {
			pub = fact.getKeySpec(pk, RSAPublicKeySpec.class);
		} catch (InvalidKeySpecException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return new BigInteger[]{pub.getModulus(),pub.getPublicExponent()};
	}
}
package com.edusasse.jwalla.security;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class Crypter {
	   public static byte[][] encrypt(PublicKey pub, byte[] textoClaro) throws NoSuchAlgorithmException, 
	    NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException,
	    BadPaddingException, InvalidAlgorithmParameterException {
	        byte[] text = null;
	        byte[] key = null;
	        
	        //-- A) Gerando uma chave simétrica de 128 bits
	        KeyGenerator kg = KeyGenerator.getInstance ("AES");
	        kg.init (128);
	        SecretKey sk = kg.generateKey ();
	        byte[] chave = sk.getEncoded();
	        //-- B) Cifrando o texto com a chave simétrica gerada
	        Cipher aescf = Cipher.getInstance ("AES/CBC/PKCS5Padding");
	        IvParameterSpec ivspec = new IvParameterSpec (new byte[16]);
	        aescf.init (Cipher.ENCRYPT_MODE, new SecretKeySpec (chave, "AES"), ivspec);
	        text = aescf.doFinal (textoClaro);
	        //-- C) Cifrando a chave com a chave pública
	        Cipher rsacf = Cipher.getInstance ("RSA");
	        rsacf.init (Cipher.ENCRYPT_MODE, pub);
	        key = rsacf.doFinal(chave);
	        
	        return new byte[][] { text, key };
	    }
}
package com.edusasse.jwalla.client.service;

public class Instruction {
	// Id do objeto distribuido
	private long DOid;
	// Metodo
	private String genericMethodString;
	// Parametros
	private Object[] args;
	
	public Instruction(){		
	}
	
	public Instruction(int DOid, String genericMethodString, Object[] args){
		this.DOid = DOid;
		this.genericMethodString = genericMethodString;
		this.args = args;
	}

	public long getDOid() {
		return DOid;
	}

	public void setDOid(long oid) {
		DOid = oid;
	}

	public String getGenericMethodString() {
		return genericMethodString;
	}

	public void setGenericMethodString(String genericMethodString) {
		this.genericMethodString = genericMethodString;
	}

	public Object[] getArgs() {
		return args;
	}

	public void setArgs(Object[] args) {
		this.args = args;
	}
	
	
	
}

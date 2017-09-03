package com.edusasse.jwalla.engine;

import java.util.HashMap;
import java.util.concurrent.Semaphore;

import com.edusasse.jwalla.client.service.Instruction;

public class JwallaController {
	private static JwallaController jc = null;
	private HashMap<Long, JwallaDObject> domap = null;
	private HashMap<Long, Semaphore> dosemmap = null;
	
	
	private JwallaController(){
		this.domap = new HashMap<Long, JwallaDObject>();
		this.dosemmap = new HashMap<Long, Semaphore>();
		this.createDObject();
	}
	
	public static JwallaController getInstance(){
		if (jc == null)
			jc = new JwallaController();
		
		return jc;
	}
	
	public Semaphore getDOSemaphore(long DOid){
		return (this.dosemmap.get(DOid));
	}
	
	public JwallaDObject getJwallaDObject(long DOid){
		return (this.domap.get(DOid));
	}

	public void execInstruction(Instruction instruction) {
		// TODO Auto-generated method stub
		System.out.println("Server LOCAL: " + instruction.getDOid() + ": " +  instruction.getGenericMethodString());
		
	}
	
	public synchronized void createDObject(){
		JwallaDObject jdo = new JwallaDObject(){};
		this.domap.put((long) 99,jdo);
		this.dosemmap.put((long) 99,new Semaphore(1));
	}
	
}

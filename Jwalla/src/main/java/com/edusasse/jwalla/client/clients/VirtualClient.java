package com.edusasse.jwalla.client.clients;

import java.io.IOException;
import java.io.StringWriter;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.concurrent.Semaphore;

import org.jdom.Attribute;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.XMLOutputter;

import com.edusasse.jwalla.client.service.Instruction;
import com.edusasse.jwalla.engine.Constants;
import com.edusasse.jwalla.util.XMLPreDefined;

public class VirtualClient implements i_JwallaClient {
	// Id - identificacao do usuario no servico -
	private long id;
	// IP
	private String ip;
	// Porta
	private int door;
	// Semaforo para controle de leitura e escrita das instrucoes
	private Semaphore sem;
	// Lista das instrucoes
	private ArrayList<Instruction> listOfInstructions;
	// Chave publica enviada pelo Cliente
	private PublicKey clientPK;
	// Maior sessao a qual o cliente faz parte atualmente
	private long maxSession;
	//
	
	public VirtualClient(long id) {
		this.id = id;
		this.clientPK = null;
		this.sem = new Semaphore(1);
		this.listOfInstructions = new ArrayList<Instruction>();
		this.maxSession = Constants.INITIAL_SESSION;
	}

	// Pega a lista de
	public String getXMLListOfInstructions() throws InterruptedException {
		this.sem.acquire();
		// Caso a lista esteja vazia nao faz nada e retorna string em branco
		if (this.listOfInstructions.isEmpty()) {
			this.sem.release();
			return "";
		}
		StringWriter sw = new StringWriter();
		Element instructions = new Element("Instructions");

		for (Instruction sl : this.listOfInstructions) {
			Element instruction = new Element("Instruction");

			Attribute atribDOid = new Attribute("DOid", String.valueOf(sl
					.getDOid()));
			Attribute atribMethod = new Attribute("method", sl
					.getGenericMethodString());
			Attribute atribParamters = new Attribute("XMLargs", XMLPreDefined.convertArgsToXML(sl.getArgs()));
			instruction.setAttribute(atribDOid);
			instruction.setAttribute(atribMethod);
			instruction.setAttribute(atribParamters);

			instructions.addContent(instruction);
		}
		// Limpa a lista de instrucoes
		this.listOfInstructions.clear();
		// Libera o acesso
		this.sem.release();

		Document doc = new Document();
		doc.setRootElement(instructions);
		XMLOutputter xout = new XMLOutputter();
		try {
			xout.output(doc, sw);
		} catch (IOException e) {
			;
		}
		return sw.toString();
		
	}

	public boolean hasInstructionForDObject(long DOid){
		for ( Instruction li:  this.listOfInstructions){
			if (li.getDOid() == DOid)
				return true;
		}
		return false;
	}
	
	// Adiciona uma nova instrucao
	public void addNewInstruction(Instruction i) throws InterruptedException {
		this.sem.acquire();
		this.listOfInstructions.add(i);
		this.sem.release();
	}

	public void setClientPK(PublicKey clientPK) {
		this.clientPK = clientPK;
	}

	public PublicKey getClientPK() {
		return clientPK;
	}

	public void setDoor(int door) {
		this.door = door;
	}

	public int getDoor() {
		return door;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}
	
	public String getIp() {
		return ip;
	}

	public long getId() {
		return id;
	}

	@Override
	public byte getKind() {
		return VIRTUAL_CLIENT;
	}

}

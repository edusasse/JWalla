package com.edusasse.jwalla.util;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.PublicKey;

import org.jdom.Attribute;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.XMLOutputter;

import com.edusasse.jwalla.client.clients.CorbaClient;
import com.edusasse.jwalla.client.clients.RMIClient;
import com.edusasse.jwalla.client.clients.VirtualClient;
import com.edusasse.jwalla.client.clients.i_JwallaClient;
import com.edusasse.jwalla.client.clients.i_RemoteClient;
import com.edusasse.jwalla.client.service.ClientInformations;
import com.edusasse.jwalla.client.service.Instruction;
import com.edusasse.jwalla.engine.Constants;
import com.edusasse.jwalla.security.MyKeyPairGenerator;

public final class XMLPreDefined {

	public static String generateErrorXML(String service, int errCode,
			String errorMessage) {
		StringWriter sw = new StringWriter();

		Element error = new Element("Error");

		Attribute atribServiceName = new Attribute("Service", service);
		Attribute atribErrorCode = new Attribute("Code", String
				.valueOf(errCode));
		Attribute atribMessage = new Attribute("Message", errorMessage);
		error.setAttribute(atribErrorCode);
		error.setAttribute(atribMessage);

		Document doc = new Document();
		doc.setRootElement(error);
		XMLOutputter xout = new XMLOutputter();
		try {
			xout.output(doc, sw);
		} catch (IOException e) {
			;
		}

		return sw.toString();
	}

	// Converte os parametros do metudo em XML utilizando XMLEncoder
	public static String convertEcncryptedToXML(byte[][] encr) {
		ByteArrayOutputStream out = new ByteArrayOutputStream();

		XMLEncoder e = new XMLEncoder(out);

		e.writeObject(encr);
		e.close();
		try {
			return (new String(out.toByteArray(), "ISO-8859-1"));
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return "";
	}

	// Converte os parametros do metudo em XML utilizando XMLEncoder
	public static byte[][] convertXMLToEcncrypted(String xml) {
		byte[][] my = null;
		xml = xml.trim();
		XMLDecoder decoder = new XMLDecoder(new ByteArrayInputStream(xml
				.getBytes()));
		my = (byte[][]) decoder.readObject();
		return my;

	}

	// Converte os parametros do metudo em XML utilizando XMLEncoder
	public static String convertArgsToXML(Object[] o) {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		XMLEncoder e = new XMLEncoder(out);
		e.writeObject(o);
		e.close();
		return out.toString();
	}

	// Converte os parametros do metudo em XML utilizando XMLEncoder
	public static Object[] convertXMLToArgs(String xml) {
		Object[] args = null;

		XMLDecoder decoder = new XMLDecoder(new ByteArrayInputStream(xml
				.getBytes()));
		args = (Object[]) decoder.readObject();
		return args;

	}

	// XML vindo do cliente em callRemoteUpdate
	public static Instruction convertXMLToInstruction(String xml) {
		// Criamos uma classe SAXBuilder que vai processar o XML
		SAXBuilder sb = new SAXBuilder();

		// Este documento agora possui toda a estrutura do arquivo.
		Document d = null;
		try {
			d = sb.build(new StringReader(xml));
		} catch (JDOMException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Instruction instr = new Instruction();
		// Recuperamos o elemento root
		Element client = d.getRootElement();

		// Recuperacao apenas nos nao virtuais
		instr.setDOid(Long.parseLong(client.getAttributeValue("DOid")));
		instr.setGenericMethodString(client.getAttributeValue("method"));
		instr.setArgs(XMLPreDefined.convertXMLToArgs(client
				.getAttributeValue("XMLargs")));

		return instr;
	}

	// XML vindo do cliente em callRemoteUpdate
	public static String convertInstructionToXML(Instruction instr) {
		StringWriter sw = new StringWriter();

		Element server = new Element("Instruction");

		Attribute atribDOid = new Attribute("DOid", String.valueOf(instr
				.getDOid()));
		Attribute atribMethod = new Attribute("method", instr
				.getGenericMethodString());
		Attribute atribXMLargs = new Attribute("XMLargs",
				convertArgsToXML(instr.getArgs()));

		server.setAttribute(atribDOid);
		server.setAttribute(atribMethod);
		server.setAttribute(atribXMLargs);

		Document doc = new Document();
		doc.setRootElement(server);
		XMLOutputter xout = new XMLOutputter();
		try {
			xout.output(doc, sw);
		} catch (IOException e) {
			;
		}

		return sw.toString();
	}

	// Carrefa as propriedade do cliente
	public static i_RemoteClient getClientPropertiesObj(long id, String xml) {
		// Criamos uma classe SAXBuilder que vai processar o XML
		SAXBuilder sb = new SAXBuilder();

		// Este documento agora possui toda a estrutura do arquivo.
		Document d = null;
		try {
			d = sb.build(new StringReader(xml));
		} catch (JDOMException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		i_RemoteClient rc = null;
		// Recuperamos o elemento root
		Element client = d.getRootElement();
		byte kind = Byte.parseByte(client.getAttributeValue("kind"));
		if (kind == VirtualClient.RMI_CLIENT) {
			rc = new RMIClient(id);
		} else if (kind == VirtualClient.CORBA_CLIENT) {
			rc = new CorbaClient(id);
		}
		return rc;
	}

	// Carrefa as propriedade do cliente
	public static String getClientProperties(String serviceName,
			i_JwallaClient rc, String xml) {
		// Criamos uma classe SAXBuilder que vai processar o XML
		SAXBuilder sb = new SAXBuilder();

		// Este documento agora possui toda a estrutura do arquivo.
		Document d = null;
		try {
			d = sb.build(new StringReader(xml));
		} catch (JDOMException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// Recuperamos o elemento root
		Element client = d.getRootElement();
	

		// Recuperacao apenas nos nao virtuais
		if (rc.getKind() == VirtualClient.RMI_CLIENT
				|| rc.getKind() == VirtualClient.CORBA_CLIENT) {
			// TODO MUDAR COM CORBA
			i_RemoteClient rmic = (i_RemoteClient) rc;
			rmic.setIp(client.getAttributeValue("ip"));
			try {
				rmic
						.setDoor(Integer.parseInt(client
								.getAttributeValue("door")));
			} catch (NumberFormatException nfe) {
				return XMLPreDefined.generateErrorXML(serviceName, 0, nfe
						.getMessage()
						+ "\nRecebido no XML: "
						+ client.getAttributeValue("door"));
			}
		} else if (rc.getKind() == VirtualClient.VIRTUAL_CLIENT) {
			VirtualClient vc = (VirtualClient) rc;
			boolean withPK = new Boolean(client.getAttributeValue("withKey"))
					.booleanValue();

			if (withPK) {
				PublicKey pk = null;
				pk = MyKeyPairGenerator.readKeyFromFile(new BigInteger(client
						.getAttributeValue("pk")), new BigInteger(client
						.getAttributeValue("exp")));
				if (pk == null) {
					return XMLPreDefined.generateErrorXML(serviceName, 0,
							"Erro ao regerar chave publica"
									+ "\nRecebido no XML: "
									+ client.getAttributeValue("pk"));
				} else {
					vc.setClientPK(pk);
				}
			}
		}
		return Constants.OK;
	}

	public static String generateVirtualClientConnectXML(String serviceName,
			long id, boolean withKey, BigInteger[] publicKeyBigInt) {
		StringWriter sw = new StringWriter();

		Element server = new Element("Service");

		Attribute atribServiceName = new Attribute("name", serviceName);
		Attribute atribId = new Attribute("id", String.valueOf(id));
		Attribute atribWithKey = new Attribute("withKey", String
				.valueOf(withKey));
		Attribute atribKey = null;
		Attribute atribExp = null;
		if (withKey) {
			atribKey = new Attribute("pk", String.valueOf(publicKeyBigInt[0]));
			atribExp = new Attribute("exp", String.valueOf(publicKeyBigInt[1]));
		}

		server.setAttribute(atribServiceName);
		server.setAttribute(atribId);
		server.setAttribute(atribWithKey);

		if (withKey) {
			server.setAttribute(atribKey);
			server.setAttribute(atribExp);
		}

		Document doc = new Document();
		doc.setRootElement(server);
		XMLOutputter xout = new XMLOutputter();
		try {
			xout.output(doc, sw);
		} catch (IOException e) {
			;
		}

		return sw.toString();
	}

	public static String generateRemoteClientConnectXML(String serviceName,
			long id) {
		StringWriter sw = new StringWriter();

		Element server = new Element("Service");

		Attribute atribServiceName = new Attribute("name", serviceName);
		Attribute atribId = new Attribute("id", String.valueOf(id));

		server.setAttribute(atribServiceName);
		server.setAttribute(atribId);

		Document doc = new Document();
		doc.setRootElement(server);
		XMLOutputter xout = new XMLOutputter();
		try {
			xout.output(doc, sw);
		} catch (IOException e) {
			;
		}

		return sw.toString();
	}

	public static long getRemoteClientConnectXMLId(String serviceName,
			String xml) {
		// Criamos uma classe SAXBuilder que vai processar o XML
		SAXBuilder sb = new SAXBuilder();

		// Este documento agora possui toda a estrutura do arquivo.
		Document d = null;
		try {
			d = sb.build(new StringReader(xml));
		} catch (JDOMException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// Recuperamos o elemento root
		Element client = d.getRootElement();

		// Recuperacao apenas nos nao virtuais
		final String name = client.getAttributeValue("name");
		final long id = Long.parseLong(client.getAttributeValue("id"));

		return id;
	}

	public static String generateVirtualClientConnectXML(String serviceName,
			boolean withKey, BigInteger[] publicKeyBigInt) {
		return generateVirtualClientConnectXML(serviceName, 0, withKey,
				publicKeyBigInt);
	}

	public static String generateRemoteClientConnectXML(String serviceName,
			String ip, int door, byte TYPE) {
		StringWriter sw = new StringWriter();

		Element server = new Element("Service");

		Attribute atribServiceName = new Attribute("name", serviceName);
		Attribute atribIp = new Attribute("ip", ip);
		Attribute atribDoor = new Attribute("door", String.valueOf(door));
		Attribute atribKind = new Attribute("kind", String.valueOf(TYPE));

		server.setAttribute(atribServiceName);
		server.setAttribute(atribIp);
		server.setAttribute(atribDoor);
		server.setAttribute(atribKind);

		Document doc = new Document();
		doc.setRootElement(server);
		XMLOutputter xout = new XMLOutputter();
		try {
			xout.output(doc, sw);
		} catch (IOException e) {
			;
		}

		return sw.toString();
	}

	// Carrefa as propriedade do cliente
	public static ClientInformations getClientInformations(String xml) {
		// Criamos uma classe SAXBuilder que vai processar o XML
		SAXBuilder sb = new SAXBuilder();

		// Este documento agora possui toda a estrutura do arquivo.
		Document d = null;
		try {
			d = sb.build(new StringReader(xml));
		} catch (JDOMException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// Recuperamos o elemento root
		Element client = d.getRootElement();

		// Recuperacao apenas nos nao virtuais
		final String name = client.getAttributeValue("name");
		final long id = Long.parseLong(client.getAttributeValue("id"));
		final boolean withPK = Boolean.parseBoolean(client
				.getAttributeValue("withKey"));
		BigInteger bi[] = new BigInteger[2];
		if (withPK) {
			bi[0] = new BigInteger(client.getAttributeValue("pk"));
			bi[1] = new BigInteger(client.getAttributeValue("exp"));
		}

		return new ClientInformations(name, id, bi);
	}

}

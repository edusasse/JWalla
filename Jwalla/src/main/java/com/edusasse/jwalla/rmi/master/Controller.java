package com.edusasse.jwalla.rmi.master;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;

import org.jdom.Attribute;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.XMLOutputter;

import com.edusasse.jwalla.client.service.Service;
import com.edusasse.jwalla.client.service.i_Service;
import com.edusasse.jwalla.client.service.i_ServiceListener;

public class Controller {
	// Instancia unica
	private static Controller con;
	// Lista dos servicos regisrrados no mestre
	private static ArrayList<Service> serviceList;
	// Lista dos ouvintes
	private static ArrayList<i_ServiceListener> serviceListeners;

	private Controller() {
		serviceList = new ArrayList<Service>();
		serviceListeners = new ArrayList<i_ServiceListener>();
	}

	// Adiciona servico
	public void addService(Service s) throws IllegalArgumentException {
		for (i_Service sl : Controller.serviceList) {
			if (sl.getDoor() == s.getDoor())
				throw new IllegalArgumentException("A porta \"" + s.getDoor()
						+ "\" utlizada para o servico \"" + s.getName().trim()
						+ "\" esta em uso por \"" + sl.getName().trim() + "\"");
			if (sl.getName().trim().equals(s.getName().trim()))
				throw new IllegalArgumentException(
						"Nome do servico ja registrado em \""
								+ sl.getName().trim() + ":" + sl.getDoor()
								+ "\"");
		}
		// Adiciona o servico
		serviceList.add(s);
		updateListenersAdded(s);
	}

	public static Controller getInstance() {
		if (con == null)
			con = new Controller();
		return con;
	}

	// Remove servico
	public void removeService(i_Service s) {
		serviceList.remove(s);
		updateListenersRemove(s);
	}

	private void updateListenersRemove(i_Service s) {
		for (i_ServiceListener sl : serviceListeners) {
			sl.serviceRemover(s);
		}
	}

	private void updateListenersAdded(i_Service s) {
		for (i_ServiceListener sl : serviceListeners) {
			sl.serviceAdded(s);
		}
	}

	public void addServiceListeners(i_ServiceListener sl) {
		serviceListeners.add(sl);
	}

	public void removeServiceListeners(i_ServiceListener sl) {
		serviceListeners.remove(sl);
	}

	public String getServices() {
		StringWriter sw = new StringWriter();
		Element services = new Element("Services");

		for (i_Service sl : serviceList) {
			Element service = new Element("Service");

			Attribute atribTitle = new Attribute("title", sl.getName().trim());
			Attribute atribDesc = new Attribute("desc", sl.getDesc());
			Attribute atribVersion = new Attribute("version", sl.getVersion());
			Attribute atribAd = new Attribute("address", String.valueOf(sl.getAddress()));
			Attribute atribDoor = new Attribute("door", String.valueOf(sl.getDoor()));
			service.setAttribute(atribTitle);
			service.setAttribute(atribDesc);
			service.setAttribute(atribVersion);
			service.setAttribute(atribDoor);

			services.addContent(service);
		}

		Document doc = new Document();
		doc.setRootElement(services);
		XMLOutputter xout = new XMLOutputter();
		try {
			xout.output(doc, sw);
		} catch (IOException e) {
			;
		}
		
		return sw.toString();

	}
}

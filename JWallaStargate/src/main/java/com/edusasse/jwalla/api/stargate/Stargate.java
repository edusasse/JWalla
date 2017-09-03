/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.edusasse.jwalla.api.stargate;

import java.io.IOException;
import java.rmi.AlreadyBoundException;

import com.edusasse.jwalla.api.stargate.Corba.CorbaServer;
import com.edusasse.jwalla.client.clients.i_RemoteClient;
import com.edusasse.jwalla.client.service.Service;
import com.edusasse.jwalla.client.service.i_Service;
import com.edusasse.jwalla.server.RMIServer;
import com.edusasse.jwalla.server.ServerServiceController;
import com.edusasse.jwalla.server.i_Server;

/**
 * 
 * @author Eduardo
 */
public class Stargate {
	private i_Service service;

	// Servers
	private i_Server srv_corba;
	private i_Server srv_rmi;
	// Clients
	private i_RemoteClient cli_corba;
	private i_RemoteClient cli_rmi;
	// Servico
	private Service mys;
	

	public Stargate(i_Service s, byte TYPE) throws AlreadyBoundException, IOException {
		this.service = s;
		// Servico Stargate
		this.mys = new Service("SG:"+s.getName(),s.getDesc()+"\nOBS: Service provided through a Stargate",s.getVersion(),"localhost",2000);
		
		if (TYPE == Paramters.RMI2CORBA) {
			
			// Servidor Corba
			this.srv_corba = new CorbaServer(s, mys);
			
			// Controladora do Servico
			ServerServiceController ssc = new ServerServiceController(s,false);
			// Servidor local RMI
			this.srv_rmi = new RMIServer(ssc,this.srv_corba);
		
		}

	}
	public i_Server getSrv_corba() {
		return this.srv_rmi;
	}
	
	public Service getStargateService() {
		return this.mys;
	}

}

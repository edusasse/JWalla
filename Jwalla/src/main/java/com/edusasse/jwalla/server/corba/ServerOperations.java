package com.edusasse.jwalla.server.corba;

import com.edusasse.jwalla.server.i_Server;


/**
* com/edusasse/jwalla/server/Corba/ServerOperations.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from Server.idl
* Ter�a-feira, 3 de Mar�o de 2009 15h40min13s BRT
*/

public interface ServerOperations extends i_Server
{
  String callServerUpdate (long id, String XMLComands);
  String getServiceName ();
  String instanceSync ();
  boolean update (String XMLComands);
  String createRemoteClientConnection (String xml);
} // interface ServerOperations

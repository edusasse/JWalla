package com.edusasse.sample.calc.server;

import org.omg.CORBA.ORB;
import org.omg.CosNaming.NameComponent;
import org.omg.CosNaming.NamingContextExt;
import org.omg.CosNaming.NamingContextExtHelper;
import org.omg.PortableServer.POA;
import org.omg.PortableServer.POAHelper;

public class Server
{
  public static void main(String args[]) {
    try{
      // Cria e inicializa o ORB
      ORB orb = ORB.init(args, null);

      // Cria a implementação e registra no ORB
      calc_numericaImpl calculadora_numerica = new calc_numericaImpl();

      // Ativa o POA
      POA rootpoa = POAHelper.narrow(orb.resolve_initial_references("RootPOA"));
      rootpoa.the_POAManager().activate();

      // Pega a referência do servidor
      org.omg.CORBA.Object ref = rootpoa.servant_to_reference(calculadora_numerica);
      calc_numerica href = calc_numericaHelper.narrow(ref);
	  
      // Obtém uma referência para o servidor de nomes
      org.omg.CORBA.Object objRef = orb.resolve_initial_references("NameService");
      NamingContextExt namecontextRef = NamingContextExtHelper.narrow(objRef);

      // Registra o servidor no servico de nomes
      String name = "calc";
      NameComponent path[] = namecontextRef.to_name(name);
      namecontextRef.rebind(path, href);

      System.out.println("Server waiting on requisitions...");

      // Aguarda chamadas dos clientes
      orb.run();
    } catch (Exception e) {
        System.err.println("Error: " + e);
        e.printStackTrace(System.out);
    }
    System.out.println("Server ended!");
  }
}

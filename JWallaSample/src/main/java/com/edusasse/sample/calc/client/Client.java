package com.edusasse.sample.calc.client;

import org.omg.CORBA.ORB;
import org.omg.CORBA.ShortHolder;
import org.omg.CosNaming.NamingContextExt;
import org.omg.CosNaming.NamingContextExtHelper;

import com.edusasse.sample.calc.server.calc_numerica;
import com.edusasse.sample.calc.server.calc_numericaHelper;

public class Client {

  public static void main(String args[]) {
	  final String[] myArgs = new String[]{};
    try {
      // Cria e inicializa o ORB
      ORB orb = ORB.init(myArgs, null);
      
      // Obtem referencia para o servico de nomes
      org.omg.CORBA.Object objRef = orb.resolve_initial_references("NameService");
      NamingContextExt namecontextRef = NamingContextExtHelper.narrow(objRef);
 
      // Obtem referencia para o servidor
      String name = "calc";
      calc_numerica c_numerica = calc_numericaHelper.narrow(namecontextRef.resolve_str(name));

      while (true) {
	      // Obtem valores
	      System.out.print("Select an operation (+, -, /, *, f): ");
	      java.io.BufferedReader reader = new java.io.BufferedReader(new java.io.InputStreamReader(System.in));
	      String op = reader.readLine();
	      if (op.equals("f"))
	      	System.exit(0);
	      
	      System.out.print("First value: ");
	      short p1 = new Short(reader.readLine()).shortValue();
	
	      System.out.print("Second value: ");
	      short p2 = new Short(reader.readLine()).shortValue();
	      
	      ShortHolder ret = new ShortHolder(); 
	      if (op.equals("+"))
	         c_numerica.soma_int(p1, p2, ret);	
	      else if (op.equals("-"))	
	      	c_numerica.sub_int(p1, p2, ret);
	      else if (op.equals("/"))	
	      	c_numerica.div_double(p1, p2, ret);
	      else if (op.equals("*"))	
	      	c_numerica.mul_double(p1, p2, ret);
	      
	      System.out.println("Result: " + ret.value);
      }

    } catch (Exception e) {
        e.printStackTrace();
    }
  }
}


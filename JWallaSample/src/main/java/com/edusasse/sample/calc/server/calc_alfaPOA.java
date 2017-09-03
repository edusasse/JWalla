package com.edusasse.sample.calc.server;


/**
* calculadora/calc_alfaPOA.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from Calculadora.idl
* Segunda-feira, 18 de Maio de 2009 15h36min02s BRT
*/

public abstract class calc_alfaPOA extends org.omg.PortableServer.Servant
 implements com.edusasse.sample.calc.server.calc_alfaOperations, org.omg.CORBA.portable.InvokeHandler
{

  // Constructors

  private static java.util.Hashtable _methods = new java.util.Hashtable ();
  static
  {
    _methods.put ("soma_str", new java.lang.Integer (0));
    _methods.put ("mistura_str", new java.lang.Integer (1));
  }

  public org.omg.CORBA.portable.OutputStream _invoke (String $method,
                                org.omg.CORBA.portable.InputStream in,
                                org.omg.CORBA.portable.ResponseHandler $rh)
  {
    org.omg.CORBA.portable.OutputStream out = null;
    java.lang.Integer __method = (java.lang.Integer)_methods.get ($method);
    if (__method == null)
      throw new org.omg.CORBA.BAD_OPERATION (0, org.omg.CORBA.CompletionStatus.COMPLETED_MAYBE);

    switch (__method.intValue ())
    {
       case 0:  // calculadora/calc_alfa/soma_str
       {
         String str1 = in.read_string ();
         String str2 = in.read_string ();
         String $result = null;
         $result = this.soma_str (str1, str2);
         out = $rh.createReply();
         out.write_string ($result);
         break;
       }

       case 1:  // calculadora/calc_alfa/mistura_str
       {
         String str1 = in.read_string ();
         String str2 = in.read_string ();
         String $result = null;
         $result = this.mistura_str (str1, str2);
         out = $rh.createReply();
         out.write_string ($result);
         break;
       }

       default:
         throw new org.omg.CORBA.BAD_OPERATION (0, org.omg.CORBA.CompletionStatus.COMPLETED_MAYBE);
    }

    return out;
  } // _invoke

  // Type-specific CORBA::Object operations
  private static String[] __ids = {
    "IDL:calculadora/calc_alfa:1.0"};

  public String[] _all_interfaces (org.omg.PortableServer.POA poa, byte[] objectId)
  {
    return (String[])__ids.clone ();
  }

  public calc_alfa _this() 
  {
    return calc_alfaHelper.narrow(
    super._this_object());
  }

  public calc_alfa _this(org.omg.CORBA.ORB orb) 
  {
    return calc_alfaHelper.narrow(
    super._this_object(orb));
  }


} // class calc_alfaPOA

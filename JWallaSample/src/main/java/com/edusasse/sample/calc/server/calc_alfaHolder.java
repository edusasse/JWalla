package com.edusasse.sample.calc.server;

/**
* calculadora/calc_alfaHolder.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from Calculadora.idl
* Segunda-feira, 18 de Maio de 2009 15h36min02s BRT
*/

public final class calc_alfaHolder implements org.omg.CORBA.portable.Streamable
{
  public com.edusasse.sample.calc.server.calc_alfa value = null;

  public calc_alfaHolder ()
  {
  }

  public calc_alfaHolder (com.edusasse.sample.calc.server.calc_alfa initialValue)
  {
    value = initialValue;
  }

  public void _read (org.omg.CORBA.portable.InputStream i)
  {
    value = com.edusasse.sample.calc.server.calc_alfaHelper.read (i);
  }

  public void _write (org.omg.CORBA.portable.OutputStream o)
  {
    com.edusasse.sample.calc.server.calc_alfaHelper.write (o, value);
  }

  public org.omg.CORBA.TypeCode _type ()
  {
    return com.edusasse.sample.calc.server.calc_alfaHelper.type ();
  }

}

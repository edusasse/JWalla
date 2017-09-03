/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.edusasse.jwalla.api.ws;

import java.net.URL;
import java.util.ArrayList;

/**
 *
 * @author Eduardo
 */
public class ControllerWS {
    private URL url;

    public ControllerWS(URL url){
        this.url = url;
    }

    public ArrayList getServices(){
        ArrayList l = new ArrayList();
        l.add("Teste");
        return l;
    }

}

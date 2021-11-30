package org.avy.viber2;

import org.avy.viber2.Toybox.Toybox;

public class Main {
    public static void main(String[] args) {
	try {
	    Toybox toybox = new Toybox();
	    toybox.VladoHibernate();
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }
}

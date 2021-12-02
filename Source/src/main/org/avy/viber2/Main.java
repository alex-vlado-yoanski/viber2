package org.avy.viber2;

import java.io.IOException;

import org.avy.viber2.Sockets.Server;

public class Main {
    public static void main(String[] args) {
	// Стартира се сървъра на порт 8081
	Server Srv = null;
	try {
	    System.out.println("Server created");
	    Srv = new Server(8081);
	} catch (Exception e) {
	    e.printStackTrace();
	    return;
	}

	try {
	    System.out.println("Server started");
	    Srv.StartServer();
	} catch (Exception e) {
	    e.printStackTrace();
	}
/*
	try {
	    System.out.println("Server sent Yee");
	    Srv.ListenServer("Yee");
	} catch (IOException e1) {
	    e1.printStackTrace();
	}*/
    }
}

package org.avy.viber2;

public class Main {
    public static void main(String[] args) {	
	Server server = new Server(8080);
	server.start();
    }
}
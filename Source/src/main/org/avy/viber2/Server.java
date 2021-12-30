package org.avy.viber2;

import java.io.IOException;
import java.net.*;

public class Server extends Thread {
    private int portNumber;
    private ServerSocket serverSocket;
    
    public Server(int portNumber){
	this.portNumber = portNumber;
    }
    
    @Override
    public void run() {
	System.out.println("Server started.");
	while(true) {
	    try {
        	System.out.println("Waiting for connection...");
     	    	
     	    	Socket socket = serverSocket.accept();
     	    	System.out.println("Connection established.");
     	    	
                RequestHandler requestHandler = new RequestHandler(socket);
                requestHandler.start();
            } catch (IOException e) {
        	System.out.println("Establishing connection failed...");
                e.printStackTrace();
            }
        }
    }
    
    public void start() {
	try {
	    System.out.println("Server starting...");
	    serverSocket = new ServerSocket(portNumber);
	    
	    this.run();
	} catch(IOException e) {
	    System.out.println("Starting server failed... Port number may be busy.");
	    e.printStackTrace();
	}	
    }
}

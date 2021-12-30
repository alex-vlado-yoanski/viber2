package org.avy.viber2;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

import org.avy.viber2.data.*;
import org.avy.viber2.tables.mapping.User;

import com.google.gson.*;

public class RequestHandler extends Thread {
    private static final int messageLength = 1500;
    private Socket socket;

    public RequestHandler(Socket socket) {
	this.socket = socket;
    }

    @Override
    public void run() {
	try {
	    // Прочитане на заявката
	    System.out.println("Reading request...");
	    byte[] inputMessage = new byte[messageLength];
	    
	    DataInputStream inputStream = new DataInputStream(socket.getInputStream());
	    inputStream.read(inputMessage, 0, messageLength);
	    
	    String request = new String(inputMessage, StandardCharsets.UTF_8);
	    request = request.trim(); // за всеки случай
	    System.out.println(request);

	    // Обработка на заявката
	    System.out.println("Procesing request...");

	    String response = process(request);
	    System.out.println(response);
	    
	    // Изпращане на отговор на заявката
	    System.out.println("Returning response...");
	    PrintWriter outputStream = new PrintWriter(socket.getOutputStream(), true);
	    outputStream.print(response);
	    outputStream.flush();
	    
	    // Затваряне на връзката
	    System.out.println("Closing response...");
	    inputStream.close();
	    outputStream.close();
	    socket.close();
	} catch (IOException e) {
	    e.printStackTrace();
	}
    }

    private String process(String request) {
	String response = null;

	try {
	    //Gson gson = new GsonBuilder().registerTypeAdapter(User.class, new LoginDataHandler()).setPrettyPrinting().create();
	    //User user = gson.fromJson(request, User.class);
	    //response = gson.toJson(user);
	    
	    response = "test44";
	} catch (Exception e) {
	    System.out.println("Processing request failed.");
	    e.printStackTrace();
	}

	return response;
    }

}

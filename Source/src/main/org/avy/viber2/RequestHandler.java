package org.avy.viber2;

import java.io.*;
import java.net.Socket;
import org.avy.viber2.data.*;
import com.google.gson.*;

public class RequestHandler extends Thread {
    private Socket socket;
    
    public RequestHandler(Socket socket) {
	this.socket = socket;
    }
    
    @Override
    public void run()
    {
	try {
	    // Прочитане на заявката
	    System.out.println("Reading request...");
	    
	    InputStreamReader inputStream = new InputStreamReader(socket.getInputStream());
	    BufferedReader dataIn = new BufferedReader(inputStream);

	    String request = null;
	    
	    String line = dataIn.readLine();
	    while(line != null && line.length() > 0)
	    {
		request += line;
		line = dataIn.readLine();
	    }
	    
	    System.out.println(request);
	    
	    // Обработка на заявката
	    System.out.println("Procesing request...");
	    
	    String response = process(request);
	    System.out.println(response);
	    
	    // Изпращане на отговор на заявката	    
	    System.out.println("Returning response...");
	    PrintWriter dataOut = new PrintWriter(socket.getOutputStream(), true);
	    
	    dataOut.println(response);
            dataOut.flush();
            
            // Затваряне на връзката
            dataIn.close();
            dataOut.close();
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

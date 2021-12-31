package org.avy.viber2;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import org.avy.viber2.data.*;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

/**
* Как се добавя нов тип заявка:
* 1. Дефиниране на номера на заявката в data/RequestType.java
* 2. Разписване на handler наследник на IDataHandler<YOUR_CLASS>
* 3. Добавяне на новия handler в process(...)
*/

public class RequestHandler extends Thread {
    private static final int MESSAGE_LENGTH = 1500;
    private static final int READ_FAILED = -1;
    
    private Socket socket;

    public RequestHandler(Socket socket) {
	this.socket = socket;
    }

    @Override
    public void run() {
	while(!socket.isClosed()) { // слушаме докато връзката е отворена
	    handle();
	}
    }

    private void handle() {
	try {
	    // Прочитане на заявката
	    byte[] inputMessage = new byte[MESSAGE_LENGTH];
	    
	    DataInputStream inputStream = new DataInputStream(socket.getInputStream());
	    int readResult = inputStream.read(inputMessage, 0, MESSAGE_LENGTH);
	    
	    // Проверяваме дали клиентът не е затвори връзката
	    if (readResult == READ_FAILED) { 
		System.out.println("Closing connection...");
		
		// TO DO
		
		socket.close();
		System.out.println("Connection closed");
		return;
	    }
	    
	    String request = new String(inputMessage, StandardCharsets.UTF_8);
	    request = request.trim(); // за всеки случай
	    
	    // Ако няма заявка продължаваме да слушаме
	    if (request.isEmpty())
		return;
	    
	    System.out.println("Reading request...");
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
	} catch (IOException e) {
	    e.printStackTrace();
	}
    }
    
    private String process(String request) {
	String response = null;

	try {
	    
	    // client test only
	    //request = "{\"requestType\" : \"2\",\"name\": \"SA\",\"password\": \"common\"}";
	    
	    // Налага се всека заявка да има добавен тип, за да знаем как да обработим заявката
	    // Ако няма тип връщаме грешка 400
	    int requestType = 0;
	    
	    JsonObject job = new Gson().fromJson(request, JsonObject.class);
	    requestType = job.get("requestType").getAsInt();
	    
	    switch(requestType) {
	    	case RequestType.LOGIN_CREDENTIALS: 
	    	case RequestType.SIGN_IN: {
	    	    LoginDataHandler login = new LoginDataHandler();
	    	    response = login.process(request);
	    	    break;
	    	}
	    	// следващият вид заявка да се разписва тук ^
	    	default:{
	    	    response = createErrorResponse(470);
	    	    break;	    	    
	    	}
	    } // switch
	} catch (Exception e) {
	    response = createErrorResponse(400);
	    System.out.println("Processing request failed.");
	    e.printStackTrace();
	}

	return response;
    }
    
    public String createErrorResponse(int errorCode) {
	String errorMessage = "Bad request."; // errorCode : 400
	
	// Формиране на описание на грешката според кода на грешка
	switch(errorCode) {
	    case 470: {
		errorMessage += " Invalid request type.";
		break;
	    }
	}
	
	// Формиране на отговора за грешка
	String response = "{\"errorCode\" : \"" + errorCode + "\", \"errorMessage\" : \"" + errorMessage + "\"}";
	
	return response;
    }
    
}

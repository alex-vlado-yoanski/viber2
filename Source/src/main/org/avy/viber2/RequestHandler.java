package org.avy.viber2;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import org.avy.viber2.data.*;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

//Как се добавя нов тип заявка:
//	1. Дефиниране на номера на заявката в data/RequestType.java
//	2. Разписване на handler наследник на IDataHandler<YOUR_CLASS>
//	3. Добавяне на новия handler в process(...)

public class RequestHandler extends Thread {
    private static final int MESSAGE_LENGTH = 1500;
    private static final int READ_FAILED = -1;

    private Socket socket;
    private boolean isSocketForClosing = false;

    public RequestHandler(Socket socket) {
	this.socket = socket;
    }

    @Override
    public void run() {
	while (!socket.isClosed()) { // слушаме докато връзката е отворена
	    handle();

	    // Ако по някаква причина не успеем да затворим сокета,
	    // въпреки това прекъсваме цикъла, за да не хвърля exception-и
	    if (!isSocketForClosing)
		break;
	}
    }

    private void handle() {
	try {
	    // Прочитане на заявката
	    byte[] inputMessage = new byte[MESSAGE_LENGTH];

	    DataInputStream inputStream = new DataInputStream(socket.getInputStream());
	    int readResult = inputStream.read(inputMessage, 0, MESSAGE_LENGTH);

	    // Проверяваме дали клиентът не е затворил връзката
	    if (readResult == READ_FAILED) {
		closeSocket();
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
	    closeSocket();
	    e.printStackTrace();
	}
    }

    private String process(String request) {
	String response = null;

	try {
	    // client test only
	    // request = "{\"requestType\":\"1\",\"name\":\"kiril\",\"password\":\"parolata\"}";
	    // request = "{\"requestType\":\"3\",\"userID\":\"2\"}";
	    // request = "{\"requestType\":\"4\",\"invitations\":[{\"invitationID\":\"0\",\"status\":\"1\",\"sender\":{\"senderID\":\"0\",\"name\":\"test\"},\"receiver\":{\"receiverID\":\"0\",\"name\":\"test\"}}]}"
	    
	    // Налага се всяка заявка да има добавен тип, за да знаем как да обработим заявката.
	    // Ако няма тип връщаме грешка 400.
	    int requestType = 0;

	    JsonObject job = new Gson().fromJson(request, JsonObject.class);
	    requestType = job.get("requestType").getAsInt();

	    switch (requestType) {
	    case RequestType.LOGIN_CREDENTIALS:
	    case RequestType.SIGN_IN: {
		LoginDataHandler login = new LoginDataHandler();
		response = login.process(request);
		break;
	    }
	    case RequestType.USER_CHATS: {
		UserChatsDataHandler userChats = new UserChatsDataHandler();
		response = userChats.process(request);
		break;
	    }
	    case RequestType.USER_INVITATIONS_EXTRACT: {
		UserInvitationsExtractorHandler invitations = new UserInvitationsExtractorHandler();
		response = invitations.process(request);
		break;
	    }
	    // следващият вид заявка да се разписва тук ^
	    default: {
		response = ResponseType.createErrorResponse(470);
		break;
	    }
	    } // switch
	} catch (Exception e) {
	    response = ResponseType.createErrorResponse(400);
	    System.out.println("Processing request failed.");
	    e.printStackTrace();
	}

	return response;
    }

    private void closeSocket() {
	isSocketForClosing = true;

	try {
	    System.out.println("Closing connection...");

	    // TODO: синхронизация на клиенти

	    socket.close();
	    System.out.println("Connection closed");
	} catch (IOException e) {
	    e.printStackTrace();
	}
    }

}

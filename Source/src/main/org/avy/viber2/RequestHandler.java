package org.avy.viber2;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

import org.avy.viber2.data.*;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

/**
 * Как се добавя нов тип заявка: 1. Дефиниране на номера на заявката в
 * data/RequestType.java 2. Разписване на handler наследник на
 * IDataHandler<YOUR_CLASS> 3. Добавяне на новия handler в process(...)
 */

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
	    if (isSocketForClosing)
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
	    // request = "{\"requestType\":\"1\",\"name\":\"SA\",\"password\":\"common\"}";
	    // request = "{\"requestType\":\"2\",\"name\":\"SA\",\"password\":\"common\"}";
	    // request = "{\"requestType\":\"3\",\"userID\":\"2\"}";
	    // request = "{\"requestType\":\"4\",\"userID\":\"2\"}";
	    // request = "{\"requestType\":\"5\",\"invitationID\":\"1\",\"status\":\"1\"}";
	    // request = "{\"requestType\":\"6\",\"sender\":\"1\",\"receiver\":\"2\"}";
	    // request = "{\"requestType\":\"7\",\"chatID\":\"2\"}";
	    // request =
	    // "{\"requestType\":\"8\",\"chatID\":\"2\",\"text\":\"hello\",\"user\":\"2\"}";
	    // request =
	    // "{\"requestType\":\"9\",\"chatID\":\"2\",\"text\":\"hello2\",\"user\":\"2\",\"messageID\":\"8\"}";
	    // request = "{\"requestType\":\"10\",\"phrase\":\"sto\", \"userID\":\"2\"}";
	    // request = "{\"requestType\":\"11\",\"phrase\":\"sto\", \"userID\":\"1\", \"chatID\":\"5\"}";
	    // request = "{\"requestType\":\"12\",\"userID\":\"2\"}";
	    // request = "{\"requestType\":\"13\",\"chatID\":\"2\",\"userID\":\"1\"}";

	    // Налага се всяка заявка да има добавен тип, за да знаем как да обработим
	    // заявката. Ако няма тип връщаме грешка 400.
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
		UserInvitationsExtractHandler invitations = new UserInvitationsExtractHandler();
		response = invitations.process(request);
		break;
	    }
	    case RequestType.USER_INVITATION_MODIFY: {
		UserInvitationModifyHandler invitation = new UserInvitationModifyHandler();
		response = invitation.process(request);
		break;
	    }
	    case RequestType.USER_INVITATION_NEW: {
		UserInvitationNewHandler invitation = new UserInvitationNewHandler();
		response = invitation.process(request);
		break;
	    }
	    case RequestType.CHAT_MESSAGES_EXTRACT: {
		ChatMessagesExtractHandler messages = new ChatMessagesExtractHandler();
		response = messages.process(request);
		break;
	    }
	    case RequestType.USER_MESSAGE_NEW: {
		UserMessageNewHandler messages = new UserMessageNewHandler();
		response = messages.process(request);
		break;
	    }
	    case RequestType.USER_MESSAGE_EDIT: {
		UserMessageEditHandler messages = new UserMessageEditHandler();
		response = messages.process(request);
		break;
	    }
	    case RequestType.USER_SEARCH: {
		UserSearchHandler user = new UserSearchHandler();
		response = user.process(request);
		break;
	    }
	    case RequestType.USER_SEARCH_FOR_GROUP: {
		UserSearchForGroupHandler user = new UserSearchForGroupHandler();
		response = user.process(request);
		break;
	    }
	    case RequestType.CREATE_CHAT: {
		CreateChatHandler chat = new CreateChatHandler();
		response = chat.process(request);
		break;
	    }
	    case RequestType.ADD_USER_TO_CHAT: {
		AddUserToChatHandler chat = new AddUserToChatHandler();
		response = chat.process(request);
		break;
	    }
	    /*
	     * case RequestType.FALE_UPLOAD: { break; } case RequestType.FALE_READ: {
	     * FileReadHandler file = new FileReadHandler(); response =
	     * file.process(request); break; }
	     */
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
	    socket.close();
	    System.out.println("Connection closed");
	} catch (IOException e) {
	    e.printStackTrace();
	}
    }

}

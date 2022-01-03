package org.avy.viber2.data;

import java.lang.reflect.Type;

import org.avy.viber2.database.DatabaseConnection;
import org.avy.viber2.tables.mapping.Chat;
import org.hibernate.Session;

import com.google.gson.*;

public class CreateChatHandler implements IDataHandler<Chat> {

    public CreateChatHandler() {
    }

    @Override
    public String process(String request) {
	String response = null;

	try {
	    // Подготвяне на json обект
	    GsonBuilder gsonBuilder = new GsonBuilder();
	    gsonBuilder.registerTypeAdapter(Chat.class, new CreateChatHandler());
	    gsonBuilder.setPrettyPrinting();

	    // Създаване на json обект
	    Gson json = gsonBuilder.create();

	    // Изчитане на заявката
	    Chat chat = json.fromJson(request, Chat.class);

	    // Обработване на заявката от базата данни
	    response = databaseProcessing(chat, json);
	} catch (Exception e) {
	    response = ResponseType.createErrorResponse(400);
	    e.printStackTrace();
	}

	return response;
    }

    private String databaseProcessing(Chat chat, Gson json) {
	String response = null;

	Chat processedChat = new Chat();

	try {
	    Session session = DatabaseConnection.getSessionFactory().openSession();
	    session.beginTransaction();
	    session.save(processedChat);
	    session.getTransaction().commit();
	    session.close();
	} catch (Exception e) {
	    response = ResponseType.createErrorResponse(570);
	    e.printStackTrace();
	}

	// Подготвяне на отговор
	try {
	    processedChat.setRequestType(chat.getRequestType());
	    response = json.toJson(processedChat);
	} catch (Exception e) {
	    response = ResponseType.createErrorResponse(571);
	    e.printStackTrace();
	}

	return response;
    }

    @Override
    public JsonElement serialize(Chat chat, Type typeOfUser, JsonSerializationContext context) {
	JsonObject jsonObject = new JsonObject();

	jsonObject.addProperty("requestType", chat.getRequestType());
	jsonObject.addProperty("chatID", chat.getID());

	return jsonObject;
    }

    @Override
    public Chat deserialize(JsonElement jsonElement, Type typeOfT, JsonDeserializationContext context)
	    throws JsonParseException {
	JsonObject jsonObject = jsonElement.getAsJsonObject();

	Chat chat = new Chat();
	chat.setRequestType(jsonObject.get("requestType").getAsInt());

	return chat;
    }

}

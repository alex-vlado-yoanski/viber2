package org.avy.viber2.data;

import java.lang.reflect.Type;
import java.sql.Timestamp;

import org.avy.viber2.database.DatabaseConnection;
import org.avy.viber2.tables.mapping.*;
import org.hibernate.Session;
import com.google.gson.*;

public class UserMessageNewHandler implements IDataHandler<Message> {

    public UserMessageNewHandler() {
    }

    @Override
    public String process(String request) {
	String response = null;

	try {
	    // Подготвяне на json обект
	    GsonBuilder gsonBuilder = new GsonBuilder();
	    gsonBuilder.registerTypeAdapter(Message.class, new UserMessageNewHandler());
	    gsonBuilder.setPrettyPrinting();

	    // Създаване на json обект
	    Gson json = gsonBuilder.create();

	    // Изчитане на заявката
	    Message message = json.fromJson(request, Message.class);

	    // Обработване на заявката от базата данни
	    response = databaseProcessing(message, json);
	} catch (Exception e) {
	    response = ResponseType.createErrorResponse(400);
	    e.printStackTrace();
	}

	return response;
    }

    private String databaseProcessing(Message message, Gson json) {
	String response = null;

	Message processedMessage = new Message();
	processedMessage.getChat().setID(message.getChat().getID());
	processedMessage.setText(message.getText());
	processedMessage.getSentBy().setID(message.getSentBy().getID());
	processedMessage.setCreateDate(new Timestamp(System.currentTimeMillis()));

	try {
	    Session session = DatabaseConnection.getSessionFactory().openSession();
	    session.beginTransaction();
	    session.save(processedMessage);
	    session.getTransaction().commit();
	    session.close();
	} catch (Exception e) {
	    response = ResponseType.createErrorResponse(570);
	    e.printStackTrace();
	}

	// Подготвяне на отговор
	try {
	    processedMessage.setRequestType(message.getRequestType());
	    response = json.toJson(processedMessage);
	} catch (Exception e) {
	    response = ResponseType.createErrorResponse(571);
	    e.printStackTrace();
	}

	return response;
    }

    @Override
    public JsonElement serialize(Message message, Type typeOfUser, JsonSerializationContext context) {
	JsonObject jsonObject = new JsonObject();

	jsonObject.addProperty("requestType", message.getRequestType());
	jsonObject.addProperty("chatID", message.getChat().getID());

	JsonObject jsonMessage = new JsonObject();
	jsonMessage.addProperty("id", message.getID());
	jsonMessage.addProperty("message", message.getText());
	jsonMessage.addProperty("date", message.getCreateDate().toString());
	jsonMessage.addProperty("sender_username", message.getSentBy().getName());
	jsonMessage.addProperty("sender_id", message.getSentBy().getID());
	jsonObject.add("message", jsonMessage);

	return jsonObject;
    }

    @Override
    public Message deserialize(JsonElement jsonElement, Type typeOfT, JsonDeserializationContext context)
	    throws JsonParseException {
	JsonObject jsonObject = jsonElement.getAsJsonObject();

	Message message = new Message();
	message.setRequestType(jsonObject.get("requestType").getAsInt());
	message.getChat().setID(jsonObject.get("chatID").getAsLong());
	message.setText(jsonObject.get("text").getAsString());
	message.getSentBy().setID(jsonObject.get("user").getAsLong());

	return message;
    }

}

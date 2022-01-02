package org.avy.viber2.data;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.avy.viber2.database.DatabaseConnection;
import org.avy.viber2.tables.mapping.*;
import org.hibernate.Session;
import com.google.gson.*;

public class ChatMessagesExtractHandler implements IDataHandler<Chat> {

    public ChatMessagesExtractHandler() {
    }

    @Override
    public String process(String request) {
	String response = null;

	try {
	    // Подготвяне на json обект
	    GsonBuilder gsonBuilder = new GsonBuilder();
	    gsonBuilder.registerTypeAdapter(Chat.class, new ChatMessagesExtractHandler());
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
	    // Обработване на заявката
	    Session session = DatabaseConnection.getSessionFactory().openSession();

	    CriteriaBuilder builder = session.getCriteriaBuilder();
	    CriteriaQuery<Message> query = builder.createQuery(Message.class);
	    Root<Message> root = query.from(Message.class);
	    List<Predicate> predicates = new ArrayList<Predicate>();

	    predicates.add(builder.equal(root.get("chat"), chat.getID()));
	    query.select(root).where(predicates.toArray(new Predicate[] {}));

	    // Изпълняване на заявката към базата данни
	    List<Message> messages = session.createQuery(query).getResultList();

	    processedChat.setID(chat.getID());
	    processedChat.setMessages(messages);
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
	JsonArray jsonArray = new JsonArray();

	jsonObject.addProperty("requestType", chat.getRequestType());

	for (Message message : chat.getMessages()) {
	    jsonObject.addProperty("messageID", message.getID());
	    jsonObject.addProperty("message", message.getText());
	    jsonObject.addProperty("date", message.getCreateDate().toString());
	    jsonObject.addProperty("sender_username", message.getSentBy().getName());
	    jsonObject.addProperty("sender_id", message.getSentBy().getID());
	}

	jsonObject.add("messages", jsonArray);

	return jsonObject;
    }

    @Override
    public Chat deserialize(JsonElement jsonElement, Type typeOfT, JsonDeserializationContext context)
	    throws JsonParseException {
	JsonObject jsonObject = jsonElement.getAsJsonObject();

	Chat chat = new Chat();
	chat.setRequestType(jsonObject.get("requestType").getAsInt());
	chat.setID(jsonObject.get("chatID").getAsLong());

	return chat;
    }

}

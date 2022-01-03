package org.avy.viber2.data;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.avy.viber2.database.DatabaseConnection;
import org.avy.viber2.tables.mapping.Message;
import org.hibernate.Session;

import com.google.gson.*;

public class UserMessageEditHandler implements IDataHandler<Message> {

    public UserMessageEditHandler() {
    }

    @Override
    public String process(String request) {
	String response = null;

	try {
	    // Подготвяне на json обект
	    GsonBuilder gsonBuilder = new GsonBuilder();
	    gsonBuilder.registerTypeAdapter(Message.class, new UserMessageEditHandler());
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

	try {
	    Session session = DatabaseConnection.getSessionFactory().openSession();
	    CriteriaBuilder builder = session.getCriteriaBuilder();
	    CriteriaQuery<Message> query = builder.createQuery(Message.class);
	    Root<Message> root = query.from(Message.class);
	    List<Predicate> predicates = new ArrayList<Predicate>();

	    predicates.add(builder.equal(root.get("ID"), message.getID()));
	    query.select(root).where(predicates.toArray(new Predicate[] {}));
	    List<Message> messages = session.createQuery(query).getResultList();

	    if (messages.size() != 0) {
		processedMessage = messages.get(0);

		processedMessage.setText(message.getText());

		session.beginTransaction();
		session.update(processedMessage);
		session.getTransaction().commit();
		session.close();
	    }

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
	message.setID(jsonObject.get("messageID").getAsLong());

	return message;
    }

}

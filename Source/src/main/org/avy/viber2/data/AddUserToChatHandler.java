package org.avy.viber2.data;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.avy.viber2.database.DatabaseConnection;
import org.avy.viber2.tables.mapping.Chat;
import org.avy.viber2.tables.mapping.User;
import org.hibernate.Session;

import com.google.gson.*;

public class AddUserToChatHandler implements IDataHandler<Chat> {

    public AddUserToChatHandler() {
    }

    @Override
    public String process(String request) {
	String response = null;

	try {
	    // Подготвяне на json обект
	    GsonBuilder gsonBuilder = new GsonBuilder();
	    gsonBuilder.registerTypeAdapter(Chat.class, new AddUserToChatHandler());
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
	    Session userSession = DatabaseConnection.getSessionFactory().openSession();
	    CriteriaBuilder userBuilder = userSession.getCriteriaBuilder();
	    CriteriaQuery<User> userQuery = userBuilder.createQuery(User.class);
	    Root<User> userRoot = userQuery.from(User.class);
	    List<Predicate> userPredicates = new ArrayList<Predicate>();

	    User user = new User();
	    userPredicates.add(userBuilder.equal(userRoot.get("ID"), chat.getUsers().get(0).getID()));
	    userQuery.select(userRoot).where(userPredicates.toArray(new Predicate[] {}));
	    List<User> users = userSession.createQuery(userQuery).getResultList();

	    if (users.size() != 0)
		user = users.get(0);

	    userSession.close();
	    processedChat.setID(chat.getID());
	    processedChat.setUser(user);
	    
	    Session session = DatabaseConnection.getSessionFactory().openSession();
	    session.beginTransaction();
	    session.update(processedChat);
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
	jsonObject.addProperty("userID", chat.getUsers().get(0).getID());

	return jsonObject;
    }

    @Override
    public Chat deserialize(JsonElement jsonElement, Type typeOfT, JsonDeserializationContext context)
	    throws JsonParseException {
	JsonObject jsonObject = jsonElement.getAsJsonObject();

	Chat chat = new Chat();
	chat.setRequestType(jsonObject.get("requestType").getAsInt());
	
	User user = new User();
	user.setID(jsonObject.get("userID").getAsLong());
	chat.setUser(user);
	
	chat.setID(jsonObject.get("chatID").getAsLong());
	
	return chat;
    }

}

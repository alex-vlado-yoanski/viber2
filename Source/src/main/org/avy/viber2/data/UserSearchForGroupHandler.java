package org.avy.viber2.data;

import java.lang.reflect.Type;
import java.util.*;

import javax.persistence.criteria.*;

import org.avy.viber2.database.DatabaseConnection;
import org.avy.viber2.tables.mapping.*;
import org.hibernate.Session;

import com.google.gson.*;

public class UserSearchForGroupHandler implements IDataHandler<UserSearch> {
    public UserSearchForGroupHandler() {
    }

    @Override
    public String process(String request) {
	String response = null;

	try {
	    // Подготвяне на json обект
	    GsonBuilder gsonBuilder = new GsonBuilder();
	    gsonBuilder.registerTypeAdapter(UserSearch.class, new UserSearchForGroupHandler());
	    gsonBuilder.setPrettyPrinting();

	    // Създаване на json обект
	    Gson json = gsonBuilder.create();

	    // Изчитане на заявката
	    UserSearch userSearch = json.fromJson(request, UserSearch.class);

	    // Обработване на заявката от базата данни
	    response = databaseProcessing(userSearch, json);
	} catch (Exception e) {
	    response = ResponseType.createErrorResponse(400);
	    e.printStackTrace();
	}

	return response;
    }

    private String databaseProcessing(UserSearch userSearch, Gson json) {
	String response = null;
	UserSearch proccesedUsers = new UserSearch();

	// Обработване на заявката
	try {
	    Session userSession = DatabaseConnection.getSessionFactory().openSession();
	    CriteriaBuilder userBuilder = userSession.getCriteriaBuilder();
	    CriteriaQuery<User> userQuery = userBuilder.createQuery(User.class);
	    Root<User> userRoot = userQuery.from(User.class);
	    List<Predicate> userPredicates = new ArrayList<Predicate>();

	    // Търсене на потребители по фраза
	    userPredicates.add(userBuilder.like(userRoot.get("name"), ("%" + userSearch.getSearchingPhrase() + "%")));
	    userQuery.select(userRoot).where(userPredicates.toArray(new Predicate[] {}));
	    List<User> foundUsers = userSession.createQuery(userQuery).getResultList();

	    // Филтриране на резултата
	    if (foundUsers.size() != 0) {
		Session chatSession = DatabaseConnection.getSessionFactory().openSession();
		CriteriaBuilder chatBuilder = chatSession.getCriteriaBuilder();
		CriteriaQuery<Chat> chatQuery = chatBuilder.createQuery(Chat.class);
		Root<Chat> chatRoot = chatQuery.from(Chat.class);
		List<Predicate> chatPredicates = new ArrayList<Predicate>();

		// Търсене на потребители по фраза
		chatPredicates.add(
		chatBuilder.equal(chatRoot.get("ID"), userSearch.getSearchingUser().getChats().get(0).getID()));
		chatQuery.select(chatRoot).where(chatPredicates.toArray(new Predicate[] {}));
		List<Chat> chats = chatSession.createQuery(chatQuery).getResultList();

		if (chats.size() != 0) {
		    // Текущият чат, в който ще се търсят потребители за добавяне
		    Chat chat = chats.get(0);
		    
		    for (User foundUser : foundUsers) {
			// Ако потребителя е този, който търси, го пропускаме
			if (foundUser.getID() == userSearch.getSearchingUser().getID())
			    continue;

			boolean bSkip = false;

			// Ако потребителят вече е в чата, го пропускаме
			for (User chatUser : chat.getUsers()) {
			    if (chatUser.getID() == foundUser.getID()) {
				bSkip = true;
				break;
			    }
			}

			if (bSkip)
			    continue;

			proccesedUsers.setUsers(foundUser);
		    }
		}
	    }
	} catch (

	Exception e) {
	    response = ResponseType.createErrorResponse(570);
	    e.printStackTrace();
	}

	// Подготвяне на отговор
	try {
	    proccesedUsers.setRequestType(userSearch.getRequestType());
	    response = json.toJson(proccesedUsers);
	} catch (Exception e) {
	    response = ResponseType.createErrorResponse(571);
	    e.printStackTrace();
	}

	return response;
    }

    @Override
    public JsonElement serialize(UserSearch user, Type typeOfUser, JsonSerializationContext context) {
	JsonObject jsonObject = new JsonObject();
	JsonArray jsonArray = new JsonArray();

	jsonObject.addProperty("requestType", user.getRequestType());

	for (User foundUser : user.getUsers()) {
	    JsonObject jsonUser = new JsonObject();
	    jsonUser.addProperty("name", foundUser.getName());
	    jsonUser.addProperty("id", foundUser.getID());
	    jsonArray.add(jsonUser);
	}

	jsonObject.add("users", jsonArray);

	return jsonObject;
    }

    @Override
    public UserSearch deserialize(JsonElement jsonElement, Type typeOfT, JsonDeserializationContext context)
	    throws JsonParseException {
	JsonObject jsonObject = jsonElement.getAsJsonObject();

	UserSearch user = new UserSearch();
	user.setRequestType(jsonObject.get("requestType").getAsInt());
	user.setSearchingPhrase(jsonObject.get("phrase").getAsString());
	user.getSearchingUser().setID(jsonObject.get("userID").getAsLong());
	user.getSearchingUser().setID(jsonObject.get("userID").getAsLong());

	Chat chat = new Chat();
	chat.setID(jsonObject.get("chatID").getAsLong());
	user.getSearchingUser().setChat(chat);

	return user;
    }
}

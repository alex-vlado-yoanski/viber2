package org.avy.viber2.data;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.*;

import org.avy.viber2.database.DatabaseConnection;
import org.avy.viber2.tables.mapping.*;
import org.hibernate.Session;

import com.google.gson.*;

public class UserChatsDataHandler implements IDataHandler<User> {
    public UserChatsDataHandler() {
    }

    @Override
    public String process(String request) {
	String response = null;

	try {
	    // Подготвяне на json обект
	    GsonBuilder gsonBuilder = new GsonBuilder();
	    gsonBuilder.registerTypeAdapter(User.class, new UserChatsDataHandler());
	    gsonBuilder.setPrettyPrinting();

	    // Създаване на json обект
	    Gson json = gsonBuilder.create();

	    // Изчитане на заявката
	    User user = json.fromJson(request, User.class);

	    // Обработване на заявката от базата данни
	    response = databaseProcessing(user, json);
	} catch (Exception e) {
	    response = ResponseType.createErrorResponse(400);
	    e.printStackTrace();
	}

	return response;
    }

    private String databaseProcessing(User user, Gson json) {
	String response = null;
	User proccesedUser = new User();

	// Обработване на заявката
	try {
	    Session session = DatabaseConnection.getSessionFactory().openSession();

	    CriteriaBuilder builder = session.getCriteriaBuilder();
	    CriteriaQuery<User> query = builder.createQuery(User.class);
	    Root<User> root = query.from(User.class);

	    List<Predicate> predicates = new ArrayList<Predicate>();
	    predicates.add(builder.equal(root.get("ID"), user.getID()));

	    query.select(root).where(predicates.toArray(new Predicate[] {}));

	    // Изпълняване на заявката към базата данни
	    List<User> users = session.createQuery(query).getResultList();

	    if (users.size() != 0) // Има намерен запис
		proccesedUser = users.get(0);

	} catch (Exception e) {
	    response = ResponseType.createErrorResponse(570);
	    e.printStackTrace();
	}

	// Подготвяне на отговор
	try {
	    proccesedUser.setRequestType(user.getRequestType());
	    response = json.toJson(proccesedUser);
	} catch (Exception ex) {
	    response = ResponseType.createErrorResponse(571);
	    ex.printStackTrace();
	}

	return response;
    }

    @Override
    public JsonElement serialize(User user, Type typeOfUser, JsonSerializationContext context) {
	JsonObject jsonObject = new JsonObject();
	JsonArray jsonArray = new JsonArray();

	jsonObject.addProperty("requestType", RequestType.USER_CHATS);

	if (user.getChats().size() != 0) {
	    // Вземаме всички чатове на потребителя
	    // Вземаме за всеки чат имената на потребителите и формираме името на чата
	    for (Chat chat : user.getChats()) {
		String chatName = "";
		List<User> chatUsers = chat.getUsers();
		long chatUsersSize = chatUsers.size();

		for (int index = 0; index < chatUsersSize; index++) {
		    User chatUser = chatUsers.get(index);

		    if (chatUser.getID() == user.getID())
			continue;

		    chatName += chatUsers.get(index).getName();

		    if (index != chatUsersSize - 1)
			chatName += ", ";
		}

		JsonObject jsonChat = new JsonObject();
		jsonChat.addProperty("chatID", chat.getID());
		jsonChat.addProperty("chatName", chatName);

		jsonArray.add(jsonChat);
	    }
	}

	jsonObject.add("chats", jsonArray);

	return jsonObject;
    }

    @Override
    public User deserialize(JsonElement jsonElement, Type typeOfT, JsonDeserializationContext context)
	    throws JsonParseException {
	JsonObject jsonObject = jsonElement.getAsJsonObject();

	User user = new User();
	user.setRequestType(jsonObject.get("requestType").getAsInt());
	user.setID(jsonObject.get("userID").getAsLong());

	return user;
    }
}

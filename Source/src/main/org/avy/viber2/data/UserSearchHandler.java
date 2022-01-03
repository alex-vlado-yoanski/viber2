package org.avy.viber2.data;

import java.lang.reflect.Type;
import java.util.*;

import javax.persistence.criteria.*;

import org.avy.viber2.database.DatabaseConnection;
import org.avy.viber2.tables.mapping.User;
import org.hibernate.Session;

import com.google.gson.*;

public class UserSearchHandler implements IDataHandler<UserSearch> {
    public UserSearchHandler() {
    }

    @Override
    public String process(String request) {
	String response = null;

	try {
	    // Подготвяне на json обект
	    GsonBuilder gsonBuilder = new GsonBuilder();
	    gsonBuilder.registerTypeAdapter(UserSearch.class, new UserSearchHandler());
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
	    Session session = DatabaseConnection.getSessionFactory().openSession();
	    CriteriaBuilder builder = session.getCriteriaBuilder();
	    CriteriaQuery<User> query = builder.createQuery(User.class);
	    Root<User> root = query.from(User.class);
	    List<Predicate> predicates = new ArrayList<Predicate>();

	    predicates.add(builder.like(root.get("name"), ("%" + userSearch.getSearchPhrase() + "%")));
	    query.select(root).where(predicates.toArray(new Predicate[] {}));
	    proccesedUsers.setUsers(session.createQuery(query).getResultList());
	} catch (Exception e) {
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
	user.setSearchPhrase(jsonObject.get("phrase").getAsString());

	return user;
    }
}

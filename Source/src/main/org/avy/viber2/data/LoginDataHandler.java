package org.avy.viber2.data;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.*;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.avy.viber2.database.DatabaseConnection;
import org.avy.viber2.tables.mapping.*;
import org.hibernate.Session;

import com.google.gson.*;

public class LoginDataHandler implements IDataHandler<User> {    
    public LoginDataHandler() {
    }

    @Override
    public String process(String request) {
	String response = null;

	try {
	    // Подготвяне на json обект
	    GsonBuilder gsonBuilder = new GsonBuilder();
	    gsonBuilder.registerTypeAdapter(User.class, new LoginDataHandler());
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
	    predicates.add(builder.equal(root.get("name"), user.getName()));

	    if (user.getRequestType() == RequestType.LOGIN_CREDENTIALS) {
		predicates.add(builder.equal(root.get("password"), user.getPassword()));
	    }
	    query.select(root).where(predicates.toArray(new Predicate[] {}));

	    // Изпълняване на заявката към базата данни
	    List<User> users = session.createQuery(query).getResultList();

	    boolean hasRecord = false;
	    if (users.size() != 0)
		hasRecord = true;

	    if (user.getRequestType() == RequestType.LOGIN_CREDENTIALS) {
		if (hasRecord)
		    proccesedUser = users.get(0);
	    } else if (user.getRequestType() == RequestType.SIGN_IN && !hasRecord) {
		proccesedUser = user; // налага се, за да може да върнем правилният код за грешка

		// Записване на потребителя в базата данни
		session.beginTransaction();
		session.save(proccesedUser); // връща генерираното ID
		session.getTransaction().commit();
	    }
	    
	    session.close();
	} catch (Exception e) {
	    response = ResponseType.createErrorResponse(570);
	    e.printStackTrace();
	}

	// Подготвяне на отговор
	try {
	    proccesedUser.setRequestType(user.getRequestType());
	    response = json.toJson(proccesedUser);
	} catch (Exception e) {
	    response = ResponseType.createErrorResponse(571);
	    e.printStackTrace();
	}

	return response;
    }
    
    @Override
    public JsonElement serialize(User user, Type typeOfUser, JsonSerializationContext context) {
	JsonObject jsonObject = new JsonObject();

	jsonObject.addProperty("requestType", user.getRequestType());
	jsonObject.addProperty("id", user.getID());

	return jsonObject;
    }

    @Override
    public User deserialize(JsonElement jsonElement, Type typeOfT, JsonDeserializationContext context)
	    throws JsonParseException {
	JsonObject jsonObject = jsonElement.getAsJsonObject();

	User user = new User();
	user.setRequestType(jsonObject.get("requestType").getAsInt());
	user.setName(jsonObject.get("name").getAsString());
	user.setPassword(jsonObject.get("password").getAsString());

	return user;
    }

}

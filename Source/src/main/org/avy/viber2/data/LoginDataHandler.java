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
    	    User userCredentials = json.fromJson(request, User.class);
    	
    	    // Обработване на заявката
    	    Session session = DatabaseConnection.getSessionFactory().openSession();
    	
    	    CriteriaBuilder builder = session.getCriteriaBuilder();
    	    CriteriaQuery<User> query = builder.createQuery(User.class);
    	    Root<User> root = query.from(User.class);
    
    	    List<Predicate> predicates = new ArrayList<Predicate>();
    	    predicates.add(builder.equal(root.get("name"), userCredentials.getName()));
    	    predicates.add(builder.equal(root.get("password"), userCredentials.getPassword()));
    	
    	    query.select(root).where(predicates.toArray(new Predicate[] {}));
    	
    	    // Изпълняване на заявката към базата данни
    	    List<User> users = session.createQuery(query).getResultList();
    	    
    	    // Подготвяне на отговор
    	    User user = new User();
    	    
    	    if (users.size() != 0) // Има намерен запис
    		user = users.get(0);    	    
    	   
    	    response = json.toJson(user);
    	    
	} catch (Exception e) {
	    e.printStackTrace();
	}
	
	return response;
    }
    
    @Override
    public JsonElement serialize(User user, Type typeOfUser, JsonSerializationContext context) {
	JsonObject jsonObject = new JsonObject();

	jsonObject.addProperty("id", user.getID());

	return jsonObject;
    }

    @Override
    public User deserialize(JsonElement jsonElement, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
	JsonObject jsonObject = jsonElement.getAsJsonObject();

	User user = new User();
	user.setName(jsonObject.get("name").getAsString());
	user.setPassword(jsonObject.get("password").getAsString());

	return user;
    }
    
}
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
	    Session session = DatabaseConnection.getSessionFactory().openSession();
	    CriteriaBuilder builder = session.getCriteriaBuilder();
	    CriteriaQuery<User> query = builder.createQuery(User.class);
	    Root<User> root = query.from(User.class);
	    List<Predicate> predicates = new ArrayList<Predicate>();

	    // Търсене на потребители по фраза
	    predicates.add(builder.like(root.get("name"), ("%" + userSearch.getSearchingPhrase() + "%")));
	    query.select(root).where(predicates.toArray(new Predicate[] {}));
	    List<User> foundUsers = session.createQuery(query).getResultList();

	    // Филтриране на резултата
	    if (foundUsers.size() != 0) {
		// Търсене на потребителя, който търси
		predicates.clear();
		predicates.add(builder.equal(root.get("ID"), userSearch.getSearchingUser().getID()));
		query.select(root).where(predicates.toArray(new Predicate[] {}));
		List<User> searchUsers = session.createQuery(query).getResultList();

		if (searchUsers.size() != 0) {
		    User searchUser = searchUsers.get(0);

		    List<UserInvitation> searchUserSentInvitations = searchUser.getSendInvitations();
		    List<UserInvitation> searchUserReceivedInvitations = searchUser.getSendInvitations();

		    for (User foundUser : foundUsers) {
			// Ако потребителя е този, който търси, го пропускаме
			if (foundUser.getID() == userSearch.getSearchingUser().getID())
			    continue;

			boolean bSkip = false;

			// Ако потребителят е изпатил покана към намереният, го пропускаме
			for (UserInvitation sentInvitation : searchUserSentInvitations) {
			    if (sentInvitation.getSender().getID() == foundUser.getID()) {
				bSkip = true;
				break;
			    }
			}

			if (bSkip)
			    continue;

			// Ако към потребителят е изпатил покана към намереният, го пропускаме
			for (UserInvitation receivedInvitation : searchUserReceivedInvitations) {
			    if (receivedInvitation.getReceiver().getID() == foundUser.getID()) {
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
	user.setSearchingPhrase(jsonObject.get("phrase").getAsString());
	user.getSearchingUser().setID(jsonObject.get("userID").getAsLong());

	return user;
    }
}

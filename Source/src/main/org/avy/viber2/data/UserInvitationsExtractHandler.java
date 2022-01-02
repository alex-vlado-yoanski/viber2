package org.avy.viber2.data;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.*;

import org.avy.viber2.database.DatabaseConnection;
import org.avy.viber2.tables.mapping.User;
import org.avy.viber2.tables.mapping.UserInvitation;
import org.hibernate.Session;

import com.google.gson.*;

public class UserInvitationsExtractHandler implements IDataHandler<User> {
    public UserInvitationsExtractHandler() {
    }

    @Override
    public String process(String request) {
	String response = null;

	try {
	    // Подготвяне на json обект
	    GsonBuilder gsonBuilder = new GsonBuilder();
	    gsonBuilder.registerTypeAdapter(User.class, new UserInvitationsExtractHandler());
	    gsonBuilder.setPrettyPrinting();

	    // Създаване на json обект
	    Gson json = gsonBuilder.create();

	    // Изчитане на заявката
	    User user = json.fromJson(request, User.class);

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

	// Подготвяне на отговор
	try {
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

	jsonObject.addProperty("requestType", RequestType.USER_INVITATIONS_EXTRACT);

	for (UserInvitation userInvitation : user.getSendInvitations()) {
	    // Покана
	    JsonObject invitation = new JsonObject();
	    invitation.addProperty("invitationID", userInvitation.getID());
	    invitation.addProperty("status", userInvitation.getStatus());
	    jsonArray.add(invitation);

	    // Изпратил поканата
	    JsonObject jsonSender = new JsonObject();
	    User sender = userInvitation.getSender();
	    jsonSender.addProperty("senderID", sender.getID());
	    jsonSender.addProperty("name", sender.getName());
	    invitation.add("sender", jsonSender);

	    // Получател на поканата
	    JsonObject jsonReceiver = new JsonObject();
	    User receiver = userInvitation.getReceiver();
	    jsonReceiver.addProperty("receiverID", receiver.getID());
	    jsonReceiver.addProperty("name", receiver.getName());
	    invitation.add("reciever", jsonReceiver);
	}

	jsonObject.add("invitations", jsonArray);

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

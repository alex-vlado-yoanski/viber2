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

public class UserInvitationsExtractorHandler implements IDataHandler<User> {
    public UserInvitationsExtractorHandler() {
    }

    @Override
    public String process(String request) {
	String response = null;
	try {
	    // Подготвяне на json обект
	    GsonBuilder gsonBuilder = new GsonBuilder();
	    gsonBuilder.registerTypeAdapter(User.class, new UserInvitationsExtractorHandler());
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
	JsonObject TopLevelJSON = new JsonObject();
	JsonObject SecondLevelJSON = new JsonObject();
	JsonObject ThirdLevelJSON = new JsonObject();
	JsonArray JSONArray = new JsonArray();

	TopLevelJSON.addProperty("requestType", RequestType.USER_INVITATIONS_EXTRACT);

	for (UserInvitation userInvitation : user.getSendInvitations()) {
	    int InvitationID = userInvitation.getID();
	    int Status = userInvitation.getStatus();

	    long SenderID = userInvitation.getSender().getID();
	    long ReceiverID = userInvitation.getReceiver().getID();

	    String SenderName = userInvitation.getSender().getName();
	    String ReceiverName = userInvitation.getReceiver().getName();

	    // Второ ниво
	    SecondLevelJSON.addProperty("invitationID", InvitationID);
	    SecondLevelJSON.addProperty("status", Status);
	    JSONArray.add(SecondLevelJSON);

	    // Трето ниво - изпратил покана
	    ThirdLevelJSON.addProperty("senderID", SenderID);
	    ThirdLevelJSON.addProperty("name", SenderName);
	    SecondLevelJSON.add("sender", ThirdLevelJSON);

	    // Присвояваме нов JSON обект, спестяваме създаване на един обект
	    ThirdLevelJSON = new JsonObject();

	    // Трето ниво - получател покана
	    ThirdLevelJSON.addProperty("receiverID", ReceiverID);
	    ThirdLevelJSON.addProperty("name", ReceiverName);
	    SecondLevelJSON.add("reciever", ThirdLevelJSON);
	}

	// Първо ниво
	TopLevelJSON.add("invitations", JSONArray);

	return TopLevelJSON;
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

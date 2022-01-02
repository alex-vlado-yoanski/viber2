package org.avy.viber2.data;

import java.lang.reflect.Type;
import java.sql.Timestamp;

import org.avy.viber2.database.DatabaseConnection;
import org.avy.viber2.tables.mapping.*;
import org.hibernate.Session;
import com.google.gson.*;

public class UserInvitationNewHandler implements IDataHandler<UserInvitation> {

    public UserInvitationNewHandler() {
    }

    @Override
    public String process(String request) {
	String response = null;

	try {
	    // Подготвяне на json обект
	    GsonBuilder gsonBuilder = new GsonBuilder();
	    gsonBuilder.registerTypeAdapter(UserInvitation.class, new UserInvitationNewHandler());
	    gsonBuilder.setPrettyPrinting();

	    // Създаване на json обект
	    Gson json = gsonBuilder.create();

	    // Изчитане на заявката
	    UserInvitation invitation = json.fromJson(request, UserInvitation.class);

	    // Обработване на заявката от базата данни
	    response = databaseProcessing(invitation, json);
	} catch (Exception e) {
	    response = ResponseType.createErrorResponse(400);
	    e.printStackTrace();
	}

	return response;
    }

    private String databaseProcessing(UserInvitation invitation, Gson json) {
	String response = null;

	UserInvitation processedInvitation = new UserInvitation();
	processedInvitation.setSender(invitation.getSender());
	processedInvitation.setReceiver(invitation.getReceiver());
	processedInvitation.setCreateDate(new Timestamp(System.currentTimeMillis()));

	try {
	    Session session = DatabaseConnection.getSessionFactory().openSession();

	    session.beginTransaction();
	    session.save(processedInvitation);
	    session.getTransaction().commit();
	} catch (Exception e) {
	    response = ResponseType.createErrorResponse(570);
	    e.printStackTrace();
	}

	// Подготвяне на отговор
	try {
	    processedInvitation.setRequestType(invitation.getRequestType());
	    response = json.toJson(processedInvitation);
	} catch (Exception e) {
	    response = ResponseType.createErrorResponse(571);
	    e.printStackTrace();
	}

	return response;
    }

    @Override
    public JsonElement serialize(UserInvitation invitation, Type typeOfUser, JsonSerializationContext context) {
	JsonObject jsonObject = new JsonObject();

	jsonObject.addProperty("requestType", invitation.getRequestType());

	return jsonObject;
    }

    @Override
    public UserInvitation deserialize(JsonElement jsonElement, Type typeOfT, JsonDeserializationContext context)
	    throws JsonParseException {
	JsonObject jsonObject = jsonElement.getAsJsonObject();

	UserInvitation invitation = new UserInvitation();
	invitation.setRequestType(jsonObject.get("requestType").getAsInt());
	invitation.getSender().setID(jsonObject.get("sender").getAsLong());
	invitation.getReceiver().setID(jsonObject.get("receiver").getAsLong());

	return invitation;
    }

}

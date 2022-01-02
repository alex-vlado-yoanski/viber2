package org.avy.viber2.data;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.criteria.*;
import org.avy.viber2.database.DatabaseConnection;
import org.avy.viber2.tables.mapping.*;
import org.hibernate.Session;
import com.google.gson.*;

public class UserInvitationModifyHandler implements IDataHandler<UserInvitation> {

    public UserInvitationModifyHandler() {
    }

    @Override
    public String process(String request) {
	String response = null;

	try {
	    // Подготвяне на json обект
	    GsonBuilder gsonBuilder = new GsonBuilder();
	    gsonBuilder.registerTypeAdapter(UserInvitation.class, new UserInvitationModifyHandler());
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

	try {
	    Session invitationSession = DatabaseConnection.getSessionFactory().openSession();
	    CriteriaBuilder invitationBuilder = invitationSession.getCriteriaBuilder();
	    CriteriaQuery<UserInvitation> invitationQuery = invitationBuilder.createQuery(UserInvitation.class);
	    Root<UserInvitation> invitationsRoot = invitationQuery.from(UserInvitation.class);
	    List<Predicate> invitationPredicates = new ArrayList<Predicate>();

	    invitationPredicates.add(invitationBuilder.equal(invitationsRoot.get("ID"), invitation.getID()));
	    invitationQuery.select(invitationsRoot).where(invitationPredicates.toArray(new Predicate[] {}));
	    List<UserInvitation> invitations = invitationSession.createQuery(invitationQuery).getResultList();

	    // Ако намерим запис, редактираме го в базата данни
	    if (invitations.size() != 0) {
		processedInvitation = invitations.get(0);
		processedInvitation.setStatus(invitation.getStatus());

		// Сменяме статуса на поканата
		invitationSession.beginTransaction();
		invitationSession.update(processedInvitation);

		// Ако статусът е променен на "ПРИЕТА", създаваме чат между изпратилия и
		// получилия
		if (processedInvitation.getStatus() == UserInvitationStatus.ACCEPTED) {
		    Session userSession = DatabaseConnection.getSessionFactory().openSession();
		    CriteriaBuilder userBuilder = userSession.getCriteriaBuilder();
		    CriteriaQuery<User> userQuery = userBuilder.createQuery(User.class);
		    Root<User> userRoot = userQuery.from(User.class);
		    List<Predicate> userPredicates = new ArrayList<Predicate>();

		    // Изчитане на данните за изпратил
		    User sender = new User();
		    userPredicates.add(userBuilder.equal(userRoot.get("ID"), processedInvitation.getSender().getID()));
		    userQuery.select(userRoot).where(userPredicates.toArray(new Predicate[] {}));
		    List<User> users = userSession.createQuery(userQuery).getResultList();

		    if (users.size() != 0)
			sender = users.get(0);

		    users.clear();
		    userPredicates.clear();

		    // Изчитане на данните за получил
		    User receiver = new User();
		    userPredicates
			    .add(userBuilder.equal(userRoot.get("ID"), processedInvitation.getReceiver().getID()));
		    userQuery.select(userRoot).where(userPredicates.toArray(new Predicate[] {}));
		    users = userSession.createQuery(userQuery).getResultList();

		    if (users.size() != 0)
			receiver = users.get(0);

		    userSession.close();

		    // Създаване на чат
		    Chat chat = new Chat();
		    chat.setUser(sender);
		    chat.setUser(receiver);

		    Session userChatSession = DatabaseConnection.getSessionFactory().openSession();
		    userChatSession.beginTransaction();
		    userChatSession.save(chat);
		    userChatSession.getTransaction().commit();
		    userChatSession.close();
		}

		invitationSession.getTransaction().commit();
		invitationSession.close();
	    }
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
	jsonObject.addProperty("invitationID  ", invitation.getID());
	jsonObject.addProperty("status", invitation.getStatus());

	return jsonObject;
    }

    @Override
    public UserInvitation deserialize(JsonElement jsonElement, Type typeOfT, JsonDeserializationContext context)
	    throws JsonParseException {
	JsonObject jsonObject = jsonElement.getAsJsonObject();

	UserInvitation invitation = new UserInvitation();
	invitation.setRequestType(jsonObject.get("requestType").getAsInt());
	invitation.setID(jsonObject.get("invitationID").getAsLong());
	invitation.setStatus(jsonObject.get("status").getAsInt());

	return invitation;
    }

}

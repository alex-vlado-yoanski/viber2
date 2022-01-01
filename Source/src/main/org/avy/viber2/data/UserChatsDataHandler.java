package org.avy.viber2.data;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.avy.viber2.database.DatabaseConnection;
import org.avy.viber2.tables.mapping.Chat;
import org.avy.viber2.tables.mapping.Message;
import org.avy.viber2.tables.mapping.User;
import org.hibernate.Session;

import com.google.gson.*;

public class UserChatsDataHandler implements IDataHandler<User> {
	private long CurrentUserID = 0;

	/**
	 * tl;dr SELECT user_chats.id,users.name,messages.text FROM user_chats JOIN users ON user_id = users.id JOIN messages ON chat_id = messages.id WHERE
	 * messages.sent_by = 2;
	 */

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

			// Подготвяне на отговор
			User proccesedUser = new User();

			if (users.size() != 0) // Има намерен запис
				proccesedUser = users.get(0);

			response = json.toJson(proccesedUser);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return response;
	}

	@Override
	public JsonElement serialize(User user, Type typeOfUser, JsonSerializationContext context) {
		JsonObject jsonObject = new JsonObject();
		JsonArray jsonArray = new JsonArray();

		jsonObject.addProperty("requestType", RequestType.USER_CHATS);

		for (Chat chat : user.getChats()) {
			List<Message> messages = chat.getMessages();
			if (messages.size() != 0)
				jsonArray.add(messages.get(0).getSentBy().getName());
		}

		jsonObject.add("chatName", jsonArray);

		return jsonObject;
	}

	@Override
	public User deserialize(JsonElement jsonElement, Type typeOfT, JsonDeserializationContext context) {
		JsonObject jsonObject = jsonElement.getAsJsonObject();

		User user = new User();
		user.setRequestType(jsonObject.get("requestType").getAsInt());
		user.setID(jsonObject.get("userID").getAsLong());

		return user;
	}
}

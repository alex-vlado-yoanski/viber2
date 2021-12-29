package org.avy.viber2.tables.mapping;

import java.lang.reflect.Type;

import com.google.gson.*;

public class UserCredentials implements JsonSerializer<User>, JsonDeserializer<User> {
    @Override
    public JsonElement serialize(User user, Type typeOfUser, JsonSerializationContext context) {
	JsonObject jsonUser = new JsonObject();

	jsonUser.addProperty("id", user.getID());
	jsonUser.addProperty("name", user.getName());
	jsonUser.addProperty("password", user.getPassword());

	return jsonUser;
    }

    @Override
    public User deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
	    throws JsonParseException {
	JsonObject jsonObject = json.getAsJsonObject();

	User userFromJSON = new User();
	userFromJSON.setID(jsonObject.get("id").getAsInt());
	userFromJSON.setName(jsonObject.get("name").getAsString());
	userFromJSON.setPassword(jsonObject.get("password").getAsString());

	return userFromJSON;
    }
}

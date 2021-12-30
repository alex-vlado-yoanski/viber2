package org.avy.viber2.data;

import java.lang.reflect.Type;
import org.avy.viber2.tables.mapping.*;
import com.google.gson.*;

public class LoginDataHandler implements JsonSerializer<User>, JsonDeserializer<User> {
    public LoginDataHandler() {
	
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

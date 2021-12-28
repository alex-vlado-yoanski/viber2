package org.avy.viber2.data;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;

import org.avy.viber2.database.DatabaseConnection;
import org.avy.viber2.tables.mapping.User;
import org.hibernate.Session;

import com.google.gson.Gson;

public class Login {
    static boolean IsUserAuthenticationCorrect(Object FromJSON, Session session) {
	// Изчитане на потребителите от базата данни
	CriteriaBuilder builder = session.getCriteriaBuilder();
	CriteriaQuery<User> criteriaQuery = builder.createQuery(User.class);
	criteriaQuery.from(User.class);

	List<User> users = session.createQuery(criteriaQuery).getResultList();

	// Сравняване на 
	/*for (User user : users) {
	    if (user.getName().equals(((User) FromJSON).getName())) {
		if (u.getPassword().equals(((User) FromJSON).getPassword())) {
		    return true;
		}
	    }
	}
*/
	return true;
    }

    static Object StringToObject(StringBuffer dataString) {
	System.out.println(dataString); // За дебъгване премахни
	Gson gson = new Gson();
	User user2 = gson.fromJson(dataString.toString(), User.class); // JSON в клас

	return user2;
    }

}
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
    static private List<User> DB(Session session) {
	// JSON test -- TO DO: Да се махне след сокетите
	CriteriaBuilder builder = session.getCriteriaBuilder();
	CriteriaQuery<User> criteriaQuery2 = builder.createQuery(User.class);
	criteriaQuery2.from(User.class);

	List<User> asd = session.createQuery(criteriaQuery2).getResultList();

	return asd;
    }

    static boolean AuthenticateUser(Object FromJSON, Session session) {
	List<User> usr = DB(session);

	for (User u : usr) {
	    if (u.getName().equals(((User) FromJSON).getName())) {
		if (u.getPassword().equals(((User) FromJSON).getPassword())) {
		    return true;
		}
	    }
	}

	return false;
    }

    static Object StringToObject(StringBuffer dataString) {
	System.out.println(dataString); // За дебъгване премахни
	Gson gson = new Gson();
	User user2 = gson.fromJson(dataString.toString(), User.class); // JSON в клас

	return user2;
    }

}
package org.avy.viber2;

import java.io.PrintWriter;
import java.util.List;

import org.avy.viber2.database.*;
import org.avy.viber2.tables.mapping.*;
import org.hibernate.*;

import com.google.gson.*;

import javax.persistence.criteria.*;

public class Main {
    public static void main(String[] args) {
	/*
	Thread Serv = null;
	// Пусни сървър
	int port = -1;
	try {
	    if (args.length >= 1) {
		port = Integer.parseInt(args[0]); // От аргументи
	    } else {
		port = 8081;
	    }
	    Serv = new Thread(new Server(port)); // Хардкоднато на 8081
	    Serv.setName("Server");
	    Serv.setDaemon(true); // Сървърт е демон ^_^
	    Serv.start(); // Стартирай сървъра
	} catch (Exception e) {
	    System.out.print("Failed to start server"); // TODO По-разбираема гершка

	    return; // Няма какво повече да се прави
	}

	// Пусни база данни
	// Database(); // TODO В нишка

	// Чакаме да приключат работа нишките
	try {
	    Serv.join();

	    // database.join()?
	} catch (InterruptedException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
	*/
	
	// Hibernate test -- TO DO: Да се махне след сокетите
	Session session = DatabaseConnection.getSessionFactory().openSession();
	
	CriteriaBuilder builder = session.getCriteriaBuilder();
	CriteriaQuery<Message> criteriaQuery = builder.createQuery(Message.class);
	criteriaQuery.from(Message.class);
	
	List<Message> tests = session.createQuery(criteriaQuery).getResultList();

	for (Message test : tests) {
	    	System.out.println(test.getCreateDate());
	}
	
	// JSON test -- TO DO: Да се махне след сокетите
	CriteriaBuilder builder2 = session.getCriteriaBuilder();
	CriteriaQuery<User> criteriaQuery2 = builder.createQuery(User.class);
	criteriaQuery2.from(User.class);
	
	List<User> asd = session.createQuery(criteriaQuery2).getResultList();
	
	User u = asd.get(0);
	
	// -> от тук
	Gson gson = new Gson();
	String json = gson.toJson(u);
	
	System.out.println(json);
	
	User user2 = gson.fromJson(json, User.class);

    }
}

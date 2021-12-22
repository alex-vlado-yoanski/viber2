package org.avy.viber2;

import java.util.List;

import org.avy.viber2.database.*;
import org.avy.viber2.tables.mapping.*;
import org.hibernate.*;
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
	
	
	Session session = DatabaseConnection.getSessionFactory().openSession();
	
	CriteriaBuilder builder = session.getCriteriaBuilder();
	CriteriaQuery<Users> criteriaQuery = builder.createQuery(Users.class);
	criteriaQuery.from(Users.class);
	
	List<Users> users = session.createQuery(criteriaQuery).getResultList();

	for (Users user : users) {
	    	System.out.println(user.getID());
		System.out.println(user.getName());
		System.out.println(user.getPassword());
	}
    }
}

package org.avy.viber2;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;

import org.avy.viber2.data.LoginDataHandler;
import org.avy.viber2.data.SocketConnection;
import org.avy.viber2.database.DatabaseConnection;
import org.avy.viber2.tables.mapping.User;
import org.hibernate.Session;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class Main {
      
    
    public static void main(String[] args) throws IOException {	
	
	
	//SocketConnection socketConnection = new SocketConnection();
	try {
	    //socketConnection.runServer(8080);
	    SocketConnection socketConnection = new SocketConnection();
	    Session session = DatabaseConnection.getSessionFactory().openSession();
	    ServerSocket serverSocket = new ServerSocket(8080);

	    while (true) {
		Socket socket = serverSocket.accept();
		
		String content;
		
		StringBuilder sb = new StringBuilder();
		try (BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
		    String line;
		    while ( (line = br.readLine()) != null) {
		        sb.append(line).append(System.lineSeparator());
		    }
		    content = sb.toString();
		    System.out.println(content);
		}
		
		Gson gson = new GsonBuilder().registerTypeAdapter(User.class, new LoginDataHandler()).setPrettyPrinting().create();
		User user = gson.fromJson(content, User.class);
		
		///
		System.out.println("\n\nUser id: " + user.getID() + " name: " + user.getName() + " password: " + user.getPassword());
		///
		
		String json = gson.toJson(user);
		System.out.println(json);
		
		try (OutputStreamWriter out = new OutputStreamWriter( socket.getOutputStream(), StandardCharsets.UTF_8)) 
		{ 
		    out.write(json.toString());
		}
	    }
	} catch (IOException e) {
	   System.out.println("Most likely socket is taken. Try new one");
	   return;
	}
	
	
	
	
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
	//Session session = DatabaseConnection.getSessionFactory().openSession();
	/*
	CriteriaBuilder builder = session.getCriteriaBuilder();
	CriteriaQuery<User> criteriaQuery = builder.createQuery(User.class);
	criteriaQuery.from(User.class);
	
	List<User> tests = session.createQuery(criteriaQuery).getResultList();

	for (User test : tests) {
	    	System.out.println(test.getID() + " " + test.getName() + " " + test.getPassword());
	}
	*/
	// JSON test -- TO DO: Да се махне след сокетите
	/*CriteriaBuilder builder2 = session.getCriteriaBuilder();
	CriteriaQuery<User> criteriaQuery2 = builder2.createQuery(User.class);
	criteriaQuery2.from(User.class);
	
	List<User> asd = session.createQuery(criteriaQuery2).getResultList();
	User u;
	try {
	      u = asd.get(1);
	} catch (IndexOutOfBoundsException e) {
	   System.out.println("Index out of bounds!!!");
	   return;
	}
	
	System.out.println(u.getID() + " " + u.getName() + " " + u.getPassword());
	
	// -> от тук
	Gson gson = new GsonBuilder().registerTypeAdapter(User.class, new UserCredentials()).setPrettyPrinting().create();
	String json = gson.toJson(u);
	System.out.println(json);
	
	User user2 = gson.fromJson(json, User.class);
	System.out.println("\n\nUser id: " + user2.getID() + " name: " + user2.getName() + " password: " + user2.getPassword());
	*/
    }



}
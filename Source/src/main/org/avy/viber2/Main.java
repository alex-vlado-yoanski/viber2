package org.avy.viber2;

import java.util.List;

import org.avy.viber2.Database.HiberConnect;
//import org.avy.viber2.Database.Users;
import org.avy.viber2.Sockets.Server;

public class Main {
    private static HiberConnect DB;

    private static void Database() {
	// Users U = new Users();

	DB = new HiberConnect();
	List<?> L = null;
	try {
	    L = DB.SelectItem("U.id", "Users U");
	} catch (Exception e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	    return;
	}

	System.out.println(L.toString());

	// U.setId(2);
	// U.setUname("Nemo");
	// U.setPassw("blublub");
	// DB.DeleteItem("users", "name", "Nemo");
	// DB.CommitAndSaveSession(U);
	// L = DB.SelectItem("U.id", "Users U");
	// System.out.println(L.toString());

	DB.exit();
    }

    public static void main(String[] args) {
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
	Database(); // TODO В нишка

	// Чакаме да приключат работа нишките
	try {
	    Serv.join();

	    // database.join()?
	} catch (InterruptedException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
    }
}

package org.avy.viber2;

import java.util.List;

import org.avy.viber2.Database.HiberConnect;
import org.avy.viber2.Database.Users;
import org.avy.viber2.Sockets.Server;

public class Main {
    public static void main(String[] args) throws Exception {
	 // Стартира се сървъра на порт 8081
	 Server Srv = new Server(8081);
	 Srv.StartServer();
	 Srv.CloseServer();

	Users U = new Users();

	HiberConnect DB = new HiberConnect();

	List L = DB.SelectItem("U.id", "Users U");

	System.out.println(L.toString());

	U.setId(2);

	/*
	 * U.setUname("Nemo"); U.setPassw("blublub");
	 */
	DB.DeleteItem("users", "name", "Nemo");
	DB.CommitAndSaveSession(U);
	L = DB.SelectItem("U.id", "Users U");
	System.out.println(L.toString());

	DB.CloseDB();
    }
}

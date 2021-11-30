/*
 * Временен клас, докато не измислим нещо по-добро за тестване на идеи и пр.
 * */

package org.avy.viber2.Toybox;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import org.avy.viber2.HibernateConnect.HiberConnect;
import org.avy.viber2.Login.Login;

public class Toybox {
    public void VladoArgon2() {
	System.out.println("*** TEST Argon2 ***");
	Login L = new Login();

	String HP = L.HashNewPassword("uncommon"),
		CMP_HASH = "$argon2d$v=19$m=65536,t=24,p=2$8sxtVVVcM/xkxmD8UuwnLQ$6H0NF2470zg6upt61Hu//RzGpcvjgO9bM6PQ4g2P+G8";

	System.out.println("Dis be of workings? If yes, here ur hash:\n" + HP);

	L.VerifyPass(CMP_HASH, "common".toCharArray());
	L.VerifyPass(CMP_HASH, "AlaBalaKonskaPanica".toCharArray());
    }

    public void VladoHibernate() throws java.sql.SQLException {
	System.out.println("*** TEST Hibernate ***");
	HiberConnect HC = new HiberConnect();
	System.out.println("Opening DB");
	HC.Setup();

	System.out.println("Closing DB");
	HC.CloseDB();
    }

    public void VladoTomcat() throws Exception {
	System.out.println("*** TEST Tomcat ***");
	ServerSocket serverSocket = null;
	try {
	    // Слушаме на порт 8082
	    serverSocket = new ServerSocket(8082);
	} catch (IOException ioe) {
	    ioe.printStackTrace();
	    return;
	}
	// Говорим с клиента
	Socket clientSocket = serverSocket.accept();
	// За връзка към света
	PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
	// За връзка от света
	BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
	// Четем какво ни е казал света
	String greeting = in.readLine();
	// Ако сме разбрали правилно света, вс е 6; иначе не минава JUnit
	if ("hello server".equals(greeting)) {
	    out.println("hello client");
	    System.out.println("Client said hi!");
	} else {
	    out.println("unrecognised greeting");
	    System.out.println("No clients :(");
	}
	// не забравяй да прекратиш вързката
	serverSocket.close();
	System.out.println("*** Bye ***");
    }
}

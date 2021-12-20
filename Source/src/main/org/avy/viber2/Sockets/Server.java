package org.avy.viber2.Sockets;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    static private int SERVER_PORT;
    private ServerSocket SSocket; // Сокет за 'слушане' на сървъра
    private DataInputStream IStream;
    private DataOutputStream OStream;
    private Socket ClientSocket; // Сокет за 'говорене' с клиента
    private String Input;

    /**
     * Инициализира сървъра
     * 
     * @param Порт на който слушаме 1024 - 49151 Виж RFC 6335
     */
    public Server(int P) throws Exception {
	if (P < 1024 || P > 49151) {
	    System.out.println("Server tried to start on invalid socket!");
	    throw new Exception("Port out of range");
	}
	SERVER_PORT = P;
    }

    /**
     * Стартриаме сървъра ни тук, задаваме портове и чакаме клиента да ни 'говори'
     */
    public void StartServer() {
	try {
	    SSocket = new ServerSocket(SERVER_PORT);
	    ClientSocket = SSocket.accept();
	    // Казваме че сме стартирали сървъра, след като сме го стартирали ^_^
	    System.out.println("Server has started");
	} catch (IOException e) {
	    System.out.println("IOException  " + e.getMessage());
	    e.printStackTrace();
	} catch (SecurityException se) {
	    System.out.println("SecurityExcepion " + se.getMessage());
	    se.printStackTrace();
	}
    }

    /**
     * Сървъра слуша за клиентска връзка
     */
    public void ServerRecieve() throws IOException {
	IStream = new DataInputStream(ClientSocket.getInputStream());
	BufferedReader IReader = new BufferedReader(new InputStreamReader(IStream));

	ClientSocket = SSocket.accept();

	this.Input = IReader.readLine();
    }

    /**
     * Сървъра изпраща съобщение към клиента
     */
    public void ServerTransmit() throws IOException {
	OStream = new DataOutputStream(ClientSocket.getOutputStream());
    }

    /**
     * Затваря съръва. Ако е затворен или никога не е бил стартиран, записва
     * грешката в лог и хвърля изключение
     */
    public void CloseServer() throws IOException {
	if (this.SSocket == null || SSocket.isClosed()) {
	    System.out.println("Server attempted to close, but is already closed or has never been started!");
	    throw new IOException("Server already closed or never started");
	} else
	    SSocket.close();
    }

    public String GetInputBuffer() {
	return this.Input;
    }
}

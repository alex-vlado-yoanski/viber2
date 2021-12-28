package org.avy.viber2.data;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

import org.avy.viber2.database.DatabaseConnection;
import org.avy.viber2.tables.mapping.User;
import org.hibernate.Session;

public class SocketConnection {
    protected ServerSocket serverSocket;
    protected static Socket socket;
    protected static DataOutputStream out;
    protected static Session session;

    protected StringBuffer ServerReadFromClient(int length, DataInputStream inputStream) {
	byte[] msgBuffer = new byte[length]; // Буфер за съобщението
	int bytesRead = 0; // Брой прочетени байта
	boolean hasBuffer = true; // Очакващ патент "Има-ли-още-данни" флаг

	// Правим стринг, не по-голям от очакваната дължина
	StringBuffer dataString = new StringBuffer(length);

	// цикъл, в който четем
	while (hasBuffer) {
	    int byteToRead;
	    try {
		byteToRead = inputStream.read(msgBuffer); // Четем в буфера
	    } catch (IOException e) {
		e.printStackTrace();
		return null;
	    }

	    bytesRead = byteToRead + bytesRead;

	    if (bytesRead <= length) {
		dataString.append(new String(msgBuffer, 0, byteToRead, StandardCharsets.UTF_8));
	    } else {
		dataString.append(new String(msgBuffer, 0, length - bytesRead + byteToRead, StandardCharsets.UTF_8));
	    }

	    // Ако прочетеното е >= дължината, край
	    if (dataString.length() >= length) {
		hasBuffer = false;
	    }
	}
	return dataString;
    }

    protected void SendToClient(String data) throws IOException {
	out = new DataOutputStream(socket.getOutputStream());
	int length = data.length();
	byte[] dataInBytes = data.getBytes(StandardCharsets.UTF_8);

	out.writeInt(length);
	out.write(dataInBytes);
    }

    public void runServer(int port) throws InterruptedException, IOException {
	
	Session session = DatabaseConnection.getSessionFactory().openSession();
	ServerSocket serverSocket = new ServerSocket(port);

	while (true) {
	    socket = serverSocket.accept();
	    
	    DataInputStream inputStream = new DataInputStream(socket.getInputStream());
	    String receivedMsg = inputStream.readUTF();
		    
	    /*char requestType = inputStream.readChar(); // Псевдо тип заявка
	    int length = inputStream.readInt(); // Дължина данни

	    // TODO други заявки (и методи)
	    switch (requestType) {
	    /* Заявка автентикация */
	    /*case 'A': {
		StringBuffer dataString = ServerReadFromClient(length, inputStream);
		if (dataString != null) {
		    User user2 = (User) Login.StringToObject(dataString);

		    System.out.println("name: " + user2.getName() + " password: " + user2.getPassword());

		    if (Login.IsUserAuthenticationCorrect(user2, session)) {
			System.out.println("AUTH_PERMITTED");
			Thread.sleep(5); // Изчакай момент
			SendToClient("AUTH_PERMITTED"); // Автентикация успешна
		    } else {
			System.out.println("INVALID_CREDS");
			SendToClient("INVALID_CREDS"); // Автентикация неуспешна
		    }
		}
	    }
		break;
	    /* Край заявка автентикация */

	    /* Заявка съобщение */
	   /* case 'M': {
		System.out.println("handling messaging");
	    }
		break;
	    /* Край заявка съобщение */

	    /* Заявка приятелство */
	    /*case 'F': {
		System.out.println("handling friendlist");
	    }
		break;
	    /* Край заявка приятелство :( */

	    // Празна или невалидна заявка
	   /* default:
		System.out.println("[No or invalid data recieved]");
	    }*/
	}
    }
}

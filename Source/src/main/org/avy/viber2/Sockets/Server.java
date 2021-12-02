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
    private PrintWriter out; // С това казваме X на клиента
    private BufferedReader in; // С това разбираме X от клиента

    public Server(int P) throws Exception {
	if (P < 0 || P > 65534) {
	    throw new Exception("Port out of range");
	}
	SERVER_PORT = P;
    }

    public void StartServer() throws Exception {
	SSocket = new ServerSocket(SERVER_PORT);
	ClientSocket = SSocket.accept();
	IStream = new DataInputStream(ClientSocket.getInputStream());
	OStream = new DataOutputStream(ClientSocket.getOutputStream());
	// ClientSocket = SSocket.accept();
    }

    public void SendMessage() throws IOException {
	// За връзка към света
	PrintWriter out = new PrintWriter(ClientSocket.getOutputStream(), true);
	// String resp = out.readLine(T);
    }

    public void CloseServer() throws IOException {
	if (!SSocket.isClosed())
	    SSocket.close();
	else
	    throw new IOException("Server already closed");
    }
}

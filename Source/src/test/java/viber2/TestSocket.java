package viber2;

import static org.junit.jupiter.api.Assertions.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import org.junit.jupiter.api.Test;

class TestSocket {
    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;

    public void startConnection(String ip, int port) throws IOException {
	clientSocket = new Socket(ip, port);
	out = new PrintWriter(clientSocket.getOutputStream(), true);
	in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
    }

    public String sendMessage(String msg) throws IOException {
	out.println(msg);
	String resp = in.readLine();
	return resp;
    }

    public void stopConnection() throws IOException {
	in.close();
	out.close();
	clientSocket.close();
    }

    @Test
    public void givenGreetingClient_whenServerRespondsWhenStarted_thenCorrect() {
	try {
	    startConnection("127.0.0.1", 8082);
	} catch (IOException e) {
	    e.printStackTrace();
	}
	String response = null;
	try {
	    response = sendMessage("hello server");
	    stopConnection();
	} catch (IOException e) {
	    e.printStackTrace();
	}
	assertEquals("hello client", response);
    }

}

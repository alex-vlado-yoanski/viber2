package org.avy.viber2.Sockets;

public class ClientHandler extends Server implements Runnable {

    public ClientHandler(int P) throws Exception {
	super(P);
    }

    @Override
    public void run() {
	super.StartServer();
    }
}

package org.avy.viber2;

import org.avy.viber2.Sockets.Server;

public class Main {
    public static void main(String[] args) throws Exception {
	// Стартира се сървъра на порт 8081
	Server Srv = new Server(8081);

	Srv.StartServer();
	Srv.CloseServer();
    }
}

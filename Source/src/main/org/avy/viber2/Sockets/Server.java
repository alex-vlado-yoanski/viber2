package org.avy.viber2.Sockets;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

public class Server extends Thread {
    private static final int MTU = 1500; // MTU също така размер буфер! LTE->1428 Ethernet->1500-9k 5G->1420
    private static int SERVER_PORT; // x.x.x.x:Port || example.com:Port
    private static ServerSocketChannel serverSocketChannel; // Сокета ни
    private static ByteBuffer buffer;
    private static SocketChannel socketChannel;

    /**
     * Конструктор на сървъра
     * 
     * @param P порт за слушане 1024 - 49151
     * 
     * @throws Exception порт извън позволения диапазон
     */
    public Server(int P) throws IllegalArgumentException {
	if (P < 1024 || P > 49151) {
	    System.out.println("Server tried to start on invalid socket!");
	    throw new IllegalArgumentException("Port out of range");
	}
	SERVER_PORT = P;
    }

    // главна нишка
    public void run() {
	System.out.print("Server is up\n\n");

	// Отваряме сокета
	try {
	    serverSocketChannel = ServerSocketChannel.open();
	    serverSocketChannel.configureBlocking(false);
	    serverSocketChannel.bind(new InetSocketAddress(SERVER_PORT));
	} catch (IOException e) {
	    e.printStackTrace();
	    return;
	}

	// Цикъл слушане на сървъра
	while (true) {
	    try {
		Thread.sleep(1); // anti-CPU hog
		socketChannel = serverSocketChannel.accept(); // non-blocking
		if (socketChannel != null) {
		    socketChannel.configureBlocking(false);
		    buffer = ByteBuffer.allocate(MTU);

		    // Тук говорим с клиента
		    while (true) {
			// TODO говори с клиента
			buffer.clear();
			int read = socketChannel.read(buffer); // non-blocking

			System.out.print(buffer);

			if (read < 0) {
			    break;
			}
			buffer.flip();
			socketChannel.write(buffer); // can be non-blocking
		    }
		    socketChannel.close();
		}
	    } catch (InterruptedException IE) {
		System.out.println(IE.getMessage());
		throw new UnsupportedOperationException("Server thread was interrupted but not handled :(\n");
	    } catch (SocketException SE) {
		// Оправяме TCP RST като зачистим буфера, не знам дали има странични ефекти,
		// трябва МНОООГО тестване
		buffer.clear();

		System.out.println(SE.getMessage());
	    } catch (Exception e) {
		e.printStackTrace();
		return;
	    }
	}
    }

    // ЗАДЪЛЖИТЕЛНО при излизане се затваря сокета!!!
    public void exit() throws IOException {
	serverSocketChannel.close();
    }
}

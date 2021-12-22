//Тества дали сървърът получава и изпраща правилно съобщения

package viber2;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import org.junit.Test;

public class TestServerRxTx {
    @Test
    public void TestServerSendRecieve() throws IOException {
	Socket Sock = new Socket("localhost", 8081);

	OutputStream OStream = Sock.getOutputStream();
	OStream.write("PING".getBytes());

	InputStream IStream = Sock.getInputStream();
	byte[] Rx = new byte[4];
	IStream.read(Rx);
	Sock.close();
	assertEquals("PONG", Rx.toString());
    }
}

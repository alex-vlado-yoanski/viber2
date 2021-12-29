package org.avy.viber2;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import org.avy.viber2.tables.mapping.User;

public class ConnectionToClient {
    ObjectInputStream in;
    ObjectOutputStream out;
    Socket socket;

    ConnectionToClient(Socket socket) throws IOException {
        this.socket = socket;
        in = new ObjectInputStream(socket.getInputStream());
        out = new ObjectOutputStream(socket.getOutputStream());
/*
        Thread read = new Thread(){
            public void run(){
                while(true){
                    try{
                        Object obj = in.readObject();
                        User messages;
			messages.put(obj);
                    }
                    catch(IOException e){ e.printStackTrace(); }
                }
            }
        };
*/
        //read.setDaemon(true); // terminate when main ends
        //read.start();
    }

    public void write(Object obj) {
        try{
            out.writeObject(obj);
        }
        catch(IOException e){ e.printStackTrace(); }
    }
}

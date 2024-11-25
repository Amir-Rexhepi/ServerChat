package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {
    public static void main(String[] args) throws IOException {
        
        ServerSocket ss = new ServerSocket(3000);

        while(true){

            Server server = new Server(ss);
            server.start();
        }
    }
}
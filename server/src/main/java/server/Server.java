package server;


import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;


public class Server extends Thread {

    public ServerSocket ss;

    public Server(ServerSocket ss) {
        this.ss = ss;
    }

    @Override
    public void run() {
        try {

            Socket s = ss.accept();
            GestioneClient gc = new GestioneClient(s);
            
        } catch (IOException e) {

            e.printStackTrace();

        }

    }


}

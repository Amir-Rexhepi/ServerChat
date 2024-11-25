package server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class GestioneClient extends Thread{
    
    public Socket s;
    public BufferedReader in;
    public BufferedWriter out;
    public String username;
    public List<GestioneClient> gc = new ArrayList<>();

    public GestioneClient(Socket s){
        try{

        this.s=s;
        in = new BufferedReader(new InputStreamReader(s.getInputStream()));
        out = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
        username = in.readLine();
        gc.add(this);
        messageBrodcast("SERVER: " + username + "si e' unito alla chat");

        }catch(Exception e){
            e.printStackTrace();
        }
    }


    @Override
    public void run() {
        try {
            //menu
            out.write("Digita '1' per andare in chat PRIVATA, '2' per quella PUBBLICA e '3' per DISCONNETTERTI");
            out.newLine();
            out.flush();

            String scelta = in.readLine();

            switch (scelta) {
                case "1":
                    
                    break;
                
                case "2":
                //menu pubblico
                    out.write("PUBBL");
                    out.newLine();
                    out.flush();

                    String messaggio;
                    while((messaggio = in.readLine()) != null){
                        if(messaggio.equals("/QUIT")){
                            break;
                        }else{
                            this.messageBrodcast(messaggio);
                        }
                        
                    }

                    break;
                case "3":

                    break;
            }



        } catch (IOException e) {

            e.printStackTrace();

        }
    }

    public void invioMessaggio(String mess){
        
        try {

            out.write(mess);

        } catch (IOException e) {

            e.printStackTrace();

        }
    }

    public void messageBrodcast(String messaggio){

        for(GestioneClient i:gc){

            invioMessaggio(messaggio);

        }
    }
}

package server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class GestioneClient extends Thread {

    public Socket s;
    public BufferedReader in;
    public BufferedWriter out;
    public String username;
    public static List<GestioneClient> gc = new ArrayList<>();

    public GestioneClient(Socket s) {
        try {
            this.s = s;
            in = new BufferedReader(new InputStreamReader(s.getInputStream()));
            out = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            username = in.readLine();
            synchronized (gc) {
                gc.add(this); // Aggiunge il client alla lista condivisa
            }
           
            // menu
            while (true) {
                
            
                String scelta = in.readLine();
                System.out.println(scelta);

                switch (scelta) {
                    case "1":
                    out.write("PRIV");
                    out.newLine();
                    out.flush();
                
                    String dst = in.readLine(); // Leggi l'username del destinatario della chat privata
                
                    // Trova il client destinatario
                    GestioneClient destinatario = null;
                    for (GestioneClient c : gc) {
                        if (c.username.equals(dst)) {
                            destinatario = c;
                            break;
                        }
                    }
                
                    if (destinatario != null) {
                        out.write("ok");
                        out.newLine();
                        out.flush();
                
                        // Ora inizia la chat privata
                        boolean connessionePriv = true;
                        while (connessionePriv) {
                            String messaggio = in.readLine(); // Leggi i messaggi dal client
                            if (messaggio.equals("/QUIT")) {
                                connessionePriv = false;
                                destinatario.invioMessaggio("SERVER: " + username + " ha lasciato la chat privata");
                                this.invioMessaggio("/QUIT"); // Comunica al client che la chat è finita
                                break;
                            } else {
                                // Invia il messaggio al destinatario della chat privata
                                destinatario.invioMessaggio(username + "(Privato): " + messaggio);
                            }
                        }
                    } else {
                        out.write("Utente non trovato!");
                        out.newLine();
                        out.flush();
                    }
                        break;

                    case "2":
                    // menu pubblico
                    messageBrodcast("SERVER: " + username + " si e' unito alla chat");
                    out.write("PUBBL");
                    out.newLine();
                    out.flush();
                    boolean connessione = true;
                    while (connessione) {
                            String messaggio = in.readLine();
                            if (messaggio.equals("/QUIT")) {
                                connessione = false;
                                messageBrodcast("SERVER: " + username + "  ha abbandonato la chat");
                                break;
                            } else {
                                System.out.println(messaggio);
                                this.messageBrodcast(username+": "+messaggio);
                            }
                        }
                        break;
                    case "3":
                        System.out.println(username + " si è disconesso");
                        break;
                }
            }

        } catch (IOException e) {

            e.printStackTrace();

        }
    }
    public void invioMessaggio(String mess) {

        try {
            out.write(mess);
            out.newLine();
            out.flush();

        } catch (IOException e) {

            e.printStackTrace();

        }
    }

    public void messageBrodcast(String messaggio) {

        synchronized (gc) {
            for (GestioneClient client : gc) {
                if (client != this) { // Non invia il messaggio a sé stesso
                    client.invioMessaggio( messaggio);
                }
            }
        }

    }
}

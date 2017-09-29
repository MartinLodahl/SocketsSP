package main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

public class TcpServerClientThread extends Thread {

    private Socket clientSocket;
    PrintWriter toClient;
    TurnstileCounter c;
    ArrayList<Turnstile> turns;

    public TcpServerClientThread(Socket s, TurnstileCounter tc, ArrayList<Turnstile> turnstiles) {
        clientSocket = s;
        c = tc;
        turns = turnstiles;
        start();
    }

    public void run() {

        System.out.println("Server log: New communication thread started...");
        HashMap<String, String> translated = new HashMap();
        translated.put("Hund", "Dog");
        translated.put("Fløde", "Cream");
        String name = "John";

        try {
            toClient = new PrintWriter(clientSocket.getOutputStream(), true);
            BufferedReader fromClient = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            toClient.println("WELCOME...");

            String clientInput;

            while ((clientInput = fromClient.readLine()) != null) {
                System.out.println("Server log: Received input - " + clientInput);

                if (clientInput.equals("SEE")) {
                    toClient.println("Number of spectators: " + c.getValue());
                } else if (clientInput.startsWith("SEE#")) {
                    toClient.print("Number of spectators: " + c.getValue());
                    for (int i = 0; i < turns.size(); i++) {
                        toClient.print(" "+turns.get(i).getName() + " number count: " + turns.get(i).getCount());
                    }
                    toClient.println(" ");

                } else if (clientInput.equals("LEAVE#")) {
                    System.out.println("Server log: Asked to leave...");
                    System.out.println("Server log: Communication thread stopped...");

                    toClient.println("GOODBYE...");

                    break;
                } else {
                    System.out.println("Server log: Asked for something unknown...");

                    toClient.println("DO NOT UNDERSTAND...");
                }
            }

            toClient.close();
            fromClient.close();
            clientSocket.close();
        } catch (IOException e) {
            System.out.println("Server log: Problem with Communication Server...");
        }
    }

    public void echo(String stringToClient) {
        toClient.println(stringToClient + ":");
    }

}

package tcpserverclient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;

public class TcpServerClientThread extends Thread {

    private Socket clientSocket;
    PrintWriter toClient;
    TcpServerHandler serverHandler;

    public TcpServerClientThread(Socket s, TcpServerHandler serverHandler) {
        clientSocket = s;
        this.serverHandler = serverHandler;
        start();
    }

    public void run() {
        serverHandler.addServer(this);
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

                if (clientInput.startsWith("ECHO#")) {
                    System.out.println("Server log: Asked for Echo");

//                    toClient.println("Echo..." + clientInput.substring(5));
                    serverHandler.echoAll(name +":"+"Echo..." + clientInput.substring(5));
                } else if (clientInput.startsWith("UPPER#")) {
                    System.out.println("Server log: Asked for uppercased...");

//                    toClient.println("Uppercased..." + clientInput.substring(6, clientInput.length()).toUpperCase());
                    serverHandler.echoAll(name +":"+"Uppercased..." + clientInput.substring(6, clientInput.length()).toUpperCase());
                } else if (clientInput.startsWith("LOWER#")) {
                    System.out.println("Server log: Asked for the lowercased...");

//                    toClient.println("Lowercased..." + clientInput.substring(6, clientInput.length()).toLowerCase());
                    serverHandler.echoAll(name +":"+"Lowercased..." + clientInput.substring(6, clientInput.length()).toLowerCase());
                } else if (clientInput.startsWith("REVERSE#")) {
                    System.out.println("Server log: Asked for reversed");
                    String reversed = "";
                    int inputLength = clientInput.length();
                    for (int i = 0; i < inputLength - 8; i++) {
                        reversed += clientInput.substring(inputLength - 1 - i, inputLength - i);
                    }
//                    toClient.println("Reversed...." + reversed);
                    serverHandler.echoAll(name +":"+"Reversed..."+reversed);

                } else if (clientInput.startsWith("TRANSLATE#")) {
                    System.out.println("Server log: Asked for translation");
                    String translation;
                    if (translated.get(clientInput.substring(10)) != null) {
                        translation = translated.get(clientInput.substring(10));
                    } else {
                        translation = "#NOT_FOUND_FOR_" + clientInput.substring(10);
                    }
//                    toClient.println("Translation..." + translation);
                    serverHandler.echoAll(name +":"+"Translation..."+translation);
                } else if (clientInput.equals("LEAVE#")) {
                    System.out.println("Server log: Asked to leave...");
                    System.out.println("Server log: Communication thread stopped...");

                    toClient.println("GOODBYE...");
                    serverHandler.removeServer(this);

                    break;
                } else if (clientInput.startsWith("name#")){
                    System.out.println("Server log: Asked to ste name...");
                    name = clientInput.substring(5);
                } 
                else {
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
        toClient.println(stringToClient+":");
    }

}

package main;

import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.util.ArrayList;

public class TcpServer {

    TurnstileCounter tc;

    public TcpServer(String ip, int port) {
        int i = 0;
        ServerSocket serverSocket = null;
        tc = new TurnstileCounter();
        try {
            serverSocket = new ServerSocket();
            serverSocket.bind(new InetSocketAddress(ip, port));
            ArrayList<Turnstile> turnstiles = new ArrayList();
            System.out.println("Server log: Server created...");
            System.out.println("Server log: Listening for clients on port " + port + "...");

            try {
                System.out.println("Server log: Waiting for connections...");

                while (true) {
                    Turnstile t = new Turnstile(tc, 202, "turnstile" + i);
                    i++;
                    turnstiles.add(t);
                    t.run();
                    new TcpServerClientThread(serverSocket.accept(), tc, turnstiles);
                }
            } catch (Exception e) {
                System.err.println("Server log: Accepting connection failed...");
            }
        } catch (Exception e) {
            System.out.println("Server log: Could not listen on port: " + port + "...");
        }
    }
}

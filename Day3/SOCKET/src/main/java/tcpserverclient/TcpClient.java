package tcpserverclient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TcpClient {

    private Socket clientSocket;
    private PrintWriter toServer;
    private BufferedReader fromServer;

    private String input, output;
    private BufferedReader clientInput;
    private TcpReader tcpReader;

    public TcpClient(String ip, int port) throws IOException, InterruptedException {

        clientInput = new BufferedReader(new InputStreamReader(System.in));

        try {
            System.out.println("Client message: Attemping to connect to host " + ip + " on port " + port);

            clientSocket = new Socket(ip, port);
            toServer = new PrintWriter(clientSocket.getOutputStream(), true);
            fromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            tcpReader = new TcpReader(fromServer, this);
        } catch (Exception e) {
            System.out.println("Client message: Unabe to connect to host: " + ip + " on port " + port);
        }

        try {

            System.out.println("Client message: Joined the server!");

            System.out.println("Server message: " + fromServer.readLine());

            while ((input = clientInput.readLine()) != null) {
                toServer.println(input);
            }

            clientInput.close();
            toServer.close();
            fromServer.close();
            clientSocket.close();
        } catch (IOException ex) {
            Logger.getLogger(TcpClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void kill() throws Throwable {
        System.exit(0);
    }
}

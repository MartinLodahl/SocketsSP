/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tcpserverclient;

import java.util.ArrayList;

/**
 *
 * @author MartinLodahl
 */
public class TcpServerHandler {

    ArrayList<TcpServerClientThread> servers;

    TcpServerHandler() {
        servers = new ArrayList();
    }

    public void addServer(TcpServerClientThread clientserver) {
        servers.add(clientserver);
    }

    public void removeServer(TcpServerClientThread clientServer) {
        servers.remove(clientServer);
    }

    public void echoAll(String echo, TcpServerClientThread clientServer) {
        servers.remove(clientServer);
        for (int i = 0; i < servers.size(); i++) {
            servers.get(i).echo(echo);
        }
        servers.add(clientServer);
    }

    public void echoAll(String echo) {
        for (int i = 0; i < servers.size(); i++) {
            servers.get(i).echo(echo);
        }
    }

}

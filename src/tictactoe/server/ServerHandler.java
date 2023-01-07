/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tictactoe.server;

import Database.DatabaseConnection;
import Database.DatabaseOperations;
import Logic.ServerHandlerLogic;
import org.apache.derby.jdbc.ClientDriver;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;

/**
 *
 * @author Eman
 */
public class ServerHandler {

    ServerSocket serverSocket;

    public ServerHandler() {   
         
    }
    
    void startServer() {
        try {
            serverSocket = new ServerSocket(5005);
            while (true) {
                Socket socket = serverSocket.accept();
                new Handler(socket);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    void stopServer() {
        try { 
           serverSocket.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

}

class Handler extends Thread {

    DataInputStream dis;
    PrintStream ps;
    static Vector<Handler> clientsVector = new Vector<Handler>();
    ServerHandlerLogic serverHandlerLogic;

    public Handler(Socket cs) {
        serverHandlerLogic = new ServerHandlerLogic();
        try {
            dis = new DataInputStream(cs.getInputStream());
            ps = new PrintStream(cs.getOutputStream());
            Handler.clientsVector.add(this);
            start();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void run() {
        while (true) {

            try {
                String str = dis.readLine();
                if (str != null) {
                    System.out.println("from client " + str);
                    serverHandlerLogic.divideMessage(str);
                    sendMessageToAll(str);
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }

        }
    }
    
     

    void sendMessageToAll(String msg) {

        for (int i = 0; i < clientsVector.size(); i++) {
            //clientsVector[i].ps.println(msg);   
            clientsVector.get(i).ps.println(msg);
        }
    }

}

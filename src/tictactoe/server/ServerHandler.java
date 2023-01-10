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
    Socket socket;
    ServerHandlerLogic serverHandlerLogic;
    String[] operation = null;
    DatabaseOperations databaseOperations;

    public Handler(Socket socket) {
        serverHandlerLogic = new ServerHandlerLogic();
        databaseOperations = new DatabaseOperations();
        try {
            dis = new DataInputStream(socket.getInputStream());
            ps = new PrintStream(socket.getOutputStream());
            this.socket = socket;
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
                    operation = serverHandlerLogic.divideMessage(str);
                    doAction();
                    sendMessageToAll(str);

                    //  String[] arrOfStr = str.split(",");
                    //  databaseOperations.signUpDatabase(arrOfStr);  
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

    void doAction() {
        int index = getClientIndex();
        System.out.println("index client = " + index);
        if (operation[0].equals("signIn")) {
            System.out.println("siginINNNNNNNNNNNNNNNNNNNNNN");
            if (serverHandlerLogic.checkUserExist()) {
                clientsVector.get(index).ps.println("signInVerified");
            } else {
                clientsVector.get(index).ps.println("signInNotVerified");
            }
        } else if (operation[0].equals("getOnlineUsers")) {
            sendAllOnlineUsers();
        }
        if (operation[0].equals("SignUp")) {
            clientsVector.get(index).ps.println("*********signUpVerified");
            index = getClientIndex();
            if (serverHandlerLogic.checksignUp() == true) {
                clientsVector.get(index).ps.println("signUpVerified");
            } else {
                clientsVector.get(index).ps.println("signUpNotVerified");
            }
        }
    }

    int getClientIndex() {

        for (int i = 0; i < clientsVector.size(); i++) {
            System.out.println("ip ser=" + socket.getInetAddress().getHostAddress());
            if (operation[1].equals(socket.getInetAddress().getHostAddress())) {
                System.out.println("trueeeeeeeeeeeeeeeeeeeeeeeeeeee");
                return i;
            }

        }
        return -1;
    }

    void sendAllOnlineUsers() {

        for (int i = 0; i < clientsVector.size(); i++) {
            String ip = clientsVector.get(i).socket.getInetAddress().getHostAddress();
            System.out.println("ip ser=" + ip);
            ps.println("sendAllUsers," + ip);
        }
    }

}

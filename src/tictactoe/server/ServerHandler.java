/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tictactoe.server;

import Database.DatabaseConnection;
import Database.DatabaseOperations;
import Logic.ServerHandlerLogic;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
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
            if (!serverSocket.isClosed()) {
                while (true) {
                    Socket socket = serverSocket.accept();
                    new Handler(socket);
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }

    void stopServer() {
        try {
            serverSocket.close();
            serverSocket = null;
        } catch (Exception ex) {

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
                } else {
                    this.stop();
                    clientsVector.remove(this);
                }
            } catch (IOException ex) {
                this.stop();
                clientsVector.remove(this);
            }

        }
    }

    void sendMessageToAll(String msg) {

        for (int i = 0; i < clientsVector.size(); i++) {
            //clientsVector[i].ps.println(msg); 
            clientsVector.get(i).ps.println(msg + i);
        }
    }

    void sendMessageToAll(String msg, String ip) {

        for (int i = 0; i < clientsVector.size(); i++) {
            //clientsVector[i].ps.println(msg); 
            if (socket.getInetAddress().getHostAddress().equals(ip)) {
                clientsVector.get(i).ps.println(msg);
            }
        }
    }

    void doAction() {
        int index;

        if (operation[0].equals("signIn")) {
            System.out.println("siginINNNNNNNNNNNNNNNNNNNNNN");
            index = getClientIndex();
            if (serverHandlerLogic.checkUserExist()) {
                clientsVector.get(index).ps.println("signInVerified");
            } else {
                clientsVector.get(index).ps.println("signInNotVerified");
            }
        } else if (operation[0].equals("getOnlineUsers")) {
            sendAllOnlineUsers();
        }

        if (operation[0].equals("SignUp")) {
            index = getClientIndex();
            System.out.println("indexxxxxx = " + index);
            //  clientsVector.get(index).ps.println("*********signUpVerified");
            if (serverHandlerLogic.checksignUp() == true) {
                System.out.println("SignUpp trueeeeeeeee");
                //  sendMessageToAll("sucessssssssssssssssssssssss");
                clientsVector.get(index).ps.println("signUpVerified");
                sendMessageToAll("signUpVerified", operation[1]);
            } else {
                System.out.println("SignUpp not trueeeeeeeee");
                clientsVector.get(index).ps.println("signUpNotVerified");
                sendMessageToAll("signUpNotVerified", operation[1]);
            }
        }
    }

    int getClientIndex() {

        for (int i = 0; i < clientsVector.size(); i++) {
            System.out.println("ip ser=" + socket.getInetAddress().getHostAddress());
            if (operation[0].equals("signIn") || operation[0].equals("SignUp")) {
                System.out.println("operation [[[[1111]]]]" + Arrays.toString(operation));
                if (operation[1].equals(socket.getInetAddress().getHostAddress())) {
                    System.out.println("trueeeeeeeeeeeeeeeeeeeeeeeeeeee");
                    return i;
                }

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

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tictactoe.server;

import Database.DatabaseOperations;
import Logic.ServerHandlerLogic;
import Logic.User;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.Inet4Address;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

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
    ArrayList<User> usersList;

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
            clientsVector.get(i).ps.println(msg);
        }
    }

    void sendMessageToSpecificUser(String msg, String ip) {

        for (int i = 0; i < clientsVector.size(); i++) {

            String loopIp = clientsVector.get(i).socket.getInetAddress().getHostAddress();
            if (loopIp.equals(ip)) {
                clientsVector.get(i).ps.println(msg);
            }
        }
    }

    void doAction() {
        if (operation[0].equals("signIn")) {
            String userIp = operation[1];
            if (serverHandlerLogic.checkUserExist()) {
                String email = operation[2];
                sendMessageToSpecificUser("signInVerified", userIp);
                serverHandlerLogic.addUserToOnlineList(email);
                //profile
                String data = serverHandlerLogic.getProfileData(email, userIp);
                sendMessageToSpecificUser("profileData," + data, userIp);

            } else {
                sendMessageToSpecificUser("signInNotVerified", userIp);
            }
        } else if (operation[0].equals("SignUp")) {
            String userIp = operation[1];
            if (serverHandlerLogic.checksignUp() == true) {
                String email = operation[3];
                sendMessageToSpecificUser("signUpVerified", userIp);
                serverHandlerLogic.addUserToOnlineList(email);
                //profile
                String data = serverHandlerLogic.getProfileData(email, userIp);
                sendMessageToSpecificUser("profileData," + data, userIp);

            } else {
                sendMessageToSpecificUser("signUpNotVerified", userIp);
            }

            ////////////////////////////////////////////////////////////////
        } else if (operation[0].equals("getOnlineUsers")) {
            String senderIp = operation[1];
            sendAllOnlineUsers(senderIp);
        } else if (operation[0].equals("sendRequest")) {
            String senderIp = operation[1];
            String receiverIp = operation[2];
            sendMessageToSpecificUser("recieveRequest," + senderIp + "," + receiverIp, receiverIp);
//////////////////////////////////////////////////////////
            String senderInfo = ServerHandlerLogic.map.get(senderIp);
            String reciverInfo = ServerHandlerLogic.map.get(receiverIp);
            System.out.println(senderInfo + "wwwwwwwwwwwwwwwwwwwwwww");
            System.out.println(reciverInfo + "uuuuuuuuuuuuuuuuuuuuuuuu");

            sendMessageToSpecificUser("getPlayersData," + senderIp + "," + senderInfo + "," + receiverIp + "," + reciverInfo, receiverIp);

        } else if (operation[0].equals("confirmRequestfromSecondPlayer")) {
            String senderIp = operation[1];
            String receiverIp = operation[2];

            String senderInfo = ServerHandlerLogic.map.get(receiverIp);
            String reciverInfo = ServerHandlerLogic.map.get(senderInfo);
            System.out.println(senderInfo + "fffffffffffffffffffffff");
            System.out.println(reciverInfo + "qqqqqqqqqqqqqqqqqqqqqqq");

            sendMessageToSpecificUser("confirmRequest," + receiverIp, senderIp);
            sendMessageToSpecificUser("getPlayersData," + receiverIp + "," + senderInfo + "," + senderInfo + "," + reciverInfo, receiverIp);
        } /////////////////////////////////////////////////////
        else if (operation[0].equals("logOut")) {
            String userIp = operation[1];
            clientsVector.remove(this);
            ServerHandlerLogic.map.remove(userIp);
        } else if (operation[0].equals("updateScore")) {
            String email = operation[1];
            int score = Integer.valueOf(operation[2]);
            serverHandlerLogic.databaseOperations.updateScore(email, score);
        } else if (operation[0].equals("game")) {
            String userIp = operation[1];

            //sendMessageToSpecificUser("sendGame", userIp);
        }
    }

    void sendAllOnlineUsers(String senderIp) {
        usersList = serverHandlerLogic.getOnlineListUsers();
        for (int i = 0; i < clientsVector.size(); i++) {
            String loopIp = clientsVector.get(i).socket.getInetAddress().getHostAddress();
            User user = usersList.get(i);
           // if (loopIp.equals(senderIp)) {continue;}
            ps.println("sendAllUsers," + loopIp + "," + user.getUserName() + "," + user.getEmail() + "," + user.getGender() + "," + user.getScore());
            System.out.println(user.toString());
        }
    }
}


/*   int getClientIndex() {
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

    int getClientIndex(String ip) {
        for (int i = 0; i < clientsVector.size(); i++) {
            //  System.out.println("ip ser=" + this.socket.getInetAddress().getHostAddress());
            System.out.println("operationnn = " + Arrays.toString(operation));
            if (ip.equals(socket.getInetAddress().getHostAddress())) {
                System.out.println("trueeeeeeeeeeeeeeeeeeeeeeeeeeee");
                return i;
            }
        }
        return -1;
    }
 */

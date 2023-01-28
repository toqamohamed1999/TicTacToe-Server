
package Logic;

import Database.DatabaseOperations;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import static Logic.Handler.clientsVector;
import java.util.Vector;


public class ServerHandler {

    ServerSocket serverSocket;

    public ServerHandler() {

    }

   public void startServer() {
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

    public void stopServer() {
        try {
            clientsVector.clear();
            serverSocket.close();
            serverSocket = null;

        } catch (Exception ex) {

        }
    }
    public static int getVectorSize(){return clientsVector.size();}

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
                    System.out.println("message from client: "+str);
                    operation = serverHandlerLogic.divideMessage(str);
                    doAction();
                    //    sendMessageToAll(str);
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
            signIn();
        } else if (operation[0].equals("SignUp")) {
            signUp();
        } else if (operation[0].equals("getOnlineUsers")) {
            String senderIp = operation[1];
            sendAllOnlineUsers(senderIp);
        } else if (operation[0].equals("sendRequest")) {
            sendRequest();
        } else if (operation[0].equals("confirmRequestfromSecondPlayer")) {
            confirmRequest();
        } else if (operation[0].equals("logOut")) {
            String userIp = operation[1];
            clientsVector.remove(this);
            ServerHandlerLogic.map.remove(userIp);
        } else if (operation[0].equals("updateScore")) {
            String email = operation[1];
            int score = Integer.valueOf(operation[2]);
            serverHandlerLogic.databaseOperations.updateScore(email, score);
        } else if (operation[0].equals("game")) {
            String recieverUserIp = operation[2];
            String gameindex = operation[1];
            sendMessageToSpecificUser("game," + gameindex, recieverUserIp);
        }
    }
    static String udata = null;

    void sendAllOnlineUsers(String senderIp) {
        usersList = serverHandlerLogic.getOnlineListUsers();
        for (int i = 0; i < usersList.size(); i++) {
            String loopIp = clientsVector.get(i).socket.getInetAddress().getHostAddress();
            User user = usersList.get(i);
            if (loopIp.equals(senderIp)) {
                continue;
            }
            ps.println("sendAllUsers," + loopIp + "," + user.getUserName() + "," + user.getEmail() + "," + user.getGender() + "," + user.getScore());
        }
    }

    private void signIn() {
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
    }

    private void signUp() {
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
    }

    private void sendRequest() {
        String senderIp = operation[1];
        String receiverIp = operation[2];
        String senderInfo = ServerHandlerLogic.map.get(senderIp);
        String reciverInfo = ServerHandlerLogic.map.get(receiverIp);

        udata = "getPlayersData," + senderIp + "," + senderInfo + "," + receiverIp + "," + reciverInfo;
        sendMessageToSpecificUser("getPlayersData," + senderIp + "," + senderInfo + "," + receiverIp + "," + reciverInfo, receiverIp);
        sendMessageToSpecificUser("recieveRequest," + senderIp + "," + receiverIp, receiverIp);
    }

    private void confirmRequest() {
        String senderIp = operation[1];
        String receiverIp = operation[2];
        sendMessageToSpecificUser(udata, senderIp);
        sendMessageToSpecificUser("confirmRequest," + receiverIp, senderIp);
    }
}

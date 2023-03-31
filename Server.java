import Pkg_Email.SendEmail;
import Pkg_Exception.User_Not_Found_Exception;
import Pkg_group.Group;
import Pkg_group.Group_Manger;
import Pkg_Person.User;
import Pkg_Person.User_Manger;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class Server implements Runnable {
    private static ServerSocket server;
    private ExecutorService poll;
    private ArrayList<ConnectionHandler> connectionHandlers;
    private boolean done;
    public static Map<String, Socket> clientHandlers = new HashMap<>();
    public Server() {
        connectionHandlers = new ArrayList<>();
        done = false;
    }

    @Override
    public void run() {
        try {
            server = new ServerSocket(9999);
            poll = Executors.newCachedThreadPool();
            while (!done) {
                Socket clint = server.accept();
                ConnectionHandler connectionHandler = new ConnectionHandler(clint);
                connectionHandlers.add(connectionHandler);
                poll.execute(connectionHandler);
            }
        } catch (IOException e) {
            shutDown();
        }
    }

    public void broadCast(String message, String email) {
        for (ConnectionHandler connectionHandler : connectionHandlers) {
            User flag = connectionHandler.user_manger.get(email);
            if (flag != null) {
                connectionHandler.seneMessag(message);
            }
        }
    }

    public void shutDown() {
        try {
            done = true;
            this.poll.shutdown();
            if (!server.isClosed()) {
                server.close();
            }
            for (ConnectionHandler ch : connectionHandlers) {
                ch.shutDown();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    static class ConnectionHandler implements Runnable {
        private BufferedReader in;
        private PrintWriter out;
        private Socket client;
        private String nickName;
        private Map<Socket, String> accounts = new HashMap<>();
        User_Manger user_manger;
        Group_Manger group_manger;

        public ConnectionHandler(Socket client) {
            this.client = client;
        }

        @Override
        public void run() {
            try {
                user_manger = new User_Manger();
                group_manger = new Group_Manger();
                User us;
                out = new PrintWriter(client.getOutputStream(), true);
                in = new BufferedReader(new BufferedReader(new InputStreamReader(client.getInputStream())));
                String choice;
                label:
                do {
                    out.println(" Enter your choice\n1 Login\n2 sing in\n3 Forgot Password\n4 Logout");
                    choice = in.readLine();
                    switch (choice) {
                        case "1" -> {
                            out.println("What's Your Name");
                            String name = in.readLine();
                            out.println("Password");
                            String password = in.readLine();
                            try {
                                us = user_manger.get(name, password);
                                if (us == null) {
                                    throw new User_Not_Found_Exception();
                                }
                                System.out.println("User Enter the server");
                                String us_choice;
                                do {
                                    out.println("1. Search for friends");
                                    out.println("2. Search for groups");
                                    out.println("3. Show my friends list");
                                    out.println("4. Show my groups list");
                                    out.println("5. Create a new group");
                                    out.println("6. Settings");
                                    out.println("7. Movements in this app");
                                    out.println("0. Back to the main window");
                                    us_choice = in.readLine();
                                    switch (us_choice) {
                                        case "1" -> {
                                            out.println("Enter Friend name ");
                                            String name2 = in.readLine();
                                            ArrayList<String> strings = user_manger.SearchFriend(name2);
                                            for(String nick : Server1.clientHandlers.keySet()) {
                                                if (nick.contains(name2)) {
                                                    strings.add(nick);
                                                }
                                            }
                                            do {
                                                if (strings.size() == 0) {
                                                    out.println("No Friends Available this name  ");
                                                    break;
                                                } else {
                                                    for (int i = 0; i < strings.size(); i++) {
                                                        int index = i + 1;
                                                        out.println(index + "." + strings);
                                                    }
                                                }
                                            }while (strings.size()==0);
                                            out.println("Select right number (0 for none)");
                                            try {
                                                String choose = in.readLine();
                                                int ch = Integer.parseInt(choose);
                                                while (ch < 0 || ch > strings.size()) {
                                                    out.println("Please select right number!");
                                                    choose = in.readLine();
                                                    ch = Integer.parseInt(choose);
                                                }
                                                if (ch == 0) {
                                                    break;
                                                } else {
                                                    out.println("Do you want to chat with " + strings.get(ch - 1));
                                                    out.println("1. Yes\n2. No");
                                                    String op = in.readLine();
                                                    int option = Integer.parseInt(op);
                                                    while (option != 1 && option != 2) {
                                                        out.println("Please select 1 or 2");
                                                        op = in.readLine();
                                                        option = Integer.parseInt(op);
                                                    }
                                                    if (option == 1) {
                                                        String s = strings.get((ch - 1));
                                                        user_manger.addFriend(s);
                                                        user_manger.WriteFriend(us.getName());
                                                        out.println("New friend has been added successfully");
                                                        PrintWriter outFriend=new PrintWriter(Server.clientHandlers.get(s).getOutputStream(),true);
                                                        outFriend.println(us.getName());
                                                        Socket command=Server.clientHandlers.get(in.readLine());
                                                        out.println(command);
                                                        Thread thread=new Thread(new SendingMessage(command));
                                                        thread.start();
                                                        startChat(command,strings.get(ch-1));
                                                    }
                                                }
                                            } catch (IOException | NumberFormatException e) {
                                                e.printStackTrace();
                                            }

//                                            out.println("What do you want to call your group?");
//                                            String groupName = in.readLine();
////                                            SimpleDateFormat date = new SimpleDateFormat("C:\\program1\\User1.csv");
//                                            Date date = new Date();
//                                            Group group = new Group(groupName, date, us.getName());
//                                            group_manger.addGroup(group);
//                                            out.println("Group is created");
                                        }
                                        case "2" -> {
                                                out.println("Enter Group Name To Fetch the Group");
                                                name = in.readLine();
                                                Group group1 = group_manger.searchGroup(name);
                                                do {
                                                    if (group1 == null) {
                                                        out.println("No Group available this name");
                                                        out.println("Plz Enter Valid Group name");
                                                        name = in.readLine();
                                                        group1 = group_manger.searchGroup(name);
                                                    } else {
                                                        out.println(group1);
                                                    }
                                                }while (group1==null);
                                                out.println("Enter  \n1.Add  \n2.remove");
                                                String str = in.readLine();
                                                if (str.equals("1")) {
                                                    out.println("What's your name");
                                                    name = in.readLine();
                                                    boolean flag = user_manger.checkName(name);
                                                    do {
                                                        out.println("What's your name");
                                                        name = in.readLine();
                                                        flag = user_manger.checkName(name);
                                                    } while (!flag);
                                                    group_manger.addUser(group1, name);
                                                } else if (str.equals("2")) {

                                                } else {
                                                    out.println("INVALID INPUT");
                                                }
                                            }
//                                            String jo_choice;
//                                            do {
//                                                out.println("Enter \n1. <show list of all the groups>\n2. Search group\n3. Which group do you want to join?\n4. Back");
//                                                jo_choice = in.readLine();
//                                                switch (jo_choice) {
//                                                    case "1" -> {
//                                                        out.println("---- ALL THE GROUPS ----");
//                                                        ArrayList<Group> g = group_manger.ViewAllGroups();
//                                                        out.println(g);
//                                                    }
//
                                                    case "3" -> {
//                                                        Group group2;
//                                                        do {
//                                                            out.println("Enter the Group Name Which group do want you join");
//                                                            String name1 = in.readLine();
//                                                            group2 = group_manger.searchGroup(name1);
//                                                            if (group2 == null) {
//                                                                System.out.println("plz enter a valid name");
//                                                            }
//                                                        } while (group2 == null);
//                                                        String str;
//                                                        do {
//                                                            out.println("Enter \ni. if admin \nii. if not admin but member \niii if not admin and not member \niv Back");
//                                                            str = in.readLine();
//                                                            switch (str) {
//                                                                case "i":
//                                                                    String i;
//                                                                    do {
//                                                                        out.println("Enter \n1 Add People \n2 Remove People \n3 show all Member \n4 Back");
//                                                                        i = in.readLine();
//                                                                        switch (i) {
//                                                                            case "1":
//                                                                                out.println("Plz Enter The People Details");
//                                                                                out.println("NAME :-");
//                                                                                String name2 = in.readLine();
//                                                                                out.println("EMAIL :-");
//                                                                                String email = in.readLine();
//                                                                                User user = new User(name2, email);
//                                                                                user_manger.addUser(user);
//                                                                                out.println("People is added");
//                                                                                break;
//                                                                            case "2":
//                                                                                out.println("Enter the User Email do you want to remove");
//                                                                                email = in.readLine();
//                                                                                if (user_manger.removeUser(email)) {
//                                                                                    user_manger.removeUser(email);
//                                                                                    out.println("User is remove");
//                                                                                } else {
//                                                                                    out.println("Plz enter the valid email Address");
//                                                                                }
//                                                                                break;
//                                                                            case "3":
//                                                                                ArrayList<User> list = user_manger.viewAllUser();
//                                                                                out.println(list);
//                                                                                break;
//                                                                            case "4":
//                                                                                break;
//                                                                            default:
//                                                                                out.println("INVALID INPUT");
//                                                                                break;
//                                                                        }
//                                                                    } while (!i.equals("4"));
//                                                                    break;
//                                                                case "ii":
//                                                                    String ii;
//                                                                    do {
//                                                                        out.println("Enter \n1 Show whole chat \n2 Enable to chat in the group \n3 Leave the group \n4 Delete message \n5 Back");
//                                                                        ii = in.readLine();
//                                                                        switch (ii) {
//                                                                            case "1":
//                                                                                out.println("Show whole chat");
//                                                                                break;
//                                                                            case "2":
//                                                                                out.println("Enable to chat in the group");
//                                                                                break;
//                                                                            case "3":
//                                                                                out.println("Leave the group");
//                                                                                break;
//                                                                            case "4":
//                                                                                out.println("Delete message");
//                                                                                break;
//                                                                            case "5":
//                                                                                break;
//                                                                            default:
//                                                                                System.out.println("INVALID INPUT");
//                                                                                break;
//                                                                        }
//                                                                    } while (!ii.equals("5"));
//                                                                    break;
//                                                                case "iii":
//                                                                    String iii;
//                                                                    do {
//                                                                        out.println("Enter \n1 Request to join the group \n2 Cancel the request \n3 Back");
//                                                                        iii = in.readLine();
//                                                                        switch (iii) {
//                                                                            case "1":
//                                                                                out.println("Request to join the group");
//                                                                                break;
//                                                                            case "2":
//                                                                                out.println("Cancel the request ");
//                                                                                break;
//                                                                            case "3":
//                                                                                break;
//                                                                            default:
//                                                                                out.println("INVALID INPUT");
//                                                                                break;
//                                                                        }
//                                                                    } while (!iii.equals("3"));
//                                                                case "iv":
//                                                                    break;
//                                                                default:
//                                                                    out.println("INVALID INPUT");
//                                                                    break;
//                                                            }
//                                                        } while (!str.equals("iv"));
//                                                    }
//                                                }
//                                            } while (!jo_choice.equals("4"));
                                        }
//                                        case "3" -> {
//                                            User user = null;
//                                            do {
//                                                out.println("Enter User Email Adders do You want to chat");
//                                                String email = in.readLine();
//                                                user = user_manger.get(email);
//                                                try {
//                                                    if (user == null) {
//                                                        out.println("User Not Available");
//                                                    } else {
//                                                        System.out.println(user.getName() + " connected!");
//                                                        broadCast(nickName + " joined the chat !", email);
//                                                        String message;
//                                                        while ((message = in.readLine()) != null) {
//                                                            if (message.equals("/nick")) {
//                                                                String[] messageSplit = message.split(" ", 2);
//                                                                if (messageSplit.length == 2) {
//                                                                    broadCast(nickName + " renamed themselves to " + messageSplit[1], email);
//                                                                    System.out.println(nickName + " renamed themselves to " + messageSplit[1]);
//                                                                    nickName = messageSplit[1];
//                                                                    out.println("SuccessFully Changed nickname to " + nickName);
//                                                                } else {
//                                                                    out.println("No Nickname provided");
//                                                                }
//                                                            } else if (message.equals("back")) {
//                                                                broadCast(nickName + " left the chat!", email);
//                                                                break;
//                                                            } else {
//                                                                broadCast(nickName + " : " + message, email);
//                                                            }
//                                                        }
//                                                    }
//                                                } catch (IOException e) {
//                                                    e.printStackTrace();
//                                                }
//                                            } while (user == null);
//                                        }
                                    }
                                } while (!us_choice.equals("4"));
                            } catch (User_Not_Found_Exception u) {
                                out.println(u);
                            }
                        }
                        case "2" -> {
                            this.out = new PrintWriter(this.client.getOutputStream(), true);
                            this.in = new BufferedReader(new InputStreamReader(this.client.getInputStream()));
                            out.println("Enter Your Email");
                            String email = in.readLine();
                            boolean b = user_manger.emailHandler(email);
                            do {
                                if (!b) {
                                    out.println("Renter Email");
                                    email = in.readLine();
                                    b = user_manger.emailHandler(email);
                                } else {
                                    break;
                                }
                            } while (!b);
                            User user1 = user_manger.get(email);
                            if (user1 == null) {
                                String ch = User.otp();
                                SendEmail sendEmail = new SendEmail();
                                sendEmail.sendEmail(ch, "Email verification otp", email, "javaapplication345@gmail.com");
                                String otp;
                                do {
                                    out.println("Enter Otp");
                                    otp = in.readLine();
                                    if (otp.equals(ch)) {
                                    } else {
                                        out.println("plz enter valid otp");
                                    }
                                } while (!otp.equals(ch));
                                out.println("Enter Name");
                                String name = in.readLine();
                                out.println("Enter Password");
                                String password = in.readLine();
                                out.println("Confirm Password");
                                String cPassword = in.readLine();
                                boolean b1 = user_manger.passwordCheck(password);
                                boolean b2 = user_manger.passwordCheck(cPassword);
                                do {
                                    if (!b1 && b2) {
                                        out.println("Password Must be 8 characters or more, needs at least one number and one letter");
                                        password = in.readLine();
                                        out.println("Confirm Password");
                                        cPassword = in.readLine();
                                        b1 = user_manger.passwordCheck(password);
                                        b2 = user_manger.passwordCheck(cPassword);
                                    } else if (!password.equals(cPassword)) {
                                        out.println("Password does not match");
                                        cPassword = in.readLine();
                                        if (cPassword.equals(password)) {
                                            break;
                                        }
                                    } else {
                                        break;
                                    }
                                } while (!b1 && !b2 && !password.equals(cPassword));
                                User user = new User(name, email, cPassword);
                                Server.clientHandlers.put(name,this.client);
                                out.println("Login Successful");
                                user_manger.addUser(user);
                            } else {
                                out.println("A user is already registered with this e-mail address.Please use another email adders ");
                            }
                        }
                        case "3" -> {
                            boolean flag;
                            do {
                                out.println("Enter Your Old Password");
                                String password = in.readLine();
                                out.println("Enter NewPassword");
                                String newPassword = in.readLine();
                                String otp = user_manger.otp;
                                flag = user_manger.changePassword(password, newPassword);
                                if (flag) {
                                    out.println("Password Change successful");
                                } else {
                                    out.println("Old password is incorrect");
                                }
                            } while (!flag);
                        }
                        case "quit" -> {
                            user_manger.Write_File();
                            group_manger.Write_To_File();
                            out.println("Thanks for Using");
                            break label;
                        }
                        default -> out.println("plz enter Valid Choice");
                    }
                    user_manger.Write_File();
                    group_manger.Write_To_File();
                    ;
                } while (true);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void shutDown() {
            try {
                in.close();
                out.close();
                if (!client.isClosed()) {
                    client.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void startChat(Socket friend,String friendNickname){
            try {
                BufferedReader inFriend = new BufferedReader(new InputStreamReader(friend.getInputStream()));
                String msgToClient = inFriend.readLine();
                while((msgToClient).equals("/quit")){
                    out.println(friendNickname + ": " + msgToClient);
                    msgToClient = inFriend.readLine();
                }
                out.println("/notOnline");
            } catch (IOException e){
                shutDown();
            }

        }

        public void seneMessag(String message) {
            out.println(message);
        }
        class SendingMessage implements Runnable {
            private Socket friend;
            private PrintWriter outToFriend;

            public SendingMessage(Socket friend) {
                this.friend = friend;
            }

            @Override
            public void run() {
                try {
                    outToFriend = new PrintWriter(friend.getOutputStream(), true);

                    //

                    String msgToSend = in.readLine();
                    while (msgToSend.equals("/quit")) {
                        outToFriend.println(msgToSend);
                        msgToSend = in.readLine();
                    }
                    outToFriend.println("/not Online");
                } catch (IOException e) {
//                    shutdown();
                }
            }
        }

    }

    public static void main(String[] args) {
        Server server = new Server();
        System.out.println("Server is Starting");
        server.run();
}
}

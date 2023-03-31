package Pkg_Person;

import java.io.*;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class User_Manger {

    ArrayList<User> users_List;
    ArrayList<String> friends_List;
    public String otp = User.otp();


    public User_Manger() {
        users_List = new ArrayList<>();
        friends_List=new ArrayList<>();
        BufferedReader reader ;
        BufferedReader reader1 ;
        String path = "C:\\program1\\User1.csv";
        try {
            String line = "";
            reader = new BufferedReader(new FileReader(path));
            reader.readLine();
            while ((line = reader.readLine()) != null) {
                String[] fields = line.split(",");
                User user = new User();
                user.setName(fields[0]);
                user.setEmail(fields[1]);
                user.setPassword(fields[2]);
                users_List.add(user);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void Write_File() {
        String path = "C:\\program1\\User1.csv";
        try {
            FileWriter fileWriter = new FileWriter(path);
            fileWriter.append("Name,Email,Password\n");
            for (User u : users_List) {
                fileWriter.append(u.getName());
                fileWriter.append(",");
                fileWriter.append(u.getEmail());
                fileWriter.append(",");
                fileWriter.append(u.getPassword());
                fileWriter.append("\n");
                fileWriter.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addFriend(String friend){
        friends_List.add(friend);
    }

    public void WriteFriend(String name){
        String path="c:\\program1\\"+name+".csv";
        try {
            FileWriter fileWriter=new FileWriter(path);
            for (String friend:friends_List){
                fileWriter.append(friend);
                fileWriter.append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addUser(User user) {
        users_List.add(user);
    }

    public User get(String name, String password) {
        for (User user : users_List) {
            if (user.getName().equals(name) && user.getPassword().equals(password)) {
                return user;
            }
        }
        return null;
    }

    public User get(String email) {
        for (User user : users_List) {
            if (user.getEmail().equals(email)) {
                return user;
            }
        }
        return null;
    }


    public ArrayList<User> viewAllUser() {
        return new ArrayList<>(users_List);
    }

    public boolean removeUser(String name) {
        for (User user : users_List) {
            if (user.getEmail().equals(name)) {
                users_List.remove(user);
                return true;
            }
        }
        return false;
    }

    public ArrayList<String> SearchFriend(String name){
        ArrayList<String> arrayList=new ArrayList<>();
        for (User user : users_List) {
            if (user.getName().contains(name)){
                arrayList.add(user.getName());
            }
        }
        return arrayList;
    }


    public boolean changePassword(String oldPassword, String newPassword) {
        for (User user : users_List) {
            if (user.getPassword().equals(oldPassword)) {
                user.setPassword(newPassword);
                return true;
            }
        }
        return false;
    }

    public boolean emailHandler(String email) {
        boolean isMatched;
            Pattern p = Pattern.compile("^[a-zA-Z0-9_+&*-]+(?:\\."+"[a-zA-Z0-9_+&*-]+)*@"+"(?:[a-zA-Z0-9-]+\\.)+[a-z"+"A-Z]{2,7}$");
            Matcher matcher = p.matcher(email);
            isMatched = matcher.matches();
            return isMatched;
    }

    public boolean passwordCheck(String password){
        return password.length()==8;
    }

    public boolean checkName(String name){
        for (User user : users_List) {
            if (user.getName().equals(name)) {
                return true;
            }
        }
        return false;
    }


    public void Login(){

    }
}

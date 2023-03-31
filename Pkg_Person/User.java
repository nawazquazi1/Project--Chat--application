package Pkg_Person;

import java.io.Serializable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class User {
    private String name;
    private String email;
    private String password;
     String otp=otp();

    public User() {
    }

    public User(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public User(String name, String email) {
        this.name = name;
        this.email = email;
        this.password = password();
    }

    public int id(){
        int id=100;
        id+=1;
        return id;
    }

    public String password() {
        String str = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789!@#$%";
        char[] ch = new char[6];
        for (int i = 0; i < 6; i++) {
            int rand = (int) (Math.random() * str.length());
            ch[i] = str.charAt(rand);
        }
        return new String(ch);
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                '}';
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public static String otp() {
        String str = "0123456789";
        char[] ch = new char[5];
        for (int i = 0; i < 5; i++) {
            int rand = (int) (Math.random() * str.length());
            ch[i] = str.charAt(rand);
        }
        return new String(ch);
    }

}

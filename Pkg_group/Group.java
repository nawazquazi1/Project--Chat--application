package Pkg_group;

import Pkg_Person.User;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class Group {
    private String name;
    private String date;
//    private User user;
    private String groupAdminName;

    public Group() {
    }

    public Group(String name, Date date,String groupAdminName) {
        this.name = name;
        this.date = String.valueOf(date);
        this.groupAdminName=groupAdminName;
    }

//    public Group(User user){
//        this.user=new User();
//    }

    public String getGroupAdminName() {
        return groupAdminName;
    }

    public void setGroupAdminName(String groupAdminName) {
        this.groupAdminName = groupAdminName;
    }

    @Override
    public String toString() {
        return "Group{" +
                "name='" + name + '\'' +
                ", date='" + date + '\'' +
                ", groupAdminName='" + groupAdminName + '\'' +
                '}';
    }


//    public User getUser() {
//        return user;
//    }

//    public void setUser(User user) {
//        this.user = user;
//    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

}

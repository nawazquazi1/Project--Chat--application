package Pkg_group;

import Pkg_Person.User;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ListIterator;
import java.util.Map;

public class Group_Manger {
    ArrayList<Group> group_list;
    ArrayList<User> users;
  HashMap<Group, String> map = new HashMap<>();

    public Group_Manger() {
        group_list = new ArrayList<>();
        BufferedReader reader;
        users = new ArrayList<>();
        String path = "C:\\program1\\Group.csv";
        try {
            String line;
            reader = new BufferedReader(new FileReader(path));
            reader.readLine();
            while ((line = reader.readLine()) != null) {
                String[] filed = line.split(",");
                Group group = new Group();
                group.setName(filed[0]);
                group.setDate(filed[1]);
                group.setGroupAdminName(filed[2]);
                group_list.add(group);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void addGroup(Group group) {
        group_list.add(group);
    }

    public void addUser(Group group, String name) {
        map.put(group, name);
    }

    public Group searchGroup(String name) {
        for (Group group : group_list) {
            if (group.getName().contains(name)) {
                return group;
            }
        }
        return null;
    }

    public int size(){
        return group_list.size();
    }

    public boolean deleteGroup(String name) {
        ListIterator<Group> groupListIterator = (ListIterator<Group>) group_list.listIterator();
        while (groupListIterator.hasNext()) {
            Group group = groupListIterator.next();
            if (group.getName().equals(name)) {
                group_list.remove(group);
                return true;
            }
        }
        return false;
    }

//    public boolean removeUser(String name){
//        for (Map.Entry<Group, String> entry : map.entrySet()){
//            if (entry.getValue().equals(name)){
//                map
//            }
//
//    }
//    }

    public ArrayList<Group> ViewAllGroups() {
        return new ArrayList<>(group_list);
    }

    public void Write_To_File() {

        try {
            FileWriter fileWriter = new FileWriter("C:\\program1\\Group.csv");
            fileWriter.append("GroupName,Date,groupAdminName\n");
            for (Group group : group_list) {
                fileWriter.append(group.getName());
                fileWriter.append(",");
                fileWriter.append((CharSequence) group.getDate());
                fileWriter.append(",");
                fileWriter.append(group.getGroupAdminName());
                fileWriter.append("\n");
                fileWriter.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



}

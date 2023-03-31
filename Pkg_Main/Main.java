package Pkg_Main;

import Pkg_Exception.User_Not_Found_Exception;
import Pkg_Person.*;
import Pkg_group.Group;
import Pkg_group.Group_Manger;;

import java.util.Date;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int choice;
        User_Manger user_manger = new User_Manger();
        Group_Manger group_manger = new Group_Manger();
        User user = new User();
        do {
            System.out.println("Enter \n 1 Login \n 2 Sing-In \n 3 Forget password\n 4 Logout");
            choice = sc.nextInt();
            if (choice == 1) {
                System.out.println("What's Your Name");
                String name = sc.next();
                System.out.println("Password");
                String password = sc.next();
                try {
                    User us = user_manger.get(name, password);
                    if (us == null) {
                        throw new User_Not_Found_Exception();
                    }
                    int us_Choice;
                    do {
                        System.out.println("Enter \n1. Create a group\n2. Join a group\n3. DM\n4. Back");
                        us_Choice = sc.nextInt();
                        switch (us_Choice) {
                            case 1:
                                System.out.println("What do you want to call your group?");
                                name = sc.next();
                                Date date = new Date();
                                Group group = new Group(name, date,us.getName());
                                System.out.println("Group is Created");
                                group_manger.addGroup(group);
                                break;
                            case 2:
                                int jo_choice;
                                do {
                                    System.out.println("Enter \n1. <show list of all the groups>\n2. Search group\n3. Which group do you want to join?\n4. Back");
                                    jo_choice = sc.nextInt();
                                    switch (jo_choice) {
                                        case 1:
                                            System.out.println("---- ALL THE GROUPS ----");
                                            group_manger.ViewAllGroups();
                                            break;
                                        case 2:
                                            System.out.println("Enter Group Name To Fetch the Group");
                                            name = sc.next();
                                            Group group1 = group_manger.searchGroup(name);
                                            if (group1 == null) {
                                                System.out.println("No Group available this name");
                                            } else {
                                                System.out.println(group1);
                                            }
                                            break;
                                        case 3:
                                            System.out.println("Enter the Group Name Which group do want you join");
                                            name = sc.next();
                                            Group group2 = group_manger.searchGroup(name);
                                            if (group2 == null) {
                                                System.out.println("Plz Enter a Valid Group Name");
                                            } else {
                                                String str;
                                                do {
                                                    System.out.println("Enter \ni. if admin \nii. if not admin but member \niii if not admin and not member \niv Back");
                                                    str = sc.next();
                                                    if (str.equals("i")) {
                                                        int i;
                                                        do {
                                                            System.out.println("Enter \n1 Add People \n2 Remove People \n3 Back");
                                                            i = sc.nextInt();
                                                            switch (i) {
                                                                case 1:
                                                                    System.out.println("Plz Enter The People Details");
                                                                    System.out.print("NAME :-");
                                                                    name = sc.next();
                                                                    System.out.print("EMAIL :-");
                                                                    String email = sc.next();
                                                                    user = new User(name, email);
                                                                    user_manger.addUser(user);
                                                                    System.out.println("People is added");
                                                                    break;
                                                                case 2:
                                                                    System.out.println("Enter the User Name do you want to remove");
                                                                    email = sc.next();
                                                                    if (user_manger.removeUser(email)) {
                                                                        System.out.println("User is remove");
                                                                    } else {
                                                                        System.out.println("Plz enter the valid email Address");
                                                                    }
                                                                    break;
                                                                case 3:
                                                                    break;
                                                                default:
                                                                    System.out.println("INVALID INPUT");
                                                                    break;
                                                            }
                                                        } while (i != 3);
                                                    } else if (str.equals("ii")) {
                                                        int ii;
                                                        do {
                                                            System.out.println("Enter \n1 Show whole chat \n2 Enable to chat in the group \n3 Leave the group \n4 Delete message \n5 Back");
                                                            ii = sc.nextInt();
                                                            switch (ii) {
                                                                case 1:
                                                                    System.out.println("Show whole chat");
                                                                    break;
                                                                case 2:
                                                                    System.out.println("Enable to chat in the group");
                                                                    break;
                                                                case 3:
                                                                    System.out.println("Leave the group");
                                                                    break;
                                                                case 4:
                                                                    System.out.println("Delete message");
                                                                    break;
                                                                case 5:
                                                                    break;
                                                                default:
                                                                    System.out.println("INVALID INPUT");
                                                            }
                                                        } while (ii != 5);
                                                    } else if (str.equals("iii")) {
                                                        int iii;
                                                        do {
                                                            System.out.println("Enter \n1 Request to join the group \n2 Cancel the request \n3 Back");
                                                            iii = sc.nextInt();
                                                            switch (iii) {
                                                                case 1:
                                                                    System.out.println("Request to join the group");
                                                                    break;
                                                                case 2:
                                                                    System.out.println("Cancel the request ");
                                                                    break;
                                                                case 3:
                                                                    break;
                                                                default:
                                                                    System.out.println("INVALID INPUT");
                                                                    break;
                                                            }
                                                        } while (iii != 3);
                                                    } else if (str.equals("iv")) {
                                                        break;
                                                    } else {
                                                        System.out.println("INVALID INPUT");
                                                    }
                                                } while (!str.equals("iv"));
                                            }
                                            break;
                                        case 4:
                                            break;
                                        default:
                                            System.out.println("INVALID INPUT");
                                            break;
                                    }
                                } while (jo_choice != 4);
                                break;
                            case 3:
                                System.out.println("Enter User Name do you want to chat");
                                String name1 = sc.next();
                                user = user_manger.get(name1);
                                try {
                                    if (user == null) {
                                        throw new User_Not_Found_Exception();
                                    } else {
                                        System.out.println("Enter \n 1 Chat With " + user.getName() + "\n 2 Delete Message");
                                        break;
                                    }
                                } catch (User_Not_Found_Exception u) {
                                    System.out.println(u);
                                }
                                break;
                        }
                    } while (us_Choice != 4);
                } catch (User_Not_Found_Exception u) {
                    System.out.println(u);
                }
            } else if (choice == 2) {
                System.out.println("What's Your Name");
                String name = sc.next();
                System.out.println("What's Your Email");
                String email = sc.next();
                System.out.println("Enter Password");
                String password = sc.next();
                user = new User(name, email, password);
                user_manger.addUser(user);
                System.out.println("Sing IN Successful");
            } else if (choice == 3) {
                System.out.println("Enter Your Old Password");
                String password = sc.nextLine();
                assert user != null;
                user.setPassword(password);
            }
        } while (choice != 4);
        user_manger.Write_File();
    }
}

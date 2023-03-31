package Pkg_Exception;

import java.io.Serial;

public class User_Not_Found_Exception extends Exception {
    @Serial
    private static final long serialVersionUID = 1l;

    public User_Not_Found_Exception() {
    }

    @Override
    public String toString() {
        return "User_Not_Found_Exception is Generated";
    }
}

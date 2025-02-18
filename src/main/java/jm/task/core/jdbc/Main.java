package jm.task.core.jdbc;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.service.UserService;
import jm.task.core.jdbc.service.UserServiceImpl;
import jm.task.core.jdbc.util.Util;

public class Main {
    public static void main(String[] args) {
        UserService userService = new UserServiceImpl ();
        userService.createUsersTable ();
        userService.saveUser ( "Igor", "Shevchenko", (byte) 24 );
        userService.saveUser ( "Maksim", "Maksimov", (byte) 18 );
        userService.saveUser ( "Programmer", "Javovich", (byte) 45 );
        userService.saveUser ( "Klaviatur", "Myshkin", (byte) 29 );
        for(User user : userService.getAllUsers ()) {
            System.out.println (user);
        }
        userService.cleanUsersTable ();
        userService.dropUsersTable ();
        Util.closeSessionFactory ();
    }
}

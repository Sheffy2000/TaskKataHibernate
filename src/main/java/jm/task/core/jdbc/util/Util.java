package jm.task.core.jdbc.util;

import jm.task.core.jdbc.model.User;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.sql.*;

public class Util {
    private static final String URL = "jdbc:mysql://localhost:3306/fortask2";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "ClassiC997521";

    private static Connection connection = null;
    private static SessionFactory sessionFactory = null;

    private Util() {
        throw new UnsupportedOperationException ( "This is a utility class and cannot be instantiated" );
    }

    public static Connection getConnection() {
        if (connection == null) {
            try {
                connection = DriverManager.getConnection ( URL, USERNAME, PASSWORD );
            } catch (SQLException e) {
                e.printStackTrace ();
            }
        }
        return connection;
    }

    public static void closeConnection() {
        if (connection != null) {
            try {
                connection.close ();
            } catch (SQLException e) {
                e.printStackTrace ();
            }
        }
    }

    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            try {
                Configuration configuration = new Configuration ();

                configuration.setProperty ( "hibernate.connection.driver_class", "com.mysql.cj.jdbc.Driver" );
                configuration.setProperty ( "hibernate.connection.url", URL );
                configuration.setProperty ( "hibernate.connection.username", USERNAME );
                configuration.setProperty ( "hibernate.connection.password", PASSWORD );
                configuration.setProperty ( "hibernate.show_sql", "true" );
//                configuration.setProperty ( "hibernate.hbm2ddl.auto", "update" );
                configuration.addAnnotatedClass ( User.class );
                sessionFactory = configuration.buildSessionFactory ();
            } catch (Exception e) {
                e.printStackTrace ();
            }
        }
        return sessionFactory;
    }

    public static void closeSessionFactory() {
        if (sessionFactory != null) {
            sessionFactory.close ();
        }
    }
}

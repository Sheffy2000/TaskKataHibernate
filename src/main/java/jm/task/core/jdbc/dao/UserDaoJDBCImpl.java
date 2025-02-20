package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDaoJDBCImpl implements UserDao {
    private final Connection connection;

    public UserDaoJDBCImpl() {
        this.connection = Util.getConnection ();
        try {
            connection.setAutoCommit ( false );
        } catch (SQLException e) {
            e.printStackTrace ();
        }
    }

    @Override
    public void createUsersTable() {
        String createTableSQL = "CREATE TABLE IF NOT EXISTS Users (" +
                "id BIGINT NOT NULL AUTO_INCREMENT, " +
                "name VARCHAR(50), " +
                "lastName VARCHAR(50), " +
                "age TINYINT, " +
                "PRIMARY KEY (id)" +
                ")";
        try (Statement stmt = connection.createStatement ()) {
            stmt.executeUpdate ( createTableSQL );
        } catch (SQLException e) {
            e.printStackTrace ();
        }
    }

    @Override
    public void dropUsersTable() {
        String dropTableSQL = "DROP TABLE IF EXISTS Users";
        try (Statement stmt = connection.createStatement ()) {
            stmt.executeUpdate ( dropTableSQL );
        } catch (SQLException e) {
            e.printStackTrace ();
        }
    }

    @Override
    public void saveUser(String name, String lastName, byte age) {
        String addSQL = "INSERT into Users (name, lastName, age) values (?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement ( addSQL )) {
            pstmt.setString ( 1, name );
            pstmt.setString ( 2, lastName );
            pstmt.setByte ( 3, age );
            int rows = pstmt.executeUpdate ();
            connection.commit ();
            System.out.println ( "User с именем -  " + name + " добавлен в базу данных" );
        } catch (SQLException e) {
            try {
                connection.rollback ();
            } catch (SQLException rollbackEx) {
                rollbackEx.printStackTrace ();
            }
            e.printStackTrace ();
        }
    }

    @Override
    public void removeUserById(long id) {
        String delSQL = "Delete from Users where id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement ( delSQL )) {
            pstmt.setLong ( 1, id );
            int cnt = pstmt.executeUpdate ();
            connection.commit ();
        } catch (SQLException e) {
            try {
                connection.rollback ();
            } catch (SQLException rollbackEx) {
                rollbackEx.printStackTrace ();
            }
            e.printStackTrace ();
        }
    }

    @Override
    public List<User> getAllUsers() {
        String getAllSQL = "SELECT * FROM Users";
        List<User> users = new ArrayList<> ();
        try (PreparedStatement pstmt = connection.prepareStatement ( getAllSQL );
             ResultSet rs = pstmt.executeQuery ()) {
            while (rs.next ()) {
                Long id = rs.getLong ( "id" );
                User user = new User ( rs.getString ( "name" ),
                        rs.getString ( "lastName" ),
                        rs.getByte ( "age" ) );
                user.setId ( id );
                users.add ( user );
            }
        } catch (SQLException e) {
            e.printStackTrace ();
        }
        return users;

    }

    @Override
    public void cleanUsersTable() {
        String cleanSQL = "TRUNCATE TABLE Users";
        try (Statement stmt = connection.createStatement ()) {
            stmt.executeUpdate ( cleanSQL );
        } catch (SQLException e) {
            e.printStackTrace ();
        }
    }
}

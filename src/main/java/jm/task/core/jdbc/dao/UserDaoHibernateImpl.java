package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;

public class UserDaoHibernateImpl implements UserDao {
    private SessionFactory sessionFactory = Util.getSessionFactory ();

    public UserDaoHibernateImpl() {

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
        Session session = sessionFactory.openSession ();
        Transaction tx = session.beginTransaction ();
        session.createSQLQuery ( createTableSQL ).executeUpdate ();
        tx.commit ();
        session.close ();
    }

    @Override
    public void dropUsersTable() {
        String dropTableSQL = "DROP TABLE IF EXISTS Users";
        Session session = sessionFactory.openSession ();
        Transaction tx = session.beginTransaction ();
        session.createSQLQuery ( dropTableSQL ).executeUpdate ();
        tx.commit ();
        session.close ();
    }

    @Override
    public void saveUser(String name, String lastName, byte age) {
        User user = new User ( name, lastName, age );
        Session session = sessionFactory.openSession ();
        Transaction tx = session.beginTransaction ();
        session.persist ( user );
        tx.commit ();
        session.close ();
    }

    @Override
    public void removeUserById(long id) {
        Session session = sessionFactory.openSession ();
        Transaction tx = session.beginTransaction ();
        User user = session.find ( User.class, id );
        session.remove ( user );
        tx.commit ();
        session.close ();
    }

    @Override
    public List<User> getAllUsers() {
        Session session = sessionFactory.openSession ();
        Transaction tx = session.beginTransaction ();
        Query<User> query = session.createQuery ( "from User", User.class );
        List<User> users = query.list ();
        tx.commit ();
        session.close ();
        return users;
    }

    @Override
    public void cleanUsersTable() {
        Session session = sessionFactory.openSession ();
        Transaction tx = session.beginTransaction ();
        session.createQuery ( "DELETE FROM User" ).executeUpdate ();
        tx.commit ();
        session.close ();
    }
}

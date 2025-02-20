package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;

public class UserDaoHibernateImpl implements UserDao {
    private final SessionFactory sessionFactory = Util.getSessionFactory ();

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
        try (Session session = sessionFactory.openSession ()) {
            Transaction tx = session.beginTransaction ();
            try {
                session.createSQLQuery ( createTableSQL ).executeUpdate ();
                tx.commit ();
            } catch (Exception e) {
                if (tx != null) {
                    tx.rollback ();
                }
                throw e;
            }
        }
    }

    @Override
    public void dropUsersTable() {
        String dropTableSQL = "DROP TABLE IF EXISTS Users";
        try (Session session = sessionFactory.openSession ()) {
            Transaction tx = session.beginTransaction ();
            try {
                session.createSQLQuery ( dropTableSQL ).executeUpdate ();
                tx.commit ();
            } catch (Exception e) {
                if (tx != null) {
                    tx.rollback ();
                }
                throw e;
            }
        }
    }

    @Override
    public void saveUser(String name, String lastName, byte age) {
        User user = new User ( name, lastName, age );
        try (Session session = sessionFactory.openSession ()) {
            Transaction tx = session.beginTransaction ();
            try {
                session.persist ( user );
                tx.commit ();
            } catch (Exception e) {
                if (tx != null) {
                    tx.rollback ();
                }
                throw e;
            }
        }
    }

    @Override
    public void removeUserById(long id) {
        try (Session session = sessionFactory.openSession ()) {
            Transaction tx = session.beginTransaction ();
            try {
                User user = session.find ( User.class, id );
                session.remove ( user );
                tx.commit ();
            } catch (Exception e) {
                if (tx != null) {
                    tx.rollback ();
                }
                throw e;
            }
        }
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
        try (Session session = sessionFactory.openSession ()) {
            Transaction tx = session.beginTransaction ();
            try {
                session.createQuery ( "DELETE FROM User" ).executeUpdate ();
                tx.commit ();
            } catch (Exception e) {
                if (tx != null) {
                    tx.rollback ();
                }
                throw e;
            }
        }
    }
}

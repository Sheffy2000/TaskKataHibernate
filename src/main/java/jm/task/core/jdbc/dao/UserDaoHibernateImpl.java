package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
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
            session.createSQLQuery ( createTableSQL ).executeUpdate ();
            tx.commit ();
        } catch (HibernateException e) {
            throw e;
        }
    }

    @Override
    public void dropUsersTable() {
        String dropTableSQL = "DROP TABLE IF EXISTS Users";
        try (Session session = sessionFactory.openSession ()) {
            Transaction tx = session.beginTransaction ();
            session.createSQLQuery ( dropTableSQL ).executeUpdate ();
            tx.commit ();
        } catch (HibernateException e) {
            throw e;
        }
    }

    @Override
    public void saveUser(String name, String lastName, byte age) {
        User user = new User ( name, lastName, age );
        Transaction tx = null;
        try (Session session = sessionFactory.openSession ()) {
            tx = session.beginTransaction ();
            session.persist ( user );
            tx.commit ();
        } catch (HibernateException e) {
            if (tx != null) tx.rollback ();
            throw e;
        }
    }

    @Override
    public void removeUserById(long id) {
        Transaction tx = null;
        try (Session session = sessionFactory.openSession ()) {
            tx = session.beginTransaction ();
            User user = session.find ( User.class, id );
            if (user != null) {
                session.remove ( user );
            } else {
                System.out.println ( "Такого пользователя нет :(" );
            }
            tx.commit ();
        } catch (HibernateException e) {
            if (tx != null) tx.rollback ();
            throw e;
        }
    }

    @Override
    public List<User> getAllUsers() {
        try (Session session = sessionFactory.openSession ()) {
            Query<User> query = session.createQuery ( "from User", User.class );
            List<User> users = query.list ();
            return users;
        } catch (HibernateException e) {
            throw e;
        }
    }

    @Override
    public void cleanUsersTable() {
        Transaction tx = null;
        try (Session session = sessionFactory.openSession ()) {
            tx = session.beginTransaction ();
            session.createQuery ( "DELETE FROM User" ).executeUpdate ();
            tx.commit ();
        } catch (HibernateException e) {
            if (tx != null) tx.rollback ();
            throw e;
        }
    }
}

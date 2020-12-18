package projet.java.service;

import org.hibernate.Session;
import org.hibernate.Transaction;
import projet.java.model.Admin;
import projet.java.model.Profil;
import projet.java.model.User;
import projet.java.utils.HibernateUtil;

import javax.persistence.Query;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

public class UserDao extends UnicastRemoteObject implements IUser{
    private Session session;
    public UserDao() throws RemoteException {
        session = HibernateUtil.getSession();

    }

    @Override
    public User add(User user) throws RemoteException {
        Transaction t = session.getTransaction();
        try {
            t.begin();
            session.save(user);
            t.commit();
        } catch (Exception e){

            e.printStackTrace();
        }

        return user;
    }

    @Override
    public User updateUser(User user) throws RemoteException {
        Transaction t = session.getTransaction();
        try {
            t.begin();
            session.merge(user);

            t.commit();
        } catch (Exception e){

            e.printStackTrace();
        }

        return user;
    }

    @Override
    public User find(String login) throws RemoteException {
        Transaction t = session.getTransaction();
        User user = null;
        Query query = session.createQuery("SELECT u FROM User u WHERE u.login=:login  ");
        query.setParameter("login", login);

        try {
            user= (User) query.getSingleResult();
        } catch (Exception e) {
            // Handle exception
        }
        if(user!= null)
            return user;
        else
            return null;
    }

    @Override
    public User findByUser(String mail, String pwd) throws RemoteException {
        Transaction t = session.getTransaction();
        User user = null;
        Query query = session.createQuery("SELECT u FROM User u WHERE u.login=:login AND u.password=:pwd  ");
        query.setParameter("login", mail);
        query.setParameter("pwd", pwd);

        try {
            user = (User) query.getSingleResult();
        } catch (Exception e) {
            // Handle exception
        }
        if(user!= null)
            return user;
        else
            return null;

    }

    @Override
    public boolean delate(long l) throws RemoteException {
        Transaction t = session.getTransaction();
        try {
            t.begin();
            User user = session.get(User.class, l);
            if (user != null) {
                String hql = "DELETE FROM User u " + "WHERE id = :userId";
                Query query = session.createQuery(hql);
                query.setParameter("userid", l);
                int result = query.executeUpdate();
                System.out.println("user supprimer: " + result);
            }
            t.commit();
        }catch (Exception e){
            t.rollback();
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public List<User> findAll() throws RemoteException {
        return session.createQuery("SELECT u FROM User u").list();
    }
}

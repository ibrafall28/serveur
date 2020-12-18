package projet.java.service;

import org.hibernate.Session;
import org.hibernate.Transaction;
import projet.java.model.Achat1;
import projet.java.model.Admin;
import projet.java.model.Entres;
import projet.java.utils.HibernateUtil;

import javax.persistence.Query;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

public class AdminDao extends UnicastRemoteObject implements IAdmin{
    private Session session;
    public AdminDao() throws RemoteException {
        session = HibernateUtil.getSession();

    }

    @Override
    public Admin add(Admin admin) throws RemoteException {
        return null;
    }

    @Override
    public Achat1 addAchat(Achat1 achat1) throws RemoteException {
        Transaction t = session.getTransaction();
        try {
            t.begin();
            session.merge(achat1);

            t.commit();
        } catch (Exception e){

            e.printStackTrace();
        }

        return achat1;
    }

    @Override
    public Admin update(Admin admin) throws RemoteException {
        return null;
    }

    @Override
    public Admin find(String login,String pwd) throws RemoteException {
        Transaction t = session.getTransaction();
        Admin admin = null;
        Query query = session.createQuery("SELECT a FROM Admin a WHERE a.login=:login AND a.password=:pwd ");
        query.setParameter("login", login);
        query.setParameter("pwd", pwd);


        try {
            admin = (Admin) query.getSingleResult();
        } catch (Exception e) {
            // Handle exception
        }
        if(admin!= null)
            return admin;
        else
            return null;

    }

    @Override
    public Admin verifiermail(String login) throws RemoteException {
        Transaction t = session.getTransaction();
        Admin admin = null;
        Query query = session.createQuery("SELECT a FROM Admin a WHERE a.login=:login  ");
        query.setParameter("login", login);


        try {
            admin = (Admin) query.getSingleResult();
        } catch (Exception e) {
            // Handle exception
        }
        if(admin!= null)
            return admin;
        else
            return null;
    }

    @Override
    public void delate() throws RemoteException {
        Transaction t = session.getTransaction();
        try {
            t.begin();

                String hql = "DELETE  FROM Achat1  ";
                Query query = session.createQuery(hql);
                int result = query.executeUpdate();
            t.commit();
        }catch (Exception e){
            t.rollback();
            e.printStackTrace();
        }


    }

    @Override
    public List<Achat1> findAll() throws RemoteException {
        return session.createQuery("SELECT a FROM Achat1 a ").list();

    }
}

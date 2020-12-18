package projet.java.service;

import org.hibernate.Session;
import org.hibernate.Transaction;
import projet.java.model.Entres;
import projet.java.model.User;
import projet.java.utils.HibernateUtil;

import javax.persistence.Query;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class EntreDao  extends UnicastRemoteObject implements IEntres {
    Session session;
    public EntreDao() throws RemoteException {
        session = HibernateUtil.getSession();

    }

    @Override
    public Entres add(Entres entres) throws RemoteException {
        Transaction t = session.getTransaction();
        try {
            t.begin();
            session.save(entres);
            t.commit();
        } catch (Exception e){

            e.printStackTrace();
        }

        return entres;
    }

    @Override
    public Entres update(Entres entres) throws RemoteException {
        Transaction t = session.getTransaction();
        try {
            t.begin();
            session.merge(entres);

            t.commit();
        } catch (Exception e){

            e.printStackTrace();
        }

        return entres;
    }

    @Override
    public double sommetotal() throws RemoteException {
        Transaction t = session.getTransaction();
        String sumHql = "SELECT SUM(total) FROM Entres ";
        Query sumQuery = session.createQuery(sumHql);
        System.out.println(((org.hibernate.query.Query) sumQuery).list().get(0));
        return Double.parseDouble(sumHql);
    }

    @Override
    public List<Entres> findbyDate(Date date) throws RemoteException {
        Transaction t = session.getTransaction();
        List<Entres>list = new ArrayList<>();
        Entres entres = null;
        Query query = session.createQuery("SELECT e FROM Entres e WHERE e.dateentre=:dateentre  ");
        query.setParameter("dateentre", date);

        return (List<Entres>) query.getResultList();
    }

    @Override
    public boolean delate(long l) throws RemoteException {
        Transaction t = session.getTransaction();
        try {
            t.begin();
            Entres entres = session.get(Entres.class, l);
            if (entres != null) {
                String hql = "DELETE FROM Entres e " + "WHERE id = :entreid";
                Query query = session.createQuery(hql);
                query.setParameter("entreid", l);
                int result = query.executeUpdate();
                System.out.println("Entres supprimer: " + result);
            }
            t.commit();
        }catch (Exception e){
            t.rollback();
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public List<Entres> findAll() throws RemoteException {
        return session.createQuery("SELECT e FROM Entres e").list();

    }

    @Override
    public List<Entres> findAllbystock(long id) throws RemoteException {
        Transaction t = session.getTransaction();
        List<Entres>list = new ArrayList<>();
        Entres entres = null;
        Query query = session.createQuery("SELECT e FROM Entres e WHERE e.produit.id=:id  ");
        query.setParameter("id", id);

        return (List<Entres>) query.getResultList();
    }
}

package projet.java.service;

import org.hibernate.Session;
import org.hibernate.Transaction;
import projet.java.model.*;
import projet.java.utils.HibernateUtil;

import javax.persistence.Query;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class FactureDao  extends UnicastRemoteObject implements IFacture {
    Session session;
    public FactureDao() throws RemoteException {
        session = HibernateUtil.getSession();

    }

    @Override
    public Facture add(Facture facture) throws RemoteException {
        Transaction t = session.getTransaction();
        try {
            t.begin();
            session.save(facture);
            t.commit();
        } catch (Exception e){

            e.printStackTrace();
        }

        return facture;
    }

    @Override
    public Facture update(Facture facture) throws RemoteException {
        Transaction t = session.getTransaction();
        try {
            t.begin();
            session.merge(facture);

            t.commit();
        } catch (Exception e){

            e.printStackTrace();
        }

        return facture;
    }

    @Override
    public Facture find(String numero) throws RemoteException {
        Transaction t = session.getTransaction();
        Facture facture = null;
        Query query = session.createQuery("SELECT f FROM Facture f WHERE f.numero=:numero  ");
        query.setParameter("numero", numero);

        try {
            facture= (Facture) query.getSingleResult();
        } catch (Exception e) {
            // Handle exception
        }
        if(facture!= null)
            return facture;
        else
            return null;
    }

    @Override
    public Facture delate(long l) throws RemoteException {
        Transaction t = session.getTransaction();
        try {
            t.begin();
            Facture facture = session.get(Facture.class, l);
            if (facture != null) {
                String hql = "DELETE FROM Facture f " + "WHERE id = :factureId";
                Query query = session.createQuery(hql);
                query.setParameter("factureId", l);
                int result = query.executeUpdate();
                System.out.println("facture supprimer: " + result);
            }
            t.commit();
        }catch (Exception e){
            t.rollback();
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public List<Facture> finAllbydate(Date date) throws RemoteException {
        Transaction t = session.getTransaction();
        List<Entres>list = new ArrayList<>();
        Entres entres = null;
        Query query = session.createQuery("SELECT f FROM Facture f WHERE f.datefacture=:datefacture  ");
        query.setParameter("datefacture", date);

        return (List<Facture>) query.getResultList();
    }

    @Override
    public List<Facture> findAll() throws RemoteException {
        return session.createQuery("SELECT f FROM Facture f").list();

    }

    @Override
    public List<DetailFacture> findAllbydetaill() throws RemoteException {
        return session.createQuery("SELECT d FROM DetailFacture d").list();
    }

    @Override
    public DetailFacture ajout(DetailFacture detailFacture) throws RemoteException {
        Transaction t = session.getTransaction();
        try {
            t.begin();
            session.save(detailFacture);
            t.commit();
        } catch (Exception e){

            e.printStackTrace();
        }

        return detailFacture;
    }

    @Override
    public DetailFacture modifier(DetailFacture detailFacture) throws RemoteException {
        return null;
    }

    @Override
    public List<DetailFacture> findbydetail(long l) throws RemoteException {
        return null;
    }

    @Override
    public boolean Supprimer(long id) throws RemoteException {
        Transaction t = session.getTransaction();
        try {
            t.begin();
            Commande commande = session.get(Commande.class, id);
            if (commande != null) {
                String hql = "DELETE FROM DetailFacture d " + "WHERE d.facture.id = :factureId";
                Query query = session.createQuery(hql);
                query.setParameter("factureId", id);
                int result = query.executeUpdate();
                System.out.println("Facture supprimer: " + result);
            }
            t.commit();
        }catch (Exception e){
            t.rollback();
            e.printStackTrace();
        }
        return false;
    }
    }


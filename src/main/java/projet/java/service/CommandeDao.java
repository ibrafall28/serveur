package projet.java.service;

import org.hibernate.Session;
import org.hibernate.Transaction;
import projet.java.model.*;
import projet.java.utils.HibernateUtil;

import javax.persistence.Query;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CommandeDao  extends UnicastRemoteObject implements ICommande{
    private Session session;
    public CommandeDao() throws RemoteException {
        session = HibernateUtil.getSession();
    }


    @Override
    public Commande add(Commande commande) throws RemoteException {
        Transaction t = session.getTransaction();
        try {
            t.begin();
            session.save(commande);
            t.commit();
        } catch (Exception e){

            e.printStackTrace();
        }

        return commande;
    }

    @Override
    public Commande update(Commande commande) throws RemoteException {
        Transaction t = session.getTransaction();
        try {
            t.begin();
            session.merge(commande);

            t.commit();
        } catch (Exception e){

            e.printStackTrace();
        }

        return commande;
    }

    @Override
    public Commande find(long id) throws RemoteException {
        Transaction t = session.getTransaction();
        Commande commande=null;

        Query query = session.createQuery("SELECT d FROM DetailCommande d WHERE d.Commande.id=:idc  ");
        query.setParameter("idc", id);
        try {
            commande = (Commande) query.getSingleResult();
        } catch (Exception e) {
            // Handle exception
        }
        if(commande!= null)
            return commande;
        else
            return null;
    }

    @Override
    public Client entreprise(String nom) throws RemoteException {
        Transaction t = session.getTransaction();
       Client client=null;

        Query query = session.createQuery("SELECT c FROM Commande c WHERE c.client.nom=:nom  ");
        query.setParameter("nom", nom);
          if(client != null){
              return client;
          }else {
              return null;
          }
    }

    @Override
    public Client personne(long id) throws RemoteException {
        Transaction t = session.getTransaction();
        Client client=null;

        Query query = session.createQuery("SELECT d FROM DetailCommande d WHERE d.client.id=:idp  ");
        query.setParameter("idp", id);
        try {
            client = (Client) query.getSingleResult();
        } catch (Exception e) {
            // Handle exception
        }
        if(client!= null)
            return client;
        else
            return null;
    }

    @Override
    public List<Commande> findbyDate(Date date) throws RemoteException {
        Transaction t = session.getTransaction();
        List<Commande>list = new ArrayList<>();
        Query query = session.createQuery("SELECT c FROM Commande c WHERE c.dateLivraision=:dateLivraision  ");
        query.setParameter("dateLivraision", date);

        return (List<Commande>) query.getResultList();
    }

    @Override
    public boolean delate(long l) throws RemoteException {
        Transaction t = session.getTransaction();
        try {
            t.begin();
            Commande commande = session.get(Commande.class, l);
            if (commande != null) {
                String hql = "DELETE FROM Commande c " + "WHERE id = :commandeid";
                Query query = session.createQuery(hql);
                query.setParameter("commandeid", l);
                int result = query.executeUpdate();
                System.out.println("commande supprimer: " + result);
            }
            t.commit();
        }catch (Exception e){
            t.rollback();
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public List<Commande> findAll() throws RemoteException {
        return session.createQuery("SELECT c FROM Commande c").list();
    }

    @Override
    public List<DetailCommande> findAllbydetaill() throws RemoteException {
        return session.createQuery("SELECT d FROM DetailCommande d").list();

    }

    @Override
    public DetailCommande ajout(DetailCommande detailCommande) throws RemoteException {
        Transaction t = session.getTransaction();
        try {
            t.begin();
            session.save(detailCommande);
            t.commit();
        } catch (Exception e){

            e.printStackTrace();
        }

        return detailCommande;
    }

    @Override
    public DetailCommande modifier(DetailCommande detailCommande) throws RemoteException {
        Transaction t = session.getTransaction();
        try {
            t.begin();
            session.merge(detailCommande);
            t.commit();
        } catch (Exception e){

            e.printStackTrace();
        }

        return detailCommande;
    }

    @Override
    public List<DetailCommande> findbydetail(long id) throws RemoteException {
        Transaction t = session.getTransaction();
        List<DetailCommande>list = new ArrayList<>();

        Query query = session.createQuery("SELECT d FROM DetailCommande d WHERE d.commande.id=:id  ");
        query.setParameter("id", id);

        return (List<DetailCommande>) query.getResultList();
    }

    @Override
    public boolean Supprimer(long id) throws RemoteException {
        Transaction t = session.getTransaction();
        try {
            t.begin();
            Commande commande = session.get(Commande.class, id);
            if (commande != null) {
                String hql = "DELETE FROM DetailCommande d " + "WHERE d.commande.id = :commandeId";
                Query query = session.createQuery(hql);
                query.setParameter("commandeId", id);
                int result = query.executeUpdate();
                System.out.println("produit supprimer: " + result);
            }
            t.commit();
        }catch (Exception e){
            t.rollback();
            e.printStackTrace();
        }
       return false;
    }
}

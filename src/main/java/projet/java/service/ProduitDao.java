package projet.java.service;

import org.hibernate.Session;
import org.hibernate.Transaction;
import projet.java.model.Entres;
import projet.java.model.Produit;
import projet.java.model.User;
import projet.java.utils.HibernateUtil;

import javax.persistence.Query;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

public class ProduitDao  extends UnicastRemoteObject implements IProduit {
    Session session;
    public ProduitDao() throws RemoteException {
        session = HibernateUtil.getSession();

    }

    @Override
    public Produit add(Produit produit) throws RemoteException {
        Transaction t = session.getTransaction();
        try {
            t.begin();
            session.save(produit);
            t.commit();
        } catch (Exception e){

            e.printStackTrace();
        }

        return produit;
    }

    @Override
    public Produit update(Produit produit) throws RemoteException {
        Transaction t = session.getTransaction();
        try {
            t.begin();
            session.merge(produit);

            t.commit();
        } catch (Exception e){

            e.printStackTrace();
        }

        return produit;
    }

    @Override
    public Produit updatebystock(long stock, long id) throws RemoteException {
        try {

            Transaction t = session.getTransaction();
            t.begin();
            String hqlUpdate = "update Produit p set p.stock = :stock where p.id = :id";
// or String hqlUpdate = "update Customer set name = :newName where name = :oldName";
            int query = session.createQuery( hqlUpdate )
                    .setParameter( "stock", stock )
                    .setParameter( "id", id )
                    .executeUpdate();
            t.commit();
            //session.close();
        }catch (Exception e){
            e.printStackTrace();
        }
        return  null;
    }

    @Override
    public Produit find(String libelle) throws RemoteException {
        Transaction t = session.getTransaction();
        Produit produit = null;
        Query query = session.createQuery("SELECT p FROM Produit p WHERE p.libelle=:libelle  ");
        query.setParameter("libelle", libelle);

        try {
            produit= (Produit) query.getSingleResult();
        } catch (Exception e) {
        }
        if(produit!= null)
            return produit;
        else
            return null;
    }

    @Override
    public Produit findbyid(long id) throws RemoteException {
        Transaction t = session.getTransaction();
        Produit produit = null;
        Query query = session.createQuery("SELECT p FROM Produit p WHERE p.id=:id  ");
        query.setParameter("id", id);

        try {
            produit= (Produit) query.getSingleResult();
        } catch (Exception e) {
        }
        if(produit!= null)
            return produit;
        else
            return null;    }

    @Override
    public Produit delate(long l) throws RemoteException {
        Transaction t = session.getTransaction();
        try {
            t.begin();
            Produit produit = session.get(Produit.class, l);
            if (produit != null) {
                String hql = "DELETE FROM Produit p " + "WHERE id = :produitId";
                Query query = session.createQuery(hql);
                query.setParameter("produitId", l);
                int result = query.executeUpdate();
                System.out.println("produit supprimer: " + result);
            }
            t.commit();
        }catch (Exception e){
            t.rollback();
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public List<Entres> finAllbystock(long l) throws RemoteException {
        Transaction t = session.getTransaction();
        Entres entres = null;
        List<Entres>list = new ArrayList<>();
        Query query = session.createQuery("From Entres e WHERE e.produit.id = :id");
        query.setParameter("id", l);

        return (List<Entres>) query.getResultList();

    }

    @Override
    public List<Produit> findAll() throws RemoteException {
        return session.createQuery("SELECT p FROM Produit p").list();

    }
}

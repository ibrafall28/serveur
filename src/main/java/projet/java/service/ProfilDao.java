package projet.java.service;

import org.hibernate.Session;
import org.hibernate.Transaction;
import projet.java.model.Profil;
import projet.java.utils.HibernateUtil;

import javax.persistence.Query;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

public class ProfilDao  extends UnicastRemoteObject implements IPofil {
    private Session session;
    public ProfilDao() throws RemoteException {
        session = HibernateUtil.getSession();
    }

    @Override
    public Profil add(Profil profil) throws RemoteException {
        Transaction t = session.getTransaction();
        try {
            t.begin();
            session.save(profil);
            t.commit();
        }catch (Exception e){
            t.rollback();
            e.printStackTrace();
        }
        return profil;
    }

    @Override
    public Profil updateprofil(Profil profil) throws RemoteException {
        Transaction t = session.getTransaction();
        try {
            t.begin();
           session.merge(profil);
            t.commit();
        } catch (Exception e){

            e.printStackTrace();
        }

        return profil;
    }

    @Override
    public Profil find(String nom) throws RemoteException {
        Transaction t = session.getTransaction();
        Profil profil = null;
        Query query = session.createQuery("SELECT p FROM Profil p WHERE p.nom=:nom  ");
        query.setParameter("nom", nom);

        try {
            profil = (Profil) query.getSingleResult();
        } catch (Exception e) {
            // Handle exception
        }
        if(profil!= null)
            return profil;
        else
            return null;
    }

    @Override
    public boolean delate(long l) throws RemoteException {
        Transaction t = session.getTransaction();
        try {
            t.begin();
            Profil profil = session.get(Profil.class, l);
            if (profil != null) {
                String hql = "DELETE FROM Profil p " + "WHERE id = :profilId";
                Query query = session.createQuery(hql);
                query.setParameter("profilId", l);
                int result = query.executeUpdate();
                System.out.println("Profil supprimer: " + result);
            }
            t.commit();
        }catch (Exception e){
            t.rollback();
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public List<Profil> findAll() throws RemoteException {
        return session.createQuery("SELECT p FROM Profil p").list();
    }
}

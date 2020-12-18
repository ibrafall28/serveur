package projet.java.service;




import org.hibernate.Session;
import org.hibernate.Transaction;
import projet.java.model.Client;
import projet.java.model.Client;
import projet.java.model.Entres;
import projet.java.model.User;
import projet.java.utils.HibernateUtil;

import javax.persistence.PersistenceException;
import javax.persistence.Query;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

public class ClientDao extends UnicastRemoteObject implements IClient{
    private Session session;
    public ClientDao() throws RemoteException {
      session = HibernateUtil.getSession();
    }


    @Override
    public Client add(Client client) throws RemoteException {
        Transaction t = session.getTransaction();
        try {
            t.begin();
            session.save(client);

            t.commit();
        } catch (Exception e){

            e.printStackTrace();
        }

        return client;
    }


    @Override
    public Client update(Client client) throws RemoteException {
        Transaction t = session.getTransaction();
        try {
            t.begin();
            session.merge(client);

            t.commit();
        } catch (Exception e){

            e.printStackTrace();
        }

        return client;
    }

    @Override
    public Client find(String nom) throws RemoteException {
        Transaction t = session.getTransaction();
        Client client = null;
        Query query = session.createQuery("SELECT c FROM Client c WHERE e.nom=:nom  ");
        query.setParameter("nom", nom);

        try {
            client= (Client) query.getSingleResult();
        } catch (Exception e) {
            // Handle exception
        }
        if(client!= null)
            return client;
        else
            return null;
    }

    @Override
    public Client delate(long l) throws RemoteException {

        Transaction t = session.getTransaction();
        try {
            t.begin();
            Client entreprise = session.get(Client.class, l);
            if (entreprise != null) {
                String hql = "DELETE FROM Client e " + "WHERE id = :clientId";
                Query query = session.createQuery(hql);
                query.setParameter("clientId", l);
                int result = query.executeUpdate();
                System.out.println("client supprimer: " + result);
            }
            t.commit();
        }catch (Exception e){
            t.rollback();
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public List<Client> findbynumero(String numero) throws RemoteException {
        Transaction t = session.getTransaction();
        List<Client>list = new ArrayList<>();
        Query query = session.createQuery("SELECT c FROM Client c WHERE c.numero=:numero  ");
        query.setParameter("numero", numero);

        return (List<Client>) query.getResultList();
    }

    @Override
    public Client findbycni(String cni) throws RemoteException {
        Transaction t = session.getTransaction();
        Client client = null;
        Query query = session.createQuery("SELECT c FROM Client c WHERE c.cni=:cni  ");
        query.setParameter("cni", cni);

        try {
            client= (Client) query.getSingleResult();
        } catch (Exception e) {
            // Handle exception
        }
        if(client!= null)
            return client;
        else
            return null;
    }

    @Override
    public Client findbytel(String tel) throws RemoteException {
        Transaction t = session.getTransaction();
        Client client = null;
        Query query = session.createQuery("SELECT c FROM Client c WHERE c.telephone=:tel  ");
        query.setParameter("tel", tel);

        try {
            client= (Client) query.getSingleResult();
        } catch (Exception e) {
            // Handle exception
        }
        if(client!= null)
            return client;
        else
            return null;    }

    @Override
    public List<Client> findAll() throws RemoteException {
        return session.createQuery("SELECT c FROM Client c").list();

    }
}

import projet.java.service.*;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {

        try {
            System.setSecurityManager(new SecurityManager());
            Registry registry = LocateRegistry.createRegistry(5003);

          IClient iClient = new ClientDao();
            registry.bind("clientRemote", iClient);

            ICommande iCommande = new CommandeDao();
            registry.bind("commandeRemote", iCommande);

            IFacture iFacture = new FactureDao();
            registry.bind("factureRemote", iFacture);



            IEntres iEntres = new EntreDao();
            registry.bind("entresRemote", iEntres);

            IUser iUser = new UserDao();
            registry.bind("userRemote", iUser);



            IPofil  iPofil = new ProfilDao();
            registry.bind("profilRemote", iPofil);

            IProduit iproduit = new ProduitDao();
            registry.bind("produitRemote", iproduit);

            IAdmin iAdmin = new AdminDao();
            registry.bind("adminRemote", iAdmin);


            System.out.println("Serveur lance sur le port 5003");
        }
        catch(Exception ex){
            ex.printStackTrace();
        }
    }
    }


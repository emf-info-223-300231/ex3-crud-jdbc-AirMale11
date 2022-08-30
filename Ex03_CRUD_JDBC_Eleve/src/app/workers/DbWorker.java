package app.workers;

import app.beans.Personne;
import app.exceptions.MyDBException;
import app.helpers.SystemLib;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class DbWorker implements DbWorkerItf {

    private Connection dbConnexion;
    private List<Personne> listePersonnes;
    private int index = 0;

    /**
     * Constructeur du worker
     */
    public DbWorker() {
    }

    @Override
    public void connecterBdMySQL(String nomDB) throws MyDBException {
        final String url_local = "jdbc:mysql://localhost:3306/" + nomDB;
        final String url_remote = "jdbc:mysql://172.23.85.187:3306/" + nomDB;
        final String user = "223";
        final String password = "emf123";

        System.out.println("url:" + url_remote);
        try {
            dbConnexion = DriverManager.getConnection(url_remote, user, password);
        } catch (SQLException ex) {
            throw new MyDBException(SystemLib.getFullMethodName(), ex.getMessage());
        }
    }

    @Override
    public void connecterBdHSQLDB(String nomDB) throws MyDBException {
        final String url = "jdbc:hsqldb:file:" + nomDB + ";shutdown=true";
        final String user = "SA";
        final String password = "";
        System.out.println("url:" + url);
        try {
            dbConnexion = DriverManager.getConnection(url, user, password);
        } catch (SQLException ex) {
            throw new MyDBException(SystemLib.getFullMethodName(), ex.getMessage());
        }
    }

    @Override
    public void connecterBdAccess(String nomDB) throws MyDBException {
        final String url = "jdbc:ucanaccess://" + nomDB;
        System.out.println("url=" + url);
        try {
            dbConnexion = DriverManager.getConnection(url);
        } catch (SQLException ex) {
            throw new MyDBException(SystemLib.getFullMethodName(), ex.getMessage());
        }
    }

    @Override
    public void deconnecter() throws MyDBException {
        try {
            if (dbConnexion != null) {
                dbConnexion.close();
            }
        } catch (SQLException ex) {
            throw new MyDBException(SystemLib.getFullMethodName(), ex.getMessage());
        }
    }

    public List<Personne> lirePersonnes() throws MyDBException {
        listePersonnes = new ArrayList<>();
         try {
            Statement st = dbConnexion.createStatement();
            ResultSet rs = st.executeQuery("select PK_PERS, Nom, Prenom,"
                    + " Date_naissance, No_rue, Rue, NPA, Ville, Actif, Salaire,"
                    + " date_modif from t_personne");
            while (rs.next()) {
                Personne current = new Personne(rs.getInt("PK_PERS"), 
                        rs.getString("Nom"),
                        rs.getString("Prenom"),
                        rs.getDate("Date_naissance"),
                        rs.getInt("No_rue"),
                        rs.getString("Rue"),
                        rs.getInt("NPA"),
                        rs.getString("Ville"),
                        rs.getBoolean("Actif"),
                        rs.getDouble("Salaire"),
                        rs.getDate("date_modif"));
                listePersonnes.add(current);
            }

        } catch (SQLException e) {
        }
        return listePersonnes;
    }
    
    public void creer(Personne p){
        
        
        listePersonnes.add(p);
        
    }
    
    public void effacer(Personne p){
        listePersonnes.remove(p);
    }
    
    public Personne lire(int num){
        
    }
    
    public void modifier(Personne p){
        
    }

    
    
    

}

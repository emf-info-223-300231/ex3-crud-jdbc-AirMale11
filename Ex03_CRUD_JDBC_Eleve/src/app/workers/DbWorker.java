package app.workers;

import app.beans.Personne;
import app.exceptions.MyDBException;
import app.helpers.DateTimeLib;
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
                double salaire = 0.0;
                if (!rs.wasNull()) {
                    salaire = rs.getDouble("Salaire");
                }
                Personne current = new Personne(rs.getInt("PK_PERS"),
                        rs.getString("Nom"),
                        rs.getString("Prenom"),
                        rs.getDate("Date_naissance"),
                        rs.getInt("No_rue"),
                        rs.getString("Rue"),
                        rs.getInt("NPA"),
                        rs.getString("Ville"),
                        rs.getBoolean("Actif"),
                        salaire,
                        rs.getDate("date_modif"));
                listePersonnes.add(current);
            }

        } catch (SQLException e) {
        }
        return listePersonnes;
    }

    public void creer(Personne p) {
        if (p != null) {
            listePersonnes.add(p);
            String prep = "INSERT INTO t_personne VALUES (DEFAULT,?,?,?,?,?,?,?,?,?,?,DEFAULT);";
            try ( PreparedStatement ps = dbConnexion.prepareStatement(prep)) {
                ps.setString(1, p.getPrenom());
                ps.setString(2, p.getNom());
                ps.setDate(3, new java.sql.Date(p.getDateNaissance().getTime()));
                ps.setInt(4, p.getNoRue());
                ps.setString(5, p.getRue());
                ps.setInt(6, p.getNpa());
                ps.setString(7, p.getLocalite());
                ps.setBoolean(8, p.isActif());
                ps.setDouble(9, p.getSalaire());
                ps.setTimestamp(10, new Timestamp((new java.util.Date()).getTime()));
                ps.executeUpdate();
            } catch (SQLException e) {

            }
        }
    }

    public void effacer(Personne p) {
        if (p != null) {
            String prep = "DELETE FROM t_personne WHERE PK_PERS=?";
            try {
                PreparedStatement ps = dbConnexion.prepareStatement(prep);
                ps.setInt(1, p.getPkPers());
                ps.executeUpdate();
            } catch (Exception e) {
            }
        }

    }

    public Personne lire(int num) {
        Personne p = listePersonnes.get(num);
        return p;
    }

    public void modifier(Personne p) {
        if (p != null) {
            String prep = "UPDATE t_personne set PK_PERS = ?, Prenom = ?, Nom = ?, Date_naissance = ?, No_rue = ?, Rue = ?, NPA = ?, Ville = ?, Actif = ?, Salaire = ?, date_modif = ? where PK_PERS=?";
            try {
                PreparedStatement ps = dbConnexion.prepareStatement(prep);
                ps.setInt(1, p.getPkPers());
                ps.setString(2, p.getPrenom());
                ps.setString(3, p.getNom());
                ps.setDate(4, new java.sql.Date(p.getDateNaissance().getTime()));
                ps.setInt(5, p.getNoRue());
                ps.setString(6, p.getRue());
                ps.setInt(7, p.getNpa());
                ps.setString(8, p.getLocalite());
                ps.setBoolean(9, p.isActif());
                ps.setDouble(10, p.getSalaire());
                ps.setTimestamp(11, new Timestamp((new java.util.Date()).getTime()));
                ps.setInt(12, p.getPkPers());
                ps.executeUpdate();
                
            } catch (Exception e) {
            }
        }
    }

}

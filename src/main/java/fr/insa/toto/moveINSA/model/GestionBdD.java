/*
Copyright 2000- Francois de Bertrand de Beuvron

This file is part of CoursBeuvron.

CoursBeuvron is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

CoursBeuvron is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU Ge
neral Public License
along with CoursBeuvron.  If not, see <http://www.gnu.org/licenses/>.
 */
package fr.insa.toto.moveINSA.model;

import fr.insa.beuvron.utils.ConsoleFdB;
import fr.insa.beuvron.utils.exceptions.ExceptionsUtils;
import fr.insa.beuvron.utils.list.ListUtils;
import fr.insa.beuvron.utils.database.ResultSetUtils;
import fr.insa.toto.moveINSA.gui.Application;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.Driver;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import org.h2.jdbc.meta.DatabaseMetaServer;
import org.springframework.boot.SpringApplication;

/**
 * Opération générales sur la base de donnée de gestion des tournois.
 * <p>
 * Les opérations plus spécifiques aux diverses tables sont réparties dans les
 * classes correspondantes.
 * </p>
 *
 * @author francois
 */
public class GestionBdD {

    /**
     * création complète du schéma de la BdD.
     *
     * @param con
     * @throws SQLException
     */
    public static void creeSchema(Connection con) throws SQLException {
    con.setAutoCommit(false);
    try (Statement st = con.createStatement()) {
        // Créer la table 'pays'
        st.executeUpdate(
                "CREATE TABLE pays ( \n"
                + ConnectionSimpleSGBD.sqlForGeneratedKeys(con, "id") + ",\n"
                + " nomPays VARCHAR(100) NOT NULL UNIQUE\n"    // Le nom du pays (doit être unique)
                + ")");
        
        // Créer la table 'specialite'
        st.executeUpdate(
                "CREATE TABLE specialite ( \n"
                + ConnectionSimpleSGBD.sqlForGeneratedKeys(con, "id") + ",\n"
                + " nomSpecialite VARCHAR(100) NOT NULL UNIQUE\n" // Le nom de la spécialité, doit être unique
                + ")");
        
        // Créer la table 'partenaire'
        st.executeUpdate(
                "CREATE TABLE partenaire ( \n"
                + ConnectionSimpleSGBD.sqlForGeneratedKeys(con, "id") + ",\n"
                + " nomEcole VARCHAR(100) NOT NULL,\n"  // Nom de l'école
                + " idPays INT NOT NULL,\n"  // ID du pays (clé étrangère vers une table 'pays')
                + " FOREIGN KEY (idPays) REFERENCES pays(id) ON DELETE RESTRICT ON UPDATE CASCADE\n"  // Contrainte de clé étrangère pour le pays
                + ")");
        
        // Créer la table 'responsable_departement'
        st.executeUpdate(
                "CREATE TABLE responsable_departement ( \n"
                + " id INT AUTO_INCREMENT PRIMARY KEY,\n"  // Génération automatique de l'ID
                + " nom VARCHAR(50) NOT NULL,\n"  // Nom du responsable
                + " prenom VARCHAR(50) NOT NULL,\n"  // Prénom du responsable
                + " idSpecialite INT NOT NULL,\n"  // Lien vers la spécialité (table specialite)
                + " FOREIGN KEY (idSpecialite) REFERENCES specialite(id) ON DELETE CASCADE ON UPDATE CASCADE\n"
                + ")");
        
        // Créer la table 'etudiant'
        st.executeUpdate(
                "CREATE TABLE etudiant ( \n"
                + ConnectionSimpleSGBD.sqlForGeneratedKeys(con, "id") + ",\n"
                + " INE INT NOT NULL UNIQUE,\n"  // Identifiant national de l'étudiant
                + " prenom VARCHAR(50) NOT NULL,\n"  // Prénom de l'étudiant
                + " nom VARCHAR(50) NOT NULL,\n"  // Nom de l'étudiant
                + " score INT DEFAULT 0,\n"  // Score de l'étudiant, avec une valeur par défaut de 0
                + " idSpecialite INT NOT NULL,\n"  // Lien vers l'ID de la spécialité
                + " FOREIGN KEY (idSpecialite) REFERENCES specialite(id) ON DELETE CASCADE ON UPDATE CASCADE\n"  // Lien vers la table spécialité
                + ")");
        
        // Créer la table 'offremobilite'
        st.executeUpdate(
                "CREATE TABLE offremobilite ( \n"
                + ConnectionSimpleSGBD.sqlForGeneratedKeys(con, "id") + ",\n"
                + " nbrplaces INT NOT NULL,\n"  // Nombre de places disponibles
                + " proposepar INT NOT NULL,\n"  // ID du partenaire (clé étrangère vers table 'partenaire')
                + " typeMobilite VARCHAR(50) NOT NULL,\n"  // Type de mobilité (ex: Erasmus, stage, échange)
                + " idPays INT NOT NULL,\n"  // ID du pays (clé étrangère vers table 'pays')
                + " semestre INT NOT NULL CHECK (semestre IN (1, 2)),\n"  // Semestre (1 ou 2)
                + " idSpecialite INT NOT NULL,\n"  // ID de la spécialité (clé étrangère vers table 'specialite')
                + " FOREIGN KEY (proposepar) REFERENCES partenaire(id) ON DELETE RESTRICT ON UPDATE CASCADE,\n"  // Contrainte clé étrangère vers 'partenaire'
                + " FOREIGN KEY (idPays) REFERENCES pays(id) ON DELETE RESTRICT ON UPDATE CASCADE,\n"  // Contrainte clé étrangère vers 'pays'
                + " FOREIGN KEY (idSpecialite) REFERENCES specialite(id) ON DELETE RESTRICT ON UPDATE CASCADE\n"  // Contrainte clé étrangère vers 'specialite'
                + ")");
        
        // Créer la table 'candidature'
        st.executeUpdate(
                "CREATE TABLE candidature ( \n"
                + ConnectionSimpleSGBD.sqlForGeneratedKeys(con, "id") + ",\n"
                + " idEtudiant INT NOT NULL,\n"  // Identifiant de l'étudiant
                + " statut VARCHAR(50) DEFAULT 'en attente',\n"  // Statut de la candidature, par défaut 'en attente'
                + " dateDepot DATETIME DEFAULT CURRENT_TIMESTAMP,\n"  // Date de dépôt
                + " vœu1 INT,\n"  // Premier vœu
                + " vœu2 INT,\n"  // Deuxième vœu
                + " vœu3 INT,\n"  // Troisième vœu
                + " vœu4 INT,\n"  // Quatrième vœu
                + " vœu5 INT,\n"  // Cinquième vœu
                + " FOREIGN KEY (idEtudiant) REFERENCES etudiant(id) ON DELETE CASCADE,\n"
                + " FOREIGN KEY (vœu1) REFERENCES offremobilite(id) ON DELETE SET NULL,\n"
                + " FOREIGN KEY (vœu2) REFERENCES offremobilite(id) ON DELETE SET NULL,\n"
                + " FOREIGN KEY (vœu3) REFERENCES offremobilite(id) ON DELETE SET NULL,\n"
                + " FOREIGN KEY (vœu4) REFERENCES offremobilite(id) ON DELETE SET NULL,\n"
                + " FOREIGN KEY (vœu5) REFERENCES offremobilite(id) ON DELETE SET NULL\n"
                + ")");
        
        // Créer les tables de connexion
        st.executeUpdate(
                "CREATE TABLE connexion_etudiant ( \n"
                + ConnectionSimpleSGBD.sqlForGeneratedKeys(con, "id") + ",\n"
                + " pseudo_etudiant VARCHAR(50) NOT NULL,\n"
                + " motDePass_etudiant VARCHAR(50) NOT NULL\n" // Dernière colonne sans virgule
                + ")");
        
        st.executeUpdate(
                "CREATE TABLE connexion_partenaire ( \n"
                + ConnectionSimpleSGBD.sqlForGeneratedKeys(con, "id") + ",\n"
                + " pseudo_partenaire VARCHAR(50) NOT NULL,\n"
                + " motDePass_partenaire VARCHAR(50) NOT NULL\n" // Dernière colonne sans virgule
                + ")");
        
        st.executeUpdate(
                "CREATE TABLE connexion_responsable_departement ( \n"
                + ConnectionSimpleSGBD.sqlForGeneratedKeys(con, "id") + ",\n"
                + " pseudo_departement VARCHAR(50) NOT NULL,\n"
                + " motDePass_departement VARCHAR(50) NOT NULL\n" // Dernière colonne sans virgule
                + ")");
        
        st.executeUpdate(
                "CREATE TABLE SRI ( \n"
                + ConnectionSimpleSGBD.sqlForGeneratedKeys(con, "id") + ",\n"
                + " pseudo_SRI VARCHAR(50) NOT NULL,\n"
                + " motDePass_SRI VARCHAR(50) NOT NULL\n" // Dernière colonne sans virgule
                + ")");
        
        con.commit();
        System.out.println("Tables créées avec succès");
    } catch (SQLException ex) {
        con.rollback();
        throw ex;
    } finally {
        con.setAutoCommit(true);
    }
}

    /**
     * suppression complete de toute la BdD.
     *
     * @param con
     * @throws SQLException
     */
   public static void deleteSchema(Connection con) throws SQLException {
    try (Statement st = con.createStatement()) {
        // Supprimer les tables dans l'ordre inverse de leur création
        st.executeUpdate("DROP TABLE IF EXISTS candidature");
        st.executeUpdate("DROP TABLE IF EXISTS connexion_etudiant");
        st.executeUpdate("DROP TABLE IF EXISTS connexion_partenaire");
        st.executeUpdate("DROP TABLE IF EXISTS connexion_responsable_departement");
        st.executeUpdate("DROP TABLE IF EXISTS etudiant");
        st.executeUpdate("DROP TABLE IF EXISTS responsable_departement");
        st.executeUpdate("DROP TABLE IF EXISTS offremobilite");
        st.executeUpdate("DROP TABLE IF EXISTS partenaire");
        st.executeUpdate("DROP TABLE IF EXISTS specialite");
        st.executeUpdate("DROP TABLE IF EXISTS pays");
        st.executeUpdate("DROP TABLE IF EXISTS SRI");

        System.out.println("Toutes les tables ont été supprimées avec succès.");
    } catch (SQLException e) {
        System.err.println("Erreur lors de la suppression des tables: " + e.getMessage());
        throw e;  // Propager l'exception pour un traitement ultérieur
    }
}

public static void insertSpecialites(Connection con) throws SQLException {
        String sql = "INSERT INTO specialite (nomSpecialite) VALUES (?)";

        // Spécialités à insérer
        String[] specialites = {
            "Génie Civil",
            "Génie Electrique",
            "Génie Mecanique",
            "Génie Thermique Energetique et Environnement",
            "Mécatronique",
            "Plasturgie",
            "Topographie"
        };

        try (PreparedStatement pstmt = con.prepareStatement(sql)) {
            for (String specialite : specialites) {
                pstmt.setString(1, specialite);
                pstmt.executeUpdate();
            }
        }
    }

   
   

    public static void razBDD(Connection con) throws SQLException {
        deleteSchema(con);
        creeSchema(con);
        insertSpecialites(con);
        
       
    }
    
   
    public static void recherche_etudiant(Connection con)throws SQLException{
    }
    public static void modif_etudiant(Connection con)throws SQLException{
    }
    public static void supprimer_etudiant(Connection con)throws SQLException{
    }
    
    public static void menuPartenaire(Connection con) {
        int rep = -1;
        while (rep != 0) {
            int i = 1;
            System.out.println("Menu partenaires");
            System.out.println("==================");
            System.out.println((i++) + ") liste de tous les partenaires");
            System.out.println((i++) + ") créer un nouveau partenaire");
            System.out.println("0) Retour");
            rep = ConsoleFdB.entreeEntier("Votre choix : ");
            try {
                int j = 1;
                if (rep == j++) {
                    List<Partenaire> users = Partenaire.tousLesPartaires(con);
                    System.out.println(users.size() + " utilisateurs : ");
                    System.out.println(ListUtils.enumerateList(users, (elem) -> elem.toString()));
                } else if (rep == j++) {
                    Partenaire.creeConsole(con);
                }
            } catch (Exception ex) {
                System.out.println(ExceptionsUtils.messageEtPremiersAppelsDansPackage(ex, "fr.insa", 3));
            }
        }
    }

    public static void menuOffre(Connection con) {
        int rep = -1;
        while (rep != 0) {
            int i = 1;
            System.out.println("Menu offres mobilité");
            System.out.println("==================");
            System.out.println((i++) + ") liste de toutes les offres");
            System.out.println((i++) + ") créer une nouvelle offre");
            System.out.println("0) Retour");
            rep = ConsoleFdB.entreeEntier("Votre choix : ");
            try {
                int j = 1;
                if (rep == j++) {
                    List<OffreMobilite> offres = OffreMobilite.toutesLesOffres(con);
                    System.out.println(offres.size() + " offres : ");
                    System.out.println(ListUtils.enumerateList(offres, (elem) -> elem.toString()));
                } else if (rep == j++) {
                    OffreMobilite.creeConsole(con);
                }
            } catch (Exception ex) {
                System.out.println(ExceptionsUtils.messageEtPremiersAppelsDansPackage(ex, "fr.insa", 3));
            }
        }
    }
   
    public static void menuBdD(Connection con) {
        int rep = -1;
        while (rep != 0) {
            int i = 1;
            System.out.println("Menu gestion base de données");
            System.out.println("============================");
            System.out.println((i++) + ") RAZ BdD = delete + create + init");
            System.out.println((i++) + ") donner un ordre SQL update quelconque");
            System.out.println((i++) + ") donner un ordre SQL query quelconque");
            System.out.println("0) Retour");
            rep = ConsoleFdB.entreeEntier("Votre choix : ");
            try {
                int j = 1;
                if (rep == j++) {
                    razBDD(con);
                
                } else if (rep == j++) {
                    String ordre = ConsoleFdB.entreeString("ordre SQL : ");
                    try (PreparedStatement pst = con.prepareStatement(ordre)) {
                        pst.executeUpdate();
                    }
                } else if (rep == j++) {
                    String ordre = ConsoleFdB.entreeString("requete SQL : ");
                    try (PreparedStatement pst = con.prepareStatement(ordre)) {
                        try (ResultSet rst = pst.executeQuery()) {
                            System.out.println(ResultSetUtils.formatResultSetAsTxt(rst));
                        }
                    }
                }
            } catch (Exception ex) {
                System.out.println(ExceptionsUtils.messageEtPremiersAppelsDansPackage(ex, "fr.insa.beuvron", 3));
            }
        }
    }

    public static void menuPrincipal() {
        int rep = -1;
        Connection con = null;
        try {
            con = ConnectionSimpleSGBD.defaultCon();
            System.out.println("Connection OK");
        } catch (SQLException ex) {
            System.out.println("Problème de connection : " + ex.getLocalizedMessage());
            throw new Error(ex);
        }
        while (rep != 0) {
            int i = 1;
            System.out.println("Menu principal");
            System.out.println("==================");
            System.out.println((i++) + ") test driver mysql");
            System.out.println((i++) + ") menu gestion BdD");
            System.out.println((i++) + ") menu partenaires");
            System.out.println((i++) + ") menu offres");
            System.out.println((i++) + ") initialiser");
            System.out.println("0) Fin");
            rep = ConsoleFdB.entreeEntier("Votre choix : ");
            try {
                int j = 1;
                if (rep == j++) {
                    try {
                        Class<Driver> mysqlDriver = (Class<Driver>) Class.forName("com.mysql.cj.jdbc.Driver");
                    } catch (ClassNotFoundException ex) {
                        System.out.println("com.mysql.cj.jdbc.Driver not found");
                    }
                    DatabaseMetaData meta = con.getMetaData();
                    System.out.println("jdbc driver version : " + meta.getDriverName() + " ; " + meta.getDriverVersion());
                } else if (rep == j++) {
                    menuBdD(con);
                  
                } else if (rep == j++) {
                    menuPartenaire(con);
                } else if (rep == j++) {
                    menuOffre(con);
                }else if (rep == j++) {
                   
                }
            } catch (Exception ex) {
                System.out.println(ExceptionsUtils.messageEtPremiersAppelsDansPackage(ex, "fr.insa", 3));
            }
        }
    }

    public static void main(String[] args) {
        menuPrincipal();
        
    }
}

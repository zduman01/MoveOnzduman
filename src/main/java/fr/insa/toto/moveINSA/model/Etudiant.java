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

You should have received a copy of the GNU General Public License
along with CoursBeuvron.  If not, see <http://www.gnu.org/licenses/>.
 */
package fr.insa.toto.moveINSA.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Etudiant {
    
    private int id;
    private int INE;
    private String prenom;
    private String nom;
    private String pseudo; 
    private String motDePasse; // Ajoutez un champ pour le mot de passe
    private static int idUtilisateur;

    public static void setIdUtilisateur(int id) {
        idUtilisateur = id;
    }

    public static int getIdUtilisateur() {
        return idUtilisateur;
    }

    // Constructeur
    public Etudiant(int id, int INE, String prenom, String nom, String motDePasse) {
        this.id = id;
        this.INE = INE;
        this.prenom = prenom;
        this.nom = nom;
        this.motDePasse = motDePasse; // Initialisation du mot de passe
        this.pseudo = "";
    }

    // Getters
    public int getId() {
        return id;
    }

    public int getINE() {
        return INE;
    }

    public String getPrenom() {
        return prenom;
    }

    public String getNom() {
        return nom;
    }

    public String getPseudo() {
        return pseudo;
    }

    public String getMotDePasse() { // Getter pour le mot de passe
        return motDePasse;
    }

    // Setters
    public void setINE(int INE) {
        this.INE = INE;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setPseudo(String pseudo) {
        this.pseudo = pseudo;
    }

    public void setMotDePasse(String motDePasse) { // Setter pour le mot de passe
        this.motDePasse = motDePasse;
    }

    // Méthode pour générer le pseudo
    public static String generatePseudo(String prenom, String nom) throws SQLException {
        Connection connection = ConnectionSimpleSGBD.defaultCon();
        String basePseudo = prenom.toLowerCase().charAt(0) + nom.toLowerCase();  // Ex: ldupont
        int count = 1;
        String pseudo = basePseudo + String.format("%02d", count);  // Ajoute 01 à la fin (ldupont01)

        // Requête pour vérifier si le pseudo existe déjà dans la base
        String sql = "SELECT COUNT(*) FROM connexion_etudiant WHERE pseudo_etudiant = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, pseudo);
            ResultSet rs = pstmt.executeQuery();

            // Tant que le pseudo existe déjà, on incrémente
            while (rs.next() && rs.getInt(1) > 0) {
                count++;
                pseudo = basePseudo + String.format("%02d", count);
                pstmt.setString(1, pseudo);
                rs = pstmt.executeQuery();
            }
        } return pseudo;  
    }
    
    public int getIdUtilisateurByPseudo(String pseudo) {
    int idUtilisateur = -1; // Valeur par défaut pour indiquer que l'utilisateur n'a pas été trouvé

    String query = "SELECT id FROM utilisateurs WHERE pseudo = ?"; // Adapté selon votre structure

    try (Connection connection = ConnectionSimpleSGBD.defaultCon();
         PreparedStatement pst = connection.prepareStatement(query)) {
        pst.setString(1, pseudo); // Définit le pseudo dans la requête

        try (ResultSet rs = pst.executeQuery()) {
            if (rs.next()) {
                idUtilisateur = rs.getInt("id"); // Récupère l'ID de l'utilisateur
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }

    return idUtilisateur; // Retourne l'ID de l'utilisateur ou -1 s'il n'est pas trouvé
}
    // Méthode pour s'inscrire
    public boolean sinscrire(String pseudo, String motDePasse, String confirmationMotDePasse) {
        // Vérification si les mots de passe correspondent
        if (!motDePasse.equals(confirmationMotDePasse)) {
            System.out.println("Les mots de passe ne correspondent pas.");
            return false; // Mots de passe ne correspondent pas
        }

        // Si tout est valide, on peut sauvegarder l'étudiant
        this.pseudo = pseudo;
        this.motDePasse = motDePasse; // Vous pouvez hacher le mot de passe ici pour plus de sécurité
        return true; // Inscription réussie
    }

    public void sauvegarde_etudiant(Connection con) throws SQLException {
        try (PreparedStatement st = con.prepareStatement(""
                + "INSERT INTO etudiant (id, INE, prenom, nom, pseudo, motDePasse) VALUES (?, ?, ?, ?, ?, ?)")) {
            st.setInt(1, this.id);
            st.setInt(2, this.INE);
            st.setString(3, this.prenom);
            st.setString(4, this.nom);
            st.setString(5, this.pseudo);
            st.setString(6, this.motDePasse); // Ajoutez le mot de passe ici
            st.executeUpdate();
        }
    }
    
    public void sauvegarder_connexion(Connection con) throws SQLException {
    if (this.pseudo == null || this.pseudo.isEmpty() || this.motDePasse == null || this.motDePasse.isEmpty()) {
        throw new SQLException("Le pseudo et le mot de passe doivent être définis.");
    }
    String sql = "INSERT INTO connexion (pseudo, motDePasse) VALUES (?, ?)";

    try (PreparedStatement pstmt = con.prepareStatement(sql)) {
        pstmt.setString(1, this.pseudo);
        pstmt.setString(2, this.motDePasse); // Vous pouvez hacher le mot de passe pour plus de sécurité
        pstmt.executeUpdate();
        System.out.println("Les informations de connexion ont été sauvegardées avec succès.");
    } catch (SQLException e) {
        e.printStackTrace();
        throw new SQLException("Erreur lors de la sauvegarde dans la table connexion.");
    }
}
    // Méthode de connexion
    public boolean seConnecter(Connection con, String pseudo, String motDePasse) throws SQLException {
        String sql = "SELECT COUNT(*) FROM etudiant WHERE pseudo = ? AND motDePasse = ?";
        try (PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setString(1, pseudo);
            pstmt.setString(2, motDePasse);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        }
        return false;
    }
    
    public void deposerCandidature(Connection con, int idOffreMobilite) throws SQLException {
        String sql = "INSERT INTO candidature (idEtudiant, idOffreMobilite, statut) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setInt(1, this.id); // ID de l'étudiant
            pstmt.setInt(2, idOffreMobilite); // ID de l'offre de mobilité
            pstmt.setString(3, "en attente"); // Statut par défaut

            pstmt.executeUpdate(); // Exécute la requête d'insertion
            System.out.println("Candidature déposée avec succès !");
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("Erreur lors du dépôt de la candidature.");
        }
    }

    public void suivreCandidature(Connection con) throws SQLException {
        String sql = "SELECT statut, dateDepot FROM candidature WHERE idEtudiant = ?";

        try (PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setInt(1, this.id);  // L'ID de l'étudiant

            ResultSet rs = pstmt.executeQuery();

            // Vérifiez si des candidatures existent
            if (!rs.isBeforeFirst()) {
                System.out.println("Aucune candidature trouvée pour l'étudiant " + prenom + " " + nom);
                return;
            }

            // Parcourir les résultats et afficher le statut
            while (rs.next()) {
                String statut = rs.getString("statut");
                String dateDepot = rs.getString("dateDepot");

                System.out.println("Statut de la candidature : " + statut);
                System.out.println("Date de dépôt : " + dateDepot);
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération des informations de candidature : " + e.getMessage());
        }
    }
    
    


}



  


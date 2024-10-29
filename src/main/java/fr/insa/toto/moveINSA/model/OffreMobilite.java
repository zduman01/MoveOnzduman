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

import com.vaadin.flow.component.notification.Notification;
import fr.insa.beuvron.utils.ConsoleFdB;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe "miroir" de la table offremobilite.
 * <p>
 * pour un commentaire plus détaillé sur ces classes "miroir", voir dans la
 * classe Partenaire
 * </p>
 *
 * @author francois
 */
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class OffreMobilite {
    private int id;
    private int nbrPlaces;
    private int proposePar; // ID du partenaire
    private String typeMobilite; // Type de mobilité
    private String ville; 
    private int idPays; // ID du pays
    private int semestre; // 1 ou 2
    private int idSpecialite; // ID de la spécialité
    private String typeNiveauEtude; // Type de niveau d'étude
    private String nomSpecialite; // Nom de la spécialité
    private String nomPartenaire;

    // Constructeur
    public OffreMobilite(int id, int nbrPlaces, int proposePar, String typeMobilite, String ville, int idPays, int semestre, int idSpecialite, String typeNiveauEtude) {
        this.id = id;
        this.nbrPlaces = nbrPlaces;
        this.proposePar = proposePar;
        this.typeMobilite = typeMobilite;
        this.ville = ville;
        this.idPays = idPays;
        this.semestre = semestre;
        this.idSpecialite = idSpecialite;
        this.typeNiveauEtude = typeNiveauEtude;
    }

    // Getters
    public int getId() {
        return id; // Ajout du getter pour l'id
    }

    public int getNbrPlaces() {
        return nbrPlaces;
    }

    public int getProposePar() {
        return proposePar;
    }

    public String getTypeMobilite() {
        return typeMobilite;
    }

    public int getIdPays() {
        return idPays;
    }
    
    public String getVille() {
        return ville;
    }

    public int getSemestre() {
        return semestre;
    }

    public int getIdSpecialite() {
        return idSpecialite;
    }

    public String getTypeNiveauEtude() {
        return typeNiveauEtude;
    }

    public String getNomSpecialite() {
        return nomSpecialite; // Getter pour le nom de la spécialité
    }
    
    public void setNomSpecialite(String nomSpecialite) {
        this.nomSpecialite = nomSpecialite; // Setter pour le nom de la spécialité
    }
    
    public String getNomPartenaire() {
        return nomPartenaire; 
    } 
    
    public void setNomPartenaire(String nomPartenaire) { 
        this.nomPartenaire = nomPartenaire;
    } // Setter pour le nom du partenaire


    public static List<OffreMobilite> toutesLesOffres(Connection connection) throws SQLException {
        List<OffreMobilite> offres = new ArrayList<>(); // Liste des offres

        String query = "SELECT id, nbrPlaces, proposePar, typeMobilite, ville, idPays, semestre, idSpecialite, type_niveau_etude FROM offremobilite";

        try (PreparedStatement pst = connection.prepareStatement(query);
             ResultSet rs = pst.executeQuery()) {
             
            while (rs.next()) {
                int idSpecialite = rs.getInt("idSpecialite"); // Récupérer l'ID de la spécialité
                int proposePar = rs.getInt("proposePar");
                // Créer l'offre
                OffreMobilite offre = new OffreMobilite(
                    rs.getInt("id"),
                    rs.getInt("nbrPlaces"),
                    rs.getInt("proposePar"), 
                    rs.getString("typeMobilite"),
                    rs.getString("ville"),
                    rs.getInt("idPays"),
                    rs.getInt("semestre"),
                    idSpecialite, // Gardez l'ID de la spécialité
                    rs.getString("type_niveau_etude") // Récupérer le type de niveau d'étude
                );
                
                // Récupérez le nom de la spécialité
                String nomSpecialite = getNomSpecialite(connection, idSpecialite);
                offre.setNomSpecialite(nomSpecialite); // Définissez le nom de la spécialité dans l'objet
                
                String nomPartenaire = getNomPartenaire(connection, proposePar);
                offre.setNomPartenaire(nomPartenaire); // Définissez le nom de la spécialité dans l'objet
                
                offres.add(offre);
            }
        }
        return offres; // Retournez la liste des offres
    }
    
   public static List<OffreMobilite> getFilteredOffers(Connection connection, String query, String destination, String programType, String duration, String level, String scholarship, String specialite) throws SQLException {
    List<OffreMobilite> offres = new ArrayList<>(); // Liste des offres

    // Construction de la requête SQL
    StringBuilder sql = new StringBuilder("SELECT id, nbrPlaces, proposePar, typeMobilite, ville, idPays, semestre, idSpecialite, type_niveau_etude FROM offremobilite WHERE 1=1"); 
    List<Object> parameters = new ArrayList<>();

    // Ajout des conditions de filtrage
    if (query != null && !query.isEmpty()) {
        sql.append(" AND (ville LIKE ? OR typeMobilite LIKE ?)");
        parameters.add("%" + query + "%");
        parameters.add("%" + query + "%");
    }

    if (destination != null) {
        sql.append(" AND idPays = ?");
        parameters.add(destination); // Assurez-vous que 'destination' correspond à l'idPays
    }

    if (specialite != null) {
        sql.append(" AND idSpecialite = ?");
        parameters.add(specialite);
    }

    // Ajoutez d'autres conditions pour programType, duration, level, scholarship si nécessaire
    if (programType != null) {
        sql.append(" AND typeMobilite = ?");
        parameters.add(programType);
    }

    if (duration != null) {
        // Supposons que 'duration' soit mappé à un champ dans la base de données
        sql.append(" AND semestre = ?"); 
        parameters.add(duration);
    }

    if (level != null) {
        sql.append(" AND type_niveau_etude = ?");
        parameters.add(level);
    }

    if (scholarship != null) {
        // Si le champ bourse existe dans la table des offres
        sql.append(" AND bourse = ?"); 
        parameters.add(scholarship);
    }

    // Exécution de la requête et récupération des résultats
    try (PreparedStatement pst = connection.prepareStatement(sql.toString())) {
        // Remplissage des paramètres dans la requête
        for (int i = 0; i < parameters.size(); i++) {
            pst.setObject(i + 1, parameters.get(i));
        }

        try (ResultSet rs = pst.executeQuery()) {
            while (rs.next()) {
                int idSpecialite = rs.getInt("idSpecialite"); // Récupérer l'ID de la spécialité
                int proposePar = rs.getInt("proposePar");
                
                // Créer l'offre
                OffreMobilite offre = new OffreMobilite(
                    rs.getInt("id"),
                    rs.getInt("nbrPlaces"),
                    proposePar, 
                    rs.getString("typeMobilite"),
                    rs.getString("ville"),
                    rs.getInt("idPays"),
                    rs.getInt("semestre"),
                    idSpecialite, // Gardez l'ID de la spécialité
                    rs.getString("type_niveau_etude") // Récupérer le type de niveau d'étude
                );

                // Récupérez le nom de la spécialité
                String nomSpecialite = getNomSpecialite(connection, idSpecialite);
                offre.setNomSpecialite(nomSpecialite); // Définissez le nom de la spécialité dans l'objet

                String nomPartenaire = getNomPartenaire(connection, proposePar);
                offre.setNomPartenaire(nomPartenaire); // Définissez le nom de la spécialité dans l'objet
                
                offres.add(offre);
            }
        }
    }
    return offres; // Retournez la liste des offres filtrées
}


    // Méthode pour récupérer le nom de la spécialité à partir de son ID
    public static String getNomSpecialite(Connection connection, int idSpecialite) throws SQLException {
        String nomSpecialite = null;
        String query = "SELECT nomSpecialite FROM specialite WHERE id = ?";

        try (PreparedStatement pst = connection.prepareStatement(query)) {
            pst.setInt(1, idSpecialite); // Définit l'ID de la spécialité dans la requête
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    nomSpecialite = rs.getString("nomSpecialite"); // Récupère le nom de la spécialité
                }
            }
        }
        
        return nomSpecialite; // Retourne le nom de la spécialité, ou null si non trouvée
    }
    
    public void enregistrerOffre(OffreMobilite offre, int idUtilisateur) {
        String insertQuery = "INSERT INTO offresenregistrees (idOffre, idUtilisateur) VALUES (?, ?)";
        try (Connection connection = ConnectionSimpleSGBD.defaultCon();
             PreparedStatement pst = connection.prepareStatement(insertQuery)) {

            pst.setInt(1, offre.getId());
            pst.setInt(2, idUtilisateur); // Remplacer par l'ID de l'utilisateur actuel

            int rowsAffected = pst.executeUpdate();

            if (rowsAffected > 0) {
                Notification.show("Offre enregistrée avec succès.");
            } else {
                Notification.show("Erreur lors de l'enregistrement de l'offre.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            Notification.show("Erreur lors de l'enregistrement : " + e.getMessage());
        }
    }
    
    public static String getNomPartenaire(Connection connection, int id) throws SQLException {
        String nomSpecialite = null;
        String query = "SELECT nomEcole FROM partenaire WHERE id = ?";

        try (PreparedStatement pst = connection.prepareStatement(query)) {
            pst.setInt(1, id); // Définit l'ID de la spécialité dans la requête
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    nomSpecialite = rs.getString("nomEcole"); // Récupère le nom de la spécialité
                }
            }
        }
        
        return nomSpecialite; // Retourne le nom de la spécialité, ou null si non trouvée
    }
}

    




    

 

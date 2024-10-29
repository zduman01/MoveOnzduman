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
package fr.insa.toto.moveINSA.gui.vues;

/**
 *
 * @author zilan
 */

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import fr.insa.toto.moveINSA.gui.EnteteInitiale;
import fr.insa.toto.moveINSA.gui.MenuGauche;
import fr.insa.toto.moveINSA.gui.vues.MesOffresEnregistrees;
import fr.insa.toto.moveINSA.model.ConnectionSimpleSGBD;
import fr.insa.toto.moveINSA.model.OffreMobilite;
import fr.insa.toto.moveINSA.model.Specialite;
import static fr.insa.toto.moveINSA.model.OffreMobilite.toutesLesOffres;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.server.VaadinService;
import com.vaadin.flow.server.VaadinSession;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Route("affichage-offres-mobilite")
public class AffichageOffresMobilite extends VerticalLayout {

    private List<OffreMobilite> offres;
    private Button saveButton;

    public AffichageOffresMobilite() {
        // Récupérer les paramètres de l'URL sous forme de Map<String, String[]>
        Map<String, String[]> queryParameters = VaadinService.getCurrentRequest().getParameterMap();

        // Extraire les valeurs de chaque paramètre
        String query = getParameter(queryParameters, "query");
        String destination = getParameter(queryParameters, "destination");
        String programType = getParameter(queryParameters, "programType");
        String duration = getParameter(queryParameters, "duration");
        String level = getParameter(queryParameters, "level");
        String scholarship = getParameter(queryParameters, "scholarship");
        String specialite = getParameter(queryParameters, "specialite");

        // Cas 1: Recherche par mot-clé
        if (query != null && !query.isEmpty()) {
            chargerEtAfficherOffresFiltrees(query, null, null, null, null, null, null);
        
        // Cas 2: Recherche par filtres (aucun mot-clé mais des filtres sont présents)
        } else if (destination != null || programType != null || duration != null ||
                   level != null || scholarship != null || specialite != null) {
            chargerEtAfficherOffresFiltrees(null, destination, programType, duration, level, scholarship, specialite);
        
        // Cas 3: Affichage de toutes les offres (aucun mot-clé ni filtre)
        } else {
            chargerEtAfficherToutesLesOffres();
        }
    }

    private String getParameter(Map<String, String[]> queryParameters, String key) {
        String[] values = queryParameters.get(key);
        return (values != null && values.length > 0) ? values[0] : null;
        
    }
    
    // Méthode pour charger les offres depuis la base de données
    private void chargerEtAfficherToutesLesOffres() {
        try (Connection connection = ConnectionSimpleSGBD.defaultCon()) {
            offres = toutesLesOffres(connection); // Récupère toutes les offres

            System.out.println("Nombre d'offres récupérées : " + offres.size());

            if (offres.isEmpty()) {
                Notification.show("Aucune offre de mobilité disponible.");
                return;
            }

            // Regroupement des offres par pays
            Map<Integer, List<OffreMobilite>> offresParPays = new HashMap<>();
            for (OffreMobilite offre : offres) {
                offresParPays.computeIfAbsent(offre.getIdPays(), k -> new ArrayList<>()).add(offre);
            }

            // Récupération des noms de pays
            Map<Integer, String> countryNames = getCountryNames(connection);
            System.out.println("Noms des pays récupérés : " + countryNames);

            // Affichage des offres par pays
            for (Map.Entry<Integer, List<OffreMobilite>> entry : offresParPays.entrySet()) {
                int idPays = entry.getKey();
                List<OffreMobilite> offreList = entry.getValue();

                // Trouver le nom du pays correspondant à l'id et le mettre en majuscules
                String countryName = countryNames.get(idPays).toUpperCase(); // Conversion en majuscules
                System.out.println("Ajout du conteneur pour le pays : " + countryName);
                 
                setMargin(false); // Désactive la marge par défaut du VerticalLayout
                setPadding(false); // Désactive le padding par défaut du VerticalLayout
                getStyle().set("margin-top", "100px"); 
                // Création d'un conteneur pour chaque pays
                VerticalLayout countryContainer = new VerticalLayout();
                countryContainer.getStyle().set("top", "300px"); 
                // Créer un Span pour le nom du pays avec style
                Span countrySpan = new Span(countryName);
                countrySpan.getStyle().set("color", "#B22222") // Changer la couleur du texte en rouge
                                .set("margin-left", "520px"); // Décaler vers la droite
                countryContainer.add(new H3(countrySpan)); // Ajoute le nom du pays en tant qu'en-tête

                // Ajout d'une ligne horizontale
                Div horizontalLine = new Div();
                horizontalLine.getStyle().set("border-bottom", "3px solid #B22222") // Ajouter une ligne noire
                                .set("margin-left", "520px")
                                .set("width", "1000px");
                                
                countryContainer.add(horizontalLine);

                // Ajouter les offres à ce conteneur
                for (OffreMobilite offre : offreList) {
                    countryContainer.add(creerConteneurOffre(offre)); // Créez un conteneur pour chaque offre
                }
                
                add(countryContainer); // Ajoute le conteneur du pays à la vue principale
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Imprime l'erreur dans la console
            Notification.show("Erreur lors du chargement des offres : " + e.getMessage());
        }
    }
    
    public void chargerEtAfficherOffresFiltrees(String query, String destination, String programType, String duration, String level, String scholarship, String specialite) {
    try (Connection connection = ConnectionSimpleSGBD.defaultCon()) {
        // Récupère les offres filtrées
        offres = OffreMobilite.getFilteredOffers(connection, query, destination, programType, duration, level, scholarship, specialite);

        System.out.println("Nombre d'offres filtrées récupérées : " + offres.size());

        if (offres.isEmpty()) {
            Notification.show("Aucune offre de mobilité disponible pour les critères sélectionnés.");
            return;
        }

        // Regroupement des offres par pays
        Map<Integer, List<OffreMobilite>> offresParPays = new HashMap<>();
        for (OffreMobilite offre : offres) {
            offresParPays.computeIfAbsent(offre.getIdPays(), k -> new ArrayList<>()).add(offre);
        }

        // Récupération des noms de pays
        Map<Integer, String> countryNames = getCountryNames(connection);
        System.out.println("Noms des pays récupérés : " + countryNames);

        // Affichage des offres par pays
        for (Map.Entry<Integer, List<OffreMobilite>> entry : offresParPays.entrySet()) {
            int idPays = entry.getKey();
            List<OffreMobilite> offreList = entry.getValue();

            // Trouver le nom du pays correspondant à l'id et le mettre en majuscules
            String countryName = countryNames.get(idPays).toUpperCase(); // Conversion en majuscules
            System.out.println("Ajout du conteneur pour le pays : " + countryName);

            setMargin(false); // Désactive la marge par défaut du VerticalLayout
            setPadding(false); // Désactive le padding par défaut du VerticalLayout
            getStyle().set("margin-top", "100px");

            // Création d'un conteneur pour chaque pays
            VerticalLayout countryContainer = new VerticalLayout();
            countryContainer.getStyle().set("top", "300px");

            // Créer un Span pour le nom du pays avec style
            Span countrySpan = new Span(countryName);
            countrySpan.getStyle().set("color", "#B22222") // Changer la couleur du texte en rouge
                            .set("margin-left", "520px"); // Décaler vers la droite
            countryContainer.add(new H3(countrySpan)); // Ajoute le nom du pays en tant qu'en-tête

            // Ajout d'une ligne horizontale
            Div horizontalLine = new Div();
            horizontalLine.getStyle().set("border-bottom", "3px solid #B22222") // Ajouter une ligne rouge
                            .set("margin-left", "520px")
                            .set("width", "1000px");

            countryContainer.add(horizontalLine);

            // Ajouter les offres à ce conteneur
            for (OffreMobilite offre : offreList) {
                countryContainer.add(creerConteneurOffre(offre)); // Créez un conteneur pour chaque offre
            }

            add(countryContainer); // Ajoute le conteneur du pays à la vue principale
        }
    } catch (SQLException e) {
        e.printStackTrace(); // Imprime l'erreur dans la console
        Notification.show("Erreur lors du chargement des offres filtrées : " + e.getMessage());
    }
}


    // Méthode pour créer un conteneur pour chaque offre
   

        // Ajouter les informations de l'offre
        private VerticalLayout creerConteneurOffre(OffreMobilite offre) {
        VerticalLayout offreContainer = new VerticalLayout();
        offreContainer.setPadding(true);
        offreContainer.setWidth("80%");
        offreContainer.setMaxWidth("1000px");
        offreContainer.getStyle().set("margin-left", "520px");
        offreContainer.getStyle().set("border", "2px solid #B22222")
            .set("border-radius", "15px")
            .set("padding", "15px");

        // Afficher les informations dans l'ordre souhaité
        offreContainer.add(new Span("Établissement partenaire: " + offre.getNomPartenaire()));
        offreContainer.add(new Span("Niveau d'étude: " + offre.getTypeNiveauEtude()));
        offreContainer.add(new Span("Spécialité: " + offre.getNomSpecialite())); // Affichez le nom de la spécialité si possible
        offreContainer.add(new Span("Ville: " + offre.getVille())); // Nouvelle information
        offreContainer.add(new Span("Semestre: " + offre.getSemestre()));
        
        Icon bookmarkIcon = new Icon(VaadinIcon.BOOKMARK);
        
        // Appliquer les styles initiaux pour l'icône
        bookmarkIcon.getStyle().set("color", "grey"); // Couleur intérieure grise
        bookmarkIcon.getStyle().set("font-size", "40px");
        // Initialiser le bouton
        saveButton = new Button(bookmarkIcon, event -> {
            int idUtilisateur = (Integer) VaadinSession.getCurrent().getAttribute("userId"); // Remplacez par l'ID utilisateur réel
            offre.enregistrerOffre(offre, idUtilisateur); // Appel à la méthode d'enregistrement
            
            bookmarkIcon.getStyle().set("color", "#B22222");
        });
        
        // Appliquer les styles initiaux pour le bouton
        
       saveButton.getStyle().set("background-color", "transparent"); // Fond transparent
       saveButton.getStyle().set("border", "none"); // Supprimer la bordure du bouton
       saveButton.getStyle().set("cursor", "pointer"); // Curseur main

        // Ajoutez le bouton à votre conteneur
        
        offreContainer.add(saveButton);

            add(offreContainer); 

    return offreContainer;
}

      
    // Méthode pour obtenir le nom des pays à partir de la base de données
    private Map<Integer, String> getCountryNames(Connection connection) throws SQLException {
        Map<Integer, String> countryMap = new HashMap<>();
        String sql = "SELECT id, nomPays FROM pays"; // Assurez-vous que la requête est correcte
        try (PreparedStatement pst = connection.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {
            while (rs.next()) {
                countryMap.put(rs.getInt("id"), rs.getString("nomPays"));
            }
        }
        return countryMap;
    }
}

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

package fr.insa.toto.moveINSA.gui;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.FlexComponent.JustifyContentMode;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.sidenav.SideNav;
import com.vaadin.flow.component.sidenav.SideNavItem;
import fr.insa.toto.moveINSA.gui.vues.DeposerCandidature;
import fr.insa.toto.moveINSA.gui.vues.NouveauPartenairePanel;
import fr.insa.toto.moveINSA.gui.vues.OffresMobilite;
import fr.insa.toto.moveINSA.gui.vues.PartenairesPanel;
import fr.insa.toto.moveINSA.gui.vues.RAZBdDPanel;
import fr.insa.toto.moveINSA.gui.vues.SuivreCandidature;
import fr.insa.toto.moveINSA.gui.vues.TestDriverPanel;
import fr.insa.toto.moveINSA.gui.vues.TodoPanel;

/**
 *
 * @author francois
 */
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.component.UI; // Assurez-vous d'importer cette classe pour la navigation

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.router.Route;
import fr.insa.toto.moveINSA.gui.vues.MesOffresEnregistrees;

public class MenuGauche extends VerticalLayout {

    public MenuGauche() {
        // Configuration de la mise en page de la barre latérale
        this.getStyle().set("background-color", "#FFFFFF"); // Couleur de fond blanche
        this.setHeight("100vh"); // Hauteur de la vue
        this.setPadding(false); // Enlever le padding par défaut

        // Configuration de la barre de menu
        VerticalLayout menuBar = new VerticalLayout();
        menuBar.getStyle().set("background-color", "#f0f0f0"); // Couleur de fond gris clair
        menuBar.getStyle().set("padding", "20px");
        menuBar.getStyle().set("position", "fixed"); // Fixer la position de la barre
        menuBar.getStyle().set("top", "80px"); // Ajuster la barre pour commencer juste en dessous de l'en-tête
        menuBar.getStyle().set("left", "0"); // Fixer la barre à gauche
        menuBar.getStyle().set("width", "450px"); // Largeur fixe de la barre
        menuBar.getStyle().set("height", "calc(100vh - 120px)"); // Hauteur ajustée de la barre en fonction de l'en-tête
        menuBar.setPadding(false); // Pas de padding
        menuBar.setMargin(false); // Pas de marges
        menuBar.setSpacing(true);
        
        // Utilisation de Flexbox pour gérer l'alignement
        menuBar.setJustifyContentMode(JustifyContentMode.START); // Commencer en haut
        menuBar.setAlignItems(Alignment.STRETCH); // Les boutons prennent toute la largeur

        // Boutons avec icône à gauche et description à droite
        Button btnDeposerCandidature = createCustomButton(VaadinIcon.PLUS, "Déposer Candidature", "Naviguer pour déposer une nouvelle candidature");
        btnDeposerCandidature.addClickListener(e -> UI.getCurrent().navigate(DeposerCandidature.class));
        
        Button btnSuivreCandidature = createCustomButton(VaadinIcon.FOLDER, "Suivre Candidature", "Suivre l'état de votre candidature");
        btnSuivreCandidature.addClickListener(e -> UI.getCurrent().navigate(SuivreCandidature.class));
        
        Button btnOffresMobilite = createCustomButton(VaadinIcon.SEARCH, "Offres de Mobilité", "Rechercher des offres de mobilité disponibles");
        btnOffresMobilite.addClickListener(e -> UI.getCurrent().navigate(OffresMobilite.class));
        
        // Nouveau bouton pour les offres enregistrées
        Button btnMesOffresEnregistrees = createCustomButton(VaadinIcon.FILE, "Mes Offres Mobilité ", "Voir les offres de mobilité que vous avez enregistrées");
        btnMesOffresEnregistrees.addClickListener(e -> UI.getCurrent().navigate(MesOffresEnregistrees.class)); // Ajoutez votre classe cible ici
        
        // Ajouter les boutons à la barre
        menuBar.add(btnDeposerCandidature, btnSuivreCandidature, btnOffresMobilite, btnMesOffresEnregistrees);

        add(menuBar); // Ajouter la barre au layout principal
    }

    private Button createCustomButton(VaadinIcon iconType, String title, String description) {
    // Créer l'icône
    Icon icon = iconType.create();
    icon.getStyle().set("color", "#B22222"); // Couleur de l'icône en rouge
    icon.getStyle().set("font-size", "30px"); // Taille fixe de l'icône pour uniformité
    

    // Créer un conteneur pour l'icône avec une largeur fixe et ajout du marging pour l'espacement
    HorizontalLayout iconContainer = new HorizontalLayout(icon);
    iconContainer.setWidth("50px"); // Ajuster la largeur pour inclure l'espacement
    

    // Créer le texte avec un titre en gras et une description plus petite
    VerticalLayout textLayout = new VerticalLayout();
    textLayout.setSpacing(false);
    textLayout.setPadding(false);

    Span titleSpan = new Span(title);
    titleSpan.getStyle().set("font-weight", "bold");
    titleSpan.getStyle().set("color", "black"); // Couleur du texte
    titleSpan.getStyle().set("margin-right", "170px");
    
    Span descriptionSpan = new Span(description);
    descriptionSpan.getStyle().set("font-size", "12px"); // Taille de texte plus petite pour la description
    descriptionSpan.getStyle().set("color", "gray"); // Couleur grise pour la description

    textLayout.add(titleSpan, descriptionSpan);

    // Layout horizontal pour contenir l'icône et le texte
    HorizontalLayout buttonContent = new HorizontalLayout();
    buttonContent.setAlignItems(Alignment.CENTER); // Centre verticalement
    buttonContent.add(iconContainer, textLayout); // Ajouter le conteneur d'icône à gauche du texte

    // Créer le bouton avec le layout à l'intérieur
    Button button = new Button(buttonContent);
    button.getStyle().set("background-color", "#FFFFFF"); // Couleur de fond blanche
    button.getStyle().set("border-radius", "5px"); // Coins arrondis
    button.getStyle().set("padding", "10px 15px"); // Espacement interne
    button.getStyle().set("border", "none"); // Aucune bordure
    button.getStyle().set("cursor", "pointer"); // Curseur en main
    button.getStyle().set("text-align", "left"); // Aligner le texte à gauche
    button.getStyle().set("width", "400px"); // Largeur du bouton
    button.getStyle().set("height", "120px"); // Hauteur du bouton

    return button;
}


}

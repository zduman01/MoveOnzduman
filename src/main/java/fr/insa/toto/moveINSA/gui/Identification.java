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

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;


@Route("")
public class Identification extends VerticalLayout {

    private Button etudiantButton;
    private Button ecolePartButton;
    private Button sriButton;
    private Button adminButton;

    public Identification() {
        // Style du layout principal pour centrer le contenu
        setSizeFull();
        setAlignItems(Alignment.CENTER); // Aligner horizontalement au centre
        setJustifyContentMode(JustifyContentMode.CENTER); // Aligner verticalement au centre
        
        EnteteInitiale enteteInitiale = new EnteteInitiale();
        add(enteteInitiale);
        this.setPadding(false);
        

        // Appliquer une couleur de fond gris clair
        getStyle().set("background-color", "#f0f0f0"); // Gris clair

        // Zone de connexion
        H3 title = new H3("Se connecter au moyen d'un : ");
        title.getStyle().set("color", "#B22222"); // Couleur rouge pour le titre

        // Boutons d'identification
        etudiantButton = createButton("Compte Étudiant", "etudiant");
        ecolePartButton = createButton("Compte École Partenaire", "ecole_partenaire");
        sriButton = createButton("Compte SRI", "sri");
        adminButton = createButton("Compte Administrateur", "administrateur");

        // Ajouter les boutons au layout
        VerticalLayout formLayout = new VerticalLayout(title, etudiantButton, ecolePartButton, sriButton, adminButton);
        formLayout.setAlignItems(Alignment.CENTER);
        formLayout.getStyle().set("background-color", "#fff"); // Fond blanc pour la zone de connexion
        formLayout.getStyle().set("padding", "40px");
        formLayout.getStyle().set("border-radius", "15px"); // Bordures arrondies
        formLayout.getStyle().set("box-shadow", "0 4px 8px rgba(0, 0, 0, 0.1)"); // Légère ombre pour donner du relief
        formLayout.setWidth("400px"); // Largeur fixe
        formLayout.setHeight("400px"); // Hauteur fixe pour former un carré

        // Ajouter l'en-tête et le formulaire de connexion
        add( formLayout);
    }

    private Button createButton(String buttonText, String role) {
        Button button = new Button(buttonText);
        button.getStyle().set("background-color", "#B22222"); // Bouton rouge
        button.getStyle().set("color", "white"); // Texte du bouton en blanc
        button.getStyle().set("width", "100%"); // Largeur à 100% pour uniformiser la taille
        button.getStyle().set("margin", "10px 0"); // Marge verticale pour espacer les boutons

        // Ajout du listener de clic après la création du bouton
        button.addClickListener(event -> {
            System.out.println("Role avant navigation : " + role); // Debug
            VaadinSession.getCurrent().setAttribute("role", role.toLowerCase().replace(" ", "_"));
            getUI().ifPresent(ui -> ui.navigate("login")); // Navigation vers la page de login
        });

        return button; // Retourne le bouton après l'ajout du listener
    }
}


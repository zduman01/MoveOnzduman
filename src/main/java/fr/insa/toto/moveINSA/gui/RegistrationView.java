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
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import fr.insa.toto.moveINSA.model.ConnectionSimpleSGBD;
import fr.insa.toto.moveINSA.model.Etudiant;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author zilan
 */
@Route("registration") // Route pour la page d'inscription
public class RegistrationView extends VerticalLayout {
    
       private TextField ineField;
        private TextField lastNameField;
        private TextField firstNameField;
        private PasswordField passwordField;
        private PasswordField confirmPasswordField;
        private Button registerButton;

    public RegistrationView() {
        
        

        setSizeFull();
        setAlignItems(Alignment.CENTER); // Aligner horizontalement au centre
        setJustifyContentMode(JustifyContentMode.CENTER); // Aligner verticalement au centre

        // En-tête avec logo à gauche et texte centré
        Image logo = new Image("images/LOGO_INSAStrasbourg.png", "");
        logo.setHeight("60px"); // Ajustez la taille du logo si nécessaire

        H1 headerText = new H1("Création de compte utilisateur");
        headerText.getStyle().set("color", "white");
        headerText.getStyle().set("text-align", "center"); // Centrer le texte

        HorizontalLayout header = new HorizontalLayout(logo, headerText);
        header.setWidthFull(); // Prend toute la largeur
        header.setAlignItems(Alignment.CENTER); // Aligne verticalement le logo et le texte
        header.getStyle().set("background-color", "#B22222"); // Couleur de fond rouge
        header.getStyle().set("padding", "10px");
        header.getStyle().set("position", "fixed"); // Fixe l'en-tête en haut
        header.getStyle().set("top", "0"); // Positionné en haut de la page
        header.getStyle().set("z-index", "1000"); // S'assurer que l'en-tête reste au-dessus
        
        // Appliquer une couleur de fond gris clair
        getStyle().set("background-color", "#f0f0f0"); // Gris clair
        
        add(header);
        
        // Champs d'inscription
        ineField = new TextField("INE");
        lastNameField = new TextField("Nom");
        firstNameField = new TextField("Prénom");
        passwordField = new PasswordField("Mot de passe");
        confirmPasswordField = new PasswordField("Confirmer le mot de passe");
        registerButton = new Button("S'inscrire");

        // Ajouter les composants au layout
        add(header, new H3("Veuillez vous inscrire"), ineField, lastNameField, firstNameField, passwordField, confirmPasswordField, registerButton);

        // Gérer le clic sur le bouton d'enregistrement
        registerButton.addClickListener(event -> {
            try {
                register();
            } catch (SQLException ex) {
                Logger.getLogger(RegistrationView.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }

    private void register() throws SQLException {
        String ine = ineField.getValue();
        String lastName = lastNameField.getValue();
        String firstName = firstNameField.getValue();
        String password = passwordField.getValue();
        String confirmPassword = confirmPasswordField.getValue();

        // Vérification des mots de passe
        if (!password.equals(confirmPassword)) {
            add(new Label("Les mots de passe ne correspondent pas."));
            return;
        }

        // Génération du pseudo
        String pseudo = Etudiant.generatePseudo(firstName,lastName);

        // Enregistrement dans la base de données
        try {
            Connection connection = ConnectionSimpleSGBD.defaultCon(); String sql = "INSERT INTO etudiant (INE, nom, prenom, pseudo, motDePasse) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, ine);
            stmt.setString(2, lastName);
            stmt.setString(3, firstName);
            stmt.setString(4, pseudo);
            stmt.setString(5, password); // Vous devriez envisager de hacher le mot de passe avant de le stocker
            stmt.executeUpdate();
            add(new Label("Inscription réussie !"));
        } catch (SQLException e) {
            add(new Label("Erreur lors de l'enregistrement : " + e.getMessage()));
        }
    }
    
    
   
}

    


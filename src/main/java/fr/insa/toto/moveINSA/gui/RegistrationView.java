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
import com.vaadin.flow.component.combobox.ComboBox;
import java.util.ArrayList;
import java.util.List;




@Route("registration") // Route pour la page d'inscription
public class RegistrationView extends VerticalLayout {

    private TextField ineField;
    private TextField lastNameField;
    private TextField firstNameField;
    private PasswordField passwordField;
    private PasswordField confirmPasswordField;
    private ComboBox<String> specialiteComboBox; // Menu déroulant pour les spécialités
    private Button registerButton;

    public RegistrationView() {
        setSizeFull();
        setAlignItems(Alignment.CENTER); // Aligner horizontalement au centre
        setJustifyContentMode(JustifyContentMode.CENTER); // Aligner verticalement au centre

        // En-tête avec logo à gauche et texte centré
        Image logo = new Image("images/LOGO_INSAStrasbourg.png", "");
        logo.setHeight("60px");

        H1 headerText = new H1("Création de compte utilisateur");
        headerText.getStyle().set("color", "white");
        headerText.getStyle().set("text-align", "center"); // Centrer le texte

        HorizontalLayout header = new HorizontalLayout(logo, headerText);
        header.setWidthFull();
        header.setAlignItems(Alignment.CENTER);
        header.getStyle().set("background-color", "#B22222"); // Couleur de fond rouge
        header.getStyle().set("padding", "10px");
        header.getStyle().set("position", "fixed"); // Fixe l'en-tête en haut
        header.getStyle().set("top", "0"); // Positionné en haut de la page
        header.getStyle().set("z-index", "1000"); // S'assurer que l'en-tête reste au-dessus
        
        // Appliquer une couleur de fond gris clair
        getStyle().set("background-color", "#f0f0f0");
        
        add(header);
        
        // Champs d'inscription
        ineField = new TextField("INE");
        lastNameField = new TextField("Nom");
        firstNameField = new TextField("Prénom");
        specialiteComboBox = new ComboBox<>("Spécialité");
        loadSpecialites(); // Charger les spécialités depuis la base de données
        specialiteComboBox.setPlaceholder("Choisissez une spécialité");
        passwordField = new PasswordField("Mot de passe");
        confirmPasswordField = new PasswordField("Confirmer le mot de passe");
        
        registerButton = new Button("S'inscrire");

        // Ajouter les composants au layout
        add(header, new H3("Veuillez vous inscrire"), ineField, lastNameField, firstNameField, specialiteComboBox, passwordField, confirmPasswordField, registerButton);

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
        String specialite = specialiteComboBox.getValue(); // Obtenir la spécialité sélectionnée

        // Vérification des mots de passe
        if (!password.equals(confirmPassword)) {
            add(new Label("Les mots de passe ne correspondent pas."));
            return;
        }

        // Génération du pseudo
        String pseudo = Etudiant.generatePseudo(firstName, lastName);

        // Enregistrement dans la base de données
        try (Connection connection = ConnectionSimpleSGBD.defaultCon()) {
            // Insérer l'étudiant
            String sqlEtudiant = "INSERT INTO etudiant (INE, nom, prenom, score, idSpecialite) VALUES (?, ?, ?, 0, ?)";
            PreparedStatement stmtEtudiant = connection.prepareStatement(sqlEtudiant);
            stmtEtudiant.setString(1, ine);
            stmtEtudiant.setString(2, lastName);
            stmtEtudiant.setString(3, firstName);
            stmtEtudiant.setString(4, getIdSpecialite(specialite, connection)); 
            stmtEtudiant.executeUpdate();

            // Insérer la connexion de l'étudiant
            String sqlConnection = "INSERT INTO connexion_etudiant (pseudo_etudiant, motDePass_etudiant) VALUES (?, ?)";
            PreparedStatement stmtConnection = connection.prepareStatement(sqlConnection);
            stmtConnection.setString(1, pseudo);
            stmtConnection.setString(2, password);
            stmtConnection.executeUpdate();

            add(new Label("Inscription réussie !"));
        } catch (SQLException e) {
            add(new Label("Erreur lors de l'enregistrement : " + e.getMessage()));
        }
    }

   
    private void loadSpecialites() {
        try (Connection connection = ConnectionSimpleSGBD.defaultCon()) {
            List<String> specialites = new ArrayList<>();
            String sql = "SELECT nomSpecialite FROM specialite"; // Remplace par le bon nom de colonne
            try (PreparedStatement stmt = connection.prepareStatement(sql);
                 ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    specialites.add(rs.getString("nomSpecialite")); // Remplace par le bon nom de colonne
                }
            }
            specialiteComboBox.setItems(specialites); // Remplir le menu avec les spécialités
        } catch (SQLException e) {
            add(new Label("Erreur lors du chargement des spécialités : " + e.getMessage()));
        }
    }

    private String getIdSpecialite(String specialite, Connection connection) throws SQLException {
        // Remplacer par le code pour récupérer l'ID de la spécialité à partir de la base de données
        String sql = "SELECT id FROM specialite WHERE nomSpecialite = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, specialite);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("id");
                }
            }
        }
        return null; // Ou gérer le cas où la spécialité n'est pas trouvée
    }
}

    
    
    
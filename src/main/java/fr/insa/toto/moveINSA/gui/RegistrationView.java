package fr.insa.toto.moveINSA.gui;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Label;
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
import java.util.ArrayList;
import java.util.List;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.Div;

@Route("registration")
public class RegistrationView extends VerticalLayout {

    private TextField ineField;
    private TextField lastNameField;
    private TextField firstNameField;
    private PasswordField passwordField;
    private PasswordField confirmPasswordField;
    private ComboBox<String> specialiteComboBox;
    private Button registerButton;

    public RegistrationView() {
        setSizeFull();

        // Ajouter l'en-tête au layout
        EnteteInitiale enteteInitiale = new EnteteInitiale();
        add(enteteInitiale);

        // Appliquer une couleur de fond gris clair
        getStyle().set("background-color", "#f0f0f0");

        // Créer un conteneur pour le formulaire d'inscription
        VerticalLayout formLayout = new VerticalLayout();

        // Champs d'inscription
        ineField = new TextField("INE");
        lastNameField = new TextField("Nom");
        firstNameField = new TextField("Prénom");
        passwordField = new PasswordField("Mot de passe");
        confirmPasswordField = new PasswordField("Confirmer le mot de passe");
        specialiteComboBox = new ComboBox<>("Spécialité");
        specialiteComboBox.setPlaceholder("Choisissez une spécialité");

        // Rendre les champs plus larges
        ineField.setWidth("300px");
        lastNameField.setWidth("300px");
        firstNameField.setWidth("300px");
        specialiteComboBox.setWidth("300px");
        passwordField.setWidth("300px");
        confirmPasswordField.setWidth("300px");

        // Titre du formulaire
        H3 title = new H3("Veuillez vous inscrire");
        title.getStyle().set("color", "#B22222"); // Couleur rouge pour le titre

        // Charger les spécialités dans la ComboBox
        loadSpecialites();

        // Initialisation du bouton d'enregistrement
        registerButton = new Button("S'inscrire", event -> handleRegistration());
        registerButton.getStyle().set("background-color", "#B22222"); // Bouton rouge
        registerButton.getStyle().set("color", "white"); // Texte du bouton en blanc

        // Ajouter les composants au conteneur de formulaire
        formLayout.add(title, ineField, lastNameField, firstNameField, specialiteComboBox, passwordField, confirmPasswordField, registerButton);
        formLayout.getStyle().set("background-color", "#fff"); // Fond blanc pour la zone de connexion
        formLayout.getStyle().set("padding", "40px");
        formLayout.getStyle().set("border-radius", "15px"); // Bordures arrondies
        formLayout.getStyle().set("box-shadow", "0 4px 8px rgba(0, 0, 0, 0.1)"); // Légère ombre pour donner du relief
        formLayout.setMaxWidth("500px"); // Largeur max du formulaire
        formLayout.setWidthFull(); // Prendre toute la largeur
        formLayout.setAlignItems(Alignment.CENTER); // Centrer horizontalement
        formLayout.setJustifyContentMode(JustifyContentMode.CENTER); // Centrer verticalement

        // Créer un espace de 50 pixels
        Div space = new Div();
        space.setHeight("50px");

        // Utiliser un conteneur parent pour centrer le formulaire
        VerticalLayout mainLayout = new VerticalLayout(enteteInitiale, space, formLayout);
        mainLayout.setSizeFull(); // Prendre toute la taille disponible
        mainLayout.setJustifyContentMode(JustifyContentMode.CENTER); // Centrer verticalement dans le layout principal
        mainLayout.setAlignItems(Alignment.CENTER); // Centrer horizontalement dans le layout principal

        // Ajouter le conteneur au layout principal
        add(mainLayout);
    }

    private void handleRegistration() {
        try {
            register();
        } catch (SQLException e) {
            add(new Label("Erreur lors de l'enregistrement : " + e.getMessage()));
        }
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
            try (PreparedStatement stmtEtudiant = connection.prepareStatement(sqlEtudiant)) {
                stmtEtudiant.setString(1, ine);
                stmtEtudiant.setString(2, lastName);
                stmtEtudiant.setString(3, firstName);
                stmtEtudiant.setString(4, getIdSpecialite(specialite, connection));
                stmtEtudiant.executeUpdate();
            }

            // Insérer la connexion de l'étudiant
            String sqlConnection = "INSERT INTO connexion_etudiant (pseudo_etudiant, motDePass_etudiant) VALUES (?, ?)";
            try (PreparedStatement stmtConnection = connection.prepareStatement(sqlConnection)) {
                stmtConnection.setString(1, pseudo);
                stmtConnection.setString(2, password);
                stmtConnection.executeUpdate();
            }

            add(new Label("Inscription réussie !"));
            getUI().ifPresent(ui -> ui.navigate("login"));
        }
    }

    private void loadSpecialites() {
        try (Connection connection = ConnectionSimpleSGBD.defaultCon()) {
            List<String> specialites = new ArrayList<>();
            String sql = "SELECT nomSpecialite FROM specialite"; // Remplacez par le bon nom de colonne
            try (PreparedStatement stmt = connection.prepareStatement(sql);
                 ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    specialites.add(rs.getString("nomSpecialite")); // Remplacez par le bon nom de colonne
                }
            }
            specialiteComboBox.setItems(specialites); // Remplir le menu avec les spécialités
        } catch (SQLException e) {
            add(new Label("Erreur lors du chargement des spécialités : " + e.getMessage()));
        }
    }

    private String getIdSpecialite(String specialite, Connection connection) throws SQLException {
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


    
    
    
package fr.insa.toto.moveINSA.gui;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import fr.insa.toto.moveINSA.model.ConnectionSimpleSGBD;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Route("") // Route pour la page de connexion
public class LoginView extends VerticalLayout {

    private TextField usernameField;
    private PasswordField passwordField;
    private Button loginButton;

    public LoginView() {
        // Style du layout principal pour centrer le contenu
        setSizeFull();
        setAlignItems(Alignment.CENTER); // Aligner horizontalement au centre
        setJustifyContentMode(JustifyContentMode.CENTER); // Aligner verticalement au centre

        // En-tête avec logo à gauche et texte centré
        Image logo = new Image("images/LOGO_INSAStrasbourg.png", "");
        logo.setHeight("60px"); // Ajustez la taille du logo si nécessaire

        H1 headerText = new H1("Bienvenue sur MoveINSA");
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

        // Zone de connexion
        H3 title = new H3("Veuillez vous connecter");
        title.getStyle().set("color", "#B22222"); // Couleur rouge pour le titre

        // Champs de saisie
        usernameField = new TextField("Nom d'utilisateur");
        usernameField.getStyle().set("border", "2px solid #B22222"); // Bordure rouge épaisse
        usernameField.getStyle().set("border-radius", "10px"); // Bordures arrondies
        usernameField.getElement().getStyle().set("--lumo-primary-text-color", "\"#B22222\"");
        usernameField.setWidth("300px");

        passwordField = new PasswordField("Mot de passe");
        passwordField.getStyle().set("border", "2px solid #B22222"); // Bordure rouge épaisse
        passwordField.getStyle().set("border-radius", "10px"); // Bordures arrondies
        passwordField.getElement().getStyle().set("--lumo-primary-text-color", "\"#B22222\"");
        passwordField.setWidth("300px");

        // Bouton de connexion
        loginButton = new Button("Se connecter");
        loginButton.getStyle().set("background-color", "#B22222"); // Bouton rouge
        loginButton.getStyle().set("color", "white"); // Texte du bouton en blanc

        // Lien pour l'enregistrement
        Anchor registerLink = new Anchor("registration", "Première connexion ?");
        registerLink.getStyle().set("color", "#B22222"); // Lien en rouge

        // Ajouter les champs de connexion
        VerticalLayout formLayout = new VerticalLayout(title, usernameField, passwordField, loginButton, registerLink);
        formLayout.setAlignItems(Alignment.CENTER);
        formLayout.getStyle().set("background-color", "#fff"); // Fond blanc pour la zone de connexion
        formLayout.getStyle().set("padding", "40px");
        formLayout.getStyle().set("border-radius", "15px"); // Bordures arrondies
        formLayout.getStyle().set("box-shadow", "0 4px 8px rgba(0, 0, 0, 0.1)"); // Légère ombre pour donner du relief
        formLayout.setWidth("400px"); // Largeur fixe
        formLayout.setHeight("400px"); // Hauteur fixe pour former un carré

        formLayout.add(registerLink);

        // Gestion du clic sur le bouton de connexion
        loginButton.addClickListener(event -> {
            try {
                login();
            } catch (SQLException e) {
                e.printStackTrace();
                add(new Paragraph("Erreur lors de la vérification des identifiants."));
            }
        });

        // Ajouter l'en-tête et le formulaire de connexion
        add(header, formLayout);
    }

    private void login() throws SQLException {
        String username = usernameField.getValue();
        String password = passwordField.getValue();

        // Connexion à la base de données
        Connection connection = ConnectionSimpleSGBD.defaultCon(); // Remplace par ta méthode de connexion

        // Préparer la requête SQL
        String sql = "SELECT * FROM connexion_etudiant WHERE pseudo_etudiant = ? AND motDePass_etudiant = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, username);  // Insertion du pseudo dans la requête
            preparedStatement.setString(2, password);  // Insertion du mot de passe dans la requête

            // Exécuter la requête
            ResultSet resultSet = preparedStatement.executeQuery();

            // Si un utilisateur est trouvé, la connexion est validée
            if (resultSet.next()) {
                VaadinSession.getCurrent().setAttribute("user", username);
                getUI().ifPresent(ui -> ui.navigate("main"));
            } else {
                // Afficher un message d'erreur si les identifiants sont incorrects
                add(new Paragraph("Nom d'utilisateur ou mot de passe incorrect"));
            }
        } finally {
            connection.close();
        }
    }
}

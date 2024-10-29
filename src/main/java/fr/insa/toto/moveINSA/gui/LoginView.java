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

@Route("login") // Route pour la page de connexion
public class LoginView extends VerticalLayout {

    private TextField usernameField;
    private PasswordField passwordField;
    private Button loginButton;
    String redirectPage="" ;
    

    public LoginView() {
        // Style du layout principal pour centrer le contenu
        setSizeFull();
        
        EnteteInitiale enteteInitiale = new EnteteInitiale();
        add(enteteInitiale);
        this.setPadding(false);
        

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
        
        loginButton.addClickListener(event -> {
        login(); // Appel de la méthode de connexion
        });
        
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
        formLayout.setHeight("400px");
        
        formLayout.getStyle().set("margin-top", "200px");// Hauteur fixe pour former un carré
        formLayout.setAlignItems(Alignment.CENTER); // Aligner horizontalement au centre
        
        formLayout.add(registerLink);
        setAlignItems(Alignment.CENTER);
        add(formLayout);
        
    }

    private void login() {
    String role = (String) VaadinSession.getCurrent().getAttribute("role"); // Récupérer le rôle depuis la session
    String username = usernameField.getValue();
    String password = passwordField.getValue();

    boolean isAuthenticated = authenticateUser(role, username, password);
    if (isAuthenticated) {
        // Redirection ou actions après la connexion réussie
        Notification.show("Connexion réussie !");
        getIdUtilisateur(username, password);
        // Rediriger vers la page appropriée en fonction du rôle
        redirectToHome(role);
    } else {
        Notification.show("Identifiants incorrects !");
    }
}

    private boolean authenticateUser(String role, String username, String password) {
        try (Connection connection = ConnectionSimpleSGBD.defaultCon()) {
            String sql = "";
            switch (role) {
                case "etudiant":
                    sql = "SELECT * FROM connexion_etudiant WHERE pseudo_etudiant = ? AND motDePass_etudiant = ?";
                    break;
                case "sri":
                    sql = "SELECT * FROM connexion_sri WHERE pseudo_sri = ? AND motDePass_sri = ?";
                    break;
                case "ecole_partenaire":
                    sql = "SELECT * FROM connexion_ecole WHERE pseudo_ecole = ? AND motDePass_ecole = ?";
                    break;
                case "administrateur":
                    sql = "SELECT * FROM connexion_admin WHERE pseudo_admin = ? AND motDePass_admin = ?";
                    break;
            }

            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setString(1, username);
                stmt.setString(2, password);
                try (ResultSet rs = stmt.executeQuery()) {
                    return rs.next(); // Retourne vrai si un enregistrement est trouvé
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false; // Gérer les exceptions comme tu le souhaites
        }
    }
    
    private void getIdUtilisateur(String username, String password) {
        try (Connection connection = ConnectionSimpleSGBD.defaultCon()) {
            // Préparation de la requête pour l'authentification
            String sql = "SELECT id FROM connexion_etudiant WHERE pseudo_etudiant = ? AND motDePass_etudiant = ?";
            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setString(1, username);
                stmt.setString(2, password);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        int userId = rs.getInt("id");
                       
                        // Stockage de l'ID et du rôle dans la session
                        VaadinSession.getCurrent().setAttribute("userId", userId);
                        
                        
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            Notification.show("Erreur de connexion à la base de données.");
        }
        
    }

   private void redirectToHome(String role) {
    
    switch (role) {
        case "etudiant":
            redirectPage = "mainEtudiant"; // Remplace par la page d'accueil de l'étudiant
            break;
        case "sri":
            redirectPage = "mainSRI"; // Remplace par la page d'accueil du SRI
            break;
        case "ecole_partenaire":
            redirectPage = "mainPartenaire"; // Remplace par la page d'accueil de l'école partenaire
            break;
        case "administrateur":
            redirectPage = "mainAdmin"; // Remplace par la page d'accueil de l'administrateur
            break;
    }
    // Assurez-vous que vous êtes dans le bon contexte pour appeler getUI()
    getUI().ifPresent(ui -> ui.navigate(redirectPage));
}

}
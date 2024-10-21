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
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import fr.insa.toto.moveINSA.gui.EnteteInitiale;

@Route("home")
public class HomeView extends VerticalLayout {

    public HomeView() {
        // Vérifiez si l'utilisateur est connecté
        if (VaadinSession.getCurrent().getAttribute("user") == null) {
            // Redirigez vers la page de connexion si l'utilisateur n'est pas connecté
            getUI().ifPresent(ui -> ui.navigate("login"));
            Notification.show("Veuillez vous connecter d'abord.", 3000, Notification.Position.MIDDLE);
            return; // Sortez si l'utilisateur n'est pas connecté
        }

        // Contenu de votre page principale
        add(new EnteteInitiale()); // Ajoutez votre en-tête ici
        add(new H3("Bienvenue sur l'application INSA")); // Exemple de contenu
    }
}

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

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import fr.insa.beuvron.utils.exceptions.ExceptionsUtils;
import fr.insa.toto.moveINSA.gui.session.SessionInfo;
import fr.insa.toto.moveINSA.gui.vues.NoConnectionToBDDErrorPanel;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.SQLException;

/**
 * Utilisé par toutes les pages comme layout. 
 * <p> C'est ici que sont initialisées les
 * infos valables pour l'ensemble de la session, et en particulier la connection
 * à la base de donnée.
 * </p>
 * @author francois
 */
public class MainLayout extends AppLayout implements BeforeEnterObserver {

    private MenuGauche menuGauche;

    public MainLayout() {
//        System.out.println("MainLayout constructeur "+this);
        this.menuGauche = new MenuGauche();
        this.addToDrawer(this.menuGauche);
        this.addToNavbar(new EnteteInitiale());
    }

    /**
     * Cette méthode est appelée systématiquement par vaadin avant l'affichage de toute
     * page ayant ce layout (donc à priori toutes les pages "normales" sauf 
     * pages d'erreurs) de l'application.
     * <p> teste systématiquement la connection à la base de donnée, et 
     * redirige vers une page d'erreur si la connection ne peut pas être 
     * établie
     * </p>
     * @param bee 
     */
    @Override
    public void beforeEnter(BeforeEnterEvent bee) {
        try {
            SessionInfo.getOrCreateConnectionToBdD();
        } catch (SQLException  ex) {
            // debug partiel
            Notification.show(ExceptionsUtils.messageEtPremiersAppelsDansPackage(ex, "fr.insa", 6));
            // debug complet
//            StringWriter mess = new StringWriter();
//            PrintWriter out = new PrintWriter(mess);
//            ex.printStackTrace(out);
//            Notification.show(mess.toString());
            // end debug
//            bee.rerouteTo(NoConnectionToBDDErrorPanel.class);   // normal
        }
    }

}

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

import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import fr.insa.toto.moveINSA.gui.MainLayoutEtudiant;
import fr.insa.toto.moveINSA.gui.session.SessionInfo;
import fr.insa.toto.moveINSA.model.GestionBdD;
import java.sql.SQLException;

/**
 *
 * @author francois
 */
@PageTitle("MoveINSA")
@Route(value = "debug/RAZBDD", layout = MainLayoutEtudiant.class)
public class RAZBdDPanel extends VerticalLayout {
    
    public RAZBdDPanel() {
        try {
            GestionBdD.razBDD(SessionInfo.getOrCreateConnectionToBdD());
            this.add(new H3("La base de données a été (ré-)initalisée"));
        } catch (SQLException ex) {
            System.out.println("Problème : " + ex.getLocalizedMessage());
            Notification.show("Problème : " + ex.getLocalizedMessage());
        }
    }
    
}

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
package fr.insa.toto.moveINSA.gui.session;

import com.vaadin.flow.server.ServiceInitEvent;
import com.vaadin.flow.server.VaadinServiceInitListener;
import java.sql.SQLException;
import org.springframework.stereotype.Component;

/**
 * Permet de couper automatiquement la connection à la BdD à la fin d'une
 * session.
 *
 * @author francois
 */
@Component
public class ServiceListener implements VaadinServiceInitListener {

    @Override
    public void serviceInit(ServiceInitEvent event) {

        // on ne cré pas automatiquement la connection à la BdD à l'ouverture
        // de la session : parfois certaines sessions inutiles sont crées
        // La connection sera crée la première fois que l'on veut y acceder
        // grace à la méthode getOrCreateConnectionToBdD de SessionInfo
//        event.getSource().addSessionInitListener(
//                initEvent -> {
//                    try {
//                        System.out.println("connecting to BDD for session " + initEvent.getSession());
//                        SessionInfo.connectToBDD(initEvent.getSession());
//                    } catch (ClassNotFoundException | SQLException ex) {
//                        throw new Error(ex);
//                    }
//                });

        // par contre on ferme automatiquement la connection à la fermeture de
        // la session. C'est dans closeConnectionToBDD que l'on vérifie que
        // la connection existe bien avant de la supprimer
        // normalement, vaadin va recréer automatiquement une nouvelle session
        // la prochaine demande d'une connection sur cette nouvelle session
        // créera une nouvelle connection à la BdD
        event.getSource().addSessionDestroyListener(
                destroyEvent -> {
                    try {
//                        System.out.println("closing connection to BDD for session " + destroyEvent.getSession());
                        SessionInfo.closeConnectionToBDD(destroyEvent.getSession());
                    } catch (SQLException ex) {
                        throw new Error(ex);
                    }
                });

    }
}

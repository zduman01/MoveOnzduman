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

import com.vaadin.flow.server.VaadinSession;
import fr.insa.toto.moveINSA.model.ConnectionSimpleSGBD;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Conserve les informations partagées par diverses pages dans une même session.
 * <p> les sessions sont gérées par vaadin. Normalement tout affichage d'une
 * page dans vaadin se fait dans une session.
 * </p>
 * <p> Cette classe rassemble dans ses attributs toutes les informations 
 * utiles qui doivent être passées d'une page à l'autre dans une même
 * session. Exemple classique : l'utilisateur se connecte sur une page,
 * et l'identité de l'utilisateur connecté doit être conservé pour savoir
 * par exemple les actions qu'il a droit de faire, ou pour afficher 
 * des informations spécifique à cet utilisateur.
 * </p>
 * <p> Pour l'instant, on ne conserve que la connection à la BdD, mais
 * n'hésitez pas à ajouter des attributs pour conserver d'autres infos.
 * </p>
 * <p> Les informations de sessions sont censées être Serializable
 * mais cela est difficile à assurer, en particulier la connection à la 
 * BdD n'est pas sérializable. On indique par le mot clé "transient"
 * sur la définition des attributs que ces attributs ne doivent pas être 
 * sérialisés.
 * </p>
 * 
 * @author francois
 */
public class SessionInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Problème de session.
     */
    public static class SessionException extends Exception {

        public SessionException(String message) {
            super(message);
        }
    }

    private transient Connection conBDD;

    public SessionInfo() {
        this.conBDD = null;
    }

    /**
     * @return the current session
     */
    public static VaadinSession curSession() throws SessionException {
        VaadinSession curS = VaadinSession.getCurrent();
        if (curS == null) {
            throw new SessionException("No Vaadin Session");
        }
        return curS;
    }

    /**
     * get the SessionInfo associated with the current session or create a new
     * SessionInfo and associate it with the current session.
     * <p> voir aussi 
     *
     * @return the SessionInfo associated with current session
     */
    public static SessionInfo getOrCreateCurSessionInfo() throws SessionException {
        VaadinSession curS = curSession();
        SessionInfo res = curS.getAttribute(SessionInfo.class);
        if (res == null) {
            res = new SessionInfo();
            curS.setAttribute(SessionInfo.class, res);
        }
        return res;
    }

    public static Connection getOrCreateConnectionToBdD() throws SQLException {
        try {
            SessionInfo curI = getOrCreateCurSessionInfo();
            if (curI.conBDD == null) {
                curI.conBDD = ConnectionSimpleSGBD.defaultCon();
            }
            return curI.conBDD;
        } catch (SessionException ex) {
            // je ne fais rien s'il n'y a pas de session : on est sans doute
            // dans un état provisoire/instable du serveur
            // la connection sera redemandée quand il y aura une vrai session
            return null;
        }
    }

    public static void closeConnectionToBDD(VaadinSession closingSession) throws SQLException {
        SessionInfo si = closingSession.getAttribute(SessionInfo.class);
        if (si != null && si.conBDD != null) {
            si.conBDD.close();
        }
    }

}

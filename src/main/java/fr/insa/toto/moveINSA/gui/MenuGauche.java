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

import com.vaadin.flow.component.sidenav.SideNav;
import com.vaadin.flow.component.sidenav.SideNavItem;
import fr.insa.toto.moveINSA.gui.vues.NouveauPartenairePanel;
import fr.insa.toto.moveINSA.gui.vues.PartenairesPanel;
import fr.insa.toto.moveINSA.gui.vues.RAZBdDPanel;
import fr.insa.toto.moveINSA.gui.vues.TodoPanel;

/**
 *
 * @author francois
 */
public class MenuGauche extends SideNav {

    public MenuGauche() {
        SideNavItem partenaires = new SideNavItem("partenaires");
        partenaires.addItem(new SideNavItem("liste", PartenairesPanel.class));
        partenaires.addItem(new SideNavItem("nouveau", NouveauPartenairePanel.class));
        SideNavItem offres = new SideNavItem("offres");
        offres.addItem(new SideNavItem("liste", TodoPanel.class));
        offres.addItem(new SideNavItem("nouvelle", TodoPanel.class));
        SideNavItem debug = new SideNavItem("debug");
        debug.addItem(new SideNavItem("raz BdD", RAZBdDPanel.class));
       this.addItem(partenaires,offres,debug);
    }
}

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
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Route;

/**
 * Utilisé par toutes les pages comme layout.
 * <p>
 * C'est ici que sont initialisées les infos valables pour l'ensemble de la
 * session, et en particulier la connection à la base de donnée.
 * </p>
 *
 * @author francois
 */
@Route("main")
public class MainLayout extends AppLayout implements BeforeEnterObserver {

    

    public MainLayout() {
//        System.out.println("MainLayout constructeur "+this);
      
         
    }

    /**
     * Cette méthode est appelée systématiquement par vaadin avant l'affichage
     * de toute page ayant ce layout (donc à priori toutes les pages "normales"
     * sauf pages d'erreurs) de l'application.
     * <p>
     * pour l'instant, je ne m'en sers pas, mais je l'ai gardé pour me souvenir
     * de cette possibilité
     * </p>
     *
     * @param bee
     */
    @Override
    public void beforeEnter(BeforeEnterEvent bee) {
        // permet par exemple de modifier la destination en cas 
        // de problème
//            bee.rerouteTo(NoConnectionToBDDErrorPanel.class);

    }

}

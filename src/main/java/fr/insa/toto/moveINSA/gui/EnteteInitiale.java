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

import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;

import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;

public class EnteteInitiale extends HorizontalLayout {

    public EnteteInitiale() {
        // Créer le logo avec le chemin direct
        Image logo = new Image("images/LOGO_INSAStrasbourg.png", "Logo INSA Strasbourg");
        logo.setHeight("80px"); // Ajuster la hauteur du logo
        logo.setWidth("auto");  // Ajuster la largeur automatiquement pour garder le ratio

        // Ajouter le logo à l'en-tête
        this.add(logo);

        // Appliquer des styles pour la barre d'en-tête
        this.setWidthFull(); // Occupe toute la largeur
        this.getStyle().set("background-color", "red"); // Couleur de fond rouge
        this.setAlignItems(Alignment.CENTER); // Aligner verticalement au centre
        this.setHeight("100px"); // Hauteur de l'en-tête
    }
}
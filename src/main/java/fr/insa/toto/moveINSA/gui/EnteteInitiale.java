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

import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;

import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;

public class EnteteInitiale extends HorizontalLayout {

    public EnteteInitiale() {
        // Créer le logo avec le chemin direct
        
        // En-tête avec logo à gauche et texte centré
        Image logo = new Image("images/LOGO_INSA.png", "");
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
        header.getStyle().set("top", "0"); 
        header.getStyle().set("left", "0");// Positionné en haut de la page
        header.getStyle().set("z-index", "1000"); // S'assurer que l'en-tête reste au-dessus
        add( header);
    }
}
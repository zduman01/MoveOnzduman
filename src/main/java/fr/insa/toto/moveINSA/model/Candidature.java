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
package fr.insa.toto.moveINSA.model;

import java.time.LocalDateTime;

/**
 *
 * @author zilan
 */
public class Candidature {
    private int id;                // Identifiant de la candidature
    private int idEtudiant;        // Identifiant de l'étudiant
    private int idOffreMobilite;   // Identifiant de l'offre de mobilité
    private String statut;         // Statut de la candidature
    private LocalDateTime dateDepot; // Date et heure du dépôt de la candidature

    // Constructeur
    public Candidature(int id, int idEtudiant, int idOffreMobilite, String statut, LocalDateTime dateDepot) {
        this.id = id;
        this.idEtudiant = idEtudiant;
        this.idOffreMobilite = idOffreMobilite;
        this.statut = statut;
        this.dateDepot = dateDepot;
    }

    // Getters et Setters
    public int getId() {
        return id;
    }

    public int getIdEtudiant() {
        return idEtudiant;
    }

    public int getIdOffreMobilite() {
        return idOffreMobilite;
    }

    public String getStatut() {
        return statut;
    }

    public LocalDateTime getDateDepot() {
        return dateDepot;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }
}

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
    private int voeux1;
    private int voeux2;
    private int voeux3;
    private int voeux4;
    private int voeux5;
    private String statut;         // Statut de la candidature
    private LocalDateTime dateDepot; // Date et heure du dépôt de la candidature


    public Candidature(int id, int idEtudiant, int voeux1, int voeux2, int voeux3, int voeux4, int voeux5, String statut, LocalDateTime dateDepot) {
        this.id = id;
        this.idEtudiant = idEtudiant;
        this.voeux1 = voeux1;
        this.voeux2 = voeux2;
        this.voeux3 = voeux3;
        this.voeux4 = voeux4;
        this.voeux5 = voeux5;
        this.statut = statut;
        this.dateDepot = dateDepot;
    }

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return the idEtudiant
     */
    public int getIdEtudiant() {
        return idEtudiant;
    }

    /**
     * @param idEtudiant the idEtudiant to set
     */
    public void setIdEtudiant(int idEtudiant) {
        this.idEtudiant = idEtudiant;
    }

    /**
     * @return the voeux1
     */
    public int getVoeux1() {
        return voeux1;
    }

    /**
     * @param voeux1 the voeux1 to set
     */
    public void setVoeux1(int voeux1) {
        this.voeux1 = voeux1;
    }

    /**
     * @return the voeux2
     */
    public int getVoeux2() {
        return voeux2;
    }

    /**
     * @param voeux2 the voeux2 to set
     */
    public void setVoeux2(int voeux2) {
        this.voeux2 = voeux2;
    }

    /**
     * @return the voeux3
     */
    public int getVoeux3() {
        return voeux3;
    }

    /**
     * @param voeux3 the voeux3 to set
     */
    public void setVoeux3(int voeux3) {
        this.voeux3 = voeux3;
    }

    /**
     * @return the voeux4
     */
    public int getVoeux4() {
        return voeux4;
    }

    /**
     * @param voeux4 the voeux4 to set
     */
    public void setVoeux4(int voeux4) {
        this.voeux4 = voeux4;
    }

    /**
     * @return the voeux5
     */
    public int getVoeux5() {
        return voeux5;
    }

    /**
     * @param voeux5 the voeux5 to set
     */
    public void setVoeux5(int voeux5) {
        this.voeux5 = voeux5;
    }

    /**
     * @return the statut
     */
    public String getStatut() {
        return statut;
    }

    /**
     * @param statut the statut to set
     */
    public void setStatut(String statut) {
        this.statut = statut;
    }

    /**
     * @return the dateDepot
     */
    public LocalDateTime getDateDepot() {
        return dateDepot;
    }

    /**
     * @param dateDepot the dateDepot to set
     */
    public void setDateDepot(LocalDateTime dateDepot) {
        this.dateDepot = dateDepot;
    }

    @Override
    public String toString() {
        return "Candidature{" + "id=" + id + ", idEtudiant=" + idEtudiant + ", voeux1=" + voeux1 + ", voeux2=" + voeux2 + ", voeux3=" + voeux3 + ", voeux4=" + voeux4 + ", voeux5=" + voeux5 + ", statut=" + statut + ", dateDepot=" + dateDepot + '}';
    }

    
}

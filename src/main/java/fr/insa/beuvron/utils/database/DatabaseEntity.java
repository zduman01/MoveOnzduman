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
package fr.insa.beuvron.utils.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Représente une entité de la base de donnée dans Java.
 * <p>
 * permet de gérer les identificateurs créés automatiquement par le sgbd </p>
 * <p>
 * Un DatabaseEntity e est crée avec un id égal à -1; lorsque e est sauvegardé
 * (saveInDB) dans la base de donnée, son id est mis à jour avec
 * l'identificateur qui lui a été affecté par le SGBD.
 * <p>
 * <p>
 * Cette classe abstraite peut servir de base aux différentes classes
 * représentant effectivement des tables d'entités dans la BdD.</p>
 * <p>
 * Typiquement, les méthodes de création utiliserons un PreparedStatement avec
 * un INSERT qui contiendra les colonnes spécifiques de la table. C'est le but
 * de la méthode abstraite createInsertStatement. La méthode saveInDB appelle
 * cette méthode, exécute le statement, et s'occupe de récupérer
 * l'identificateur créé.
 * </p>
 * <p>
 * ATTENTION : ce PreparedStatement devra être créé avec l'option
 * PreparedStatement.RETURN_GENERATED_KEYS
 * </p>
 *
 * @author francois
 */
public abstract class DatabaseEntity {

    private int id;

    /**
     * Constructeur typiquement utilisé lorsque l'on a retrouvé une entité déjà
     * existante dans la base de donnée.
     *
     * @param id
     */
    public DatabaseEntity(int id) {
        this.id = id;
    }

    /**
     * Constructeur typiquement utilisé lorsque l'on crée une nouvelle entité en
     * mémoire avant de la sauvegarder dans la base de donnée.
     */
    public DatabaseEntity() {
        this(-1);
    }

    /**
     * Doit renvoyer un PreparedStatement permettant d'insérer une nouvelle entité
     * dans la base de donnée.
     * <p> ATTENTION : Ce PreparedStatement doit avoir l'option 
     * PreparedStatement.RETURN_GENERATED_KEYS.
     * </p>
     * @param con
     * @return 
     */
    public abstract PreparedStatement preparedStatementPourInsert(Connection con)
            throws SQLException; 
    
    public static class EntiteDejaSauvegardee extends SQLException {
        public EntiteDejaSauvegardee() {
            super("L'entité à déjà été sauvegardée (id != -1");
        }
    }
    
    /**
     * Sauvegarde une nouvelle entité et retourne la clé affecté automatiquement
     * par le SGBD.
     * <p> la clé est également sauvegardée dans l'attribut id de la DatabaseEntity
     * </p>
     * @param con
     * @return la clé de la nouvelle entité dans la table de la BdD
     * @throws EntiteDejaSauvegardee si l'id de l'entité est différent de -1 
     * @throws SQLException si autre problème avec la BdD
     */
    public int saveInDB(Connection con) throws SQLException {
        if (this.id != -1) {
            throw new EntiteDejaSauvegardee();
        }
        PreparedStatement insert = this.preparedStatementPourInsert(con);
        insert.executeUpdate();
        try (ResultSet rid = insert.getGeneratedKeys()) {
            rid.next();
            this.id = rid.getInt(1);
            return this.id;
        }
    }

    public int getId() {
        return id;
    }

    @Override
    public int hashCode() {
        if (this.id != -1) {
            return this.id;
        } else {
            return super.hashCode();
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        DatabaseEntity other = (DatabaseEntity) obj;
        if (this.id == -1 && other.id == -1) {
            return this == other;
        } else {
            return this.id == other.id;
        }
    }

}

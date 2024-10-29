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
import com.vaadin.flow.component.html.Label;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Specialite {
 
    
    public static List<String> loadSpecialites() {
    List<String> specialites = new ArrayList<>();
    // Il n'est pas nécessaire de passer la connexion si la méthode est statique
    try (Connection connection = ConnectionSimpleSGBD.defaultCon()) {
        String sql = "SELECT nomSpecialite FROM specialite";
        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                specialites.add(rs.getString("nomSpecialite"));
            }
        }
    } catch (SQLException e) {
        // Log de l'erreur (vous pouvez aussi lancer une exception si nécessaire)
        System.err.println("Erreur lors du chargement des spécialités : " + e.getMessage());
    }
    return specialites;
}
    
}

package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import models.Utilisateur;

public class LoginDAO {

    private Connection conn;

    public LoginDAO() {
        try {
            conn = Connexion.getConnexion();
        } catch (Exception e) {
            System.out.println("Erreur : " + e.getMessage());
        }
    }

    public Utilisateur connecter(String nomUtilisateur, String motDePasse) {
        try {
            String sql = "SELECT * FROM utilisateur WHERE nomUtilisateur=? AND motDePasse=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, nomUtilisateur);
            ps.setString(2, motDePasse);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Utilisateur(
                    rs.getInt("id"),
                    rs.getString("nomUtilisateur"),
                    rs.getString("motDePasse"),
                    rs.getString("nom"),
                    rs.getString("prenom")
                );
            }
            return null; // null = échec connexion
        } catch (Exception e) {
            System.out.println("Erreur login : " + e.getMessage());
            return null;
        }
    }
}
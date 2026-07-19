/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import models.Lecteur;

/**
 *
 * @author Admin
 */
public class LecteurDAO {

    private Connection conn;

    public LecteurDAO() {
        try {
            conn = Connexion.getConnexion();
        } catch (Exception e) {
            System.out.println("Erreur : " + e.getMessage());
        }
    }

    //Ajpouter un lecteur
    public boolean ajouter(Lecteur lecteur) {
        try {
            String sql = "INSERT INTO lecteur(nom, prenom, telephone,adresse, dateInscription) VALUES(?,?,?,?,?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, lecteur.getNom());
            ps.setString(2, lecteur.getPrenom());
            ps.setString(3, lecteur.getTelephone());
            ps.setString(4, lecteur.getAdresse());
            ps.setString(5, lecteur.getDateInscription());
            ps.executeUpdate();
            models.Journal.ajouter(models.Journal.TYPE_LECTEUR,
                    "Lecteur inscrit : " + lecteur.getNom()
                    + " " + lecteur.getPrenom());
            return true;
        } catch (Exception e) {
            System.out.println("Erreur ajout : " + e.getMessage());
            return false;
        }
    }

    // Récupérer tous les lecteurs
    public ArrayList<Lecteur> getTousLesLecteurs() {
        ArrayList<Lecteur> lecteurs = new ArrayList<>();
        try {
            String sql = "SELECT * FROM lecteur ORDER BY idLecteur DESC";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Lecteur lecteur = new Lecteur(
                        rs.getInt("idLecteur"),
                        rs.getString("nom"),
                        rs.getString("prenom"),
                        rs.getString("telephone"),
                        rs.getString("adresse"),
                        rs.getString("dateInscription")
                );
                lecteurs.add(lecteur);
            }
        } catch (Exception e) {
            System.out.println("Erreur lecture : " + e.getMessage());
        }
        return lecteurs;
    }

    // Modifier un lecteur
    public boolean modifier(Lecteur lecteur) {
        try {
            String sql = "UPDATE lecteur SET nom=?, prenom=?, telephone=?,adresse=? WHERE idLecteur=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, lecteur.getNom());
            ps.setString(2, lecteur.getPrenom());
            ps.setString(3, lecteur.getTelephone());
            ps.setString(4, lecteur.getAdresse());
            ps.setInt(5, lecteur.getIdLecteur());
            ps.executeUpdate();
            return true;
        } catch (Exception e) {
            System.out.println("Erreur modification : " + e.getMessage());
            return false;
        }
    }

    // Supprimer un lecteur
    public boolean supprimer(int idLecteur) {
        try {
            String sql = "DELETE FROM lecteur WHERE idLecteur=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, idLecteur);
            ps.executeUpdate();
            return true;
        } catch (Exception e) {
            System.out.println("Erreur suppression : " + e.getMessage());
            return false;
        }
    }

    // Rechercher un lecteur
    public ArrayList<Lecteur> rechercher(String texte) {
        ArrayList<Lecteur> lecteurs = new ArrayList<>();
        try {
            String sql = "SELECT * FROM lecteur WHERE nom LIKE ? OR prenom LIKE ? OR telephone LIKE ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, "%" + texte + "%");
            ps.setString(2, "%" + texte + "%");
            ps.setString(3, "%" + texte + "%");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Lecteur lecteur = new Lecteur(
                        rs.getInt("idLecteur"),
                        rs.getString("nom"),
                        rs.getString("prenom"),
                        rs.getString("telephone"),
                        rs.getString("adresse"),
                        rs.getString("dateInscription")
                );
                lecteurs.add(lecteur);
            }
        } catch (Exception e) {
            System.out.println("Erreur recherche : " + e.getMessage());
        }
        return lecteurs;
    }

    public boolean aDesEmpruntsEnCours(int idLecteur) {
    try {
        String sql = "SELECT COUNT(*) FROM emprunt "
            + "WHERE idLecteur = ? AND statut IN ('en cours', 'retard')";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, idLecteur);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) return rs.getInt(1) > 0;
    } catch (Exception e) {
        System.out.println("Erreur vérif emprunts : " + e.getMessage());
    }
    return false;
}
    
    public int getNbLecteurs() {
        try {
            String sql = "SELECT COUNT(*) FROM lecteur";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (Exception e) {
            System.out.println("Erreur : " + e.getMessage());
        }
        return 0;
    }

    public int getNbEmpruntsActifs(int idLecteur) {
        try {
            String sql = "SELECT COUNT(*) FROM emprunt WHERE idLecteur = ? AND statut = 'en cours'";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, idLecteur);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (Exception e) {
            System.out.println("Erreur : " + e.getMessage());
        }
        return 0;
    }

    public String getAdresse(int idLecteur) {
        try {
            String sql = "SELECT adresse FROM lecteur WHERE idLecteur = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, idLecteur);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getString("adresse") != null
                        ? rs.getString("adresse") : "";
            }
        } catch (Exception e) {
            System.out.println("Erreur : " + e.getMessage());
        }
        return "";
    }

    public String getDateInscription(int idLecteur) {
        try {
            String sql = "SELECT dateInscription FROM lecteur WHERE idLecteur = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, idLecteur);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getString("dateInscription") != null
                        ? rs.getString("dateInscription") : "—";
            }
        } catch (Exception e) {
            System.out.println("Erreur : " + e.getMessage());
        }
        return "—";
    }

    public boolean lecteurExisteDeja(String nom, String prenom, String telephone) {
        try {
            String sql = "SELECT COUNT(*) FROM lecteur WHERE "
                    + "UPPER(nom) = ? AND LOWER(prenom) = ? AND telephone = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, nom.toUpperCase());
            ps.setString(2, prenom.toLowerCase());
            ps.setString(3, telephone);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (Exception e) {
            System.out.println("Erreur doublon lecteur : " + e.getMessage());
        }
        return false;
    }

    public int getNbInscriptionsAujourdhui() {
        try {
            String sql = "SELECT COUNT(*) FROM lecteur "
                    + "WHERE DATE(dateInscription) = CURDATE()";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (Exception e) {
            System.out.println("Erreur : " + e.getMessage());
        }
        return 0;
    }
    // Téléphone unique (pour AjouterLecteur)
public boolean telephoneExisteDeja(String telephone) {
    try {
        String sql = "SELECT COUNT(*) FROM lecteur WHERE telephone = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, telephone.trim());
        ResultSet rs = ps.executeQuery();
        if (rs.next()) return rs.getInt(1) > 0;
    } catch (Exception e) {
        System.out.println("Erreur vérif téléphone : " + e.getMessage());
    }
    return false;
}

// Téléphone unique en excluant le lecteur courant (pour ModifierLecteur)
public boolean telephoneExisteDeja(String telephone, int idExclure) {
    try {
        String sql = "SELECT COUNT(*) FROM lecteur "
                + "WHERE telephone = ? AND idLecteur != ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, telephone.trim());
        ps.setInt(2, idExclure);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) return rs.getInt(1) > 0;
    } catch (Exception e) {
        System.out.println("Erreur vérif téléphone modif : " + e.getMessage());
    }
    return false;
}
}

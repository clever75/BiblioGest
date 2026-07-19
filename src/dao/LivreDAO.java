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
import models.Journal;
import models.Livre;

/**
 *
 * @author Admin
 */
public class LivreDAO {

    private Connection conn;

    public LivreDAO() {
        try {
            conn = Connexion.getConnexion();
        } catch (Exception e) {
            System.out.println("Erreur : " + e.getMessage());
        }
    }

    //Ajouter un livre
    public boolean ajouter(Livre livre) {
        try {
            String sql = "INSERT INTO livre(titre,auteur,categorie,quantite) VALUES(?,?,?,?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, livre.getTitre());
            ps.setString(2, livre.getAuteur());
            ps.setString(3, livre.getCategorie());
            ps.setInt(4, livre.getQuantite());
            ps.executeUpdate();
            Journal.ajouter(models.Journal.TYPE_LIVRE,
                    "Livre ajouté : " + livre.getTitre());
            return true;
        } catch (Exception e) {
            System.out.println("Erreur ajout : " + e.getMessage());
            return false;
        }
    }
public boolean estEmprunte(int idLivre) {
    try {
        String sql = "SELECT COUNT(*) FROM emprunt "
            + "WHERE idLivre = ? AND statut IN ('en cours', 'retard')";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, idLivre);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) return rs.getInt(1) > 0;
    } catch (Exception e) {
        System.out.println("Erreur vérif emprunt livre : " + e.getMessage());
    }
    return false;
}
    public int getTotalExemplaires() {
        try {
            String sql = "SELECT SUM(quantite) FROM livre";
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

    //Récupérer tous les livres 
    public ArrayList<Livre> getTousLesLivres() {
        ArrayList<Livre> livres = new ArrayList<>();
        try {
            String sql = "SELECT l.*, "
                    + "(l.quantite - COUNT(e.idEmprunt)) AS dispo "
                    + "FROM livre l "
                    + "LEFT JOIN emprunt e ON l.idLivre = e.idLivre "
                    + "AND e.statut = 'en cours' "
                    + "GROUP BY l.idLivre "
                    + "ORDER BY l.idLivre DESC";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int total = rs.getInt("quantite");
                int dispo = rs.getInt("dispo");
                Livre livre = new Livre(
                        rs.getInt("idLivre"),
                        rs.getString("titre"),
                        rs.getString("auteur"),
                        rs.getString("categorie"),
                        total,
                        dispo
                );
                livres.add(livre);
            }
        } catch (Exception e) {
            System.out.println("Erreur lecture : " + e.getMessage());
        }
        return livres;
    }

    //Modifier un livre
    public boolean modifier(Livre livre) {
        try {
            String sql = "UPDATE livre SET titre=?,auteur=?,categorie=?,quantite=? WHERE idLivre=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, livre.getTitre());
            ps.setString(2, livre.getAuteur());
            ps.setString(3, livre.getCategorie());
            ps.setInt(4, livre.getQuantite());

            ps.setInt(5, livre.getIdLivre());
            ps.executeUpdate();
            models.Journal.ajouter(models.Journal.TYPE_LIVRE,
                    "Livre ajouté : " + livre.getTitre());
            return true;
        } catch (Exception e) {
            System.out.println("Erreur modification : " + e.getMessage());
            return false;
        }
    }

    //Supprimer un livre
    public boolean supprimer(int idLivre) {
        try {
            String sql = "DELETE FROM livre WHERE idLivre=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, idLivre);
            ps.executeUpdate();
            return true;
        } catch (Exception e) {
            System.out.println("Erreur suppression : " + e.getMessage());
            return false;
        }
    }

    //Rechercher un livre
    public ArrayList<Livre> rechercher(String texte) {
        ArrayList<Livre> livres = new ArrayList<>();
        try {
            String sql = "SELECT l.*, "
                    + "(l.quantite - COUNT(e.idEmprunt)) AS dispo "
                    + "FROM livre l "
                    + "LEFT JOIN emprunt e ON l.idLivre = e.idLivre "
                    + "AND e.statut = 'en cours' "
                    + "WHERE l.titre LIKE ? OR l.auteur LIKE ? "
                    + "GROUP BY l.idLivre";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, "%" + texte + "%");
            ps.setString(2, "%" + texte + "%");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int total = rs.getInt("quantite");
                int dispo = rs.getInt("dispo");
                Livre livre = new Livre(
                        rs.getInt("idLivre"),
                        rs.getString("titre"),
                        rs.getString("auteur"),
                        rs.getString("categorie"),
                        total,
                        dispo
                );
                livres.add(livre);
            }
        } catch (Exception e) {
            System.out.println("Erreur recherche : " + e.getMessage());
        }
        return livres;
    }

    public int getLivresDisponibles() {
        int count = 0;
        try {
            String sql = "SELECT SUM(quantite - emprunts_en_cours) FROM ("
                    + "SELECT l.quantite, COUNT(e.idEmprunt) AS emprunts_en_cours "
                    + "FROM livre l "
                    + "LEFT JOIN emprunt e ON l.idLivre = e.idLivre "
                    + "AND e.statut = 'en cours' "
                    + "GROUP BY l.idLivre"
                    + ") AS sous_requete";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                count = rs.getInt(1);
            }
        } catch (Exception e) {
            System.out.println("Erreur : " + e.getMessage());
            e.printStackTrace();
        }
        return count;
    }

    public int[] getDispoEtTotal(int idLivre) {
        int[] result = {0, 0};
        try {
            String sql = "SELECT l.quantite, "
                    + "(l.quantite - COUNT(e.idEmprunt)) AS dispo "
                    + "FROM livre l "
                    + "LEFT JOIN emprunt e ON l.idLivre = e.idLivre "
                    + "AND e.statut = 'en cours' "
                    + "WHERE l.idLivre = ? "
                    + "GROUP BY l.idLivre";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, idLivre);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                result[1] = rs.getInt("quantite");
                result[0] = rs.getInt("dispo");
            }
        } catch (Exception e) {
            System.out.println("Erreur dispo : " + e.getMessage());
        }
        return result;
    }

    public int getNbTotalLivres() {
        try {
            String sql = "SELECT COUNT(*) FROM livre";
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

    public ArrayList<Livre> rechercherParCategorie(String categorie) {
        ArrayList<Livre> livres = new ArrayList<>();
        try {
            String sql = "SELECT l.*, "
                    + "(l.quantite - COUNT(e.idEmprunt)) AS dispo "
                    + "FROM livre l "
                    + "LEFT JOIN emprunt e ON l.idLivre = e.idLivre "
                    + "AND e.statut = 'en cours' "
                    + "WHERE l.categorie = ? "
                    + "GROUP BY l.idLivre";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, categorie);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                livres.add(new Livre(
                        rs.getInt("idLivre"),
                        rs.getString("titre"),
                        rs.getString("auteur"),
                        rs.getString("categorie"),
                        rs.getInt("quantite"),
                        rs.getInt("dispo")
                ));
            }
        } catch (Exception e) {
            System.out.println("Erreur : " + e.getMessage());
        }
        return livres;
    }

    public boolean livreExisteDeja(String titre, String auteur) {
        try {
            String sql = "SELECT COUNT(*) FROM livre WHERE "
                    + "LOWER(titre) = LOWER(?) AND LOWER(auteur) = LOWER(?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, titre.trim());
            ps.setString(2, auteur.trim());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (Exception e) {
            System.out.println("Erreur vérif doublon : " + e.getMessage());
        }
        return false;
    }

    public ArrayList<String> getToutesLesCategories() {
        ArrayList<String> categories = new ArrayList<>();
        try {
            String sql = "SELECT DISTINCT categorie FROM livre ORDER BY categorie";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String cat = rs.getString("categorie");
                if (cat != null && !cat.trim().isEmpty()) {
                    categories.add(cat);
                }
            }
        } catch (Exception e) {
            System.out.println("Erreur catégories : " + e.getMessage());
        }
        return categories;
    }
}

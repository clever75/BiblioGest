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
import models.Emprunt;

/**
 *
 * @author Admin
 */
public class EmpruntDAO {

    private Connection conn;

    public EmpruntDAO() {
        try {
            conn = Connexion.getConnexion();
        } catch (Exception e) {
            System.out.println("Erreur : " + e.getMessage());
        }
    }

    //Enregister un emprunt
   public boolean emprunter(int idLivre, int idLecteur, 
        String dateEmprunt, String dateRetourPrevue) {
    try {
        String sqlVerif = "SELECT (l.quantite - COUNT(e.idEmprunt)) AS dispo "
                + "FROM livre l LEFT JOIN emprunt e ON l.idLivre = e.idLivre "
                + "AND e.statut = 'en cours' "
                + "WHERE l.idLivre = ? GROUP BY l.idLivre";
        PreparedStatement psVerif = conn.prepareStatement(sqlVerif);
        psVerif.setInt(1, idLivre);
        ResultSet rs = psVerif.executeQuery();
        if (rs.next() && rs.getInt("dispo") <= 0) return false;

        String sql = "INSERT INTO emprunt(idLivre, idLecteur, "
                + "dateEmprunt, dateRetourPrevue, statut) "
                + "VALUES(?, ?, ?, ?, 'en cours')";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, idLivre);
        ps.setInt(2, idLecteur);
        ps.setString(3, dateEmprunt);
        ps.setString(4, dateRetourPrevue);
        ps.executeUpdate();

        // Récupérer le titre pour le journal
        String sqlTitre = "SELECT titre FROM livre WHERE idLivre = ?";
        PreparedStatement psTitre = conn.prepareStatement(sqlTitre);
        psTitre.setInt(1, idLivre);
        ResultSet rsTitre = psTitre.executeQuery();
        String titre = rsTitre.next() ? rsTitre.getString("titre") : "—";

        models.Journal.ajouter(models.Journal.TYPE_EMPRUNT,
            "Emprunt créé : " + titre);

        return true;
    } catch (Exception e) {
        System.out.println("Erreur emprunt : " + e.getMessage());
        return false;
    }
}
   public int getNbEmpruntsAujourdhui() {
    try {
        String sql = "SELECT COUNT(*) FROM emprunt "
            + "WHERE dateEmprunt = CURDATE()";
        PreparedStatement ps = conn.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) return rs.getInt(1);
    } catch (Exception e) {
        System.out.println("Erreur : " + e.getMessage());
    }
    return 0;
}

public int getNbRetoursAujourdhui() {
    try {
        String sql = "SELECT COUNT(*) FROM emprunt "
            + "WHERE dateRetourReelle = CURDATE()";
        PreparedStatement ps = conn.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) return rs.getInt(1);
    } catch (Exception e) {
        System.out.println("Erreur : " + e.getMessage());
    }
    return 0;
}

public double getAmendesTotalesAujourdhui() {
    try {
        String sql = "SELECT COALESCE(SUM(amende), 0) "
            + "FROM emprunt "
            + "WHERE dateRetourReelle = CURDATE() "
            + "AND amende > 0";
        PreparedStatement ps = conn.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) return rs.getDouble(1);
    } catch (Exception e) {
        System.out.println("Erreur : " + e.getMessage());
    }
    return 0;
}
public ArrayList<Emprunt> rechercherParStatutEtPeriode(
        String statut, String dateDebut, String dateFin) {
    ArrayList<Emprunt> emprunts = new ArrayList<>();
    try {
        String sql = "SELECT e.idEmprunt, e.idLivre, e.idLecteur, l.titre, "
                + "lec.nom, lec.prenom, e.dateEmprunt, e.dateRetourPrevue, "
                + "e.dateRetourReelle, e.statut "
                + "FROM emprunt e "
                + "JOIN livre l ON e.idLivre = l.idLivre "
                + "JOIN lecteur lec ON e.idLecteur = lec.idLecteur "
                + "WHERE e.statut = ? "
                + "AND e.dateEmprunt BETWEEN ? AND ? "
                + "ORDER BY e.idEmprunt DESC";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, statut);
        ps.setString(2, dateDebut);
        ps.setString(3, dateFin);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            emprunts.add(new Emprunt(
                    rs.getInt("idEmprunt"),
                    rs.getInt("idLivre"),
                    rs.getInt("idLecteur"),
                    rs.getString("titre"),
                    rs.getString("nom") + " " + rs.getString("prenom"),
                    rs.getString("dateEmprunt"),
                    rs.getString("dateRetourPrevue"),
                    rs.getString("dateRetourReelle"),
                    rs.getString("statut")
            ));
        }
    } catch (Exception e) {
        System.out.println("Erreur : " + e.getMessage());
    }
    return emprunts;
}
public ArrayList<Emprunt> getEmpruntsParPeriode(
        String dateDebut, String dateFin) {
    ArrayList<Emprunt> emprunts = new ArrayList<>();
    try {
        String sql = "SELECT e.idEmprunt, e.idLivre, e.idLecteur, l.titre, "
                + "lec.nom, lec.prenom, e.dateEmprunt, e.dateRetourPrevue, "
                + "e.dateRetourReelle, e.statut "
                + "FROM emprunt e "
                + "JOIN livre l ON e.idLivre = l.idLivre "
                + "JOIN lecteur lec ON e.idLecteur = lec.idLecteur "
                + "WHERE e.dateEmprunt BETWEEN ? AND ? "
                + "ORDER BY e.idEmprunt DESC";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, dateDebut);
        ps.setString(2, dateFin);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            emprunts.add(new Emprunt(
                    rs.getInt("idEmprunt"),
                    rs.getInt("idLivre"),
                    rs.getInt("idLecteur"),
                    rs.getString("titre"),
                    rs.getString("nom") + " " + rs.getString("prenom"),
                    rs.getString("dateEmprunt"),
                    rs.getString("dateRetourPrevue"),
                    rs.getString("dateRetourReelle"),
                    rs.getString("statut")
            ));
        }
    } catch (Exception e) {
        System.out.println("Erreur : " + e.getMessage());
    }
    return emprunts;
}
    //Enregistrer un retour
    public boolean retourner(int idEmprunt, double amende,
        String dateRetourReelle, String etatLivre, String remarque) {
    try {
        // Récupérer le titre avant la mise à jour
        String sqlTitre = "SELECT l.titre FROM livre l "
            + "JOIN emprunt e ON l.idLivre = e.idLivre "
            + "WHERE e.idEmprunt = ?";
        PreparedStatement psTitre = conn.prepareStatement(sqlTitre);
        psTitre.setInt(1, idEmprunt);
        ResultSet rsTitre = psTitre.executeQuery();
        String titre = rsTitre.next() ? rsTitre.getString("titre") : "—";

        String sql = "UPDATE emprunt SET statut='rendu', dateRetourReelle=?, "
                + "amende=?, etatLivre=?, remarque=? WHERE idEmprunt=?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, dateRetourReelle);
        ps.setDouble(2, amende);
        ps.setString(3, etatLivre);
        ps.setString(4, remarque);
        ps.setInt(5, idEmprunt);
        ps.executeUpdate();

        // Remettre le livre disponible
        String sql2 = "UPDATE livre SET disponible=1 WHERE idLivre = "
                + "(SELECT idLivre FROM emprunt WHERE idEmprunt=?)";
        PreparedStatement ps2 = conn.prepareStatement(sql2);
        ps2.setInt(1, idEmprunt);
        ps2.executeUpdate();

        models.Journal.ajouter(models.Journal.TYPE_RETOUR,
            "Retour enregistré : " + titre);

        return true;
    } catch (Exception e) {
        System.out.println("Erreur retour : " + e.getMessage());
        return false;
    }
}

    //Récupérer tous les emprunts en cours
    public ArrayList<Emprunt> getTousLesEmprunts() {
        ArrayList<Emprunt> emprunts = new ArrayList<>();
        try {
            String sql = "SELECT e.idEmprunt, e.idLivre, e.idLecteur, l.titre, "
                    + "lec.nom, lec.prenom, e.dateEmprunt, e.dateRetourPrevue, "
                    + "e.dateRetourReelle, e.statut "
                    + "FROM emprunt e "
                    + "JOIN livre l ON e.idLivre = l.idLivre "
                    + "JOIN lecteur lec ON e.idLecteur = lec.idLecteur "
                    + "ORDER BY e.idEmprunt DESC";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                // Vérifier si en retard
                java.time.LocalDate dateRetour = java.time.LocalDate.parse(rs.getString("dateRetourPrevue"));
                java.time.LocalDate aujourdhui = java.time.LocalDate.now();
                String statut = rs.getString("statut");

                if (!statut.equals("rendu") && aujourdhui.isAfter(dateRetour)) {
                    statut = "retard";
                    String sqlUpdate = "UPDATE emprunt SET statut = 'retard' WHERE idEmprunt = ?";
                    PreparedStatement psUpdate = conn.prepareStatement(sqlUpdate);
                    psUpdate.setInt(1, rs.getInt("idEmprunt"));
                    psUpdate.executeUpdate();
                }

                Emprunt emprunt = new Emprunt(
                        rs.getInt("idEmprunt"),
                        rs.getInt("idLivre"),
                        rs.getInt("idLecteur"),
                        rs.getString("titre"),
                        rs.getString("nom") + " " + rs.getString("prenom"),
                        rs.getString("dateEmprunt"),
                        rs.getString("dateRetourPrevue"),
                        rs.getString("dateRetourReelle"),
                        statut // ← statut calculé
                );
                emprunts.add(emprunt);
            }
        } catch (Exception e) {
            System.out.println("Erreur lecture : " + e.getMessage());
        }
        return emprunts;
    }

    //Récupérer les emprunts récents
    public ArrayList<Emprunt> getEmpruntsRecents(int limite) {
        ArrayList<Emprunt> emprunts = new ArrayList<>();
        try {
            String sql = "SELECT e.idEmprunt, e.idLivre, e.idLecteur, l.titre, "
                    + "lec.nom, lec.prenom, e.dateEmprunt, e.dateRetourPrevue, "
                    + "e.dateRetourReelle, e.statut "
                    + "FROM emprunt e "
                    + "JOIN livre l ON e.idLivre = l.idLivre "
                    + "JOIN lecteur lec ON e.idLecteur = lec.idLecteur "
                    + "ORDER BY e.idEmprunt DESC LIMIT ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, limite);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                java.time.LocalDate dateRetour = java.time.LocalDate.parse(rs.getString("dateRetourPrevue"));
                java.time.LocalDate aujourdhui = java.time.LocalDate.now();
                String statut = rs.getString("statut");
                if (!statut.equals("rendu") && aujourdhui.isAfter(dateRetour)) {
                    statut = "retard";
                }
                emprunts.add(new Emprunt(
                        rs.getInt("idEmprunt"),
                        rs.getInt("idLivre"),
                        rs.getInt("idLecteur"),
                        rs.getString("titre"),
                        rs.getString("nom") + " " + rs.getString("prenom"),
                        rs.getString("dateEmprunt"),
                        rs.getString("dateRetourPrevue"),
                        rs.getString("dateRetourReelle"),
                        statut
                ));
            }
        } catch (Exception e) {
            System.out.println("Erreur : " + e.getMessage());
        }
        return emprunts;
    }

    //Récupérer les livres disponibles
    public ArrayList<Object[]> getLivresDisponibles() {
        ArrayList<Object[]> livres = new ArrayList<>();
        try {
            // Calculer dispo dynamiquement
            String sql = "SELECT l.idLivre, l.titre, "
                    + "(l.quantite - COUNT(e.idEmprunt)) AS dispo "
                    + "FROM livre l "
                    + "LEFT JOIN emprunt e ON l.idLivre = e.idLivre "
                    + "AND e.statut = 'en cours' "
                    + "GROUP BY l.idLivre "
                    + "HAVING dispo > 0"; // ← seulement les livres avec exemplaires disponibles
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Object[] ligne = {rs.getInt("idLivre"), rs.getString("titre")};
                livres.add(ligne);
            }
        } catch (Exception e) {
            System.out.println("Erreur : " + e.getMessage());
        }
        return livres;
    }

    //Emprunts actifs
    public int getNbEmpruntsActifs() {
        try {
            String sql = "SELECT COUNT(*) FROM emprunt WHERE statut = 'en cours'";
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

    //Emprunts en retards
    public int getNbRetards() {
        try {
            String sql = "SELECT COUNT(*) FROM emprunt WHERE statut = 'retard'";
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
    public ArrayList<models.Emprunt> getHistoriqueParLecteur(int idLecteur) {
    ArrayList<models.Emprunt> liste = new ArrayList<>();
    String sql = "SELECT e.*, l.titre FROM emprunt e " +
                 "JOIN livre l ON e.idLivre = l.idLivre " +
                 "WHERE e.idLecteur = ? " +
                 "ORDER BY e.dateEmprunt DESC";
    try (java.sql.PreparedStatement ps = conn.prepareStatement(sql)) {
        ps.setInt(1, idLecteur);
        java.sql.ResultSet rs = ps.executeQuery();
        while (rs.next()) {
    Emprunt emp = new Emprunt(
        rs.getInt("idEmprunt"),
        rs.getInt("idLivre"),
        rs.getInt("idLecteur"),
        rs.getString("titre"),
        "",  // nomLecteur (pas dans le SELECT e.*)
        rs.getString("dateEmprunt"),
        rs.getString("dateRetourPrevue"),
        rs.getString("dateRetourReelle"),
        rs.getString("statut")
    );
    liste.add(emp);
}
    } catch (Exception e) {
        e.printStackTrace();
    }
    return liste;
}
    public int getNbEmpruntsActifs(int idLecteur) {
    String sql = "SELECT COUNT(*) FROM emprunt " +
             "WHERE idLecteur = ? AND statut IN ('en cours', 'retard')";
    try (java.sql.PreparedStatement ps = 
                 conn.prepareStatement(sql)) {
        ps.setInt(1, idLecteur);
        java.sql.ResultSet rs = ps.executeQuery();
        if (rs.next()) return rs.getInt(1);
    } catch (Exception e) {
        e.printStackTrace();
    }
    return 0;
}

    //Recherche d'emprunt par statut
    public ArrayList<Emprunt> rechercherParStatut(String statut) {
        ArrayList<Emprunt> emprunts = new ArrayList<>();
        try {
            String sql = "SELECT e.idEmprunt, e.idLivre, e.idLecteur, l.titre, "
                    + "lec.nom, lec.prenom, e.dateEmprunt, e.dateRetourPrevue, "
                    + "e.dateRetourReelle, e.statut "
                    + "FROM emprunt e "
                    + "JOIN livre l ON e.idLivre = l.idLivre "
                    + "JOIN lecteur lec ON e.idLecteur = lec.idLecteur "
                    + "WHERE e.statut = ? "
                    + "ORDER BY e.idEmprunt DESC";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, statut);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                emprunts.add(new Emprunt(
                        rs.getInt("idEmprunt"),
                        rs.getInt("idLivre"),
                        rs.getInt("idLecteur"),
                        rs.getString("titre"),
                        rs.getString("nom") + " " + rs.getString("prenom"),
                        rs.getString("dateEmprunt"),
                        rs.getString("dateRetourPrevue"),
                        rs.getString("dateRetourReelle"),
                        rs.getString("statut")
                ));
            }
        } catch (Exception e) {
            System.out.println("Erreur : " + e.getMessage());
        }
        return emprunts;
    }
    //Récupérer les lecteurs

    public ArrayList<Object[]> getLecteurs() {
        ArrayList<Object[]> lecteurs = new ArrayList<>();
        try {
            String sql = "SELECT idLecteur, nom,prenom FROM lecteur";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Object[] ligne = {
                    rs.getInt("idLecteur"),
                    rs.getString("nom") + " " + rs.getString("prenom")
                };
                lecteurs.add(ligne);
            }
        } catch (Exception e) {
            System.out.println("Erreur : " + e.getMessage());
        }
        return lecteurs;
    }

    public int[] getStatistiquesParPeriode(String dateDebut, String dateFin) {
        int[] stats = {0, 0, 0, 0};
        try {
            String sql = "SELECT "
                    + "COUNT(*) AS total, "
                    + "SUM(CASE WHEN statut='rendu' THEN 1 ELSE 0 END) AS rendus, "
                    + "SUM(CASE WHEN statut='retard' THEN 1 ELSE 0 END) AS retards, "
                    + "SUM(CASE WHEN statut='en cours' THEN 1 ELSE 0 END) AS en_cours "
                    + "FROM emprunt WHERE dateEmprunt BETWEEN ? AND ?";
            java.sql.PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, dateDebut);
            ps.setString(2, dateFin);
            java.sql.ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                stats[0] = rs.getInt("total");
                stats[1] = rs.getInt("rendus");
                stats[2] = rs.getInt("retards");
                stats[3] = rs.getInt("en_cours");
            }
        } catch (Exception e) {
            System.out.println("Erreur stats : " + e.getMessage());
        }
        return stats;
    }

    public ArrayList<Object[]> getTopLivres(String dateDebut, String dateFin) {
        ArrayList<Object[]> top = new ArrayList<>();
        try {
            String sql = "SELECT l.titre, COUNT(e.idEmprunt) AS nb "
                    + "FROM emprunt e JOIN livre l ON e.idLivre = l.idLivre "
                    + "WHERE e.dateEmprunt BETWEEN ? AND ? "
                    + "GROUP BY l.idLivre ORDER BY nb DESC LIMIT 5";
            java.sql.PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, dateDebut);
            ps.setString(2, dateFin);
            java.sql.ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                top.add(new Object[]{rs.getString("titre"), rs.getInt("nb")});
            }
        } catch (Exception e) {
            System.out.println("Erreur top livres : " + e.getMessage());
        }
        return top;
    }

    public ArrayList<Object[]> getTopLecteurs(String dateDebut, String dateFin) {
    ArrayList<Object[]> liste = new ArrayList<>();
    try {
        String sql = "SELECT CONCAT(l.nom, ' ', l.prenom) AS lecteur, "
                + "COUNT(e.idEmprunt) AS nb "
                + "FROM emprunt e "
                + "JOIN lecteur l ON e.idLecteur = l.idLecteur "
                + "WHERE e.dateEmprunt BETWEEN ? AND ? "
                + "GROUP BY e.idLecteur "
                + "ORDER BY nb DESC LIMIT 5";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, dateDebut);
        ps.setString(2, dateFin);
        ResultSet rs = ps.executeQuery();

        // Debug temporaire
        System.out.println("Top lecteurs entre " + dateDebut + " et " + dateFin);
        int count = 0;
        while (rs.next()) {
            count++;
            System.out.println(count + ". " + rs.getString("lecteur") 
                    + " → " + rs.getInt("nb"));
            liste.add(new Object[]{
                rs.getString("lecteur"),
                rs.getInt("nb")
            });
        }
        System.out.println("Total trouvés : " + count);

    } catch (Exception e) {
        System.out.println("Erreur top lecteurs : " + e.getMessage());
        e.printStackTrace();
    }
    return liste;
}
    public boolean lecteurADejaLivre(int idLecteur, int idLivre) {
    try {
        String sql = "SELECT COUNT(*) FROM emprunt "
                + "WHERE idLecteur = ? AND idLivre = ? "
                + "AND statut IN ('en cours', 'retard')";
        java.sql.PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, idLecteur);
        ps.setInt(2, idLivre);
        java.sql.ResultSet rs = ps.executeQuery();
        if (rs.next()) return rs.getInt(1) > 0;
    } catch (Exception e) {
        System.out.println("Erreur vérif doublon emprunt : " + e.getMessage());
    }
    return false;
}
}

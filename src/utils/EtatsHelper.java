package utils;

import dao.EmpruntDAO;
import dao.LecteurDAO;
import dao.LivreDAO;
import models.Emprunt;
import models.Lecteur;
import models.Livre;
import java.util.ArrayList;

/**
 * Génère les états imprimables de BiblioGest.
 * Chaque méthode construit un HTML et l'ouvre dans le navigateur.
 * Usage : EtatsHelper.etatListeLivres();
 */
public class EtatsHelper {

    // ── Couleurs de l'app ──────────────────────────────────────────────────────
    private static final String COULEUR_FOND    = "#1a2035";
    private static final String COULEUR_OR      = "#d4a843";
    private static final String COULEUR_VERT    = "#3bad72";
    private static final String COULEUR_ROUGE   = "#e05252";
    private static final String COULEUR_TEXTE   = "#1a2035";
    private static final String COULEUR_GRIS    = "#9aa0b0";

    // ── CSS commun à tous les états ────────────────────────────────────────────
    private static String cssCommun() {
        return "<style>"
            + "* { box-sizing: border-box; margin: 0; padding: 0; }"
            + "body { font-family: 'Segoe UI', Arial, sans-serif; "
            + "       background: #f5f0e8; color: " + COULEUR_TEXTE + "; }"
            + ".page { max-width: 1000px; margin: 0 auto; padding: 24px; }"

            // En-tête
            + ".header { background: " + COULEUR_FOND + "; color: white; "
            + "          padding: 20px 28px; border-radius: 8px 8px 0 0; "
            + "          display: flex; justify-content: space-between; "
            + "          align-items: center; }"
            + ".header-left h1 { font-size: 22px; font-weight: 700; }"
            + ".header-left .sous-titre { font-size: 13px; "
            + "                           color: rgba(255,255,255,0.6); "
            + "                           margin-top: 4px; }"
            + ".header-right { text-align: right; font-size: 12px; "
            + "                color: rgba(255,255,255,0.7); }"
            + ".badge-app { background: " + COULEUR_OR + "; color: " + COULEUR_FOND + "; "
            + "             font-weight: 700; font-size: 13px; "
            + "             padding: 4px 12px; border-radius: 4px; "
            + "             margin-bottom: 8px; display: inline-block; }"

            // Barre dorée sous l'en-tête
            + ".barre-or { background: " + COULEUR_OR + "; height: 4px; }"

            // Corps
            + ".corps { background: white; padding: 24px 28px; "
            + "         border-radius: 0 0 8px 8px; "
            + "         box-shadow: 0 2px 8px rgba(0,0,0,0.08); }"

            // Stats KPI
            + ".kpi-row { display: flex; gap: 12px; margin-bottom: 24px; }"
            + ".kpi { flex: 1; background: #f5f0e8; border-radius: 6px; "
            + "       padding: 12px 16px; text-align: center; }"
            + ".kpi .val { font-size: 28px; font-weight: 700; color: " + COULEUR_FOND + "; }"
            + ".kpi .lab { font-size: 11px; color: " + COULEUR_GRIS + "; "
            + "            text-transform: uppercase; margin-top: 4px; }"

            // Tableau
            + "table { width: 100%; border-collapse: collapse; margin-top: 8px; }"
            + "thead tr { background: " + COULEUR_FOND + "; color: white; }"
            + "thead th { padding: 10px 12px; text-align: left; "
            + "            font-size: 12px; font-weight: 600; "
            + "            text-transform: uppercase; letter-spacing: 0.5px; }"
            + "tbody tr:nth-child(even) { background: #f9f7f4; }"
            + "tbody tr:hover { background: #f0ebe0; }"
            + "tbody td { padding: 9px 12px; font-size: 13px; "
            + "            border-bottom: 1px solid #eee; }"

            // Badges statut
            + ".badge { display: inline-block; padding: 2px 10px; "
            + "         border-radius: 20px; font-size: 11px; font-weight: 600; }"
            + ".badge-vert  { background: #e6faf0; color: #1e8c50; "
            + "               border: 1px solid " + COULEUR_VERT + "; }"
            + ".badge-rouge { background: #fdeaea; color: #b01e1e; "
            + "               border: 1px solid " + COULEUR_ROUGE + "; }"
            + ".badge-or    { background: #fff8e6; color: #b46e0a; "
            + "               border: 1px solid " + COULEUR_OR + "; }"
            + ".badge-gris  { background: #f0f0f0; color: #666; "
            + "               border: 1px solid #ccc; }"

            // Pied de page
            + ".footer { text-align: center; font-size: 11px; "
            + "          color: " + COULEUR_GRIS + "; margin-top: 20px; "
            + "          padding-top: 12px; border-top: 1px solid #eee; }"

            // Impression
            + "@media print {"
            + "  body { background: white; }"
            + "  .page { padding: 0; }"
            + "  .no-print { display: none; }"
            + "  .corps { box-shadow: none; }"
            + "}"
            + "</style>";
    }

    // ── En-tête HTML commune ───────────────────────────────────────────────────
    private static String enteteHTML(String titreEtat, String sousTitre) {
        String date = new java.text.SimpleDateFormat("dd/MM/yyyy à HH:mm")
                .format(new java.util.Date());
        return "<!DOCTYPE html><html lang='fr'><head>"
            + "<meta charset='UTF-8'>"
            + "<title>BiblioGest — " + titreEtat + "</title>"
            + cssCommun()
            + "</head><body><div class='page'>"
            + "<div class='header'>"
            + "  <div class='header-left'>"
            + "    <div class='badge-app'>📚 BiblioGest</div>"
            + "    <h1>" + titreEtat + "</h1>"
            + "    <div class='sous-titre'>" + sousTitre + "</div>"
            + "  </div>"
            + "  <div class='header-right'>"
            + "    Imprimé le<br><strong>" + date + "</strong>"
            + "  </div>"
            + "</div>"
            + "<div class='barre-or'></div>"
            + "<div class='corps'>";
    }

    private static String piedHTML() {
        return "</div>" // ferme .corps
            + "<div class='footer'>BiblioGest — Système de gestion de bibliothèque"
            + " &nbsp;|&nbsp; Document généré automatiquement</div>"
            + "</div></body></html>";
    }

    // ── Méthode commune d'ouverture dans le navigateur ─────────────────────────
    private static void ouvrir(String html, String nomFichier) {
        try {
            java.io.File f = java.io.File.createTempFile(nomFichier, ".html");
            java.io.FileWriter fw = new java.io.FileWriter(f, java.nio.charset.StandardCharsets.UTF_8);
            fw.write(html);
            fw.close();
            java.awt.Desktop.getDesktop().browse(f.toURI());
        } catch (Exception e) {
            javax.swing.JOptionPane.showMessageDialog(null,
                "Erreur lors de la génération de l'état :\n" + e.getMessage(),
                "Erreur", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    // ══════════════════════════════════════════════════════════════════════════
    // ÉTAT 1 — Liste des livres
    // ══════════════════════════════════════════════════════════════════════════
    public static void etatListeLivres() {
        LivreDAO dao = new LivreDAO();
        ArrayList<Livre> livres = dao.getTousLesLivres();

        int totalLivres = livres.size();
        int totalExemplaires = livres.stream().mapToInt(Livre::getQuantite).sum();
        int totalDispo = livres.stream().mapToInt(Livre::getNbDisponibles).sum();
        int totalEmpruntes = totalExemplaires - totalDispo;

        StringBuilder html = new StringBuilder();
        html.append(enteteHTML("Liste des Livres",
            totalLivres + " titre(s) enregistré(s)"));

        // KPIs
        html.append("<div class='kpi-row'>")
            .append(kpi(String.valueOf(totalLivres),    "Titres"))
            .append(kpi(String.valueOf(totalExemplaires),"Exemplaires"))
            .append(kpi(String.valueOf(totalDispo),      "Disponibles"))
            .append(kpi(String.valueOf(totalEmpruntes),  "Empruntés"))
            .append("</div>");

        // Tableau
        html.append("<table><thead><tr>")
            .append("<th>#</th><th>Titre</th><th>Auteur</th>")
            .append("<th>Catégorie</th><th>Total</th><th>Disponibles</th>")
            .append("</tr></thead><tbody>");

        int i = 1;
        for (Livre l : livres) {
            String badgeDispo = l.getNbDisponibles()> 0
                ? "<span class='badge badge-vert'>" + l.getNbDisponibles()+ " dispo</span>"
                : "<span class='badge badge-rouge'>Indisponible</span>";
            html.append("<tr>")
                .append("<td>").append(i++).append("</td>")
                .append("<td><strong>").append(esc(l.getTitre())).append("</strong></td>")
                .append("<td>").append(esc(l.getAuteur())).append("</td>")
                .append("<td><span class='badge badge-gris'>")
                .append(esc(l.getCategorie())).append("</span></td>")
                .append("<td>").append(l.getQuantite()).append("</td>")
                .append("<td>").append(badgeDispo).append("</td>")
                .append("</tr>");
        }

        if (livres.isEmpty()) {
            html.append("<tr><td colspan='6' style='text-align:center;color:#999;padding:20px'>")
                .append("Aucun livre enregistré.</td></tr>");
        }

        html.append("</tbody></table>");
        html.append(piedHTML());
        ouvrir(html.toString(), "bibliogest_livres");
    }

    // ══════════════════════════════════════════════════════════════════════════
    // ÉTAT 2 — Liste des lecteurs
    // ══════════════════════════════════════════════════════════════════════════
    public static void etatListeLecteurs() {
        LecteurDAO dao = new LecteurDAO();
        ArrayList<Lecteur> lecteurs = dao.getTousLesLecteurs();

        StringBuilder html = new StringBuilder();
        html.append(enteteHTML("Liste des Lecteurs",
            lecteurs.size() + " membre(s) enregistré(s)"));

        html.append(kpiRow(
            kpi(String.valueOf(lecteurs.size()), "Membres"),
            kpi(String.valueOf(dao.getNbInscriptionsAujourdhui()), "Inscrits aujourd'hui"),
            "", ""
        ));

        html.append("<table><thead><tr>")
            .append("<th>#</th><th>Nom</th><th>Prénom</th>")
            .append("<th>Téléphone</th><th>Adresse</th><th>Inscription</th>")
            .append("<th>Emprunts actifs</th>")
            .append("</tr></thead><tbody>");

        int i = 1;
        for (Lecteur l : lecteurs) {
            int nbEmprunts = dao.getNbEmpruntsActifs(l.getIdLecteur());
            String badgeEmp = nbEmprunts > 0
                ? "<span class='badge badge-or'>" + nbEmprunts + " en cours</span>"
                : "<span class='badge badge-gris'>Aucun</span>";
            html.append("<tr>")
                .append("<td>").append(i++).append("</td>")
                .append("<td><strong>").append(esc(l.getNom())).append("</strong></td>")
                .append("<td>").append(esc(l.getPrenom())).append("</td>")
                .append("<td>").append(esc(l.getTelephone())).append("</td>")
                .append("<td>").append(esc(l.getAdresse())).append("</td>")
                .append("<td>").append(formaterDate(l.getDateInscription())).append("</td>")
                .append("<td>").append(badgeEmp).append("</td>")
                .append("</tr>");
        }

        if (lecteurs.isEmpty()) {
            html.append("<tr><td colspan='7' style='text-align:center;color:#999;padding:20px'>")
                .append("Aucun lecteur enregistré.</td></tr>");
        }

        html.append("</tbody></table>");
        html.append(piedHTML());
        ouvrir(html.toString(), "bibliogest_lecteurs");
    }

    // ══════════════════════════════════════════════════════════════════════════
    // ÉTAT 3 — Emprunts en cours
    // ══════════════════════════════════════════════════════════════════════════
    public static void etatEmpruntsEnCours() {
        EmpruntDAO dao = new EmpruntDAO();
        ArrayList<Emprunt> encours  = dao.rechercherParStatut("en cours");
        ArrayList<Emprunt> retards  = dao.rechercherParStatut("retard");

        ArrayList<Emprunt> tous = new ArrayList<>();
        tous.addAll(retards);   // retards en premier
        tous.addAll(encours);

        StringBuilder html = new StringBuilder();
        html.append(enteteHTML("Emprunts en cours",
            tous.size() + " emprunt(s) actif(s) — dont "
            + retards.size() + " en retard"));

        html.append("<div class='kpi-row'>")
            .append(kpi(String.valueOf(tous.size()),    "Total actifs"))
            .append(kpi(String.valueOf(encours.size()), "En cours"))
            .append(kpi("<span style='color:" + COULEUR_ROUGE + "'>"
                        + retards.size() + "</span>",   "En retard"))
            .append("</div>");

        html.append("<table><thead><tr>")
            .append("<th>#</th><th>Livre</th><th>Lecteur</th>")
            .append("<th>Date emprunt</th><th>Retour prévu</th>")
            .append("<th>Retard</th><th>Statut</th>")
            .append("</tr></thead><tbody>");

        int i = 1;
        java.time.LocalDate aujourd = java.time.LocalDate.now();
        for (Emprunt e : tous) {
            java.time.LocalDate prevue = java.time.LocalDate.parse(e.getDateRetourPrevue());
            long joursRetard = java.time.temporal.ChronoUnit.DAYS.between(prevue, aujourd);
            String retardTxt = joursRetard > 0
                ? "<span style='color:" + COULEUR_ROUGE + ";font-weight:700'>"
                  + joursRetard + " jour(s)</span>"
                : "—";
            String badge = e.getStatut().equals("retard")
                ? "<span class='badge badge-rouge'>En retard</span>"
                : "<span class='badge badge-or'>En cours</span>";

            String rowStyle = e.getStatut().equals("retard")
                ? " style='background:#fff5f5'" : "";
            html.append("<tr").append(rowStyle).append(">")
                .append("<td>").append(i++).append("</td>")
                .append("<td><strong>").append(esc(e.getTitre())).append("</strong></td>")
                .append("<td>").append(esc(e.getNomLecteur())).append("</td>")
                .append("<td>").append(formaterDate(e.getDateEmprunt())).append("</td>")
                .append("<td>").append(formaterDate(e.getDateRetourPrevue())).append("</td>")
                .append("<td>").append(retardTxt).append("</td>")
                .append("<td>").append(badge).append("</td>")
                .append("</tr>");
        }

        if (tous.isEmpty()) {
            html.append("<tr><td colspan='7' style='text-align:center;color:#999;padding:20px'>")
                .append("Aucun emprunt en cours.</td></tr>");
        }

        html.append("</tbody></table>");
        html.append(piedHTML());
        ouvrir(html.toString(), "bibliogest_emprunts_encours");
    }

    // ══════════════════════════════════════════════════════════════════════════
    // ÉTAT 4 — Historique complet des emprunts
    // ══════════════════════════════════════════════════════════════════════════
    public static void etatHistoriqueEmprunts() {
        EmpruntDAO dao = new EmpruntDAO();
        ArrayList<Emprunt> tous = dao.getTousLesEmprunts();

        long nbRendus  = tous.stream().filter(e -> "rendu".equals(e.getStatut())).count();
        long nbRetards = tous.stream().filter(e -> "retard".equals(e.getStatut())).count();
        long nbEncours = tous.stream().filter(e -> "en cours".equals(e.getStatut())).count();

        StringBuilder html = new StringBuilder();
        html.append(enteteHTML("Historique des Emprunts",
            tous.size() + " emprunt(s) au total"));

        html.append("<div class='kpi-row'>")
            .append(kpi(String.valueOf(tous.size()),   "Total"))
            .append(kpi(String.valueOf(nbEncours),     "En cours"))
            .append(kpi(String.valueOf(nbRetards),     "En retard"))
            .append(kpi(String.valueOf(nbRendus),      "Rendus"))
            .append("</div>");

        html.append("<table><thead><tr>")
            .append("<th>#</th><th>Livre</th><th>Lecteur</th>")
            .append("<th>Emprunté le</th><th>Retour prévu</th>")
            .append("<th>Retour réel</th><th>Statut</th>")
            .append("</tr></thead><tbody>");

        int i = 1;
        for (Emprunt e : tous) {
            String badge;
            switch (e.getStatut()) {
                case "rendu":   badge = "<span class='badge badge-vert'>Rendu</span>"; break;
                case "retard":  badge = "<span class='badge badge-rouge'>En retard</span>"; break;
                default:        badge = "<span class='badge badge-or'>En cours</span>"; break;
            }
            String retourReel = (e.getDateRetourReelle() != null
                && !e.getDateRetourReelle().isEmpty()
                && !e.getDateRetourReelle().equals("null"))
                ? formaterDate(e.getDateRetourReelle()) : "—";

            html.append("<tr>")
                .append("<td>").append(i++).append("</td>")
                .append("<td><strong>").append(esc(e.getTitre())).append("</strong></td>")
                .append("<td>").append(esc(e.getNomLecteur())).append("</td>")
                .append("<td>").append(formaterDate(e.getDateEmprunt())).append("</td>")
                .append("<td>").append(formaterDate(e.getDateRetourPrevue())).append("</td>")
                .append("<td>").append(retourReel).append("</td>")
                .append("<td>").append(badge).append("</td>")
                .append("</tr>");
        }

        if (tous.isEmpty()) {
            html.append("<tr><td colspan='7' style='text-align:center;color:#999;padding:20px'>")
                .append("Aucun emprunt enregistré.</td></tr>");
        }

        html.append("</tbody></table>");
        html.append(piedHTML());
        ouvrir(html.toString(), "bibliogest_historique");
    }

    // ══════════════════════════════════════════════════════════════════════════
    // ÉTAT 5 — Rapport des retards avec amendes
    // ══════════════════════════════════════════════════════════════════════════
    public static void etatRapportRetards() {
        EmpruntDAO dao = new EmpruntDAO();
        ArrayList<Emprunt> retards = dao.rechercherParStatut("retard");

        java.time.LocalDate aujourd = java.time.LocalDate.now();
        int totalAmende = 0;
        final int TARIF_JOUR = 100;

        // Calculer total amendes
        for (Emprunt e : retards) {
            java.time.LocalDate prevue = java.time.LocalDate.parse(e.getDateRetourPrevue());
            long jours = java.time.temporal.ChronoUnit.DAYS.between(prevue, aujourd);
            if (jours > 0) totalAmende += jours * TARIF_JOUR;
        }

        StringBuilder html = new StringBuilder();
        html.append(enteteHTML("Rapport des Retards",
            retards.size() + " emprunt(s) en retard"));

        html.append("<div class='kpi-row'>")
            .append(kpi("<span style='color:" + COULEUR_ROUGE + "'>"
                        + retards.size() + "</span>", "Livres en retard"))
            .append(kpi(String.format("%,d", totalAmende) + " F",
                        "Amendes potentielles"))
            .append(kpi(TARIF_JOUR + " F/jour", "Tarif retard"))
            .append("</div>");

        if (!retards.isEmpty()) {
            html.append("<div style='background:#fff5f5;border:1px solid #e05252;"
                + "border-radius:6px;padding:12px 16px;margin-bottom:16px;"
                + "font-size:13px;color:#b01e1e;'>"
                + "⚠️ Ces lecteurs doivent être contactés pour récupérer les livres en retard."
                + "</div>");
        }

        html.append("<table><thead><tr>")
            .append("<th>#</th><th>Livre</th><th>Lecteur</th>")
            .append("<th>Emprunté le</th><th>Retour prévu</th>")
            .append("<th>Jours de retard</th><th>Amende calculée</th>")
            .append("</tr></thead><tbody>");

        int i = 1;
        for (Emprunt e : retards) {
            java.time.LocalDate prevue = java.time.LocalDate.parse(e.getDateRetourPrevue());
            long jours = java.time.temporal.ChronoUnit.DAYS.between(prevue, aujourd);
            int amende = (int)(jours * TARIF_JOUR);

            html.append("<tr style='background:#fff5f5'>")
                .append("<td>").append(i++).append("</td>")
                .append("<td><strong>").append(esc(e.getTitre())).append("</strong></td>")
                .append("<td>").append(esc(e.getNomLecteur())).append("</td>")
                .append("<td>").append(formaterDate(e.getDateEmprunt())).append("</td>")
                .append("<td style='color:" + COULEUR_ROUGE + ";font-weight:600'>")
                .append(formaterDate(e.getDateRetourPrevue())).append("</td>")
                .append("<td><span class='badge badge-rouge'>")
                .append(jours).append(" jour(s)</span></td>")
                .append("<td style='font-weight:700;color:" + COULEUR_ROUGE + "'>")
                .append(String.format("%,d", amende)).append(" F CFA</td>")
                .append("</tr>");
        }

        if (retards.isEmpty()) {
            html.append("<tr><td colspan='7' style='text-align:center;"
                + "color:" + COULEUR_VERT + ";padding:20px;font-weight:600'>")
                .append("✓ Aucun retard en ce moment !</td></tr>");
        } else {
            html.append("<tr style='background:#1a2035;color:white;font-weight:700'>")
                .append("<td colspan='6' style='padding:10px 12px;text-align:right'>")
                .append("TOTAL AMENDES POTENTIELLES</td>")
                .append("<td style='padding:10px 12px;color:" + COULEUR_OR + ";font-size:15px'>")
                .append(String.format("%,d", totalAmende)).append(" F CFA</td>")
                .append("</tr>");
        }

        html.append("</tbody></table>");
        html.append(piedHTML());
        ouvrir(html.toString(), "bibliogest_retards");
    }

    // ══════════════════════════════════════════════════════════════════════════
    // Utilitaires privés
    // ══════════════════════════════════════════════════════════════════════════

    private static String kpi(String valeur, String label) {
        return "<div class='kpi'>"
            + "<div class='val'>" + valeur + "</div>"
            + "<div class='lab'>" + label + "</div>"
            + "</div>";
    }

    private static String kpiRow(String... kpis) {
        StringBuilder sb = new StringBuilder("<div class='kpi-row'>");
        for (String k : kpis) {
            if (!k.isEmpty()) sb.append(k);
        }
        sb.append("</div>");
        return sb.toString();
    }

    /** Échappe les caractères HTML dangereux */
    private static String esc(String s) {
        if (s == null) return "—";
        return s.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;");
    }

    /** Convertit yyyy-MM-dd en dd/MM/yyyy */
    private static String formaterDate(String dateStr) {
        if (dateStr == null || dateStr.isEmpty() || dateStr.equals("null")) return "—";
        try {
            java.time.LocalDate d = java.time.LocalDate.parse(dateStr);
            return d.format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        } catch (Exception e) {
            return dateStr;
        }
    }
    // ══════════════════════════════════════════════════════════════
// EXPORT EXCEL — Liste des livres
// ══════════════════════════════════════════════════════════════
public static void exportExcelLivres() {
    LivreDAO dao = new LivreDAO();
    ArrayList<Livre> livres = dao.getTousLesLivres();

    StringBuilder csv = new StringBuilder();
    // BOM UTF-8 pour que Excel affiche bien les accents
    csv.append('\uFEFF');
    csv.append("Titre;Auteur;Catégorie;Total exemplaires;Disponibles;Statut\n");

    for (Livre l : livres) {
        csv.append(esc(l.getTitre())).append(";")
           .append(esc(l.getAuteur())).append(";")
           .append(esc(l.getCategorie())).append(";")
           .append(l.getQuantite()).append(";")
           .append(l.getNbDisponibles()).append(";")
           .append(l.getNbDisponibles() > 0 ? "Disponible" : "Indisponible")
           .append("\n");
    }

    sauvegarderCSV(csv.toString(), "BiblioGest_Livres");
}

// ══════════════════════════════════════════════════════════════
// EXPORT EXCEL — Liste des lecteurs
// ══════════════════════════════════════════════════════════════
public static void exportExcelLecteurs() {
    LecteurDAO dao = new LecteurDAO();
    ArrayList<Lecteur> lecteurs = dao.getTousLesLecteurs();

    StringBuilder csv = new StringBuilder();
    csv.append('\uFEFF');
    csv.append("Nom;Prénom;Téléphone;Adresse;Date inscription;Emprunts actifs\n");

    for (Lecteur l : lecteurs) {
        int nb = dao.getNbEmpruntsActifs(l.getIdLecteur());
        csv.append(esc(l.getNom())).append(";")
           .append(esc(l.getPrenom())).append(";")
           .append(esc(l.getTelephone())).append(";")
           .append(esc(l.getAdresse())).append(";")
           .append(formaterDate(l.getDateInscription())).append(";")
           .append(nb)
           .append("\n");
    }

    sauvegarderCSV(csv.toString(), "BiblioGest_Lecteurs");
}

// ══════════════════════════════════════════════════════════════
// EXPORT EXCEL — Historique emprunts
// ══════════════════════════════════════════════════════════════
public static void exportExcelEmprunts() {
    EmpruntDAO dao = new EmpruntDAO();
    ArrayList<Emprunt> emprunts = dao.getTousLesEmprunts();

    StringBuilder csv = new StringBuilder();
    csv.append('\uFEFF');
    csv.append("Livre;Lecteur;Date emprunt;Date retour prévue;"
             + "Date retour réelle;Statut\n");

    for (Emprunt e : emprunts) {
        String retourReel = (e.getDateRetourReelle() != null
            && !e.getDateRetourReelle().isEmpty()
            && !e.getDateRetourReelle().equals("null"))
            ? formaterDate(e.getDateRetourReelle()) : "Non rendu";

        String statut;
        switch (e.getStatut()) {
            case "rendu":    statut = "Rendu"; break;
            case "retard":   statut = "En retard"; break;
            default:         statut = "En cours";
        }

        csv.append(esc(e.getTitre())).append(";")
           .append(esc(e.getNomLecteur())).append(";")
           .append(formaterDate(e.getDateEmprunt())).append(";")
           .append(formaterDate(e.getDateRetourPrevue())).append(";")
           .append(retourReel).append(";")
           .append(statut)
           .append("\n");
    }

    sauvegarderCSV(csv.toString(), "BiblioGest_Emprunts");
}

// ══════════════════════════════════════════════════════════════
// Méthode commune — ouvre une boîte "Enregistrer sous"
// ══════════════════════════════════════════════════════════════
private static void sauvegarderCSV(String contenu, String nomDefaut) {
    javax.swing.JFileChooser fc = new javax.swing.JFileChooser();
    fc.setDialogTitle("Enregistrer le fichier Excel");
    fc.setSelectedFile(new java.io.File(nomDefaut + "_"
        + new java.text.SimpleDateFormat("yyyyMMdd").format(new java.util.Date())
        + ".csv"));
    fc.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter(
        "Fichier CSV (Excel)", "csv"));

    int rep = fc.showSaveDialog(null);
    if (rep != javax.swing.JFileChooser.APPROVE_OPTION) return;

    java.io.File fichier = fc.getSelectedFile();
    if (!fichier.getName().endsWith(".csv")) {
        fichier = new java.io.File(fichier.getAbsolutePath() + ".csv");
    }

    try {
        java.io.FileWriter fw = new java.io.FileWriter(
            fichier, java.nio.charset.StandardCharsets.UTF_8);
        fw.write(contenu);
        fw.close();

        // Ouvrir directement dans Excel
        java.awt.Desktop.getDesktop().open(fichier);

    } catch (Exception e) {
        javax.swing.JOptionPane.showMessageDialog(null,
            "Erreur lors de l'export : " + e.getMessage(),
            "Erreur", javax.swing.JOptionPane.ERROR_MESSAGE);
    }
}
}
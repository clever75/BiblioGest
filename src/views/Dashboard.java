/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package views;

import dao.EmpruntDAO;
import dao.LecteurDAO;
import dao.LivreDAO;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import models.Emprunt;

/**
 *
 * @author Admin
 */
public class Dashboard extends JPanel {

    /**
     * Creates new form Dasboard
     */
    private javax.swing.JPanel bottomPanel = null;

    public Dashboard() {
        initComponents();
        btnActualiser.addActionListener(e -> chargerDonnees());
        chargerDonnees();
    }

   public void chargerDonnees() {
    dao.LivreDAO livreDAO = new dao.LivreDAO();
    dao.LecteurDAO lecteurDAO = new dao.LecteurDAO();
    dao.EmpruntDAO empruntDAO = new dao.EmpruntDAO();

    // ── KPI cartes ────────────────────────────────────────
    lblTotalLivres.setText(String.valueOf(livreDAO.getNbTotalLivres()));
    lblDisponibles.setText(String.valueOf(livreDAO.getLivresDisponibles()));
    lblLecteurs0.setText(String.valueOf(lecteurDAO.getNbLecteurs()));
    lblEmpruntsActifs.setText(String.valueOf(empruntDAO.getNbEmpruntsActifs()));

    // ── Bannière alerte retards ───────────────────────────
    int retards = empruntDAO.getNbRetards();
    alertePanel.setVisible(retards > 0);
    lblAlerte.setText(retards + " emprunt(s) en retard");

    // ── Panels milieu ─────────────────────────────────────
    remplirRetards(empruntDAO);
    remplirBientotRetour(empruntDAO);

    // ── Bas du dashboard ──────────────────────────────────
    remplirCartesJour(empruntDAO, lecteurDAO);
    remplirJournal();
    remplirSante(livreDAO, empruntDAO);
}    
   private void remplirCartesJour(dao.EmpruntDAO empruntDAO,
        dao.LecteurDAO lecteurDAO) {
    int empruntsAuj = empruntDAO.getNbEmpruntsAujourdhui();
    int retoursAuj  = empruntDAO.getNbRetoursAujourdhui();
    int inscritsAuj = lecteurDAO.getNbInscriptionsAujourdhui();
    double amendes  = empruntDAO.getAmendesTotalesAujourdhui();

    remplirCarte(carteJour1, "Emprunts aujourd'hui",
            String.valueOf(empruntsAuj),
            new java.awt.Color(59, 173, 114),
            new java.awt.Color(230, 249, 239));
    remplirCarte(carteJour2, "Retours aujourd'hui",
            String.valueOf(retoursAuj),
            new java.awt.Color(212, 168, 67),
            new java.awt.Color(255, 248, 230));
    remplirCarte(carteJour3, "Inscriptions aujourd'hui",
            String.valueOf(inscritsAuj),
            new java.awt.Color(56, 132, 220),
            new java.awt.Color(230, 240, 255));
    remplirCarte(carteJour4, "Amendes perçues",
            (int) amendes + " FCFA",
            new java.awt.Color(110, 80, 200),
            new java.awt.Color(238, 235, 255));
}

private void remplirCarte(javax.swing.JPanel carte, String label,
        String valeur, java.awt.Color couleur,
        java.awt.Color bgClair) {
    carte.removeAll();
    carte.setLayout(new java.awt.BorderLayout());
    carte.setBackground(java.awt.Color.WHITE);
    carte.setBorder(javax.swing.BorderFactory.createCompoundBorder(
            javax.swing.BorderFactory.createLineBorder(
                    new java.awt.Color(232, 226, 216)),
            javax.swing.BorderFactory.createEmptyBorder(12, 14, 12, 14)));

    javax.swing.JPanel ico = new javax.swing.JPanel(
            new java.awt.GridBagLayout());
    ico.setBackground(bgClair);
    ico.setPreferredSize(new java.awt.Dimension(40, 40));
    javax.swing.JLabel icoLbl = new javax.swing.JLabel("●");
    icoLbl.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 10));
    icoLbl.setForeground(couleur);
    ico.add(icoLbl);

    javax.swing.JPanel textes = new javax.swing.JPanel();
    textes.setLayout(new javax.swing.BoxLayout(
            textes, javax.swing.BoxLayout.Y_AXIS));
    textes.setBackground(java.awt.Color.WHITE);
    textes.setBorder(javax.swing.BorderFactory
            .createEmptyBorder(0, 10, 0, 0));

    javax.swing.JLabel lblVal = new javax.swing.JLabel(valeur);
    lblVal.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 18));
    lblVal.setForeground(couleur);

    javax.swing.JLabel lblLbl = new javax.swing.JLabel(label);
    lblLbl.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 11));
    lblLbl.setForeground(new java.awt.Color(154, 160, 176));

    textes.add(lblVal);
    textes.add(lblLbl);

    carte.add(ico, java.awt.BorderLayout.WEST);
    carte.add(textes, java.awt.BorderLayout.CENTER);
    carte.revalidate();
    carte.repaint();
}

private void remplirJournal() {
    journalPanel.removeAll();
    journalPanel.setLayout(new java.awt.BorderLayout());
    journalPanel.add(creerPanelJournal(), java.awt.BorderLayout.CENTER);
    journalPanel.revalidate();
    journalPanel.repaint();
}

private void remplirSante(dao.LivreDAO livreDAO,
        dao.EmpruntDAO empruntDAO) {
    int dispos = livreDAO.getLivresDisponibles();
    int total  = livreDAO.getTotalExemplaires();
    int actifs = empruntDAO.getNbEmpruntsActifs();
    int retards = empruntDAO.getNbRetards();

    santePanel.removeAll();
    santePanel.setLayout(new java.awt.BorderLayout());
    santePanel.add(creerPanelSante(total, dispos, actifs, retards),
            java.awt.BorderLayout.CENTER);
    santePanel.revalidate();
    santePanel.repaint();
}
    private javax.swing.JPanel creerCarteJour(String label,
            String valeur, java.awt.Color couleur,
            java.awt.Color bgClair) {

        javax.swing.JPanel carte = new javax.swing.JPanel(
                new java.awt.BorderLayout());
        carte.setBackground(java.awt.Color.WHITE);
        carte.setBorder(javax.swing.BorderFactory.createCompoundBorder(
                javax.swing.BorderFactory.createLineBorder(
                        new java.awt.Color(232, 226, 216)),
                javax.swing.BorderFactory.createEmptyBorder(
                        12, 14, 12, 14)));

        // Icône colorée
        javax.swing.JPanel ico = new javax.swing.JPanel(
                new java.awt.GridBagLayout());
        ico.setBackground(bgClair);
        ico.setPreferredSize(new java.awt.Dimension(40, 40));
        javax.swing.JLabel icoLbl = new javax.swing.JLabel("●");
        icoLbl.setFont(new java.awt.Font(
                "Segoe UI", java.awt.Font.BOLD, 10));
        icoLbl.setForeground(couleur);
        ico.add(icoLbl);

        // Textes
        javax.swing.JPanel textes = new javax.swing.JPanel();
        textes.setLayout(new javax.swing.BoxLayout(
                textes, javax.swing.BoxLayout.Y_AXIS));
        textes.setBackground(java.awt.Color.WHITE);
        textes.setBorder(javax.swing.BorderFactory
                .createEmptyBorder(0, 10, 0, 0));

        javax.swing.JLabel lblVal = new javax.swing.JLabel(valeur);
        lblVal.setFont(new java.awt.Font(
                "Segoe UI", java.awt.Font.BOLD, 18));
        lblVal.setForeground(couleur);

        javax.swing.JLabel lblLbl = new javax.swing.JLabel(label);
        lblLbl.setFont(new java.awt.Font(
                "Segoe UI", java.awt.Font.PLAIN, 11));
        lblLbl.setForeground(new java.awt.Color(154, 160, 176));

        textes.add(lblVal);
        textes.add(lblLbl);

        carte.add(ico, java.awt.BorderLayout.WEST);
        carte.add(textes, java.awt.BorderLayout.CENTER);
        return carte;
    }

    private javax.swing.JPanel creerPanelJournal() {
        javax.swing.JPanel panel = new javax.swing.JPanel(
                new java.awt.BorderLayout());
        panel.setBackground(java.awt.Color.WHITE);
        panel.setBorder(javax.swing.BorderFactory.createLineBorder(
                new java.awt.Color(232, 226, 216)));

        // Titre
        panel.add(creerTitrePanel(
                "Dernières activités",
                new java.awt.Color(26, 32, 53),
                models.Journal.getActions().size() + " action(s)",
                new java.awt.Color(154, 160, 176),
                new java.awt.Color(240, 240, 240)),
                java.awt.BorderLayout.NORTH);

        // Feed
        javax.swing.JPanel feed = new javax.swing.JPanel();
        feed.setLayout(new javax.swing.BoxLayout(
                feed, javax.swing.BoxLayout.Y_AXIS));
        feed.setBackground(java.awt.Color.WHITE);

        java.util.ArrayList<models.Journal.Action> actions
                = models.Journal.getActions();

        if (actions.isEmpty()) {
            javax.swing.JPanel vide = new javax.swing.JPanel(
                    new java.awt.GridBagLayout());
            vide.setBackground(java.awt.Color.WHITE);
            javax.swing.JLabel msg = new javax.swing.JLabel(
                    "Aucune activité pour le moment");
            msg.setFont(new java.awt.Font(
                    "Segoe UI", java.awt.Font.PLAIN, 12));
            msg.setForeground(new java.awt.Color(154, 160, 176));
            vide.add(msg);
            feed.add(vide);
        } else {
            for (int i = 0; i < actions.size(); i++) {
                feed.add(creerLigneAction(actions.get(i), i));
            }
        }

        panel.add(feed, java.awt.BorderLayout.CENTER);
        return panel;
    }

    private javax.swing.JPanel creerPanelSante(int totalLivres,
            int dispos, int actifs, int retards) {

        javax.swing.JPanel panel = new javax.swing.JPanel(
                new java.awt.BorderLayout());
        panel.setBackground(java.awt.Color.WHITE);
        panel.setBorder(javax.swing.BorderFactory.createLineBorder(
                new java.awt.Color(232, 226, 216)));

        panel.add(creerTitrePanel(
                "Santé de la bibliothèque",
                new java.awt.Color(59, 173, 114),
                "", null, null),
                java.awt.BorderLayout.NORTH);

        javax.swing.JPanel corps = new javax.swing.JPanel();
        corps.setLayout(new javax.swing.BoxLayout(
                corps, javax.swing.BoxLayout.Y_AXIS));
        corps.setBackground(java.awt.Color.WHITE);
        corps.setBorder(javax.swing.BorderFactory
                .createEmptyBorder(14, 16, 14, 16));

        int tauxDispo = totalLivres > 0
                ? (dispos * 100 / totalLivres) : 0;
        int tauxActif = totalLivres > 0
                ? (actifs * 100 / totalLivres) : 0;
        int tauxRetard = actifs > 0
                ? (retards * 100 / actifs) : 0;

        corps.add(creerBarre("Taux de disponibilité",
                tauxDispo + "%",
                tauxDispo,
                new java.awt.Color(59, 173, 114),
                new java.awt.Color(230, 249, 239)));
        corps.add(creerEspaceV(10));

        corps.add(creerBarre("Taux d'emprunt actif",
                tauxActif + "%",
                tauxActif,
                new java.awt.Color(212, 168, 67),
                new java.awt.Color(255, 248, 230)));
        corps.add(creerEspaceV(10));

        corps.add(creerBarre("Taux de retard",
                tauxRetard + "%",
                tauxRetard,
                new java.awt.Color(224, 82, 82),
                new java.awt.Color(255, 235, 235)));
        corps.add(creerEspaceV(14));

        // Séparateur
        javax.swing.JSeparator sep = new javax.swing.JSeparator();
        sep.setForeground(new java.awt.Color(232, 226, 216));
        sep.setMaximumSize(new java.awt.Dimension(32767, 1));
        sep.setAlignmentX(0.0f);
        corps.add(sep);
        corps.add(creerEspaceV(12));

        // Stats texte
        corps.add(creerStatLigne(
                "Livres empruntés en ce moment",
                String.valueOf(actifs),
                new java.awt.Color(26, 32, 53)));
        corps.add(creerEspaceV(8));
        corps.add(creerStatLigne(
                "Emprunts en retard",
                String.valueOf(retards),
                new java.awt.Color(224, 82, 82)));

        panel.add(corps, java.awt.BorderLayout.CENTER);
        return panel;
    }

    private javax.swing.JPanel creerTitrePanel(String titre,
            java.awt.Color couleurBarre, String badgeTexte,
            java.awt.Color badgeFg, java.awt.Color badgeBg) {

        javax.swing.JPanel row = new javax.swing.JPanel(
                new java.awt.BorderLayout());
        row.setBackground(java.awt.Color.WHITE);
        row.setPreferredSize(new java.awt.Dimension(0, 46));
        row.setBorder(javax.swing.BorderFactory.createCompoundBorder(
                javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0,
                        new java.awt.Color(232, 226, 216)),
                javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 14)));

        javax.swing.JPanel barre = new javax.swing.JPanel();
        barre.setBackground(couleurBarre);
        barre.setPreferredSize(new java.awt.Dimension(4, 0));

        javax.swing.JLabel lblTitre = new javax.swing.JLabel(
                "  " + titre);
        lblTitre.setFont(new java.awt.Font(
                "Segoe UI", java.awt.Font.BOLD, 13));
        lblTitre.setForeground(new java.awt.Color(26, 32, 53));

        row.add(barre, java.awt.BorderLayout.WEST);
        row.add(lblTitre, java.awt.BorderLayout.CENTER);

        if (badgeTexte != null && !badgeTexte.isEmpty()) {
            javax.swing.JLabel badge
                    = new javax.swing.JLabel(badgeTexte);
            badge.setFont(new java.awt.Font(
                    "Segoe UI", java.awt.Font.BOLD, 11));
            badge.setForeground(badgeFg);
            badge.setOpaque(true);
            badge.setBackground(badgeBg);
            badge.setBorder(javax.swing.BorderFactory
                    .createEmptyBorder(2, 10, 2, 10));
            row.add(badge, java.awt.BorderLayout.EAST);
        }
        return row;
    }

    private javax.swing.JPanel creerBarre(String label,
            String pct, int valeur,
            java.awt.Color couleur, java.awt.Color bgClair) {

        javax.swing.JPanel p = new javax.swing.JPanel(
                new java.awt.BorderLayout());
        p.setBackground(java.awt.Color.WHITE);
        p.setMaximumSize(new java.awt.Dimension(32767, 42));
        p.setAlignmentX(0.0f);

        javax.swing.JPanel top = new javax.swing.JPanel(
                new java.awt.BorderLayout());
        top.setBackground(java.awt.Color.WHITE);

        javax.swing.JLabel lblLabel = new javax.swing.JLabel(label);
        lblLabel.setFont(new java.awt.Font(
                "Segoe UI", java.awt.Font.PLAIN, 12));
        lblLabel.setForeground(new java.awt.Color(90, 96, 112));

        javax.swing.JLabel lblPct = new javax.swing.JLabel(pct);
        lblPct.setFont(new java.awt.Font(
                "Segoe UI", java.awt.Font.BOLD, 12));
        lblPct.setForeground(couleur);

        top.add(lblLabel, java.awt.BorderLayout.WEST);
        top.add(lblPct, java.awt.BorderLayout.EAST);

        // Barre de progression custom
        int clampedVal = Math.min(100, Math.max(0, valeur));
        javax.swing.JPanel trackPanel = new javax.swing.JPanel(
                new java.awt.BorderLayout()) {
            @Override
            protected void paintComponent(java.awt.Graphics g) {
                super.paintComponent(g);
                java.awt.Graphics2D g2
                        = (java.awt.Graphics2D) g.create();
                g2.setRenderingHint(
                        java.awt.RenderingHints.KEY_ANTIALIASING,
                        java.awt.RenderingHints.VALUE_ANTIALIAS_ON);
                // Track
                g2.setColor(bgClair);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 6, 6);
                // Fill
                int fill = (int) (getWidth() * clampedVal / 100.0);
                if (fill > 0) {
                    g2.setColor(couleur);
                    g2.fillRoundRect(0, 0, fill, getHeight(), 6, 6);
                }
                g2.dispose();
            }
        };
        trackPanel.setBackground(java.awt.Color.WHITE);
        trackPanel.setPreferredSize(new java.awt.Dimension(0, 7));
        trackPanel.setMaximumSize(new java.awt.Dimension(32767, 7));

        p.add(top, java.awt.BorderLayout.NORTH);
        p.add(trackPanel, java.awt.BorderLayout.CENTER);
        return p;
    }

    private javax.swing.JPanel creerLigneAction(
            models.Journal.Action action, int index) {

        javax.swing.JPanel ligne = new javax.swing.JPanel(
                new java.awt.BorderLayout());
        ligne.setBackground(index % 2 == 0
                ? java.awt.Color.WHITE
                : new java.awt.Color(250, 249, 247));
        ligne.setBorder(javax.swing.BorderFactory.createCompoundBorder(
                javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0,
                        new java.awt.Color(240, 235, 228)),
                javax.swing.BorderFactory.createEmptyBorder(10, 16, 10, 16)));
        ligne.setMaximumSize(new java.awt.Dimension(32767, 52));

        java.awt.Color couleur;
        switch (action.type) {
            case models.Journal.TYPE_EMPRUNT:
                couleur = new java.awt.Color(59, 173, 114);
                break;
            case models.Journal.TYPE_RETOUR:
                couleur = new java.awt.Color(212, 168, 67);
                break;
            case models.Journal.TYPE_LIVRE:
                couleur = new java.awt.Color(26, 32, 53);
                break;
            case models.Journal.TYPE_LECTEUR:
                couleur = new java.awt.Color(56, 132, 220);
                break;
            default:
                couleur = new java.awt.Color(154, 160, 176);
        }

        // Point coloré dessiné
        final java.awt.Color c = couleur;
        javax.swing.JPanel point = new javax.swing.JPanel() {
            @Override
            protected void paintComponent(java.awt.Graphics g) {
                super.paintComponent(g);
                java.awt.Graphics2D g2
                        = (java.awt.Graphics2D) g.create();
                g2.setRenderingHint(
                        java.awt.RenderingHints.KEY_ANTIALIASING,
                        java.awt.RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(c);
                g2.fillOval(8, 12, 10, 10);
                g2.dispose();
            }
        };
        point.setOpaque(false);
        point.setPreferredSize(new java.awt.Dimension(28, 0));

        javax.swing.JLabel lblMsg
                = new javax.swing.JLabel(action.message);
        lblMsg.setFont(new java.awt.Font(
                "Segoe UI", java.awt.Font.PLAIN, 13));
        lblMsg.setForeground(new java.awt.Color(26, 32, 53));

        javax.swing.JLabel lblHeure
                = new javax.swing.JLabel(action.heure);
        lblHeure.setFont(new java.awt.Font(
                "Segoe UI", java.awt.Font.PLAIN, 11));
        lblHeure.setForeground(new java.awt.Color(154, 160, 176));

        ligne.add(point, java.awt.BorderLayout.WEST);
        ligne.add(lblMsg, java.awt.BorderLayout.CENTER);
        ligne.add(lblHeure, java.awt.BorderLayout.EAST);
        return ligne;
    }

    private javax.swing.JPanel creerStatLigne(String label,
            String valeur, java.awt.Color couleurVal) {
        javax.swing.JPanel p = new javax.swing.JPanel(
                new java.awt.BorderLayout());
        p.setBackground(java.awt.Color.WHITE);
        p.setMaximumSize(new java.awt.Dimension(32767, 22));
        p.setAlignmentX(0.0f);

        javax.swing.JLabel lbl = new javax.swing.JLabel(label);
        lbl.setFont(new java.awt.Font(
                "Segoe UI", java.awt.Font.PLAIN, 12));
        lbl.setForeground(new java.awt.Color(90, 96, 112));

        javax.swing.JLabel val = new javax.swing.JLabel(valeur);
        val.setFont(new java.awt.Font(
                "Segoe UI", java.awt.Font.BOLD, 12));
        val.setForeground(couleurVal);

        p.add(lbl, java.awt.BorderLayout.WEST);
        p.add(val, java.awt.BorderLayout.EAST);
        return p;
    }

    private javax.swing.JPanel creerEspaceV(int hauteur) {
        javax.swing.JPanel esp = new javax.swing.JPanel(); // ← manquait ".JPanel"
        esp.setBackground(java.awt.Color.WHITE);
        esp.setMaximumSize(new java.awt.Dimension(32767, hauteur));
        esp.setPreferredSize(new java.awt.Dimension(0, hauteur));
        esp.setAlignmentX(0.0f);
        return esp;
    }

    private void remplirRetards(EmpruntDAO empruntDAO) {
        donutPanel.removeAll();
        donutPanel.setLayout(new BorderLayout());

        // ── 1. Calculer d'abord ───────────────────────────────────
        ArrayList<Emprunt> emprunts = empruntDAO.getTousLesEmprunts();
        int count = 0;
        for (Emprunt e : emprunts) {
            if (e.getStatut().equals("retard")) {
                count++;
            }
        }

        // ── 2. Titre + badge ─────────────────────────────────────
        JPanel titreRow = new JPanel(new BorderLayout());
        titreRow.setBackground(Color.WHITE);
        titreRow.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0,
                        new Color(232, 226, 216)),
                BorderFactory.createEmptyBorder(0, 0, 0, 0)));
        titreRow.setPreferredSize(new Dimension(0, 44));

        JPanel barreRouge = new JPanel();
        barreRouge.setBackground(new Color(224, 82, 82));
        barreRouge.setPreferredSize(new Dimension(4, 0));

        JLabel titre = new JLabel("  Emprunts en retard");
        titre.setFont(new Font("Segoe UI", Font.BOLD, 13));
        titre.setForeground(new Color(26, 32, 53));

        JLabel badge = new JLabel(String.valueOf(count));
        badge.setFont(new Font("Segoe UI", Font.BOLD, 11));
        badge.setForeground(new Color(224, 82, 82));
        badge.setOpaque(true);
        badge.setBackground(new Color(255, 240, 240));
        badge.setBorder(BorderFactory.createEmptyBorder(2, 8, 2, 8));

        titreRow.add(barreRouge, BorderLayout.WEST);
        titreRow.add(titre, BorderLayout.CENTER);
        titreRow.add(badge, BorderLayout.EAST);
        donutPanel.add(titreRow, BorderLayout.NORTH);

        // ── 3. Contenu ───────────────────────────────────────────
        if (count == 0) {
            JLabel ok = new JLabel(
                    "  ✓ Aucun retard en ce moment", JLabel.LEFT);
            ok.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 12));
            ok.setForeground(new Color(59, 173, 114));
            ok.setOpaque(true);
            ok.setBackground(Color.WHITE);
            donutPanel.add(ok, BorderLayout.CENTER);
        } else {
            String[] colonnes = {"Livre", "Lecteur", "Retard"};
            DefaultTableModel model = new DefaultTableModel(colonnes, 0) {
                @Override
                public boolean isCellEditable(int r, int c) {
                    return false;
                }
            };

            for (Emprunt e : emprunts) {
                if (e.getStatut().equals("retard")) {
                    long diff = new Date().getTime()
                            - parseDate(e.getDateRetourPrevue()).getTime();
                    long jours = diff / (1000 * 60 * 60 * 24);
                    model.addRow(new Object[]{
                        e.getTitre(),
                        e.getNomLecteur(),
                        jours + " jour(s)"
                    });
                }
            }

            JTable table = new JTable(model);
            table.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            table.setRowHeight(36);
            table.setGridColor(new Color(232, 226, 216));
            table.getTableHeader().setFont(
                    new Font("Segoe UI", Font.BOLD, 11));
            table.getTableHeader().setBackground(new Color(255, 240, 240));
            table.getTableHeader().setForeground(new Color(224, 82, 82));
            table.setDefaultEditor(Object.class, null);

            table.getColumnModel().getColumn(2).setCellRenderer(
                    new DefaultTableCellRenderer() {
                @Override
                public Component getTableCellRendererComponent(
                        JTable t, Object v, boolean sel,
                        boolean foc, int r, int c) {
                    super.getTableCellRendererComponent(
                            t, v, sel, foc, r, c);
                    setForeground(new Color(224, 82, 82));
                    setFont(getFont().deriveFont(Font.BOLD));
                    return this;
                }
            });

            JScrollPane scroll = new JScrollPane(table);
            scroll.setBorder(BorderFactory.createEmptyBorder());
            donutPanel.add(scroll, BorderLayout.CENTER);
        }

        donutPanel.revalidate();
        donutPanel.repaint();
    }

    private void remplirBientotRetour(EmpruntDAO empruntDAO) {
        barresPanel.removeAll();
        barresPanel.setLayout(new java.awt.BorderLayout());
        barresPanel.setBackground(java.awt.Color.WHITE);

        // ── Titre ────────────────────────────────────────────────
        javax.swing.JPanel titreRow = new javax.swing.JPanel(
                new java.awt.BorderLayout());
        titreRow.setBackground(java.awt.Color.WHITE);
        titreRow.setPreferredSize(new java.awt.Dimension(0, 46));
        titreRow.setBorder(javax.swing.BorderFactory.createCompoundBorder(
                javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0,
                        new java.awt.Color(232, 226, 216)),
                javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 12)));

        javax.swing.JPanel barreDoree = new javax.swing.JPanel();
        barreDoree.setBackground(new java.awt.Color(212, 168, 67));
        barreDoree.setPreferredSize(new java.awt.Dimension(4, 0));

        javax.swing.JLabel titre = new javax.swing.JLabel(
                "  Retours prévus sous 3 jours");
        titre.setFont(new java.awt.Font("Segoe UI Emoji", java.awt.Font.BOLD, 13));
        titre.setForeground(new java.awt.Color(26, 32, 53));

        // Compter d'abord
        ArrayList<Emprunt> emprunts = empruntDAO.getTousLesEmprunts();
        java.util.Date aujourd = new java.util.Date();
        int count = 0;
        for (Emprunt e : emprunts) {
            if (!e.getStatut().equals("en cours")) {
                continue;
            }
            java.util.Date dr = parseDate(e.getDateRetourPrevue());
            if (dr == null) {
                continue;
            }
            long jours = (dr.getTime() - aujourd.getTime())
                    / (1000 * 60 * 60 * 24);
            if (jours >= 0 && jours <= 3) {
                count++;
            }
        }

        javax.swing.JLabel badge = new javax.swing.JLabel(
                String.valueOf(count));
        badge.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 11));
        badge.setForeground(new java.awt.Color(180, 120, 20));
        badge.setOpaque(true);
        badge.setBackground(new java.awt.Color(255, 248, 230));
        badge.setBorder(javax.swing.BorderFactory.createCompoundBorder(
                javax.swing.BorderFactory.createLineBorder(
                        new java.awt.Color(212, 168, 67)),
                javax.swing.BorderFactory.createEmptyBorder(2, 10, 2, 10)));

        titreRow.add(barreDoree, java.awt.BorderLayout.WEST);
        titreRow.add(titre, java.awt.BorderLayout.CENTER);
        titreRow.add(badge, java.awt.BorderLayout.EAST);
        barresPanel.add(titreRow, java.awt.BorderLayout.NORTH);

        // ── Contenu ───────────────────────────────────────────────
        if (count == 0) {
            javax.swing.JPanel vide = new javax.swing.JPanel(
                    new java.awt.GridBagLayout());
            vide.setBackground(java.awt.Color.WHITE);
            javax.swing.JPanel inner = new javax.swing.JPanel();
            inner.setLayout(new javax.swing.BoxLayout(
                    inner, javax.swing.BoxLayout.Y_AXIS));
            inner.setBackground(java.awt.Color.WHITE);

            javax.swing.JLabel ico = new javax.swing.JLabel("✓");
            ico.setFont(new java.awt.Font("Segoe UI Emoji", java.awt.Font.BOLD, 28));
            ico.setForeground(new java.awt.Color(59, 173, 114));
            ico.setAlignmentX(java.awt.Component.CENTER_ALIGNMENT);

            javax.swing.JLabel msg = new javax.swing.JLabel(
                    "Aucun retour prévu dans 3 jours");
            msg.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 12));
            msg.setForeground(new java.awt.Color(154, 160, 176));
            msg.setAlignmentX(java.awt.Component.CENTER_ALIGNMENT);

            inner.add(ico);
            inner.add(javax.swing.Box.createVerticalStrut(6));
            inner.add(msg);
            vide.add(inner);
            barresPanel.add(vide, java.awt.BorderLayout.CENTER);
        } else {
            String[] colonnes = {"Livre", "Lecteur", "Date retour"};
            javax.swing.table.DefaultTableModel model
                    = new javax.swing.table.DefaultTableModel(colonnes, 0) {
                @Override
                public boolean isCellEditable(int r, int c) {
                    return false;
                }
            };

            for (Emprunt e : emprunts) {
                if (!e.getStatut().equals("en cours")) {
                    continue;
                }
                java.util.Date dr = parseDate(e.getDateRetourPrevue());
                if (dr == null) {
                    continue;
                }
                long jours = (dr.getTime() - aujourd.getTime())
                        / (1000 * 60 * 60 * 24);
                if (jours >= 0 && jours <= 3) {
                    model.addRow(new Object[]{
                        e.getTitre(),
                        e.getNomLecteur(),
                        e.getDateRetourPrevue()
                    });
                }
            }

            javax.swing.JTable table = creerTableauStylise(model);
            table.getTableHeader().setBackground(
                    new java.awt.Color(255, 251, 240));
            table.getTableHeader().setForeground(
                    new java.awt.Color(180, 120, 20));

            // Colonne date en doré + centré
            table.getColumnModel().getColumn(2).setCellRenderer(
                    new javax.swing.table.DefaultTableCellRenderer() {
                @Override
                public java.awt.Component getTableCellRendererComponent(
                        javax.swing.JTable t, Object v, boolean sel,
                        boolean foc, int r, int c) {
                    super.getTableCellRendererComponent(
                            t, v, sel, foc, r, c);
                    setHorizontalAlignment(CENTER);
                    setForeground(new java.awt.Color(180, 120, 20));
                    setFont(getFont().deriveFont(java.awt.Font.BOLD));
                    return this;
                }
            });

            table.getColumnModel().getColumn(0).setPreferredWidth(150);
            table.getColumnModel().getColumn(1).setPreferredWidth(130);
            table.getColumnModel().getColumn(2).setPreferredWidth(90);

            javax.swing.JScrollPane scroll
                    = new javax.swing.JScrollPane(table);
            scroll.setBorder(javax.swing.BorderFactory.createEmptyBorder());
            barresPanel.add(scroll, java.awt.BorderLayout.CENTER);
        }

        barresPanel.revalidate();
        barresPanel.repaint();
    }

    private javax.swing.JTable creerTableauStylise(
            javax.swing.table.DefaultTableModel model) {
        javax.swing.JTable table = new javax.swing.JTable(model);
        table.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 12));
        table.setRowHeight(38);
        table.setGridColor(new java.awt.Color(240, 235, 228));
        table.setShowVerticalLines(false);
        table.setSelectionBackground(new java.awt.Color(250, 246, 238));
        table.setSelectionForeground(new java.awt.Color(26, 32, 53));
        table.setDefaultEditor(Object.class, null);
        table.setIntercellSpacing(new java.awt.Dimension(0, 0));

        table.getTableHeader().setFont(
                new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 11));
        table.getTableHeader().setPreferredSize(
                new java.awt.Dimension(0, 36));
        table.getTableHeader().setBorder(
                javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0,
                        new java.awt.Color(232, 226, 216)));

        // Alternance lignes
        table.setDefaultRenderer(Object.class,
                new javax.swing.table.DefaultTableCellRenderer() {
            @Override
            public java.awt.Component getTableCellRendererComponent(
                    javax.swing.JTable t, Object v, boolean sel,
                    boolean foc, int r, int c) {
                super.getTableCellRendererComponent(
                        t, v, sel, foc, r, c);
                setBorder(javax.swing.BorderFactory
                        .createEmptyBorder(0, 12, 0, 12));
                if (sel) {
                    setBackground(new java.awt.Color(250, 246, 238));
                    setForeground(new java.awt.Color(26, 32, 53));
                } else {
                    setBackground(r % 2 == 0
                            ? java.awt.Color.WHITE
                            : new java.awt.Color(250, 249, 247));
                    setForeground(new java.awt.Color(60, 66, 82));
                }
                return this;
            }
        });
        return table;
    }

    private java.util.Date parseDate(String dateStr) {
        try {
            return new SimpleDateFormat("yyyy-MM-dd").parse(dateStr);
        } catch (Exception ex) {
            return null;
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        dashboardBody = new javax.swing.JPanel();
        alertePanel = new javax.swing.JPanel();
        icoAlerte = new javax.swing.JLabel();
        lblAlerte = new javax.swing.JLabel();
        statsPanel = new javax.swing.JPanel();
        cardLivres = new javax.swing.JPanel();
        pnl = new javax.swing.JPanel();
        lblTitreLivres = new javax.swing.JLabel();
        labelTotalLivres = new javax.swing.JLabel();
        lblTotalLivres = new javax.swing.JLabel();
        cardDisponibles = new javax.swing.JPanel();
        pnl1 = new javax.swing.JPanel();
        icoCardDisponibles = new javax.swing.JLabel();
        lblTitreDisponiles = new javax.swing.JLabel();
        lblDisponibles = new javax.swing.JLabel();
        cardLecteurs = new javax.swing.JPanel();
        pnl2 = new javax.swing.JPanel();
        icoCardlecteurs = new javax.swing.JLabel();
        lblTitreLecteurs = new javax.swing.JLabel();
        lblLecteurs0 = new javax.swing.JLabel();
        cardEmprunts = new javax.swing.JPanel();
        pnl3 = new javax.swing.JPanel();
        icoCardEmprunts = new javax.swing.JLabel();
        lblTitreEmprunts = new javax.swing.JLabel();
        lblEmpruntsActifs = new javax.swing.JLabel();
        graphiquesPanel = new javax.swing.JPanel();
        donutPanel = new javax.swing.JPanel();
        barresPanel = new javax.swing.JPanel();
        jourPanel = new javax.swing.JPanel();
        carteJour1 = new javax.swing.JPanel();
        carteJour2 = new javax.swing.JPanel();
        carteJour3 = new javax.swing.JPanel();
        carteJour4 = new javax.swing.JPanel();
        ligneBasePanel = new javax.swing.JPanel();
        journalPanel = new javax.swing.JPanel();
        santePanel = new javax.swing.JPanel();
        headerPanel = new javax.swing.JPanel();
        lblPageTitle = new javax.swing.JLabel();
        headerRight = new javax.swing.JPanel();
        btnActualiser = new javax.swing.JButton();

        setBackground(new java.awt.Color(245, 240, 232));
        setLayout(new java.awt.BorderLayout());

        dashboardBody.setBackground(new java.awt.Color(245, 240, 232));
        dashboardBody.setBorder(javax.swing.BorderFactory.createEmptyBorder(24, 28, 24, 28));
        dashboardBody.setLayout(new javax.swing.BoxLayout(dashboardBody, javax.swing.BoxLayout.Y_AXIS));

        alertePanel.setBackground(new java.awt.Color(255, 240, 240));
        alertePanel.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(224, 180, 180)), javax.swing.BorderFactory.createEmptyBorder(0, 16, 0, 16)));
        alertePanel.setMaximumSize(new java.awt.Dimension(32767, 44));
        alertePanel.setPreferredSize(new java.awt.Dimension(0, 44));
        alertePanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 12, 0));

        icoAlerte.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/warning.png"))); // NOI18N
        alertePanel.add(icoAlerte);

        lblAlerte.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblAlerte.setForeground(new java.awt.Color(224, 82, 82));
        lblAlerte.setText("2 emprunts en retard");
        alertePanel.add(lblAlerte);

        dashboardBody.add(alertePanel);

        statsPanel.setBackground(new java.awt.Color(245, 240, 232));
        statsPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 16, 0));
        statsPanel.setMaximumSize(new java.awt.Dimension(32767, 110));
        statsPanel.setPreferredSize(new java.awt.Dimension(0, 110));
        statsPanel.setLayout(new java.awt.GridLayout(1, 4, 16, 0));

        cardLivres.setBackground(new java.awt.Color(255, 255, 255));
        cardLivres.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(232, 226, 216)), javax.swing.BorderFactory.createEmptyBorder(16, 20, 16, 20)));
        cardLivres.setLayout(new java.awt.BorderLayout());

        pnl.setBackground(new java.awt.Color(255, 255, 255));
        pnl.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 8, 0));

        lblTitreLivres.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/stack-of-books.png"))); // NOI18N
        pnl.add(lblTitreLivres);

        labelTotalLivres.setBackground(new java.awt.Color(90, 96, 112));
        labelTotalLivres.setText("Total livres");
        pnl.add(labelTotalLivres);

        cardLivres.add(pnl, java.awt.BorderLayout.NORTH);

        lblTotalLivres.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        lblTotalLivres.setText("6");
        lblTotalLivres.setBorder(javax.swing.BorderFactory.createEmptyBorder(8, 0, 0, 0));
        cardLivres.add(lblTotalLivres, java.awt.BorderLayout.CENTER);

        statsPanel.add(cardLivres);

        cardDisponibles.setBackground(new java.awt.Color(255, 255, 255));
        cardDisponibles.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(232, 226, 216)), javax.swing.BorderFactory.createEmptyBorder(16, 20, 16, 20)));
        cardDisponibles.setLayout(new java.awt.BorderLayout());

        pnl1.setBackground(new java.awt.Color(255, 255, 255));
        pnl1.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 8, 0));

        icoCardDisponibles.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/check.png"))); // NOI18N
        pnl1.add(icoCardDisponibles);

        lblTitreDisponiles.setForeground(new java.awt.Color(90, 96, 112));
        lblTitreDisponiles.setText("Disponibles");
        pnl1.add(lblTitreDisponiles);

        cardDisponibles.add(pnl1, java.awt.BorderLayout.NORTH);

        lblDisponibles.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        lblDisponibles.setForeground(new java.awt.Color(51, 153, 0));
        lblDisponibles.setText("8");
        lblDisponibles.setBorder(javax.swing.BorderFactory.createEmptyBorder(8, 0, 0, 0));
        cardDisponibles.add(lblDisponibles, java.awt.BorderLayout.CENTER);

        statsPanel.add(cardDisponibles);

        cardLecteurs.setBackground(new java.awt.Color(255, 255, 255));
        cardLecteurs.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(232, 226, 216)), javax.swing.BorderFactory.createEmptyBorder(16, 20, 16, 20)));
        cardLecteurs.setLayout(new java.awt.BorderLayout());

        pnl2.setBackground(new java.awt.Color(255, 255, 255));
        pnl2.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 8, 0));

        icoCardlecteurs.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/user2.png"))); // NOI18N
        pnl2.add(icoCardlecteurs);

        lblTitreLecteurs.setForeground(new java.awt.Color(90, 96, 112));
        lblTitreLecteurs.setText("Lecteurs");
        pnl2.add(lblTitreLecteurs);

        cardLecteurs.add(pnl2, java.awt.BorderLayout.NORTH);

        lblLecteurs0.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        lblLecteurs0.setForeground(new java.awt.Color(255, 204, 153));
        lblLecteurs0.setText("5");
        lblLecteurs0.setBorder(javax.swing.BorderFactory.createEmptyBorder(8, 0, 0, 0));
        cardLecteurs.add(lblLecteurs0, java.awt.BorderLayout.CENTER);

        statsPanel.add(cardLecteurs);

        cardEmprunts.setBackground(new java.awt.Color(255, 255, 255));
        cardEmprunts.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(232, 226, 216)), javax.swing.BorderFactory.createEmptyBorder(16, 20, 16, 20)));
        cardEmprunts.setLayout(new java.awt.BorderLayout());

        pnl3.setBackground(new java.awt.Color(255, 255, 255));
        pnl3.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 8, 0));

        icoCardEmprunts.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/return.png"))); // NOI18N
        pnl3.add(icoCardEmprunts);

        lblTitreEmprunts.setForeground(new java.awt.Color(90, 96, 112));
        lblTitreEmprunts.setText("Emprunts actifs");
        pnl3.add(lblTitreEmprunts);

        cardEmprunts.add(pnl3, java.awt.BorderLayout.NORTH);

        lblEmpruntsActifs.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        lblEmpruntsActifs.setForeground(new java.awt.Color(255, 204, 102));
        lblEmpruntsActifs.setText("2");
        lblEmpruntsActifs.setBorder(javax.swing.BorderFactory.createEmptyBorder(8, 0, 0, 0));
        cardEmprunts.add(lblEmpruntsActifs, java.awt.BorderLayout.CENTER);

        statsPanel.add(cardEmprunts);

        dashboardBody.add(statsPanel);

        graphiquesPanel.setBackground(new java.awt.Color(245, 240, 232));
        graphiquesPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 16, 0));
        graphiquesPanel.setMaximumSize(new java.awt.Dimension(32767, 260));
        graphiquesPanel.setPreferredSize(new java.awt.Dimension(0, 260));
        graphiquesPanel.setLayout(new java.awt.GridLayout(1, 2, 16, 0));

        donutPanel.setBackground(new java.awt.Color(255, 255, 255));
        donutPanel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(232, 226, 216)));
        donutPanel.setLayout(new java.awt.BorderLayout());
        graphiquesPanel.add(donutPanel);

        barresPanel.setBackground(new java.awt.Color(255, 255, 255));
        barresPanel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(232, 226, 216)));
        barresPanel.setLayout(new java.awt.BorderLayout());
        graphiquesPanel.add(barresPanel);

        dashboardBody.add(graphiquesPanel);

        jourPanel.setBackground(new java.awt.Color(245, 240, 232));
        jourPanel.setMaximumSize(new java.awt.Dimension(32767, 76));
        jourPanel.setPreferredSize(new java.awt.Dimension(0, 76));
        jourPanel.setLayout(new java.awt.GridLayout(1, 4, 12, 0));

        carteJour1.setBackground(new java.awt.Color(255, 255, 255));
        carteJour1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(245, 240, 232)));
        carteJour1.setMaximumSize(new java.awt.Dimension(32767, 14));
        jourPanel.add(carteJour1);

        carteJour2.setBackground(new java.awt.Color(255, 255, 255));
        carteJour2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(245, 240, 232)));
        carteJour2.setMaximumSize(new java.awt.Dimension(32767, 14));
        jourPanel.add(carteJour2);

        carteJour3.setBackground(new java.awt.Color(255, 255, 255));
        carteJour3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(245, 240, 232)));
        carteJour3.setMaximumSize(new java.awt.Dimension(32767, 14));
        jourPanel.add(carteJour3);

        carteJour4.setBackground(new java.awt.Color(255, 255, 255));
        carteJour4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(245, 240, 232)));
        carteJour4.setMaximumSize(new java.awt.Dimension(32767, 14));
        jourPanel.add(carteJour4);

        dashboardBody.add(jourPanel);

        ligneBasePanel.setBackground(new java.awt.Color(245, 240, 232));
        ligneBasePanel.setMaximumSize(new java.awt.Dimension(32767, 260));
        ligneBasePanel.setPreferredSize(new java.awt.Dimension(0, 260));
        ligneBasePanel.setLayout(new java.awt.GridLayout(1, 2, 12, 0));

        journalPanel.setBackground(new java.awt.Color(255, 255, 255));
        journalPanel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(232, 226, 216)));
        ligneBasePanel.add(journalPanel);

        santePanel.setBackground(new java.awt.Color(255, 255, 255));
        santePanel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(232, 226, 216)));
        ligneBasePanel.add(santePanel);

        dashboardBody.add(ligneBasePanel);

        add(dashboardBody, java.awt.BorderLayout.CENTER);

        headerPanel.setBackground(new java.awt.Color(245, 240, 232));
        headerPanel.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(232, 226, 216)));
        headerPanel.setPreferredSize(new java.awt.Dimension(0, 70));
        headerPanel.setVerifyInputWhenFocusTarget(false);
        headerPanel.setLayout(new java.awt.BorderLayout());

        lblPageTitle.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        lblPageTitle.setForeground(new java.awt.Color(26, 32, 53));
        lblPageTitle.setText("Tableau de bord");
        lblPageTitle.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 30, 28, 0));
        headerPanel.add(lblPageTitle, java.awt.BorderLayout.WEST);

        headerRight.setBackground(new java.awt.Color(245, 240, 232));
        headerRight.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT, 28, 18));

        btnActualiser.setBackground(new java.awt.Color(212, 168, 67));
        btnActualiser.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnActualiser.setForeground(new java.awt.Color(26, 32, 53));
        btnActualiser.setText("Actualiser");
        btnActualiser.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnActualiser.setFocusPainted(false);
        btnActualiser.setPreferredSize(new java.awt.Dimension(110, 40));
        headerRight.add(btnActualiser);

        headerPanel.add(headerRight, java.awt.BorderLayout.CENTER);

        add(headerPanel, java.awt.BorderLayout.NORTH);
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel alertePanel;
    private javax.swing.JPanel barresPanel;
    private javax.swing.JButton btnActualiser;
    private javax.swing.JPanel cardDisponibles;
    private javax.swing.JPanel cardEmprunts;
    private javax.swing.JPanel cardLecteurs;
    private javax.swing.JPanel cardLivres;
    private javax.swing.JPanel carteJour1;
    private javax.swing.JPanel carteJour2;
    private javax.swing.JPanel carteJour3;
    private javax.swing.JPanel carteJour4;
    private javax.swing.JPanel dashboardBody;
    private javax.swing.JPanel donutPanel;
    private javax.swing.JPanel graphiquesPanel;
    private javax.swing.JPanel headerPanel;
    private javax.swing.JPanel headerRight;
    private javax.swing.JLabel icoAlerte;
    private javax.swing.JLabel icoCardDisponibles;
    private javax.swing.JLabel icoCardEmprunts;
    private javax.swing.JLabel icoCardlecteurs;
    private javax.swing.JPanel jourPanel;
    private javax.swing.JPanel journalPanel;
    private javax.swing.JLabel labelTotalLivres;
    private javax.swing.JLabel lblAlerte;
    private javax.swing.JLabel lblDisponibles;
    private javax.swing.JLabel lblEmpruntsActifs;
    private javax.swing.JLabel lblLecteurs0;
    private javax.swing.JLabel lblPageTitle;
    private javax.swing.JLabel lblTitreDisponiles;
    private javax.swing.JLabel lblTitreEmprunts;
    private javax.swing.JLabel lblTitreLecteurs;
    private javax.swing.JLabel lblTitreLivres;
    private javax.swing.JLabel lblTotalLivres;
    private javax.swing.JPanel ligneBasePanel;
    private javax.swing.JPanel pnl;
    private javax.swing.JPanel pnl1;
    private javax.swing.JPanel pnl2;
    private javax.swing.JPanel pnl3;
    private javax.swing.JPanel santePanel;
    private javax.swing.JPanel statsPanel;
    // End of variables declaration//GEN-END:variables
}

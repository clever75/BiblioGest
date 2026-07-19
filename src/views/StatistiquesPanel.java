/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package views;

import java.util.ArrayList;

/**
 *
 * @author Admin
 */
public class StatistiquesPanel extends javax.swing.JPanel {

    /**
     * Creates new form StatistiquesPanel
     */
    private dao.EmpruntDAO empruntDAO = new dao.EmpruntDAO();
    private javax.swing.JTable topLecteursTable;
    private javax.swing.JScrollPane topLecteursScrollPane;
    private javax.swing.JTable detailTable;
private javax.swing.JPanel detailPanel;
private javax.swing.JLabel lblDetailTitre;

  public StatistiquesPanel() {
    initComponents();
    initStyles();
    initTopLecteurs();
    initDetailPanel();
    initDates();
    initCartesCliquables();
    initBoutons();
    chargerStats();
}

  private void initStyles() {
    btnAppliquer.putClientProperty("JButton.buttonType", "roundRect");
    btnImprimer.putClientProperty("JButton.buttonType", "roundRect");

    topTable.setDefaultEditor(Object.class, null);
    topTable.setShowVerticalLines(false);
    topTable.getTableHeader().setFont(
            new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 12));
    topTable.getTableHeader().setBackground(
            new java.awt.Color(240, 235, 224));
    topTable.getTableHeader().setForeground(
            new java.awt.Color(90, 96, 112));

    topTable.getColumnModel().getColumn(0).setCellRenderer(
            new javax.swing.table.DefaultTableCellRenderer() {
        @Override
        public java.awt.Component getTableCellRendererComponent(
                javax.swing.JTable t, Object v, boolean sel,
                boolean foc, int r, int c) {
            super.getTableCellRendererComponent(t, v, sel, foc, r, c);
            setHorizontalAlignment(CENTER);
            if (v != null && !v.toString().equals("—")) {
                try {
                    int rang = Integer.parseInt(v.toString());
                    if (rang == 1) {
                        setBackground(new java.awt.Color(212, 168, 67));
                        setForeground(new java.awt.Color(26, 32, 53));
                        setFont(getFont().deriveFont(java.awt.Font.BOLD));
                    } else if (rang == 2) {
                        setBackground(new java.awt.Color(232, 226, 216));
                        setForeground(new java.awt.Color(90, 96, 112));
                        setFont(getFont().deriveFont(java.awt.Font.PLAIN));
                    } else {
                        setBackground(new java.awt.Color(245, 240, 232));
                        setForeground(new java.awt.Color(90, 96, 112));
                        setFont(getFont().deriveFont(java.awt.Font.PLAIN));
                    }
                } catch (NumberFormatException ex) {
                    setBackground(java.awt.Color.WHITE);
                    setForeground(new java.awt.Color(90, 96, 112));
                }
            } else {
                setBackground(java.awt.Color.WHITE);
                setForeground(new java.awt.Color(90, 96, 112));
            }
if (sel) {
    setBackground(new java.awt.Color(212, 168, 67));
    setForeground(new java.awt.Color(26, 32, 53));
}            return this;
        }
    });

    javax.swing.table.DefaultTableCellRenderer centerRenderer
            = new javax.swing.table.DefaultTableCellRenderer();
    centerRenderer.setHorizontalAlignment(javax.swing.JLabel.CENTER);
    topTable.getColumnModel().getColumn(2).setCellRenderer(centerRenderer);
}

private void initDates() {
    java.util.Calendar cal = java.util.Calendar.getInstance();
    dateFin.setDate(cal.getTime());
    cal.set(java.util.Calendar.DAY_OF_MONTH, 1);
    dateDebut.setDate(cal.getTime());
}

private void initBoutons() {
    btnAppliquer.addActionListener(e -> chargerStats());
    btnImprimer.addActionListener(e -> imprimerRapport());
}

private void initCartesCliquables() {
    carte1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    carte2.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    carte3.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    carte4.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

    carte1.addMouseListener(new java.awt.event.MouseAdapter() {
        @Override
        public void mouseClicked(java.awt.event.MouseEvent e) {
            lblDetailTitre.setText("Détail — Total emprunts");
            String debut = new java.text.SimpleDateFormat("yyyy-MM-dd")
                    .format(dateDebut.getDate());
            String fin = new java.text.SimpleDateFormat("yyyy-MM-dd")
                    .format(dateFin.getDate());
            javax.swing.table.DefaultTableModel model =
                new javax.swing.table.DefaultTableModel(
                    new String[]{"Livre", "Lecteur",
                        "Date emprunt", "Date retour prévue", "Statut"}, 0) {
                @Override
                public boolean isCellEditable(int r, int c) { return false; }
            };
            for (models.Emprunt emp :
                    empruntDAO.getEmpruntsParPeriode(debut, fin)) {
                model.addRow(new Object[]{
                    emp.getTitre(),
                    emp.getNomLecteur(),
                    formaterDate(emp.getDateEmprunt()),
                    formaterDate(emp.getDateRetourPrevue()),
                    emp.getStatut()
                });
            }
            detailTable.setModel(model);
            detailPanel.setVisible(true);
            bodyPanel.revalidate();
            bodyPanel.repaint();
        }
    });

    carte2.addMouseListener(new java.awt.event.MouseAdapter() {
        @Override
        public void mouseClicked(java.awt.event.MouseEvent e) {
            afficherDetail("Rendus", "rendu");
        }
    });

    carte3.addMouseListener(new java.awt.event.MouseAdapter() {
        @Override
        public void mouseClicked(java.awt.event.MouseEvent e) {
            afficherDetail("En retard", "retard");
        }
    });

    carte4.addMouseListener(new java.awt.event.MouseAdapter() {
        @Override
        public void mouseClicked(java.awt.event.MouseEvent e) {
            afficherDetail("En cours", "en cours");
        }
    });
}
    private void initTopLecteurs() {
        // Panneau gauche — Top livres (déjà existant = topPanel)
        // On va juste reorganiser dans un conteneur 2 colonnes

        // Créer le tableau Top lecteurs
        topLecteursTable = new javax.swing.JTable();
        topLecteursTable.setModel(new javax.swing.table.DefaultTableModel(
                new Object[][]{}, new String[]{"#", "Lecteur", "Nb emprunts"}
        ));
        topLecteursTable.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 13));
        topLecteursTable.setRowHeight(40);
        topLecteursTable.setShowVerticalLines(false);
        topLecteursTable.setDefaultEditor(Object.class, null);
        topLecteursTable.setSelectionBackground(new java.awt.Color(250, 246, 238));
        topLecteursTable.getTableHeader().setFont(
                new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 12));
        topLecteursTable.getTableHeader().setBackground(
                new java.awt.Color(240, 235, 224));
        topLecteursTable.getTableHeader().setForeground(
                new java.awt.Color(90, 96, 112));

        // Renderer rang coloré (même style que topTable)
        topLecteursTable.getColumnModel().getColumn(0).setPreferredWidth(50);
        topLecteursTable.getColumnModel().getColumn(1).setPreferredWidth(250);
        topLecteursTable.getColumnModel().getColumn(2).setPreferredWidth(100);
        topLecteursTable.getColumnModel().getColumn(0).setCellRenderer(
                new javax.swing.table.DefaultTableCellRenderer() {
            @Override
            public java.awt.Component getTableCellRendererComponent(
                    javax.swing.JTable t, Object v, boolean sel,
                    boolean foc, int r, int c) {
                super.getTableCellRendererComponent(t, v, sel, foc, r, c);
                setHorizontalAlignment(CENTER);
                try {
                    int rang = Integer.parseInt(v.toString());
                    if (rang == 1) {
                        setBackground(new java.awt.Color(212, 168, 67));
                        setForeground(new java.awt.Color(26, 32, 53));
                        setFont(getFont().deriveFont(java.awt.Font.BOLD));
                    } else if (rang == 2) {
                        setBackground(new java.awt.Color(232, 226, 216));
                        setForeground(new java.awt.Color(90, 96, 112));
                    } else {
                        setBackground(new java.awt.Color(245, 240, 232));
                        setForeground(new java.awt.Color(90, 96, 112));
                    }
                } catch (Exception ex) {
                    setBackground(java.awt.Color.WHITE);
                }
                if (sel) {
    setBackground(new java.awt.Color(212, 168, 67));
    setForeground(new java.awt.Color(26, 32, 53));
}
                return this;
            }
        });

        // Centrer colonne nb emprunts
        javax.swing.table.DefaultTableCellRenderer center
                = new javax.swing.table.DefaultTableCellRenderer();
        center.setHorizontalAlignment(javax.swing.JLabel.CENTER);
        topLecteursTable.getColumnModel().getColumn(2).setCellRenderer(center);

        topLecteursScrollPane = new javax.swing.JScrollPane(topLecteursTable);

        // Panneau Top lecteurs complet
        javax.swing.JPanel topLecteursPanel = new javax.swing.JPanel(
                new java.awt.BorderLayout());
        topLecteursPanel.setBackground(java.awt.Color.WHITE);
        topLecteursPanel.setBorder(javax.swing.BorderFactory.createLineBorder(
                new java.awt.Color(232, 226, 216)));

        // Titre
        javax.swing.JPanel titreLecteursPanel = new javax.swing.JPanel(
                new java.awt.BorderLayout());
        titreLecteursPanel.setBackground(java.awt.Color.WHITE);
        titreLecteursPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(
                14, 0, 14, 0));
        titreLecteursPanel.setPreferredSize(new java.awt.Dimension(0, 44));

        javax.swing.JPanel bandeOr = new javax.swing.JPanel();
        bandeOr.setBackground(new java.awt.Color(212, 168, 67));
        bandeOr.setPreferredSize(new java.awt.Dimension(4, 0));

        javax.swing.JLabel lblTitreLecteurs = new javax.swing.JLabel(
                "Top 5 lecteurs actifs");
        lblTitreLecteurs.setFont(new java.awt.Font("Segoe UI",
                java.awt.Font.BOLD, 14));
        lblTitreLecteurs.setForeground(new java.awt.Color(26, 32, 53));
        lblTitreLecteurs.setBorder(javax.swing.BorderFactory.createEmptyBorder(
                0, 12, 0, 0));

        titreLecteursPanel.add(bandeOr, java.awt.BorderLayout.WEST);
        titreLecteursPanel.add(lblTitreLecteurs, java.awt.BorderLayout.CENTER);

        topLecteursPanel.add(titreLecteursPanel, java.awt.BorderLayout.NORTH);
        topLecteursPanel.add(topLecteursScrollPane, java.awt.BorderLayout.CENTER);

        // Conteneur 2 colonnes
        javax.swing.JPanel doubleTopPanel = new javax.swing.JPanel(
                new java.awt.GridLayout(1, 2, 16, 0));
        doubleTopPanel.setBackground(new java.awt.Color(245, 240, 232));

        doubleTopPanel.add(topPanel);       // Top livres (existant)
        doubleTopPanel.add(topLecteursPanel); // Top lecteurs (nouveau)

        // Remplacer topPanel dans bodyPanel
        bodyPanel.remove(topPanel);
        bodyPanel.add(doubleTopPanel);
        bodyPanel.revalidate();
        bodyPanel.repaint();
    }

    private void chargerStats() {
        if (dateDebut.getDate() == null || dateFin.getDate() == null) {
            javax.swing.JOptionPane.showMessageDialog(this,
                    "Veuillez sélectionner les deux dates.",
                    "Erreur", javax.swing.JOptionPane.ERROR_MESSAGE);
            return;
        }

        String debut = new java.text.SimpleDateFormat("yyyy-MM-dd")
                .format(dateDebut.getDate());
        String fin = new java.text.SimpleDateFormat("yyyy-MM-dd")
                .format(dateFin.getDate());

        int[] stats = empruntDAO.getStatistiquesParPeriode(debut, fin);

        // Mettre à jour les valeurs
        lblValCarte1.setText(String.valueOf(stats[0]));
        lblValCarte2.setText(String.valueOf(stats[1]));
        lblValCarte3.setText(String.valueOf(stats[2]));
        lblValCarte4.setText(String.valueOf(stats[3]));

        // Top livres
        java.util.ArrayList<Object[]> topLivres
                = empruntDAO.getTopLivres(debut, fin);
        afficherTopLivres(topLivres);
        // Top lecteurs
        java.util.ArrayList<Object[]> topLecteurs
                = empruntDAO.getTopLecteurs(debut, fin);
        afficherTopLecteurs(topLecteurs);
    }
private void afficherTopLecteurs(java.util.ArrayList<Object[]> topLecteurs) {
    if (topLecteursTable == null) return;

    javax.swing.table.DefaultTableModel model =
            (javax.swing.table.DefaultTableModel) topLecteursTable.getModel();
    model.setRowCount(0);

    int rang = 1;
    for (Object[] row : topLecteurs) {
        model.addRow(new Object[]{rang++, row[0], row[1]});
    }

    if (topLecteurs.isEmpty()) {
        model.addRow(new Object[]{"—", "Aucun emprunt sur cette période", "—"});
    }
    topLecteursTable.setDefaultRenderer(Object.class,
    new javax.swing.table.DefaultTableCellRenderer() {
    @Override
    public java.awt.Component getTableCellRendererComponent(
            javax.swing.JTable t, Object v, boolean sel,
            boolean foc, int r, int c) {
        super.getTableCellRendererComponent(t, v, sel, foc, r, c);
        if (sel) {
            setBackground(new java.awt.Color(212, 168, 67));
            setForeground(new java.awt.Color(26, 32, 53));
        } else {
            setBackground(r % 2 == 0
                ? java.awt.Color.WHITE
                : new java.awt.Color(250, 249, 247));
            setForeground(new java.awt.Color(26, 32, 53));
        }
        return this;
    }
});
}
    private void afficherTopLivres(java.util.ArrayList<Object[]> topLivres) {
        javax.swing.table.DefaultTableModel model
                = (javax.swing.table.DefaultTableModel) topTable.getModel();
        model.setRowCount(0);

        int rang = 1;
        for (Object[] row : topLivres) {
            model.addRow(new Object[]{rang++, row[0], row[1]});
        }

        if (topLivres.isEmpty()) {
            model.addRow(new Object[]{"—", "Aucun emprunt sur cette période", "—"});
        }
        topTable.setDefaultRenderer(Object.class,
    new javax.swing.table.DefaultTableCellRenderer() {
    @Override
    public java.awt.Component getTableCellRendererComponent(
            javax.swing.JTable t, Object v, boolean sel,
            boolean foc, int r, int c) {
        super.getTableCellRendererComponent(t, v, sel, foc, r, c);
        if (sel) {
            setBackground(new java.awt.Color(212, 168, 67));
            setForeground(new java.awt.Color(26, 32, 53));
        } else {
            setBackground(r % 2 == 0
                ? java.awt.Color.WHITE
                : new java.awt.Color(250, 249, 247));
            setForeground(new java.awt.Color(26, 32, 53));
        }
        return this;
    }
});
    }
private void initDetailPanel() {
    detailPanel = new javax.swing.JPanel(new java.awt.BorderLayout());
    detailPanel.setBackground(java.awt.Color.WHITE);
    detailPanel.setBorder(javax.swing.BorderFactory.createLineBorder(
            new java.awt.Color(232, 226, 216)));
    detailPanel.setVisible(false); // caché par défaut

    // Titre du panneau détail
    javax.swing.JPanel titreDPanel = new javax.swing.JPanel(
            new java.awt.BorderLayout());
    titreDPanel.setBackground(java.awt.Color.WHITE);
    titreDPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(
            14, 0, 14, 0));
    titreDPanel.setPreferredSize(new java.awt.Dimension(0, 44));

    javax.swing.JPanel bandeDetail = new javax.swing.JPanel();
    bandeDetail.setBackground(new java.awt.Color(212, 168, 67));
    bandeDetail.setPreferredSize(new java.awt.Dimension(4, 0));

    lblDetailTitre = new javax.swing.JLabel("Détail");
    lblDetailTitre.setFont(new java.awt.Font("Segoe UI",
            java.awt.Font.BOLD, 14));
    lblDetailTitre.setForeground(new java.awt.Color(26, 32, 53));
    lblDetailTitre.setBorder(javax.swing.BorderFactory.createEmptyBorder(
            0, 12, 0, 0));

    // Bouton fermer
    javax.swing.JButton btnFermer = new javax.swing.JButton("✕ Fermer");
    btnFermer.setFont(new java.awt.Font("Segoe UI",
            java.awt.Font.PLAIN, 11));
    btnFermer.setForeground(new java.awt.Color(90, 96, 112));
    btnFermer.setBorderPainted(false);
    btnFermer.setContentAreaFilled(false);
    btnFermer.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    btnFermer.addActionListener(e -> {
        detailPanel.setVisible(false);
        bodyPanel.revalidate();
        bodyPanel.repaint();
    });

    titreDPanel.add(bandeDetail, java.awt.BorderLayout.WEST);
    titreDPanel.add(lblDetailTitre, java.awt.BorderLayout.CENTER);
    titreDPanel.add(btnFermer, java.awt.BorderLayout.EAST);

    // Tableau détail
    detailTable = new javax.swing.JTable();
    detailTable.setFont(new java.awt.Font("Segoe UI",
            java.awt.Font.PLAIN, 13));
    detailTable.setRowHeight(38);
    detailTable.setShowVerticalLines(false);
    detailTable.setDefaultEditor(Object.class, null);
    detailTable.setSelectionBackground(
            new java.awt.Color(250, 246, 238));
    detailTable.setDefaultRenderer(Object.class,
    new javax.swing.table.DefaultTableCellRenderer() {
    @Override
    public java.awt.Component getTableCellRendererComponent(
            javax.swing.JTable t, Object v, boolean sel,
            boolean foc, int r, int c) {
        super.getTableCellRendererComponent(t, v, sel, foc, r, c);
        setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 10, 0, 10));
        if (sel) {
            setBackground(new java.awt.Color(212, 168, 67));
            setForeground(new java.awt.Color(26, 32, 53));
        } else {
            setBackground(r % 2 == 0
                ? java.awt.Color.WHITE
                : new java.awt.Color(250, 249, 247));
            setForeground(new java.awt.Color(26, 32, 53));
        }
        return this;
    }
});
    detailTable.getTableHeader().setFont(
            new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 12));
    detailTable.getTableHeader().setBackground(
            new java.awt.Color(240, 235, 224));
    detailTable.getTableHeader().setForeground(
            new java.awt.Color(90, 96, 112));

    javax.swing.JScrollPane detailScroll =
            new javax.swing.JScrollPane(detailTable);
    detailScroll.setPreferredSize(new java.awt.Dimension(0, 200));

    detailPanel.add(titreDPanel, java.awt.BorderLayout.NORTH);
    detailPanel.add(detailScroll, java.awt.BorderLayout.CENTER);

    // Espaceur
    javax.swing.JPanel espaceur = new javax.swing.JPanel();
    espaceur.setBackground(new java.awt.Color(245, 240, 232));
    espaceur.setMaximumSize(new java.awt.Dimension(32767, 16));
    espaceur.setPreferredSize(new java.awt.Dimension(0, 16));

    // Insérer dans bodyPanel avant les tops
    bodyPanel.add(espaceur, bodyPanel.getComponentCount() - 1);
    bodyPanel.add(detailPanel, bodyPanel.getComponentCount() - 1);
    bodyPanel.revalidate();
}

private void afficherDetail(String titre, String statut) {
    lblDetailTitre.setText("Détail — " + titre);

    // Récupérer les dates du filtre
    String debut = new java.text.SimpleDateFormat("yyyy-MM-dd")
            .format(dateDebut.getDate());
    String fin = new java.text.SimpleDateFormat("yyyy-MM-dd")
            .format(dateFin.getDate());

    java.util.ArrayList<models.Emprunt> emprunts =
            empruntDAO.rechercherParStatutEtPeriode(statut, debut, fin);

    javax.swing.table.DefaultTableModel model =
            new javax.swing.table.DefaultTableModel(
                    new String[]{"Livre", "Lecteur",
                        "Date emprunt", "Date retour prévue"}, 0) {
        @Override
        public boolean isCellEditable(int r, int c) { return false; }
    };
for (models.Emprunt e : emprunts) {
    model.addRow(new Object[]{
        e.getTitre(),
        e.getNomLecteur(),
        formaterDate(e.getDateEmprunt()),
        formaterDate(e.getDateRetourPrevue())
    });
}

if (emprunts.isEmpty()) {
    model.addRow(new Object[]{
        "Aucun emprunt dans cette catégorie", "", "", ""});
}

// setModel D'ABORD
detailTable.setModel(model);

// ENSUITE les renderers sur les colonnes
javax.swing.table.DefaultTableCellRenderer dateRenderer =
    new javax.swing.table.DefaultTableCellRenderer() {
    @Override
    public java.awt.Component getTableCellRendererComponent(
            javax.swing.JTable t, Object v, boolean sel,
            boolean foc, int r, int c) {
        if (v != null) {
            try {
                java.time.LocalDate d = java.time.LocalDate.parse(v.toString());
                v = d.format(java.time.format.DateTimeFormatter
                        .ofPattern("dd/MM/yyyy"));
            } catch (Exception ex) {}
        }
        super.getTableCellRendererComponent(t, v, sel, foc, r, c);
        setHorizontalAlignment(CENTER);
        if (sel) {
            setBackground(new java.awt.Color(212, 168, 67));
            setForeground(new java.awt.Color(26, 32, 53));
        } else {
            setBackground(r % 2 == 0
                ? java.awt.Color.WHITE
                : new java.awt.Color(250, 249, 247));
        }
        return this;
    }
};
detailTable.getColumnModel().getColumn(2).setCellRenderer(dateRenderer);
detailTable.getColumnModel().getColumn(3).setCellRenderer(dateRenderer);

detailPanel.setVisible(true);
bodyPanel.revalidate();
bodyPanel.repaint();
    detailPanel.setVisible(true);
    bodyPanel.revalidate();
    bodyPanel.repaint();
}

private String formaterDate(String date) {
    try {
        java.time.LocalDate d = java.time.LocalDate.parse(date);
        return d.format(java.time.format.DateTimeFormatter
                .ofPattern("dd/MM/yyyy"));
    } catch (Exception ex) {
        return date != null ? date : "—";
    }
}
    private void imprimerRapport() {
        if (dateDebut.getDate() == null || dateFin.getDate() == null) {
            javax.swing.JOptionPane.showMessageDialog(this,
                    "Veuillez sélectionner les deux dates.",
                    "Erreur", javax.swing.JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            String debut = new java.text.SimpleDateFormat("yyyy-MM-dd")
                    .format(dateDebut.getDate());
            String fin = new java.text.SimpleDateFormat("yyyy-MM-dd")
                    .format(dateFin.getDate());
            String debutAff = new java.text.SimpleDateFormat("dd/MM/yyyy")
                    .format(dateDebut.getDate());
            String finAff = new java.text.SimpleDateFormat("dd/MM/yyyy")
                    .format(dateFin.getDate());
            String maintenant = new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm")
                    .format(new java.util.Date());

            int[] stats = empruntDAO.getStatistiquesParPeriode(debut, fin);
            java.util.ArrayList<Object[]> topLivres
                    = empruntDAO.getTopLivres(debut, fin);

            // Choisir où sauvegarder
            javax.swing.JFileChooser fc = new javax.swing.JFileChooser();
            fc.setSelectedFile(new java.io.File("rapport_statistiques.pdf"));
            fc.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter(
                    "PDF", "pdf"));
            if (fc.showSaveDialog(this) != javax.swing.JFileChooser.APPROVE_OPTION) {
                return;
            }

            String chemin = fc.getSelectedFile().getAbsolutePath();
            if (!chemin.endsWith(".pdf")) {
                chemin += ".pdf";
            }

            // Document PDF
            com.lowagie.text.Document doc = new com.lowagie.text.Document(
                    com.lowagie.text.PageSize.A4, 40, 40, 40, 40);
            com.lowagie.text.pdf.PdfWriter writer
                    = com.lowagie.text.pdf.PdfWriter.getInstance(doc,
                            new java.io.FileOutputStream(chemin));
            doc.open();

            com.lowagie.text.pdf.PdfContentByte cb = writer.getDirectContent();

            // ── Polices ──────────────────────────────────────────────
            com.lowagie.text.Font fTitreGrand = new com.lowagie.text.Font(
                    com.lowagie.text.Font.HELVETICA, 20, com.lowagie.text.Font.BOLD,
                    new java.awt.Color(26, 32, 53));
            com.lowagie.text.Font fTitreDoré = new com.lowagie.text.Font(
                    com.lowagie.text.Font.HELVETICA, 20, com.lowagie.text.Font.BOLD,
                    new java.awt.Color(212, 168, 67));
            com.lowagie.text.Font fSousTitre = new com.lowagie.text.Font(
                    com.lowagie.text.Font.HELVETICA, 10, com.lowagie.text.Font.NORMAL,
                    new java.awt.Color(154, 160, 176));
            com.lowagie.text.Font fRapportTitre = new com.lowagie.text.Font(
                    com.lowagie.text.Font.HELVETICA, 14, com.lowagie.text.Font.BOLD,
                    new java.awt.Color(26, 32, 53));
            com.lowagie.text.Font fDate = new com.lowagie.text.Font(
                    com.lowagie.text.Font.HELVETICA, 9, com.lowagie.text.Font.NORMAL,
                    new java.awt.Color(154, 160, 176));
            com.lowagie.text.Font fLabel = new com.lowagie.text.Font(
                    com.lowagie.text.Font.HELVETICA, 9, com.lowagie.text.Font.NORMAL,
                    new java.awt.Color(90, 96, 112));
            com.lowagie.text.Font fValeur = new com.lowagie.text.Font(
                    com.lowagie.text.Font.HELVETICA, 20, com.lowagie.text.Font.BOLD,
                    new java.awt.Color(26, 32, 53));
            com.lowagie.text.Font fValVert = new com.lowagie.text.Font(
                    com.lowagie.text.Font.HELVETICA, 20, com.lowagie.text.Font.BOLD,
                    new java.awt.Color(59, 173, 114));
            com.lowagie.text.Font fValRouge = new com.lowagie.text.Font(
                    com.lowagie.text.Font.HELVETICA, 20, com.lowagie.text.Font.BOLD,
                    new java.awt.Color(224, 82, 82));
            com.lowagie.text.Font fValOrange = new com.lowagie.text.Font(
                    com.lowagie.text.Font.HELVETICA, 20, com.lowagie.text.Font.BOLD,
                    new java.awt.Color(224, 154, 48));
            com.lowagie.text.Font fNormal = new com.lowagie.text.Font(
                    com.lowagie.text.Font.HELVETICA, 10, com.lowagie.text.Font.NORMAL,
                    new java.awt.Color(26, 32, 53));
            com.lowagie.text.Font fBold = new com.lowagie.text.Font(
                    com.lowagie.text.Font.HELVETICA, 10, com.lowagie.text.Font.BOLD,
                    new java.awt.Color(26, 32, 53));
            com.lowagie.text.Font fEntete = new com.lowagie.text.Font(
                    com.lowagie.text.Font.HELVETICA, 10, com.lowagie.text.Font.BOLD,
                    new java.awt.Color(90, 96, 112));
            com.lowagie.text.Font fSection = new com.lowagie.text.Font(
                    com.lowagie.text.Font.HELVETICA, 11, com.lowagie.text.Font.BOLD,
                    new java.awt.Color(26, 32, 53));
            com.lowagie.text.Font fFooter = new com.lowagie.text.Font(
                    com.lowagie.text.Font.HELVETICA, 8, com.lowagie.text.Font.NORMAL,
                    new java.awt.Color(154, 160, 176));

            // ── HEADER ───────────────────────────────────────────────
            com.lowagie.text.pdf.PdfPTable header
                    = new com.lowagie.text.pdf.PdfPTable(2);
            header.setWidthPercentage(100);
            header.setWidths(new float[]{1f, 1f});
            header.setSpacingAfter(6);

            // Logo gauche
            com.lowagie.text.Phrase logoPhrase = new com.lowagie.text.Phrase();
            logoPhrase.add(new com.lowagie.text.Chunk("Biblio", fTitreGrand));
            logoPhrase.add(new com.lowagie.text.Chunk("Gest", fTitreDoré));
            com.lowagie.text.pdf.PdfPCell cellLogo
                    = new com.lowagie.text.pdf.PdfPCell();
            cellLogo.addElement(new com.lowagie.text.Paragraph(logoPhrase));
            cellLogo.addElement(new com.lowagie.text.Paragraph(
                    "Gestion de bibliotheque", fSousTitre));
            cellLogo.setBorder(0);
            cellLogo.setPaddingBottom(10);

            // Titre rapport droite
            com.lowagie.text.pdf.PdfPCell cellTitre
                    = new com.lowagie.text.pdf.PdfPCell();
            com.lowagie.text.Paragraph pTitre
                    = new com.lowagie.text.Paragraph("Rapport de statistiques", fRapportTitre);
            pTitre.setAlignment(com.lowagie.text.Element.ALIGN_RIGHT);
            com.lowagie.text.Paragraph pDate
                    = new com.lowagie.text.Paragraph("Imprime le : " + maintenant, fDate);
            pDate.setAlignment(com.lowagie.text.Element.ALIGN_RIGHT);
            cellTitre.addElement(pTitre);
            cellTitre.addElement(pDate);
            cellTitre.setBorder(0);
            cellTitre.setPaddingBottom(10);

            header.addCell(cellLogo);
            header.addCell(cellTitre);
            doc.add(header);

            // Ligne dorée séparatrice
            com.lowagie.text.pdf.PdfPTable ligne
                    = new com.lowagie.text.pdf.PdfPTable(1);
            ligne.setWidthPercentage(100);
            ligne.setSpacingAfter(14);
            com.lowagie.text.pdf.PdfPCell ligneCell
                    = new com.lowagie.text.pdf.PdfPCell();
            ligneCell.setBackgroundColor(new java.awt.Color(212, 168, 67));
            ligneCell.setFixedHeight(3);
            ligneCell.setBorder(0);
            ligne.addCell(ligneCell);
            doc.add(ligne);

            // ── PERIODE ───────────────────────────────────────────────
            com.lowagie.text.pdf.PdfPTable periodeTable
                    = new com.lowagie.text.pdf.PdfPTable(1);
            periodeTable.setWidthPercentage(100);
            periodeTable.setSpacingAfter(20);
            com.lowagie.text.pdf.PdfPCell periodeCell
                    = new com.lowagie.text.pdf.PdfPCell(
                            new com.lowagie.text.Phrase(
                                    "Periode analysee : " + debutAff + "  au  " + finAff, fBold));
            periodeCell.setBackgroundColor(new java.awt.Color(245, 240, 232));
            periodeCell.setBorderColor(new java.awt.Color(212, 168, 67));
            periodeCell.setBorderWidthLeft(3);
            periodeCell.setBorderWidthTop(0);
            periodeCell.setBorderWidthRight(0);
            periodeCell.setBorderWidthBottom(0);
            periodeCell.setPadding(10);
            periodeTable.addCell(periodeCell);
            doc.add(periodeTable);

            // ── CARTES STATS ─────────────────────────────────────────
            com.lowagie.text.pdf.PdfPTable cartes
                    = new com.lowagie.text.pdf.PdfPTable(4);
            cartes.setWidthPercentage(100);
            cartes.setSpacingAfter(20);

            int total = stats[0];
            String[][] cartesData = {
                {"Total emprunts", String.valueOf(stats[0]), "1A2035"},
                {"Rendus", String.valueOf(stats[1]), "3BAD72"},
                {"En retard", String.valueOf(stats[2]), "E05252"},
                {"En cours", String.valueOf(stats[3]), "E09A30"}
            };
            com.lowagie.text.Font[] fVals = {fValeur, fValVert, fValRouge, fValOrange};
            java.awt.Color[] couleurs = {
                new java.awt.Color(26, 32, 53),
                new java.awt.Color(59, 173, 114),
                new java.awt.Color(224, 82, 82),
                new java.awt.Color(224, 154, 48)
            };

            for (int i = 0; i < 4; i++) {
                // Bande colorée
                com.lowagie.text.pdf.PdfPTable carteInterne
                        = new com.lowagie.text.pdf.PdfPTable(1);

                com.lowagie.text.pdf.PdfPCell bande
                        = new com.lowagie.text.pdf.PdfPCell();
                bande.setBackgroundColor(couleurs[i]);
                bande.setFixedHeight(4);
                bande.setBorder(0);
                carteInterne.addCell(bande);

                com.lowagie.text.pdf.PdfPCell labelCell
                        = new com.lowagie.text.pdf.PdfPCell(
                                new com.lowagie.text.Phrase(cartesData[i][0], fLabel));
                labelCell.setBorder(0);
                labelCell.setPaddingLeft(10);
                labelCell.setPaddingTop(8);
                labelCell.setPaddingBottom(2);
                carteInterne.addCell(labelCell);

                com.lowagie.text.pdf.PdfPCell valCell
                        = new com.lowagie.text.pdf.PdfPCell(
                                new com.lowagie.text.Phrase(cartesData[i][1], fVals[i]));
                valCell.setBorder(0);
                valCell.setPaddingLeft(10);
                valCell.setPaddingBottom(10);
                carteInterne.addCell(valCell);

                com.lowagie.text.pdf.PdfPCell carte
                        = new com.lowagie.text.pdf.PdfPCell();
                carte.addElement(carteInterne);
                carte.setBorderColor(new java.awt.Color(232, 226, 216));
                carte.setPadding(0);
                cartes.addCell(carte);
            }
            doc.add(cartes);

            // ── DETAIL STATS ─────────────────────────────────────────
            com.lowagie.text.pdf.PdfPTable titreSection1
                    = new com.lowagie.text.pdf.PdfPTable(1);
            titreSection1.setWidthPercentage(100);
            titreSection1.setSpacingAfter(6);
            com.lowagie.text.pdf.PdfPCell ts1
                    = new com.lowagie.text.pdf.PdfPCell(
                            new com.lowagie.text.Phrase("Detail des statistiques", fSection));
            ts1.setBorder(0);
            ts1.setBorderWidthLeft(3);
            ts1.setBorderColorLeft(new java.awt.Color(212, 168, 67));
            ts1.setPaddingLeft(8);
            ts1.setPaddingBottom(4);
            titreSection1.addCell(ts1);
            doc.add(titreSection1);

            com.lowagie.text.pdf.PdfPTable detail
                    = new com.lowagie.text.pdf.PdfPTable(3);
            detail.setWidthPercentage(100);
            detail.setWidths(new float[]{3f, 1f, 1f});
            detail.setSpacingAfter(20);

            // En-têtes
            String[] detailHeaders = {"Indicateur", "Valeur", "Pourcentage"};
            for (String h : detailHeaders) {
                com.lowagie.text.pdf.PdfPCell hCell
                        = new com.lowagie.text.pdf.PdfPCell(
                                new com.lowagie.text.Phrase(h, fEntete));
                hCell.setBackgroundColor(new java.awt.Color(240, 235, 224));
                hCell.setPadding(8);
                detail.addCell(hCell);
            }

            // Lignes
            String[][] detailData = {
                {"Total emprunts", String.valueOf(stats[0]), "100%"},
                {"Rendus", String.valueOf(stats[1]),
                    total > 0 ? (stats[1] * 100 / total) + "%" : "0%"},
                {"En retard", String.valueOf(stats[2]),
                    total > 0 ? (stats[2] * 100 / total) + "%" : "0%"},
                {"En cours", String.valueOf(stats[3]),
                    total > 0 ? (stats[3] * 100 / total) + "%" : "0%"}
            };
            java.awt.Color[] detailCouleurs = {
                new java.awt.Color(26, 32, 53),
                new java.awt.Color(59, 173, 114),
                new java.awt.Color(224, 82, 82),
                new java.awt.Color(224, 154, 48)
            };

            for (int i = 0; i < detailData.length; i++) {
                com.lowagie.text.pdf.PdfPCell c1
                        = new com.lowagie.text.pdf.PdfPCell(
                                new com.lowagie.text.Phrase(detailData[i][0], fNormal));
                c1.setPadding(8);
                c1.setBorderColor(new java.awt.Color(245, 240, 232));

                com.lowagie.text.Font fV = new com.lowagie.text.Font(
                        com.lowagie.text.Font.HELVETICA, 10,
                        com.lowagie.text.Font.BOLD, detailCouleurs[i]);
                com.lowagie.text.pdf.PdfPCell c2
                        = new com.lowagie.text.pdf.PdfPCell(
                                new com.lowagie.text.Phrase(detailData[i][1], fV));
                c2.setPadding(8);
                c2.setBorderColor(new java.awt.Color(245, 240, 232));

                com.lowagie.text.pdf.PdfPCell c3
                        = new com.lowagie.text.pdf.PdfPCell(
                                new com.lowagie.text.Phrase(detailData[i][2], fNormal));
                c3.setPadding(8);
                c3.setBorderColor(new java.awt.Color(245, 240, 232));

                detail.addCell(c1);
                detail.addCell(c2);
                detail.addCell(c3);
            }
            doc.add(detail);

            // ── TOP 5 LIVRES ─────────────────────────────────────────
            com.lowagie.text.pdf.PdfPTable titreSection2
                    = new com.lowagie.text.pdf.PdfPTable(1);
            titreSection2.setWidthPercentage(100);
            titreSection2.setSpacingAfter(6);
            com.lowagie.text.pdf.PdfPCell ts2
                    = new com.lowagie.text.pdf.PdfPCell(
                            new com.lowagie.text.Phrase(
                                    "Top 5 livres les plus empruntes", fSection));
            ts2.setBorder(0);
            ts2.setBorderWidthLeft(3);
            ts2.setBorderColorLeft(new java.awt.Color(212, 168, 67));
            ts2.setPaddingLeft(8);
            ts2.setPaddingBottom(4);
            titreSection2.addCell(ts2);
            doc.add(titreSection2);

            com.lowagie.text.pdf.PdfPTable top
                    = new com.lowagie.text.pdf.PdfPTable(3);
            top.setWidthPercentage(100);
            top.setWidths(new float[]{0.5f, 3f, 1f});
            top.setSpacingAfter(24);

            // En-têtes top
            String[] topHeaders = {"#", "Titre", "Nb emprunts"};
            for (String h : topHeaders) {
                com.lowagie.text.pdf.PdfPCell hCell
                        = new com.lowagie.text.pdf.PdfPCell(
                                new com.lowagie.text.Phrase(h, fEntete));
                hCell.setBackgroundColor(new java.awt.Color(240, 235, 224));
                hCell.setPadding(8);
                hCell.setHorizontalAlignment(
                        com.lowagie.text.Element.ALIGN_CENTER);
                top.addCell(hCell);
            }

            if (topLivres.isEmpty()) {
                com.lowagie.text.pdf.PdfPCell vide
                        = new com.lowagie.text.pdf.PdfPCell(
                                new com.lowagie.text.Phrase(
                                        "Aucun emprunt sur cette periode", fNormal));
                vide.setColspan(3);
                vide.setPadding(10);
                vide.setBorderColor(new java.awt.Color(245, 240, 232));
                top.addCell(vide);
            } else {
                int rang = 1;
                for (Object[] row : topLivres) {
                    // Badge rang
                    java.awt.Color bgRang = rang == 1
                            ? new java.awt.Color(212, 168, 67)
                            : rang == 2
                                    ? new java.awt.Color(232, 226, 216)
                                    : new java.awt.Color(245, 240, 232);
                    com.lowagie.text.pdf.PdfPCell cRang
                            = new com.lowagie.text.pdf.PdfPCell(
                                    new com.lowagie.text.Phrase(
                                            String.valueOf(rang), fBold));
                    cRang.setBackgroundColor(bgRang);
                    cRang.setPadding(8);
                    cRang.setHorizontalAlignment(
                            com.lowagie.text.Element.ALIGN_CENTER);
                    cRang.setBorderColor(new java.awt.Color(245, 240, 232));

                    com.lowagie.text.pdf.PdfPCell cTitre
                            = new com.lowagie.text.pdf.PdfPCell(
                                    new com.lowagie.text.Phrase(
                                            row[0].toString(), fNormal));
                    cTitre.setPadding(8);
                    cTitre.setBorderColor(new java.awt.Color(245, 240, 232));

                    com.lowagie.text.pdf.PdfPCell cNb
                            = new com.lowagie.text.pdf.PdfPCell(
                                    new com.lowagie.text.Phrase(
                                            row[1].toString(), fBold));
                    cNb.setPadding(8);
                    cNb.setHorizontalAlignment(
                            com.lowagie.text.Element.ALIGN_CENTER);
                    cNb.setBorderColor(new java.awt.Color(245, 240, 232));

                    top.addCell(cRang);
                    top.addCell(cTitre);
                    top.addCell(cNb);
                    rang++;
                }
            }
            doc.add(top);

            // ── FOOTER ───────────────────────────────────────────────
            com.lowagie.text.pdf.PdfPTable footer
                    = new com.lowagie.text.pdf.PdfPTable(2);
            footer.setWidthPercentage(100);

            com.lowagie.text.pdf.PdfPCell fLeft
                    = new com.lowagie.text.pdf.PdfPCell(
                            new com.lowagie.text.Phrase(
                                    "BiblioGest — Rapport genere automatiquement", fFooter));
            fLeft.setBorder(com.lowagie.text.Rectangle.TOP);
            fLeft.setBorderColor(new java.awt.Color(232, 226, 216));
            fLeft.setPaddingTop(8);

            com.lowagie.text.Paragraph fRightP
                    = new com.lowagie.text.Paragraph("Page 1 / 1", fFooter);
            fRightP.setAlignment(com.lowagie.text.Element.ALIGN_RIGHT);
            com.lowagie.text.pdf.PdfPCell fRight
                    = new com.lowagie.text.pdf.PdfPCell();
            fRight.addElement(fRightP);
            fRight.setBorder(com.lowagie.text.Rectangle.TOP);
            fRight.setBorderColor(new java.awt.Color(232, 226, 216));
            fRight.setPaddingTop(8);

            footer.addCell(fLeft);
            footer.addCell(fRight);
            doc.add(footer);

            doc.close();

            // Ouvrir le PDF
            java.awt.Desktop.getDesktop().open(new java.io.File(chemin));
            javax.swing.JOptionPane.showMessageDialog(this,
                    "Rapport genere avec succes !",
                    "Succes", javax.swing.JOptionPane.INFORMATION_MESSAGE);

        } catch (Exception ex) {
            javax.swing.JOptionPane.showMessageDialog(this,
                    "Erreur : " + ex.getMessage(),
                    "Erreur", javax.swing.JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
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

        headerPanel = new javax.swing.JPanel();
        lblTitre = new javax.swing.JLabel();
        bodyPanel = new javax.swing.JPanel();
        filtrePanel = new javax.swing.JPanel();
        du = new javax.swing.JLabel();
        dateDebut = new com.toedter.calendar.JDateChooser();
        au = new javax.swing.JLabel();
        dateFin = new com.toedter.calendar.JDateChooser();
        btnAppliquer = new javax.swing.JButton();
        btnImprimer = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        statsPanel = new javax.swing.JPanel();
        carte1 = new javax.swing.JPanel();
        bande1 = new javax.swing.JPanel();
        center1 = new javax.swing.JPanel();
        lblTitreCarte1 = new javax.swing.JLabel();
        lblValCarte1 = new javax.swing.JLabel();
        carte2 = new javax.swing.JPanel();
        bande2 = new javax.swing.JPanel();
        center2 = new javax.swing.JPanel();
        lblTitreCarte2 = new javax.swing.JLabel();
        lblValCarte2 = new javax.swing.JLabel();
        carte3 = new javax.swing.JPanel();
        bande3 = new javax.swing.JPanel();
        center3 = new javax.swing.JPanel();
        lblTitreCarte3 = new javax.swing.JLabel();
        lblValCarte3 = new javax.swing.JLabel();
        carte4 = new javax.swing.JPanel();
        bande4 = new javax.swing.JPanel();
        center4 = new javax.swing.JPanel();
        lblTitreCarte4 = new javax.swing.JLabel();
        lblValCarte4 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        topPanel = new javax.swing.JPanel();
        topTitrePanel = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        lblTopTitre = new javax.swing.JLabel();
        topScrollPane = new javax.swing.JScrollPane();
        topTable = new javax.swing.JTable();

        setBackground(new java.awt.Color(245, 240, 232));
        setLayout(new java.awt.BorderLayout());

        headerPanel.setBackground(new java.awt.Color(245, 240, 232));
        headerPanel.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(232, 226, 216)));
        headerPanel.setPreferredSize(new java.awt.Dimension(0, 60));
        headerPanel.setLayout(new java.awt.BorderLayout());

        lblTitre.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        lblTitre.setForeground(new java.awt.Color(26, 32, 53));
        lblTitre.setText("Statistiques");
        lblTitre.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 28, 0, 0));
        headerPanel.add(lblTitre, java.awt.BorderLayout.CENTER);

        add(headerPanel, java.awt.BorderLayout.NORTH);

        bodyPanel.setBackground(new java.awt.Color(245, 240, 232));
        bodyPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(20, 28, 24, 28));
        bodyPanel.setLayout(new javax.swing.BoxLayout(bodyPanel, javax.swing.BoxLayout.Y_AXIS));

        filtrePanel.setBackground(new java.awt.Color(255, 255, 255));
        filtrePanel.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(232, 226, 216)), javax.swing.BorderFactory.createEmptyBorder(0, 16, 0, 16)));
        filtrePanel.setMaximumSize(new java.awt.Dimension(32767, 56));
        filtrePanel.setPreferredSize(new java.awt.Dimension(0, 56));
        filtrePanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 12, 12));

        du.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        du.setForeground(new java.awt.Color(26, 32, 53));
        du.setText("Du :");
        filtrePanel.add(du);

        dateDebut.setPreferredSize(new java.awt.Dimension(160, 32));
        filtrePanel.add(dateDebut);

        au.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        au.setForeground(new java.awt.Color(26, 32, 53));
        au.setText("Au :");
        filtrePanel.add(au);

        dateFin.setPreferredSize(new java.awt.Dimension(160, 32));
        filtrePanel.add(dateFin);

        btnAppliquer.setBackground(new java.awt.Color(212, 168, 67));
        btnAppliquer.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnAppliquer.setForeground(new java.awt.Color(26, 32, 53));
        btnAppliquer.setText("Appliquer");
        btnAppliquer.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnAppliquer.setFocusPainted(false);
        filtrePanel.add(btnAppliquer);

        btnImprimer.setBackground(new java.awt.Color(26, 32, 53));
        btnImprimer.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnImprimer.setForeground(new java.awt.Color(255, 255, 255));
        btnImprimer.setText("Imprimer");
        btnImprimer.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnImprimer.setFocusPainted(false);
        filtrePanel.add(btnImprimer);

        bodyPanel.add(filtrePanel);

        jPanel1.setBackground(new java.awt.Color(245, 240, 232));
        jPanel1.setMaximumSize(new java.awt.Dimension(32767, 20));
        jPanel1.setPreferredSize(new java.awt.Dimension(0, 20));
        bodyPanel.add(jPanel1);

        statsPanel.setBackground(new java.awt.Color(245, 240, 232));
        statsPanel.setMaximumSize(new java.awt.Dimension(32767, 110));
        statsPanel.setPreferredSize(new java.awt.Dimension(0, 110));
        statsPanel.setLayout(new java.awt.GridLayout(1, 4, 16, 0));

        carte1.setBackground(new java.awt.Color(255, 255, 255));
        carte1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(232, 226, 216)));
        carte1.setLayout(new java.awt.BorderLayout());

        bande1.setBackground(new java.awt.Color(26, 32, 53));
        bande1.setPreferredSize(new java.awt.Dimension(0, 6));
        carte1.add(bande1, java.awt.BorderLayout.NORTH);

        center1.setBackground(new java.awt.Color(255, 255, 255));
        center1.setBorder(javax.swing.BorderFactory.createEmptyBorder(12, 18, 12, 18));
        center1.setLayout(new java.awt.BorderLayout());

        lblTitreCarte1.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblTitreCarte1.setForeground(new java.awt.Color(90, 96, 112));
        lblTitreCarte1.setText("Total emprunts");
        center1.add(lblTitreCarte1, java.awt.BorderLayout.NORTH);

        lblValCarte1.setFont(new java.awt.Font("Segoe UI", 1, 28)); // NOI18N
        lblValCarte1.setForeground(new java.awt.Color(26, 32, 53));
        lblValCarte1.setText("0");
        center1.add(lblValCarte1, java.awt.BorderLayout.CENTER);

        carte1.add(center1, java.awt.BorderLayout.CENTER);

        statsPanel.add(carte1);

        carte2.setBackground(new java.awt.Color(255, 255, 255));
        carte2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(232, 226, 216)));
        carte2.setLayout(new java.awt.BorderLayout());

        bande2.setBackground(new java.awt.Color(59, 173, 114));
        bande2.setPreferredSize(new java.awt.Dimension(0, 6));
        carte2.add(bande2, java.awt.BorderLayout.NORTH);

        center2.setBackground(new java.awt.Color(255, 255, 255));
        center2.setBorder(javax.swing.BorderFactory.createEmptyBorder(12, 18, 12, 18));
        center2.setLayout(new java.awt.BorderLayout());

        lblTitreCarte2.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblTitreCarte2.setForeground(new java.awt.Color(90, 96, 112));
        lblTitreCarte2.setText("Rendus");
        center2.add(lblTitreCarte2, java.awt.BorderLayout.NORTH);

        lblValCarte2.setFont(new java.awt.Font("Segoe UI", 1, 28)); // NOI18N
        lblValCarte2.setForeground(new java.awt.Color(59, 173, 114));
        lblValCarte2.setText("0");
        center2.add(lblValCarte2, java.awt.BorderLayout.CENTER);

        carte2.add(center2, java.awt.BorderLayout.CENTER);

        statsPanel.add(carte2);

        carte3.setBackground(new java.awt.Color(255, 255, 255));
        carte3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(232, 226, 216)));
        carte3.setLayout(new java.awt.BorderLayout());

        bande3.setBackground(new java.awt.Color(224, 82, 82));
        bande3.setPreferredSize(new java.awt.Dimension(0, 6));
        carte3.add(bande3, java.awt.BorderLayout.NORTH);

        center3.setBackground(new java.awt.Color(255, 255, 255));
        center3.setBorder(javax.swing.BorderFactory.createEmptyBorder(12, 18, 12, 18));
        center3.setLayout(new java.awt.BorderLayout());

        lblTitreCarte3.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblTitreCarte3.setForeground(new java.awt.Color(90, 96, 112));
        lblTitreCarte3.setText("En retard");
        center3.add(lblTitreCarte3, java.awt.BorderLayout.NORTH);

        lblValCarte3.setFont(new java.awt.Font("Segoe UI", 1, 28)); // NOI18N
        lblValCarte3.setForeground(new java.awt.Color(224, 82, 82));
        lblValCarte3.setText("0");
        center3.add(lblValCarte3, java.awt.BorderLayout.CENTER);

        carte3.add(center3, java.awt.BorderLayout.CENTER);

        statsPanel.add(carte3);

        carte4.setBackground(new java.awt.Color(255, 255, 255));
        carte4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(232, 226, 216)));
        carte4.setLayout(new java.awt.BorderLayout());

        bande4.setBackground(new java.awt.Color(224, 154, 48));
        bande4.setPreferredSize(new java.awt.Dimension(0, 6));
        carte4.add(bande4, java.awt.BorderLayout.NORTH);

        center4.setBackground(new java.awt.Color(255, 255, 255));
        center4.setBorder(javax.swing.BorderFactory.createEmptyBorder(12, 18, 12, 18));
        center4.setLayout(new java.awt.BorderLayout());

        lblTitreCarte4.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblTitreCarte4.setForeground(new java.awt.Color(90, 96, 112));
        lblTitreCarte4.setText("En cours");
        center4.add(lblTitreCarte4, java.awt.BorderLayout.NORTH);

        lblValCarte4.setFont(new java.awt.Font("Segoe UI", 1, 28)); // NOI18N
        lblValCarte4.setForeground(new java.awt.Color(224, 154, 48));
        lblValCarte4.setText("0");
        center4.add(lblValCarte4, java.awt.BorderLayout.CENTER);

        carte4.add(center4, java.awt.BorderLayout.CENTER);

        statsPanel.add(carte4);

        bodyPanel.add(statsPanel);

        jPanel2.setBackground(new java.awt.Color(245, 240, 232));
        jPanel2.setMaximumSize(new java.awt.Dimension(32767, 20));
        jPanel2.setPreferredSize(new java.awt.Dimension(0, 20));
        bodyPanel.add(jPanel2);

        topPanel.setBackground(new java.awt.Color(255, 255, 255));
        topPanel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(232, 226, 216)));
        topPanel.setLayout(new java.awt.BorderLayout());

        topTitrePanel.setBackground(new java.awt.Color(255, 255, 255));
        topTitrePanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(14, 0, 14, 0));
        topTitrePanel.setPreferredSize(new java.awt.Dimension(0, 44));
        topTitrePanel.setLayout(new java.awt.BorderLayout());

        jPanel3.setBackground(new java.awt.Color(212, 168, 67));
        jPanel3.setPreferredSize(new java.awt.Dimension(4, 0));
        topTitrePanel.add(jPanel3, java.awt.BorderLayout.WEST);

        lblTopTitre.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblTopTitre.setForeground(new java.awt.Color(26, 32, 53));
        lblTopTitre.setText("Top 5 livres empruntés");
        lblTopTitre.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 12, 0, 0));
        topTitrePanel.add(lblTopTitre, java.awt.BorderLayout.CENTER);

        topPanel.add(topTitrePanel, java.awt.BorderLayout.NORTH);

        topTable.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        topTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "#", "Titre", "Nb emprunts"
            }
        ));
        topTable.setRowHeight(40);
        topTable.setSelectionBackground(new java.awt.Color(250, 246, 238));
        topScrollPane.setViewportView(topTable);
        if (topTable.getColumnModel().getColumnCount() > 0) {
            topTable.getColumnModel().getColumn(0).setPreferredWidth(50);
            topTable.getColumnModel().getColumn(1).setPreferredWidth(400);
            topTable.getColumnModel().getColumn(2).setPreferredWidth(120);
        }

        topPanel.add(topScrollPane, java.awt.BorderLayout.CENTER);

        bodyPanel.add(topPanel);

        add(bodyPanel, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel au;
    private javax.swing.JPanel bande1;
    private javax.swing.JPanel bande2;
    private javax.swing.JPanel bande3;
    private javax.swing.JPanel bande4;
    private javax.swing.JPanel bodyPanel;
    private javax.swing.JButton btnAppliquer;
    private javax.swing.JButton btnImprimer;
    private javax.swing.JPanel carte1;
    private javax.swing.JPanel carte2;
    private javax.swing.JPanel carte3;
    private javax.swing.JPanel carte4;
    private javax.swing.JPanel center1;
    private javax.swing.JPanel center2;
    private javax.swing.JPanel center3;
    private javax.swing.JPanel center4;
    private com.toedter.calendar.JDateChooser dateDebut;
    private com.toedter.calendar.JDateChooser dateFin;
    private javax.swing.JLabel du;
    private javax.swing.JPanel filtrePanel;
    private javax.swing.JPanel headerPanel;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JLabel lblTitre;
    private javax.swing.JLabel lblTitreCarte1;
    private javax.swing.JLabel lblTitreCarte2;
    private javax.swing.JLabel lblTitreCarte3;
    private javax.swing.JLabel lblTitreCarte4;
    private javax.swing.JLabel lblTopTitre;
    private javax.swing.JLabel lblValCarte1;
    private javax.swing.JLabel lblValCarte2;
    private javax.swing.JLabel lblValCarte3;
    private javax.swing.JLabel lblValCarte4;
    private javax.swing.JPanel statsPanel;
    private javax.swing.JPanel topPanel;
    private javax.swing.JScrollPane topScrollPane;
    private javax.swing.JTable topTable;
    private javax.swing.JPanel topTitrePanel;
    // End of variables declaration//GEN-END:variables
}

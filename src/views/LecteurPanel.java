/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package views;

import dao.LecteurDAO;
import java.util.ArrayList;
import javax.swing.table.DefaultTableModel;
import models.Lecteur;

/**
 *
 * @author Admin
 */
public class LecteurPanel extends javax.swing.JPanel {

    private javax.swing.ImageIcon icoModifier;
    private javax.swing.ImageIcon icoSupprimer;
    private ArrayList<Integer> idsLecteurs = new java.util.ArrayList<>();

    public LecteurPanel() {
        initComponents();
        // ── Bouton état lecteurs ──
        javax.swing.JButton btnEtatLecteurs = new javax.swing.JButton("📄 État lecteurs");
        btnEtatLecteurs.setBackground(new java.awt.Color(245, 240, 232));
        btnEtatLecteurs.setForeground(new java.awt.Color(26, 32, 53));
        btnEtatLecteurs.setFont(new java.awt.Font("Segoe UI Emoji", java.awt.Font.PLAIN, 12));
        btnEtatLecteurs.setBorder(javax.swing.BorderFactory.createLineBorder(
                new java.awt.Color(212, 168, 67)));
        btnEtatLecteurs.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnEtatLecteurs.setFocusPainted(false);
        btnEtatLecteurs.addActionListener(e -> utils.EtatsHelper.etatListeLecteurs());
        lecteurHeaderRight.add(btnEtatLecteurs, 0);
        lecteurHeaderRight.revalidate();

        //
        javax.swing.JButton btnExcelLecteurs = new javax.swing.JButton("📊 Excel");
        btnExcelLecteurs.setBackground(new java.awt.Color(33, 115, 70));
        btnExcelLecteurs.setForeground(java.awt.Color.WHITE);
        btnExcelLecteurs.setFont(new java.awt.Font("Segoe UI Emoji", java.awt.Font.PLAIN, 12));
        btnExcelLecteurs.setBorder(javax.swing.BorderFactory.createEmptyBorder(4, 12, 4, 12));
        btnExcelLecteurs.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnExcelLecteurs.setFocusPainted(false);
        btnExcelLecteurs.addActionListener(e -> utils.EtatsHelper.exportExcelLecteurs());
        lecteurHeaderRight.add(btnExcelLecteurs, 0);
//
        icoModifier = new javax.swing.ImageIcon(
                getClass().getResource("/Images/edit.png"));
        icoSupprimer = new javax.swing.ImageIcon(
                getClass().getResource("/Images/delete.png"));

        btnAjouterlecteur1.putClientProperty(
                "JButton.buttonType", "roundRect");

        styliserTableau();
        initRenderers();
        initEvenements();
        chargerLecteurs();
    }

    private void styliserTableau() {
        lecteursTable.setDefaultEditor(Object.class, null);
        lecteursTable.setShowVerticalLines(false);
        lecteursTable.setRowHeight(52);
        lecteursTable.setIntercellSpacing(new java.awt.Dimension(0, 0));
        lecteursTable.setSelectionBackground(new java.awt.Color(250, 246, 238));
        lecteursTable.setSelectionForeground(new java.awt.Color(26, 32, 53));

        // En-tête
        lecteursTable.getTableHeader().setBackground(new java.awt.Color(245, 240, 232));
        lecteursTable.getTableHeader().setFont(
                new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 11));
        lecteursTable.getTableHeader().setForeground(new java.awt.Color(154, 160, 176));
        lecteursTable.getTableHeader().setPreferredSize(new java.awt.Dimension(0, 42));

        // Largeurs
        lecteursTable.getColumnModel().getColumn(0).setPreferredWidth(220);
        lecteursTable.getColumnModel().getColumn(1).setPreferredWidth(140);
        lecteursTable.getColumnModel().getColumn(2).setPreferredWidth(130);
        lecteursTable.getColumnModel().getColumn(3).setPreferredWidth(120);
        lecteursTable.getColumnModel().getColumn(4).setPreferredWidth(100);

        // Renderer général
        lecteursTable.setDefaultRenderer(Object.class,
                new javax.swing.table.DefaultTableCellRenderer() {
            @Override
            public java.awt.Component getTableCellRendererComponent(
                    javax.swing.JTable t, Object v, boolean sel,
                    boolean foc, int r, int c) {
                super.getTableCellRendererComponent(t, v, sel, foc, r, c);
                setHorizontalAlignment(c == 0 ? LEFT : CENTER);
                setBorder(javax.swing.BorderFactory.createEmptyBorder(
                        0, c == 0 ? 14 : 8, 0, 8));
                setFont(new java.awt.Font("Segoe UI",
                        java.awt.Font.PLAIN, 13));
                if (sel) {
                    setBackground(new java.awt.Color(250, 246, 238));
                    setForeground(new java.awt.Color(26, 32, 53));
                } else {
                    setBackground(r % 2 == 0
                            ? java.awt.Color.WHITE
                            : new java.awt.Color(250, 249, 247));
                    setForeground(new java.awt.Color(90, 96, 112));
                }
                return this;
            }
        });
    }

    private void initRenderers() {
        // Colonne Membre (0) — avatar + nom complet
        lecteursTable.getColumnModel().getColumn(0)
                .setCellRenderer(new MembreCellRenderer());

        // Colonne Emprunts (3) — badge coloré
        lecteursTable.getColumnModel().getColumn(3)
                .setCellRenderer(new EmpruntsBadgeRenderer());

        // Colonne Actions (4) — boutons
        lecteursTable.getColumnModel().getColumn(4)
                .setCellRenderer(new ActionsBtnRenderer());
    }

    private void initEvenements() {
        btnAjouterlecteur1.addActionListener(e -> {
            AjouterLecteur dialog = new AjouterLecteur(
                    (java.awt.Frame) javax.swing.SwingUtilities
                            .getWindowAncestor(this),
                    true);
            dialog.setVisible(true);
            chargerLecteurs();
        });
        lecteursTable.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                int row = lecteursTable.rowAtPoint(e.getPoint());
                int col = lecteursTable.columnAtPoint(e.getPoint());
                if (row < 0) {
                    return;
                }

                if (col == 4) {
                    // Modifier ou Supprimer
                    java.awt.Rectangle rect = lecteursTable.getCellRect(row, col, false);
                    int relX = e.getX() - rect.x;
                    if (relX < rect.width / 2) {
                        modifierLecteur(row);
                    } else {
                        supprimerLecteur(row);
                    }
                } else if (col == 0) {
                    // Historique des emprunts
                    int id = idsLecteurs.get(row);
                    String nom = lecteursTable.getValueAt(row, 0).toString();
                    HistoriqueEmprunts dialog = new HistoriqueEmprunts(
                            (java.awt.Frame) javax.swing.SwingUtilities.getWindowAncestor(LecteurPanel.this),
                            true, id, nom);
                    dialog.setVisible(true);
                }
            }
        });

        initRecherche();
    }

    private void chargerLecteurs() {
        dao.LecteurDAO lecteurDAO = new dao.LecteurDAO();
        DefaultTableModel model = (DefaultTableModel) lecteursTable.getModel();
        model.setRowCount(0);
        idsLecteurs.clear();

        ArrayList<models.Lecteur> lecteurs = lecteurDAO.getTousLesLecteurs();
        for (models.Lecteur l : lecteurs) {
            int nbEmprunts = lecteurDAO.getNbEmpruntsActifs(l.getIdLecteur());
            String dateInscription = lecteurDAO.getDateInscription(l.getIdLecteur());

            model.addRow(new Object[]{
                l.getNom() + " " + l.getPrenom(), // Membre
                l.getTelephone(), // Téléphone
                dateInscription != null ? formaterDate(dateInscription) : "—",
                nbEmprunts > 0 ? nbEmprunts + " emprunt(s)" : "—", // Emprunts
                "" // Actions
            });
            idsLecteurs.add(l.getIdLecteur());
        }
    }

    private String formaterDate(String date) {
        try {
            java.time.LocalDate d = java.time.LocalDate.parse(date);
            return d.format(java.time.format.DateTimeFormatter
                    .ofPattern("dd/MM/yyyy"));
        } catch (Exception e) {
            return date;
        }
    }

    private void initRecherche() {
        txtRechercheLecteur.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusGained(java.awt.event.FocusEvent e) {
                if (txtRechercheLecteur.getText().equals("Rechercher un lecteur . . .")) {
                    txtRechercheLecteur.setText("");
                    txtRechercheLecteur.setForeground(new java.awt.Color(26, 32, 53));
                }
            }

            @Override
            public void focusLost(java.awt.event.FocusEvent e) {
                if (txtRechercheLecteur.getText().isEmpty()) {
                    txtRechercheLecteur.setText("Rechercher un lecteur . . .");
                    txtRechercheLecteur.setForeground(new java.awt.Color(154, 160, 176));
                }
            }
        });

        txtRechercheLecteur.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyReleased(java.awt.event.KeyEvent e) {
                String texte = txtRechercheLecteur.getText();
                if (!texte.equals("Rechercher un lecteur . . .")) {
                    rechercherLecteurs(texte);
                }
            }
        });
    }

    private void rechercherLecteurs(String texte) {
        dao.LecteurDAO lecteurDAO = new dao.LecteurDAO();
        DefaultTableModel model = (DefaultTableModel) lecteursTable.getModel();
        model.setRowCount(0);
        idsLecteurs.clear();

        ArrayList<models.Lecteur> lecteurs = texte.isEmpty()
                ? lecteurDAO.getTousLesLecteurs()
                : lecteurDAO.rechercher(texte);

        for (models.Lecteur l : lecteurs) {
            int nbEmprunts = lecteurDAO.getNbEmpruntsActifs(l.getIdLecteur());
            model.addRow(new Object[]{
                l.getNom(),
                l.getPrenom(),
                l.getTelephone(),
                nbEmprunts > 0 ? nbEmprunts + " emprunt(s)" : "—",
                ""
            });
            idsLecteurs.add(l.getIdLecteur());
        }
    }

    private void modifierLecteur(int row) {
        DefaultTableModel model = (DefaultTableModel) lecteursTable.getModel();
        int id = idsLecteurs.get(row);

        // Séparer nom et prénom
        String membre = model.getValueAt(row, 0).toString();
        String[] parts = membre.split(" ", 2);
        String nom = parts.length >= 1 ? parts[0] : "";
        String prenom = parts.length >= 2 ? parts[1] : "";

        String tel = model.getValueAt(row, 1) != null
                ? model.getValueAt(row, 1).toString()
                : "";

        LecteurDAO lecteurDAO = new LecteurDAO();
        String adresse = lecteurDAO.getAdresse(id);

        ModifierLecteur dialog = new ModifierLecteur(
                (java.awt.Frame) javax.swing.SwingUtilities.getWindowAncestor(this),
                true, id, nom, prenom, tel, adresse);
        dialog.setVisible(true);
        chargerLecteurs();
    }

    private void supprimerLecteur(int row) {
        DefaultTableModel model = (DefaultTableModel) lecteursTable.getModel();
        String nom = model.getValueAt(row, 0).toString();
        int id = idsLecteurs.get(row);

        // Vérifier emprunts en cours
        dao.LecteurDAO lecteurDAO = new dao.LecteurDAO();
        if (lecteurDAO.aDesEmpruntsEnCours(id)) {
            javax.swing.JOptionPane.showMessageDialog(this,
                    "Impossible de supprimer " + nom + ".\n"
                    + "Ce lecteur a des emprunts en cours ou en retard.",
                    "Suppression impossible",
                    javax.swing.JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = javax.swing.JOptionPane.showConfirmDialog(this,
                "Supprimer " + nom + " ?", "Confirmation",
                javax.swing.JOptionPane.YES_NO_OPTION);

        if (confirm == javax.swing.JOptionPane.YES_OPTION) {
            if (lecteurDAO.supprimer(id)) {
                model.removeRow(row);
                idsLecteurs.remove(row);
            } else {
                javax.swing.JOptionPane.showMessageDialog(this,
                        "Erreur lors de la suppression.",
                        "Erreur", javax.swing.JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // ── Badge emprunts actifs ──────────────────────────────────
    private class EmpruntsBadgeRenderer
            extends javax.swing.table.DefaultTableCellRenderer {

        @Override
        public java.awt.Component getTableCellRendererComponent(
                javax.swing.JTable t, Object v, boolean sel,
                boolean foc, int r, int c) {

            javax.swing.JPanel cell = new javax.swing.JPanel(
                    new java.awt.GridBagLayout());
            cell.setBackground(r % 2 == 0
                    ? java.awt.Color.WHITE
                    : new java.awt.Color(250, 249, 247));
            if (sel) {
                cell.setBackground(
                        new java.awt.Color(250, 246, 238));
            }

            if (v == null || v.toString().equals("—")) {
                javax.swing.JLabel lbl = new javax.swing.JLabel("—");
                lbl.setForeground(new java.awt.Color(200, 200, 200));
                lbl.setFont(new java.awt.Font(
                        "Segoe UI", java.awt.Font.PLAIN, 13));
                cell.add(lbl);
                return cell;
            }

            String texte = v.toString();
            cell.add(creerBadge(texte,
                    new java.awt.Color(255, 246, 225),
                    new java.awt.Color(180, 110, 10)));
            return cell;
        }
    }

    // ── Boutons Modifier / Supprimer ──────────────────────────
    private class ActionsBtnRenderer
            implements javax.swing.table.TableCellRenderer {

        @Override
        public java.awt.Component getTableCellRendererComponent(
                javax.swing.JTable t, Object v, boolean sel,
                boolean foc, int r, int c) {

            javax.swing.JPanel panel = new javax.swing.JPanel(
                    new java.awt.FlowLayout(
                            java.awt.FlowLayout.CENTER, 6, 9));
            panel.setBackground(r % 2 == 0
                    ? java.awt.Color.WHITE
                    : new java.awt.Color(250, 249, 247));
            if (sel) {
                panel.setBackground(
                        new java.awt.Color(250, 246, 238));
            }

            javax.swing.JButton btnMod = new javax.swing.JButton();
            btnMod.setIcon(icoModifier);
            btnMod.setPreferredSize(
                    new java.awt.Dimension(32, 32));
            btnMod.setBorder(javax.swing.BorderFactory
                    .createEmptyBorder());
            btnMod.setBackground(
                    new java.awt.Color(240, 235, 224));
            btnMod.setFocusPainted(false);
            btnMod.setCursor(new java.awt.Cursor(
                    java.awt.Cursor.HAND_CURSOR));

            javax.swing.JButton btnSup = new javax.swing.JButton();
            btnSup.setIcon(icoSupprimer);
            btnSup.setPreferredSize(
                    new java.awt.Dimension(32, 32));
            btnSup.setBorder(javax.swing.BorderFactory
                    .createEmptyBorder());
            btnSup.setBackground(
                    new java.awt.Color(255, 240, 240));
            btnSup.setFocusPainted(false);
            btnSup.setCursor(new java.awt.Cursor(
                    java.awt.Cursor.HAND_CURSOR));

            panel.add(btnMod);
            panel.add(btnSup);
            return panel;
        }
    }

    // ── Utilitaire badge arrondi ──────────────────────────────
    private javax.swing.JLabel creerBadge(String texte,
            java.awt.Color bg, java.awt.Color fg) {
        javax.swing.JLabel badge = new javax.swing.JLabel(texte) {
            @Override
            protected void paintComponent(java.awt.Graphics g) {
                java.awt.Graphics2D g2 = (java.awt.Graphics2D) g.create();
                g2.setRenderingHint(
                        java.awt.RenderingHints.KEY_ANTIALIASING,
                        java.awt.RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0,
                        getWidth(), getHeight(), 16, 16);
                super.paintComponent(g);
                g2.dispose();
            }
        };
        badge.setOpaque(false);
        badge.setBackground(bg);
        badge.setForeground(fg);
        badge.setFont(new java.awt.Font(
                "Segoe UI", java.awt.Font.BOLD, 11));
        badge.setHorizontalAlignment(
                javax.swing.SwingConstants.CENTER);
        badge.setBorder(javax.swing.BorderFactory
                .createEmptyBorder(3, 12, 3, 12));
        return badge;
    }

    private class MembreCellRenderer
            implements javax.swing.table.TableCellRenderer {

        @Override
        public java.awt.Component getTableCellRendererComponent(
                javax.swing.JTable t, Object v, boolean sel,
                boolean foc, int r, int c) {

            javax.swing.JPanel panel = new javax.swing.JPanel(
                    new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 12, 6));
            panel.setBackground(r % 2 == 0
                    ? java.awt.Color.WHITE
                    : new java.awt.Color(250, 249, 247));
            if (sel) {
                panel.setBackground(new java.awt.Color(250, 246, 238));
            }

            String nomComplet = v != null ? v.toString() : "";
            String[] parts = nomComplet.split(" ", 2);
            String init = "";
            if (parts.length >= 1 && parts[0].length() > 0) {
                init += parts[0].charAt(0);
            }
            if (parts.length >= 2 && parts[1].length() > 0) {
                init += parts[1].charAt(0);
            }

            // Avatar
            javax.swing.JLabel avatar = new javax.swing.JLabel(
                    init.toUpperCase()) {
                @Override
                protected void paintComponent(java.awt.Graphics g) {
                    java.awt.Graphics2D g2 = (java.awt.Graphics2D) g.create();
                    g2.setRenderingHint(
                            java.awt.RenderingHints.KEY_ANTIALIASING,
                            java.awt.RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(new java.awt.Color(212, 168, 67));
                    g2.fillOval(0, 0, getWidth(), getHeight());
                    super.paintComponent(g);
                    g2.dispose();
                }
            };
            avatar.setPreferredSize(new java.awt.Dimension(36, 36));
            avatar.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
            avatar.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 13));
            avatar.setForeground(new java.awt.Color(26, 32, 53));
            avatar.setOpaque(false);

            // Nom
            javax.swing.JLabel lblNom = new javax.swing.JLabel(nomComplet);
            lblNom.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 13));
            lblNom.setForeground(new java.awt.Color(26, 32, 53));

            panel.add(avatar);
            panel.add(lblNom);
            return panel;
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated
    // <editor-fold defaultstate="collapsed" desc="Generated
    // Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lecteurHeaderPanel = new javax.swing.JPanel();
        lblLecteursTitre = new javax.swing.JLabel();
        lecteurHeaderRight = new javax.swing.JPanel();
        btnAjouterlecteur1 = new javax.swing.JButton();
        lecteurBodyPanel = new javax.swing.JPanel();
        lecteurRecherchePanel = new javax.swing.JPanel();
        icoRechercheLecteur = new javax.swing.JLabel();
        txtRechercheLecteur = new javax.swing.JTextField();
        lecteurTablePanel = new javax.swing.JPanel();
        lecteurScrollPane = new javax.swing.JScrollPane();
        lecteursTable = new javax.swing.JTable();

        setBackground(new java.awt.Color(245, 240, 232));
        setLayout(new java.awt.BorderLayout());

        lecteurHeaderPanel.setBackground(new java.awt.Color(245, 240, 232));
        lecteurHeaderPanel.setBorder(javax.swing.BorderFactory.createCompoundBorder(
                javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(232, 226, 216)),
                javax.swing.BorderFactory.createEmptyBorder(0, 28, 0, 28)));
        lecteurHeaderPanel.setPreferredSize(new java.awt.Dimension(0, 70));
        lecteurHeaderPanel.setLayout(new java.awt.BorderLayout());

        lblLecteursTitre.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        lblLecteursTitre.setForeground(new java.awt.Color(26, 32, 53));
        lblLecteursTitre.setText("Lecteurs");
        lblLecteursTitre.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 10, 1, 1));
        lecteurHeaderPanel.add(lblLecteursTitre, java.awt.BorderLayout.WEST);

        lecteurHeaderRight.setBackground(new java.awt.Color(245, 240, 232));
        lecteurHeaderRight.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT, 28, 18));

        btnAjouterlecteur1.setBackground(new java.awt.Color(212, 168, 67));
        btnAjouterlecteur1.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnAjouterlecteur1.setForeground(new java.awt.Color(26, 32, 53));
        btnAjouterlecteur1.setText("+  Ajouter un lecteur");
        btnAjouterlecteur1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnAjouterlecteur1.setFocusPainted(false);
        lecteurHeaderRight.add(btnAjouterlecteur1);

        lecteurHeaderPanel.add(lecteurHeaderRight, java.awt.BorderLayout.EAST);

        add(lecteurHeaderPanel, java.awt.BorderLayout.PAGE_START);

        lecteurBodyPanel.setBackground(new java.awt.Color(245, 240, 232));
        lecteurBodyPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(20, 28, 14, 28));
        lecteurBodyPanel.setLayout(new java.awt.BorderLayout());

        lecteurRecherchePanel.setBackground(new java.awt.Color(255, 255, 255));
        lecteurRecherchePanel.setBorder(javax.swing.BorderFactory.createCompoundBorder(
                javax.swing.BorderFactory.createLineBorder(new java.awt.Color(232, 226, 216)),
                javax.swing.BorderFactory.createEmptyBorder(0, 16, 0, 16)));
        lecteurRecherchePanel.setPreferredSize(new java.awt.Dimension(0, 56));
        lecteurRecherchePanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 12, 12));

        icoRechercheLecteur.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/search.png"))); // NOI18N
        lecteurRecherchePanel.add(icoRechercheLecteur);

        txtRechercheLecteur.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        txtRechercheLecteur.setForeground(new java.awt.Color(154, 160, 176));
        txtRechercheLecteur.setText("Rechercher un lecteur . . .");
        txtRechercheLecteur.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 8, 0, 0));
        txtRechercheLecteur.setPreferredSize(new java.awt.Dimension(400, 32));
        lecteurRecherchePanel.add(txtRechercheLecteur);

        lecteurBodyPanel.add(lecteurRecherchePanel, java.awt.BorderLayout.NORTH);

        lecteurTablePanel.setBackground(new java.awt.Color(245, 240, 232));
        lecteurTablePanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(16, 0, 0, 0));
        lecteurTablePanel.setLayout(new java.awt.BorderLayout());

        lecteurScrollPane.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(232, 226, 216)));

        lecteursTable.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        lecteursTable.setModel(new javax.swing.table.DefaultTableModel(
                new Object[][] {
                        { null, null, null, null, null },
                        { null, null, null, null, null },
                        { null, null, null, null, null },
                        { null, null, null, null, null }
                },
                new String[] {
                        "Membre", "Téléphone", "Inscription", "Emprunts", "Actions"
                }) {
            Class[] types = new Class[] {
                    java.lang.String.class, java.lang.Integer.class, java.lang.String.class, java.lang.String.class,
                    java.lang.String.class
            };

            public Class getColumnClass(int columnIndex) {
                return types[columnIndex];
            }
        });
        lecteursTable.setGridColor(new java.awt.Color(232, 226, 216));
        lecteursTable.setRowHeight(44);
        lecteursTable.setSelectionBackground(new java.awt.Color(250, 246, 238));
        lecteursTable.setSelectionForeground(new java.awt.Color(26, 32, 53));
        lecteurScrollPane.setViewportView(lecteursTable);
        if (lecteursTable.getColumnModel().getColumnCount() > 0) {
            lecteursTable.getColumnModel().getColumn(0).setPreferredWidth(150);
            lecteursTable.getColumnModel().getColumn(1).setPreferredWidth(150);
            lecteursTable.getColumnModel().getColumn(2).setPreferredWidth(130);
            lecteursTable.getColumnModel().getColumn(3).setResizable(false);
            lecteursTable.getColumnModel().getColumn(3).setPreferredWidth(120);
            lecteursTable.getColumnModel().getColumn(4).setPreferredWidth(100);
        }

        lecteurTablePanel.add(lecteurScrollPane, java.awt.BorderLayout.CENTER);

        lecteurBodyPanel.add(lecteurTablePanel, java.awt.BorderLayout.CENTER);

        add(lecteurBodyPanel, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAjouterlecteur1;
    private javax.swing.JLabel icoRechercheLecteur;
    private javax.swing.JLabel lblLecteursTitre;
    private javax.swing.JPanel lecteurBodyPanel;
    private javax.swing.JPanel lecteurHeaderPanel;
    private javax.swing.JPanel lecteurHeaderRight;
    private javax.swing.JPanel lecteurRecherchePanel;
    private javax.swing.JScrollPane lecteurScrollPane;
    private javax.swing.JPanel lecteurTablePanel;
    private javax.swing.JTable lecteursTable;
    private javax.swing.JTextField txtRechercheLecteur;
    // End of variables declaration//GEN-END:variables
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package views;

import dao.EmpruntDAO;
import dao.LivreDAO;
import java.util.ArrayList;
import javax.swing.table.DefaultTableModel;
import models.Emprunt;
import models.Livre;

/**
 *
 * @author Admin
 */
public class EmpruntPanel extends javax.swing.JPanel {

    private EmpruntDAO empruntDAO = new EmpruntDAO();
    private ArrayList<Integer> idsEmprunts = new ArrayList<>();
    private javax.swing.ImageIcon icoRetour;

    public EmpruntPanel() {
        initComponents();
// ── Boutons états emprunts ──
        javax.swing.JButton btnEtatEncours = new javax.swing.JButton("📄 En cours");
        btnEtatEncours.setBackground(new java.awt.Color(245, 240, 232));
        btnEtatEncours.setForeground(new java.awt.Color(26, 32, 53));
        btnEtatEncours.setFont(new java.awt.Font("Segoe UI Emoji", java.awt.Font.PLAIN, 12));
        btnEtatEncours.setBorder(javax.swing.BorderFactory.createLineBorder(
                new java.awt.Color(212, 168, 67)));
        btnEtatEncours.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnEtatEncours.setFocusPainted(false);
        btnEtatEncours.addActionListener(e -> utils.EtatsHelper.etatEmpruntsEnCours());

        javax.swing.JButton btnEtatHistorique = new javax.swing.JButton("📄 Historique");
        btnEtatHistorique.setBackground(new java.awt.Color(245, 240, 232));
        btnEtatHistorique.setForeground(new java.awt.Color(26, 32, 53));
        btnEtatHistorique.setFont(new java.awt.Font("Segoe UI Emoji", java.awt.Font.PLAIN, 12));
        btnEtatHistorique.setBorder(javax.swing.BorderFactory.createLineBorder(
                new java.awt.Color(212, 168, 67)));
        btnEtatHistorique.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnEtatHistorique.setFocusPainted(false);
        btnEtatHistorique.addActionListener(e -> utils.EtatsHelper.etatHistoriqueEmprunts());

        javax.swing.JButton btnEtatRetards = new javax.swing.JButton("⚠ Retards");
        btnEtatRetards.setBackground(new java.awt.Color(255, 235, 235));
        btnEtatRetards.setForeground(new java.awt.Color(180, 30, 30));
        btnEtatRetards.setFont(new java.awt.Font("Segoe UI Emoji", java.awt.Font.BOLD, 12));
        btnEtatRetards.setBorder(javax.swing.BorderFactory.createLineBorder(
                new java.awt.Color(224, 82, 82)));
        btnEtatRetards.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnEtatRetards.setFocusPainted(false);
        btnEtatRetards.addActionListener(e -> utils.EtatsHelper.etatRapportRetards());

        ///j
javax.swing.JButton btnExcelEmprunts = new javax.swing.JButton("📊 Excel");
        btnExcelEmprunts.setBackground(new java.awt.Color(33, 115, 70));
        btnExcelEmprunts.setForeground(java.awt.Color.WHITE);
        btnExcelEmprunts.setFont(new java.awt.Font("Segoe UI Emoji", java.awt.Font.PLAIN, 12));
        btnExcelEmprunts.setBorder(javax.swing.BorderFactory.createEmptyBorder(4, 12, 4, 12));
        btnExcelEmprunts.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnExcelEmprunts.setFocusPainted(false);
        btnExcelEmprunts.addActionListener(e -> utils.EtatsHelper.exportExcelEmprunts());
        empruntsHeaderRight.add(btnExcelEmprunts, 0);

        empruntsHeaderRight.add(btnEtatEncours, 0);
        empruntsHeaderRight.add(btnEtatHistorique, 1);
        empruntsHeaderRight.add(btnEtatRetards, 2);
        empruntsHeaderRight.revalidate();
        empruntDAO = new EmpruntDAO();
        btnNouvelEmprunt.putClientProperty(
                "JButton.buttonType", "roundRect");

        styliserTableau();
        initRenderers();
        initEvenements();
        chargerEmprunts();
    }

    private void styliserTableau() {
        empruntsTable.setDefaultEditor(Object.class, null);
        empruntsTable.setShowVerticalLines(false);
        empruntsTable.setRowHeight(48);
        empruntsTable.setIntercellSpacing(
                new java.awt.Dimension(0, 0));
        empruntsTable.setSelectionBackground(
                new java.awt.Color(250, 246, 238));
        empruntsTable.setSelectionForeground(
                new java.awt.Color(26, 32, 53));

        empruntsTable.getTableHeader().setBackground(
                new java.awt.Color(245, 240, 232));
        empruntsTable.getTableHeader().setFont(
                new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 11));
        empruntsTable.getTableHeader().setForeground(
                new java.awt.Color(154, 160, 176));
        empruntsTable.getTableHeader().setPreferredSize(
                new java.awt.Dimension(0, 42));

        empruntsTable.getColumnModel().getColumn(0)
                .setPreferredWidth(210);
        empruntsTable.getColumnModel().getColumn(1)
                .setPreferredWidth(180);
        empruntsTable.getColumnModel().getColumn(2)
                .setPreferredWidth(115);
        empruntsTable.getColumnModel().getColumn(3)
                .setPreferredWidth(115);
        empruntsTable.getColumnModel().getColumn(4)
                .setPreferredWidth(110);
        empruntsTable.getColumnModel().getColumn(5)
                .setPreferredWidth(110);
        empruntsTable.getColumnModel().getColumn(6)
                .setPreferredWidth(100);

        // Renderer général
        empruntsTable.setDefaultRenderer(Object.class,
                new javax.swing.table.DefaultTableCellRenderer() {
            @Override
            public java.awt.Component
                    getTableCellRendererComponent(
                            javax.swing.JTable t, Object v,
                            boolean sel, boolean foc,
                            int r, int c) {
                super.getTableCellRendererComponent(
                        t, v, sel, foc, r, c);
                // Livre + Lecteur gauche, dates centré
                setHorizontalAlignment(
                        c <= 1 ? LEFT : CENTER);
                setBorder(javax.swing.BorderFactory
                        .createEmptyBorder(
                                0, c <= 1 ? 18 : 8, 0, 8));
                setFont(new java.awt.Font("Segoe UI",
                        c == 0 ? java.awt.Font.BOLD
                                : java.awt.Font.PLAIN, 13));
                if (sel) {
                    setBackground(
                            new java.awt.Color(250, 246, 238));
                    setForeground(
                            new java.awt.Color(26, 32, 53));
                } else {
                    setBackground(r % 2 == 0
                            ? java.awt.Color.WHITE
                            : new java.awt.Color(250, 249, 247));
                    setForeground(c == 0
                            ? new java.awt.Color(26, 32, 53)
                            : new java.awt.Color(90, 96, 112));
                }
                return this;
            }
        });
    }

    private void initRenderers() {
        // Statut arrondi — colonne 4
        empruntsTable.getColumnModel().getColumn(4)
                .setCellRenderer(new StatutRenderer());

        // Bouton retourner — colonne 6
        empruntsTable.getColumnModel().getColumn(6)
                .setCellRenderer(new RetourBtnRenderer());
        // Date retour réelle — colonne 5
        empruntsTable.getColumnModel().getColumn(5)
                .setCellRenderer(new DateRetourCellRenderer());
    }

    private void initEvenements() {
        btnNouvelEmprunt.addActionListener(e -> {
            AjouterEmprunt dialog = new AjouterEmprunt(
                    (java.awt.Frame) javax.swing.SwingUtilities
                            .getWindowAncestor(this), true);
            dialog.setVisible(true);
            chargerEmprunts();
        });

        empruntsTable.addMouseListener(
                new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(
                    java.awt.event.MouseEvent e) {
                int row = empruntsTable.rowAtPoint(
                        e.getPoint());
                int col = empruntsTable.columnAtPoint(
                        e.getPoint());
                if (row >= 0 && col == 6) {
                    Object s = empruntsTable
                            .getValueAt(row, 4);
                    if (s != null && !s.toString()
                            .equals("rendu")) {
                        enregistrerRetour(row);
                    }
                }
            }
        });

        // Listener combiné statut + recherche texte
        cmbStatut.addActionListener(e -> rechercherCombine());
        txtRechercheEmprunt.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyReleased(java.awt.event.KeyEvent e) {
                rechercherCombine();
            }
        });

    }

    private void rechercherCombine() {
        String texte = txtRechercheEmprunt.getText().trim();
        if (texte.equals("Rechercher . . .")) {
            texte = "";
        }

        String selected = cmbStatut.getSelectedItem().toString();
        String statut;
        switch (selected) {
            case "En cours":
                statut = "en cours";
                break;
            case "Rendu":
                statut = "rendu";
                break;
            case "Retard":
                statut = "retard";
                break;
            default:
                statut = "tous";
        }

        DefaultTableModel model = (DefaultTableModel) empruntsTable.getModel();
        model.setRowCount(0);
        idsEmprunts.clear();

        ArrayList<Emprunt> emprunts = statut.equals("tous")
                ? empruntDAO.getTousLesEmprunts()
                : empruntDAO.rechercherParStatut(statut);

        final String texteF = texte;
        for (Emprunt e : emprunts) {
            if (!texteF.isEmpty()
                    && !e.getTitre().toLowerCase().contains(texteF.toLowerCase())
                    && !e.getNomLecteur().toLowerCase().contains(texteF.toLowerCase())
                    && !e.getDateEmprunt().contains(texteF)) {
                continue;
            }
            String dateReelle = e.getDateRetourReelle() != null
                    ? formaterDate(e.getDateRetourReelle()) : "—";
            model.addRow(new Object[]{
                e.getTitre(), e.getNomLecteur(),
                formaterDate(e.getDateEmprunt()),
                formaterDate(e.getDateRetourPrevue()),
                e.getStatut(), dateReelle, ""
            });
            idsEmprunts.add(e.getIdEmprunt());
        }
    }

    private void chargerEmprunts() {
        DefaultTableModel model = (DefaultTableModel) empruntsTable.getModel();
        model.setRowCount(0);
        idsEmprunts.clear();

        ArrayList<Emprunt> emprunts = empruntDAO.getTousLesEmprunts();
        for (Emprunt e : emprunts) {
            String dateReelle = e.getDateRetourReelle() != null ? formaterDate(e.getDateRetourReelle()) : "—";
            model.addRow(new Object[]{
                e.getTitre(),
                e.getNomLecteur(),
                formaterDate(e.getDateEmprunt()),
                formaterDate(e.getDateRetourPrevue()),
                e.getStatut(),
                dateReelle,
                ""
            });
            idsEmprunts.add(e.getIdEmprunt());
        }
    }

    private void filtrerParStatut() {
        String selected = cmbStatut.getSelectedItem().toString();
        String statut;
        switch (selected) {
            case "En cours":
                statut = "en cours";
                break;
            case "Rendu":
                statut = "rendu";
                break;
            case "Retard":
                statut = "retard";
                break;
            default:
                statut = "tous";
        }
        DefaultTableModel model = (DefaultTableModel) empruntsTable.getModel();
        model.setRowCount(0);
        idsEmprunts.clear();

        ArrayList<Emprunt> emprunts = statut.equals("tous")
                ? empruntDAO.getTousLesEmprunts()
                : empruntDAO.rechercherParStatut(statut);

        for (Emprunt e : emprunts) {
            String dateReelle = e.getDateRetourReelle() != null ? formaterDate(e.getDateRetourReelle()) : "—";
            model.addRow(new Object[]{
                e.getTitre(),
                e.getNomLecteur(),
                formaterDate(e.getDateEmprunt()),
                formaterDate(e.getDateRetourPrevue()),
                e.getStatut(),
                dateReelle,
                ""
            });
            idsEmprunts.add(e.getIdEmprunt());
        }
    }

    private void initRecherche() {
        txtRechercheEmprunt.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusGained(java.awt.event.FocusEvent e) {
                if (txtRechercheEmprunt.getText().equals("Rechercher . . .")) {
                    txtRechercheEmprunt.setText("");
                    txtRechercheEmprunt.setForeground(new java.awt.Color(26, 32, 53));
                }
            }

            @Override
            public void focusLost(java.awt.event.FocusEvent e) {
                if (txtRechercheEmprunt.getText().isEmpty()) {
                    txtRechercheEmprunt.setText("Rechercher . . .");
                    txtRechercheEmprunt.setForeground(new java.awt.Color(154, 160, 176));
                }
            }
        });

        // KeyListener pour filtrer en temps réel
        txtRechercheEmprunt.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyReleased(java.awt.event.KeyEvent e) {
                String texte = txtRechercheEmprunt.getText().trim();
                if (!texte.equals("Rechercher . . .")) {
                    rechercherEmprunts(texte);
                }
            }
        });
    }

    private void rechercherEmprunts(String texte) {
        DefaultTableModel model = (DefaultTableModel) empruntsTable.getModel();
        model.setRowCount(0);
        idsEmprunts.clear();

        ArrayList<Emprunt> emprunts = empruntDAO.getTousLesEmprunts();

        for (Emprunt e : emprunts) {
            // Filtrer par titre ou lecteur
            if (texte.isEmpty()
                    || e.getTitre().toLowerCase().contains(texte.toLowerCase())
                    || e.getNomLecteur().toLowerCase().contains(texte.toLowerCase())) {

                String dateReelle = e.getDateRetourReelle() != null ? formaterDate(e.getDateRetourReelle()) : "—";

                model.addRow(new Object[]{
                    e.getTitre(),
                    e.getNomLecteur(),
                    formaterDate(e.getDateEmprunt()),
                    formaterDate(e.getDateRetourPrevue()),
                    e.getStatut(),
                    dateReelle,
                    ""
                });
                idsEmprunts.add(e.getIdEmprunt());
            }
        }
    }

    private String formaterDate(String date) {
        try {
            java.time.LocalDate d = java.time.LocalDate.parse(date);
            return d.format(java.time.format.DateTimeFormatter
                    .ofPattern("dd/MM/yyyy"));
        } catch (Exception ex) {
            return date;
        }
    }

    private void enregistrerRetour(int row) {
        int idEmprunt = idsEmprunts.get(row);
        String titre = empruntsTable.getValueAt(row, 0).toString();
        String lecteur = empruntsTable.getValueAt(row, 1).toString();
        String dateEmp = empruntsTable.getValueAt(row, 2).toString();
        String datePrev = empruntsTable.getValueAt(row, 3).toString();
        String statut = empruntsTable.getValueAt(row, 4).toString();
        try {
            java.time.LocalDate d = java.time.LocalDate.parse(
                    datePrev,
                    java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy")
            );
            datePrev = d.toString(); // → "yyyy-MM-dd"
        } catch (Exception ex) {
            // datePrev reste tel quel si déjà au bon format
        }
        RetourEmprunt dialog = new RetourEmprunt(
                (java.awt.Frame) javax.swing.SwingUtilities.getWindowAncestor(this),
                true, idEmprunt, titre, lecteur, dateEmp, datePrev, statut);
        dialog.setVisible(true);
        chargerEmprunts();
    }
// ── Statut badge arrondi ──────────────────────────────────

    private class StatutRenderer
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
            if (v == null) {
                return cell;
            }

            String val = v.toString();
            java.awt.Color bg, fg;
            String texte;
            switch (val) {
                case "retard":
                    bg = new java.awt.Color(255, 235, 235);
                    fg = new java.awt.Color(200, 50, 50);
                    texte = "En retard";
                    break;
                case "en cours":
                    bg = new java.awt.Color(255, 246, 225);
                    fg = new java.awt.Color(180, 110, 10);
                    texte = "En cours";
                    break;
                case "rendu":
                    bg = new java.awt.Color(230, 249, 239);
                    fg = new java.awt.Color(30, 140, 80);
                    texte = "Rendu";
                    break;
                default:
                    bg = new java.awt.Color(240, 240, 240);
                    fg = new java.awt.Color(90, 96, 112);
                    texte = val;
            }

            javax.swing.JLabel badge
                    = new javax.swing.JLabel(texte) {
                @Override
                protected void paintComponent(
                        java.awt.Graphics g) {
                    java.awt.Graphics2D g2
                            = (java.awt.Graphics2D) g.create();
                    g2.setRenderingHint(
                            java.awt.RenderingHints.KEY_ANTIALIASING,
                            java.awt.RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(getBackground());
                    g2.fillRoundRect(0, 0,
                            getWidth(), getHeight(), 20, 20);
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
                    .createEmptyBorder(4, 14, 4, 14));
            cell.add(badge);
            return cell;
        }
    }

// ── Bouton Retourner ──────────────────────────────────────
    private class RetourBtnRenderer
            implements javax.swing.table.TableCellRenderer {

        @Override
        public java.awt.Component getTableCellRendererComponent(
                javax.swing.JTable t, Object v, boolean sel,
                boolean foc, int r, int c) {

            javax.swing.JPanel panel = new javax.swing.JPanel(
                    new java.awt.GridBagLayout());
            panel.setBackground(r % 2 == 0
                    ? java.awt.Color.WHITE
                    : new java.awt.Color(250, 249, 247));
            if (sel) {
                panel.setBackground(
                        new java.awt.Color(250, 246, 238));
            }

            Object statut = t.getValueAt(r, 4);
            if (statut == null || statut.toString()
                    .equals("rendu")) {
                return panel;
            }

            javax.swing.JButton btn
                    = new javax.swing.JButton("↩ Retourner");
            btn.setFont(new java.awt.Font(
                    "Segoe UI Emoji", java.awt.Font.BOLD, 11));
            btn.setBackground(
                    new java.awt.Color(59, 173, 114));
            btn.setForeground(java.awt.Color.WHITE);
            btn.setPreferredSize(
                    new java.awt.Dimension(100, 30));
            btn.setBorder(javax.swing.BorderFactory
                    .createEmptyBorder(4, 8, 4, 8));
            btn.setFocusPainted(false);
            btn.setCursor(new java.awt.Cursor(
                    java.awt.Cursor.HAND_CURSOR));
            panel.add(btn);
            return panel;
        }
    }

    private class DateRetourCellRenderer
            implements javax.swing.table.TableCellRenderer {

        @Override
        public java.awt.Component getTableCellRendererComponent(
                javax.swing.JTable t, Object v, boolean sel,
                boolean foc, int r, int c) {

            javax.swing.JLabel lbl = new javax.swing.JLabel();
            lbl.setOpaque(true);
            lbl.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
            lbl.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 8, 0, 8));

            // Récupérer le statut de la colonne 4
            Object statutObj = t.getValueAt(r, 4);
            String statut = statutObj != null ? statutObj.toString() : "";

            String valeur = v != null ? v.toString() : "";

            if (valeur.equals("—") || valeur.isEmpty()) {
                // Pas encore rendu
                if (statut.equalsIgnoreCase("en retard")) {
                    lbl.setText("Non rendu");
                    lbl.setFont(new java.awt.Font("Segoe UI",
                            java.awt.Font.ITALIC, 12));
                    lbl.setForeground(new java.awt.Color(220, 80, 80));
                } else {
                    lbl.setText("Non rendu");
                    lbl.setFont(new java.awt.Font("Segoe UI",
                            java.awt.Font.ITALIC, 12));
                    lbl.setForeground(new java.awt.Color(160, 160, 160));
                }
            } else {
                // Livre rendu — afficher la date en vert doux
                lbl.setText(valeur);
                lbl.setFont(new java.awt.Font("Segoe UI",
                        java.awt.Font.PLAIN, 13));
                lbl.setForeground(new java.awt.Color(60, 160, 100));
            }

            // Fond alterné
            if (sel) {
                lbl.setBackground(new java.awt.Color(250, 246, 238));
            } else {
                lbl.setBackground(r % 2 == 0
                        ? java.awt.Color.WHITE
                        : new java.awt.Color(250, 249, 247));
            }

            return lbl;
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

        empruntHeaderPanel = new javax.swing.JPanel();
        lblEmpruntsTitle = new javax.swing.JLabel();
        empruntsHeaderRight = new javax.swing.JPanel();
        btnNouvelEmprunt = new javax.swing.JButton();
        empruntBodyPanel = new javax.swing.JPanel();
        empruntRecherchePanel = new javax.swing.JPanel();
        icoRechercheEmprunt = new javax.swing.JLabel();
        txtRechercheEmprunt = new javax.swing.JTextField();
        cmbStatut = new javax.swing.JComboBox<>();
        empruntTablePanel = new javax.swing.JPanel();
        empruntScrollPane = new javax.swing.JScrollPane();
        empruntsTable = new javax.swing.JTable();

        setBackground(new java.awt.Color(245, 240, 230));
        setLayout(new java.awt.BorderLayout());

        empruntHeaderPanel.setBackground(new java.awt.Color(245, 240, 232));
        empruntHeaderPanel.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(232, 226, 216)), javax.swing.BorderFactory.createEmptyBorder(0, 28, 0, 0)));
        empruntHeaderPanel.setPreferredSize(new java.awt.Dimension(0, 70));
        empruntHeaderPanel.setLayout(new java.awt.BorderLayout());

        lblEmpruntsTitle.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        lblEmpruntsTitle.setForeground(new java.awt.Color(26, 32, 53));
        lblEmpruntsTitle.setText("Emprunts");
        empruntHeaderPanel.add(lblEmpruntsTitle, java.awt.BorderLayout.WEST);

        empruntsHeaderRight.setBackground(new java.awt.Color(245, 240, 232));
        empruntsHeaderRight.setForeground(new java.awt.Color(245, 240, 232));
        empruntsHeaderRight.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT, 28, 18));

        btnNouvelEmprunt.setBackground(new java.awt.Color(212, 168, 67));
        btnNouvelEmprunt.setForeground(new java.awt.Color(26, 32, 53));
        btnNouvelEmprunt.setText("+  Nouvel emprunt");
        btnNouvelEmprunt.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnNouvelEmprunt.setFocusPainted(false);
        btnNouvelEmprunt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNouvelEmpruntActionPerformed(evt);
            }
        });
        empruntsHeaderRight.add(btnNouvelEmprunt);

        empruntHeaderPanel.add(empruntsHeaderRight, java.awt.BorderLayout.EAST);

        add(empruntHeaderPanel, java.awt.BorderLayout.NORTH);

        empruntBodyPanel.setBackground(new java.awt.Color(245, 240, 232));
        empruntBodyPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(20, 28, 24, 28));
        empruntBodyPanel.setLayout(new java.awt.BorderLayout());

        empruntRecherchePanel.setBackground(new java.awt.Color(255, 255, 255));
        empruntRecherchePanel.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(232, 226, 216)), javax.swing.BorderFactory.createEmptyBorder(0, 16, 0, 16)));
        empruntRecherchePanel.setAutoscrolls(true);
        empruntRecherchePanel.setPreferredSize(new java.awt.Dimension(0, 56));
        empruntRecherchePanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 12, 12));

        icoRechercheEmprunt.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/search.png"))); // NOI18N
        empruntRecherchePanel.add(icoRechercheEmprunt);

        txtRechercheEmprunt.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        txtRechercheEmprunt.setForeground(new java.awt.Color(154, 160, 176));
        txtRechercheEmprunt.setText("Rechercher . . .");
        txtRechercheEmprunt.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 8, 0, 0));
        txtRechercheEmprunt.setPreferredSize(new java.awt.Dimension(280, 32));
        empruntRecherchePanel.add(txtRechercheEmprunt);

        cmbStatut.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Tous", "En cours", "Rendu", "Retard", " " }));
        cmbStatut.setPreferredSize(new java.awt.Dimension(150, 32));
        empruntRecherchePanel.add(cmbStatut);

        empruntBodyPanel.add(empruntRecherchePanel, java.awt.BorderLayout.NORTH);

        empruntTablePanel.setBackground(new java.awt.Color(245, 240, 232));
        empruntTablePanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(16, 0, 0, 0));
        empruntTablePanel.setLayout(new java.awt.BorderLayout());

        empruntScrollPane.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(232, 226, 216)));

        empruntsTable.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        empruntsTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "Livre", "Lecteur", "Date emprunt", "Date retour prévue", "Statut", "Date retour réelle", "Actions"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        empruntsTable.setRowHeight(44);
        empruntsTable.setSelectionBackground(new java.awt.Color(250, 246, 238));
        empruntScrollPane.setViewportView(empruntsTable);
        if (empruntsTable.getColumnModel().getColumnCount() > 0) {
            empruntsTable.getColumnModel().getColumn(0).setPreferredWidth(200);
            empruntsTable.getColumnModel().getColumn(1).setPreferredWidth(180);
            empruntsTable.getColumnModel().getColumn(2).setPreferredWidth(120);
            empruntsTable.getColumnModel().getColumn(3).setPreferredWidth(120);
            empruntsTable.getColumnModel().getColumn(4).setPreferredWidth(110);
            empruntsTable.getColumnModel().getColumn(5).setPreferredWidth(100);
        }

        empruntTablePanel.add(empruntScrollPane, java.awt.BorderLayout.CENTER);

        empruntBodyPanel.add(empruntTablePanel, java.awt.BorderLayout.CENTER);

        add(empruntBodyPanel, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void btnNouvelEmpruntActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNouvelEmpruntActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnNouvelEmpruntActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnNouvelEmprunt;
    private javax.swing.JComboBox<String> cmbStatut;
    private javax.swing.JPanel empruntBodyPanel;
    private javax.swing.JPanel empruntHeaderPanel;
    private javax.swing.JPanel empruntRecherchePanel;
    private javax.swing.JScrollPane empruntScrollPane;
    private javax.swing.JPanel empruntTablePanel;
    private javax.swing.JPanel empruntsHeaderRight;
    private javax.swing.JTable empruntsTable;
    private javax.swing.JLabel icoRechercheEmprunt;
    private javax.swing.JLabel lblEmpruntsTitle;
    private javax.swing.JTextField txtRechercheEmprunt;
    // End of variables declaration//GEN-END:variables
}

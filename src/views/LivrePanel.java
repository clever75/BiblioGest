package views;

import dao.LivreDAO;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import models.Livre;

public class LivrePanel extends JPanel {

    private LivreDAO livreDAO = new LivreDAO();
    private ArrayList<Integer> idsLivres = new ArrayList<>();
    private ImageIcon icoModifier;
    private ImageIcon icoSupprimer;
    private ArrayList<Integer> quantitesLivres = new ArrayList<>();

    public LivrePanel() {
        initComponents();
        // ── Bouton état livres ──
        javax.swing.JButton btnEtatLivres = new javax.swing.JButton("📄 État livres");
        btnEtatLivres.setBackground(new java.awt.Color(245, 240, 232));
        btnEtatLivres.setForeground(new java.awt.Color(26, 32, 53));
        btnEtatLivres.setFont(new java.awt.Font("Segoe UI Emoji", java.awt.Font.PLAIN, 12));
        btnEtatLivres.setBorder(javax.swing.BorderFactory.createLineBorder(
                new java.awt.Color(212, 168, 67)));
        btnEtatLivres.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnEtatLivres.setFocusPainted(false);
        btnEtatLivres.addActionListener(e -> utils.EtatsHelper.etatListeLivres());
        livreHeaderRight.add(btnEtatLivres, 0); // 0 = avant btnAjouterLivre
        livreHeaderRight.revalidate();
        btnAjouterLivre.putClientProperty("JButton.buttonType", "roundRect");
        icoModifier = new ImageIcon(getClass().getResource("/Images/edit.png"));
        icoSupprimer = new ImageIcon(getClass().getResource("/Images/delete.png"));

        //Excel
        javax.swing.JButton btnExcelLivres = new javax.swing.JButton("📊 Excel");
        btnExcelLivres.setBackground(new java.awt.Color(33, 115, 70));
        btnExcelLivres.setForeground(java.awt.Color.WHITE);
        btnExcelLivres.setFont(new java.awt.Font("Segoe UI Emoji", java.awt.Font.PLAIN, 12));
        btnExcelLivres.setBorder(javax.swing.BorderFactory.createEmptyBorder(4, 12, 4, 12));
        btnExcelLivres.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnExcelLivres.setFocusPainted(false);
        btnExcelLivres.addActionListener(e -> utils.EtatsHelper.exportExcelLivres());
        livreHeaderRight.add(btnExcelLivres, 0);
        //
        styliserTableau();
        initRenderers();
        initEvenements();
        chargerLivres();
        // Charger les genres dans le filtre
        cmbGenre.removeAllItems();
        cmbGenre.addItem("Tous les genres");
        for (String cat : livreDAO.getToutesLesCategories()) {
            cmbGenre.addItem(cat);
        }

    }

    private void styliserTableau() {
        livresTable.setDefaultEditor(Object.class, null);
        livresTable.setShowVerticalLines(false);
        livresTable.setRowHeight(48);
        livresTable.setIntercellSpacing(new java.awt.Dimension(0, 0));
        livresTable.setSelectionBackground(new java.awt.Color(250, 246, 238));
        livresTable.setSelectionForeground(new java.awt.Color(26, 32, 53));

        livresTable.getTableHeader().setBackground(
                new java.awt.Color(245, 240, 232));
        livresTable.getTableHeader().setFont(
                new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 11));
        livresTable.getTableHeader().setForeground(
                new java.awt.Color(154, 160, 176));
        livresTable.getTableHeader().setPreferredSize(
                new java.awt.Dimension(0, 42));

        // Largeurs colonnes
        livresTable.getColumnModel().getColumn(0).setPreferredWidth(260);
        livresTable.getColumnModel().getColumn(1).setPreferredWidth(180);
        livresTable.getColumnModel().getColumn(2).setPreferredWidth(120);
        livresTable.getColumnModel().getColumn(3).setPreferredWidth(130);
        livresTable.getColumnModel().getColumn(4).setPreferredWidth(120);
    }

    private void initRenderers() {
        // ── Renderer général ─────────────────────────────────────
        livresTable.setDefaultRenderer(Object.class,
                new javax.swing.table.DefaultTableCellRenderer() {
            @Override
            public java.awt.Component getTableCellRendererComponent(
                    javax.swing.JTable t, Object v, boolean sel,
                    boolean foc, int r, int c) {
                super.getTableCellRendererComponent(
                        t, v, sel, foc, r, c);
                // Alignement
                setHorizontalAlignment(c == 0 || c == 1
                        ? LEFT : CENTER);
                // Padding
                setBorder(javax.swing.BorderFactory
                        .createEmptyBorder(0, c == 0 ? 20 : 8, 0,
                                c == 0 ? 8 : 8));
                // Couleurs alternance
                if (sel) {
                    setBackground(new java.awt.Color(250, 246, 238));
                    setForeground(new java.awt.Color(26, 32, 53));
                } else {
                    setBackground(r % 2 == 0
                            ? java.awt.Color.WHITE
                            : new java.awt.Color(250, 249, 247));
                    setForeground(c == 0
                            ? new java.awt.Color(26, 32, 53)
                            : new java.awt.Color(90, 96, 112));
                }
                // Titre en gras
                setFont(new java.awt.Font("Segoe UI",
                        c == 0 ? java.awt.Font.BOLD
                                : java.awt.Font.PLAIN, 13));
                return this;
            }
        });

        // ── Genre badge coloré — colonne 2 ───────────────────────
        livresTable.getColumnModel().getColumn(2)
                .setCellRenderer(new GenreBadgeRenderer());

        // ── Disponibilité X/Y — colonne 3 ────────────────────────
        livresTable.getColumnModel().getColumn(3)
                .setCellRenderer(new DispoBadgeRenderer());

        // ── Actions — colonne 4 ───────────────────────────────────
        livresTable.getColumnModel().getColumn(4)
                .setCellRenderer(new ActionsRenderer());
    }

    private void initEvenements() {
        btnAjouterLivre.addActionListener(e -> {
            AjouterLivre dialog = new AjouterLivre(
                    (java.awt.Frame) SwingUtilities.getWindowAncestor(this),
                    true);
            dialog.setVisible(true);
            chargerLivres();
            rechargerGenres();

        });

        livresTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = livresTable.rowAtPoint(e.getPoint());
                int col = livresTable.columnAtPoint(e.getPoint());
                if (row >= 0 && col == 4) {
                    java.awt.Rectangle rect
                            = livresTable.getCellRect(row, col, false);
                    int relX = e.getX() - rect.x;
                    if (relX < rect.width / 2) {
                        modifierLivre(row);
                    } else {
                        supprimerLivre(row);
                    }
                }
            }
        });

        initRecherche();
        cmbGenre.addActionListener(e -> filtrerParGenre());
    }

    private void rechargerGenres() {
        Object selected = cmbGenre.getSelectedItem();
        cmbGenre.removeAllItems();
        cmbGenre.addItem("Tous les genres");
        for (String cat : livreDAO.getToutesLesCategories()) {
            cmbGenre.addItem(cat);
        }
        // Remettre la sélection précédente si possible
        if (selected != null) {
            cmbGenre.setSelectedItem(selected);
        }
    }

    private void chargerLivres() {
        DefaultTableModel model = (DefaultTableModel) livresTable.getModel();
        model.setRowCount(0);
        idsLivres.clear();
        quantitesLivres.clear();
        ArrayList<Livre> livres = livreDAO.getTousLesLivres();
        for (Livre livre : livres) {
            model.addRow(new Object[]{
                livre.getTitre(), livre.getAuteur(), livre.getCategorie(), livre.getStatut(), ""
            });
            idsLivres.add(livre.getIdLivre());
            quantitesLivres.add(livre.getQuantite());
        }
    }

    private void initRecherche() {
        txtSearch.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (txtSearch.getText().equals("Rechercher un livre . . .")) {
                    txtSearch.setText("");
                    txtSearch.setForeground(new java.awt.Color(26, 32, 53));
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (txtSearch.getText().isEmpty()) {
                    txtSearch.setText("Rechercher un livre . . .");
                    txtSearch.setForeground(new Color(154, 160, 176));
                }
            }
        });
        txtSearch.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                String texte = txtSearch.getText();
                if (!texte.equals("Rechercher un livre . . .")) {
                    rechercherLivres(texte);
                }
            }
        });
    }

    private void rechercherLivres(String texte) {
        DefaultTableModel model = (DefaultTableModel) livresTable.getModel();
        model.setRowCount(0);
        idsLivres.clear();
        ArrayList<Livre> livres = texte.isEmpty()
                ? livreDAO.getTousLesLivres() : livreDAO.rechercher(texte);
        for (Livre l : livres) {
            model.addRow(new Object[]{
                l.getTitre(), l.getAuteur(), l.getCategorie(), l.getStatut(), ""
            });
            idsLivres.add(l.getIdLivre());
        }
    }

    private void filtrerParGenre() {
        Object selected = cmbGenre.getSelectedItem();
        if (selected == null) {
            return;  // ← ajoute cette ligne
        }
        String genre = selected.toString();
        DefaultTableModel model = (DefaultTableModel) livresTable.getModel();
        model.setRowCount(0);
        idsLivres.clear();
        ArrayList<Livre> livres = genre.equals("Tous les genres")
                ? livreDAO.getTousLesLivres() : livreDAO.rechercherParCategorie(genre);
        for (Livre l : livres) {
            model.addRow(new Object[]{
                l.getTitre(), l.getAuteur(), l.getCategorie(), l.getStatut(), ""
            });
            idsLivres.add(l.getIdLivre());
        }
    }

    private void modifierLivre(int row) {
        DefaultTableModel model = (DefaultTableModel) livresTable.getModel();
        int id = idsLivres.get(row);
        String titre = model.getValueAt(row, 0).toString();
        String auteur = model.getValueAt(row, 1).toString();
        String categorie = model.getValueAt(row, 2).toString();
        int quantite = quantitesLivres.get(row);
        ModifierLivre dialog = new ModifierLivre(
                (java.awt.Frame) javax.swing.SwingUtilities.getWindowAncestor(this),
                true, id, titre, auteur, categorie, quantite);
        dialog.setVisible(true);
        chargerLivres();
    }

    private void supprimerLivre(int row) {
        DefaultTableModel model = (DefaultTableModel) livresTable.getModel();
        String titre = model.getValueAt(row, 0).toString();
        int id = idsLivres.get(row);

        // Vérifier si le livre est actuellement emprunté
        if (livreDAO.estEmprunte(id)) {
            javax.swing.JOptionPane.showMessageDialog(this,
                    "Impossible de supprimer \"" + titre + "\".\n"
                    + "Ce livre est actuellement emprunté.",
                    "Suppression impossible",
                    javax.swing.JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = javax.swing.JOptionPane.showConfirmDialog(this,
                "Supprimer le livre \"" + titre + "\" ?", "Confirmation",
                javax.swing.JOptionPane.YES_NO_OPTION);

        if (confirm == javax.swing.JOptionPane.YES_OPTION) {
            if (livreDAO.supprimer(id)) {
                model.removeRow(row);
                idsLivres.remove(row);
            } else {
                javax.swing.JOptionPane.showMessageDialog(this,
                        "Erreur lors de la suppression.",
                        "Erreur", javax.swing.JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    // ── Renderer Genre badge ──────────────────────────────────────

    private class GenreBadgeRenderer
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

            String genre = v.toString();
            java.awt.Color bg, fg;
            switch (genre.toLowerCase()) {
                case "roman":
                    bg = new java.awt.Color(232, 240, 255);
                    fg = new java.awt.Color(60, 90, 180);
                    break;
                case "histoire":
                    bg = new java.awt.Color(255, 243, 220);
                    fg = new java.awt.Color(160, 100, 20);
                    break;
                case "science":
                    bg = new java.awt.Color(225, 245, 254);
                    fg = new java.awt.Color(20, 120, 160);
                    break;
                case "contes":
                    bg = new java.awt.Color(243, 229, 255);
                    fg = new java.awt.Color(110, 50, 170);
                    break;
                case "art":
                    bg = new java.awt.Color(255, 236, 230);
                    fg = new java.awt.Color(180, 60, 40);
                    break;
                default:
                    bg = new java.awt.Color(240, 240, 240);
                    fg = new java.awt.Color(90, 96, 112);
            }
            cell.add(creerBadge(genre, bg, fg));
            return cell;
        }
    }

// ── Renderer Disponibilité X/Y ────────────────────────────────
    private class DispoBadgeRenderer
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

            int dispo = 0, total = 0;
            try {
                if (r < idsLivres.size()) {
                    int[] dt = livreDAO.getDispoEtTotal(idsLivres.get(r));
                    dispo = dt[0];
                    total = dt[1];
                }
            } catch (Exception ex) {
            }

            java.awt.Color bg, fg;
            if (dispo == 0) {
                bg = new java.awt.Color(255, 235, 235);
                fg = new java.awt.Color(200, 50, 50);
            } else if (dispo < total) {
                bg = new java.awt.Color(255, 246, 225);
                fg = new java.awt.Color(180, 110, 10);
            } else {
                bg = new java.awt.Color(230, 249, 239);
                fg = new java.awt.Color(30, 140, 80);
            }
            cell.add(creerBadge(dispo + "/" + total + " ex.", bg, fg));
            return cell;
        }
    }

// ── Renderer Actions ──────────────────────────────────────────
    private class ActionsRenderer
            implements javax.swing.table.TableCellRenderer {

        @Override
        public java.awt.Component getTableCellRendererComponent(
                javax.swing.JTable t, Object v, boolean sel,
                boolean foc, int r, int c) {

            javax.swing.JPanel panel = new javax.swing.JPanel(
                    new java.awt.FlowLayout(
                            java.awt.FlowLayout.CENTER, 8, 9));
            panel.setBackground(r % 2 == 0
                    ? java.awt.Color.WHITE
                    : new java.awt.Color(250, 249, 247));
            if (sel) {
                panel.setBackground(
                        new java.awt.Color(250, 246, 238));
            }

            javax.swing.JButton btnMod = new javax.swing.JButton();
            btnMod.setIcon(icoModifier);
            btnMod.setPreferredSize(new java.awt.Dimension(32, 32));
            btnMod.setBorder(
                    javax.swing.BorderFactory.createEmptyBorder());
            btnMod.setBackground(new java.awt.Color(240, 235, 224));
            btnMod.setFocusPainted(false);
            btnMod.setCursor(new java.awt.Cursor(
                    java.awt.Cursor.HAND_CURSOR));
            btnMod.setToolTipText("Modifier");

            javax.swing.JButton btnSup = new javax.swing.JButton();
            btnSup.setIcon(icoSupprimer);
            btnSup.setPreferredSize(new java.awt.Dimension(32, 32));
            btnSup.setBorder(
                    javax.swing.BorderFactory.createEmptyBorder());
            btnSup.setBackground(new java.awt.Color(255, 240, 240));
            btnSup.setFocusPainted(false);
            btnSup.setCursor(new java.awt.Cursor(
                    java.awt.Cursor.HAND_CURSOR));
            btnSup.setToolTipText("Supprimer");

            panel.add(btnMod);
            panel.add(btnSup);
            return panel;
        }
    }

// ── Utilitaire badge arrondi ──────────────────────────────────
    private javax.swing.JLabel creerBadge(String texte,
            java.awt.Color bg, java.awt.Color fg) {
        javax.swing.JLabel badge = new javax.swing.JLabel(texte) {
            @Override
            protected void paintComponent(java.awt.Graphics g) {
                java.awt.Graphics2D g2
                        = (java.awt.Graphics2D) g.create();
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
                .createEmptyBorder(3, 14, 3, 14));
        return badge;
    }

    //Méthode pour charger les livres
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        livreHeaderPanel = new javax.swing.JPanel();
        lblLivresTitre = new javax.swing.JLabel();
        livreHeaderRight = new javax.swing.JPanel();
        btnAjouterLivre = new javax.swing.JButton();
        livreBodyPanel = new javax.swing.JPanel();
        recherchePanel = new javax.swing.JPanel();
        icoRecherche = new javax.swing.JLabel();
        txtSearch = new javax.swing.JTextField();
        cmbGenre = new javax.swing.JComboBox<>();
        livreTablePanel = new javax.swing.JPanel();
        livreScrollPane = new javax.swing.JScrollPane();
        livresTable = new javax.swing.JTable();

        setBackground(new java.awt.Color(245, 240, 232));
        setLayout(new java.awt.BorderLayout());

        livreHeaderPanel.setBackground(new java.awt.Color(245, 240, 232));
        livreHeaderPanel.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(232, 226, 216)), javax.swing.BorderFactory.createEmptyBorder(0, 28, 0, 28)));
        livreHeaderPanel.setPreferredSize(new java.awt.Dimension(0, 70));
        livreHeaderPanel.setLayout(new java.awt.BorderLayout());

        lblLivresTitre.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        lblLivresTitre.setForeground(new java.awt.Color(26, 32, 53));
        lblLivresTitre.setText("Livres");
        livreHeaderPanel.add(lblLivresTitre, java.awt.BorderLayout.WEST);

        livreHeaderRight.setBackground(new java.awt.Color(245, 240, 232));
        livreHeaderRight.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT, 28, 18));

        btnAjouterLivre.setBackground(new java.awt.Color(212, 168, 67));
        btnAjouterLivre.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnAjouterLivre.setForeground(new java.awt.Color(26, 32, 53));
        btnAjouterLivre.setText("+ Ajouter un livre");
        btnAjouterLivre.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnAjouterLivre.setFocusPainted(false);
        livreHeaderRight.add(btnAjouterLivre);

        livreHeaderPanel.add(livreHeaderRight, java.awt.BorderLayout.CENTER);

        add(livreHeaderPanel, java.awt.BorderLayout.NORTH);

        livreBodyPanel.setBackground(new java.awt.Color(245, 240, 232));
        livreBodyPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(20, 28, 24, 28));
        livreBodyPanel.setLayout(new java.awt.BorderLayout());

        recherchePanel.setBackground(new java.awt.Color(255, 255, 255));
        recherchePanel.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(232, 226, 216)), javax.swing.BorderFactory.createEmptyBorder(0, 16, 0, 16)));
        recherchePanel.setMaximumSize(new java.awt.Dimension(32767, 56));
        recherchePanel.setPreferredSize(new java.awt.Dimension(0, 56));
        recherchePanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 12, 12));

        icoRecherche.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/search.png"))); // NOI18N
        recherchePanel.add(icoRecherche);

        txtSearch.setColumns(20);
        txtSearch.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        txtSearch.setForeground(new java.awt.Color(154, 160, 176));
        txtSearch.setText("Rechercher un livre . . .");
        txtSearch.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 8, 0, 0));
        recherchePanel.add(txtSearch);

        cmbGenre.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Tous les genres", "Roman", "Science", "Histoire", "Art", " " }));
        cmbGenre.setPreferredSize(new java.awt.Dimension(150, 32));
        recherchePanel.add(cmbGenre);

        livreBodyPanel.add(recherchePanel, java.awt.BorderLayout.NORTH);

        livreTablePanel.setBackground(new java.awt.Color(245, 240, 232));
        livreTablePanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(16, 0, 0, 0));
        livreTablePanel.setLayout(new java.awt.BorderLayout());

        livreScrollPane.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(232, 226, 216)));

        livresTable.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        livresTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Titre", "Auteur", "Genre", "Disponible", "Actions"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        livresTable.setGridColor(new java.awt.Color(232, 226, 216));
        livresTable.setRowHeight(44);
        livresTable.setSelectionBackground(new java.awt.Color(250, 246, 238));
        livresTable.setSelectionForeground(new java.awt.Color(26, 32, 53));
        livreScrollPane.setViewportView(livresTable);
        if (livresTable.getColumnModel().getColumnCount() > 0) {
            livresTable.getColumnModel().getColumn(0).setPreferredWidth(250);
            livresTable.getColumnModel().getColumn(1).setPreferredWidth(180);
            livresTable.getColumnModel().getColumn(2).setPreferredWidth(120);
            livresTable.getColumnModel().getColumn(3).setPreferredWidth(100);
            livresTable.getColumnModel().getColumn(4).setPreferredWidth(120);
        }

        livreTablePanel.add(livreScrollPane, java.awt.BorderLayout.CENTER);

        livreBodyPanel.add(livreTablePanel, java.awt.BorderLayout.CENTER);

        add(livreBodyPanel, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAjouterLivre;
    private javax.swing.JComboBox<String> cmbGenre;
    private javax.swing.JLabel icoRecherche;
    private javax.swing.JLabel lblLivresTitre;
    private javax.swing.JPanel livreBodyPanel;
    private javax.swing.JPanel livreHeaderPanel;
    private javax.swing.JPanel livreHeaderRight;
    private javax.swing.JScrollPane livreScrollPane;
    private javax.swing.JPanel livreTablePanel;
    private javax.swing.JTable livresTable;
    private javax.swing.JPanel recherchePanel;
    private javax.swing.JTextField txtSearch;
    // End of variables declaration//GEN-END:variables

}

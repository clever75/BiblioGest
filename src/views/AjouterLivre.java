/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package views;

import dao.LivreDAO;
import models.Livre;

/**
 *
 * @author Admin
 */
public class AjouterLivre extends javax.swing.JDialog {

    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(AjouterLivre.class.getName());

    /**
     * Creates new form AjouterLivre
     */
    private LivreDAO livreDAO = new LivreDAO();
    private int quantite = 1;

    public AjouterLivre(java.awt.Frame parent, boolean modal) {
        super(parent, true);
        initComponents();
        setLocationRelativeTo(parent);

        initStyles();
        initQuantite();
        initApercu();
        initAutoComplete();
        initEvenements();
    }

    private void initStyles() {
        btnEnregistrer.putClientProperty("JButton.buttonType", "roundRect");
        btnAnnuler.putClientProperty("JButton.buttonType", "roundRect");
        btnEnregistrer.setText("+ Ajouter le livre");
        lblAperçuTitre.setText("—");
        lblAperçuAuteur.setText("—");
        lblAperçuCat.setText("—");
        lblAperçuQty.setText("1 exemplaire");
        // Bordures arrondies sur les badges aperçu
        lblAperçuCat.setBorder(javax.swing.BorderFactory
                .createEmptyBorder(2, 10, 2, 10));
        lblAperçuQty.setBorder(javax.swing.BorderFactory
                .createEmptyBorder(2, 10, 2, 10));

        // Focus doré sur les champs
        ajouterFocusDore(txtTitre);
        ajouterFocusDore(txtAuteur);
        // Charger les catégories depuis la BD
        cmbCategorie.removeAllItems();
        java.util.ArrayList<String> cats = livreDAO.getToutesLesCategories();

// Catégories par défaut si BD vide
        if (cats.isEmpty()) {
            cats.add("Roman");
            cats.add("Science");
            cats.add("Histoire");
            cats.add("Art");
            cats.add("Contes");
        }
        for (String cat : cats) {
            cmbCategorie.addItem(cat);
        }
    }

    private void ajouterFocusDore(javax.swing.JTextField field) {
        field.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusGained(java.awt.event.FocusEvent e) {
                field.setBorder(javax.swing.BorderFactory.createCompoundBorder(
                        javax.swing.BorderFactory.createLineBorder(
                                new java.awt.Color(212, 168, 67), 1),
                        javax.swing.BorderFactory.createEmptyBorder(0, 6, 0, 6)));
            }

            @Override
            public void focusLost(java.awt.event.FocusEvent e) {
                field.setBorder(javax.swing.BorderFactory.createCompoundBorder(
                        javax.swing.BorderFactory.createLineBorder(
                                new java.awt.Color(232, 226, 216), 1),
                        javax.swing.BorderFactory.createEmptyBorder(0, 6, 0, 6)));
            }
        });
        // Bordure initiale
        field.setBorder(javax.swing.BorderFactory.createCompoundBorder(
                javax.swing.BorderFactory.createLineBorder(
                        new java.awt.Color(232, 226, 216), 1),
                javax.swing.BorderFactory.createEmptyBorder(0, 6, 0, 6)));
    }

    private void initQuantite() {
        // Remplacer le spinner par les boutons +/-
        panelQuantite.remove(txtQuantite);

        javax.swing.JPanel qtyPanel = new javax.swing.JPanel(
                new java.awt.BorderLayout());
        qtyPanel.setBackground(java.awt.Color.WHITE);
        qtyPanel.setBorder(javax.swing.BorderFactory.createLineBorder(
                new java.awt.Color(232, 226, 216)));
        qtyPanel.setMaximumSize(new java.awt.Dimension(32767, 40));
        qtyPanel.setAlignmentX(0.0f);

        javax.swing.JButton btnMoins = new javax.swing.JButton("−");
        btnMoins.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 16));
        btnMoins.setBackground(new java.awt.Color(245, 240, 232));
        btnMoins.setForeground(new java.awt.Color(26, 32, 53));
        btnMoins.setBorder(javax.swing.BorderFactory.createEmptyBorder());
        btnMoins.setFocusPainted(false);
        btnMoins.setPreferredSize(new java.awt.Dimension(40, 0));
        btnMoins.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        javax.swing.JLabel lblQty = new javax.swing.JLabel("1",
                javax.swing.SwingConstants.CENTER);
        lblQty.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 14));
        lblQty.setForeground(new java.awt.Color(26, 32, 53));

        javax.swing.JButton btnPlus = new javax.swing.JButton("+");
        btnPlus.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 16));
        btnPlus.setBackground(new java.awt.Color(245, 240, 232));
        btnPlus.setForeground(new java.awt.Color(26, 32, 53));
        btnPlus.setBorder(javax.swing.BorderFactory.createEmptyBorder());
        btnPlus.setFocusPainted(false);
        btnPlus.setPreferredSize(new java.awt.Dimension(40, 0));
        btnPlus.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        btnMoins.addActionListener(e -> {
            if (quantite > 1) {
                quantite--;
                lblQty.setText(String.valueOf(quantite));
                majApercu();
            } else {
                btnMoins.setEnabled(false);
            }
        });
        btnPlus.addActionListener(e -> {
            if (quantite < 100) {
                quantite++;
                lblQty.setText(String.valueOf(quantite));
                btnMoins.setEnabled(true);
                majApercu();
            }
        });

        qtyPanel.add(btnMoins, java.awt.BorderLayout.WEST);
        qtyPanel.add(lblQty, java.awt.BorderLayout.CENTER);
        qtyPanel.add(btnPlus, java.awt.BorderLayout.EAST);
        panelQuantite.add(qtyPanel);
        panelQuantite.revalidate();
    }

    private void initApercu() {
        majApercu();
    }

    private void majApercu() {
        String titre = txtTitre.getText().trim();
        String auteur = txtAuteur.getText().trim();
        Object catObj = cmbCategorie.getSelectedItem();
        String cat = catObj != null ? catObj.toString().trim() : "";

        lblAperçuTitre.setText(titre.isEmpty() ? "—" : titre);
        lblAperçuAuteur.setText(auteur.isEmpty() ? "—" : auteur);
        lblAperçuQty.setText(quantite + (quantite > 1
                ? " exemplaires" : " exemplaire"));

        // Badge genre coloré
        java.awt.Color bg, fg;
        switch (cat.toLowerCase()) {
            case "roman":
                bg = new java.awt.Color(232, 240, 255);
                fg = new java.awt.Color(60, 90, 180);
                break;
            case "science":
                bg = new java.awt.Color(225, 245, 254);
                fg = new java.awt.Color(20, 120, 160);
                break;
            case "histoire":
                bg = new java.awt.Color(255, 243, 220);
                fg = new java.awt.Color(160, 100, 20);
                break;
            case "art":
                bg = new java.awt.Color(255, 236, 230);
                fg = new java.awt.Color(180, 60, 40);
                break;
            case "contes":
                bg = new java.awt.Color(243, 229, 255);
                fg = new java.awt.Color(110, 50, 170);
                break;
            default:
                bg = new java.awt.Color(240, 240, 240);
                fg = new java.awt.Color(154, 160, 176);
                cat = "—";
        }
        lblAperçuCat.setText(cat.isEmpty() ? "—" : cat);
        lblAperçuCat.setBackground(bg);
        lblAperçuCat.setForeground(fg);
    }

    private void initAutoComplete() {
        // ComboBox avec autocomplétion — quand on tape ça filtre
        javax.swing.JTextField editor
                = (javax.swing.JTextField) cmbCategorie.getEditor()
                        .getEditorComponent();

        editor.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyReleased(java.awt.event.KeyEvent e) {
                // Ignorer les touches de navigation
                int code = e.getKeyCode();
                if (code == java.awt.event.KeyEvent.VK_ENTER
                        || code == java.awt.event.KeyEvent.VK_ESCAPE
                        || code == java.awt.event.KeyEvent.VK_UP
                        || code == java.awt.event.KeyEvent.VK_DOWN) {
                    majApercu();
                    return;
                }

                String texte = editor.getText(); // garder la casse originale
                String texteLower = texte.toLowerCase();
                java.util.ArrayList<String> catsList = livreDAO.getToutesLesCategories();
                if (catsList.isEmpty()) {
                    catsList.add("Roman");
                    catsList.add("Science");
                    catsList.add("Histoire");
                    catsList.add("Art");
                    catsList.add("Contes");
                }
                String[] items = catsList.toArray(new String[0]);
                cmbCategorie.hidePopup();
                cmbCategorie.removeAllItems();
                for (String item : items) {
                    if (item.toLowerCase().startsWith(texteLower)) {
                        cmbCategorie.addItem(item);
                    }
                }

                if (cmbCategorie.getItemCount() > 0) {
                    cmbCategorie.showPopup();
                }

                // Remettre le texte tapé sans déclencher l'action
                editor.setText(texte);
                editor.setCaretPosition(texte.length());
// Aperçu direct avec le texte tapé
                lblAperçuCat.setText(texte.isEmpty() ? "—" : texte);
                majApercuAvecCat(texte);
            }
        });
    }

    private void initEvenements() {
        btnAnnuler.addActionListener(e -> dispose());
        btnEnregistrer.addActionListener(e -> ajouter());

        // Aperçu en temps réel
        txtTitre.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyReleased(java.awt.event.KeyEvent e) {
                majApercu();
                verifierDoublon();
            }
        });
        txtAuteur.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyReleased(java.awt.event.KeyEvent e) {
                verifierDoublon();
            }
        });
        cmbCategorie.addActionListener(e -> majApercu());
    }

    private void verifierDoublon() {
        String titre = txtTitre.getText().trim();
        String auteur = txtAuteur.getText().trim();
        if (titre.length() >= 3 && auteur.length() >= 3) {
            boolean existe = livreDAO.livreExisteDeja(titre, auteur);
            // Colorier le border du titre en rouge si doublon
            if (existe) {
                txtTitre.setBorder(javax.swing.BorderFactory.createCompoundBorder(
                        javax.swing.BorderFactory.createLineBorder(
                                new java.awt.Color(224, 82, 82), 1),
                        javax.swing.BorderFactory.createEmptyBorder(0, 6, 0, 6)));
                txtTitre.setToolTipText(
                        "⚠ Ce livre existe déjà !");
            } else {
                txtTitre.setBorder(javax.swing.BorderFactory.createCompoundBorder(
                        javax.swing.BorderFactory.createLineBorder(
                                new java.awt.Color(232, 226, 216), 1),
                        javax.swing.BorderFactory.createEmptyBorder(0, 6, 0, 6)));
                txtTitre.setToolTipText(null);
            }
        }
    }

    private void ajouter() {
        String titre = txtTitre.getText().trim();
        String auteur = txtAuteur.getText().trim();
        Object catObj = cmbCategorie.getSelectedItem();
        String categorie = catObj != null ? catObj.toString().trim() : "";

        if (titre.isEmpty()) {
            javax.swing.JOptionPane.showMessageDialog(this,
                    "Le titre est obligatoire.", "Erreur",
                    javax.swing.JOptionPane.ERROR_MESSAGE);
            txtTitre.requestFocus();
            return;
        }
        if (titre.length() < 2) {
    javax.swing.JOptionPane.showMessageDialog(this,
        "Le titre doit contenir au moins 2 caractères !",
        "Erreur", javax.swing.JOptionPane.ERROR_MESSAGE);
    txtTitre.requestFocus();
    return;
}
        if (auteur.isEmpty()) {
            javax.swing.JOptionPane.showMessageDialog(this,
                    "L'auteur est obligatoire.", "Erreur",
                    javax.swing.JOptionPane.ERROR_MESSAGE);
            txtAuteur.requestFocus();
            return;
        }
        if (!auteur.matches("[a-zA-ZÀ-ÿ\\s\\-'.]+")) {
    javax.swing.JOptionPane.showMessageDialog(this,
        "Le nom de l'auteur ne doit contenir que des lettres !",
        "Erreur", javax.swing.JOptionPane.ERROR_MESSAGE);
    txtAuteur.requestFocus();
    return;
}
        if (categorie.isEmpty() || categorie.equals(" ")) {
            javax.swing.JOptionPane.showMessageDialog(this,
                    "La catégorie est obligatoire.", "Erreur",
                    javax.swing.JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Vérifier doublon
        if (livreDAO.livreExisteDeja(titre, auteur)) {
            int rep = javax.swing.JOptionPane.showConfirmDialog(this,
                    "⚠ \"" + titre + "\" de " + auteur
                    + " existe déjà.\nVoulez-vous quand même ajouter ?",
                    "Doublon détecté",
                    javax.swing.JOptionPane.YES_NO_OPTION,
                    javax.swing.JOptionPane.WARNING_MESSAGE);
            if (rep != javax.swing.JOptionPane.YES_OPTION) {
                return;
            }
        }
        if (quantite < 1) {
            javax.swing.JOptionPane.showMessageDialog(this,
                    "La quantité doit être au moins 1.",
                    "Erreur", javax.swing.JOptionPane.ERROR_MESSAGE);
            return;
        }

        int confirm = javax.swing.JOptionPane.showConfirmDialog(this,
    "Confirmer l'ajout de « " + titre + " » (" + quantite + " exemplaire(s)) ?",
    "Confirmation",
    javax.swing.JOptionPane.YES_NO_OPTION,
    javax.swing.JOptionPane.QUESTION_MESSAGE);
if (confirm != javax.swing.JOptionPane.YES_OPTION) return;
        if (livreDAO.ajouter(new Livre(titre, auteur, categorie, quantite))) {
            javax.swing.JOptionPane.showMessageDialog(this,
                    "✓ Livre ajouté avec succès !");
            dispose();
        } else {
            javax.swing.JOptionPane.showMessageDialog(this,
                    "Erreur lors de l'ajout.", "Erreur",
                    javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    private void majApercuAvecCat(String cat) {
        String titre = txtTitre.getText().trim();
        String auteur = txtAuteur.getText().trim();

        lblAperçuTitre.setText(titre.isEmpty() ? "—" : titre);
        lblAperçuAuteur.setText(auteur.isEmpty() ? "—" : auteur);
        lblAperçuQty.setText(quantite + (quantite > 1
                ? " exemplaires" : " exemplaire"));

        java.awt.Color bg, fg;
        switch (cat.toLowerCase()) {
            case "roman":
                bg = new java.awt.Color(232, 240, 255);
                fg = new java.awt.Color(60, 90, 180);
                break;
            case "science":
                bg = new java.awt.Color(225, 245, 254);
                fg = new java.awt.Color(20, 120, 160);
                break;
            case "histoire":
                bg = new java.awt.Color(255, 243, 220);
                fg = new java.awt.Color(160, 100, 20);
                break;
            case "art":
                bg = new java.awt.Color(255, 236, 230);
                fg = new java.awt.Color(180, 60, 40);
                break;
            case "contes":
                bg = new java.awt.Color(243, 229, 255);
                fg = new java.awt.Color(110, 50, 170);
                break;
            default:
                bg = new java.awt.Color(240, 240, 240);
                fg = new java.awt.Color(154, 160, 176);
                cat = cat.isEmpty() ? "—" : cat;
        }
        lblAperçuCat.setText(cat);
        lblAperçuCat.setBackground(bg);
        lblAperçuCat.setForeground(fg);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        titlePanel = new javax.swing.JPanel();
        lblTitle = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        formPanel = new javax.swing.JPanel();
        ligne1Panel = new javax.swing.JPanel();
        panelTitre = new javax.swing.JPanel();
        lblTitre = new javax.swing.JLabel();
        txtTitre = new javax.swing.JTextField();
        panelAuteur = new javax.swing.JPanel();
        lblAuteur = new javax.swing.JLabel();
        txtAuteur = new javax.swing.JTextField();
        panelSep1 = new javax.swing.JPanel();
        ligne2Panel = new javax.swing.JPanel();
        panelCategorie = new javax.swing.JPanel();
        lblCategorie = new javax.swing.JLabel();
        cmbCategorie = new javax.swing.JComboBox<>();
        panelQuantite = new javax.swing.JPanel();
        lblQuantite = new javax.swing.JLabel();
        txtQuantite = new javax.swing.JSpinner();
        panelSep2 = new javax.swing.JPanel();
        sep1 = new javax.swing.JSeparator();
        panelSep3 = new javax.swing.JPanel();
        lblAperçu = new javax.swing.JLabel();
        aperçuPanel = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        panelInfo = new javax.swing.JPanel();
        lblAperçuTitre = new javax.swing.JLabel();
        lblAperçuAuteur = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        lblAperçuCat = new javax.swing.JLabel();
        lblAperçuQty = new javax.swing.JLabel();
        buttonsPanel = new javax.swing.JPanel();
        btnAnnuler = new javax.swing.JButton();
        btnEnregistrer = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Ajouter livre");
        setPreferredSize(new java.awt.Dimension(620, 500));
        setSize(new java.awt.Dimension(620, 500));

        titlePanel.setBackground(new java.awt.Color(36, 32, 53));
        titlePanel.setPreferredSize(new java.awt.Dimension(0, 58));
        titlePanel.setLayout(new java.awt.BorderLayout());

        lblTitle.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        lblTitle.setForeground(new java.awt.Color(255, 255, 255));
        lblTitle.setText("Ajouter un livre");
        lblTitle.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 16, 0, 0));
        titlePanel.add(lblTitle, java.awt.BorderLayout.CENTER);

        jPanel4.setBackground(new java.awt.Color(212, 168, 67));
        jPanel4.setPreferredSize(new java.awt.Dimension(4, 0));
        titlePanel.add(jPanel4, java.awt.BorderLayout.WEST);

        getContentPane().add(titlePanel, java.awt.BorderLayout.NORTH);

        formPanel.setBackground(new java.awt.Color(245, 240, 232));
        formPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(24, 28, 16, 28));
        formPanel.setLayout(new javax.swing.BoxLayout(formPanel, javax.swing.BoxLayout.Y_AXIS));

        ligne1Panel.setBackground(new java.awt.Color(245, 240, 232));
        ligne1Panel.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        ligne1Panel.setAlignmentX(0.0F);
        ligne1Panel.setMaximumSize(new java.awt.Dimension(32767, 66));
        ligne1Panel.setPreferredSize(new java.awt.Dimension(0, 66));
        ligne1Panel.setLayout(new java.awt.GridLayout(1, 2, 16, 0));

        panelTitre.setBackground(new java.awt.Color(245, 240, 232));
        panelTitre.setLayout(new javax.swing.BoxLayout(panelTitre, javax.swing.BoxLayout.Y_AXIS));

        lblTitre.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblTitre.setForeground(new java.awt.Color(154, 160, 176));
        lblTitre.setText("TITRE *");
        lblTitre.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 6, 0));
        panelTitre.add(lblTitre);

        txtTitre.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        txtTitre.setAlignmentX(0.0F);
        txtTitre.setMaximumSize(new java.awt.Dimension(32767, 40));
        txtTitre.setPreferredSize(new java.awt.Dimension(0, 40));
        panelTitre.add(txtTitre);

        ligne1Panel.add(panelTitre);

        panelAuteur.setBackground(new java.awt.Color(245, 240, 232));
        panelAuteur.setLayout(new javax.swing.BoxLayout(panelAuteur, javax.swing.BoxLayout.Y_AXIS));

        lblAuteur.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblAuteur.setForeground(new java.awt.Color(154, 160, 176));
        lblAuteur.setText("AUTEUR *");
        lblAuteur.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 6, 0));
        panelAuteur.add(lblAuteur);

        txtAuteur.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        txtAuteur.setAlignmentX(0.0F);
        txtAuteur.setMaximumSize(new java.awt.Dimension(32767, 40));
        txtAuteur.setPreferredSize(new java.awt.Dimension(0, 40));
        panelAuteur.add(txtAuteur);

        ligne1Panel.add(panelAuteur);

        formPanel.add(ligne1Panel);

        panelSep1.setBackground(new java.awt.Color(245, 240, 232));
        panelSep1.setAlignmentX(0.0F);
        panelSep1.setMaximumSize(new java.awt.Dimension(32767, 14));
        formPanel.add(panelSep1);

        ligne2Panel.setBackground(new java.awt.Color(245, 240, 232));
        ligne2Panel.setAlignmentX(0.0F);
        ligne2Panel.setMaximumSize(new java.awt.Dimension(32767, 66));
        ligne2Panel.setPreferredSize(new java.awt.Dimension(0, 66));
        ligne2Panel.setLayout(new java.awt.GridLayout(1, 2, 16, 0));

        panelCategorie.setBackground(new java.awt.Color(245, 240, 232));
        panelCategorie.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        panelCategorie.setLayout(new javax.swing.BoxLayout(panelCategorie, javax.swing.BoxLayout.Y_AXIS));

        lblCategorie.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblCategorie.setForeground(new java.awt.Color(154, 160, 176));
        lblCategorie.setText("CATÉGORIE *");
        lblCategorie.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 6, 0));
        panelCategorie.add(lblCategorie);

        cmbCategorie.setEditable(true);
        cmbCategorie.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Roman", "Science", "Art", " " }));
        cmbCategorie.setAlignmentX(0.0F);
        cmbCategorie.setMaximumSize(new java.awt.Dimension(32767, 40));
        cmbCategorie.setPreferredSize(new java.awt.Dimension(72, 40));
        cmbCategorie.addActionListener(this::cmbCategorieActionPerformed);
        panelCategorie.add(cmbCategorie);

        ligne2Panel.add(panelCategorie);

        panelQuantite.setBackground(new java.awt.Color(245, 240, 232));
        panelQuantite.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        panelQuantite.setLayout(new javax.swing.BoxLayout(panelQuantite, javax.swing.BoxLayout.Y_AXIS));

        lblQuantite.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblQuantite.setForeground(new java.awt.Color(154, 160, 176));
        lblQuantite.setText("QUANTITÉ *");
        lblQuantite.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 6, 0));
        panelQuantite.add(lblQuantite);

        txtQuantite.setAlignmentX(0.0F);
        txtQuantite.setMaximumSize(new java.awt.Dimension(32767, 40));
        txtQuantite.setPreferredSize(new java.awt.Dimension(64, 40));
        panelQuantite.add(txtQuantite);

        ligne2Panel.add(panelQuantite);

        formPanel.add(ligne2Panel);

        panelSep2.setBackground(new java.awt.Color(245, 240, 232));
        panelSep2.setAlignmentX(0.0F);
        panelSep2.setMaximumSize(new java.awt.Dimension(32767, 14));
        formPanel.add(panelSep2);

        sep1.setForeground(new java.awt.Color(232, 226, 216));
        sep1.setAlignmentX(0.0F);
        sep1.setMaximumSize(new java.awt.Dimension(32767, 1));
        formPanel.add(sep1);

        panelSep3.setBackground(new java.awt.Color(245, 240, 232));
        panelSep3.setAlignmentX(0.0F);
        panelSep3.setMaximumSize(new java.awt.Dimension(32767, 14));
        formPanel.add(panelSep3);

        lblAperçu.setFont(new java.awt.Font("Segoe UI", 1, 10)); // NOI18N
        lblAperçu.setForeground(new java.awt.Color(154, 160, 176));
        lblAperçu.setText("APERÇU");
        lblAperçu.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 8, 0));
        formPanel.add(lblAperçu);

        aperçuPanel.setBackground(new java.awt.Color(255, 255, 255));
        aperçuPanel.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(232, 226, 216)), javax.swing.BorderFactory.createEmptyBorder(12, 14, 12, 14)));
        aperçuPanel.setAlignmentX(0.0F);
        aperçuPanel.setMaximumSize(new java.awt.Dimension(32767, 68));
        aperçuPanel.setPreferredSize(new java.awt.Dimension(0, 68));
        aperçuPanel.setLayout(new java.awt.BorderLayout());

        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/open-book.png"))); // NOI18N
        jLabel2.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 14));
        aperçuPanel.add(jLabel2, java.awt.BorderLayout.WEST);

        panelInfo.setBackground(new java.awt.Color(255, 255, 255));
        panelInfo.setLayout(new javax.swing.BoxLayout(panelInfo, javax.swing.BoxLayout.Y_AXIS));

        lblAperçuTitre.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        lblAperçuTitre.setForeground(new java.awt.Color(26, 32, 53));
        lblAperçuTitre.setText("jLabel3");
        panelInfo.add(lblAperçuTitre);

        lblAperçuAuteur.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        lblAperçuAuteur.setForeground(new java.awt.Color(154, 160, 176));
        lblAperçuAuteur.setText("jLabel4");
        panelInfo.add(lblAperçuAuteur);

        aperçuPanel.add(panelInfo, java.awt.BorderLayout.CENTER);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT));

        lblAperçuCat.setBackground(new java.awt.Color(232, 240, 255));
        lblAperçuCat.setFont(new java.awt.Font("Segoe UI", 1, 11)); // NOI18N
        lblAperçuCat.setForeground(new java.awt.Color(60, 90, 180));
        lblAperçuCat.setText("jLabel3");
        lblAperçuCat.setOpaque(true);
        jPanel1.add(lblAperçuCat);

        lblAperçuQty.setBackground(new java.awt.Color(230, 249, 239));
        lblAperçuQty.setFont(new java.awt.Font("Segoe UI", 1, 11)); // NOI18N
        lblAperçuQty.setForeground(new java.awt.Color(30, 140, 80));
        lblAperçuQty.setText("1.ex");
        lblAperçuQty.setOpaque(true);
        jPanel1.add(lblAperçuQty);

        aperçuPanel.add(jPanel1, java.awt.BorderLayout.EAST);

        formPanel.add(aperçuPanel);

        getContentPane().add(formPanel, java.awt.BorderLayout.CENTER);

        buttonsPanel.setBackground(new java.awt.Color(245, 240, 232));
        buttonsPanel.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 0, 0, 0, new java.awt.Color(232, 226, 216)));
        buttonsPanel.setPreferredSize(new java.awt.Dimension(0, 60));
        buttonsPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT, 12, 16));

        btnAnnuler.setBackground(new java.awt.Color(232, 226, 216));
        btnAnnuler.setForeground(new java.awt.Color(90, 96, 112));
        btnAnnuler.setText("Annuler");
        btnAnnuler.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnAnnuler.setFocusPainted(false);
        buttonsPanel.add(btnAnnuler);

        btnEnregistrer.setBackground(new java.awt.Color(212, 168, 67));
        btnEnregistrer.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnEnregistrer.setForeground(new java.awt.Color(26, 32, 53));
        btnEnregistrer.setText("Ajouter");
        btnEnregistrer.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnEnregistrer.setFocusPainted(false);
        btnEnregistrer.addActionListener(this::btnEnregistrerActionPerformed);
        buttonsPanel.add(btnEnregistrer);

        getContentPane().add(buttonsPanel, java.awt.BorderLayout.SOUTH);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void cmbCategorieActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbCategorieActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cmbCategorieActionPerformed

    private void btnEnregistrerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEnregistrerActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnEnregistrerActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ReflectiveOperationException | javax.swing.UnsupportedLookAndFeelException ex) {
            logger.log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                AjouterLivre dialog = new AjouterLivre(new javax.swing.JFrame(), true);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel aperçuPanel;
    private javax.swing.JButton btnAnnuler;
    private javax.swing.JButton btnEnregistrer;
    private javax.swing.JPanel buttonsPanel;
    private javax.swing.JComboBox<String> cmbCategorie;
    private javax.swing.JPanel formPanel;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JLabel lblAperçu;
    private javax.swing.JLabel lblAperçuAuteur;
    private javax.swing.JLabel lblAperçuCat;
    private javax.swing.JLabel lblAperçuQty;
    private javax.swing.JLabel lblAperçuTitre;
    private javax.swing.JLabel lblAuteur;
    private javax.swing.JLabel lblCategorie;
    private javax.swing.JLabel lblQuantite;
    private javax.swing.JLabel lblTitle;
    private javax.swing.JLabel lblTitre;
    private javax.swing.JPanel ligne1Panel;
    private javax.swing.JPanel ligne2Panel;
    private javax.swing.JPanel panelAuteur;
    private javax.swing.JPanel panelCategorie;
    private javax.swing.JPanel panelInfo;
    private javax.swing.JPanel panelQuantite;
    private javax.swing.JPanel panelSep1;
    private javax.swing.JPanel panelSep2;
    private javax.swing.JPanel panelSep3;
    private javax.swing.JPanel panelTitre;
    private javax.swing.JSeparator sep1;
    private javax.swing.JPanel titlePanel;
    private javax.swing.JTextField txtAuteur;
    private javax.swing.JSpinner txtQuantite;
    private javax.swing.JTextField txtTitre;
    // End of variables declaration//GEN-END:variables
}

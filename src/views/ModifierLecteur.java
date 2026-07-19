/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package views;

import dao.LecteurDAO;
import models.Lecteur;

/**
 *
 * @author Admin
 */
public class ModifierLecteur extends javax.swing.JDialog {

    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(ModifierLecteur.class.getName());

    /**
     * Creates new form ModifierLecteur
     */
    private LecteurDAO lecteurDAO = new LecteurDAO();
    private int idLecteur;

   public ModifierLecteur(java.awt.Frame parent, boolean modal, int idLecteur,
        String nom, String prenom, String telephone, String adresse) {
    super(parent, true);
    initComponents();
    setLocationRelativeTo(parent);

    this.idLecteur = idLecteur;

    initStyles(nom, prenom, telephone, adresse);
    initEvenements(nom, prenom, telephone);
}

    private void enregistrer() {
       String nom = txtNom.getText().trim().toUpperCase();
String prenom = txtPrenom.getText().trim();
if (!prenom.isEmpty()) {
    prenom = prenom.substring(0, 1).toUpperCase() 
             + prenom.substring(1).toLowerCase();
}
String telephone = txtTelephone.getText().trim();
String adresse = txtAdresse.getText().trim();

if (nom.isEmpty() || prenom.isEmpty()) {
    javax.swing.JOptionPane.showMessageDialog(this,
        "Nom et prénom sont obligatoires.",
        "Erreur", javax.swing.JOptionPane.ERROR_MESSAGE);
    return;
}
if (!nom.matches("[a-zA-ZÀ-ÿ\\s\\-']+")) {
    javax.swing.JOptionPane.showMessageDialog(this,
        "Le nom ne doit contenir que des lettres !",
        "Erreur", javax.swing.JOptionPane.ERROR_MESSAGE);
    txtNom.requestFocus();
    return;
}
if (!prenom.matches("[a-zA-ZÀ-ÿ\\s\\-']+")) {
    javax.swing.JOptionPane.showMessageDialog(this,
        "Le prénom ne doit contenir que des lettres !",
        "Erreur", javax.swing.JOptionPane.ERROR_MESSAGE);
    txtPrenom.requestFocus();
    return;
}
// Téléphone obligatoire
if (telephone.isEmpty()) {
    javax.swing.JOptionPane.showMessageDialog(this,
        "Le numéro de téléphone est obligatoire.",
        "Erreur", javax.swing.JOptionPane.ERROR_MESSAGE);
    txtTelephone.requestFocus();
    return;
}

// Adresse obligatoire
if (adresse.isEmpty()) {
    javax.swing.JOptionPane.showMessageDialog(this,
        "L'adresse est obligatoire.",
        "Erreur", javax.swing.JOptionPane.ERROR_MESSAGE);
    txtAdresse.requestFocus();
    return;
}

        // Validation téléphone — chiffres seulement
        if (!telephone.isEmpty() && !telephone.matches("[0-9+\\s]+")) {
            javax.swing.JOptionPane.showMessageDialog(this,
                    "Le téléphone ne doit contenir que des chiffres.",
                    "Erreur", javax.swing.JOptionPane.ERROR_MESSAGE);
            txtTelephone.requestFocus();
            return;
        }

        // Validation longueur téléphone
        if (!telephone.isEmpty() && telephone.replaceAll("[^0-9]", "").length() < 8) {
            javax.swing.JOptionPane.showMessageDialog(this,
                    "Le numéro de téléphone doit avoir au moins 8 chiffres.",
                    "Erreur", javax.swing.JOptionPane.ERROR_MESSAGE);
            txtTelephone.requestFocus();
            return;
        }
        if (lecteurDAO.telephoneExisteDeja(telephone, idLecteur)) {
    javax.swing.JOptionPane.showMessageDialog(this,
        "Ce numéro de téléphone est déjà utilisé par un autre lecteur !",
        "Erreur", javax.swing.JOptionPane.ERROR_MESSAGE);
    txtTelephone.requestFocus();
    return;
}
        models.Lecteur lecteur = new models.Lecteur(idLecteur, nom, prenom, telephone,adresse, "");
        int confirm = javax.swing.JOptionPane.showConfirmDialog(this,
    "Confirmer la modification de " + nom + " " + prenom + " ?",
    "Confirmation",
    javax.swing.JOptionPane.YES_NO_OPTION,
    javax.swing.JOptionPane.QUESTION_MESSAGE);
if (confirm != javax.swing.JOptionPane.YES_OPTION) return;
        if (lecteurDAO.modifier(lecteur)) {
            javax.swing.JOptionPane.showMessageDialog(this, "Lecteur modifié avec succès !");
            dispose();
        } else {
            javax.swing.JOptionPane.showMessageDialog(this,
                    "Erreur lors de la modification.",
                    "Erreur", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }
private void initStyles(String nom, String prenom,
        String telephone, String adresse) {
    btnEnregistrer.putClientProperty("JButton.buttonType", "roundRect");
    btnAnnuler.putClientProperty("JButton.buttonType", "roundRect");

    // Pré-remplir les champs
    txtNom.setText(nom);
    txtPrenom.setText(prenom);
    txtTelephone.setText(telephone);
    txtAdresse.setText(adresse);

    // Labels uppercase
    jLabel1.setText("NOM *");
    jLabel1.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 10));
    jLabel1.setForeground(new java.awt.Color(154, 160, 176));

    lblPrenom.setText("PRÉNOM *");
    lblPrenom.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 10));
    lblPrenom.setForeground(new java.awt.Color(154, 160, 176));

    lblTelephone.setText("TÉLÉPHONE");
    lblTelephone.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 10));
    lblTelephone.setForeground(new java.awt.Color(154, 160, 176));

    lblAdresse.setText("ADRESSE");
    lblAdresse.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 10));
    lblAdresse.setForeground(new java.awt.Color(154, 160, 176));

    // Titre header
    lblTitle.setText("Modifier le lecteur");

    // Bandeau — initiales avatar
    String init = (nom.length() > 0 ? String.valueOf(nom.charAt(0)) : "")
        + (prenom.length() > 0 ? String.valueOf(prenom.charAt(0)) : "");
    lblAvatarBandeau.setText(init.toUpperCase().isEmpty() ? "?" : init.toUpperCase());
    lblBandeauNom.setText(nom + " " + prenom);
    lblBandeauSub.setText(telephone.isEmpty() ? "Aucun téléphone" : telephone);

    // Aperçu initial
    majApercu(nom, prenom, telephone);

    // Focus orange sur les champs
    ajouterFocusOrange(txtNom);
    ajouterFocusOrange(txtPrenom);
    ajouterFocusOrange(txtTelephone);
    ajouterFocusOrange(txtAdresse);
}

private void ajouterFocusOrange(javax.swing.JTextField field) {
    field.addFocusListener(new java.awt.event.FocusAdapter() {
        @Override
        public void focusGained(java.awt.event.FocusEvent e) {
            field.setBorder(javax.swing.BorderFactory.createCompoundBorder(
                javax.swing.BorderFactory.createLineBorder(
                    new java.awt.Color(224, 154, 48), 1),
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
    field.setBorder(javax.swing.BorderFactory.createCompoundBorder(
        javax.swing.BorderFactory.createLineBorder(
            new java.awt.Color(232, 226, 216), 1),
        javax.swing.BorderFactory.createEmptyBorder(0, 6, 0, 6)));
}

private void majApercu(String nom, String prenom, String telephone) {
    // Avatar initiales
    String init = (nom.length() > 0 ? String.valueOf(nom.charAt(0)) : "")
        + (prenom.length() > 0 ? String.valueOf(prenom.charAt(0)) : "");
    avatarLabel.setText(init.toUpperCase().isEmpty() ? "?" : init.toUpperCase());

    // Nom complet
    String nomComplet = (nom + " " + prenom).trim();
    lblApercuNom.setText(nomComplet.isEmpty() ? "— —" : nomComplet);
    lblApercuSub.setText(telephone.isEmpty() ? "Aucun téléphone" : telephone);
}

private void initEvenements(String nomInit, String prenomInit,
        String telInit) {
    btnAnnuler.addActionListener(e -> dispose());
    btnEnregistrer.addActionListener(e -> enregistrer());

    // Aperçu en temps réel
    java.awt.event.KeyAdapter kl = new java.awt.event.KeyAdapter() {
        @Override
        public void keyReleased(java.awt.event.KeyEvent e) {
            String nom = txtNom.getText().trim();
            String prenom = txtPrenom.getText().trim();
            String tel = txtTelephone.getText().trim();
            majApercu(nom, prenom, tel);
        }
    };
    txtNom.addKeyListener(kl);
    txtPrenom.addKeyListener(kl);
    txtTelephone.addKeyListener(kl);

    // Filtre téléphone — chiffres + + espace seulement
    txtTelephone.addKeyListener(new java.awt.event.KeyAdapter() {
        @Override
        public void keyTyped(java.awt.event.KeyEvent e) {
            char c = e.getKeyChar();
            if (!Character.isDigit(c) && c != '+'
                    && c != ' '
                    && c != java.awt.event.KeyEvent.VK_BACK_SPACE) {
                e.consume();
            }
        }
    });
}
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        formPanel = new javax.swing.JPanel();
        beandeauPanel = new javax.swing.JPanel();
        avatarBandeau = new javax.swing.JPanel();
        lblAvatarBandeau = new javax.swing.JLabel();
        infoBandeau = new javax.swing.JPanel();
        lblBandeauNom = new javax.swing.JLabel();
        lblBandeauSub = new javax.swing.JLabel();
        lblBadgeModif = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        panelSep1 = new javax.swing.JPanel();
        ligne1 = new javax.swing.JPanel();
        panelNom = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        txtNom = new javax.swing.JTextField();
        panelPrenom = new javax.swing.JPanel();
        lblPrenom = new javax.swing.JLabel();
        txtPrenom = new javax.swing.JTextField();
        panelSep2 = new javax.swing.JPanel();
        ligne2 = new javax.swing.JPanel();
        panelTel = new javax.swing.JPanel();
        lblTelephone = new javax.swing.JLabel();
        txtTelephone = new javax.swing.JTextField();
        panelAdresse = new javax.swing.JPanel();
        lblAdresse = new javax.swing.JLabel();
        txtAdresse = new javax.swing.JTextField();
        panelSep3 = new javax.swing.JPanel();
        lblApercu = new javax.swing.JLabel();
        panelSep4 = new javax.swing.JPanel();
        apercuPanel = new javax.swing.JPanel();
        avatarWrapper = new javax.swing.JPanel();
        avatarLabel = new javax.swing.JLabel();
        panelInfos = new javax.swing.JPanel();
        lblApercuNom = new javax.swing.JLabel();
        lblApercuSub = new javax.swing.JLabel();
        panelBadges = new javax.swing.JPanel();
        lblBadgeActif = new javax.swing.JLabel();
        lblApercuNum = new javax.swing.JLabel();
        buttonsPanel = new javax.swing.JPanel();
        btnAnnuler = new javax.swing.JButton();
        btnEnregistrer = new javax.swing.JButton();
        titlePanel = new javax.swing.JPanel();
        barreVerte = new javax.swing.JPanel();
        headerCenter = new javax.swing.JPanel();
        lblTitle = new javax.swing.JLabel();
        badgeWrapper = new javax.swing.JPanel();
        labelBadge = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Modifier le lecteur");
        setBackground(new java.awt.Color(245, 240, 232));
        setModal(true);
        setPreferredSize(new java.awt.Dimension(660, 500));
        setResizable(false);

        formPanel.setBackground(new java.awt.Color(245, 240, 232));
        formPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(24, 28, 16, 28));
        formPanel.setLayout(new javax.swing.BoxLayout(formPanel, javax.swing.BoxLayout.Y_AXIS));

        beandeauPanel.setBackground(new java.awt.Color(255, 255, 255));
        beandeauPanel.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(232, 226, 216)), javax.swing.BorderFactory.createEmptyBorder(10, 14, 10, 14)));
        beandeauPanel.setAlignmentX(0.0F);
        beandeauPanel.setMaximumSize(new java.awt.Dimension(32767, 60));
        beandeauPanel.setLayout(new java.awt.BorderLayout());

        avatarBandeau.setBackground(new java.awt.Color(255, 255, 255));
        avatarBandeau.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 14));
        avatarBandeau.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 0, 0));

        lblAvatarBandeau.setBackground(new java.awt.Color(255, 248, 230));
        lblAvatarBandeau.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblAvatarBandeau.setForeground(new java.awt.Color(224, 154, 48));
        lblAvatarBandeau.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblAvatarBandeau.setText("?");
        lblAvatarBandeau.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(224, 154, 48)));
        lblAvatarBandeau.setOpaque(true);
        lblAvatarBandeau.setPreferredSize(new java.awt.Dimension(40, 40));
        avatarBandeau.add(lblAvatarBandeau);

        beandeauPanel.add(avatarBandeau, java.awt.BorderLayout.WEST);

        infoBandeau.setBackground(new java.awt.Color(255, 255, 255));
        infoBandeau.setLayout(new javax.swing.BoxLayout(infoBandeau, javax.swing.BoxLayout.Y_AXIS));

        lblBandeauNom.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        lblBandeauNom.setForeground(new java.awt.Color(16, 32, 53));
        lblBandeauNom.setText("- -");
        infoBandeau.add(lblBandeauNom);

        lblBandeauSub.setForeground(new java.awt.Color(154, 160, 176));
        lblBandeauSub.setText("-");
        infoBandeau.add(lblBandeauSub);

        beandeauPanel.add(infoBandeau, java.awt.BorderLayout.CENTER);

        lblBadgeModif.setBackground(new java.awt.Color(255, 248, 230));
        lblBadgeModif.setFont(new java.awt.Font("Segoe UI", 1, 11)); // NOI18N
        lblBadgeModif.setForeground(new java.awt.Color(180, 110, 10));
        lblBadgeModif.setText("En cours de modif. . .");
        lblBadgeModif.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(212, 168, 67)), javax.swing.BorderFactory.createEmptyBorder(3, 10, 3, 10)));
        lblBadgeModif.setOpaque(true);
        beandeauPanel.add(lblBadgeModif, java.awt.BorderLayout.EAST);

        formPanel.add(beandeauPanel);

        jPanel3.setBackground(new java.awt.Color(245, 240, 232));
        jPanel3.setAlignmentX(0.0F);
        jPanel3.setMaximumSize(new java.awt.Dimension(32767, 16));
        jPanel3.setName(""); // NOI18N
        jPanel3.setPreferredSize(new java.awt.Dimension(0, 16));
        formPanel.add(jPanel3);

        panelSep1.setBackground(new java.awt.Color(245, 240, 232));
        panelSep1.setAlignmentX(0.0F);
        panelSep1.setMaximumSize(new java.awt.Dimension(32767, 14));
        panelSep1.setPreferredSize(new java.awt.Dimension(0, 16));
        formPanel.add(panelSep1);

        ligne1.setBackground(new java.awt.Color(245, 240, 232));
        ligne1.setAlignmentX(0.0F);
        ligne1.setMaximumSize(new java.awt.Dimension(32767, 66));
        ligne1.setLayout(new java.awt.GridLayout(1, 2, 16, 0));

        panelNom.setBackground(new java.awt.Color(245, 240, 232));
        panelNom.setLayout(new javax.swing.BoxLayout(panelNom, javax.swing.BoxLayout.Y_AXIS));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(26, 32, 53));
        jLabel1.setText("Nom");
        jLabel1.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 6, 0));
        panelNom.add(jLabel1);

        txtNom.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        txtNom.setAlignmentX(0.0F);
        txtNom.setMaximumSize(new java.awt.Dimension(32767, 40));
        txtNom.setPreferredSize(new java.awt.Dimension(0, 40));
        panelNom.add(txtNom);

        ligne1.add(panelNom);

        panelPrenom.setBackground(new java.awt.Color(245, 240, 232));
        panelPrenom.setLayout(new javax.swing.BoxLayout(panelPrenom, javax.swing.BoxLayout.Y_AXIS));

        lblPrenom.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblPrenom.setForeground(new java.awt.Color(26, 32, 53));
        lblPrenom.setText("Prénom");
        lblPrenom.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 6, 0));
        panelPrenom.add(lblPrenom);

        txtPrenom.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        txtPrenom.setAlignmentX(0.0F);
        txtPrenom.setMaximumSize(new java.awt.Dimension(32767, 40));
        txtPrenom.setPreferredSize(new java.awt.Dimension(0, 40));
        panelPrenom.add(txtPrenom);

        ligne1.add(panelPrenom);

        formPanel.add(ligne1);

        panelSep2.setBackground(new java.awt.Color(245, 240, 232));
        panelSep2.setAlignmentX(0.0F);
        panelSep2.setMaximumSize(new java.awt.Dimension(32767, 14));
        panelSep2.setPreferredSize(new java.awt.Dimension(0, 16));
        formPanel.add(panelSep2);

        ligne2.setBackground(new java.awt.Color(245, 240, 232));
        ligne2.setAlignmentX(0.0F);
        ligne2.setMaximumSize(new java.awt.Dimension(32767, 66));
        ligne2.setLayout(new java.awt.GridLayout(1, 2, 16, 0));

        panelTel.setBackground(new java.awt.Color(245, 240, 232));
        panelTel.setLayout(new javax.swing.BoxLayout(panelTel, javax.swing.BoxLayout.Y_AXIS));

        lblTelephone.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblTelephone.setForeground(new java.awt.Color(26, 32, 53));
        lblTelephone.setText("Téléphone");
        lblTelephone.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 6, 0));
        panelTel.add(lblTelephone);

        txtTelephone.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        txtTelephone.setAlignmentX(0.0F);
        txtTelephone.setMaximumSize(new java.awt.Dimension(32767, 40));
        txtTelephone.setPreferredSize(new java.awt.Dimension(0, 40));
        panelTel.add(txtTelephone);

        ligne2.add(panelTel);

        panelAdresse.setBackground(new java.awt.Color(245, 240, 232));
        panelAdresse.setLayout(new javax.swing.BoxLayout(panelAdresse, javax.swing.BoxLayout.Y_AXIS));

        lblAdresse.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblAdresse.setForeground(new java.awt.Color(26, 32, 53));
        lblAdresse.setText("Adresse");
        lblAdresse.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 6, 0));
        panelAdresse.add(lblAdresse);

        txtAdresse.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        txtAdresse.setAlignmentX(0.0F);
        txtAdresse.setMaximumSize(new java.awt.Dimension(32767, 40));
        txtAdresse.setPreferredSize(new java.awt.Dimension(0, 40));
        panelAdresse.add(txtAdresse);

        ligne2.add(panelAdresse);

        formPanel.add(ligne2);

        panelSep3.setBackground(new java.awt.Color(245, 240, 232));
        panelSep3.setAlignmentX(0.0F);
        panelSep3.setMaximumSize(new java.awt.Dimension(32767, 8));
        panelSep3.setPreferredSize(new java.awt.Dimension(0, 16));
        formPanel.add(panelSep3);

        lblApercu.setFont(new java.awt.Font("Segoe UI", 1, 10)); // NOI18N
        lblApercu.setForeground(new java.awt.Color(154, 160, 176));
        lblApercu.setText("APERÇU APRÈS MODIFICATION");
        lblApercu.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 8, 0));
        formPanel.add(lblApercu);

        panelSep4.setBackground(new java.awt.Color(245, 240, 232));
        panelSep4.setAlignmentX(0.0F);
        panelSep4.setMaximumSize(new java.awt.Dimension(32767, 8));
        panelSep4.setPreferredSize(new java.awt.Dimension(0, 16));
        formPanel.add(panelSep4);

        apercuPanel.setBackground(new java.awt.Color(255, 255, 255));
        apercuPanel.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(232, 226, 216)), javax.swing.BorderFactory.createEmptyBorder(12, 14, 12, 14)));
        apercuPanel.setAlignmentX(0.0F);
        apercuPanel.setMaximumSize(new java.awt.Dimension(32767, 64));
        apercuPanel.setLayout(new java.awt.BorderLayout());

        avatarWrapper.setBackground(new java.awt.Color(255, 255, 255));
        avatarWrapper.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 14));
        avatarWrapper.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 0, 0));

        avatarLabel.setBackground(new java.awt.Color(255, 248, 230));
        avatarLabel.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        avatarLabel.setForeground(new java.awt.Color(212, 168, 67));
        avatarLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        avatarLabel.setText("?");
        avatarLabel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(212, 168, 67)));
        avatarLabel.setOpaque(true);
        avatarLabel.setPreferredSize(new java.awt.Dimension(40, 40));
        avatarWrapper.add(avatarLabel);

        apercuPanel.add(avatarWrapper, java.awt.BorderLayout.WEST);

        panelInfos.setBackground(new java.awt.Color(255, 255, 255));
        panelInfos.setLayout(new javax.swing.BoxLayout(panelInfos, javax.swing.BoxLayout.Y_AXIS));

        lblApercuNom.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        lblApercuNom.setForeground(new java.awt.Color(26, 32, 53));
        lblApercuNom.setText("- -");
        panelInfos.add(lblApercuNom);

        lblApercuSub.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        lblApercuSub.setForeground(new java.awt.Color(154, 160, 176));
        lblApercuSub.setText("Tapez le nom et prénom. . .");
        panelInfos.add(lblApercuSub);

        apercuPanel.add(panelInfos, java.awt.BorderLayout.CENTER);

        panelBadges.setBackground(new java.awt.Color(255, 255, 255));
        panelBadges.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT, 6, 0));

        lblBadgeActif.setBackground(new java.awt.Color(230, 249, 239));
        lblBadgeActif.setFont(new java.awt.Font("Segoe UI", 1, 11)); // NOI18N
        lblBadgeActif.setForeground(new java.awt.Color(30, 140, 80));
        lblBadgeActif.setText("Membre actif");
        lblBadgeActif.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(59, 173, 114)), javax.swing.BorderFactory.createEmptyBorder(3, 10, 3, 10)));
        lblBadgeActif.setOpaque(true);
        panelBadges.add(lblBadgeActif);

        lblApercuNum.setBackground(new java.awt.Color(255, 248, 230));
        lblApercuNum.setFont(new java.awt.Font("Segoe UI", 1, 11)); // NOI18N
        lblApercuNum.setForeground(new java.awt.Color(180, 110, 10));
        lblApercuNum.setText("BG-2026-????");
        lblApercuNum.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(212, 168, 67)), javax.swing.BorderFactory.createEmptyBorder(3, 10, 3, 10)));
        lblApercuNum.setOpaque(true);
        panelBadges.add(lblApercuNum);

        apercuPanel.add(panelBadges, java.awt.BorderLayout.EAST);

        formPanel.add(apercuPanel);

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

        btnEnregistrer.setBackground(new java.awt.Color(224, 154, 48));
        btnEnregistrer.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnEnregistrer.setForeground(new java.awt.Color(255, 255, 255));
        btnEnregistrer.setText("Enregistrer");
        btnEnregistrer.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnEnregistrer.setFocusPainted(false);
        buttonsPanel.add(btnEnregistrer);

        getContentPane().add(buttonsPanel, java.awt.BorderLayout.SOUTH);

        titlePanel.setBackground(new java.awt.Color(26, 32, 53));
        titlePanel.setPreferredSize(new java.awt.Dimension(0, 58));
        titlePanel.setLayout(new java.awt.BorderLayout());

        barreVerte.setBackground(new java.awt.Color(59, 173, 114));
        barreVerte.setPreferredSize(new java.awt.Dimension(4, 0));
        titlePanel.add(barreVerte, java.awt.BorderLayout.WEST);

        headerCenter.setBackground(new java.awt.Color(26, 32, 53));
        headerCenter.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 12, 10));

        lblTitle.setFont(new java.awt.Font("Segoe UI", 1, 15)); // NOI18N
        lblTitle.setForeground(new java.awt.Color(255, 255, 255));
        lblTitle.setText("jLabel3");
        headerCenter.add(lblTitle);

        titlePanel.add(headerCenter, java.awt.BorderLayout.CENTER);

        badgeWrapper.setBackground(new java.awt.Color(26, 32, 53));
        badgeWrapper.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT, 16, 14));

        labelBadge.setBackground(new java.awt.Color(255, 248, 230));
        labelBadge.setFont(new java.awt.Font("Segoe UI", 1, 11)); // NOI18N
        labelBadge.setForeground(new java.awt.Color(180, 110, 10));
        labelBadge.setText("Modification");
        labelBadge.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(212, 168, 67)), javax.swing.BorderFactory.createEmptyBorder(3, 12, 3, 12)));
        labelBadge.setOpaque(true);
        badgeWrapper.add(labelBadge);

        titlePanel.add(badgeWrapper, java.awt.BorderLayout.EAST);

        getContentPane().add(titlePanel, java.awt.BorderLayout.NORTH);

        pack();
    }// </editor-fold>//GEN-END:initComponents

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
                ModifierLecteur dialog = new ModifierLecteur(new javax.swing.JFrame(), true, 0, "", "", "","");
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
    private javax.swing.JPanel apercuPanel;
    private javax.swing.JPanel avatarBandeau;
    private javax.swing.JLabel avatarLabel;
    private javax.swing.JPanel avatarWrapper;
    private javax.swing.JPanel badgeWrapper;
    private javax.swing.JPanel barreVerte;
    private javax.swing.JPanel beandeauPanel;
    private javax.swing.JButton btnAnnuler;
    private javax.swing.JButton btnEnregistrer;
    private javax.swing.JPanel buttonsPanel;
    private javax.swing.JPanel formPanel;
    private javax.swing.JPanel headerCenter;
    private javax.swing.JPanel infoBandeau;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JLabel labelBadge;
    private javax.swing.JLabel lblAdresse;
    private javax.swing.JLabel lblApercu;
    private javax.swing.JLabel lblApercuNom;
    private javax.swing.JLabel lblApercuNum;
    private javax.swing.JLabel lblApercuSub;
    private javax.swing.JLabel lblAvatarBandeau;
    private javax.swing.JLabel lblBadgeActif;
    private javax.swing.JLabel lblBadgeModif;
    private javax.swing.JLabel lblBandeauNom;
    private javax.swing.JLabel lblBandeauSub;
    private javax.swing.JLabel lblPrenom;
    private javax.swing.JLabel lblTelephone;
    private javax.swing.JLabel lblTitle;
    private javax.swing.JPanel ligne1;
    private javax.swing.JPanel ligne2;
    private javax.swing.JPanel panelAdresse;
    private javax.swing.JPanel panelBadges;
    private javax.swing.JPanel panelInfos;
    private javax.swing.JPanel panelNom;
    private javax.swing.JPanel panelPrenom;
    private javax.swing.JPanel panelSep1;
    private javax.swing.JPanel panelSep2;
    private javax.swing.JPanel panelSep3;
    private javax.swing.JPanel panelSep4;
    private javax.swing.JPanel panelTel;
    private javax.swing.JPanel titlePanel;
    private javax.swing.JTextField txtAdresse;
    private javax.swing.JTextField txtNom;
    private javax.swing.JTextField txtPrenom;
    private javax.swing.JTextField txtTelephone;
    // End of variables declaration//GEN-END:variables
}

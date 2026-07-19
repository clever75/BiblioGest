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
public class AjouterLecteur extends javax.swing.JDialog {

    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(AjouterLecteur.class.getName());

    /**
     * Creates new form AjouterLecteur
     */
    public AjouterLecteur(java.awt.Frame parent, boolean modal) {
    super(parent, true);
    initComponents();
    setLocationRelativeTo(parent);
    txtDate.setDate(new java.util.Date());

    initStyles();
    initEvenements();
    txtDate.setMaxSelectableDate(new java.util.Date());
}
private void ajouter() {
    dao.LecteurDAO lecteurDAO = new dao.LecteurDAO();
    String nom = txtNom.getText().trim().toUpperCase();
    String prenom = txtPrenom.getText().trim();
    if (!prenom.isEmpty()) {
        prenom = prenom.substring(0, 1).toUpperCase() 
                 + prenom.substring(1).toLowerCase();
    }
    String telephone = txtTelephone.getText().trim();
    String adresse = txtAdresse.getText().trim();

    // Nom et prénom obligatoires
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
    // Téléphone OBLIGATOIRE
    if (telephone.isEmpty()) {
        javax.swing.JOptionPane.showMessageDialog(this,
            "Le numéro de téléphone est obligatoire.",
            "Erreur", javax.swing.JOptionPane.ERROR_MESSAGE);
        txtTelephone.requestFocus();
        return;
    }
    
if (telephone.replaceAll("[^0-9]", "").length() > 10) {
    javax.swing.JOptionPane.showMessageDialog(this,
        "Le numéro de téléphone ne peut pas dépasser 10 chiffres !",
        "Erreur", javax.swing.JOptionPane.ERROR_MESSAGE);
    txtTelephone.requestFocus();
    return;
}
    // Format téléphone
    if (!telephone.matches("[0-9+\\s]+")) {
        javax.swing.JOptionPane.showMessageDialog(this,
            "Le téléphone ne doit contenir que des chiffres.",
            "Erreur", javax.swing.JOptionPane.ERROR_MESSAGE);
        txtTelephone.requestFocus();
        return;
    }

    if (telephone.replaceAll("[^0-9]", "").length() < 8) {
        javax.swing.JOptionPane.showMessageDialog(this,
            "Le numéro de téléphone doit avoir au moins 8 chiffres.",
            "Erreur", javax.swing.JOptionPane.ERROR_MESSAGE);
        txtTelephone.requestFocus();
        return;
    }

    // Adresse OBLIGATOIRE
    if (adresse.isEmpty()) {
        javax.swing.JOptionPane.showMessageDialog(this,
            "L'adresse est obligatoire.",
            "Erreur", javax.swing.JOptionPane.ERROR_MESSAGE);
        txtAdresse.requestFocus();
        return;
    }

    // Date
    java.util.Date dateSelectionnee = txtDate.getDate();
    if (dateSelectionnee == null) {
        javax.swing.JOptionPane.showMessageDialog(this,
            "Veuillez sélectionner une date d'inscription.",
            "Erreur", javax.swing.JOptionPane.ERROR_MESSAGE);
        return;
    }
    if (dateSelectionnee.after(new java.util.Date())) {
    javax.swing.JOptionPane.showMessageDialog(this,
        "La date d'inscription ne peut pas être dans le futur !",
        "Erreur", javax.swing.JOptionPane.ERROR_MESSAGE);
    return;
}

    String date = new java.text.SimpleDateFormat("yyyy-MM-dd")
        .format(dateSelectionnee);

    // Vérifier doublon (même nom + prénom + téléphone)
    // Vérifier téléphone unique
if (lecteurDAO.telephoneExisteDeja(telephone)) {
    javax.swing.JOptionPane.showMessageDialog(this,
        "Ce numéro de téléphone est déjà utilisé par un autre lecteur !",
        "Erreur", javax.swing.JOptionPane.ERROR_MESSAGE);
    txtTelephone.requestFocus();
    return;
}

// Vérifier doublon nom+prénom
if (lecteurDAO.lecteurExisteDeja(nom, prenom, telephone)) {
    int rep = javax.swing.JOptionPane.showConfirmDialog(this,
        "Un lecteur avec ce nom et ce téléphone existe déjà.\n"
        + "Voulez-vous quand même l'ajouter ?",
        "Doublon détecté",
        javax.swing.JOptionPane.YES_NO_OPTION,
        javax.swing.JOptionPane.WARNING_MESSAGE);
    if (rep != javax.swing.JOptionPane.YES_OPTION) return;
}
    models.Lecteur lecteur = new models.Lecteur(nom, prenom, telephone, adresse, date);
int confirm = javax.swing.JOptionPane.showConfirmDialog(this,
    "Confirmer l'ajout de " + nom + " " + prenom + " ?",
    "Confirmation",
    javax.swing.JOptionPane.YES_NO_OPTION,
    javax.swing.JOptionPane.QUESTION_MESSAGE);
if (confirm != javax.swing.JOptionPane.YES_OPTION) return;
    if (lecteurDAO.ajouter(lecteur)) {
        javax.swing.JOptionPane.showMessageDialog(this,
            "Lecteur ajouté avec succès !");
        dispose();
    } else {
        javax.swing.JOptionPane.showMessageDialog(this,
            "Erreur lors de l'ajout.",
            "Erreur", javax.swing.JOptionPane.ERROR_MESSAGE);
    }
}  
    private void initStyles() {
    btnEnregistrer.putClientProperty("JButton.buttonType", "roundRect");
    btnAnnuler.putClientProperty("JButton.buttonType", "roundRect");

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

    lblDate.setText("DATE D'INSCRIPTION");
    lblDate.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 10));
    lblDate.setForeground(new java.awt.Color(154, 160, 176));

    lblAdresse.setText("ADRESSE");
    lblAdresse.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 10));
    lblAdresse.setForeground(new java.awt.Color(154, 160, 176));

    // Header — titre + sous-titre
    jLabel2.setText("Ajouter un lecteur");
    jLabel2.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 15));
    jLabel2.setForeground(java.awt.Color.WHITE);

    lblSub.setForeground(new java.awt.Color(255, 255, 255, 100));

    // Focus vert sur les champs
    ajouterFocusVert(txtNom);
    ajouterFocusVert(txtPrenom);
    ajouterFocusVert(txtTelephone);
    ajouterFocusVert(txtAdresse);
}

private void ajouterFocusVert(javax.swing.JTextField field) {
    field.addFocusListener(new java.awt.event.FocusAdapter() {
        @Override
        public void focusGained(java.awt.event.FocusEvent e) {
            field.setBorder(javax.swing.BorderFactory.createCompoundBorder(
                javax.swing.BorderFactory.createLineBorder(
                    new java.awt.Color(59, 173, 114), 1),
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

private void majApercu() {
  String nom = txtNom.getText().trim().toUpperCase();
String prenom = txtPrenom.getText().trim();
if (!prenom.isEmpty())
    prenom = prenom.substring(0, 1).toUpperCase() + prenom.substring(1).toLowerCase();

    if (nom.isEmpty() && prenom.isEmpty()) {
        lblApercuNom.setText("— —");
        lblApercuSub.setText("Tapez le nom et prénom pour voir l'aperçu");
        avatarLabel.setText("?");
    } else {
        lblApercuNom.setText((nom + " " + prenom).trim());
        String tel = txtTelephone.getText().trim();
        lblApercuSub.setText(tel.isEmpty() ? "Aucun téléphone" : tel);
        String init = (nom.length() > 0
            ? String.valueOf(nom.charAt(0)) : "")
            + (prenom.length() > 0
            ? String.valueOf(prenom.charAt(0)) : "");
        avatarLabel.setText(init.toUpperCase());
    }
    apercuPanel.revalidate();
    apercuPanel.repaint();
}

private void initEvenements() {
    btnAnnuler.addActionListener(e -> dispose());
    btnEnregistrer.addActionListener(e -> ajouter());

    java.awt.event.KeyAdapter kl = new java.awt.event.KeyAdapter() {
        @Override
        public void keyReleased(java.awt.event.KeyEvent e) {
            majApercu();
        }
    };
    txtNom.addKeyListener(kl);
    txtPrenom.addKeyListener(kl);
    txtTelephone.addKeyListener(kl);
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
        barreVerte = new javax.swing.JPanel();
        headerCenter = new javax.swing.JPanel();
        panelAvatar = new javax.swing.JPanel();
        labelAvatar = new javax.swing.JLabel();
        txtPanel = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        lblSub = new javax.swing.JLabel();
        badgeWrapper = new javax.swing.JPanel();
        labelBadge = new javax.swing.JLabel();
        formPanel = new javax.swing.JPanel();
        ligne1Panel = new javax.swing.JPanel();
        panelNom = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        txtNom = new javax.swing.JTextField();
        panelPrenom = new javax.swing.JPanel();
        lblPrenom = new javax.swing.JLabel();
        txtPrenom = new javax.swing.JTextField();
        sep1 = new javax.swing.JPanel();
        ligne2Panel = new javax.swing.JPanel();
        panelTel = new javax.swing.JPanel();
        lblTelephone = new javax.swing.JLabel();
        txtTelephone = new javax.swing.JTextField();
        panelDate = new javax.swing.JPanel();
        lblDate = new javax.swing.JLabel();
        txtDate = new com.toedter.calendar.JDateChooser();
        sep2 = new javax.swing.JPanel();
        panelAdresse = new javax.swing.JPanel();
        lblAdresse = new javax.swing.JLabel();
        txtAdresse = new javax.swing.JTextField();
        sep3 = new javax.swing.JPanel();
        jSeparator1 = new javax.swing.JSeparator();
        sep4 = new javax.swing.JPanel();
        lblApercu = new javax.swing.JLabel();
        sep5 = new javax.swing.JPanel();
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

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Ajouter un lecteur");
        setUndecorated(true);
        setPreferredSize(new java.awt.Dimension(660, 520));

        titlePanel.setBackground(new java.awt.Color(26, 32, 53));
        titlePanel.setPreferredSize(new java.awt.Dimension(0, 58));
        titlePanel.setLayout(new java.awt.BorderLayout());

        barreVerte.setBackground(new java.awt.Color(59, 173, 114));
        barreVerte.setPreferredSize(new java.awt.Dimension(4, 0));
        titlePanel.add(barreVerte, java.awt.BorderLayout.WEST);

        headerCenter.setBackground(new java.awt.Color(26, 32, 53));
        headerCenter.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 12, 10));

        panelAvatar.setBackground(new java.awt.Color(59, 173, 50));
        panelAvatar.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(59, 173, 114)));
        panelAvatar.setPreferredSize(new java.awt.Dimension(36, 36));
        panelAvatar.setLayout(new java.awt.GridBagLayout());

        labelAvatar.setBackground(new java.awt.Color(59, 173, 114));
        labelAvatar.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        labelAvatar.setText("+U");
        panelAvatar.add(labelAvatar, new java.awt.GridBagConstraints());

        headerCenter.add(panelAvatar);

        txtPanel.setBackground(new java.awt.Color(26, 32, 53));
        txtPanel.setLayout(new javax.swing.BoxLayout(txtPanel, javax.swing.BoxLayout.Y_AXIS));

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 15)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("jLabel2");
        txtPanel.add(jLabel2);

        lblSub.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        lblSub.setForeground(new java.awt.Color(255, 255, 255));
        lblSub.setText("Remplissez les informations du nouveau membre");
        txtPanel.add(lblSub);

        headerCenter.add(txtPanel);

        titlePanel.add(headerCenter, java.awt.BorderLayout.CENTER);

        badgeWrapper.setBackground(new java.awt.Color(26, 32, 53));
        badgeWrapper.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT, 16, 14));

        labelBadge.setBackground(new java.awt.Color(59, 173, 40));
        labelBadge.setFont(new java.awt.Font("Segoe UI", 1, 11)); // NOI18N
        labelBadge.setText("Nouveau membre");
        labelBadge.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(59, 173, 114)), javax.swing.BorderFactory.createEmptyBorder(3, 12, 3, 12)));
        labelBadge.setOpaque(true);
        badgeWrapper.add(labelBadge);

        titlePanel.add(badgeWrapper, java.awt.BorderLayout.EAST);

        getContentPane().add(titlePanel, java.awt.BorderLayout.NORTH);

        formPanel.setBackground(new java.awt.Color(245, 240, 232));
        formPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(24, 28, 16, 28));
        formPanel.setLayout(new javax.swing.BoxLayout(formPanel, javax.swing.BoxLayout.Y_AXIS));

        ligne1Panel.setBackground(new java.awt.Color(245, 240, 232));
        ligne1Panel.setAlignmentX(0.0F);
        ligne1Panel.setMaximumSize(new java.awt.Dimension(32767, 66));
        ligne1Panel.setLayout(new java.awt.GridLayout(1, 2, 16, 0));

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

        ligne1Panel.add(panelNom);

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

        ligne1Panel.add(panelPrenom);

        formPanel.add(ligne1Panel);

        sep1.setBackground(new java.awt.Color(245, 240, 232));
        sep1.setAlignmentX(0.0F);
        sep1.setMaximumSize(new java.awt.Dimension(32767, 14));
        formPanel.add(sep1);

        ligne2Panel.setBackground(new java.awt.Color(245, 240, 232));
        ligne2Panel.setAlignmentX(0.0F);
        ligne2Panel.setMaximumSize(new java.awt.Dimension(32767, 66));
        ligne2Panel.setLayout(new java.awt.GridLayout(1, 2, 16, 0));

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

        ligne2Panel.add(panelTel);

        panelDate.setBackground(new java.awt.Color(245, 240, 232));
        panelDate.setLayout(new javax.swing.BoxLayout(panelDate, javax.swing.BoxLayout.Y_AXIS));

        lblDate.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblDate.setForeground(new java.awt.Color(26, 32, 53));
        lblDate.setText("Date d'inscription");
        lblDate.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 6, 0));
        panelDate.add(lblDate);

        txtDate.setAlignmentX(0.0F);
        txtDate.setMaximumSize(new java.awt.Dimension(32767, 40));
        txtDate.setPreferredSize(new java.awt.Dimension(0, 40));
        panelDate.add(txtDate);

        ligne2Panel.add(panelDate);

        formPanel.add(ligne2Panel);

        sep2.setBackground(new java.awt.Color(245, 240, 232));
        sep2.setAlignmentX(0.0F);
        sep2.setMaximumSize(new java.awt.Dimension(32767, 14));
        formPanel.add(sep2);

        panelAdresse.setBackground(new java.awt.Color(245, 240, 232));
        panelAdresse.setMaximumSize(new java.awt.Dimension(32767, 66));
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

        formPanel.add(panelAdresse);

        sep3.setBackground(new java.awt.Color(245, 240, 232));
        sep3.setAlignmentX(0.0F);
        sep3.setMaximumSize(new java.awt.Dimension(32767, 14));
        formPanel.add(sep3);

        jSeparator1.setForeground(new java.awt.Color(232, 226, 216));
        jSeparator1.setAlignmentX(0.0F);
        jSeparator1.setMaximumSize(new java.awt.Dimension(32767, 1));
        formPanel.add(jSeparator1);

        sep4.setBackground(new java.awt.Color(245, 240, 232));
        sep4.setAlignmentX(0.0F);
        sep4.setMaximumSize(new java.awt.Dimension(32767, 14));
        formPanel.add(sep4);

        lblApercu.setFont(new java.awt.Font("Segoe UI", 1, 10)); // NOI18N
        lblApercu.setForeground(new java.awt.Color(154, 160, 176));
        lblApercu.setText("APERÇU DU MEMBRE");
        lblApercu.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 8, 0));
        formPanel.add(lblApercu);

        sep5.setBackground(new java.awt.Color(245, 240, 232));
        sep5.setAlignmentX(0.0F);
        sep5.setMaximumSize(new java.awt.Dimension(32767, 8));
        formPanel.add(sep5);

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

        btnEnregistrer.setBackground(new java.awt.Color(59, 173, 114));
        btnEnregistrer.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnEnregistrer.setForeground(new java.awt.Color(255, 255, 255));
        btnEnregistrer.setText("Ajouter lecteur");
        btnEnregistrer.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnEnregistrer.setFocusPainted(false);
        buttonsPanel.add(btnEnregistrer);

        getContentPane().add(buttonsPanel, java.awt.BorderLayout.SOUTH);

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
                AjouterLecteur dialog = new AjouterLecteur(new javax.swing.JFrame(), true);
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
    private javax.swing.JLabel avatarLabel;
    private javax.swing.JPanel avatarWrapper;
    private javax.swing.JPanel badgeWrapper;
    private javax.swing.JPanel barreVerte;
    private javax.swing.JButton btnAnnuler;
    private javax.swing.JButton btnEnregistrer;
    private javax.swing.JPanel buttonsPanel;
    private javax.swing.JPanel formPanel;
    private javax.swing.JPanel headerCenter;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JLabel labelAvatar;
    private javax.swing.JLabel labelBadge;
    private javax.swing.JLabel lblAdresse;
    private javax.swing.JLabel lblApercu;
    private javax.swing.JLabel lblApercuNom;
    private javax.swing.JLabel lblApercuNum;
    private javax.swing.JLabel lblApercuSub;
    private javax.swing.JLabel lblBadgeActif;
    private javax.swing.JLabel lblDate;
    private javax.swing.JLabel lblPrenom;
    private javax.swing.JLabel lblSub;
    private javax.swing.JLabel lblTelephone;
    private javax.swing.JPanel ligne1Panel;
    private javax.swing.JPanel ligne2Panel;
    private javax.swing.JPanel panelAdresse;
    private javax.swing.JPanel panelAvatar;
    private javax.swing.JPanel panelBadges;
    private javax.swing.JPanel panelDate;
    private javax.swing.JPanel panelInfos;
    private javax.swing.JPanel panelNom;
    private javax.swing.JPanel panelPrenom;
    private javax.swing.JPanel panelTel;
    private javax.swing.JPanel sep1;
    private javax.swing.JPanel sep2;
    private javax.swing.JPanel sep3;
    private javax.swing.JPanel sep4;
    private javax.swing.JPanel sep5;
    private javax.swing.JPanel titlePanel;
    private javax.swing.JTextField txtAdresse;
    private com.toedter.calendar.JDateChooser txtDate;
    private javax.swing.JTextField txtNom;
    private javax.swing.JPanel txtPanel;
    private javax.swing.JTextField txtPrenom;
    private javax.swing.JTextField txtTelephone;
    // End of variables declaration//GEN-END:variables
}

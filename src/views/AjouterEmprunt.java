/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package views;

import dao.EmpruntDAO;

/**
 *
 * @author Admin
 */
public class AjouterEmprunt extends javax.swing.JDialog {

    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(AjouterEmprunt.class.getName());

    /**
     * Creates new form AjouterEmprunt
     */
    private EmpruntDAO empruntDAO = new EmpruntDAO();
    private java.util.ArrayList<Object[]> livres;
    private java.util.ArrayList<Object[]> lecteurs;
    private boolean succes = false;

public AjouterEmprunt(java.awt.Frame parent, boolean modal) {
    super(parent, modal);
    initComponents();
    setLocationRelativeTo(parent);
    initStyles();
    initDates();
    chargerDonnees();
    initActions();
}
private void initStyles() {
    btnEnregistrer.putClientProperty("JButton.buttonType", "roundRect");
    btnAnnuler.putClientProperty("JButton.buttonType", "roundRect");
}

private void initDates() {
    txtDateEmprunt.setDate(new java.util.Date());
    java.util.Calendar cal = java.util.Calendar.getInstance();
    cal.add(java.util.Calendar.DAY_OF_MONTH, 14);
    txtDateRetour.setDate(cal.getTime());
    txtDateEmprunt.setMaxSelectableDate(new java.util.Date());
}

private void initActions() {
    btnAnnuler.addActionListener(e -> dispose());
    btnEnregistrer.addActionListener(e -> confirmerEmprunt());
    cmbLivre.addActionListener(e -> mettreAJourApercu());
    cmbLecteur.addActionListener(e -> mettreAJourApercu());
}

private void mettreAJourApercu() {
    // Livre
    if (cmbLivre.getSelectedIndex() > 0) {
        lblApercuLivre.setText(cmbLivre.getSelectedItem().toString());
        lblApercuLivre.setForeground(new java.awt.Color(26, 32, 53));
    } else {
        lblApercuLivre.setText("— sélectionner un livre");
        lblApercuLivre.setForeground(new java.awt.Color(154, 160, 176));
    }

    // Lecteur
    if (cmbLecteur.getSelectedIndex() > 0) {
        lblApercuLecteur.setText(
                "👤 " + cmbLecteur.getSelectedItem().toString());
        lblApercuLecteur.setForeground(new java.awt.Color(90, 96, 112));
    } else {
        lblApercuLecteur.setText("— sélectionner un lecteur");
        lblApercuLecteur.setForeground(new java.awt.Color(154, 160, 176));
    }

    // Durée
if (txtDateEmprunt.getDate() != null
        && txtDateRetour.getDate() != null) {
    long diff = txtDateRetour.getDate().getTime()
            - txtDateEmprunt.getDate().getTime();
    long jours = diff / (1000 * 60 * 60 * 24);

    if (jours > 0) {
        delaiLabel.setText(jours + " jour(s)");
        // Couleur verte si délai raisonnable (≤ 14 jours)
        // Couleur orange si long (> 14 jours)
        if (jours <= 14) {
            delaiLabel.setBackground(new java.awt.Color(220, 252, 231));
            delaiLabel.setForeground(new java.awt.Color(22, 120, 55));
        } else {
            delaiLabel.setBackground(new java.awt.Color(255, 246, 225));
            delaiLabel.setForeground(new java.awt.Color(180, 110, 10));
        }
    } else {
        delaiLabel.setText("Date invalide");
        delaiLabel.setBackground(new java.awt.Color(255, 237, 237));
        delaiLabel.setForeground(new java.awt.Color(180, 30, 30));
    }
}}
    private void chargerDonnees() {
    // Vider d'abord
    cmbLivre.removeAllItems();
    cmbLecteur.removeAllItems();
    
    // Charger livres disponibles
    livres = empruntDAO.getLivresDisponibles();
    cmbLivre.addItem("Sélectionner un livre");
    for (Object[] livre : livres) {
        cmbLivre.addItem(livre[1].toString());
    }

    // Charger lecteurs
    lecteurs = empruntDAO.getLecteurs();
    cmbLecteur.addItem("Sélectionner un lecteur");
    for (Object[] lecteur : lecteurs) {
        cmbLecteur.addItem(lecteur[1].toString());
    }
}

    private void confirmerEmprunt() {
    // ── Récupérer l'index réel ────────────────────────────────
    int indexLivre = cmbLivre.getSelectedIndex();
    int indexLecteur = cmbLecteur.getSelectedIndex();

    // Si éditable et texte tapé à la main → chercher dans la liste
    if (indexLivre == -1) {
        String texte = cmbLivre.getEditor().getItem().toString().trim();
        for (int i = 0; i < livres.size(); i++) {
            if (livres.get(i)[1].toString().equalsIgnoreCase(texte)) {
                indexLivre = i + 1; // +1 car item 0 = "Sélectionner"
                break;
            }
        }
    }

    if (indexLecteur == -1) {
        String texte = cmbLecteur.getEditor().getItem().toString().trim();
        for (int i = 0; i < lecteurs.size(); i++) {
            if (lecteurs.get(i)[1].toString().equalsIgnoreCase(texte)) {
                indexLecteur = i + 1;
                break;
            }
        }
    }

    // ── Validations ───────────────────────────────────────────
    if (indexLivre <= 0) {
        javax.swing.JOptionPane.showMessageDialog(this,
                "Veuillez sélectionner un livre.", "Attention",
                javax.swing.JOptionPane.WARNING_MESSAGE);
        return;
    }
    if (indexLecteur <= 0) {
        javax.swing.JOptionPane.showMessageDialog(this,
                "Veuillez sélectionner un lecteur.", "Attention",
                javax.swing.JOptionPane.WARNING_MESSAGE);
        return;
    }
    if (txtDateEmprunt.getDate() == null) {
        javax.swing.JOptionPane.showMessageDialog(this,
                "Veuillez sélectionner la date d'emprunt.", "Attention",
                javax.swing.JOptionPane.WARNING_MESSAGE);
        return;
    }
    if (txtDateRetour.getDate() == null) {
        javax.swing.JOptionPane.showMessageDialog(this,
                "Veuillez sélectionner la date de retour.", "Attention",
                javax.swing.JOptionPane.WARNING_MESSAGE);
        return;
    }
// Vérifier que la date de retour est après la date d'emprunt
if (!txtDateRetour.getDate().after(txtDateEmprunt.getDate())) {
    javax.swing.JOptionPane.showMessageDialog(this,
        "La date de retour doit être après la date d'emprunt.",
        "Erreur", javax.swing.JOptionPane.ERROR_MESSAGE);
    return;
}
long diff = txtDateRetour.getDate().getTime() 
          - txtDateEmprunt.getDate().getTime();
long jours = diff / (1000 * 60 * 60 * 24);
if (jours > 30) {
    int rep = javax.swing.JOptionPane.showConfirmDialog(this,
        "La durée de l'emprunt est de " + jours + " jours.\n"
        + "La limite recommandée est 30 jours.\n"
        + "Voulez-vous quand même continuer ?",
        "Durée longue",
        javax.swing.JOptionPane.YES_NO_OPTION,
        javax.swing.JOptionPane.WARNING_MESSAGE);
    if (rep != javax.swing.JOptionPane.YES_OPTION) return;
}

// Vérifier que la date d'emprunt n'est pas dans le futur
java.util.Date aujourd = new java.util.Date();
java.util.Calendar calAuj = java.util.Calendar.getInstance();
calAuj.setTime(aujourd);
calAuj.set(java.util.Calendar.HOUR_OF_DAY, 23);
calAuj.set(java.util.Calendar.MINUTE, 59);
if (txtDateEmprunt.getDate().after(calAuj.getTime())) {
    javax.swing.JOptionPane.showMessageDialog(this,
        "La date d'emprunt ne peut pas être dans le futur.",
        "Erreur", javax.swing.JOptionPane.ERROR_MESSAGE);
    return;
}
// ── Vérification limite emprunts ──────────────────────────
// ── Récupérer les IDs ─────────────────────────────────────
int idLecteur = (int) lecteurs.get(indexLecteur - 1)[0];
int idLivre   = (int) livres.get(indexLivre - 1)[0];

// ── Vérification limite emprunts ──────────────────────────
int nbActifs = empruntDAO.getNbEmpruntsActifs(idLecteur);

if (nbActifs >= 3) {
    javax.swing.JOptionPane.showMessageDialog(this,
        "Impossible d'enregistrer cet emprunt.\n"
        + "Ce lecteur a déjà " + nbActifs + " emprunt(s) en cours.\n"
        + "Limite maximale : 3 livres à la fois.",
        "Limite atteinte",
        javax.swing.JOptionPane.WARNING_MESSAGE);
    return;
}
if (empruntDAO.lecteurADejaLivre(idLecteur, idLivre)) {
    javax.swing.JOptionPane.showMessageDialog(this,
        "Ce lecteur a déjà emprunté ce livre et ne l'a pas encore rendu !",
        "Doublon",
        javax.swing.JOptionPane.WARNING_MESSAGE);
    return;
}

    // ── Enregistrement ────────────────────────────────────────

    String dateEmprunt = new java.text.SimpleDateFormat("yyyy-MM-dd")
            .format(txtDateEmprunt.getDate());
    String dateRetour  = new java.text.SimpleDateFormat("yyyy-MM-dd")
            .format(txtDateRetour.getDate());

    int confirm = javax.swing.JOptionPane.showConfirmDialog(this,
    "Confirmer l'emprunt de « " + cmbLivre.getSelectedItem() + " »\n"
    + "pour " + cmbLecteur.getSelectedItem() + " ?",
    "Confirmation",
    javax.swing.JOptionPane.YES_NO_OPTION,
    javax.swing.JOptionPane.QUESTION_MESSAGE);
if (confirm != javax.swing.JOptionPane.YES_OPTION) return;
    boolean ok = empruntDAO.emprunter(
            idLivre, idLecteur, dateEmprunt, dateRetour);
    if (ok) {
        javax.swing.JOptionPane.showMessageDialog(
                this, "Emprunt enregistré !");
        succes = true;
        dispose();
    } else {
        javax.swing.JOptionPane.showMessageDialog(this,
                "Erreur : livre indisponible ou erreur BD.", "Erreur",
                javax.swing.JOptionPane.ERROR_MESSAGE);
    }
}
    public boolean isSucces() {
        return succes;
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
        jLabel1 = new javax.swing.JLabel();
        formPanel = new javax.swing.JPanel();
        lblLivre = new javax.swing.JLabel();
        cmbLivre = new javax.swing.JComboBox<>();
        jPanel1 = new javax.swing.JPanel();
        lblLecteur = new javax.swing.JLabel();
        cmbLecteur = new javax.swing.JComboBox<>();
        jPanel2 = new javax.swing.JPanel();
        datesPanel = new javax.swing.JPanel();
        panelGauche = new javax.swing.JPanel();
        lblDateEmprunt = new javax.swing.JLabel();
        txtDateEmprunt = new com.toedter.calendar.JDateChooser();
        panelDroit = new javax.swing.JPanel();
        lblDateRetourPrevue = new javax.swing.JLabel();
        txtDateRetour = new com.toedter.calendar.JDateChooser();
        apercuPanel = new javax.swing.JPanel();
        apercuIco = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        apercuTexte = new javax.swing.JPanel();
        lblApercuLivre = new javax.swing.JLabel();
        lblApercuLecteur = new javax.swing.JLabel();
        delaiLabel = new javax.swing.JLabel();
        buttonsPanel = new javax.swing.JPanel();
        btnAnnuler = new javax.swing.JButton();
        btnEnregistrer = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Nouvel emprunt");
        setAlwaysOnTop(true);
        setPreferredSize(new java.awt.Dimension(600, 500));

        titlePanel.setBackground(new java.awt.Color(26, 32, 53));
        titlePanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(14, 20, 14, 20));
        titlePanel.setPreferredSize(new java.awt.Dimension(0, 70));
        titlePanel.setLayout(new java.awt.BorderLayout());

        lblTitle.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        lblTitle.setForeground(new java.awt.Color(255, 255, 255));
        lblTitle.setText("Nouvel emprunt");
        lblTitle.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 4, 0));
        titlePanel.add(lblTitle, java.awt.BorderLayout.CENTER);

        jLabel1.setForeground(new java.awt.Color(136, 146, 164));
        jLabel1.setText("Remplissez les informations de l''emprunt");
        titlePanel.add(jLabel1, java.awt.BorderLayout.PAGE_END);

        getContentPane().add(titlePanel, java.awt.BorderLayout.NORTH);

        formPanel.setBackground(new java.awt.Color(245, 240, 232));
        formPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(24, 28, 16, 28));
        formPanel.setLayout(new javax.swing.BoxLayout(formPanel, javax.swing.BoxLayout.Y_AXIS));

        lblLivre.setFont(new java.awt.Font("Segoe UI", 1, 11)); // NOI18N
        lblLivre.setForeground(new java.awt.Color(90, 96, 112));
        lblLivre.setText("Livre");
        lblLivre.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 6, 0));
        formPanel.add(lblLivre);

        cmbLivre.setEditable(true);
        cmbLivre.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        cmbLivre.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Roman", "Science", "Art", " " }));
        cmbLivre.setAlignmentX(0.0F);
        cmbLivre.setMaximumSize(new java.awt.Dimension(32767, 40));
        cmbLivre.setPreferredSize(new java.awt.Dimension(0, 40));
        cmbLivre.addActionListener(this::cmbLivreActionPerformed);
        formPanel.add(cmbLivre);

        jPanel1.setBackground(new java.awt.Color(245, 240, 232));
        jPanel1.setAlignmentX(0.0F);
        jPanel1.setMaximumSize(new java.awt.Dimension(32767, 16));
        jPanel1.setPreferredSize(new java.awt.Dimension(0, 16));
        formPanel.add(jPanel1);

        lblLecteur.setFont(new java.awt.Font("Segoe UI", 1, 11)); // NOI18N
        lblLecteur.setForeground(new java.awt.Color(90, 96, 112));
        lblLecteur.setText("Lecteur");
        lblLecteur.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 6, 0));
        formPanel.add(lblLecteur);

        cmbLecteur.setEditable(true);
        cmbLecteur.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        cmbLecteur.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Roman", "Science", "Art", " " }));
        cmbLecteur.setAlignmentX(0.0F);
        cmbLecteur.setMaximumSize(new java.awt.Dimension(32767, 40));
        cmbLecteur.setPreferredSize(new java.awt.Dimension(0, 40));
        cmbLecteur.addActionListener(this::cmbLecteurActionPerformed);
        formPanel.add(cmbLecteur);

        jPanel2.setBackground(new java.awt.Color(245, 240, 232));
        jPanel2.setAlignmentX(0.0F);
        jPanel2.setMaximumSize(new java.awt.Dimension(32767, 16));
        jPanel2.setName(""); // NOI18N
        jPanel2.setPreferredSize(new java.awt.Dimension(0, 16));
        formPanel.add(jPanel2);

        datesPanel.setBackground(new java.awt.Color(245, 240, 232));
        datesPanel.setAlignmentX(0.0F);
        datesPanel.setMaximumSize(new java.awt.Dimension(32767, 70));
        datesPanel.setName(""); // NOI18N
        datesPanel.setPreferredSize(new java.awt.Dimension(0, 76));
        datesPanel.setLayout(new java.awt.GridLayout(1, 2, 12, 0));

        panelGauche.setBackground(new java.awt.Color(245, 240, 232));
        panelGauche.setLayout(new javax.swing.BoxLayout(panelGauche, javax.swing.BoxLayout.Y_AXIS));

        lblDateEmprunt.setFont(new java.awt.Font("Segoe UI", 1, 11)); // NOI18N
        lblDateEmprunt.setForeground(new java.awt.Color(90, 96, 112));
        lblDateEmprunt.setText("Date emprunt");
        lblDateEmprunt.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 6, 0));
        panelGauche.add(lblDateEmprunt);

        txtDateEmprunt.setAlignmentX(0.0F);
        txtDateEmprunt.setMaximumSize(new java.awt.Dimension(32767, 40));
        txtDateEmprunt.setPreferredSize(new java.awt.Dimension(0, 40));
        panelGauche.add(txtDateEmprunt);

        datesPanel.add(panelGauche);

        panelDroit.setBackground(new java.awt.Color(245, 240, 232));
        panelDroit.setLayout(new javax.swing.BoxLayout(panelDroit, javax.swing.BoxLayout.Y_AXIS));

        lblDateRetourPrevue.setFont(new java.awt.Font("Segoe UI", 1, 11)); // NOI18N
        lblDateRetourPrevue.setForeground(new java.awt.Color(90, 96, 112));
        lblDateRetourPrevue.setText("Date retour");
        lblDateRetourPrevue.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 6, 0));
        panelDroit.add(lblDateRetourPrevue);

        txtDateRetour.setAlignmentX(0.0F);
        txtDateRetour.setMaximumSize(new java.awt.Dimension(32767, 40));
        txtDateRetour.setPreferredSize(new java.awt.Dimension(0, 40));
        panelDroit.add(txtDateRetour);

        datesPanel.add(panelDroit);

        formPanel.add(datesPanel);

        apercuPanel.setBackground(new java.awt.Color(255, 255, 255));
        apercuPanel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(232, 226, 216)));
        apercuPanel.setAlignmentX(0.0F);
        apercuPanel.setMaximumSize(new java.awt.Dimension(32767, 70));
        apercuPanel.setPreferredSize(new java.awt.Dimension(0, 70));
        apercuPanel.setLayout(new java.awt.BorderLayout());

        apercuIco.setBackground(new java.awt.Color(230, 240, 255));
        apercuIco.setPreferredSize(new java.awt.Dimension(46, 0));

        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/open-book.png"))); // NOI18N
        jLabel3.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 14));
        apercuIco.add(jLabel3);

        apercuPanel.add(apercuIco, java.awt.BorderLayout.WEST);

        apercuTexte.setBackground(new java.awt.Color(255, 255, 255));
        apercuTexte.setLayout(new javax.swing.BoxLayout(apercuTexte, javax.swing.BoxLayout.Y_AXIS));

        lblApercuLivre.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        lblApercuLivre.setForeground(new java.awt.Color(154, 160, 176));
        lblApercuLivre.setText("- Sélectionnez un lecteur");
        apercuTexte.add(lblApercuLivre);

        lblApercuLecteur.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        lblApercuLecteur.setForeground(new java.awt.Color(154, 160, 176));
        lblApercuLecteur.setText("- Sélectionnez un lecteur");
        lblApercuLecteur.setToolTipText("");
        apercuTexte.add(lblApercuLecteur);

        apercuPanel.add(apercuTexte, java.awt.BorderLayout.CENTER);

        delaiLabel.setBackground(new java.awt.Color(230, 240, 255));
        delaiLabel.setForeground(new java.awt.Color(24, 95, 165));
        delaiLabel.setText("14 jours");
        delaiLabel.setBorder(javax.swing.BorderFactory.createEmptyBorder(3, 12, 3, 12));
        delaiLabel.setOpaque(true);
        apercuPanel.add(delaiLabel, java.awt.BorderLayout.EAST);

        formPanel.add(apercuPanel);

        getContentPane().add(formPanel, java.awt.BorderLayout.CENTER);

        buttonsPanel.setBackground(new java.awt.Color(255, 255, 255));
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
        btnEnregistrer.setText("Emprunter");
        btnEnregistrer.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnEnregistrer.setFocusPainted(false);
        buttonsPanel.add(btnEnregistrer);

        getContentPane().add(buttonsPanel, java.awt.BorderLayout.SOUTH);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void cmbLivreActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbLivreActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cmbLivreActionPerformed

    private void cmbLecteurActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbLecteurActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cmbLecteurActionPerformed

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
                AjouterEmprunt dialog = new AjouterEmprunt(new javax.swing.JFrame(), true);
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
    private javax.swing.JPanel apercuIco;
    private javax.swing.JPanel apercuPanel;
    private javax.swing.JPanel apercuTexte;
    private javax.swing.JButton btnAnnuler;
    private javax.swing.JButton btnEnregistrer;
    private javax.swing.JPanel buttonsPanel;
    private javax.swing.JComboBox<String> cmbLecteur;
    private javax.swing.JComboBox<String> cmbLivre;
    private javax.swing.JPanel datesPanel;
    private javax.swing.JLabel delaiLabel;
    private javax.swing.JPanel formPanel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JLabel lblApercuLecteur;
    private javax.swing.JLabel lblApercuLivre;
    private javax.swing.JLabel lblDateEmprunt;
    private javax.swing.JLabel lblDateRetourPrevue;
    private javax.swing.JLabel lblLecteur;
    private javax.swing.JLabel lblLivre;
    private javax.swing.JLabel lblTitle;
    private javax.swing.JPanel panelDroit;
    private javax.swing.JPanel panelGauche;
    private javax.swing.JPanel titlePanel;
    private com.toedter.calendar.JDateChooser txtDateEmprunt;
    private com.toedter.calendar.JDateChooser txtDateRetour;
    // End of variables declaration//GEN-END:variables
}

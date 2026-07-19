/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package views;

/**
 *
 * @author Admin
 */
public class RetourEmprunt extends javax.swing.JDialog {

    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(RetourEmprunt.class.getName());

    /**
     * Creates new form RetourEmprunt
     */
    private String dateEmpruntStr; // à déclarer en haut avec les autres attributs
    private int idEmprunt;
    private String dateRetourPrevue;
    private dao.EmpruntDAO empruntDAO = new dao.EmpruntDAO();

    private static final int TARIF_JOUR = 100;
    private static final int AMENDE_USE = 500;
    private static final int AMENDE_ABIME = 2000;

    public RetourEmprunt(java.awt.Frame parent, boolean modal,
            int idEmprunt, String titre, String lecteur,
            String dateEmprunt, String dateRetourPrevue, String statut) {
        super(parent, modal);
        initComponents();
        setLocationRelativeTo(parent);
        // ... reste du code

        this.idEmprunt = idEmprunt;
        this.dateRetourPrevue = dateRetourPrevue;
        this.dateEmpruntStr = dateEmprunt;

        // Remplir les infos
        lblValLivre.setText(titre);
        lblValLecteur.setText(lecteur);
        lblValEmprunt.setText(dateEmprunt);
        lblValPrevue.setText(dateRetourPrevue);

        // Statut coloré
        switch (statut) {
            case "retard":
                lblValStatut.setText("En retard");
                lblValStatut.setForeground(new java.awt.Color(224, 82, 82));
                break;
            case "en cours":
                lblValStatut.setText("En cours");
                lblValStatut.setForeground(new java.awt.Color(224, 154, 48));
                break;
            default:
                lblValStatut.setText(statut);
                lblValStatut.setForeground(new java.awt.Color(59, 173, 114));
        }

        // Date retour = aujourd'hui par défaut
        dateRetour.setDate(new java.util.Date());

        // ButtonGroup pour les radio
        javax.swing.ButtonGroup grp = new javax.swing.ButtonGroup();
        grp.add(rdoBon);
        grp.add(rdoUse);
        grp.add(rdoAbime);

        // Calcul amende à chaque changement de date
        dateRetour.addPropertyChangeListener("date", evt -> calculerAmende());

        // Calcul amende à chaque changement d'état
        rdoBon.addActionListener(e -> calculerAmende());
        rdoUse.addActionListener(e -> calculerAmende());
        rdoAbime.addActionListener(e -> calculerAmende());

        // Calcul initial
        calculerAmende();

        // Boutons
        btnAnnuler.addActionListener(e -> dispose());
        btnConfirmer.addActionListener(e -> confirmerRetour());
        dateRetour.setMaxSelectableDate(new java.util.Date());
        
    }

    private void calculerAmende() {
        int joursRetard = 0;
        int amende = 0;

        if (dateRetour.getDate() != null && dateRetourPrevue != null) {
            try {
                java.text.SimpleDateFormat sdf
                        = new java.text.SimpleDateFormat("yyyy-MM-dd");
                java.util.Date prevue = sdf.parse(dateRetourPrevue);
                java.util.Date reelle = dateRetour.getDate();

                long diff = reelle.getTime() - prevue.getTime();
                joursRetard = (int) (diff / (1000 * 60 * 60 * 24));

                if (joursRetard > 0) {
                    amende += joursRetard * TARIF_JOUR;
                    lblValRetard.setText(joursRetard + " jour(s)");
                    lblValRetard.setForeground(new java.awt.Color(224, 82, 82));
                } else {
                    lblValRetard.setText("0 jour");
                    lblValRetard.setForeground(new java.awt.Color(59, 173, 114));
                }
            } catch (Exception ex) {
                lblValRetard.setText("--");
            }
        }

        // Amende état du livre
        if (rdoAbime.isSelected()) {
            amende += AMENDE_ABIME;
        } else if (rdoUse.isSelected()) {
            amende += AMENDE_USE;
        }

        // Afficher amende
        lblAmende.setText(amende + " FCFA");
        if (amende > 0) {
            lblAmende.setForeground(new java.awt.Color(224, 82, 82));
            amendePanel.setBackground(new java.awt.Color(255, 240, 240));
            amendePanel.setBorder(javax.swing.BorderFactory.createCompoundBorder(
                    javax.swing.BorderFactory.createLineBorder(
                            new java.awt.Color(224, 82, 82)),
                    javax.swing.BorderFactory.createEmptyBorder(12, 16, 12, 16)));
        } else {
            lblAmende.setForeground(new java.awt.Color(59, 173, 114));
            amendePanel.setBackground(new java.awt.Color(238, 251, 244));
            amendePanel.setBorder(javax.swing.BorderFactory.createCompoundBorder(
                    javax.swing.BorderFactory.createLineBorder(
                            new java.awt.Color(59, 173, 114)),
                    javax.swing.BorderFactory.createEmptyBorder(12, 16, 12, 16)));
        }
    }

    private void confirmerRetour() {
        if (dateRetour.getDate() == null) {
            javax.swing.JOptionPane.showMessageDialog(this,
                    "Veuillez sélectionner la date de retour.",
                    "Erreur", javax.swing.JOptionPane.ERROR_MESSAGE);
            return;
        }
// Date de retour ne peut pas être dans le futur
        java.util.Calendar now = java.util.Calendar.getInstance();
        now.set(java.util.Calendar.HOUR_OF_DAY, 23);
        now.set(java.util.Calendar.MINUTE, 59);
        if (dateRetour.getDate().after(now.getTime())) {
            javax.swing.JOptionPane.showMessageDialog(this,
                    "La date de retour ne peut pas être dans le futur.",
                    "Erreur", javax.swing.JOptionPane.ERROR_MESSAGE);
            return;
        }

// Date de retour ne peut pas être avant la date d'emprunt
       try {
    java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
    java.util.Date debutEmprunt = sdf.parse(dateEmpruntStr);
    if (dateRetour.getDate().before(debutEmprunt)) {
        javax.swing.JOptionPane.showMessageDialog(this,
            "La date de retour ne peut pas être avant\n"
            + "la date d'emprunt (" + lblValEmprunt.getText() + ") !",
            "Erreur", javax.swing.JOptionPane.ERROR_MESSAGE);
        return;
    }
} catch (Exception ex) {
    // date mal formatée — on laisse passer
}
        String dateReelle = new java.text.SimpleDateFormat("yyyy-MM-dd")
                .format(dateRetour.getDate());

        String etat = rdoBon.isSelected() ? "bon"
                : rdoUse.isSelected() ? "use" : "abime";

        String remarque = txtRemarque.getText().trim();
        if (remarque.length() > 300) {
    javax.swing.JOptionPane.showMessageDialog(this,
        "La remarque ne peut pas dépasser 300 caractères !",
        "Erreur", javax.swing.JOptionPane.ERROR_MESSAGE);
    return;
}

        // Calcul amende finale
        int amende = 0;
        try {
            java.text.SimpleDateFormat sdf
                    = new java.text.SimpleDateFormat("yyyy-MM-dd");
            java.util.Date prevue = sdf.parse(dateRetourPrevue);
            java.util.Date reelle = dateRetour.getDate();
            long diff = reelle.getTime() - prevue.getTime();
            int joursRetard = (int) (diff / (1000 * 60 * 60 * 24));
            if (joursRetard > 0) {
                amende += joursRetard * TARIF_JOUR;
            }
        } catch (Exception ex) {
        }

        if (rdoAbime.isSelected()) {
            amende += AMENDE_ABIME;
        } else if (rdoUse.isSelected()) {
            amende += AMENDE_USE;
        }

        // Confirmation si amende > 0
        if (amende > 0) {
            int rep = javax.swing.JOptionPane.showConfirmDialog(this,
                    "Une amende de " + amende + " FCFA sera enregistrée.\n"
                    + "Confirmer le retour ?",
                    "Confirmation", javax.swing.JOptionPane.YES_NO_OPTION);
            if (rep != javax.swing.JOptionPane.YES_OPTION) {
                return;
            }
        }

        if (empruntDAO.retourner(idEmprunt, amende, dateReelle, etat, remarque)) {
            javax.swing.JOptionPane.showMessageDialog(this,
                    amende > 0
                            ? "Retour enregistré.\nAmende : " + amende + " FCFA"
                            : "Retour enregistré avec succès !",
                    "Succès", javax.swing.JOptionPane.INFORMATION_MESSAGE);
            dispose();
        } else {
            javax.swing.JOptionPane.showMessageDialog(this,
                    "Erreur lors de l'enregistrement.",
                    "Erreur", javax.swing.JOptionPane.ERROR_MESSAGE);
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
        jPanel2 = new javax.swing.JPanel();
        headerCenter = new javax.swing.JPanel();
        lblTitreDialog = new javax.swing.JLabel();
        mainPanel = new javax.swing.JPanel();
        infoPanel = new javax.swing.JPanel();
        cel1 = new javax.swing.JPanel();
        lblTitreLivre = new javax.swing.JLabel();
        lblValLivre = new javax.swing.JLabel();
        cel2 = new javax.swing.JPanel();
        lblTitreLecteur = new javax.swing.JLabel();
        lblValLecteur = new javax.swing.JLabel();
        cel3 = new javax.swing.JPanel();
        lblTitreStatut = new javax.swing.JLabel();
        lblValStatut = new javax.swing.JLabel();
        cel4 = new javax.swing.JPanel();
        lblTiteEmprunt = new javax.swing.JLabel();
        lblValEmprunt = new javax.swing.JLabel();
        cel5 = new javax.swing.JPanel();
        lblTitrePrevue = new javax.swing.JLabel();
        lblValPrevue = new javax.swing.JLabel();
        cel6 = new javax.swing.JPanel();
        lblTitreRetard = new javax.swing.JLabel();
        lblValRetard = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        formPanel = new javax.swing.JPanel();
        lblRetour = new javax.swing.JLabel();
        dateRetour = new com.toedter.calendar.JDateChooser();
        jPanel3 = new javax.swing.JPanel();
        lblEtat = new javax.swing.JLabel();
        etatPanel = new javax.swing.JPanel();
        rdoBon = new javax.swing.JRadioButton();
        rdoUse = new javax.swing.JRadioButton();
        rdoAbime = new javax.swing.JRadioButton();
        jPanel4 = new javax.swing.JPanel();
        lblRemarque = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtRemarque = new javax.swing.JTextArea();
        jPanel5 = new javax.swing.JPanel();
        amendePanel = new javax.swing.JPanel();
        lblAmendeCalculee = new javax.swing.JLabel();
        lblAmende = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        panelActions = new javax.swing.JPanel();
        btnAnnuler = new javax.swing.JButton();
        btnConfirmer = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Retour de livre");
        setPreferredSize(new java.awt.Dimension(520, 580));
        setResizable(false);

        headerPanel.setBackground(new java.awt.Color(26, 32, 53));
        headerPanel.setForeground(new java.awt.Color(255, 255, 255));
        headerPanel.setPreferredSize(new java.awt.Dimension(0, 60));
        headerPanel.setLayout(new java.awt.BorderLayout());

        jPanel2.setBackground(new java.awt.Color(59, 173, 114));
        jPanel2.setPreferredSize(new java.awt.Dimension(0, 5));

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 533, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 5, Short.MAX_VALUE)
        );

        headerPanel.add(jPanel2, java.awt.BorderLayout.NORTH);

        headerCenter.setBackground(new java.awt.Color(26, 32, 53));
        headerCenter.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        headerCenter.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 20, 0));

        lblTitreDialog.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        lblTitreDialog.setForeground(new java.awt.Color(255, 255, 255));
        lblTitreDialog.setText("Retour de livre");
        headerCenter.add(lblTitreDialog);

        headerPanel.add(headerCenter, java.awt.BorderLayout.CENTER);

        getContentPane().add(headerPanel, java.awt.BorderLayout.NORTH);

        mainPanel.setBackground(new java.awt.Color(245, 240, 232));
        mainPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(16, 20, 16, 20));
        mainPanel.setLayout(new javax.swing.BoxLayout(mainPanel, javax.swing.BoxLayout.Y_AXIS));

        infoPanel.setBackground(new java.awt.Color(255, 255, 255));
        infoPanel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(232, 226, 216)));
        infoPanel.setAlignmentX(0.0F);
        infoPanel.setMaximumSize(new java.awt.Dimension(32767, 110));
        infoPanel.setPreferredSize(new java.awt.Dimension(0, 100));
        infoPanel.setLayout(new java.awt.GridLayout(2, 3));

        cel1.setBackground(new java.awt.Color(255, 255, 255));
        cel1.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 14, 10, 14));
        cel1.setLayout(new java.awt.BorderLayout());

        lblTitreLivre.setFont(new java.awt.Font("Segoe UI", 1, 10)); // NOI18N
        lblTitreLivre.setForeground(new java.awt.Color(154, 160, 176));
        lblTitreLivre.setText("LIVRE");
        cel1.add(lblTitreLivre, java.awt.BorderLayout.CENTER);

        lblValLivre.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        lblValLivre.setForeground(new java.awt.Color(26, 32, 53));
        lblValLivre.setText("--");
        cel1.add(lblValLivre, java.awt.BorderLayout.PAGE_END);

        infoPanel.add(cel1);

        cel2.setBackground(new java.awt.Color(255, 255, 255));
        cel2.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 14, 10, 14));
        cel2.setLayout(new java.awt.BorderLayout());

        lblTitreLecteur.setFont(new java.awt.Font("Segoe UI", 1, 10)); // NOI18N
        lblTitreLecteur.setForeground(new java.awt.Color(154, 160, 176));
        lblTitreLecteur.setText("LECTEUR");
        cel2.add(lblTitreLecteur, java.awt.BorderLayout.CENTER);

        lblValLecteur.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        lblValLecteur.setForeground(new java.awt.Color(26, 32, 53));
        lblValLecteur.setText("--");
        cel2.add(lblValLecteur, java.awt.BorderLayout.PAGE_END);

        infoPanel.add(cel2);

        cel3.setBackground(new java.awt.Color(255, 255, 255));
        cel3.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 14, 10, 14));
        cel3.setLayout(new java.awt.BorderLayout());

        lblTitreStatut.setFont(new java.awt.Font("Segoe UI", 1, 10)); // NOI18N
        lblTitreStatut.setForeground(new java.awt.Color(154, 160, 176));
        lblTitreStatut.setText("STATUT");
        cel3.add(lblTitreStatut, java.awt.BorderLayout.CENTER);

        lblValStatut.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        lblValStatut.setForeground(new java.awt.Color(26, 32, 53));
        lblValStatut.setText("--");
        cel3.add(lblValStatut, java.awt.BorderLayout.PAGE_END);

        infoPanel.add(cel3);

        cel4.setBackground(new java.awt.Color(255, 255, 255));
        cel4.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 14, 10, 14));
        cel4.setLayout(new java.awt.BorderLayout());

        lblTiteEmprunt.setFont(new java.awt.Font("Segoe UI", 1, 10)); // NOI18N
        lblTiteEmprunt.setForeground(new java.awt.Color(154, 160, 176));
        lblTiteEmprunt.setText("EMPRUNTÉ LE");
        cel4.add(lblTiteEmprunt, java.awt.BorderLayout.CENTER);

        lblValEmprunt.setForeground(new java.awt.Color(90, 96, 112));
        lblValEmprunt.setText("--");
        cel4.add(lblValEmprunt, java.awt.BorderLayout.PAGE_END);

        infoPanel.add(cel4);

        cel5.setBackground(new java.awt.Color(255, 255, 255));
        cel5.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 14, 10, 14));
        cel5.setLayout(new java.awt.BorderLayout());

        lblTitrePrevue.setFont(new java.awt.Font("Segoe UI", 1, 10)); // NOI18N
        lblTitrePrevue.setForeground(new java.awt.Color(154, 160, 176));
        lblTitrePrevue.setText("RETOUR PRÉVU");
        cel5.add(lblTitrePrevue, java.awt.BorderLayout.CENTER);

        lblValPrevue.setForeground(new java.awt.Color(224, 82, 82));
        lblValPrevue.setText("--");
        cel5.add(lblValPrevue, java.awt.BorderLayout.PAGE_END);

        infoPanel.add(cel5);

        cel6.setBackground(new java.awt.Color(255, 255, 255));
        cel6.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 14, 10, 14));
        cel6.setLayout(new java.awt.BorderLayout());

        lblTitreRetard.setFont(new java.awt.Font("Segoe UI", 1, 10)); // NOI18N
        lblTitreRetard.setForeground(new java.awt.Color(154, 160, 176));
        lblTitreRetard.setText("JOURS RETARD");
        cel6.add(lblTitreRetard, java.awt.BorderLayout.CENTER);

        lblValRetard.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        lblValRetard.setForeground(new java.awt.Color(224, 82, 82));
        lblValRetard.setText("--");
        cel6.add(lblValRetard, java.awt.BorderLayout.PAGE_END);

        infoPanel.add(cel6);

        mainPanel.add(infoPanel);

        jPanel1.setBackground(new java.awt.Color(245, 240, 232));
        jPanel1.setMaximumSize(new java.awt.Dimension(32767, 14));
        jPanel1.setPreferredSize(new java.awt.Dimension(0, 14));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 493, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 14, Short.MAX_VALUE)
        );

        mainPanel.add(jPanel1);

        formPanel.setBackground(new java.awt.Color(255, 255, 255));
        formPanel.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(232, 226, 216)), javax.swing.BorderFactory.createEmptyBorder(16, 16, 16, 16)));
        formPanel.setAlignmentX(0.0F);
        formPanel.setLayout(new javax.swing.BoxLayout(formPanel, javax.swing.BoxLayout.Y_AXIS));

        lblRetour.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblRetour.setForeground(new java.awt.Color(26, 32, 53));
        lblRetour.setText("Date retour réelle");
        lblRetour.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 6, 0));
        formPanel.add(lblRetour);

        dateRetour.setAlignmentX(0.0F);
        dateRetour.setMaximumSize(new java.awt.Dimension(32767, 38));
        dateRetour.setPreferredSize(new java.awt.Dimension(0, 38));
        formPanel.add(dateRetour);

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));
        jPanel3.setMaximumSize(new java.awt.Dimension(32767, 14));
        jPanel3.setPreferredSize(new java.awt.Dimension(0, 14));

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 459, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 14, Short.MAX_VALUE)
        );

        formPanel.add(jPanel3);

        lblEtat.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblEtat.setForeground(new java.awt.Color(26, 32, 53));
        lblEtat.setText("État du livre");
        lblEtat.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 6, 0));
        formPanel.add(lblEtat);

        etatPanel.setBackground(new java.awt.Color(255, 255, 255));
        etatPanel.setAlignmentX(0.0F);
        etatPanel.setMaximumSize(new java.awt.Dimension(32767, 36));
        etatPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 10, 0));

        rdoBon.setSelected(true);
        rdoBon.setText("Bon état");
        etatPanel.add(rdoBon);

        rdoUse.setText("Usé");
        etatPanel.add(rdoUse);

        rdoAbime.setText("Abîmé");
        etatPanel.add(rdoAbime);

        formPanel.add(etatPanel);

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));
        jPanel4.setMaximumSize(new java.awt.Dimension(32767, 14));
        jPanel4.setPreferredSize(new java.awt.Dimension(0, 14));

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 459, Short.MAX_VALUE)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 14, Short.MAX_VALUE)
        );

        formPanel.add(jPanel4);

        lblRemarque.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblRemarque.setForeground(new java.awt.Color(26, 32, 53));
        lblRemarque.setText("Remarque (optionnel)");
        lblRemarque.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 6, 0));
        formPanel.add(lblRemarque);

        jScrollPane1.setAlignmentX(0.0F);
        jScrollPane1.setMaximumSize(new java.awt.Dimension(32767, 70));
        jScrollPane1.setPreferredSize(new java.awt.Dimension(0, 70));

        txtRemarque.setColumns(20);
        txtRemarque.setLineWrap(true);
        txtRemarque.setRows(3);
        txtRemarque.setWrapStyleWord(true);
        txtRemarque.setAlignmentX(0.0F);
        jScrollPane1.setViewportView(txtRemarque);

        formPanel.add(jScrollPane1);

        mainPanel.add(formPanel);

        jPanel5.setBackground(new java.awt.Color(245, 240, 232));
        jPanel5.setMaximumSize(new java.awt.Dimension(32767, 14));
        jPanel5.setPreferredSize(new java.awt.Dimension(0, 14));

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 493, Short.MAX_VALUE)
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 14, Short.MAX_VALUE)
        );

        mainPanel.add(jPanel5);

        amendePanel.setBackground(new java.awt.Color(255, 248, 236));
        amendePanel.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(224, 154, 48)), javax.swing.BorderFactory.createEmptyBorder(12, 16, 12, 16)));
        amendePanel.setAlignmentX(0.0F);
        amendePanel.setMaximumSize(new java.awt.Dimension(32767, 50));
        amendePanel.setLayout(new java.awt.BorderLayout());

        lblAmendeCalculee.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblAmendeCalculee.setForeground(new java.awt.Color(26, 32, 53));
        lblAmendeCalculee.setText("Amende calculée");
        amendePanel.add(lblAmendeCalculee, java.awt.BorderLayout.WEST);

        lblAmende.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        lblAmende.setForeground(new java.awt.Color(224, 154, 48));
        lblAmende.setText("0 FCFA");
        amendePanel.add(lblAmende, java.awt.BorderLayout.EAST);

        mainPanel.add(amendePanel);

        jPanel6.setBackground(new java.awt.Color(245, 240, 232));
        jPanel6.setMaximumSize(new java.awt.Dimension(32767, 14));
        jPanel6.setPreferredSize(new java.awt.Dimension(0, 14));

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 493, Short.MAX_VALUE)
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 14, Short.MAX_VALUE)
        );

        mainPanel.add(jPanel6);

        panelActions.setBackground(new java.awt.Color(245, 240, 232));
        panelActions.setAlignmentX(0.0F);
        panelActions.setMaximumSize(new java.awt.Dimension(32767, 44));
        panelActions.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT, 10, 0));

        btnAnnuler.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnAnnuler.setForeground(new java.awt.Color(90, 96, 112));
        btnAnnuler.setText("Annuler");
        btnAnnuler.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(232, 223, 216)));
        btnAnnuler.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnAnnuler.setPreferredSize(new java.awt.Dimension(100, 38));
        panelActions.add(btnAnnuler);

        btnConfirmer.setBackground(new java.awt.Color(59, 173, 114));
        btnConfirmer.setForeground(new java.awt.Color(255, 255, 255));
        btnConfirmer.setText("Confirmer le retour");
        btnConfirmer.setBorder(javax.swing.BorderFactory.createEmptyBorder(6, 16, 6, 16));
        btnConfirmer.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnConfirmer.setFocusPainted(false);
        btnConfirmer.setPreferredSize(new java.awt.Dimension(180, 38));
        panelActions.add(btnConfirmer);

        mainPanel.add(panelActions);

        getContentPane().add(mainPanel, java.awt.BorderLayout.CENTER);

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
                RetourEmprunt dialog = new RetourEmprunt(new javax.swing.JFrame(), true,
                        0, "Test", "Test", "2025-01-01", "2025-01-15", "en cours");
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
    private javax.swing.JPanel amendePanel;
    private javax.swing.JButton btnAnnuler;
    private javax.swing.JButton btnConfirmer;
    private javax.swing.JPanel cel1;
    private javax.swing.JPanel cel2;
    private javax.swing.JPanel cel3;
    private javax.swing.JPanel cel4;
    private javax.swing.JPanel cel5;
    private javax.swing.JPanel cel6;
    private com.toedter.calendar.JDateChooser dateRetour;
    private javax.swing.JPanel etatPanel;
    private javax.swing.JPanel formPanel;
    private javax.swing.JPanel headerCenter;
    private javax.swing.JPanel headerPanel;
    private javax.swing.JPanel infoPanel;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblAmende;
    private javax.swing.JLabel lblAmendeCalculee;
    private javax.swing.JLabel lblEtat;
    private javax.swing.JLabel lblRemarque;
    private javax.swing.JLabel lblRetour;
    private javax.swing.JLabel lblTiteEmprunt;
    private javax.swing.JLabel lblTitreDialog;
    private javax.swing.JLabel lblTitreLecteur;
    private javax.swing.JLabel lblTitreLivre;
    private javax.swing.JLabel lblTitrePrevue;
    private javax.swing.JLabel lblTitreRetard;
    private javax.swing.JLabel lblTitreStatut;
    private javax.swing.JLabel lblValEmprunt;
    private javax.swing.JLabel lblValLecteur;
    private javax.swing.JLabel lblValLivre;
    private javax.swing.JLabel lblValPrevue;
    private javax.swing.JLabel lblValRetard;
    private javax.swing.JLabel lblValStatut;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JPanel panelActions;
    private javax.swing.JRadioButton rdoAbime;
    private javax.swing.JRadioButton rdoBon;
    private javax.swing.JRadioButton rdoUse;
    private javax.swing.JTextArea txtRemarque;
    // End of variables declaration//GEN-END:variables
}

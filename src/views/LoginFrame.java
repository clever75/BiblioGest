/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package views;

import com.formdev.flatlaf.FlatLightLaf;
import dao.LoginDAO;
import java.awt.Color;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import models.Utilisateur;

/**
 *
 * @author Admin
 */
public class LoginFrame extends JFrame {

    private static final java.util.logging.Logger logger = Logger.getLogger(LoginFrame.class.getName());

    /**
     * Creates new form LoginFrame
     */
    private LoginDAO loginDAO = new LoginDAO();

    public LoginFrame() {
        initComponents();
        setLocationRelativeTo(null);
        getContentPane().setBackground(Color.WHITE);
        pack();
        btnConnexion.putClientProperty("JButton.buttonType", "roundRect");

        // Arrondi FlatLaf
        System.out.println(UIManager.getLookAndFeel());
        btnConnexion.putClientProperty("JButton.buttonType", "roundRect");
        txtUsername.putClientProperty("JTextField.arc", 8);
        txtPassword.putClientProperty("JTextField.arc", 8);

        // Vider le mot de passe
        txtPassword.setText("");

        // Action bouton
        btnConnexion.addActionListener(e -> seConnecter());
    }

    private void seConnecter() {
        String username = txtUsername.getText().trim();
        String password = new String(txtPassword.getPassword()).trim();

        if (username.isEmpty() || password.isEmpty()) {
            lblErreur.setText("Veuillez remplir tous les champs.");
            return;
        }

        Utilisateur user = loginDAO.connecter(username, password);

        if (user != null) {
            new MainFrame(user).setVisible(true);
            dispose();
        } else {
            lblErreur.setText("Identifiants incorrects.");
            txtPassword.setText("");
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        brandPanel = new javax.swing.JPanel();
        brandContent = new javax.swing.JPanel();
        icoLogin = new javax.swing.JLabel();
        lblBrandTitle = new javax.swing.JLabel();
        lblBrandSub = new javax.swing.JLabel();
        lblSlogan = new javax.swing.JLabel();
        formPanel = new javax.swing.JPanel();
        formContent = new javax.swing.JPanel();
        lblFormTitle = new javax.swing.JLabel();
        lblFormSub = new javax.swing.JLabel();
        lblUser = new javax.swing.JLabel();
        txtUsername = new javax.swing.JTextField();
        jPanel1 = new javax.swing.JPanel();
        lblPassword = new javax.swing.JLabel();
        txtPassword = new javax.swing.JPasswordField();
        jPanel2 = new javax.swing.JPanel();
        btnConnexion = new javax.swing.JButton();
        lblErreur = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Page de connexion");
        setAutoRequestFocus(false);
        setBackground(new java.awt.Color(255, 255, 255));
        setMinimumSize(new java.awt.Dimension(800, 500));
        setPreferredSize(new java.awt.Dimension(900, 480));

        brandPanel.setBackground(new java.awt.Color(26, 32, 53));
        brandPanel.setPreferredSize(new java.awt.Dimension(380, 0));
        brandPanel.setLayout(new java.awt.GridBagLayout());

        brandContent.setBackground(new java.awt.Color(26, 32, 53));
        brandContent.setLayout(new javax.swing.BoxLayout(brandContent, javax.swing.BoxLayout.Y_AXIS));

        icoLogin.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/book.png"))); // NOI18N
        icoLogin.setAlignmentX(0.5F);
        icoLogin.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 20, 0));
        brandContent.add(icoLogin);

        lblBrandTitle.setFont(new java.awt.Font("Segoe UI", 1, 32)); // NOI18N
        lblBrandTitle.setForeground(new java.awt.Color(212, 168, 67));
        lblBrandTitle.setText("BiblioGest");
        lblBrandTitle.setAlignmentX(0.5F);
        brandContent.add(lblBrandTitle);

        lblBrandSub.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lblBrandSub.setForeground(new java.awt.Color(154, 160, 176));
        lblBrandSub.setText("Gestion de bibliothèque");
        lblBrandSub.setAlignmentX(0.5F);
        lblBrandSub.setBorder(javax.swing.BorderFactory.createEmptyBorder(8, 0, 32, 0));
        brandContent.add(lblBrandSub);

        lblSlogan.setFont(new java.awt.Font("Segoe UI", 2, 13)); // NOI18N
        lblSlogan.setForeground(new java.awt.Color(100, 110, 140));
        lblSlogan.setText("Gérez vos livres en toute simplicité");
        lblSlogan.setAlignmentX(0.5F);
        brandContent.add(lblSlogan);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        brandPanel.add(brandContent, gridBagConstraints);

        getContentPane().add(brandPanel, java.awt.BorderLayout.WEST);

        formPanel.setBackground(new java.awt.Color(255, 255, 255));
        formPanel.setLayout(new java.awt.GridBagLayout());

        formContent.setBackground(new java.awt.Color(255, 255, 255));
        formContent.setPreferredSize(new java.awt.Dimension(320, 400));
        formContent.setLayout(new javax.swing.BoxLayout(formContent, javax.swing.BoxLayout.Y_AXIS));

        lblFormTitle.setFont(new java.awt.Font("Segoe UI", 1, 26)); // NOI18N
        lblFormTitle.setForeground(new java.awt.Color(26, 32, 53));
        lblFormTitle.setText("Connexion");
        lblFormTitle.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 8, 0));
        formContent.add(lblFormTitle);

        lblFormSub.setForeground(new java.awt.Color(154, 160, 176));
        lblFormSub.setText("Entrez vos identifiants");
        lblFormSub.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 32, 0));
        formContent.add(lblFormSub);

        lblUser.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblUser.setForeground(new java.awt.Color(26, 32, 53));
        lblUser.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblUser.setText("Nom d'utlilisateur");
        lblUser.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 6, 0));
        formContent.add(lblUser);

        txtUsername.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        txtUsername.setAlignmentX(0.0F);
        txtUsername.setMaximumSize(new java.awt.Dimension(320, 42));
        txtUsername.setMinimumSize(new java.awt.Dimension(420, 42));
        txtUsername.setPreferredSize(new java.awt.Dimension(320, 42));
        formContent.add(txtUsername);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setMaximumSize(new java.awt.Dimension(320, 16));
        jPanel1.setPreferredSize(new java.awt.Dimension(0, 16));
        formContent.add(jPanel1);

        lblPassword.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblPassword.setForeground(new java.awt.Color(26, 32, 53));
        lblPassword.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblPassword.setText("Mot de passe");
        lblPassword.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 6, 0));
        formContent.add(lblPassword);

        txtPassword.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        txtPassword.setAlignmentX(0.0F);
        txtPassword.setMaximumSize(new java.awt.Dimension(320, 42));
        txtPassword.setPreferredSize(new java.awt.Dimension(320, 42));
        formContent.add(txtPassword);

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setMaximumSize(new java.awt.Dimension(320, 24));
        jPanel2.setPreferredSize(new java.awt.Dimension(10, 24));
        formContent.add(jPanel2);

        btnConnexion.setBackground(new java.awt.Color(212, 168, 67));
        btnConnexion.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnConnexion.setForeground(new java.awt.Color(26, 32, 53));
        btnConnexion.setText("SE CONNECTER");
        btnConnexion.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnConnexion.setFocusPainted(false);
        btnConnexion.setMaximumSize(new java.awt.Dimension(320, 46));
        btnConnexion.setPreferredSize(new java.awt.Dimension(320, 46));
        formContent.add(btnConnexion);

        lblErreur.setForeground(new java.awt.Color(224, 82, 82));
        lblErreur.setAlignmentX(0.5F);
        lblErreur.setBorder(javax.swing.BorderFactory.createEmptyBorder(12, 0, 0, 0));
        formContent.add(lblErreur);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        formPanel.add(formContent, gridBagConstraints);

        getContentPane().add(formPanel, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
      FlatLightLaf.setup();

        SwingUtilities.invokeLater(() -> {
          Utilisateur user = null;
            new MainFrame(user).setVisible(true);
        });
}

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel brandContent;
    private javax.swing.JPanel brandPanel;
    private javax.swing.JButton btnConnexion;
    private javax.swing.JPanel formContent;
    private javax.swing.JPanel formPanel;
    private javax.swing.JLabel icoLogin;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JLabel lblBrandSub;
    private javax.swing.JLabel lblBrandTitle;
    private javax.swing.JLabel lblErreur;
    private javax.swing.JLabel lblFormSub;
    private javax.swing.JLabel lblFormTitle;
    private javax.swing.JLabel lblPassword;
    private javax.swing.JLabel lblSlogan;
    private javax.swing.JLabel lblUser;
    private javax.swing.JPasswordField txtPassword;
    private javax.swing.JTextField txtUsername;
    // End of variables declaration//GEN-END:variables
}

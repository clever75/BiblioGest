/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package views;

import com.formdev.flatlaf.FlatLightLaf;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import models.Utilisateur;
import views.LivrePanel;
import views.Dashboard;

/**
 *
 * @author Admin
 */
public class MainFrame extends javax.swing.JFrame {

    public MainFrame(Utilisateur user) {
        initComponents();
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        this.setLocation(100, 50);
        // Afficher le nom dans la sidebar
        lblUserName.setText(user.getNom() + " " + user.getPrenom());
        lblUserRole.setText(user.getNomUtilisateur());

        initNavigation();
        chargerDashboard();
    }

    private void initNavigation() {
        navDashboard.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                chargerDashboard();
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                navDashboard.setBackground(new java.awt.Color(34, 42, 64));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (!navDashboard.getBackground().equals(new Color(37, 45, 69))) {
                    navDashboard.setBackground(new java.awt.Color(26, 32, 53));
                }
            }
        });

        navLivres.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                ouvrirPanel(new LivrePanel(), "Livres", navLivres, lblLivres);
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                navLivres.setBackground(new Color(34, 42, 64));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                navLivres.setBackground(new java.awt.Color(26, 32, 53));
            }
        });

        navLecteurs.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                ouvrirPanel(new LecteurPanel(), "Lecteurs", navLecteurs, lblLecteurs);
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                navLecteurs.setBackground(new Color(34, 42, 64));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                navLecteurs.setBackground(new Color(26, 32, 53));
            }
        });

        navEmprunts.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                ouvrirPanel(new EmpruntPanel(), "Emprunts", navEmprunts, lblEmprunts);
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                navEmprunts.setBackground(new Color(34, 42, 64));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                navEmprunts.setBackground(new Color(26, 32, 53));
            }
        });

        navQuitter.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                System.exit(0);
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                navQuitter.setBackground(new Color(45, 26, 26));
                lblQuitter.setForeground(new Color(224, 82, 82));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                navQuitter.setBackground(new Color(26, 32, 53));
                lblQuitter.setForeground(new Color(128, 128, 128));
            }
        });
        navStatistiques.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                ouvrirPanel(new StatistiquesPanel(), "Statistiques", navStatistiques, lblNavStatistiques);
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                navEmprunts.setBackground(new Color(34, 42, 64));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                navEmprunts.setBackground(new Color(26, 32, 53));
            }
        });

        //btnActualiser.addActionListener(e -> chargerDashboard());
    }

    private void setNavActif(JPanel panel, JLabel label) {
        Color bgNormal = new Color(26, 32, 53);
        Color fgNormal = new Color(154, 160, 176);

        navDashboard.setBackground(bgNormal);
        navLivres.setBackground(bgNormal);
        navLecteurs.setBackground(bgNormal);
        navEmprunts.setBackground(bgNormal);
        navStatistiques.setBackground(bgNormal);

        navDashboard.setBorder(BorderFactory.createEmptyBorder(0, 3, 0, 0));
        navLivres.setBorder(BorderFactory.createEmptyBorder(1, 3, 1, 1));
        navLecteurs.setBorder(BorderFactory.createEmptyBorder(1, 3, 1, 1));
        navEmprunts.setBorder(BorderFactory.createEmptyBorder(1, 3, 1, 1));
        navStatistiques.setBorder(BorderFactory.createEmptyBorder(1, 3, 1, 1));

        navDashboard.setForeground(fgNormal);
        lblLivres.setForeground(fgNormal);
        lblLecteurs.setForeground(fgNormal);
        lblEmprunts.setForeground(fgNormal);
        lblNavStatistiques.setBackground(bgNormal);

        panel.setBackground(new Color(37, 45, 69));
        panel.setBorder(BorderFactory.createMatteBorder(0, 3, 0, 0,
                new Color(212, 168, 67)));
        setForeground(java.awt.Color.WHITE);
    }

    private void ouvrirPanel(JPanel panel, String titre,
            JPanel navItem, JLabel navLabel) {
        setNavActif(navItem, navLabel);
        contentPanel.removeAll();
        contentPanel.add(panel, BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    private Dashboard dashboard = new Dashboard();

    private void chargerDashboard() {
        setNavActif(navDashboard, lblDashboard);
        contentPanel.removeAll();
        contentPanel.add(dashboard, java.awt.BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();
        dashboard.chargerDonnees(); // ← méthode dans Dashboard.java
    }

    public void allerVersEmprunts() {
        ouvrirPanel(new EmpruntPanel(), "Emprunts", navEmprunts, lblEmprunts);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        sidebarPanel = new javax.swing.JPanel();
        logoPanel = new javax.swing.JPanel();
        lblAppName = new javax.swing.JLabel();
        lblAppSub = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        navPanel = new javax.swing.JPanel();
        navDashboard = new javax.swing.JPanel();
        icoDashboard = new javax.swing.JLabel();
        lblDashboard = new javax.swing.JLabel();
        navLivres = new javax.swing.JPanel();
        icoLivres = new javax.swing.JLabel();
        lblLivres = new javax.swing.JLabel();
        navLecteurs = new javax.swing.JPanel();
        icoLecteurs = new javax.swing.JLabel();
        lblLecteurs = new javax.swing.JLabel();
        navEmprunts = new javax.swing.JPanel();
        icoEmprunts = new javax.swing.JLabel();
        lblEmprunts = new javax.swing.JLabel();
        navStatistiques = new javax.swing.JPanel();
        icoEmprunts1 = new javax.swing.JLabel();
        lblNavStatistiques = new javax.swing.JLabel();
        userPanel = new javax.swing.JPanel();
        userInfoRow = new javax.swing.JPanel();
        lblAvatar = new javax.swing.JLabel();
        userInfoPanel = new javax.swing.JPanel();
        lblUserName = new javax.swing.JLabel();
        lblUserRole = new javax.swing.JLabel();
        jSeparator2 = new javax.swing.JSeparator();
        navQuitter = new javax.swing.JPanel();
        icoQuitter = new javax.swing.JLabel();
        lblQuitter = new javax.swing.JLabel();
        contentPanel = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Gestion Bibliothèque");
        setBackground(new java.awt.Color(245, 245, 245));
        setMinimumSize(new java.awt.Dimension(800, 600));
        setSize(new java.awt.Dimension(800, 600));

        sidebarPanel.setBackground(new java.awt.Color(26, 32, 53));
        sidebarPanel.setMinimumSize(new java.awt.Dimension(220, 0));
        sidebarPanel.setPreferredSize(new java.awt.Dimension(240, 0));
        sidebarPanel.setLayout(new java.awt.BorderLayout());

        logoPanel.setBackground(new java.awt.Color(26, 32, 53));
        logoPanel.setForeground(new java.awt.Color(0, 0, 53));
        logoPanel.setPreferredSize(new java.awt.Dimension(240, 80));
        logoPanel.setLayout(new javax.swing.BoxLayout(logoPanel, javax.swing.BoxLayout.Y_AXIS));

        lblAppName.setBackground(new java.awt.Color(26, 32, 53));
        lblAppName.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        lblAppName.setForeground(new java.awt.Color(212, 168, 67));
        lblAppName.setText("BiblioGest");
        lblAppName.setBorder(javax.swing.BorderFactory.createEmptyBorder(28, 20, 4, 20));
        logoPanel.add(lblAppName);

        lblAppSub.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        lblAppSub.setForeground(new java.awt.Color(154, 160, 176));
        lblAppSub.setText("Gestion de bibliothèque");
        lblAppSub.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 20, 20, 20));
        logoPanel.add(lblAppSub);

        jSeparator1.setForeground(new java.awt.Color(45, 53, 80));
        jSeparator1.setMaximumSize(new java.awt.Dimension(240, 1));
        jSeparator1.setPreferredSize(new java.awt.Dimension(240, 1));
        logoPanel.add(jSeparator1);

        sidebarPanel.add(logoPanel, java.awt.BorderLayout.NORTH);

        navPanel.setBackground(new java.awt.Color(26, 32, 53));
        navPanel.setPreferredSize(new java.awt.Dimension(240, 0));
        navPanel.setLayout(new javax.swing.BoxLayout(navPanel, javax.swing.BoxLayout.Y_AXIS));

        navDashboard.setBackground(new java.awt.Color(37, 45, 69));
        navDashboard.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 3, 0, 0, new java.awt.Color(212, 168, 67)));
        navDashboard.setMaximumSize(new java.awt.Dimension(240, 48));
        navDashboard.setMinimumSize(new java.awt.Dimension(220, 48));
        navDashboard.setPreferredSize(new java.awt.Dimension(240, 48));
        navDashboard.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 16, 0));

        icoDashboard.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/dashboard.png"))); // NOI18N
        navDashboard.add(icoDashboard);

        lblDashboard.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        lblDashboard.setForeground(new java.awt.Color(154, 160, 176));
        lblDashboard.setText("Tableau de bord");
        navDashboard.add(lblDashboard);

        navPanel.add(navDashboard);

        navLivres.setBackground(new java.awt.Color(26, 32, 53));
        navLivres.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 3, 1, 1));
        navLivres.setMaximumSize(new java.awt.Dimension(240, 48));
        navLivres.setMinimumSize(new java.awt.Dimension(220, 48));
        navLivres.setPreferredSize(new java.awt.Dimension(240, 48));
        navLivres.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 16, 5));

        icoLivres.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/books.png"))); // NOI18N
        navLivres.add(icoLivres);

        lblLivres.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        lblLivres.setForeground(new java.awt.Color(154, 160, 176));
        lblLivres.setText("Livres");
        navLivres.add(lblLivres);

        navPanel.add(navLivres);

        navLecteurs.setBackground(new java.awt.Color(26, 32, 53));
        navLecteurs.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 3, 1, 1));
        navLecteurs.setMaximumSize(new java.awt.Dimension(240, 48));
        navLecteurs.setMinimumSize(new java.awt.Dimension(220, 48));
        navLecteurs.setPreferredSize(new java.awt.Dimension(240, 48));
        navLecteurs.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 16, 5));

        icoLecteurs.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/users.png"))); // NOI18N
        navLecteurs.add(icoLecteurs);

        lblLecteurs.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        lblLecteurs.setForeground(new java.awt.Color(154, 160, 176));
        lblLecteurs.setText("Lecteurs");
        navLecteurs.add(lblLecteurs);

        navPanel.add(navLecteurs);

        navEmprunts.setBackground(new java.awt.Color(26, 32, 53));
        navEmprunts.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 3, 1, 1));
        navEmprunts.setMaximumSize(new java.awt.Dimension(240, 48));
        navEmprunts.setMinimumSize(new java.awt.Dimension(220, 48));
        navEmprunts.setPreferredSize(new java.awt.Dimension(240, 48));
        navEmprunts.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 16, 5));

        icoEmprunts.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/arrows-repeat.png"))); // NOI18N
        navEmprunts.add(icoEmprunts);

        lblEmprunts.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        lblEmprunts.setForeground(new java.awt.Color(154, 160, 176));
        lblEmprunts.setText("Emprunts");
        navEmprunts.add(lblEmprunts);

        navPanel.add(navEmprunts);

        navStatistiques.setBackground(new java.awt.Color(26, 32, 53));
        navStatistiques.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 3, 1, 1));
        navStatistiques.setMaximumSize(new java.awt.Dimension(240, 48));
        navStatistiques.setMinimumSize(new java.awt.Dimension(220, 48));
        navStatistiques.setPreferredSize(new java.awt.Dimension(240, 48));
        navStatistiques.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 16, 5));

        icoEmprunts1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/chart-histogram.png"))); // NOI18N
        navStatistiques.add(icoEmprunts1);

        lblNavStatistiques.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        lblNavStatistiques.setForeground(new java.awt.Color(154, 160, 176));
        lblNavStatistiques.setText("Statistiques");
        navStatistiques.add(lblNavStatistiques);

        navPanel.add(navStatistiques);

        sidebarPanel.add(navPanel, java.awt.BorderLayout.CENTER);

        userPanel.setBackground(new java.awt.Color(26, 32, 53));
        userPanel.setLayout(new javax.swing.BoxLayout(userPanel, javax.swing.BoxLayout.Y_AXIS));

        userInfoRow.setBackground(new java.awt.Color(26, 32, 53));
        userInfoRow.setMaximumSize(new java.awt.Dimension(240, 60));
        userInfoRow.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 12, 8));

        lblAvatar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/user.png"))); // NOI18N
        lblAvatar.setBorder(javax.swing.BorderFactory.createEmptyBorder(12, 16, 12, 8));
        userInfoRow.add(lblAvatar);

        userInfoPanel.setBackground(new java.awt.Color(26, 32, 53));
        userInfoPanel.setLayout(new javax.swing.BoxLayout(userInfoPanel, javax.swing.BoxLayout.Y_AXIS));

        lblUserName.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblUserName.setForeground(new java.awt.Color(255, 255, 255));
        lblUserName.setText("Administrateur");
        userInfoPanel.add(lblUserName);

        lblUserRole.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        lblUserRole.setForeground(new java.awt.Color(154, 160, 176));
        lblUserRole.setText("admin");
        userInfoPanel.add(lblUserRole);
        userInfoPanel.add(jSeparator2);

        userInfoRow.add(userInfoPanel);

        userPanel.add(userInfoRow);

        navQuitter.setBackground(new java.awt.Color(26, 32, 53));
        navQuitter.setForeground(new java.awt.Color(26, 32, 53));
        navQuitter.setMaximumSize(new java.awt.Dimension(240, 44));
        navQuitter.setPreferredSize(new java.awt.Dimension(240, 44));
        navQuitter.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 16, 0));

        icoQuitter.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/logout.png"))); // NOI18N
        navQuitter.add(icoQuitter);

        lblQuitter.setForeground(new java.awt.Color(128, 128, 128));
        lblQuitter.setText("Quitter");
        navQuitter.add(lblQuitter);

        userPanel.add(navQuitter);

        sidebarPanel.add(userPanel, java.awt.BorderLayout.SOUTH);

        getContentPane().add(sidebarPanel, java.awt.BorderLayout.WEST);

        contentPanel.setBackground(new java.awt.Color(245, 240, 232));
        contentPanel.setLayout(new java.awt.BorderLayout());
        getContentPane().add(contentPanel, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        FlatLightLaf.setup();
        java.awt.EventQueue.invokeLater(() -> new LoginFrame().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel contentPanel;
    private javax.swing.JLabel icoDashboard;
    private javax.swing.JLabel icoEmprunts;
    private javax.swing.JLabel icoEmprunts1;
    private javax.swing.JLabel icoLecteurs;
    private javax.swing.JLabel icoLivres;
    private javax.swing.JLabel icoQuitter;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JLabel lblAppName;
    private javax.swing.JLabel lblAppSub;
    private javax.swing.JLabel lblAvatar;
    private javax.swing.JLabel lblDashboard;
    private javax.swing.JLabel lblEmprunts;
    private javax.swing.JLabel lblLecteurs;
    private javax.swing.JLabel lblLivres;
    private javax.swing.JLabel lblNavStatistiques;
    private javax.swing.JLabel lblQuitter;
    private javax.swing.JLabel lblUserName;
    private javax.swing.JLabel lblUserRole;
    private javax.swing.JPanel logoPanel;
    private javax.swing.JPanel navDashboard;
    private javax.swing.JPanel navEmprunts;
    private javax.swing.JPanel navLecteurs;
    private javax.swing.JPanel navLivres;
    private javax.swing.JPanel navPanel;
    private javax.swing.JPanel navQuitter;
    private javax.swing.JPanel navStatistiques;
    private javax.swing.JPanel sidebarPanel;
    private javax.swing.JPanel userInfoPanel;
    private javax.swing.JPanel userInfoRow;
    private javax.swing.JPanel userPanel;
    // End of variables declaration//GEN-END:variables
}

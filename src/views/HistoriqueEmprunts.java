package views;

import dao.EmpruntDAO;
import java.awt.*;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class HistoriqueEmprunts extends JDialog {

    public HistoriqueEmprunts(Frame parent, boolean modal,
            int idLecteur, String nomLecteur) {
        super(parent, modal);
        setTitle("Historique — " + nomLecteur);
        setSize(800, 500);
        setLocationRelativeTo(parent);
        setResizable(false);
        getContentPane().setBackground(new Color(245, 240, 232));
        setLayout(new BorderLayout());

        // ── HEADER ────────────────────────────────────────────
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(245, 240, 232));
        header.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0,
                        new Color(232, 226, 216)),
                BorderFactory.createEmptyBorder(0, 28, 0, 28)));
        header.setPreferredSize(new Dimension(0, 70));

        // Avatar + Nom
        JPanel left = new JPanel(new FlowLayout(FlowLayout.LEFT, 14, 15));
        left.setBackground(new Color(245, 240, 232));

        // Initiales pour l'avatar
        String[] parts = nomLecteur.split(" ", 2);
        String init = "";
        if (parts.length >= 1 && parts[0].length() > 0)
            init += parts[0].charAt(0);
        if (parts.length >= 2 && parts[1].length() > 0)
            init += parts[1].charAt(0);
        final String initiales = init.toUpperCase();

        JLabel avatar = new JLabel(initiales) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(212, 168, 67));
                g2.fillOval(0, 0, getWidth(), getHeight());
                super.paintComponent(g);
                g2.dispose();
            }
        };
        avatar.setPreferredSize(new Dimension(40, 40));
        avatar.setHorizontalAlignment(SwingConstants.CENTER);
        avatar.setFont(new Font("Segoe UI", Font.BOLD, 15));
        avatar.setForeground(new Color(26, 32, 53));
        avatar.setOpaque(false);

        JPanel nomPanel = new JPanel();
        nomPanel.setOpaque(false);
        nomPanel.setLayout(new BoxLayout(nomPanel, BoxLayout.Y_AXIS));
        JLabel lblNom = new JLabel(nomLecteur);
        lblNom.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblNom.setForeground(new Color(26, 32, 53));
        JLabel lblSous = new JLabel("Historique des emprunts");
        lblSous.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblSous.setForeground(new Color(154, 160, 176));
        nomPanel.add(lblNom);
        nomPanel.add(lblSous);

        left.add(avatar);
        left.add(nomPanel);
        header.add(left, BorderLayout.WEST);

        // Bouton fermer
        JButton btnFermer = new JButton("✕  Fermer") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                super.paintComponent(g);
                g2.dispose();
            }
        };
        btnFermer.setFont(new Font("Segoe UI Emoji", Font.BOLD, 12));
        btnFermer.setForeground(new Color(26, 32, 53));
        btnFermer.setBackground(new Color(232, 226, 216));
        btnFermer.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));
        btnFermer.setFocusPainted(false);
        btnFermer.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnFermer.setContentAreaFilled(false);
        btnFermer.addActionListener(e -> dispose());

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 28, 18));
        right.setBackground(new Color(245, 240, 232));
        right.add(btnFermer);
        header.add(right, BorderLayout.EAST);

        add(header, BorderLayout.NORTH);

        // ── BODY ──────────────────────────────────────────────
        JPanel body = new JPanel(new BorderLayout());
        body.setBackground(new Color(245, 240, 232));
        body.setBorder(BorderFactory.createEmptyBorder(20, 28, 20, 28));

        // Tableau
        String[] colonnes = {"Livre", "Date emprunt",
                "Date retour prévue", "Date retour réelle", "Statut"};
        DefaultTableModel model = new DefaultTableModel(colonnes, 0) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };

        JTable table = new JTable(model);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.setRowHeight(48);
        table.setShowVerticalLines(false);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.setSelectionBackground(new Color(250, 246, 238));
        table.setSelectionForeground(new Color(26, 32, 53));
        table.setGridColor(new Color(232, 226, 216));

        // En-tête
        table.getTableHeader().setBackground(new Color(245, 240, 232));
        table.getTableHeader().setFont(
                new Font("Segoe UI", Font.BOLD, 11));
        table.getTableHeader().setForeground(
                new Color(154, 160, 176));
        table.getTableHeader().setPreferredSize(
                new Dimension(0, 42));

        // Largeurs colonnes
        table.getColumnModel().getColumn(0).setPreferredWidth(220);
        table.getColumnModel().getColumn(1).setPreferredWidth(120);
        table.getColumnModel().getColumn(2).setPreferredWidth(140);
        table.getColumnModel().getColumn(3).setPreferredWidth(140);
        table.getColumnModel().getColumn(4).setPreferredWidth(110);

        // Renderer général
        table.setDefaultRenderer(Object.class,
                new javax.swing.table.DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(
                    JTable t, Object v, boolean sel,
                    boolean foc, int r, int c) {
                super.getTableCellRendererComponent(
                        t, v, sel, foc, r, c);
                setHorizontalAlignment(c == 0 ? LEFT : CENTER);
                setBorder(BorderFactory.createEmptyBorder(
                        0, c == 0 ? 14 : 8, 0, 8));
                setFont(new Font("Segoe UI", Font.PLAIN, 13));
                if (sel) {
                    setBackground(new Color(250, 246, 238));
                    setForeground(new Color(26, 32, 53));
                } else {
                    setBackground(r % 2 == 0
                            ? Color.WHITE
                            : new Color(250, 249, 247));
                    setForeground(new Color(90, 96, 112));
                }
                return this;
            }
        });

        // Renderer colonne Statut (badges colorés)
        table.getColumnModel().getColumn(4).setCellRenderer(
                new javax.swing.table.DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(
                    JTable t, Object v, boolean sel,
                    boolean foc, int r, int c) {

                JPanel cell = new JPanel(new GridBagLayout());
                cell.setBackground(r % 2 == 0
                        ? Color.WHITE
                        : new Color(250, 249, 247));
                if (sel)
                    cell.setBackground(new Color(250, 246, 238));

                String statut = v != null ? v.toString() : "";
                Color bg, fg;

                switch (statut) {
                    case "Rendu ✓":
                        bg = new Color(220, 252, 231);
                        fg = new Color(22, 120, 55);
                        break;
                    case "En retard ⚠️":
                        bg = new Color(255, 237, 213);
                        fg = new Color(180, 80, 10);
                        break;
                    default: // En cours
                        bg = new Color(219, 234, 254);
                        fg = new Color(30, 80, 180);
                }

                JLabel badge = new JLabel(statut) {
                    @Override
                    protected void paintComponent(Graphics g) {
                        Graphics2D g2 = (Graphics2D) g.create();
                        g2.setRenderingHint(
                                RenderingHints.KEY_ANTIALIASING,
                                RenderingHints.VALUE_ANTIALIAS_ON);
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
                badge.setFont(new Font("Segoe UI", Font.BOLD, 11));
                badge.setHorizontalAlignment(SwingConstants.CENTER);
                badge.setBorder(BorderFactory.createEmptyBorder(
                        3, 12, 3, 12));
                cell.add(badge);
                return cell;
            }
        });

        // Charger les données
        EmpruntDAO empruntDAO = new EmpruntDAO();
        ArrayList<models.Emprunt> emprunts =
                empruntDAO.getHistoriqueParLecteur(idLecteur);

        for (models.Emprunt emp : emprunts) {
            String statut;
            if (emp.getDateRetourReelle() != null
                    && !emp.getDateRetourReelle().isEmpty()) {
                statut = "Rendu ✓";
            } else {
                try {
                    java.time.LocalDate prevue = java.time.LocalDate
                            .parse(emp.getDateRetourPrevue());
                    statut = java.time.LocalDate.now().isAfter(prevue)
                            ? "En retard ⚠️" : "En cours";
                } catch (Exception ex) {
                    statut = "En cours";
                }
            }
            model.addRow(new Object[]{
                emp.getTitre(),
                emp.getDateEmprunt(),
                emp.getDateRetourPrevue(),
                (emp.getDateRetourReelle() != null
                        && !emp.getDateRetourReelle().isEmpty())
                        ? emp.getDateRetourReelle() : "—",
                statut
            });
        }

        // Message si aucun emprunt
        if (model.getRowCount() == 0) {
            model.addRow(new Object[]{
                "Aucun emprunt enregistré pour ce lecteur",
                "—", "—", "—", "—"
            });
        }

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createLineBorder(
                new Color(232, 226, 216)));
        scroll.getViewport().setBackground(Color.WHITE);

        body.add(scroll, BorderLayout.CENTER);
        add(body, BorderLayout.CENTER);
    }
}
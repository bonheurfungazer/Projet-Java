package com.campusconnect.ui;

import com.campusconnect.models.*;
import com.campusconnect.services.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.util.List;
import java.time.LocalDate;
import java.awt.*;

public class DashboardGUI extends JFrame {

    // Services
    private GestionActeurs gestionActeurs;
    private GestionFormation gestionFormation;
    private GestionSuiviAcademique gestionSuivi;
    private GestionPlanning gestionPlanning;

    // UI Components
    private CardLayout cardLayout;
    private JPanel cardsContainer;
    private JPanel dashboardPanel;
    private JPanel studentsPanel;
    private DefaultTableModel studentsTableModel;

    // Dashboard Components
    private JLabel lblTotalStudents;
    private JLabel lblTotalTeachers;
    private JLabel lblActiveCourses;

    // VIP Theme Colors & Fonts
    private final Color bgDark = new Color(18, 18, 18);
    private final Color bgPanel = new Color(30, 30, 30);
    private final Color sidebarColor = new Color(20, 20, 20);
    private final Color textPrimary = new Color(240, 240, 240);
    private final Color textSecondary = new Color(170, 170, 170);
    private final Color accentGold = new Color(212, 175, 55);
    private final Color accentGreen = new Color(46, 204, 113);

    private final Font headerFont = new Font("Segoe UI", Font.BOLD, 26);
    private final Font cardTitleFont = new Font("Segoe UI", Font.BOLD, 14);
    private final Font cardValueFont = new Font("Segoe UI", Font.BOLD, 32);
    private final Font regularFont = new Font("Segoe UI", Font.PLAIN, 15);

    public DashboardGUI(GestionActeurs act, GestionFormation form, GestionSuiviAcademique suivi, GestionPlanning plan) {
        this.gestionActeurs = act;
        this.gestionFormation = form;
        this.gestionSuivi = suivi;
        this.gestionPlanning = plan;

        setTitle("CampusConnect VIP - Admin Dashboard");
        setSize(1100, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(bgDark);

        initUI();
    }

    private void initUI() {
        setLayout(new BorderLayout());

        // --- Sidebar ---
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setPreferredSize(new Dimension(220, 0));
        sidebar.setBackground(sidebarColor);
        sidebar.setBorder(new EmptyBorder(30, 20, 20, 20));

        JLabel logo = new JLabel("CampusConnect");
        logo.setFont(new Font("Segoe UI", Font.BOLD, 20));
        logo.setForeground(accentGold);
        logo.setAlignmentX(Component.LEFT_ALIGNMENT);
        sidebar.add(logo);

        JLabel subtitle = new JLabel("Premium Edition");
        subtitle.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        subtitle.setForeground(accentGreen);
        subtitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        sidebar.add(subtitle);

        sidebar.add(Box.createRigidArea(new Dimension(0, 50)));

        JButton btnDash = createSidebarButton("Dashboard");
        btnDash.addActionListener(e -> cardLayout.show(cardsContainer, "Dashboard"));
        sidebar.add(btnDash);
        sidebar.add(Box.createRigidArea(new Dimension(0, 15)));

        JButton btnStudents = createSidebarButton("Étudiants");
        btnStudents.addActionListener(e -> {
            cardLayout.show(cardsContainer, "Students");
            refreshStudentsTable();
        });
        sidebar.add(btnStudents);

        sidebar.add(Box.createVerticalGlue());
        JLabel author = new JLabel("<html><br><b>Réalisé par:</b><br>Guepi takouo peguy maeva</html>");
        author.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        author.setForeground(textSecondary);
        author.setAlignmentX(Component.LEFT_ALIGNMENT);
        sidebar.add(author);

        add(sidebar, BorderLayout.WEST);

        // --- Cards Container ---
        cardLayout = new CardLayout();
        cardsContainer = new JPanel(cardLayout);
        cardsContainer.setBackground(bgDark);

        dashboardPanel = new JPanel();
        dashboardPanel.setLayout(new BoxLayout(dashboardPanel, BoxLayout.Y_AXIS));
        dashboardPanel.setBackground(bgDark);
        dashboardPanel.setBorder(new EmptyBorder(30, 40, 30, 40));

        // --- Dashboard Header ---
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        headerPanel.setBackground(bgDark);
        JLabel headerLabel = new JLabel("Bienvenue, Administrateur");
        headerLabel.setFont(headerFont);
        headerLabel.setForeground(textPrimary);
        headerPanel.add(headerLabel);
        dashboardPanel.add(headerPanel);
        dashboardPanel.add(Box.createRigidArea(new Dimension(0, 30)));

        buildStatisticsCards();

        refreshDashboardStats();
        cardsContainer.add(dashboardPanel, "Dashboard");

        // --- Students Panel ---
        studentsPanel = new JPanel();
        studentsPanel.setLayout(new BorderLayout(0, 20));
        studentsPanel.setBackground(bgDark);
        studentsPanel.setBorder(new EmptyBorder(30, 40, 30, 40));

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(bgDark);
        JLabel title = new JLabel("Gestion des Étudiants");
        title.setFont(headerFont);
        title.setForeground(textPrimary);
        topPanel.add(title, BorderLayout.WEST);
        JButton btnAddStudent = new JButton("+ Ajouter");
        btnAddStudent.setBackground(accentGreen);
        btnAddStudent.setForeground(bgDark);
        btnAddStudent.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnAddStudent.addActionListener(e -> showAddStudentDialog());
        topPanel.add(btnAddStudent, BorderLayout.EAST);
        studentsPanel.add(topPanel, BorderLayout.NORTH);

        String[] columns = {"Matricule", "Nom", "Prénom", "Filière", "Année"};
        studentsTableModel = new DefaultTableModel(columns, 0);
        JTable studentsTable = new JTable(studentsTableModel);
        studentsTable.setBackground(bgPanel);
        studentsTable.setForeground(textPrimary);
        studentsTable.setGridColor(new Color(60, 60, 60));
        studentsTable.setRowHeight(30);
        studentsTable.getTableHeader().setBackground(new Color(40, 40, 40));
        studentsTable.getTableHeader().setForeground(accentGold);
        studentsTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        JScrollPane scrollPane = new JScrollPane(studentsTable);
        scrollPane.getViewport().setBackground(bgDark);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(50, 50, 50)));
        studentsPanel.add(scrollPane, BorderLayout.CENTER);

        cardsContainer.add(studentsPanel, "Students");
        refreshStudentsTable();

        add(cardsContainer, BorderLayout.CENTER);
    }

    private JButton createSidebarButton(String text) {
        JButton btn = new JButton(text);
        btn.setAlignmentX(Component.LEFT_ALIGNMENT);
        btn.setBackground(sidebarColor);
        btn.setForeground(textPrimary);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setFont(regularFont);
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private void buildStatisticsCards() {
        JPanel cardsPanel = new JPanel(new GridLayout(1, 3, 25, 0));
        cardsPanel.setBackground(bgDark);
        cardsPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 140));
        cardsPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        lblTotalStudents = new JLabel("0");
        lblTotalTeachers = new JLabel("0");
        lblActiveCourses = new JLabel("0");

        cardsPanel.add(createCard("Total Étudiants", lblTotalStudents, accentGold));
        cardsPanel.add(createCard("Total Enseignants", lblTotalTeachers, accentGreen));
        cardsPanel.add(createCard("Cours Actifs", lblActiveCourses, new Color(155, 89, 182)));

        dashboardPanel.add(cardsPanel);
        dashboardPanel.add(Box.createRigidArea(new Dimension(0, 40)));
    }

    private void refreshStudentsTable() {
        studentsTableModel.setRowCount(0);
        List<Etudiant> etudiants = gestionActeurs.getEtudiants();
        for (Etudiant e : etudiants) {
            studentsTableModel.addRow(new Object[]{e.getMatricule(), e.getNom(), e.getPrenom(), e.getFiliere(), e.getAnneeEtude()});
        }
    }

    private void refreshDashboardStats() {
        lblTotalStudents.setText(String.valueOf(gestionActeurs.getEtudiants().size()));
        lblTotalTeachers.setText(String.valueOf(gestionActeurs.getEnseignants().size()));
        lblActiveCourses.setText(String.valueOf(gestionFormation.getCours().size()));
    }

    private void showAddStudentDialog() {
        JDialog dialog = new JDialog(this, "Ajouter un Étudiant", true);
        dialog.setSize(400, 450);
        dialog.setLocationRelativeTo(this);
        dialog.getContentPane().setBackground(bgDark);

        JPanel panel = new JPanel(new GridLayout(8, 2, 10, 10));
        panel.setBackground(bgDark);
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel lblId = new JLabel("ID unique :"); lblId.setForeground(textPrimary);
        JTextField txtId = new JTextField();
        JLabel lblMatricule = new JLabel("Matricule :"); lblMatricule.setForeground(textPrimary);
        JTextField txtMatricule = new JTextField();
        JLabel lblNom = new JLabel("Nom :"); lblNom.setForeground(textPrimary);
        JTextField txtNom = new JTextField();
        JLabel lblPrenom = new JLabel("Prénom :"); lblPrenom.setForeground(textPrimary);
        JTextField txtPrenom = new JTextField();
        JLabel lblEmail = new JLabel("Email :"); lblEmail.setForeground(textPrimary);
        JTextField txtEmail = new JTextField();
        JLabel lblFiliere = new JLabel("Filière :"); lblFiliere.setForeground(textPrimary);
        JTextField txtFiliere = new JTextField();
        JLabel lblAnnee = new JLabel("Année (ex: L3) :"); lblAnnee.setForeground(textPrimary);
        JTextField txtAnnee = new JTextField();

        JButton btnSubmit = new JButton("Enregistrer");
        btnSubmit.setBackground(accentGold);
        btnSubmit.setForeground(bgDark);
        btnSubmit.setFont(new Font("Segoe UI", Font.BOLD, 14));

        btnSubmit.addActionListener(e -> {
            try {
                Etudiant nouveau = new Etudiant(txtId.getText(), txtNom.getText(), txtPrenom.getText(),
                        txtEmail.getText(), LocalDate.now(), txtMatricule.getText(), txtAnnee.getText(), txtFiliere.getText());
                gestionActeurs.ajouterPersonne(nouveau);
                refreshStudentsTable();
                refreshDashboardStats();
                dialog.dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Veuillez remplir correctement les champs.", "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        });

        panel.add(lblId); panel.add(txtId);
        panel.add(lblMatricule); panel.add(txtMatricule);
        panel.add(lblNom); panel.add(txtNom);
        panel.add(lblPrenom); panel.add(txtPrenom);
        panel.add(lblEmail); panel.add(txtEmail);
        panel.add(lblFiliere); panel.add(txtFiliere);
        panel.add(lblAnnee); panel.add(txtAnnee);
        panel.add(new JLabel("")); panel.add(btnSubmit);

        dialog.add(panel);
        dialog.setVisible(true);
    }

    private JPanel createCard(String title, JLabel valueLabel, Color accentColor) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(bgPanel);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(50, 50, 50), 2),
            new EmptyBorder(25, 25, 25, 25)
        ));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(cardTitleFont);
        titleLabel.setForeground(textSecondary);

        valueLabel.setFont(cardValueFont);
        valueLabel.setForeground(accentColor);

        card.add(titleLabel, BorderLayout.NORTH);
        card.add(valueLabel, BorderLayout.CENTER);

        return card;
    }
}

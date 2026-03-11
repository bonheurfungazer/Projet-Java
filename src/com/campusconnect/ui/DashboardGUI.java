package com.campusconnect.ui;

import com.campusconnect.models.*;
import com.campusconnect.services.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class DashboardGUI extends JFrame {

    // Services
    private GestionActeurs gestionActeurs;
    private GestionFormation gestionFormation;
    private GestionSuiviAcademique gestionSuivi;
    private GestionPlanning gestionPlanning;

    // UI Components
    private JPanel mainContentPanel;
    private JLabel lblTotalStudents;
    private JLabel lblTotalTeachers;
    private JLabel lblActiveCourses;
    private DefaultListModel<String> activityModel;
    private DefaultListModel<String> scheduleModel;

    // Colors & Fonts
    private final Color primaryBlue = new Color(19, 127, 236);
    private final Color bgColor = new Color(245, 247, 250);
    private final Color sidebarColor = Color.WHITE;
    private final Font headerFont = new Font("SansSerif", Font.BOLD, 24);
    private final Font cardTitleFont = new Font("SansSerif", Font.BOLD, 14);
    private final Font cardValueFont = new Font("SansSerif", Font.BOLD, 28);
    private final Font regularFont = new Font("SansSerif", Font.PLAIN, 14);

    public DashboardGUI(GestionActeurs act, GestionFormation form, GestionSuiviAcademique suivi, GestionPlanning plan) {
        this.gestionActeurs = act;
        this.gestionFormation = form;
        this.gestionSuivi = suivi;
        this.gestionPlanning = plan;

        setTitle("CampusConnect - Admin Dashboard v1");
        setSize(1024, 768);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        initUI();
        loadData();
    }

    private void initUI() {
        setLayout(new BorderLayout());

        // 1. Sidebar
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setPreferredSize(new Dimension(200, 0));
        sidebar.setBackground(sidebarColor);
        sidebar.setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel logo = new JLabel("CampusConnect");
        logo.setFont(new Font("SansSerif", Font.BOLD, 18));
        logo.setForeground(primaryBlue);
        logo.setAlignmentX(Component.LEFT_ALIGNMENT);
        sidebar.add(logo);
        sidebar.add(Box.createRigidArea(new Dimension(0, 40)));

        String[] menuItems = {"Dashboard", "Students", "Teachers", "Courses", "Scheduling", "Grades"};
        for (String item : menuItems) {
            JButton btn = new JButton(item);
            btn.setAlignmentX(Component.LEFT_ALIGNMENT);
            btn.setBackground(sidebarColor);
            btn.setBorderPainted(false);
            btn.setFocusPainted(false);
            btn.setFont(regularFont);
            btn.setHorizontalAlignment(SwingConstants.LEFT);
            btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
            if(item.equals("Dashboard")) {
                btn.setForeground(primaryBlue);
                btn.setFont(new Font("SansSerif", Font.BOLD, 14));
            }
            sidebar.add(btn);
            sidebar.add(Box.createRigidArea(new Dimension(0, 10)));
        }

        sidebar.add(Box.createVerticalGlue());
        JLabel author = new JLabel("<html><br><b>Réalisé par:</b><br>Guepi takouo peguy maeva</html>");
        author.setFont(new Font("SansSerif", Font.PLAIN, 12));
        author.setForeground(Color.GRAY);
        author.setAlignmentX(Component.LEFT_ALIGNMENT);
        sidebar.add(author);
        add(sidebar, BorderLayout.WEST);

        // 2. Main Content Wrapper
        JPanel wrapperPanel = new JPanel(new BorderLayout());
        wrapperPanel.setBackground(bgColor);

        // Header
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        headerPanel.setBackground(bgColor);
        headerPanel.setBorder(new EmptyBorder(20, 30, 10, 30));
        JLabel headerLabel = new JLabel("Welcome back, Admin");
        headerLabel.setFont(headerFont);
        headerPanel.add(headerLabel);
        wrapperPanel.add(headerPanel, BorderLayout.NORTH);

        // Main Content Container
        mainContentPanel = new JPanel();
        mainContentPanel.setLayout(new BoxLayout(mainContentPanel, BoxLayout.Y_AXIS));
        mainContentPanel.setBackground(bgColor);
        mainContentPanel.setBorder(new EmptyBorder(10, 30, 30, 30));

        buildStatisticsCards();
        buildDetailsSections();

        wrapperPanel.add(mainContentPanel, BorderLayout.CENTER);
        add(wrapperPanel, BorderLayout.CENTER);
    }

    private void buildStatisticsCards() {
        JPanel cardsPanel = new JPanel(new GridLayout(1, 3, 20, 0));
        cardsPanel.setBackground(bgColor);
        cardsPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120));
        cardsPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        lblTotalStudents = new JLabel("0");
        lblTotalTeachers = new JLabel("0");
        lblActiveCourses = new JLabel("0");

        cardsPanel.add(createCard("Total Students", lblTotalStudents, primaryBlue));
        cardsPanel.add(createCard("Total Teachers", lblTotalTeachers, new Color(46, 204, 113)));
        cardsPanel.add(createCard("Active Courses", lblActiveCourses, new Color(155, 89, 182)));

        mainContentPanel.add(cardsPanel);
        mainContentPanel.add(Box.createRigidArea(new Dimension(0, 30)));
    }

    private JPanel createCard(String title, JLabel valueLabel, Color accentColor) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(230, 230, 230)),
            new EmptyBorder(20, 20, 20, 20)
        ));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(cardTitleFont);
        titleLabel.setForeground(Color.GRAY);

        valueLabel.setFont(cardValueFont);
        valueLabel.setForeground(accentColor);

        card.add(titleLabel, BorderLayout.NORTH);
        card.add(valueLabel, BorderLayout.CENTER);

        return card;
    }

    private void buildDetailsSections() {
        JPanel detailsPanel = new JPanel(new GridLayout(1, 2, 20, 0));
        detailsPanel.setBackground(bgColor);
        detailsPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Left: Schedule
        JPanel schedulePanel = new JPanel(new BorderLayout());
        schedulePanel.setBackground(Color.WHITE);
        schedulePanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(230, 230, 230)),
            new EmptyBorder(20, 20, 20, 20)
        ));

        JLabel scheduleTitle = new JLabel("Weekly Schedule");
        scheduleTitle.setFont(cardTitleFont);
        scheduleTitle.setBorder(new EmptyBorder(0, 0, 10, 0));
        schedulePanel.add(scheduleTitle, BorderLayout.NORTH);

        scheduleModel = new DefaultListModel<>();
        JList<String> scheduleList = new JList<>(scheduleModel);
        scheduleList.setFont(regularFont);
        schedulePanel.add(new JScrollPane(scheduleList), BorderLayout.CENTER);

        // Right: Activity
        JPanel activityPanel = new JPanel(new BorderLayout());
        activityPanel.setBackground(Color.WHITE);
        activityPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(230, 230, 230)),
            new EmptyBorder(20, 20, 20, 20)
        ));

        JLabel activityTitle = new JLabel("Recent Activity");
        activityTitle.setFont(cardTitleFont);
        activityTitle.setBorder(new EmptyBorder(0, 0, 10, 0));
        activityPanel.add(activityTitle, BorderLayout.NORTH);

        activityModel = new DefaultListModel<>();
        JList<String> activityList = new JList<>(activityModel);
        activityList.setFont(regularFont);
        activityPanel.add(new JScrollPane(activityList), BorderLayout.CENTER);

        detailsPanel.add(schedulePanel);
        detailsPanel.add(activityPanel);

        mainContentPanel.add(detailsPanel);
    }

    private void loadData() {
        // Update stats
        lblTotalStudents.setText(String.valueOf(gestionActeurs.getEtudiants().size()));
        lblTotalTeachers.setText(String.valueOf(gestionActeurs.getEnseignants().size()));
        lblActiveCourses.setText(String.valueOf(gestionFormation.getCours().size()));

        // Update schedule
        scheduleModel.clear();
        scheduleModel.addElement("Loading upcoming sessions...");
        // In a real scenario, we would loop through gestionPlanning.getSeances()
        // but since seances are not exposed we will add a placeholder for now
        scheduleModel.addElement("1. POO (INFO-301) - Amphi A - 08:00");
        scheduleModel.addElement("2. BDD (INFO-302) - Salle 402 - 10:30");

        // Update Activity
        activityModel.clear();
        for(Etudiant e : gestionActeurs.getEtudiants()) {
            activityModel.addElement("New student registered: " + e.getPrenom() + " " + e.getNom());
        }
        for(Enseignant e : gestionActeurs.getEnseignants()) {
            activityModel.addElement("Teacher updated: " + e.getNom());
        }
    }
}

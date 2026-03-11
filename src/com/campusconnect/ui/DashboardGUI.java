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
    private JPanel enseignantsPanel;
    private JPanel coursPanel;
    private JPanel sallesPanel;
    private JPanel planningPanel;
    private DefaultTableModel planningTableModel;
    private DefaultTableModel sallesTableModel;
    private DefaultTableModel coursTableModel;
    private DefaultTableModel enseignantsTableModel;
    private DefaultTableModel studentsTableModel;
    private JTable studentsTable;

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
        sidebar.add(Box.createRigidArea(new Dimension(0, 15)));

        JButton btnEnseignants = createSidebarButton("Enseignants");
        btnEnseignants.addActionListener(e -> {
            cardLayout.show(cardsContainer, "Enseignants");
            refreshEnseignantsTable();
        });
        sidebar.add(btnEnseignants);
        sidebar.add(Box.createRigidArea(new Dimension(0, 15)));

        JButton btnCours = createSidebarButton("Cours");
        btnCours.addActionListener(e -> {
            cardLayout.show(cardsContainer, "Cours");
            refreshCoursTable();
        });
        sidebar.add(btnCours);
        sidebar.add(Box.createRigidArea(new Dimension(0, 15)));

        JButton btnSalles = createSidebarButton("Salles");
        btnSalles.addActionListener(e -> {
            cardLayout.show(cardsContainer, "Salles");
            refreshSallesTable();
        });
        sidebar.add(btnSalles);
        sidebar.add(Box.createRigidArea(new Dimension(0, 15)));

        JButton btnPlanning = createSidebarButton("Planning");
        btnPlanning.addActionListener(e -> {
            cardLayout.show(cardsContainer, "Planning");
            refreshPlanningTable();
        });
        sidebar.add(btnPlanning);

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
        JButton btnEditStudent = new JButton("Modifier");
        btnEditStudent.setBackground(accentGold);
        btnEditStudent.setForeground(bgDark);
        btnEditStudent.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnEditStudent.addActionListener(e -> showEditStudentDialog(studentsTable));

        JButton btnDeleteStudent = new JButton("Supprimer");
        btnDeleteStudent.setBackground(new Color(231, 76, 60));
        btnDeleteStudent.setForeground(textPrimary);
        btnDeleteStudent.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnDeleteStudent.addActionListener(e -> deleteSelectedStudent(studentsTable));

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(bgDark);
        buttonPanel.add(btnAddStudent);
        buttonPanel.add(btnEditStudent);
        buttonPanel.add(btnDeleteStudent);
        topPanel.add(buttonPanel, BorderLayout.EAST);

        studentsPanel.add(topPanel, BorderLayout.NORTH);

        String[] columns = {"Matricule", "Nom", "Prénom", "Filière", "Année"};
        studentsTableModel = new DefaultTableModel(columns, 0);
        studentsTable = new JTable(studentsTableModel);
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
        // --- Enseignants Panel ---
        enseignantsPanel = new JPanel(new BorderLayout(0, 20));
        enseignantsPanel.setBackground(bgDark);
        enseignantsPanel.setBorder(new EmptyBorder(30, 40, 30, 40));

        JPanel topPanelEns = new JPanel(new BorderLayout());
        topPanelEns.setBackground(bgDark);
        JLabel titleEns = new JLabel("Gestion des Enseignants");
        titleEns.setFont(headerFont);
        titleEns.setForeground(textPrimary);
        topPanelEns.add(titleEns, BorderLayout.WEST);

        JButton btnAddEns = new JButton("+ Ajouter");
        btnAddEns.setBackground(accentGreen);
        btnAddEns.setForeground(bgDark);
        btnAddEns.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnAddEns.addActionListener(e -> showAddEnseignantDialog());
        JButton btnEditEns = new JButton("Modifier");
        btnEditEns.setBackground(accentGold);
        btnEditEns.setForeground(bgDark);
        btnEditEns.setFont(new Font("Segoe UI", Font.BOLD, 14));
        JButton btnDelEns = new JButton("Supprimer");
        btnDelEns.setBackground(new Color(231, 76, 60));
        btnDelEns.setForeground(textPrimary);
        btnDelEns.setFont(new Font("Segoe UI", Font.BOLD, 14));
        JPanel btnPanelEns = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnPanelEns.setBackground(bgDark);
        btnPanelEns.add(btnAddEns);
        btnPanelEns.add(btnEditEns);
        btnPanelEns.add(btnDelEns);
        topPanelEns.add(btnPanelEns, BorderLayout.EAST);
        enseignantsPanel.add(topPanelEns, BorderLayout.NORTH);

        String[] colsEns = {"ID", "Nom", "Prénom", "Département", "Statut"};
        enseignantsTableModel = new DefaultTableModel(colsEns, 0);
        JTable enseignantsTable = new JTable(enseignantsTableModel);
        enseignantsTable.setBackground(bgPanel);
        enseignantsTable.setForeground(textPrimary);
        enseignantsTable.setGridColor(new Color(60, 60, 60));
        enseignantsTable.setRowHeight(30);
        enseignantsTable.getTableHeader().setBackground(new Color(40, 40, 40));
        enseignantsTable.getTableHeader().setForeground(accentGold);
        enseignantsTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        JScrollPane scrollEns = new JScrollPane(enseignantsTable);
        scrollEns.getViewport().setBackground(bgDark);
        scrollEns.setBorder(BorderFactory.createLineBorder(new Color(50, 50, 50)));
        enseignantsPanel.add(scrollEns, BorderLayout.CENTER);
        cardsContainer.add(enseignantsPanel, "Enseignants");
        // --- Cours Panel ---
        coursPanel = new JPanel(new BorderLayout(0, 20));
        coursPanel.setBackground(bgDark);
        coursPanel.setBorder(new EmptyBorder(30, 40, 30, 40));

        JPanel topPanelCours = new JPanel(new BorderLayout());
        topPanelCours.setBackground(bgDark);
        JLabel titleCours = new JLabel("Gestion des Cours");
        titleCours.setFont(headerFont);
        titleCours.setForeground(textPrimary);
        topPanelCours.add(titleCours, BorderLayout.WEST);

        JButton btnAddCours = new JButton("+ Ajouter");
        btnAddCours.setBackground(accentGreen);
        btnAddCours.setForeground(bgDark);
        btnAddCours.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnAddCours.addActionListener(e -> showAddCoursDialog());
        JButton btnEditCours = new JButton("Modifier");
        btnEditCours.setBackground(accentGold);
        btnEditCours.setForeground(bgDark);
        btnEditCours.setFont(new Font("Segoe UI", Font.BOLD, 14));
        JButton btnDelCours = new JButton("Supprimer");
        btnDelCours.setBackground(new Color(231, 76, 60));
        btnDelCours.setForeground(textPrimary);
        btnDelCours.setFont(new Font("Segoe UI", Font.BOLD, 14));
        JPanel btnPanelCours = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnPanelCours.setBackground(bgDark);
        btnPanelCours.add(btnAddCours);
        btnPanelCours.add(btnEditCours);
        btnPanelCours.add(btnDelCours);
        topPanelCours.add(btnPanelCours, BorderLayout.EAST);
        coursPanel.add(topPanelCours, BorderLayout.NORTH);

        String[] colsCours = {"Code", "Intitulé", "Description", "Vol. Horaire", "Responsable"};
        coursTableModel = new DefaultTableModel(colsCours, 0);
        JTable coursTable = new JTable(coursTableModel);
        coursTable.setBackground(bgPanel);
        coursTable.setForeground(textPrimary);
        coursTable.setGridColor(new Color(60, 60, 60));
        coursTable.setRowHeight(30);
        coursTable.getTableHeader().setBackground(new Color(40, 40, 40));
        coursTable.getTableHeader().setForeground(accentGold);
        coursTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        JScrollPane scrollCours = new JScrollPane(coursTable);
        scrollCours.getViewport().setBackground(bgDark);
        scrollCours.setBorder(BorderFactory.createLineBorder(new Color(50, 50, 50)));
        coursPanel.add(scrollCours, BorderLayout.CENTER);
        cardsContainer.add(coursPanel, "Cours");
        // --- Salles Panel ---
        sallesPanel = new JPanel(new BorderLayout(0, 20));
        sallesPanel.setBackground(bgDark);
        sallesPanel.setBorder(new EmptyBorder(30, 40, 30, 40));

        JPanel topPanelSalles = new JPanel(new BorderLayout());
        topPanelSalles.setBackground(bgDark);
        JLabel titleSalles = new JLabel("Gestion des Salles");
        titleSalles.setFont(headerFont);
        titleSalles.setForeground(textPrimary);
        topPanelSalles.add(titleSalles, BorderLayout.WEST);

        JButton btnAddSalles = new JButton("+ Ajouter");
        btnAddSalles.setBackground(accentGreen);
        btnAddSalles.setForeground(bgDark);
        btnAddSalles.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnAddSalles.addActionListener(e -> showAddSalleDialog());
        JButton btnEditSalles = new JButton("Modifier");
        btnEditSalles.setBackground(accentGold);
        btnEditSalles.setForeground(bgDark);
        btnEditSalles.setFont(new Font("Segoe UI", Font.BOLD, 14));
        JButton btnDelSalles = new JButton("Supprimer");
        btnDelSalles.setBackground(new Color(231, 76, 60));
        btnDelSalles.setForeground(textPrimary);
        btnDelSalles.setFont(new Font("Segoe UI", Font.BOLD, 14));
        JPanel btnPanelSalles = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnPanelSalles.setBackground(bgDark);
        btnPanelSalles.add(btnAddSalles);
        btnPanelSalles.add(btnEditSalles);
        btnPanelSalles.add(btnDelSalles);
        topPanelSalles.add(btnPanelSalles, BorderLayout.EAST);
        sallesPanel.add(topPanelSalles, BorderLayout.NORTH);

        String[] colsSalles = {"ID Salle", "Type", "Capacité"};
        sallesTableModel = new DefaultTableModel(colsSalles, 0);
        JTable sallesTable = new JTable(sallesTableModel);
        sallesTable.setBackground(bgPanel);
        sallesTable.setForeground(textPrimary);
        sallesTable.setGridColor(new Color(60, 60, 60));
        sallesTable.setRowHeight(30);
        sallesTable.getTableHeader().setBackground(new Color(40, 40, 40));
        sallesTable.getTableHeader().setForeground(accentGold);
        sallesTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        JScrollPane scrollSalles = new JScrollPane(sallesTable);
        scrollSalles.getViewport().setBackground(bgDark);
        scrollSalles.setBorder(BorderFactory.createLineBorder(new Color(50, 50, 50)));
        sallesPanel.add(scrollSalles, BorderLayout.CENTER);
        cardsContainer.add(sallesPanel, "Salles");
        // --- Planning Panel ---
        planningPanel = new JPanel(new BorderLayout(0, 20));
        planningPanel.setBackground(bgDark);
        planningPanel.setBorder(new EmptyBorder(30, 40, 30, 40));

        JPanel topPanelPlan = new JPanel(new BorderLayout());
        topPanelPlan.setBackground(bgDark);
        JLabel titlePlan = new JLabel("Gestion du Planning");
        titlePlan.setFont(headerFont);
        titlePlan.setForeground(textPrimary);
        topPanelPlan.add(titlePlan, BorderLayout.WEST);

        JButton btnAddPlan = new JButton("+ Planifier");
        btnAddPlan.setBackground(accentGreen);
        btnAddPlan.setForeground(bgDark);
        btnAddPlan.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnAddPlan.addActionListener(e -> showAddPlanningDialog());
        JButton btnEditPlan = new JButton("Modifier");
        btnEditPlan.setBackground(accentGold);
        btnEditPlan.setForeground(bgDark);
        btnEditPlan.setFont(new Font("Segoe UI", Font.BOLD, 14));
        JButton btnDelPlan = new JButton("Supprimer");
        btnDelPlan.setBackground(new Color(231, 76, 60));
        btnDelPlan.setForeground(textPrimary);
        btnDelPlan.setFont(new Font("Segoe UI", Font.BOLD, 14));
        JPanel btnPanelPlan = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnPanelPlan.setBackground(bgDark);
        btnPanelPlan.add(btnAddPlan);
        btnPanelPlan.add(btnEditPlan);
        btnPanelPlan.add(btnDelPlan);
        topPanelPlan.add(btnPanelPlan, BorderLayout.EAST);
        planningPanel.add(topPanelPlan, BorderLayout.NORTH);

        String[] colsPlan = {"Date", "Début", "Fin", "Cours", "Groupe", "Salle", "Enseignant"};
        planningTableModel = new DefaultTableModel(colsPlan, 0);
        JTable planningTable = new JTable(planningTableModel);
        planningTable.setBackground(bgPanel);
        planningTable.setForeground(textPrimary);
        planningTable.setGridColor(new Color(60, 60, 60));
        planningTable.setRowHeight(30);
        planningTable.getTableHeader().setBackground(new Color(40, 40, 40));
        planningTable.getTableHeader().setForeground(accentGold);
        planningTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        JScrollPane scrollPlan = new JScrollPane(planningTable);
        scrollPlan.getViewport().setBackground(bgDark);
        scrollPlan.setBorder(BorderFactory.createLineBorder(new Color(50, 50, 50)));
        planningPanel.add(scrollPlan, BorderLayout.CENTER);
        cardsContainer.add(planningPanel, "Planning");

        btnEditPlan.addActionListener(e -> showEditPlanningDialog(planningTable));
        btnDelPlan.addActionListener(e -> deleteSelectedPlanning(planningTable));
        refreshPlanningTable();


        btnEditSalles.addActionListener(e -> showEditSalleDialog(sallesTable));
        btnDelSalles.addActionListener(e -> deleteSelectedSalle(sallesTable));
        refreshSallesTable();


        btnEditCours.addActionListener(e -> showEditCoursDialog(coursTable));
        btnDelCours.addActionListener(e -> deleteSelectedCours(coursTable));
        refreshCoursTable();


        btnEditEns.addActionListener(e -> showEditEnseignantDialog(enseignantsTable));
        btnDelEns.addActionListener(e -> deleteSelectedEnseignant(enseignantsTable));
        refreshEnseignantsTable();

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
        dashboardPanel.add(Box.createVerticalGlue());
        dashboardPanel.add(Box.createRigidArea(new Dimension(0, 40)));
    }

    private void refreshPlanningTable() {
        planningTableModel.setRowCount(0);
        for (Seance s : gestionPlanning.getSeances()) {
            planningTableModel.addRow(new Object[]{s.getDate().toString(), s.getHeureDebut().toString(), s.getHeureFin().toString(), s.getGroupe().getCours().getCode(), s.getGroupe().getNom(), s.getSalle().getIdSalle(), s.getEnseignant().getNom()});
        }
    }

    private void refreshSallesTable() {
        sallesTableModel.setRowCount(0);
        for (Salle s : gestionFormation.getSalles()) {
            sallesTableModel.addRow(new Object[]{s.getIdSalle(), s.getType(), s.getCapaciteAccueil()});
        }
    }

    private void refreshCoursTable() {
        coursTableModel.setRowCount(0);
        for (Cours c : gestionFormation.getCours()) {
            coursTableModel.addRow(new Object[]{c.getCode(), c.getIntitule(), c.getDescription(), c.getVolumeHoraire(), c.getResponsable().getNom()});
        }
    }

    private void refreshEnseignantsTable() {
        enseignantsTableModel.setRowCount(0);
        for (Enseignant e : gestionActeurs.getEnseignants()) {
            enseignantsTableModel.addRow(new Object[]{e.getId(), e.getNom(), e.getPrenom(), e.getDepartement(), e.getStatut()});
        }
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

    private void deleteSelectedStudent(JTable table) {
        int row = table.getSelectedRow();
        if (row >= 0) {
            String matricule = (String) table.getValueAt(row, 0);
            Etudiant e = gestionActeurs.getEtudiantByMatricule(matricule);
            if (e != null) {
                int confirm = JOptionPane.showConfirmDialog(this, "Voulez-vous vraiment supprimer cet étudiant ?", "Confirmation", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    gestionActeurs.supprimerPersonne(e);
                    refreshStudentsTable();
                    refreshDashboardStats();
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner un étudiant.", "Erreur", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void showEditStudentDialog(JTable table) {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner un étudiant.", "Erreur", JOptionPane.WARNING_MESSAGE);
            return;
        }
        String matricule = (String) table.getValueAt(row, 0);
        Etudiant e = gestionActeurs.getEtudiantByMatricule(matricule);
        if (e == null) return;

        JDialog dialog = new JDialog(this, "Modifier un Étudiant", true);
        dialog.setSize(400, 450);
        dialog.setLocationRelativeTo(this);
        dialog.getContentPane().setBackground(bgDark);

        JPanel panel = new JPanel(new GridLayout(6, 2, 10, 10));
        panel.setBackground(bgDark);
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel lblNom = new JLabel("Nom :"); lblNom.setForeground(textPrimary);
        JTextField txtNom = new JTextField(e.getNom());
        JLabel lblPrenom = new JLabel("Prénom :"); lblPrenom.setForeground(textPrimary);
        JTextField txtPrenom = new JTextField(e.getPrenom());
        JLabel lblEmail = new JLabel("Email :"); lblEmail.setForeground(textPrimary);
        JTextField txtEmail = new JTextField(e.getEmail());
        JLabel lblFiliere = new JLabel("Filière :"); lblFiliere.setForeground(textPrimary);
        JTextField txtFiliere = new JTextField(e.getFiliere());
        JLabel lblAnnee = new JLabel("Année (ex: L3) :"); lblAnnee.setForeground(textPrimary);
        JTextField txtAnnee = new JTextField(e.getAnneeEtude());

        JButton btnSubmit = new JButton("Enregistrer");
        btnSubmit.setBackground(accentGold);
        btnSubmit.setForeground(bgDark);
        btnSubmit.setFont(new Font("Segoe UI", Font.BOLD, 14));

        btnSubmit.addActionListener(event -> {
            e.setNom(txtNom.getText());
            e.setPrenom(txtPrenom.getText());
            e.setEmail(txtEmail.getText());
            e.setFiliere(txtFiliere.getText());
            e.setAnneeEtude(txtAnnee.getText());
            refreshStudentsTable();
            dialog.dispose();
        });

        panel.add(lblNom); panel.add(txtNom);
        panel.add(lblPrenom); panel.add(txtPrenom);
        panel.add(lblEmail); panel.add(txtEmail);
        panel.add(lblFiliere); panel.add(txtFiliere);
        panel.add(lblAnnee); panel.add(txtAnnee);
        panel.add(new JLabel("")); panel.add(btnSubmit);

        dialog.add(panel);
        dialog.setVisible(true);
    }

    private void showAddPlanningDialog() {
        JDialog dialog = new JDialog(this, "Planifier une Séance", true);
        dialog.setSize(400, 450);
        dialog.setLocationRelativeTo(this);
        dialog.getContentPane().setBackground(bgDark);

        JPanel panel = new JPanel(new GridLayout(7, 2, 10, 10));
        panel.setBackground(bgDark);
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel lblCodeCours = new JLabel("Code Cours :"); lblCodeCours.setForeground(textPrimary);
        JTextField txtCodeCours = new JTextField();
        JLabel lblGrpIndex = new JLabel("Index Groupe (1,2..) :"); lblGrpIndex.setForeground(textPrimary);
        JTextField txtGrpIndex = new JTextField();
        JLabel lblIdEns = new JLabel("ID Enseignant :"); lblIdEns.setForeground(textPrimary);
        JTextField txtIdEns = new JTextField();
        JLabel lblIdSalle = new JLabel("ID Salle :"); lblIdSalle.setForeground(textPrimary);
        JTextField txtIdSalle = new JTextField();
        JLabel lblDate = new JLabel("Date (YYYY-MM-DD) :"); lblDate.setForeground(textPrimary);
        JTextField txtDate = new JTextField();
        JLabel lblDebut = new JLabel("Début (HH:MM) :"); lblDebut.setForeground(textPrimary);
        JTextField txtDebut = new JTextField();
        JLabel lblFin = new JLabel("Fin (HH:MM) :"); lblFin.setForeground(textPrimary);
        JTextField txtFin = new JTextField();

        JButton btnSubmit = new JButton("Enregistrer");
        btnSubmit.setBackground(accentGold);
        btnSubmit.setForeground(bgDark);
        btnSubmit.setFont(new Font("Segoe UI", Font.BOLD, 14));

        btnSubmit.addActionListener(e -> {
            try {
                Cours cours = gestionFormation.getCoursByCode(txtCodeCours.getText());
                if (cours == null || cours.getGroupes().isEmpty()) throw new Exception("Cours introuvable ou sans groupe");
                int idx = Integer.parseInt(txtGrpIndex.getText()) - 1;
                Groupe grp = cours.getGroupes().get(idx);
                Enseignant ens = gestionActeurs.getEnseignantById(txtIdEns.getText());
                Salle salle = gestionFormation.getSalleById(txtIdSalle.getText());
                if (ens == null || salle == null) throw new Exception("Enseignant ou Salle introuvable");
                LocalDate date = LocalDate.parse(txtDate.getText());
                java.time.LocalTime debut = java.time.LocalTime.parse(txtDebut.getText());
                java.time.LocalTime fin = java.time.LocalTime.parse(txtFin.getText());
                gestionPlanning.planifierSeance(grp, ens, salle, date, debut, fin);
                refreshPlanningTable();
                dialog.dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Erreur: " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        });

        panel.add(lblCodeCours); panel.add(txtCodeCours);
        panel.add(lblGrpIndex); panel.add(txtGrpIndex);
        panel.add(lblIdEns); panel.add(txtIdEns);
        panel.add(lblIdSalle); panel.add(txtIdSalle);
        panel.add(lblDate); panel.add(txtDate);
        panel.add(lblDebut); panel.add(txtDebut);
        panel.add(lblFin); panel.add(txtFin);

        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.add(panel, BorderLayout.CENTER);
        wrapper.add(btnSubmit, BorderLayout.SOUTH);
        dialog.add(wrapper);
        dialog.setVisible(true);
    }

    private void showEditPlanningDialog(JTable table) {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner une séance.", "Erreur", JOptionPane.WARNING_MESSAGE);
            return;
        }
        String dateStr = (String) table.getValueAt(row, 0);
        String debutStr = (String) table.getValueAt(row, 1);
        String codeCours = (String) table.getValueAt(row, 3);
        Seance s = gestionPlanning.getSeanceByDetails(codeCours, dateStr, debutStr);
        if (s == null) return;

        JDialog dialog = new JDialog(this, "Modifier une Séance", true);
        dialog.setSize(400, 250);
        dialog.setLocationRelativeTo(this);
        dialog.getContentPane().setBackground(bgDark);

        JPanel panel = new JPanel(new GridLayout(3, 2, 10, 10));
        panel.setBackground(bgDark);
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel lblDate = new JLabel("Date (YYYY-MM-DD) :"); lblDate.setForeground(textPrimary);
        JTextField txtDate = new JTextField(s.getDate().toString());
        JLabel lblDebut = new JLabel("Début (HH:MM) :"); lblDebut.setForeground(textPrimary);
        JTextField txtDebut = new JTextField(s.getHeureDebut().toString());
        JLabel lblFin = new JLabel("Fin (HH:MM) :"); lblFin.setForeground(textPrimary);
        JTextField txtFin = new JTextField(s.getHeureFin().toString());

        JButton btnSubmit = new JButton("Enregistrer");
        btnSubmit.setBackground(accentGold);
        btnSubmit.setForeground(bgDark);
        btnSubmit.setFont(new Font("Segoe UI", Font.BOLD, 14));

        btnSubmit.addActionListener(event -> {
            try {
                s.setDate(LocalDate.parse(txtDate.getText()));
                s.setHeureDebut(java.time.LocalTime.parse(txtDebut.getText()));
                s.setHeureFin(java.time.LocalTime.parse(txtFin.getText()));
                refreshPlanningTable();
                dialog.dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Format date/heure invalide.", "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        });

        panel.add(lblDate); panel.add(txtDate);
        panel.add(lblDebut); panel.add(txtDebut);
        panel.add(lblFin); panel.add(txtFin);

        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.add(panel, BorderLayout.CENTER);
        wrapper.add(btnSubmit, BorderLayout.SOUTH);
        dialog.add(wrapper);
        dialog.setVisible(true);
    }

    private void deleteSelectedPlanning(JTable table) {
        int row = table.getSelectedRow();
        if (row >= 0) {
            String dateStr = (String) table.getValueAt(row, 0);
            String debutStr = (String) table.getValueAt(row, 1);
            String codeCours = (String) table.getValueAt(row, 3);
            Seance s = gestionPlanning.getSeanceByDetails(codeCours, dateStr, debutStr);
            if (s != null) {
                int confirm = JOptionPane.showConfirmDialog(this, "Voulez-vous vraiment supprimer cette séance ?", "Confirmation", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    gestionPlanning.supprimerSeance(s);
                    refreshPlanningTable();
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner une séance.", "Erreur", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void showAddSalleDialog() {
        JDialog dialog = new JDialog(this, "Ajouter une Salle", true);
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(this);
        dialog.getContentPane().setBackground(bgDark);

        JPanel panel = new JPanel(new GridLayout(4, 2, 10, 10));
        panel.setBackground(bgDark);
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel lblId = new JLabel("ID Salle :"); lblId.setForeground(textPrimary);
        JTextField txtId = new JTextField();
        JLabel lblType = new JLabel("Type (AMPHI, TP, COURS_CLASSIQUE) :"); lblType.setForeground(textPrimary);
        JTextField txtType = new JTextField();
        JLabel lblCap = new JLabel("Capacité :"); lblCap.setForeground(textPrimary);
        JTextField txtCap = new JTextField();

        JButton btnSubmit = new JButton("Enregistrer");
        btnSubmit.setBackground(accentGold);
        btnSubmit.setForeground(bgDark);
        btnSubmit.setFont(new Font("Segoe UI", Font.BOLD, 14));

        btnSubmit.addActionListener(e -> {
            try {
                Salle nouvelle = new Salle(txtId.getText(), Integer.parseInt(txtCap.getText()), Salle.TypeSalle.valueOf(txtType.getText().toUpperCase()));
                gestionFormation.ajouterSalle(nouvelle);
                refreshSallesTable();
                dialog.dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Veuillez remplir correctement les champs.", "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        });

        panel.add(lblId); panel.add(txtId);
        panel.add(lblType); panel.add(txtType);
        panel.add(lblCap); panel.add(txtCap);
        panel.add(new JLabel("")); panel.add(btnSubmit);

        dialog.add(panel);
        dialog.setVisible(true);
    }

    private void showEditSalleDialog(JTable table) {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner une salle.", "Erreur", JOptionPane.WARNING_MESSAGE);
            return;
        }
        String id = (String) table.getValueAt(row, 0);
        Salle s = gestionFormation.getSalleById(id);
        if (s == null) return;

        JDialog dialog = new JDialog(this, "Modifier une Salle", true);
        dialog.setSize(400, 250);
        dialog.setLocationRelativeTo(this);
        dialog.getContentPane().setBackground(bgDark);

        JPanel panel = new JPanel(new GridLayout(3, 2, 10, 10));
        panel.setBackground(bgDark);
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel lblType = new JLabel("Type (AMPHI, TP, COURS_CLASSIQUE) :"); lblType.setForeground(textPrimary);
        JTextField txtType = new JTextField(s.getType().toString());
        JLabel lblCap = new JLabel("Capacité :"); lblCap.setForeground(textPrimary);
        JTextField txtCap = new JTextField(String.valueOf(s.getCapaciteAccueil()));

        JButton btnSubmit = new JButton("Enregistrer");
        btnSubmit.setBackground(accentGold);
        btnSubmit.setForeground(bgDark);
        btnSubmit.setFont(new Font("Segoe UI", Font.BOLD, 14));

        btnSubmit.addActionListener(event -> {
            s.setType(Salle.TypeSalle.valueOf(txtType.getText().toUpperCase()));
            s.setCapaciteAccueil(Integer.parseInt(txtCap.getText()));
            refreshSallesTable();
            dialog.dispose();
        });

        panel.add(lblType); panel.add(txtType);
        panel.add(lblCap); panel.add(txtCap);
        panel.add(new JLabel("")); panel.add(btnSubmit);

        dialog.add(panel);
        dialog.setVisible(true);
    }

    private void deleteSelectedSalle(JTable table) {
        int row = table.getSelectedRow();
        if (row >= 0) {
            String id = (String) table.getValueAt(row, 0);
            Salle s = gestionFormation.getSalleById(id);
            if (s != null) {
                int confirm = JOptionPane.showConfirmDialog(this, "Voulez-vous vraiment supprimer cette salle ?", "Confirmation", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    gestionFormation.supprimerSalle(s);
                    refreshSallesTable();
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner une salle.", "Erreur", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void showAddCoursDialog() {
        JDialog dialog = new JDialog(this, "Ajouter un Cours", true);
        dialog.setSize(400, 400);
        dialog.setLocationRelativeTo(this);
        dialog.getContentPane().setBackground(bgDark);

        JPanel panel = new JPanel(new GridLayout(6, 2, 10, 10));
        panel.setBackground(bgDark);
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel lblCode = new JLabel("Code :"); lblCode.setForeground(textPrimary);
        JTextField txtCode = new JTextField();
        JLabel lblIntitule = new JLabel("Intitulé :"); lblIntitule.setForeground(textPrimary);
        JTextField txtIntitule = new JTextField();
        JLabel lblDesc = new JLabel("Description :"); lblDesc.setForeground(textPrimary);
        JTextField txtDesc = new JTextField();
        JLabel lblVol = new JLabel("Volume horaire :"); lblVol.setForeground(textPrimary);
        JTextField txtVol = new JTextField();
        JLabel lblResp = new JLabel("ID Responsable :"); lblResp.setForeground(textPrimary);
        JTextField txtResp = new JTextField();

        JButton btnSubmit = new JButton("Enregistrer");
        btnSubmit.setBackground(accentGold);
        btnSubmit.setForeground(bgDark);
        btnSubmit.setFont(new Font("Segoe UI", Font.BOLD, 14));

        btnSubmit.addActionListener(e -> {
            try {
                Enseignant resp = gestionActeurs.getEnseignantById(txtResp.getText());
                if (resp == null) {
                    JOptionPane.showMessageDialog(dialog, "Enseignant responsable introuvable.", "Erreur", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                Cours nouveau = new Cours(txtCode.getText(), txtIntitule.getText(), txtDesc.getText(),
                        Integer.parseInt(txtVol.getText()), resp);
                gestionFormation.ajouterCours(nouveau);
                refreshCoursTable();
                refreshDashboardStats();
                dialog.dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Veuillez remplir correctement les champs.", "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        });

        panel.add(lblCode); panel.add(txtCode);
        panel.add(lblIntitule); panel.add(txtIntitule);
        panel.add(lblDesc); panel.add(txtDesc);
        panel.add(lblVol); panel.add(txtVol);
        panel.add(lblResp); panel.add(txtResp);
        panel.add(new JLabel("")); panel.add(btnSubmit);

        dialog.add(panel);
        dialog.setVisible(true);
    }

    private void showEditCoursDialog(JTable table) {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner un cours.", "Erreur", JOptionPane.WARNING_MESSAGE);
            return;
        }
        String code = (String) table.getValueAt(row, 0);
        Cours c = gestionFormation.getCoursByCode(code);
        if (c == null) return;

        JDialog dialog = new JDialog(this, "Modifier un Cours", true);
        dialog.setSize(400, 350);
        dialog.setLocationRelativeTo(this);
        dialog.getContentPane().setBackground(bgDark);

        JPanel panel = new JPanel(new GridLayout(5, 2, 10, 10));
        panel.setBackground(bgDark);
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel lblCode = new JLabel("Code :"); lblCode.setForeground(textPrimary);
        JTextField txtCode = new JTextField(c.getCode());
        JLabel lblIntitule = new JLabel("Intitulé :"); lblIntitule.setForeground(textPrimary);
        JTextField txtIntitule = new JTextField(c.getIntitule());
        JLabel lblDesc = new JLabel("Description :"); lblDesc.setForeground(textPrimary);
        JTextField txtDesc = new JTextField(c.getDescription());
        JLabel lblVol = new JLabel("Volume horaire :"); lblVol.setForeground(textPrimary);
        JTextField txtVol = new JTextField(String.valueOf(c.getVolumeHoraire()));

        JButton btnSubmit = new JButton("Enregistrer");
        btnSubmit.setBackground(accentGold);
        btnSubmit.setForeground(bgDark);
        btnSubmit.setFont(new Font("Segoe UI", Font.BOLD, 14));

        btnSubmit.addActionListener(event -> {
            c.setCode(txtCode.getText());
            c.setIntitule(txtIntitule.getText());
            c.setDescription(txtDesc.getText());
            c.setVolumeHoraire(Integer.parseInt(txtVol.getText()));
            refreshCoursTable();
            dialog.dispose();
        });

        panel.add(lblCode); panel.add(txtCode);
        panel.add(lblIntitule); panel.add(txtIntitule);
        panel.add(lblDesc); panel.add(txtDesc);
        panel.add(lblVol); panel.add(txtVol);
        panel.add(new JLabel("")); panel.add(btnSubmit);

        dialog.add(panel);
        dialog.setVisible(true);
    }

    private void deleteSelectedCours(JTable table) {
        int row = table.getSelectedRow();
        if (row >= 0) {
            String code = (String) table.getValueAt(row, 0);
            Cours c = gestionFormation.getCoursByCode(code);
            if (c != null) {
                int confirm = JOptionPane.showConfirmDialog(this, "Voulez-vous vraiment supprimer ce cours ?", "Confirmation", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    gestionFormation.supprimerCours(c);
                    refreshCoursTable();
                    refreshDashboardStats();
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner un cours.", "Erreur", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void showAddEnseignantDialog() {
        JDialog dialog = new JDialog(this, "Ajouter un Enseignant", true);
        dialog.setSize(400, 450);
        dialog.setLocationRelativeTo(this);
        dialog.getContentPane().setBackground(bgDark);

        JPanel panel = new JPanel(new GridLayout(7, 2, 10, 10));
        panel.setBackground(bgDark);
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel lblId = new JLabel("ID unique :"); lblId.setForeground(textPrimary);
        JTextField txtId = new JTextField();
        JLabel lblNom = new JLabel("Nom :"); lblNom.setForeground(textPrimary);
        JTextField txtNom = new JTextField();
        JLabel lblPrenom = new JLabel("Prénom :"); lblPrenom.setForeground(textPrimary);
        JTextField txtPrenom = new JTextField();
        JLabel lblEmail = new JLabel("Email :"); lblEmail.setForeground(textPrimary);
        JTextField txtEmail = new JTextField();
        JLabel lblDep = new JLabel("Département :"); lblDep.setForeground(textPrimary);
        JTextField txtDep = new JTextField();
        JLabel lblStatut = new JLabel("Statut (PERMANENT/VACATAIRE) :"); lblStatut.setForeground(textPrimary);
        JTextField txtStatut = new JTextField();

        JButton btnSubmit = new JButton("Enregistrer");
        btnSubmit.setBackground(accentGold);
        btnSubmit.setForeground(bgDark);
        btnSubmit.setFont(new Font("Segoe UI", Font.BOLD, 14));

        btnSubmit.addActionListener(e -> {
            try {
                Enseignant nouveau = new Enseignant(txtId.getText(), txtNom.getText(), txtPrenom.getText(),
                        txtEmail.getText(), LocalDate.now(), Enseignant.Statut.valueOf(txtStatut.getText().toUpperCase()), txtDep.getText());
                gestionActeurs.ajouterPersonne(nouveau);
                refreshEnseignantsTable();
                refreshDashboardStats();
                dialog.dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Veuillez remplir correctement les champs.", "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        });

        panel.add(lblId); panel.add(txtId);
        panel.add(lblNom); panel.add(txtNom);
        panel.add(lblPrenom); panel.add(txtPrenom);
        panel.add(lblEmail); panel.add(txtEmail);
        panel.add(lblDep); panel.add(txtDep);
        panel.add(lblStatut); panel.add(txtStatut);
        panel.add(new JLabel("")); panel.add(btnSubmit);

        dialog.add(panel);
        dialog.setVisible(true);
    }

    private void showEditEnseignantDialog(JTable table) {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner un enseignant.", "Erreur", JOptionPane.WARNING_MESSAGE);
            return;
        }
        String id = (String) table.getValueAt(row, 0);
        Enseignant e = gestionActeurs.getEnseignantById(id);
        if (e == null) return;

        JDialog dialog = new JDialog(this, "Modifier un Enseignant", true);
        dialog.setSize(400, 450);
        dialog.setLocationRelativeTo(this);
        dialog.getContentPane().setBackground(bgDark);

        JPanel panel = new JPanel(new GridLayout(6, 2, 10, 10));
        panel.setBackground(bgDark);
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel lblNom = new JLabel("Nom :"); lblNom.setForeground(textPrimary);
        JTextField txtNom = new JTextField(e.getNom());
        JLabel lblPrenom = new JLabel("Prénom :"); lblPrenom.setForeground(textPrimary);
        JTextField txtPrenom = new JTextField(e.getPrenom());
        JLabel lblEmail = new JLabel("Email :"); lblEmail.setForeground(textPrimary);
        JTextField txtEmail = new JTextField(e.getEmail());
        JLabel lblDep = new JLabel("Département :"); lblDep.setForeground(textPrimary);
        JTextField txtDep = new JTextField(e.getDepartement());
        JLabel lblStatut = new JLabel("Statut (PERMANENT/VACATAIRE) :"); lblStatut.setForeground(textPrimary);
        JTextField txtStatut = new JTextField(e.getStatut().toString());

        JButton btnSubmit = new JButton("Enregistrer");
        btnSubmit.setBackground(accentGold);
        btnSubmit.setForeground(bgDark);
        btnSubmit.setFont(new Font("Segoe UI", Font.BOLD, 14));

        btnSubmit.addActionListener(event -> {
            e.setNom(txtNom.getText());
            e.setPrenom(txtPrenom.getText());
            e.setEmail(txtEmail.getText());
            e.setDepartement(txtDep.getText());
            e.setStatut(Enseignant.Statut.valueOf(txtStatut.getText().toUpperCase()));
            refreshEnseignantsTable();
            dialog.dispose();
        });

        panel.add(lblNom); panel.add(txtNom);
        panel.add(lblPrenom); panel.add(txtPrenom);
        panel.add(lblEmail); panel.add(txtEmail);
        panel.add(lblDep); panel.add(txtDep);
        panel.add(lblStatut); panel.add(txtStatut);
        panel.add(new JLabel("")); panel.add(btnSubmit);

        dialog.add(panel);
        dialog.setVisible(true);
    }

    private void deleteSelectedEnseignant(JTable table) {
        int row = table.getSelectedRow();
        if (row >= 0) {
            String id = (String) table.getValueAt(row, 0);
            Enseignant e = gestionActeurs.getEnseignantById(id);
            if (e != null) {
                int confirm = JOptionPane.showConfirmDialog(this, "Voulez-vous vraiment supprimer cet enseignant ?", "Confirmation", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    gestionActeurs.supprimerPersonne(e);
                    refreshEnseignantsTable();
                    refreshDashboardStats();
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner un enseignant.", "Erreur", JOptionPane.WARNING_MESSAGE);
        }
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

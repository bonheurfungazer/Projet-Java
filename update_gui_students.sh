#!/bin/bash
awk '
/import javax.swing.border.EmptyBorder;/ {
    print $0
    print "import javax.swing.table.DefaultTableModel;"
    print "import java.util.List;"
    print "import java.time.LocalDate;"
    next
}
/private JPanel studentsPanel;/ {
    print $0
    print "    private DefaultTableModel studentsTableModel;"
    next
}
/cardsContainer\.add\(dashboardPanel, "Dashboard"\);/ {
    print $0
    print ""
    print "        // --- Students Panel ---"
    print "        studentsPanel = new JPanel();"
    print "        studentsPanel.setLayout(new BorderLayout(0, 20));"
    print "        studentsPanel.setBackground(bgDark);"
    print "        studentsPanel.setBorder(new EmptyBorder(30, 40, 30, 40));"
    print ""
    print "        JPanel topPanel = new JPanel(new BorderLayout());"
    print "        topPanel.setBackground(bgDark);"
    print "        JLabel title = new JLabel(\"Gestion des Étudiants\");"
    print "        title.setFont(headerFont);"
    print "        title.setForeground(textPrimary);"
    print "        topPanel.add(title, BorderLayout.WEST);"
    print "        JButton btnAddStudent = new JButton(\"+ Ajouter\");"
    print "        btnAddStudent.setBackground(accentGreen);"
    print "        btnAddStudent.setForeground(bgDark);"
    print "        btnAddStudent.setFont(new Font(\"Segoe UI\", Font.BOLD, 14));"
    print "        btnAddStudent.addActionListener(e -> showAddStudentDialog());"
    print "        topPanel.add(btnAddStudent, BorderLayout.EAST);"
    print "        studentsPanel.add(topPanel, BorderLayout.NORTH);"
    print ""
    print "        String[] columns = {\"Matricule\", \"Nom\", \"Prénom\", \"Filière\", \"Année\"};"
    print "        studentsTableModel = new DefaultTableModel(columns, 0);"
    print "        JTable studentsTable = new JTable(studentsTableModel);"
    print "        studentsTable.setBackground(bgPanel);"
    print "        studentsTable.setForeground(textPrimary);"
    print "        studentsTable.setGridColor(new Color(60, 60, 60));"
    print "        studentsTable.setRowHeight(30);"
    print "        studentsTable.getTableHeader().setBackground(new Color(40, 40, 40));"
    print "        studentsTable.getTableHeader().setForeground(accentGold);"
    print "        studentsTable.getTableHeader().setFont(new Font(\"Segoe UI\", Font.BOLD, 14));"
    print "        JScrollPane scrollPane = new JScrollPane(studentsTable);"
    print "        scrollPane.getViewport().setBackground(bgDark);"
    print "        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(50, 50, 50)));"
    print "        studentsPanel.add(scrollPane, BorderLayout.CENTER);"
    print ""
    print "        cardsContainer.add(studentsPanel, \"Students\");"
    print "        refreshStudentsTable();"
    next
}
/btnStudents.addActionListener/ {
    print "        btnStudents.addActionListener(e -> {"
    print "            cardLayout.show(cardsContainer, \"Students\");"
    print "            refreshStudentsTable();"
    print "        });"
    getline
    getline
    next
}
/^    private void refreshDashboardStats/ {
    print "    private void refreshStudentsTable() {"
    print "        studentsTableModel.setRowCount(0);"
    print "        List<Etudiant> etudiants = gestionActeurs.getEtudiants();"
    print "        for (Etudiant e : etudiants) {"
    print "            studentsTableModel.addRow(new Object[]{e.getMatricule(), e.getNom(), e.getPrenom(), e.getFiliere(), e.getAnneeEtude()});"
    print "        }"
    print "    }"
    print ""
    print $0
    next
}
{ print }
' src/com/campusconnect/ui/DashboardGUI.java > temp.java && mv temp.java src/com/campusconnect/ui/DashboardGUI.java

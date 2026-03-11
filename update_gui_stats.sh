#!/bin/bash
awk '
/cardsContainer\.add\(dashboardPanel, "Dashboard"\);/ {
    print "        refreshDashboardStats();"
    print $0
    next
}
/^    private JPanel createCard/ {
    print "    private void refreshDashboardStats() {"
    print "        lblTotalStudents.setText(String.valueOf(gestionActeurs.getEtudiants().size()));"
    print "        lblTotalTeachers.setText(String.valueOf(gestionActeurs.getEnseignants().size()));"
    print "        lblActiveCourses.setText(String.valueOf(gestionFormation.getCours().size()));"
    print "    }"
    print ""
    print $0
    next
}
{ print }
' src/com/campusconnect/ui/DashboardGUI.java > temp.java && mv temp.java src/com/campusconnect/ui/DashboardGUI.java

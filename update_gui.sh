#!/bin/bash
awk '
/add\(sidebar, BorderLayout.WEST\);/ {
    print "        sidebar.add(Box.createVerticalGlue());"
    print "        JLabel author = new JLabel(\"<html><br><b>Réalisé par:</b><br>Guepi takouo peguy maeva</html>\");"
    print "        author.setFont(new Font(\"SansSerif\", Font.PLAIN, 12));"
    print "        author.setForeground(Color.GRAY);"
    print "        author.setAlignmentX(Component.LEFT_ALIGNMENT);"
    print "        sidebar.add(author);"
}
{ print }
' src/com/campusconnect/ui/DashboardGUI.java > temp.java && mv temp.java src/com/campusconnect/ui/DashboardGUI.java

#!/bin/bash
awk '
/boolean continuer = true;/ {
    print "        boolean continuer = true;"
    print "        clearScreen();"
    next
}
/String choix = scanner.nextLine();/ {
    print "            String choix = scanner.nextLine();"
    print "            clearScreen();"
    next
}
/System.out.println\("Choix invalide."\);/ {
    print "                        System.out.println(\"Choix invalide.\");"
    print "                }"
    print "                if (continuer) {"
    print "                    System.out.println(\"\\nAppuyez sur Entrée pour continuer...\");"
    print "                    scanner.nextLine();"
    print "                    clearScreen();"
    print "                }"
    next
}
/^    private static void menuInscription/ {
    print "    private static void clearScreen() {"
    print "        System.out.print(\"\\033[H\\033[2J\");"
    print "        System.out.flush();"
    print "    }"
    print ""
    print $0
    next
}
{ print }
' src/com/campusconnect/ui/CampusApp.java > temp.java && mv temp.java src/com/campusconnect/ui/CampusApp.java

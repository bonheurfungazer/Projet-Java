package com.campusconnect.benchmarks;

import com.campusconnect.models.Cours;
import com.campusconnect.models.Salle;
import com.campusconnect.services.GestionFormation;
import java.util.ArrayList;
import java.util.List;

public class BenchmarkGestionFormation {
    public static void main(String[] args) {
        int numElements = 10000;
        if (args.length > 0) {
            numElements = Integer.parseInt(args[0]);
        }

        System.out.println("Running benchmark with " + numElements + " elements...");

        GestionFormation gf = new GestionFormation();
        List<Cours> testCours = new ArrayList<>();
        List<Salle> testSalles = new ArrayList<>();

        for (int i = 0; i < numElements; i++) {
            testCours.add(new Cours("C" + i, "Cours " + i, "Desc", 30, null));
            testSalles.add(new Salle("S" + i, 30, Salle.TypeSalle.COURS_CLASSIQUE));
        }

        // Measure Cours addition
        long start = System.nanoTime();
        for (Cours c : testCours) {
            gf.ajouterCours(c);
        }
        long end = System.nanoTime();
        double coursTime = (end - start) / 1e6;
        System.out.printf("Time to add %d unique Cours: %.2f ms\n", numElements, coursTime);

        // Measure duplicate Cours addition
        start = System.nanoTime();
        for (Cours c : testCours) {
            gf.ajouterCours(c);
        }
        end = System.nanoTime();
        double dupCoursTime = (end - start) / 1e6;
        System.out.printf("Time to check %d duplicate Cours: %.2f ms\n", numElements, dupCoursTime);

        // Functional check
        if (gf.getCours().size() != numElements) {
            System.err.println("ERROR: Expected " + numElements + " courses, but got " + gf.getCours().size());
        } else {
            System.out.println("Functional check passed: No duplicates in Cours.");
        }

        // Measure Salle addition
        start = System.nanoTime();
        for (Salle s : testSalles) {
            gf.ajouterSalle(s);
        }
        end = System.nanoTime();
        double salleTime = (end - start) / 1e6;
        System.out.printf("Time to add %d unique Salles: %.2f ms\n", numElements, salleTime);

        // Measure duplicate Salle addition
        start = System.nanoTime();
        for (Salle s : testSalles) {
            gf.ajouterSalle(s);
        }
        end = System.nanoTime();
        double dupSalleTime = (end - start) / 1e6;
        System.out.printf("Time to check %d duplicate Salles: %.2f ms\n", numElements, dupSalleTime);

        // Functional check
        if (gf.getSalles().size() != numElements) {
            System.err.println("ERROR: Expected " + numElements + " salles, but got " + gf.getSalles().size());
        } else {
            System.out.println("Functional check passed: No duplicates in Salles.");
        }
    }
}

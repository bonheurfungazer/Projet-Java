# CampusConnect

**CampusConnect** est une application console et graphique de système de gestion universitaire développée en Java dans le cadre d'un projet de Programmation Orientée Objet (POO). Elle centralise la gestion des acteurs (étudiants, enseignants), de l'offre de formation (cours, groupes, salles), et du suivi académique (inscriptions, notes, emploi du temps).

## Prérequis et Dépendances

Pour compiler et exécuter ce projet, vous avez besoin de :

- **Java Development Kit (JDK) 17 ou supérieur.**
  Vous pouvez vérifier votre version de Java en exécutant la commande suivante dans votre terminal :
  ```bash
  java -version
  javac -version
  ```
- Aucun framework externe ou bibliothèque tierce n'est requis (le projet utilise uniquement la bibliothèque standard de Java et Swing pour l'interface).

## Instructions de Compilation et d'Exécution

Ouvrez un terminal (invite de commandes) et placez-vous dans le dossier **racine** du projet (celui qui contient le fichier `README.md` et le dossier `src`).

### 1. Compilation

Compilez l'ensemble des fichiers source Java avec la commande suivante :

```bash
javac -cp src src/com/campusconnect/models/*.java src/com/campusconnect/exceptions/*.java src/com/campusconnect/services/*.java src/com/campusconnect/ui/*.java
```

*(Si vous êtes sous Windows et que la commande précédente pose problème à cause des espaces ou des jokers `*.java`, vous pouvez utiliser cette commande alternative avec PowerShell)* :
```powershell
Get-ChildItem -Recurse src\*.java | ForEach-Object { javac -cp src $_.FullName }
```

### 2. Exécution

Une fois la compilation réussie (aucun message d'erreur ne devrait s'afficher), vous pouvez lancer le projet de deux manières différentes :

**Lancement de l'Interface Graphique (GUI) - Recommandé :**
Exécutez la commande suivante depuis la racine du projet pour ouvrir le tableau de bord administrateur (inspiré de notre design) :
```bash
java -cp src com.campusconnect.ui.Main
```

**Lancement de l'Interface Console :**
Si vous préférez utiliser l'application via les menus dans le terminal, exécutez cette commande (qui passera l'argument "console") :
```bash
java -cp src com.campusconnect.ui.Main console
```

## Fonctionnalités Principales

1. **Affichage des acteurs :** Lister tous les étudiants et enseignants enregistrés.
2. **Inscription :** Inscrire un étudiant à un groupe spécifique d'un cours (avec vérification de la capacité).
3. **Saisie de notes :** Saisir une note et son coefficient pour un étudiant dans un cours donné.
4. **Relevé de notes :** Générer le relevé avec calcul de la moyenne générale d'un étudiant.
5. **Planification de séance :** Assigner une salle, un enseignant et un horaire pour un cours (avec détection stricte des conflits horaires, de salle et de capacité).
6. **Interface Graphique (Dashboard) :** Une vue d'ensemble claire des statistiques (étudiants, enseignants, cours actifs) et des activités récentes de l'université.

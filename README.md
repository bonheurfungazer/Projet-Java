# 🎓 CampusConnect

![Java Version](https://img.shields.io/badge/Java-17%2B-blue)
![Architecture](https://img.shields.io/badge/Architecture-POO-green)
![UI](https://img.shields.io/badge/Interface-Swing%20%2F%20Console-orange)

**CampusConnect** est une application logicielle de gestion universitaire. Elle centralise et automatise la gestion des acteurs de l'université (étudiants, enseignants), de l'offre de formation (cours, groupes, salles), et du suivi académique (inscriptions, notes, emplois du temps avec détection des conflits).

---
**👨‍🎓 Projet Réalisé par : Guepi Takouo Peguy Maeva**
---

## 🚀 Fonctionnalités Principales

- 👥 **Gestion des Acteurs :** Lister tous les étudiants inscrits et les enseignants enregistrés.
- 📝 **Inscriptions Intelligentes :** Inscrire un étudiant à un groupe de TD/CM avec vérification automatique de la capacité maximale des salles.
- 📊 **Suivi Académique :** Saisir des notes avec coefficients et générer instantanément un relevé de notes global.
- 📅 **Planification Optimisée :** Assigner une salle, un enseignant et un horaire pour un cours. Le système intègre un algorithme robuste pour empêcher tout conflit horaire, de salle, ou d'enseignant.
- 💻 **Interfaces Flexibles :**
  - **Tableau de Bord Administrateur (GUI)** : Interface graphique moderne développée avec Java Swing offrant une vue d'ensemble statistiques et des activités de l'université.
  - **Console Interactive (CLI)** : Une option en ligne de commande légère et performante.

## 🛠️ Prérequis et Dépendances

Pour compiler et exécuter ce projet sur votre machine locale, vous avez besoin de :

- **Java Development Kit (JDK) 17** ou supérieur.
  Vérifiez votre installation via votre terminal :
  ```bash
  java -version
  javac -version
  ```
- *Zéro dépendance tierce :* L'application repose intégralement sur les librairies standards de Java (AWT/Swing, java.time, Collections).

## ⚙️ Instructions de Compilation et d'Exécution

Ouvrez un terminal et placez-vous dans le dossier **racine** du projet (celui contenant le dossier `src` et le fichier `README.md`).

### 1. Compilation

Compilez l'ensemble du projet avec la commande suivante :

```bash
javac -cp src src/com/campusconnect/models/*.java src/com/campusconnect/exceptions/*.java src/com/campusconnect/services/*.java src/com/campusconnect/ui/*.java
```

*(Sous Windows avec PowerShell, utilisez plutôt cette commande si la précédente échoue)* :
```powershell
Get-ChildItem -Recurse src\*.java | ForEach-Object { javac -cp src $_.FullName }
```

### 2. Démarrage du Projet

Le système CampusConnect peut être lancé de deux manières, selon vos préférences.

#### Option A : Lancement de l'Interface Graphique (Dashboard) 🌟 Recommandé
Exécutez la commande suivante depuis la racine du projet pour ouvrir le tableau de bord visuel :
```bash
java -cp src com.campusconnect.ui.Main
```

#### Option B : Lancement de l'Interface Console (CLI)
Si vous préférez interagir via les menus du terminal, ajoutez l'argument `console` :
```bash
java -cp src com.campusconnect.ui.Main console
```

---
*Projet développé dans le cadre d'une évaluation de Programmation Orientée Objet (POO).*

# LinePermission → LogAnalyzer → AuditDB (SHELL)

![Java](https://img.shields.io/badge/Java-17%2B-blue)
![SQLite](https://img.shields.io/badge/SQLite-3.x-lightblue)
![HikariCP](https://img.shields.io/badge/HikariCP-5.1.0-orange)
![BCrypt](https://img.shields.io/badge/BCrypt-0.4-red)
![Licence](https://img.shields.io/badge/Licence-Pédagogique-green)

Application console Java qui évolue en trois étapes :

| Brief | Nom | Contenu |
| :---: | :--- | :--- |
| **1** | **LinePermission** | Mini-shell : comptes, session, fichiers, permissions |
| **2** | **LogAnalyzer** | Analyse du journal d'accès avec les Streams Java |
| **3** | **AuditDB** | Migration vers une base de données relationnelle (SQLite + JDBC) |

---

## 📚 Contexte

### Brief 1 — LinePermission

Un mini-shell qui reproduit, en version simplifiée, le système de permissions de fichiers de Linux.

- **Deux catégories** : Propriétaire / Autres (jamais de cumul).
- **Trois droits** : `r` (lire), `w` (modifier), `d` (supprimer).
- **Format d'affichage** : `rwd|r-- proprietaire nomFichier`.

### Brief 2 — LogAnalyzer

Chaque action (lecture, écriture, changement de droits, refus) laisse une trace dans un journal. **LogAnalyzer** transforme ces lignes brutes en statistiques claires grâce aux **Streams Java** (filter, map, groupingBy, etc.).

### Brief 3 — AuditDB

Les données ne vivent plus en mémoire ni dans des fichiers texte, mais dans une **base de données relationnelle** (SQLite). Cela résout :

- ❌ `OutOfMemoryError` quand les données deviennent volumineuses
- ❌ La lenteur de relecture des fichiers
- ❌ L'impossibilité de partager les données entre sessions

L'architecture passe du stockage fichier au **pattern DAO** avec **JDBC**, en respectant les **4 piliers de la POO**.

---

## ⚙️ Fonctionnalités

### 🔐 Comptes et session (Brief 1)

| Commande | Effet |
| :--- | :--- |
| `signup` | Créer un compte (login + mot de passe hashé avec BCrypt) |
| `login` | Se connecter avec ses identifiants |
| `logout` | Fermer la session en cours |
| `help` | Afficher les commandes disponibles |
| `exit` | Quitter l'application |

### 📁 Fichiers et permissions (Brief 1)

| Commande | Droit requis | Effet |
| :--- | :--- | :--- |
| `ls` | Aucun | Lister tous les fichiers avec leurs droits |
| `touch <f>` | Aucun | Créer un fichier vide en `rwd|---` |
| `cat <f>` | `r` | Afficher le contenu du fichier |
| `nano <f>` | `w` | Éditer le contenu (saisie multi-ligne jusqu'à `EOF`) |
| `chmod <r\|w\|d> <f>` | Propriétaire | Donner un droit aux autres |
| `chmod -<r\|w\|d> <f>` | Propriétaire | Retirer un droit aux autres |

### 📊 Journal d'accès et audit (Brief 2 + 3)

| Choix | Analyse | Requête SQL |
| :---: | :--- | :--- |
| 1 | Nombre total d'actions | `SELECT COUNT(*) FROM logs` |
| 2 | Nombre d'accès refusés | `WHERE resultat = 'REFUSE'` |
| 3 | Utilisateurs distincts | `SELECT DISTINCT login ... JOIN` |
| 4 | Actions par utilisateur | `GROUP BY login` |
| 5 | Top 3 des fichiers consultés | `ORDER BY n DESC LIMIT 3` |
| 6 | Accès refusés d'un utilisateur | `WHERE login = ? AND resultat = 'REFUSE'` |
| 7 | Utilisateur le plus actif | `ORDER BY n DESC LIMIT 1` |
| 8 | Répartition des actions par type | `GROUP BY action` |

---

## 🏗️ Architecture

### Structure des packages

```text
src/main/java/ma/youcode/lineperm/
├── Main.java                     # Point d'entrée (ferme le pool HikariCP)
├── db/
│   └── DBConnection.java         # Singleton HikariCP
├── dao/                          # Data Access Object
│   ├── Dao.java                  # Interface générique
│   ├── AbstractDao.java          # Code commun (getConnection)
│   ├── UserDao.java              # CRUD utilisateurs
│   ├── FichierDao.java           # CRUD fichiers
│   └── LogDao.java               # Insert + 8 analyses SQL
├── model/                        # Entités métier
│   ├── User.java                 # id, login, passwordHash
│   ├── FichierProtege.java       # id, nom, proprietaireId, droits, contenu
│   └── AccessLog.java            # id, utilisateurId, fichierId, action, resultat, date
├── access/
│   └── ControleAcces.java        # Règle des catégories (statique)
├── service/                      # Logique métier
│   ├── UserService.java          # signup, login
│   ├── FileService.java          # touch, chmod, peutLire, peutEcrire
│   └── LogService.java           # analyses (délègue au DAO)
└── ui/
    └── ConsoleApp.java           # Boucle, prompt, switch, gardes

## Test
 java -jar LinePermission.jar

## Compile
javac -d target/classes -cp "lib/*" $(find src/main/java -name "*.java")

## Build JAR
jar cfm LinePermission.jar manifest.txt -C target/classes .
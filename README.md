# LinePermission & LogAnalyzer (SHELL)

![Java](https://img.shields.io/badge/Java-17%2B-blue)
![BCrypt](https://img.shields.io/badge/BCrypt-0.4-orange)
![Streams](https://img.shields.io/badge/Java%20Streams-API-purple)
![Licence](https://img.shields.io/badge/Licence-Pédagogique-green)

Une application console Java qui reproduit un mini-shell de gestion de permissions de fichiers (inspiré de Linux), et qui analyse son propre journal d'accès avec les **Streams Java**.

Le projet se construit en deux briefs :

| Brief | Contenu | Livrable |
| :--- | :--- | :--- |
| **1** | Comptes, session, fichiers et permissions | Application console complète |
| **2** | Analyse du journal d'accès (LogAnalyzer) | Extension : menu de statistiques |

---

## 📚 Contexte

### Brief 1 — LinePerm

Sur un vrai système Linux, chaque fichier appartient à un propriétaire et possède des droits définis pour deux catégories :

- **Le propriétaire** : celui qui a créé le fichier.
- **Les autres** : tous les autres utilisateurs (pas de notion de groupe).

Les droits disponibles sont :
- `r` — lire le contenu
- `w` — modifier le contenu
- `d` — supprimer le fichier

**Règle d'or** : Une seule catégorie s'applique. Si vous êtes le propriétaire, seuls les droits de gauche comptent. Sinon, seuls ceux de droite.

### Brief 2 — LogAnalyzer

Chaque action (lecture, écriture, changement de droits, accès refusé) laisse une trace dans un fichier `access.log`. Un fichier de milliers de lignes est illisible pour un humain. **LogAnalyzer** transforme ces lignes brutes en statistiques claires :

- Combien d'accès refusés ?
- Quel utilisateur est le plus actif ?
- Quels fichiers sont les plus consultés ?

---

## ⚙️ Fonctionnalités

### 🔐 Brief 1 — Comptes et session

| Commande | Effet |
| :--- | :--- |
| `signup` | Créer un compte (login + mot de passe hashé avec BCrypt). |
| `login` | Se connecter avec ses identifiants. |
| `logout` | Fermer la session en cours. |
| `exit` | Quitter l'application. |
| `help` | Afficher les commandes disponibles. |

### 📁 Brief 1 — Fichiers et permissions

| Commande | Droit requis | Effet |
| :--- | :--- | :--- |
| `ls` | Aucun | Liste tous les fichiers avec leurs droits. |
| `touch <f>` | Aucun | Crée un fichier vide en `rwd|---`, dont le créateur est propriétaire. |
| `cat <f>` | `r` | Affiche le contenu du fichier. |
| `nano <f>` | `w` | Saisie multi-ligne qui remplace le contenu, jusqu'à `EOF`. |
| `chmod <r\|w\|d> <f>` | Propriétaire | Donne un droit aux autres. |
| `chmod -<r\|w\|d> <f>` | Propriétaire | Retire un droit aux autres. |

### 📊 Brief 2 — LogAnalyzer
| `stats` | Propriétaire | avais l'accée au Logs
| Choix | Analyse | Outils Stream |
| :---: | :--- | :--- |
| 1 | Nombre total d'actions | `count` |
| 2 | Nombre d'accès refusés | `filter` + `count` |
| 3 | Utilisateurs distincts | `map` + `distinct` |
| 4 | Actions par utilisateur | `groupingBy` + `counting` |
| 5 | Top 3 des fichiers consultés | `groupingBy` + `sorted` + `limit` |
| 6 | Accès refusés d'un utilisateur | `filter` + saisie |
| 7 | Utilisateur le plus actif | `groupingBy` + `max` + `Optional` |
| 8 | Répartition des actions par type | `groupingBy` |

---

## 🏗️ Architecture

L'application suit une architecture en couches pour garantir la séparation des responsabilités.

```text
src/main/java/ma/youcode/lineperm/
├── Main.java                     # Point d'entrée
├── model/                        # Entités métier (données pures)
│   ├── User.java                 # Login + Hash BCrypt
│   ├── FichierProtege.java       # Nom, propriétaire, 6 booléens (r,w,d × proprio/autres)
│   └── AccessLog.java            # Une ligne de log (6 champs)
├── access/                       # Règle des catégories
│   └── ControleAcces.java        # Méthodes statiques, aucun affichage
├── service/                      # Logique métier + persistance
│   ├── UserService.java          # Comptes (Map, BCrypt, users.txt)
│   ├── FileService.java          # Fichiers (Map, files.txt, data/)
│   └── LogService.java           # Analyse des logs (Streams, access.log)
└── ui/                           # Interface utilisateur
    └── ConsoleApp.java           # Boucle, prompt, switch, gardes, menu stats
# LinePermission (SHELL)

![Java](https://img.shields.io/badge/Java-17%2B-blue)
![BCrypt](https://img.shields.io/badge/BCrypt-0.4-orange)

**LinePermission** est une application console Java qui reproduit, en version simplifiée, le système de permissions de fichiers de Linux.

Le projet se construit en deux parties :
1. **Comptes et session** : authentification sécurisée avec persistance des comptes.
2. **Fichiers et permissions** : gestion des fichiers, droits de lecture/écriture/suppression et partage (`chmod`).

---

## 📚 Contexte

Sur un vrai système Linux, chaque fichier appartient à un propriétaire et possède des droits définis pour deux catégories :
- **Le propriétaire** : celui qui a créé le fichier.
- **Les autres** : tous les autres utilisateurs (pas de notion de groupe).

Les droits disponibles sont :
- `r` — lire le contenu
- `w` — modifier le contenu
- `d` — supprimer le fichier

**Règle d'or** : Une seule catégorie s'applique. Si vous êtes le propriétaire, seuls les droits de gauche comptent. Sinon, seuls ceux de droite.

---

## ⚙️ Fonctionnalités

### Partie 1 (implémentée)
| Commande | Effet |
| :--- | :--- |
| `signup` | Créer un compte (login + mot de passe hashé avec BCrypt). |
| `login` | Se connecter avec ses identifiants. |
| `logout` | Fermer la session en cours. |
| `exit` | Quitter l'application. |
| `help` | Afficher les commandes disponibles. |

### Partie 2 (à venir / extension)
| Commande | Effet |
| :--- | :--- |
| `touch` | Créer un nouveau fichier. |
| `ls` | Lister les fichiers avec leurs permissions. |
| `cat` | Lire le contenu d’un fichier (si droit `r`). |
| `nano` | Modifier le contenu (si droit `w`). |
| `chmod` | Modifier les droits des « autres » (si propriétaire). |

---

## 🏗️ Architecture (Conception)

L'application suit une architecture en couches (MVC simplifié) pour garantir la séparation des responsabilités.

```text
src/
└── ma/
    └── youcode/
        └── lineperm/
            ├── Main.java                # Point d'entrée
            ├── model/                   # Entités métier
            │   ├── User.java            # Login + Hash
            │   └── Fichier.java         # Nom, contenu, droits, propriétaire
            ├── service/                 # Logique métier & persistance
            │   ├── UserService.java     # Gestion des comptes (Map, BCrypt)
            │   └── FileService.java     # Gestion des fichiers & permissions
            └── ui/                      # Interface utilisateur
                └── ConsoleApp.java      # Boucle de lecture, prompt, switch
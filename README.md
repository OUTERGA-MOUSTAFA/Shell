# LinePermission (SHELL)

![Java](https://img.shields.io/badge/Java-17%2B-blue)
![BCrypt](https://img.shields.io/badge/BCrypt-0.4-orange)
![Licence](https://img.shields.io/badge/Licence-Pédagogique-green)

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

### 🔐 Partie 1 — Comptes et session

| Commande | Effet |
| :--- | :--- |
| `signup` | Créer un compte (login + mot de passe hashé avec BCrypt). |
| `login` | Se connecter avec ses identifiants. |
| `logout` | Fermer la session en cours. |
| `exit` | Quitter l'application. |
| `help` | Afficher les commandes disponibles. |

### 📁 Partie 2 — Fichiers et permissions

| Commande | Droit requis | Effet |
| :--- | :--- | :--- |
| `ls` | Aucun | Liste tous les fichiers avec leurs droits. |
| `touch <f>` | Aucun | Crée un fichier vide en `rwd|---`, dont le créateur est propriétaire. |
| `cat <f>` | `r` | Affiche le contenu du fichier. |
| `nano <f>` | `w` | Saisie multi-ligne qui remplace le contenu, jusqu'à une ligne `EOF`. |
| `chmod <r\|w\|d> <f>` | Propriétaire | Donne un droit aux autres. |
| `chmod -<r\|w\|d> <f>` | Propriétaire | Retire un droit aux autres. |

---

## 🏗️ Architecture

L'application suit une architecture en couches pour garantir la séparation des responsabilités.

```text
src/main/java/ma/youcode/lineperm/
├── Main.java                     # Point d'entrée
├── model/                        # Entités métier
│   ├── User.java                 # Login + Hash BCrypt
│   └── FichierProtege.java       # Nom, propriétaire, 6 booléens (r,w,d × proprio/autres)
├── access/                       # Règle des catégories
│   └── ControleAcces.java        # Méthodes statiques, aucune I/O, aucun affichage
├── service/                      # Logique métier + persistance
│   ├── UserService.java          # Comptes (Map, BCrypt, users.txt)
│   └── FileService.java          # Fichiers (Map, files.txt, data/)
└── ui/                           # Interface utilisateur
    └── ConsoleApp.java           # Boucle, prompt, switch, gardes
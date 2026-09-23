## 🗄️ Brief 3 — AuditDB (JDBC + SQLite)

### Architecture DAO

- `Dao<T>` : interface (contrat)
- `AbstractDao<T>` : code commun
- `UserDao`, `FichierDao`, `LogDao` : implémentations concrètes
- `DBConnection` : Singleton (une seule connexion SQLite)

### Base de données SQLite

Fichier `auditdb.db` créé automatiquement à la racine.

3 tables : `users`, `fichiers`, `logs` (avec clés étrangères et index).

### Compilation

```bash
find src/main/java -name "*.java" > sources.txt
javac -d target/classes -cp "lib/jbcrypt-0.4.jar:lib/sqlite-jdbc-3.46.1.0.jar" @sources.txt
rm sources.txt
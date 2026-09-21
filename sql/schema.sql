
-- AuditDB — Schéma de la base
PRAGMA foreign_keys = ON; -- ikhdem on delete cascad
CREATE TABLE IF NOT EXISTS users (
    id            INTEGER PRIMARY KEY AUTOINCREMENT,
    login         TEXT UNIQUE NOT NULL,
    password_hash TEXT NOT NULL
);

CREATE TABLE IF NOT EXISTS fichiers (
    id              INTEGER PRIMARY KEY AUTOINCREMENT,
    nom             TEXT NOT NULL,
    proprietaire_id INTEGER NOT NULL,
    droits_proprio  TEXT NOT NULL DEFAULT 'rwd',
    droits_autres   TEXT NOT NULL DEFAULT '---',
    FOREIGN KEY (proprietaire_id) REFERENCES users(id)
);


CREATE TABLE IF NOT EXISTS logs (
    id             INTEGER PRIMARY KEY AUTOINCREMENT,
    utilisateur_id INTEGER NOT NULL,
    fichier_id     INTEGER NOT NULL,
    action         TEXT NOT NULL,
    resultat       TEXT NOT NULL,
    date_action    TEXT NOT NULL DEFAULT (datetime('now')),
    FOREIGN KEY (utilisateur_id) REFERENCES users(id),
    FOREIGN KEY (fichier_id)     REFERENCES fichiers(id)
);

-- Index pour accélérer les analyses
-- CREATE INDEX idx_logs_user   ON logs(utilisateur_id);
-- CREATE INDEX idx_logs_action ON logs(action);
-- CREATE INDEX idx_logs_result ON logs(resultat);
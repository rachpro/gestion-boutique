-- =====================================================================
-- BASE DE DONNÉES : gestion_boutique
-- SGBD : PostgreSQL 16
-- Application : Gestion Boutique (Java EE)
-- Auteur : RACHID KONE 
-- Date : Octobre 2026
-- =====================================================================

-- =====================================================================
-- TABLE utilisateur
-- =====================================================================
CREATE TABLE utilisateur (
    id              BIGSERIAL PRIMARY KEY,
    nom             VARCHAR(80)  NOT NULL,
    prenom          VARCHAR(80)  NOT NULL,
    matricule       VARCHAR(50)  NOT NULL UNIQUE,
    sexe            VARCHAR(10)  NOT NULL,
    date_naissance  DATE         NOT NULL,
    identifiant     VARCHAR(80)  NOT NULL UNIQUE,
    mot_de_passe    VARCHAR(255) NOT NULL,
    role            VARCHAR(20)  NOT NULL DEFAULT 'VENDEUR',
    actif           BOOLEAN      NOT NULL DEFAULT TRUE,
    date_creation   TIMESTAMP    NOT NULL DEFAULT NOW()
);
CREATE INDEX idx_utilisateur_identifiant ON utilisateur(identifiant);
CREATE INDEX idx_utilisateur_matricule   ON utilisateur(matricule);

-- =====================================================================
-- TABLE produit
-- =====================================================================
CREATE TABLE produit (
    id          BIGSERIAL PRIMARY KEY,
    nom         VARCHAR(150)   NOT NULL,
    prix_achat  NUMERIC(12, 2) NOT NULL,
    prix_vente  NUMERIC(12, 2) NOT NULL,
    stock       INTEGER        NOT NULL DEFAULT 0
);

-- =====================================================================
-- TABLE client
-- =====================================================================
CREATE TABLE client (
    id              BIGSERIAL PRIMARY KEY,
    nom             VARCHAR(80)  NOT NULL,
    prenom          VARCHAR(80)  NOT NULL,
    telephone       VARCHAR(20)  NOT NULL,
    email           VARCHAR(120),
    adresse         VARCHAR(255),
    actif           BOOLEAN      NOT NULL DEFAULT TRUE,
    date_creation   TIMESTAMP    NOT NULL DEFAULT NOW()
);
CREATE INDEX idx_client_nom       ON client(nom);
CREATE INDEX idx_client_telephone ON client(telephone);

-- =====================================================================
-- TABLE grossiste
-- =====================================================================
CREATE TABLE grossiste (
    id              BIGSERIAL PRIMARY KEY,
    nom             VARCHAR(150) NOT NULL,
    telephone       VARCHAR(20)  NOT NULL,
    email           VARCHAR(120),
    adresse         VARCHAR(255),
    contact_nom     VARCHAR(80),
    actif           BOOLEAN      NOT NULL DEFAULT TRUE,
    date_creation   TIMESTAMP    NOT NULL DEFAULT NOW()
);
CREATE INDEX idx_grossiste_nom ON grossiste(nom);

-- =====================================================================
-- TABLE achat
-- =====================================================================
CREATE TABLE achat (
    id              BIGSERIAL PRIMARY KEY,
    client_id       BIGINT         NOT NULL REFERENCES client(id),
    utilisateur_id  BIGINT         NOT NULL REFERENCES utilisateur(id),
    date_achat      TIMESTAMP      NOT NULL DEFAULT NOW(),
    total           NUMERIC(12, 2) NOT NULL DEFAULT 0,
    statut          VARCHAR(20)    NOT NULL DEFAULT 'PAYE'
);
CREATE INDEX idx_achat_client ON achat(client_id);
CREATE INDEX idx_achat_date   ON achat(date_achat DESC);

-- =====================================================================
-- TABLE ligne_achat
-- =====================================================================
CREATE TABLE ligne_achat (
    id              BIGSERIAL PRIMARY KEY,
    achat_id        BIGINT         NOT NULL REFERENCES achat(id) ON DELETE CASCADE,
    produit_id      BIGINT         NOT NULL REFERENCES produit(id),
    quantite        INTEGER        NOT NULL CHECK (quantite > 0),
    prix_unitaire   NUMERIC(12, 2) NOT NULL,
    sous_total      NUMERIC(12, 2) NOT NULL
);
CREATE INDEX idx_ligne_achat_achat ON ligne_achat(achat_id);

-- =====================================================================
-- TABLE livraison
-- =====================================================================
CREATE TABLE livraison (
    id              BIGSERIAL PRIMARY KEY,
    grossiste_id    BIGINT         NOT NULL REFERENCES grossiste(id),
    utilisateur_id  BIGINT         NOT NULL REFERENCES utilisateur(id),
    date_livraison  TIMESTAMP      NOT NULL DEFAULT NOW(),
    total           NUMERIC(12, 2) NOT NULL DEFAULT 0,
    reference       VARCHAR(50)
);
CREATE INDEX idx_livraison_grossiste ON livraison(grossiste_id);
CREATE INDEX idx_livraison_date      ON livraison(date_livraison DESC);

-- =====================================================================
-- TABLE ligne_livraison
-- =====================================================================
CREATE TABLE ligne_livraison (
    id              BIGSERIAL PRIMARY KEY,
    livraison_id    BIGINT         NOT NULL REFERENCES livraison(id) ON DELETE CASCADE,
    produit_id      BIGINT         NOT NULL REFERENCES produit(id),
    quantite        INTEGER        NOT NULL CHECK (quantite > 0),
    prix_unitaire   NUMERIC(12, 2) NOT NULL,
    sous_total      NUMERIC(12, 2) NOT NULL
);
CREATE INDEX idx_ligne_livraison_liv ON ligne_livraison(livraison_id);

-- =====================================================================
-- DONNÉES INITIALES : utilisateur admin
-- Mot de passe : admin123 (hash BCrypt)
-- =====================================================================
INSERT INTO utilisateur
    (nom, prenom, matricule, sexe, date_naissance,
     identifiant, mot_de_passe, role, actif)
VALUES
    ('Admin', 'Super', 'ADM001', 'M', '1990-01-01',
     'admin',
     '$2a$12$H3eukO5s9CcvNTisgfX7GekFzYsTzVAObpfp.8X773cd7SseKdxwq',
     'ADMIN', TRUE);
# RAPPORT DE PROJET — GESTION BOUTIQUE

**Candidat** : KONE RACHID
**Date** : 10 Octobre 2026
**Poste visé** : Développeur Full Stack

---

## 1. Présentation générale

**Application** : Gestion Boutique
**Objectif** : Application complète de gestion pour une boutique de produits alimentaires.

**Technologies utilisées** :
- **Langage** : Java 17
- **Plateforme** : Jakarta EE 10 (Servlets, JSP, JSTL)
- **Base de données** : PostgreSQL 16
- **Pool de connexions** : HikariCP
- **Sécurité** : BCrypt (hashage des mots de passe)
- **Génération PDF** : iText 8
- **Build** : Maven
- **Serveur d'application** : Apache Tomcat 10.1

**Architecture** : MVC (Modèle - Vue - Contrôleur) en couches.

---

## 2. Liste des fonctionnalités réalisées

### 2.1 Authentification et sécurité
- ✅ Connexion / Déconnexion avec session HTTP
- ✅ Hashage des mots de passe avec BCrypt (cost 12)
- ✅ Filtre d'authentification (`AuthFilter`) protégeant les URLs sensibles
- ✅ Filtre d'administration (`AdminFilter`) restreignant `/utilisateurs/*` au rôle ADMIN
- ✅ Gestion des rôles : **ADMIN** et **VENDEUR**

### 2.2 Gestion des utilisateurs
- ✅ Ajout d'utilisateur (Nom, Prénom, Matricule, Sexe, Date de naissance, Identifiant, Mot de passe, Rôle)
- ✅ Liste complète avec badges de rôle et statut
- ✅ Modification (rôle, mot de passe, statut)
- ✅ Activation / Désactivation (avec protection du dernier admin actif)
- ✅ Validation métier centralisée

### 2.3 Gestion des produits
- ✅ Ajout d'un produit (Nom, Prix d'achat, Prix de vente, Stock)
- ✅ Recherche par nom (insensible à la casse via `ILIKE`)
- ✅ Utilisation de `BigDecimal` pour les prix (précision garantie)

### 2.4 Gestion des clients
- ✅ Ajout / Modification / Activation / Désactivation
- ✅ Recherche par nom ou prénom
- ✅ Validation email et téléphone

### 2.5 Gestion des grossistes
- ✅ Ajout / Modification / Activation / Désactivation
- ✅ Recherche par nom
- ✅ Champs spécifiques (contact, référence fournisseur)

### 2.6 Achats (ventes clients)
- ✅ Enregistrement d'un achat multi-produits
- ✅ Calcul automatique du total
- ✅ **Décrémentation du stock** avec vérification de disponibilité
- ✅ **Transaction atomique** (rollback en cas d'erreur)
- ✅ Historique avec **filtre par date**
- ✅ Détail complet d'un achat

### 2.7 Livraisons (approvisionnements)
- ✅ Enregistrement d'une livraison multi-produits depuis un grossiste
- ✅ **Augmentation automatique du stock**
- ✅ Référence de bon de livraison (BL)
- ✅ Historique avec **filtre par date**
- ✅ Détail complet

### 2.8 Impression du ticket de caisse
- ✅ Génération d'un **PDF** avec **iText 8**
- ✅ Mise en page A4 élégante
- ✅ Accès depuis la liste des achats ou le détail

### 2.9 Tableau de bord
- ✅ Cartes de raccourcis vers les modules
- ✅ Affichage du nom et du rôle de l'utilisateur connecté

---

## 3. Fonctionnalités demandées non réalisées

Toutes les fonctionnalités explicitement demandées dans le cahier des charges ont été implémentées.

Améliorations possibles (non demandées, hors périmètre temps) :
- Annulation d'un achat (avec recrédit du stock)
- Envoi de ticket par email
- Export Excel des historiques
- Statistiques graphiques

---

## 4. Architecture de réalisation

### 4.1 Architecture MVC en couches

- **Modèle** : `Utilisateur`, `Produit`, `Client`, `Grossiste`, `Achat`, `LigneAchat`, `Livraison`, `LigneLivraison`
- **Vue** : JSP + JSTL + CSS custom
- **Contrôleur** : Servlets annotés (`@WebServlet`), Filtres (`@WebFilter`)
- **DAO** : `XxxDAO` avec `PreparedStatement`, `try-with-resources`
- **Service** : logique métier, validations
- **Configuration** : `db.properties` + `DatabaseConnection` (pool HikariCP)

### 4.2 Structure du projet
gestion-boutique/
├── pom.xml
├── ddl.sql
├── RAPPORT.md
├── README.md
├── captures/ → 8 captures d'écran
└── src/main/
├── java/com/gestionboutique/
│ ├── config/ → DatabaseConnection (HikariCP)
│ ├── controller/ → 23 Servlets (points d'entrée HTTP)
│ ├── dao/ → 6 DAO (accès PostgreSQL)
│ ├── filter/ → AuthFilter, AdminFilter
│ ├── model/ → 9 entités métier
│ ├── service/ → 7 services (logique métier)
│ └── util/ → SessionUtil, HashGenerator
├── resources/
│ └── db.properties → Configuration base de données
└── webapp/
├── css/style.css
├── js/validation.js
├── login.jsp
└── WEB-INF/
├── web.xml
└── views/ → 20+ JSP

### 4.3 Modèle Conceptuel de Données (MCD)

UTILISATEUR (id, nom, prenom, matricule, sexe, date_naissance, identifiant, mot_de_passe, role, actif, date_creation)
PRODUIT (id, nom, prix_achat, prix_vente, stock)
CLIENT (id, nom, prenom, telephone, email, adresse, actif, date_creation)
GROSSISTE (id, nom, telephone, email, adresse, contact_nom, actif, date_creation)
ACHAT (id, client_id → CLIENT, utilisateur_id → UTILISATEUR, date_achat, total, statut)
LIGNE_ACHAT (id, achat_id → ACHAT, produit_id → PRODUIT, quantite, prix_unitaire, sous_total)
LIVRAISON (id, grossiste_id → GROSSISTE, utilisateur_id → UTILISATEUR, date_livraison, total, reference)
LIGNE_LIVRAISON (id, livraison_id → LIVRAISON, produit_id → PRODUIT, quantite, prix_unitaire, sous_total)


### 4.4 Diagramme de classes (résumé)
┌─────────────────┐ ┌─────────────────┐
│ Utilisateur │ │ Produit │
├─────────────────┤ ├─────────────────┤
│ id │ │ id │
│ nom │ │ nom │
│ prenom │ │ prixAchat │
│ matricule │ │ prixVente │
│ sexe │ │ stock │
│ dateNaissance │ └─────────────────┘
│ identifiant │
│ motDePasse │ ┌─────────────────┐
│ role (Role) │ │ Client │
│ actif │ ├─────────────────┤
└─────────────────┘ │ id │
│ nom │
┌─────────────────┐ │ prenom │
│ Grossiste │ │ telephone │
├─────────────────┤ │ email │
│ id │ │ adresse │
│ nom │ │ actif │
│ telephone │ └─────────────────┘
│ email │
│ adresse │ ┌─────────────────┐
│ contactNom │ │ Livraison │
│ actif │ ├─────────────────┤
└─────────────────┘ │ id │
│ grossisteId │
┌─────────────────┐ │ utilisateurId │
│ Achat │ │ dateLivraison │
├─────────────────┤ │ total │
│ id │ │ reference │
│ clientId │ │ lignes[] │
│ utilisateurId │ └─────────────────┘
│ dateAchat │ │
│ total │ │ 1..*
│ statut │ ▼
│ lignes[] │ ┌─────────────────┐
└─────────────────┘ │ LigneLivraison │
│ ├─────────────────┤
│ 1..* │ id │
▼ │ produitId │
┌─────────────────┐ │ quantite │
│ LigneAchat │ │ prixUnitaire │
├─────────────────┤ │ sousTotal │
│ id │ └─────────────────┘
│ produitId │
│ quantite │
│ prixUnitaire │
│ sousTotal │
└─────────────────┘


---

## 5. IHM réalisée et tests

### 5.1 Pages réalisées

| Page | URL |
|------|-----|
| Connexion | `/login` |
| Tableau de bord | `/dashboard` |
| Liste utilisateurs | `/utilisateurs/liste` |
| Ajouter utilisateur | `/utilisateurs/nouveau` |
| Modifier utilisateur | `/utilisateurs/modifier?id=X` |
| Ajouter produit | `/produits/ajouter` |
| Rechercher produit | `/produits/recherche` |
| Liste clients | `/clients/liste` |
| Ajouter client | `/clients/nouveau` |
| Modifier client | `/clients/modifier?id=X` |
| Liste grossistes | `/grossistes/liste` |
| Ajouter grossiste | `/grossistes/nouveau` |
| Modifier grossiste | `/grossistes/modifier?id=X` |
| Nouvel achat | `/achats/nouveau` |
| Historique achats | `/achats/historique` |
| Détail achat | `/achats/detail?id=X` |
| Ticket PDF | `/tickets/imprimer?id=X` |
| Nouvelle livraison | `/livraisons/nouvelle` |
| Historique livraisons | `/livraisons/historique` |
| Détail livraison | `/livraisons/detail?id=X` |

### 5.2 Scénarios de test validés

| # | Test | Résultat |
|---|------|----------|
| 1 | Connexion avec `admin` / `admin123` | ✅ |
| 2 | Création d'un utilisateur VENDEUR | ✅ |
| 3 | Connexion en tant que VENDEUR | ✅ |
| 4 | Accès refusé pour VENDEUR sur `/utilisateurs/liste` | ✅ |
| 5 | Création d'un produit, recherche, vérification en base | ✅ |
| 6 | Création d'un client, modification, désactivation | ✅ |
| 7 | Création d'un grossiste | ✅ |
| 8 | Enregistrement d'un achat avec 3 produits | ✅ |
| 9 | Vérification en base : stock décrémenté | ✅ |
| 10 | Impression du ticket PDF | ✅ |
| 11 | Enregistrement d'une livraison | ✅ |
| 12 | Vérification en base : stock augmenté | ✅ |
| 13 | Filtre par date sur l'historique des achats | ✅ |
| 14 | Filtre par date sur l'historique des livraisons | ✅ |

### 5.3 Captures d'écran

Les captures d'écran ci-dessous illustrent les principales interfaces de l'application.
Elles sont également disponibles dans le dossier `captures/` du livrable.

#### 1. Page de connexion
![Connexion](captures/01-connexion.png)

#### 2. Tableau de bord
![Dashboard](captures/02-dashboard.png)

#### 3. Liste des utilisateurs
![Utilisateurs](captures/03-liste-utilisateurs.png)

#### 4. Nouvel achat avec panier dynamique
![Nouvel achat](captures/04-nouvel-achat.png)

#### 5. Ticket de caisse PDF
![Ticket](captures/05-ticket-pdf.png)

#### 6. Historique des achats avec filtre par date
![Historique](captures/06-historique-achats.png)

#### 7. Nouvelle livraison
![Livraison](captures/07-nouvelle-livraison.png)

#### 8. Liste des clients
![Clients](captures/08-liste-clients.png)

---

## 6. Points techniques forts

- **Pool de connexions HikariCP** : performance et robustesse (pas d'ouverture/fermeture à chaque requête)
- **Configuration externalisée** (`db.properties`) : aucun mot de passe codé en dur
- **Transactions atomiques** pour achat et livraison (rollback en cas d'erreur)
- **Hashage BCrypt** des mots de passe (cost 12, jamais en clair)
- **Filtres d'authentification et d'autorisation** : sécurité en profondeur
- **Validation métier centralisée** dans les Services
- **Architecture en couches** respectant MVC
- **Compatibilité Jakarta EE 10** (Tomcat 10+, JDK 17)
- **Recherche insensible à la casse** via `ILIKE` (spécifique PostgreSQL)
- **Types `BigDecimal`** pour les prix (précision garantie)

---

## 7. Instructions de déploiement

### 7.1 Prérequis
- JDK 17+
- PostgreSQL 16+
- Apache Tomcat 10.1+
- Maven 3.8+

### 7.2 Étapes de déploiement

1. **Créer la base de données** :
   ```bash
   psql -U postgres -c "CREATE DATABASE gestion_boutique;"
   psql -U postgres -d gestion_boutique -f ddl.sql

2. Configurer la connexion dans src/main/resources/db.properties :

db.url=jdbc:postgresql://localhost:5432/gestion_boutique
db.user=postgres
db.password=VOTRE_MOT_DE_PASSE

3. Compiler :

mvn clean package

4.Déployer :
copy target/gestion-boutique.war TOMCAT_HOME/webapps/

5. Démarrer Tomcat et accéder à :
http://localhost:8080/gestion-boutique/login

6.Se connecter avec :
  COMPTE ADMIN :
Identifiant : admin
Mot de passe : admin123

 COMPTE VENDEUR :
Identifiant : marie
Mot de passe : marie123

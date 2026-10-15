# BiblioGest

Application de bureau (Windows) pour gérer une bibliothèque : catalogue de livres, lecteurs, emprunts, retours, amendes, statistiques et états imprimables.

![Aperçu de BiblioGest](docs/apercu.jpg)

## Le problème

Beaucoup de petites bibliothèques gèrent leurs emprunts sur papier : livres perdus, retards non suivis, aucune vision de l'activité. BiblioGest centralise tout dans une seule application.

## Fonctionnalités

- **Livres** : ajout, modification, suppression, recherche, filtre par catégorie, nombre d'exemplaires total et disponible, détection des doublons.
- **Lecteurs** : inscription, modification, recherche, historique des emprunts, contrôle des doublons (nom, prénom, téléphone).
- **Emprunts** : durée par défaut de 14 jours, **3 emprunts actifs maximum par lecteur**, un lecteur ne peut pas emprunter deux fois le même livre en même temps.
- **Retours et amendes** : 100 F par jour de retard, plus un supplément si le livre est rendu usé (500 F) ou abîmé (2 000 F).
- **Tableau de bord** : emprunts et retours du jour, amendes du jour, emprunts actifs, retards.
- **Statistiques** par période : livres et lecteurs les plus actifs, rapport PDF (JasperReports).
- **États et exports** : listes de livres, lecteurs, emprunts, historique, rapport des retards ; export CSV lisible dans Excel.
- Écran de connexion avec identifiant et mot de passe.

## Stack

Java 8 · Swing (interfaces dessinées avec NetBeans) · FlatLaf (thème) · MariaDB / MySQL via JDBC (pilote MariaDB Connector/J) · JasperReports, iText, JFreeChart · packaging Ant (NetBeans) · installeur Inno Setup qui embarque Java et MariaDB.

## Architecture

Application en couches :

```
src/models/   objets métier (Livre, Lecteur, Emprunt, Utilisateur)
src/dao/      accès à la base (un DAO par entité, requêtes préparées)
src/views/    interfaces Swing
src/utils/    états et exports
src/reports/  modèle de rapport JasperReports
lib/          bibliothèques .jar nécessaires
```

## Essayer l'application (le plus simple)

1. Télécharger `BiblioGest_Setup_v1.0.exe` dans la page [Releases](https://github.com/clever75/BiblioGest/releases) et lancer l'installeur (droits administrateur demandés).
2. L'installeur met en place l'application, **sa propre base MariaDB** (service Windows `BiblioGestDB`, port 3307, accessible uniquement depuis votre PC), les tables et un compte de démonstration. Rien d'autre à installer.
3. Lancer BiblioGest et se connecter avec le compte de démonstration ci-dessous.

## Compte de démonstration

| Identifiant | Mot de passe |
|-------------|--------------|
| `demo`      | `demo1234`   |

Compte fictif créé par l'installeur, uniquement pour tester.

## Installation pour le développement

Prérequis : JDK 8 ou plus, NetBeans, MariaDB ou MySQL (XAMPP convient).

1. Créer la base `biblioapp`, puis importer `database/schema.sql` (tables) et `database/seed.sql` (compte de démonstration).
2. Ouvrir le dossier dans NetBeans (*File → Open Project*). Les `.jar` de `lib/` sont déjà référencés.
3. Lancer le projet : la classe principale est `views.MainFrame`.

La connexion à la base se règle dans un fichier `config.properties` placé à côté de l'application (jamais envoyé sur GitHub). **Sans ce fichier**, l'application utilise les réglages de développement `localhost:3306`, utilisateur `root`, mot de passe vide (XAMPP). Format du fichier :

```properties
db.host=127.0.0.1
db.port=3306
db.name=biblioapp
db.user=root
db.password=
```

## Ce que j'ai appris

- Concevoir une interface de bureau ergonomique avec Swing.
- Séparer l'interface, la logique métier et l'accès aux données.
- Traduire des règles métier (limites d'emprunt, amendes) en code vérifiable.
- Produire des états PDF et des exports, et empaqueter une application Java pour Windows.

## Limites connues et pistes d'amélioration

- Les mots de passe des utilisateurs de l'application sont stockés en clair dans la table `utilisateur` : à remplacer par un hachage (BCrypt). C'est la première amélioration prévue.
- Pas de tests automatisés.

## Contexte

Projet réalisé en 2026 dans le cadre du BTS en développement d'applications à DEFITECH (Lomé).

**Auteur** : ASSAN Ablavi Clever, [GitHub](https://github.com/clever75)


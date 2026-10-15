-- MariaDB dump 10.19  Distrib 10.4.32-MariaDB, for Win64 (AMD64)
--
-- Host: localhost    Database: biblioapp
-- ------------------------------------------------------
-- Server version	10.4.32-MariaDB

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Current Database: `biblioapp`
--

CREATE DATABASE /*!32312 IF NOT EXISTS*/ `biblioapp` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci */;

USE `biblioapp`;

--
-- Table structure for table `emprunt`
--

DROP TABLE IF EXISTS `emprunt`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `emprunt` (
  `idEmprunt` int(11) NOT NULL AUTO_INCREMENT,
  `idLivre` int(11) NOT NULL,
  `idLecteur` int(11) NOT NULL,
  `dateEmprunt` date NOT NULL,
  `dateRetourPrevue` date NOT NULL,
  `dateRetourReelle` date DEFAULT NULL,
  `statut` varchar(20) NOT NULL DEFAULT 'en cours',
  `etatLivre` varchar(20) DEFAULT NULL,
  `remarque` varchar(255) DEFAULT NULL,
  `amende` decimal(10,2) DEFAULT 0.00,
  `amendePaye` varchar(10) DEFAULT 'non',
  PRIMARY KEY (`idEmprunt`),
  KEY `idLivre` (`idLivre`),
  KEY `idLecteur` (`idLecteur`),
  CONSTRAINT `emprunt_ibfk_1` FOREIGN KEY (`idLivre`) REFERENCES `livre` (`idLivre`),
  CONSTRAINT `emprunt_ibfk_2` FOREIGN KEY (`idLecteur`) REFERENCES `lecteur` (`idLecteur`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `lecteur`
--

DROP TABLE IF EXISTS `lecteur`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `lecteur` (
  `idLecteur` int(11) NOT NULL AUTO_INCREMENT,
  `nom` varchar(100) NOT NULL,
  `prenom` varchar(100) NOT NULL,
  `telephone` varchar(20) DEFAULT NULL,
  `dateInscription` date NOT NULL DEFAULT curdate(),
  `adresse` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`idLecteur`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `livre`
--

DROP TABLE IF EXISTS `livre`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `livre` (
  `idLivre` int(11) NOT NULL AUTO_INCREMENT,
  `titre` varchar(100) NOT NULL,
  `auteur` varchar(100) NOT NULL,
  `categorie` varchar(50) NOT NULL,
  `statut` varchar(20) NOT NULL DEFAULT 'disponible',
  `quantite` int(11) NOT NULL DEFAULT 1,
  `disponible` tinyint(1) DEFAULT 1,
  PRIMARY KEY (`idLivre`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `utilisateur`
--

DROP TABLE IF EXISTS `utilisateur`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `utilisateur` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `nom` varchar(50) NOT NULL,
  `prenom` varchar(50) NOT NULL,
  `nomUtilisateur` varchar(50) NOT NULL,
  `motDePasse` varchar(50) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping routines for database 'biblioapp'
--
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed

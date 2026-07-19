/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models;

/**
 *
 * @author Admin
 */
public class Emprunt {

    private int idEmprunt;
    private int idLivre;
    private int idLecteur;
    private String titre;
    private String nomLecteur;
    private String dateEmprunt;
    private String dateRetourPrevue;
    private String dateRetourReelle;
    private String statut;

    // Constructeur complet
    public Emprunt(int idEmprunt, int idLivre, int idLecteur,
            String titre, String nomLecteur,
            String dateEmprunt, String dateRetourPrevue,
            String dateRetourReelle, String statut) {
        this.idEmprunt = idEmprunt;
        this.idLivre = idLivre;
        this.idLecteur = idLecteur;
        this.titre = titre;
        this.nomLecteur = nomLecteur;
        this.dateEmprunt = dateEmprunt;
        this.dateRetourPrevue = dateRetourPrevue;
        this.dateRetourReelle = dateRetourReelle;
        this.statut = statut;
    }

    // Getters
    public int getIdEmprunt() {
        return idEmprunt;
    }

    public int getIdLivre() {
        return idLivre;
    }

    public int getIdLecteur() {
        return idLecteur;
    }

    public String getTitre() {
        return titre;
    }

    public String getNomLecteur() {
        return nomLecteur;
    }

    public String getDateEmprunt() {
        return dateEmprunt;
    }

    public String getDateRetourPrevue() {
        return dateRetourPrevue;
    }

    public String getDateRetourReelle() {
        return dateRetourReelle;
    }

    public String getStatut() {
        return statut;
    }

    // Setters
    public void setIdEmprunt(int idEmprunt) {
        this.idEmprunt = idEmprunt;
    }

    public void setIdLivre(int idLivre) {
        this.idLivre = idLivre;
    }

    public void setIdLecteur(int idLecteur) {
        this.idLecteur = idLecteur;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public void setNomLecteur(String nomLecteur) {
        this.nomLecteur = nomLecteur;
    }

    public void setDateEmprunt(String dateEmprunt) {
        this.dateEmprunt = dateEmprunt;
    }

    public void setDateRetourPrevue(String dateRetourPrevue) {
        this.dateRetourPrevue = dateRetourPrevue;
    }

    public void setDateRetourReelle(String dateRetourReelle) {
        this.dateRetourReelle = dateRetourReelle;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }
}

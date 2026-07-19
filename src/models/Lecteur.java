/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

/**
 *
 * @author Admin
 */
public class Lecteur {

    private int idLecteur;
    private String nom;
    private String prenom;
    private String telephone;
    private String adresse;

    private String dateInscription;

    public Lecteur(String nom, String prenom, String telephone,String adresse, String dateInscription) {
        this.nom = nom;
        this.prenom = prenom;
        this.telephone = telephone;
        this.adresse=adresse;
        this.dateInscription = dateInscription;
    }

    public Lecteur(int idLecteur, String nom, String prenom, String telephone,String adresse, String dateInscription) {
        this.idLecteur = idLecteur;
        this.nom = nom;
        this.prenom = prenom;
        this.telephone = telephone;
        this.adresse=adresse;
        this.dateInscription = dateInscription;
    }

    public int getIdLecteur() {
        return idLecteur;
    }

    public String getNom() {
        return nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public String getTelephone() {
        return telephone;
    }

    public String getAdresse() {
        return adresse;
    }

    public String getDateInscription() {
        return dateInscription;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

}

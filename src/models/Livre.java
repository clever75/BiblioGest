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
public class Livre {

    private int idLivre;
    private String titre;
    private String auteur;
    private String categorie;
    private int quantite;
    private int nbDisponibles;

    public Livre(String titre, String auteur, String categorie, int quantite) {
        this.titre = titre;
        this.auteur = auteur;
        this.categorie = categorie;
        this.quantite = quantite;
    }

    public Livre(int idLivre, String titre, String auteur, String categorie, int quantite, int nbDisponibles) {
        this.idLivre = idLivre;
        this.titre = titre;
        this.auteur = auteur;
        this.categorie = categorie;
        this.quantite = quantite;
        this.nbDisponibles = nbDisponibles;
    }

    public int getIdLivre() {
        return idLivre;
    }

    public String getTitre() {
        return titre;
    }

    public String getAuteur() {
        return auteur;
    }

    public String getCategorie() {
        return categorie;
    }

    public String getStatut() {
        return nbDisponibles > 0 ? "disponible" : "indisponible";
    }

    public int getNbDisponibles() {
        return nbDisponibles;
    }

    public int getQuantite() {
        return quantite;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public void setAuteur(String auteur) {
        this.auteur = auteur;
    }

    public void setCategorie(String categorie) {
        this.categorie = categorie;
    }

    public void setQuantite(int quantite) {
        this.quantite = quantite;
    }

    public void setNbDisponibles(int nbDisponibles) {
        this.nbDisponibles = nbDisponibles;
    }
}

package utils;

import javafx.beans.property.SimpleStringProperty;

public class MonTicket {
    private final SimpleStringProperty produit ;
    private final SimpleStringProperty prixboutique ;
    private final SimpleStringProperty prixkollere ;

    private final SimpleStringProperty quantite ;

    public MonTicket(String item, String prixboutique, String prixkollere, String quantite) {
        this.produit = new SimpleStringProperty(item);
        this.prixboutique = new SimpleStringProperty(prixboutique) ;
        this.prixkollere = new SimpleStringProperty(prixkollere);

        this.quantite = new SimpleStringProperty(quantite);
    }

    public String getProduit() {
        return produit.get();
    }

    public SimpleStringProperty itemProperty() {
        return produit;
    }

    public void setItem(String item) {
        this.produit.set(item);
    }

    public String getPrixboutique() {
        return prixboutique.get();
    }

    public SimpleStringProperty prixboutiqueProperty() {
        return prixboutique;
    }

    public void setPrixboutique(String prixboutique) {
        this.prixboutique.set(prixboutique);
    }

    public String getPrixkollere() {
        return prixkollere.get();
    }

    public SimpleStringProperty prixkollereProperty() {
        return prixkollere;
    }

    public void setPrixkollere(String prixkollere) {
        this.prixkollere.set(prixkollere);
    }



    public String getQuantite() {
        return quantite.get();
    }

    public SimpleStringProperty quantiteProperty() {
        return quantite;
    }

    public void setQuantite(String quantite) {
        this.quantite.set(quantite);
    }
}

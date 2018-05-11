package utils;

import javafx.beans.property.SimpleStringProperty;

public class Historique {
    private final SimpleStringProperty item ;
    private final SimpleStringProperty prixboutique ;
    private final SimpleStringProperty prixkollere ;
    private final SimpleStringProperty client ;

    public Historique(String item, String prixboutique, String prixkollere, String client) {
        this.item = new SimpleStringProperty(item) ;
        this.prixboutique = new SimpleStringProperty(prixboutique);
        this.prixkollere = new SimpleStringProperty(prixkollere) ;
        this.client = new SimpleStringProperty(client);
    }

    public String getItem() {
        return item.get();
    }

    public SimpleStringProperty itemProperty() {
        return item;
    }

    public void setItem(String item) {
        this.item.set(item);
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

    public String getClient() {
        return client.get();
    }

    public SimpleStringProperty clientProperty() {
        return client;
    }

    public void setClient(String client) {
        this.client.set(client);
    }
}

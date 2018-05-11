package utils;

public class Ticket {

    private String item,voucherId,prixoutique,prixkollere;

    public Ticket(String item, String voucherId, String prixoutique, String prixkollere) {
        this.item = item;
        this.voucherId = voucherId;
        this.prixoutique = prixoutique;
        this.prixkollere = prixkollere;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public String getVoucherId() {
        return voucherId;
    }

    public void setVoucherId(String voucherId) {
        this.voucherId = voucherId;
    }

    public String getPrixoutique() {
        return prixoutique;
    }

    public void setPrixoutique(String prixoutique) {
        this.prixoutique = prixoutique;
    }

    public String getPrixkollere() {
        return prixkollere;
    }

    public void setPrixkollere(String prixkollere) {
        this.prixkollere = prixkollere;
    }

    @Override
    public String toString() {
        return item;
    }
}

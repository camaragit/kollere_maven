package modules.carte.achat;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.validation.NumberValidator;
import com.jfoenix.validation.RequiredFieldValidator;
import de.jensd.fx.glyphs.GlyphsBuilder;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import utils.KollereUtils;
import utils.Ticket;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Observable;
import java.util.ResourceBundle;

public class AchatController implements Initializable {
    @FXML
    private AnchorPane inscription;

    @FXML
    private AnchorPane holderAnchor;

    @FXML
    private JFXTextField txtprixboutique;

    @FXML
    private JFXButton btnsave;
    @FXML
    private JFXComboBox<Ticket> moncombo;

    @FXML
    private JFXTextField txtprixkollere;

    @FXML
    void acheter(ActionEvent event) {

        if(moncombo.getValue()!=null)
        {
            if(KollereUtils.IDCARTE.equals(""))
            {
                if(KollereUtils.lireidnfc())
                {
                    acheterFunction();
                }
            }
            else acheterFunction();

        }
        else KollereUtils.showAlert("Veuillez selectionner un produit d'abord","Achat par Carte","erreur");

    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("controlleur Achat carte");
        moncombo.setOnAction((e)->{

            txtprixboutique.setText(KollereUtils.NUMBER_FORMAT.format(Integer.parseInt(moncombo.getValue().getPrixoutique().toString()))+" F CFA");
            txtprixkollere.setText(KollereUtils.NUMBER_FORMAT.format(Integer.parseInt(moncombo.getValue().getPrixkollere().toString()))+" F CFA");
            txtprixboutique.setPromptText("Prix "+KollereUtils.TITRE_BOUTIQUE.getValue());
        });

    }

    public void chargerlist(ObservableList<Ticket> listeticket){
        moncombo.getItems().clear();
        moncombo.getItems().addAll(listeticket);

    }
    public void reload(){
        KollereUtils.IDCARTE="";
        txtprixboutique.setText("");
        txtprixkollere.setText("");

        ObjectMapper mapper = new ObjectMapper();
        ObservableList<Ticket> lt = FXCollections.observableArrayList();

        String url = null;
        try {
            url = "http://services.ajit.sn/ws/resto/listitemsresto?commerce="+ URLEncoder.encode(KollereUtils.TITRE_BOUTIQUE.getValue(), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        //  System.out.println(u);
        JsonNode actualObj = KollereUtils.callWebservice(url);
        System.out.println(actualObj.toString());
        if(actualObj!=null)
        {
            int t= actualObj.size();
            lt = FXCollections.observableArrayList();
            System.out.println(t);
            for(int i=0;i< t;i++)
            {
                JsonNode val= actualObj.get(i);

                System.out.println(val.toString());
                if(!val.get("item").asText().equals(""))
                    lt.add(new Ticket(val.get("item").asText(),val.get("voucherid").asText(),val.get("valeurItem").get("prixResto").asText(),val.get("valeurItem").get("prixKollere").asText()));
            }


            this.chargerlist(lt);
        }
        else KollereUtils.showAlert("Impossible de charger la liste des items","Chargement Item","erreur");

    }

    public void acheterFunction(){
        try {
            String url = "http://services.ajit.sn/ws/resto/ticketachat?token="+KollereUtils.TOKEN+"&nfcid="+KollereUtils.IDCARTE+"&voucherid="+moncombo.getValue().getVoucherId()+"&item="+ URLEncoder.encode(moncombo.getValue().getItem(), "UTF-8")+"&montant="+moncombo.getValue().getPrixoutique();
            JsonNode actualObj = KollereUtils.callWebservice(url);
            if(actualObj!=null)
            {
                if(actualObj.get("code").asText().equals("0"))
                {

                    String urlcash ="http://services.ajit.sn/ws/resto/bilan?agentid="+KollereUtils.AGENTID+"&jour=jour&mois=mois&annee=annee";

                    JsonNode cash = KollereUtils.callWebservice(urlcash);
                    if(cash==null)
                    {
                        KollereUtils.showAlert("Impossible de recuperer le cash agent","Achat","erreur");
                        return;
                    }
                    if(!cash.get("code").asText().equals("0"))
                    {
                        KollereUtils.showAlert(cash.get("message").asText(),"Achat","erreur");
                        return;

                    }
                    KollereUtils.SOLDE.setValue("SOLDE :"+KollereUtils.NUMBER_FORMAT.format(actualObj.get("montantRestant").asInt())+" F CFA | CASH : "+cash.get("montantRecu").asText());
                    KollereUtils.showAlert("Achat effectuée avec succès","Annulation","infos");
                    reload();
                }
                else KollereUtils.showAlert(actualObj.get("message").asText(),"Achat","erreur");
            }
            else KollereUtils.showAlert("Erreur inconnue \n Veuillez contacter le support","Achat","erreur");

        } catch (UnsupportedEncodingException e) {
            KollereUtils.showAlert(e.getMessage(),"Encodage","erreur");
            e.printStackTrace();
        }
    }
}

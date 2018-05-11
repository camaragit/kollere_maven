package modules.historique;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDatePicker;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.cell.PropertyValueFactory;
import org.codehaus.jackson.JsonNode;
import utils.Historique;
import utils.KollereUtils;

import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.ResourceBundle;

public class HistoryController implements Initializable {
    @FXML
    private TableView<Historique> tablehistorique;
    private ObservableList<Historique> list_achats;
    @FXML
    private TableColumn<Historique, String> item;

    @FXML
    private TableColumn<Historique, String> prix_boutique;

    @FXML
    private TableColumn<Historique, String> prix_kollere;
    @FXML
    private JFXDatePicker dateselected;
    @FXML
    private TableColumn<Historique, String> numclient;

    @FXML
    private Label entete;
    @FXML
    private JFXButton btnannul;

    @FXML
    void annulertrx(ActionEvent event) {

       Historique h = tablehistorique.getSelectionModel().getSelectedItem();
       if(h!=null)
       {

           TextInputDialog dialog = new TextInputDialog("");
           dialog.setTitle("Annulation");
           dialog.setHeaderText("Vous avez demandé l'annulation de l' achat \n"+"Item : "+h.getItem()+"\nTicket : "+h.getClient()+"\n Montant : "+h.getPrixboutique());
           dialog.setContentText("Saisir votre motif :");


// Traditional way to get the response value.
           Optional<String> result = dialog.showAndWait();
/*           if (result.isPresent()){
               System.out.println("Your name: " + result.get());
           }*/

// The Java 8 way to get the response value (with lambda expression).000000

           result.ifPresent(motif -> {
               if(!motif.equals(""))
               {
                   try {

                       try {
                           Number mnt =  KollereUtils.NUMBER_FORMAT.parse(h.getPrixboutique().substring(0,h.getPrixboutique().indexOf("F CFA")));
                           String url = "http://services.ajit.sn/ws/resto/itemannulation?token="+KollereUtils.TOKEN+"&item="+URLEncoder.encode(h.getItem(),"UTF-8")+"&motif="+URLEncoder.encode(motif,"UTF-8")+"&montant="+mnt;

                           System.out.println("Votre motif: " + motif);
                           System.out.println(h.getItem());
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
                                   KollereUtils.showAlert("Transaction annulée avec succès","Annulation","info");
                                   //KollereUtils.SOLDE.setValue("SOLDE     : "+KollereUtils.NUMBER_FORMAT.format(actualObj.get("montantRestant").asInt())+" F CFA");
                               }
                               else KollereUtils.showAlert(actualObj.get("message").asText(),"Annulation","erreur");
                           }
                           else KollereUtils.showAlert("Erreur inconnue \n Veuillez contacter le support","Achat","erreur");

                       } catch (ParseException e) {
                           KollereUtils.showAlert(e.getMessage(),"Annulation","erreur");
                       }


                   } catch (UnsupportedEncodingException e) {
                       KollereUtils.showAlert(e.getMessage(),"Annulation","erreur");
                   }
               }
               else KollereUtils.showAlert("Vous devez saisir votre motif pour annuler la transaction","Annulation","info");



           });

       }
       else
           KollereUtils.showAlert("Veuillez selectionner une ligne d'abord","Annulation","erreur");

    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
    System.out.println("controlleur hisstorique");

    }
    @FXML
    void findAchat(ActionEvent event) {
      //  DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy, EEEE dd MMMM yyyy");
        tablehistorique.setVisible(false);
        btnannul.setVisible(false);
        entete.setVisible(false);
        LocalDate date = dateselected.getValue();
        if(date!=null)
        {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE dd MMMM yyyy");
            DateTimeFormatter formatjour = DateTimeFormatter.ofPattern("dd");
            DateTimeFormatter formatmois = DateTimeFormatter.ofPattern("MM");
            DateTimeFormatter formatannee = DateTimeFormatter.ofPattern("yyyy");

            String jour = formatjour.format(date);
            String mois = formatmois.format(date);
            String annee = formatannee.format(date);
            String datef =formatter.format(date);
            String url="http://services.ajit.sn/ws/resto/achatfromsite?token="+ KollereUtils.TOKEN+"&jour="+jour+"&mois="+mois+"&annee="+annee;
            System.out.println(url);
            JsonNode actualObj = KollereUtils.callWebservice(url);
            if(actualObj!=null){
                if(actualObj.get("code").asText().equals("0"))
                {
                    // System.out.println(actualObj.get("lAchats"));
                    JsonNode tab = actualObj.get("lAchats");
                    int taille =tab.size();
                    list_achats= FXCollections.observableArrayList();
                    if(taille>0){
                        tablehistorique.setVisible(true);
                        entete.setVisible(true);
                       // btnannul.setVisible(true);
                        for(int i=0;i<taille;i++)
                        {
                            JsonNode val = tab.get(i);
                            System.out.println(val.get("item")); //val.get("montantKollere").asText()
                            Historique historique = new Historique(val.get("item").asText(),KollereUtils.NUMBER_FORMAT.format(val.get("prixrestaurant").asInt())+" F CFA",KollereUtils.NUMBER_FORMAT.format(val.get("prixkollere").asInt())+" F CFA",val.get("ticket").asText());

                            list_achats.add(historique);
                        }
                        item.setCellValueFactory(new PropertyValueFactory<>("item"));
                        prix_boutique.setCellValueFactory(new PropertyValueFactory<>("prixboutique"));
                        prix_kollere.setCellValueFactory(new PropertyValueFactory<>("prixkollere"));
                        numclient.setCellValueFactory(new PropertyValueFactory<>("client"));

                        entete.setText("Détails des achats du "+datef);
                        tablehistorique.getItems().clear();
                        tablehistorique.getItems().addAll(list_achats);
                    }
                    else{
                        KollereUtils.showAlert("Pas d'achat effectuté le "+datef,"Historique","infos");
                    }

                }
                else {
                    KollereUtils.showAlert(actualObj.get("message").asText(),"Historique","erreur");

                }

            }
            else {
                KollereUtils.showAlert("Erreur Veuillez contacter le support technique","Historique","erreur");

            }
        }
        else {
            KollereUtils.showAlert("Veuillez selectionner une date","Historique","erreur");

        }


    }
}

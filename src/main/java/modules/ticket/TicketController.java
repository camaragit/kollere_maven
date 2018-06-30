package modules.ticket;

import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.codehaus.jackson.JsonNode;
import utils.Historique;
import utils.KollereUtils;
import utils.MonTicket;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class TicketController implements Initializable {
    @FXML
    private TableView<MonTicket> tableitems;
    private ObservableList<MonTicket> list_achats;
    @FXML
    private TableColumn<Historique, String> produit;

    @FXML
    private TableColumn<Historique, String> prix_boutique;

    @FXML
    private TableColumn<Historique, String> prix_kollere;
    @FXML
    private TableColumn<Historique, String> quantite;
    @FXML
    private AnchorPane  holderAnchor;

    @FXML
    private JFXTextField codevalidation;

    @FXML
    private Pane detailsContentPane;
    @FXML
    private Pane panierContain;
    @FXML
    private Label prixshop;
    @FXML
    private Label item;
    @FXML
    private Label prixk;
    @FXML
    private Label erreur;

    @FXML
    private Label valitem;

    @FXML
    private Label valprixshop;
    @FXML
    private Label valprixk;
    @FXML
    private Label lbprixtbtique;

    @FXML
    private Label prixtbtique;


    @FXML
    private Label prixtkollere;
    @FXML
    private Pane titlePane;
    Alert a = new Alert(Alert.AlertType.INFORMATION);
    Boolean isTicket = false;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
    KollereUtils.TICKET="";
        a.setTitle("Paiement");
        a.setHeaderText("");
        Stage stage = (Stage) a.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image(KollereUtils.IMAGE_LOC));
        System.out.println("controlleur Ticket");
    }

    @FXML
    void sendcode(ActionEvent event) {
        KollereUtils.TICKET="";
        detailsContentPane.setVisible(false);
        panierContain.setVisible(false);
        titlePane.setVisible(false);
        erreur.setText("");
        if(!codevalidation.getText().equals(""))
        {
            String suffix = codevalidation.getText().substring(0,2);
            JsonNode actualObj;
            //Les gateaux d'anniversaire
            if(suffix.equals("GT"))
            {
                           /* if(KollereUtils.netIsAvailable())
            {*/
                actualObj = KollereUtils.callWebservice("http://services.ajit.sn/ws/resto/infosticket?ticket="+codevalidation.getText()+"&token="+KollereUtils.TOKEN);
                // System.out.println(actualObj.toString());
                if(actualObj!=null)
                {
                    if(actualObj.get("code").asText().equals("0"))
                    {
                        System.out.println("c le bon");
                        detailsContentPane.setVisible(true);
                        panierContain.setVisible(false);
                        titlePane.setVisible(true);
                        KollereUtils.TICKET=codevalidation.getText();
                        prixshop.setText("PRIX "+KollereUtils.TITRE_BOUTIQUE.getValue());
                        valitem.setText(actualObj.get("item").asText());
                        valprixshop.setText(KollereUtils.NUMBER_FORMAT.format(actualObj.get("prixResto").asInt())+" F CFA");
                        valprixk.setText(KollereUtils.NUMBER_FORMAT.format(actualObj.get("montantKollere").asInt())+" F CFA");
                        erreur.setVisible(false);
                        isTicket = true;

                    }
                    else {
                        erreur.setText(actualObj.get("message").asText()+" !");
                        erreur.setVisible(true);



                    }
                    System.out.println(actualObj.get("code"));
                    System.out.println(actualObj.get("item"));
                    System.out.println(actualObj.get("prix"));
                }
                else{
                    erreur.setText("Erreur non traitée Veuillez contacter le support!");
                }
            }
            //les autres tickets
            else{
                actualObj = KollereUtils.callWebservice("http://services.ajit.sn/ws/resto/listpaniertemsresto?token="+KollereUtils.TOKEN+"&codepanier="+codevalidation.getText());
                if(actualObj.get("code").asText().equals("0"))
                {
                    JsonNode paniers = actualObj.get("paniers");
                    int taille = paniers.size();
                    list_achats= FXCollections.observableArrayList();
                    detailsContentPane.setVisible(false);
                    panierContain.setVisible(true);
                    for(int i=0;i<taille;i++)
                    {
                        JsonNode val = paniers.get(i);
                        System.out.println(val);
                        MonTicket ticket = new MonTicket(val.get("item").asText(),KollereUtils.NUMBER_FORMAT.format(val.get("prixrestaurant").asInt())+" F CFA",KollereUtils.NUMBER_FORMAT.format(val.get("prixsurkollere").asInt())+" F CFA",val.get("quantite").asText());
                        list_achats.add(ticket);
                    }
                    produit.setCellValueFactory(new PropertyValueFactory<>("produit"));
                    prix_boutique.setCellValueFactory(new PropertyValueFactory<>("prixboutique"));
                    prix_kollere.setCellValueFactory(new PropertyValueFactory<>("prixkollere"));
                    quantite.setCellValueFactory(new PropertyValueFactory<>("quantite"));

                    //entete.setText("Détails des achats du "+datef);
                    lbprixtbtique.setText("PRIX TOTAL "+KollereUtils.TITRE_BOUTIQUE.getValue());
                    prixtbtique.setText(KollereUtils.NUMBER_FORMAT.format(actualObj.get("montantResto").asInt())+"F CFA");
                    prixtkollere.setText(KollereUtils.NUMBER_FORMAT.format(actualObj.get("montantKollere").asInt())+"F CFA");
                    tableitems.getItems().clear();
                    tableitems.getItems().addAll(list_achats);
                    isTicket = false;
                    KollereUtils.TICKET=codevalidation.getText();

                }
                else {
                    erreur.setText(actualObj.get("message").asText()+" !");
                    erreur.setVisible(true);



                }

            }

        }
        else {
            erreur.setText("Veuillez saisir le code du ticket client !");
            erreur.setVisible(true);
        }

    }
    @FXML
    void acheter(ActionEvent event) {

        System.out.println(KollereUtils.TOKEN);
        System.out.println(KollereUtils.TICKET);
        String url =  isTicket ? "http://services.ajit.sn/ws/resto/payedticket?token="+KollereUtils.TOKEN+"&ticket="+KollereUtils.TICKET :"http://services.ajit.sn/ws/resto/payedpanier?token=" + KollereUtils.TOKEN + "&codepanier=" + KollereUtils.TICKET;
        JsonNode actualObj = KollereUtils.callWebservice(url);
        if(actualObj!=null) {
          /*  if(KollereUtils.netIsAvailable())
            {*/
                if (actualObj.get("code").asText().equals("0")) {

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
                  //  KollereUtils.SOLDE.setValue("SOLDE     : "+KollereUtils.NUMBER_FORMAT.format(actualObj.get("montantRestant").asInt())+" F CFA");
                    a.setContentText("Achat bien effectue");
                    a.showAndWait();
                   // erreur.getScene().getWindow().hide();
                    reset();



                } else {
                    a.setAlertType(Alert.AlertType.ERROR);
                    a.setContentText(actualObj.get("message").asText());
                    a.showAndWait();

                    // KollereUtils.notificate(actualObj.get("message").asText(), "Paiement", "erreur");

                }
           /* }
            else {
                erreur.setText("Revoir votre ");
            }*/

        }

        else {
          //  KollereUtils.notificate("Erreurrrrrrrrrrrrrrrrrr!","Erreur","erreur");
          //  System.out.println("erreurrrrrrrr!!!!!!!!!!!!!!!!!!");
        }

    }
    void loadMain() {
        try {
            Parent parent = FXMLLoader.load(getClass().getResource("/views/Dashboard.fxml"));
            Stage stage = new Stage(StageStyle.UNDECORATED);
            stage.setTitle("KOLLERE");
            stage.setScene(new Scene(parent,1150,727));
            stage.setResizable(false);
            stage.setOnCloseRequest((e)->{
                if(KollereUtils.TOKEN!=null)
                {
                    KollereUtils.showAlert("Vous devez vous déconnecter d'abord !!!","Fermeture","erreur");
                    e.consume();
                }

            });
            stage.show();
            KollereUtils.setStageIcon(stage);
        } catch (IOException ex) {
            //Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void reset()
    {
      System.out.println("RESET TICKET");
        detailsContentPane.setVisible(false);
        panierContain.setVisible(false);
        titlePane.setVisible(false);
        codevalidation.setText("");
        prixshop.setText("");
        valitem.setText("");
        valprixshop.setText("");
        valprixk.setText("");
        erreur.setVisible(false);
    }
}

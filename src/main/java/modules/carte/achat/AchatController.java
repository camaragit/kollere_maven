package modules.carte.achat;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.validation.NumberValidator;
import com.jfoenix.validation.RequiredFieldValidator;
import de.jensd.fx.glyphs.GlyphsBuilder;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.TilePane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import org.apache.commons.io.FilenameUtils;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import utils.KollereUtils;
import utils.MonTicket;
import utils.Ticket;


import java.io.*;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Observable;
import java.util.ResourceBundle;

public class AchatController implements Initializable {

    @FXML
    private AnchorPane inscription;
/*
    @FXML
    private AnchorPane holderAnchor;*/

/*    @FXML
    private AnchorPane famille;*/

/*    @FXML
    private ScrollPane root;

    @FXML
    private TilePane tile;*/
  private ObservableList<MonTicket> tabajout;
@FXML
private TableView<MonTicket> tabproduits;
    @FXML
    private Spinner<Integer> quantitetxt;

    @FXML
    private AnchorPane holderAnchor;

   /* @FXML
    private JFXTextField txtprixboutique;*/

    @FXML
    private JFXButton btnsave;
   /* @FXML
    private JFXComboBox<Ticket> moncombo;*/
   @FXML
   private TableColumn<MonTicket, String> produit;

    @FXML
    private TableColumn<MonTicket, String> prixboutique;

    @FXML
    private TableColumn<MonTicket, String> prixkollere;
    @FXML
    private TableColumn<MonTicket, String> quantite;
   @FXML
   private JFXComboBox<String> famille;
   @FXML
   private JFXComboBox<String> items;


    @FXML
    private Label valprixshop;

    @FXML
    private Label valprixk;

   // private JFXTextField txtprixkollere;
    int prixUBoutique,prixUKollere,prixTboutique,prixTkollere;
    @FXML
    private Label valitemprixboutique;
    @FXML
    private Label valitemprixkollere;
    @FXML
    private Label prixitemboutique;

    @FXML
    void acheter(ActionEvent event) {

        if(!tabproduits.getItems().isEmpty())
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
        else KollereUtils.showAlert("Veuillez ajouter au moins un produit ","Achat par Carte","erreur");

    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        valprixk.setText("0");
        valprixshop.setText("0");
        valitemprixboutique.setText("");
        valitemprixkollere.setText("");
        prixTboutique = 0;
        prixTkollere = 0;
        prixitemboutique.setText("Prix "+KollereUtils.TITRE_BOUTIQUE.getValue());

        System.out.println("Le code du panier est "+ KollereUtils.CODEPANIER);
        items.setOnAction(e->{
            quantitetxt.getEditor().textProperty().setValue("1");
            valitemprixboutique.setText("");
            valitemprixkollere.setText("");
            prixTkollere = 0;
            prixTboutique = 0;
            //txtprixkollere.setText("");
            //txtprixboutique.setText("");

            String url = null;
            try {
              //  System
                if(items.getItems()!=null)
                {
                    url = "http://services.ajit.sn/ws/resto/tarifsrestoitem?item="+URLEncoder.encode(items.getValue(), "UTF-8")+"&commerce="+URLEncoder.encode(KollereUtils.TITRE_BOUTIQUE.getValue(), "UTF-8");
                    JsonNode actualObj = KollereUtils.callWebservice(url);
                    System.out.println(actualObj.toString());
                    if(actualObj!=null)
                    {
                        if(actualObj.get("code")!=null && "1".equals(actualObj.get("code").asText()))
                        {

                            KollereUtils.showAlert(actualObj.get("message").asText(),"Releve Prix","erreur");
                        }
                        else{
                            int t= actualObj.size();
                            System.out.println(t);
                            prixUBoutique = prixUKollere = 0;
                            for(int i=0;i< t;i++)
                            {
                                JsonNode val= actualObj.get(i);
                                if(!"".equals(val.get("item").asText())){
                                    prixUBoutique = val.get("valeurItem").get("prixResto").asInt();
                                    prixUKollere = val.get("valeurItem").get("prixKollere").asInt();
                                    valitemprixboutique.setText(KollereUtils.NUMBER_FORMAT.format(prixUBoutique)+"F CFA");
                                    valitemprixkollere.setText(KollereUtils.NUMBER_FORMAT.format(prixUKollere)+" F CFA");
                                    prixTboutique = prixUBoutique;
                                    prixTkollere = prixUKollere;
                                 //   txtprixkollere.setText(KollereUtils.NUMBER_FORMAT.format(prixUKollere));
                                 //   txtprixboutique.setText(String.valueOf(prixUBoutique));
                                  //  txtprixkollere.setText(String.valueOf(prixUKollere));


                                }
                            }
                        }



                    }
                    else KollereUtils.showAlert("Impossible de charger les prix","releve prix","erreur");
                }
            } catch (Exception ex) {
               // ex.printStackTrace();
                System.out.println(ex.getMessage());
            }

        });

        /*System.out.println("controlleur Achat carte");
        String path = System.getProperty("user.dir")+ File.separator+"src"+File.separator+"main"+File.separator+"resources"+File.separator+"images"+File.separator+"familles";
        File folder = new File(path);
        File[] listOfFiles = folder.listFiles();

        for (final File file : listOfFiles) {

            ImageView imageView=null;
            //imageView = createImageView(file);
            Image image = null;
            try {
                image = new Image(new FileInputStream(file), 150, 100, true,
                        true); //new Image(new FileInputStream(file));
                imageView = new ImageView(image);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            String fileNameWithOutExt = FilenameUtils.removeExtension(file.getName());

            Label flow = new Label();
            flow.setTextFill(Color.WHITE);
            flow.setFont(new Font("Times New Roman", 20));
            flow.setStyle("-fx-background-color: #999000; -fx-border-radius: 20px; -fx-border-color: #999000; ");

            flow.setText(fileNameWithOutExt);
            StackPane pane = new StackPane();
            pane.setAlignment(Pos.BOTTOM_CENTER);
            imageView.setFitWidth(150);
           //koller imageView.setFitHeight();
            pane.getChildren().add(imageView);
            pane.getChildren().add(flow);
            pane.setPrefSize(70,50);
           pane.getStyleClass().add("card-white");
            //tile.getChildren().add(pane);
           // tile.getChildren().addAll(pane);

          //  tile.getChildren().add(pane);
        }
*/
        System.out.println("controlleur Achat carte");
        famille.setOnAction((e)->{
            quantitetxt.getEditor().textProperty().setValue("1");
           // txtprixkollere.setText("");
           // txtprixboutique.setText("");
            valitemprixkollere.setText("");
            valitemprixboutique.setText("");
            prixTkollere = 0;
            prixTboutique = 0;

           // items.getItems().clear();
            try {
                if(famille.getValue()!=null){
                    ObservableList<String> lti = FXCollections.observableArrayList();
                    System.out.println(" changement de famille "+famille.getValue());
                    String url = null;

                    http://services.ajit.sn/ws/resto/listitemsfamillecommerce?famille=PLATS&commerce=commerce

                    url = "http://services.ajit.sn/ws/resto/listitemsfamillecommerce?famille="+URLEncoder.encode(famille.getValue(), "UTF-8");
                    url +="&commerce="+URLEncoder.encode(KollereUtils.TITRE_BOUTIQUE.getValue(),"UTF-8");

                    //  System.out.println(u);
                    JsonNode actualObj = KollereUtils.callWebservice(url);
                    System.out.println(actualObj.toString());
                    if(actualObj!=null)
                    {
                        int t= actualObj.size();
                        lti = FXCollections.observableArrayList();
                        System.out.println(t);
                        for(int i=0;i< t;i++)
                        {
                            JsonNode val= actualObj.get(i);
                            lti.add(actualObj.get(i).asText());
                        }
                       // items.getItems().
                       if(items.getItems()!=null)
                       {
                           items.getItems().clear();
                       }
                       items.getItems().addAll(lti);
                        //items.getSelectionModel().selectFirst();
                        Platform.runLater(items::requestFocus);


                    }
                    else KollereUtils.showAlert("Impossible de charger la liste des items","Chargement Item","erreur");
                }

            }
            catch (Exception exp){

            }


        });
        quantitetxt.getEditor().textProperty().addListener((obs, oldValue, newValue) -> {
            if(items.getValue()!=null && !"".equals(newValue)){
                try {
                    int a = Integer.parseInt(newValue);

                        int pk = Integer.parseInt(newValue) * prixUKollere;
                        int pb = Integer.parseInt(newValue) * prixUBoutique;
                        valitemprixboutique.setText(KollereUtils.NUMBER_FORMAT.format(pb)+" F CFA");
                        valitemprixkollere.setText(KollereUtils.NUMBER_FORMAT.format(pk)+" F CFA");
                        prixTkollere = pk;
                        prixTboutique = pb;

                }
                catch (Exception ex){
                    quantitetxt.getEditor().textProperty().setValue("1");

                }


            }
            else {
//                KollereUtils.showAlert("Veuillez selectionner d'abord un produit", "Changement quantite", "erreur");
                quantitetxt.getEditor().textProperty().setValue("1");
            }

        });



    }

    public void chargerlist(ObservableList<String> listeticket){
        items.getItems().clear();
        famille.getItems().clear();
        famille.getItems().addAll(listeticket);
        reload();

    }
    public void reload(){
        KollereUtils.IDCARTE="";
        KollereUtils.CODETICKET="";
        KollereUtils.CODEPANIER =0;
        tabproduits.getItems().clear();
        quantitetxt.getEditor().textProperty().setValue("1");
        valitemprixkollere.setText("");
        valitemprixboutique.setText("");
        prixTboutique = prixUKollere = 0;
        //txtprixboutique.setText("");
       // txtprixkollere.setText("");
        gettotaux();
/*      //  txtprixboutique.setText("");
        //txtprixkollere.setText("");

        ObjectMapper mapper = new ObjectMapper();
        ObservableList<String> lt = FXCollections.observableArrayList();

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
                lt.add(actualObj.get(i).asText());

                System.out.println(val.toString());
*//*
                if(!val.get("item").asText().equals(""))
                    lt.add(new Ticket(val.get("item").asText(),val.get("voucherid").asText(),val.get("valeurItem").get("prixResto").asText(),val.get("valeurItem").get("prixKollere").asText()));
*//*
            }


            this.chargerlist(lt);
        }
        else KollereUtils.showAlert("Impossible de charger la liste des items","Chargement Item","erreur");*/

    }

    public void acheterFunction(){
     //   String url = "http://services.ajit.sn/ws/resto/ticketachatpanier?token=token&nfcid=nfcid&codepanier=codepanier";
        try {
            String url = "http://services.ajit.sn/ws/resto/ticketachatpanier?token="+KollereUtils.TOKEN+"&nfcid="+KollereUtils.IDCARTE+"&codepanier="+KollereUtils.CODETICKET;
            //String url = "http://services.ajit.sn/ws/resto/ticketachat?token="+KollereUtils.TOKEN+"&nfcid="+KollereUtils.IDCARTE+"&voucherid="+moncombo.getValue().getVoucherId()+"&item="+ URLEncoder.encode(moncombo.getValue().getItem(), "UTF-8")+"&montant="+moncombo.getValue().getPrixoutique();
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
                    KollereUtils.SOLDE.setValue("SOLDE :"+KollereUtils.NUMBER_FORMAT.format(actualObj.get("solde").asInt())+" F CFA | CASH : "+cash.get("montantRecu").asText());
                    KollereUtils.showAlert("Achat effectuée avec succès","Validation Achat","infos");
                    reload();
                }
                else KollereUtils.showAlert(actualObj.get("message").asText(),"Achat","erreur");
            }
            else KollereUtils.showAlert("Erreur inconnue \n Veuillez contacter le support","Achat","erreur");

        } catch (Exception e) {
            KollereUtils.showAlert(e.getMessage(),"Encodage","erreur");
            e.printStackTrace();
        }
    }
    @FXML
    void ajouter(ActionEvent event) {
        if(!valitemprixboutique.getText().equals(""))
        {
            try {
                String url = "http://services.ajit.sn/ws/resto/loadingpanier?commerce="+URLEncoder.encode(KollereUtils.TITRE_BOUTIQUE.getValue(), "UTF-8");
                url+="&panier="+ KollereUtils.CODEPANIER+"&item="+URLEncoder.encode(items.getValue(), "UTF-8");
                url+="&prixresto="+prixTboutique+"&prixkollere="+prixTkollere;
                url+="&quantite="+quantitetxt.getValue();
                JsonNode actualObj = KollereUtils.callWebservice(url);
                System.out.println(actualObj.toString());
                if(actualObj!=null)
                {
                    if(actualObj.get("code")!=null && "0".equals(actualObj.get("code").asText()))
                    {
                        KollereUtils.CODEPANIER = actualObj.get("panierid").asInt();
                        KollereUtils.CODETICKET = actualObj.get("codepanier").asText();
                        System.out.println("Le nouveau code du panier  "+ KollereUtils.CODETICKET);
                        tabajout = FXCollections.observableArrayList();
                        MonTicket prod = new MonTicket(items.getValue(),String.valueOf(prixTboutique),String.valueOf(prixTkollere),String.valueOf(quantitetxt.getValue()));
                        tabajout.add(prod);
                        produit.setCellValueFactory(new PropertyValueFactory<>("produit"));
                        quantite.setCellValueFactory(new PropertyValueFactory<>("quantite"));
                        prixboutique.setCellValueFactory(new PropertyValueFactory<>("prixboutique"));
                        prixkollere.setCellValueFactory(new PropertyValueFactory<>("prixkollere"));
                        tabproduits.getItems().addAll(tabajout);
                        gettotaux();
                    }
                    else KollereUtils.showAlert(actualObj.get("message").asText(),"Ajout Panier","erreur");


                }
                else KollereUtils.showAlert("Impossible d'ajouter "+items.getItems()+" au panier","Ajout Panier","erreur");

            }
            catch (Exception e){

            }
        }
        else {
            KollereUtils.showAlert("Veuillez selectionner un produit","Ajout Panier","erreur");
        }





    }


    void gettotaux() {
        int valb =0, valk =0;

        for(int i=0; i<tabproduits.getItems().size();i++)
        {
            valk+= Integer.parseInt(tabproduits.getItems().get(i).getPrixkollere());
            valb+= Integer.parseInt(tabproduits.getItems().get(i).getPrixboutique());

        }
        valprixshop.setText(String.valueOf(valb));
        valprixk.setText(String.valueOf(valk));

    }

    @FXML
    void supprimer(ActionEvent event) {

        MonTicket prod = tabproduits.getSelectionModel().getSelectedItem();
        if(prod!=null){
            try {

                String url = "http://services.ajit.sn/ws/resto/editingpanier?commerce="+URLEncoder.encode(KollereUtils.TITRE_BOUTIQUE.getValue(), "UTF-8");
                url+="&panier="+KollereUtils.CODETICKET+"&item="+URLEncoder.encode(prod.getProduit(), "UTF-8") ;
                url+="&quantite=0";
                JsonNode actualObj = KollereUtils.callWebservice(url);
                System.out.println(actualObj.toString());
                if(actualObj!=null)
                {
                    if(actualObj.get("code")!=null && "0".equals(actualObj.get("code").asText()))
                    {
                        int i = tabproduits.getSelectionModel().getSelectedIndex();
                        tabproduits.getItems().remove(i);
                        gettotaux();
                    }
                    else KollereUtils.showAlert(actualObj.get("message").asText(),"Ajout Panier","erreur");
                }
                else KollereUtils.showAlert("Impossible de supprimer "+prod.getProduit()+" du panier","Suppression dans Panier","erreur");



            }
            catch (Exception ex){

            }

        }
        else System.out.println("OKKKKKK");

    }


}

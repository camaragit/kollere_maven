package Controllers;

import com.jfoenix.controls.*;
import com.jfoenix.effects.JFXDepthManager;
import javafx.animation.FadeTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import modules.carte.achat.AchatController;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import utils.KollereUtils;
import utils.Ticket;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ResourceBundle;


public class DashBoardController implements Initializable{
   // System.out.println("")
    @FXML
    private HBox mainhbox;
    @FXML
    private JFXDrawer drawer;
    @FXML
    private Label erreur;
    @FXML
    private JFXHamburger hamburger;
    @FXML
    private JFXTextField lastname;
    @FXML
    private JFXTextField firstname;
    @FXML
    private JFXTextField idcarte;
    @FXML
    private JFXTextField heure;
    @FXML
    private Pane panea;
    Pane mp;
    Thread t;
    @FXML
    private HBox toolBarRight;

    @FXML
    private Label lblMenu;

    @FXML
    private Label titreboutique;
    @FXML
    private JFXButton btndcnx;
    @FXML
    private JFXButton btnCarte;
    @FXML
    private Label solde;

    @FXML
    private VBox menuCarte;
    @FXML
    private VBox mainvbox;
JFXDepthManager manager;
int niv=5;
    JFXPopup popupc;
    JFXPopup popup;

    @FXML
    private Label agentname;
    AnchorPane codev;
    AnchorPane historique;
    AnchorPane inscription;
    AnchorPane achat;
    AchatController achatController;


    @FXML
    private VBox overflowContainer;
    @FXML
    private JFXButton btnminimize;

    @FXML
    private VBox carteContainer;

    @FXML
    void reduire(ActionEvent event) throws Exception {
        Stage stage= (Stage) btnminimize.getScene().getWindow();
        stage.setIconified(true);

    }
    @FXML
    void deconnecter(ActionEvent event) throws Exception {

        
        killprocess();
//KollereUtils.getMacadress();
       loadLogin();

    }
    @FXML
    void loadticket(ActionEvent event) {

        killprocess();
        KollereUtils.IDCARTE="";
      //  System.out.println("cliquéééé");
        setNode(codev);
    }
@FXML
void loadhistory(ActionEvent event){
    System.out.println(Thread.currentThread().getName());
    killprocess();
    KollereUtils.IDCARTE="";
        setNode(historique);
}
    @FXML
    void loadthome(ActionEvent event) {
        System.out.println(Thread.currentThread().getName());
            killprocess();
        KollereUtils.IDCARTE="";
        setNode(mp);
    }



    @FXML
    void diminuer(ActionEvent event) {
        --niv;
        System.out.println("niveau "+niv);
        manager.setDepth(panea,niv);
    }
public VBox getMainvbox(){
        return this.mainvbox;
}

    public void setMainvbox(VBox mainvbox) {
        this.mainvbox = mainvbox;
    }
    //Set selected node to a content holder
    private void setNode(Node node) {

        mainvbox.getChildren().clear();
        mainvbox.getChildren().add((Node) node);

        if(popupc.isVisible())
            popupc.close();
        if(popup.isVisible())
            popup.close();
        FadeTransition ft = new FadeTransition(Duration.millis(1500));
        ft.setNode(node);
        ft.setFromValue(0.1);
        ft.setToValue(1);
        ft.setCycleCount(1);
        ft.setAutoReverse(false);
        ft.play();
    }

    @Override
public void initialize(URL url, ResourceBundle resourceBundle)   {

    manager = new JFXDepthManager();
    solde.textProperty().bind(KollereUtils.SOLDE);
    titreboutique.textProperty().bind(KollereUtils.TITRE_BOUTIQUE);
    agentname.textProperty().bind(KollereUtils.AGENT_BOUTIQUE);

       JFXRippler fXRippler2 = new JFXRippler(lblMenu);
        fXRippler2.setMaskType((JFXRippler.RipplerMask.RECT));
        toolBarRight.getChildren().add(fXRippler2);

        popup = new JFXPopup();
        popup.setContent(overflowContainer);
       // popup.setContent(test);
      //  popup.setPopupContainer(stackPane);
        popup.setSource(lblMenu);
        lblMenu.setOnMouseClicked((MouseEvent e) -> {
            popup.show(JFXPopup.PopupVPosition.TOP, JFXPopup.PopupHPosition.LEFT, 10, 30);
        });
        popupc = new JFXPopup();
        popupc.setContent(menuCarte);
        popupc.setSource(btnCarte);

        btnCarte.setOnMouseClicked((MouseEvent e) -> {
            //System.out.println("je suis dans setOnmouse.....");
           // popupc.show(JFXPopup.PopupVPosition.TOP, JFXPopup.PopupHPosition.LEFT, 205, 20);
            popupc.show(JFXPopup.PopupVPosition.TOP, JFXPopup.PopupHPosition.LEFT,195,-160);
        });
       // btnCarte.setOnMouseClicked();



   // manager.setDepth(panea,niv);
    try {

        //VBox vBox= FXMLLoader.load(getClass().getResource("menu.fxml"));
       // drawer.setSidePane(vBox);
        FXMLLoader loadermain =new FXMLLoader();
        FXMLLoader loaderticket =new FXMLLoader();
        FXMLLoader loaderhisto =new FXMLLoader();
        FXMLLoader loaderins =new FXMLLoader();
        FXMLLoader loaderachat =new FXMLLoader(getClass().getResource("/views/achat.fxml"));
        mp =  loadermain.load(getClass().getResource("/views/main.fxml"));
        codev =  loaderticket.load(getClass().getResource("/views/AchatTicket.fxml"));
        historique = loaderhisto.load(getClass().getResource("/views/history.fxml"));
        inscription = loaderins.load(getClass().getResource("/views/inscription.fxml"));
        achat = loaderachat.load();
        achatController = (AchatController) loaderachat.getController();
        KollereUtils.IDCARTE="";


        this.setNode(mp);
        //mainvbox.getChildren().add(mp);
    }
    catch (Exception e)
    {
        System.out.println( e.getStackTrace()+"===="+e.getMessage());

    }







}
    @FXML
    void loadAchat(ActionEvent event) {
        System.out.println(Thread.currentThread().getName());
            killprocess();
            KollereUtils.READ =true;
            t = KollereUtils.lectureCarte();
            t.start();
            KollereUtils.IDCARTE="";


        ObjectMapper mapper = new ObjectMapper();
        ObservableList<Ticket> lti = FXCollections.observableArrayList();

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
                lti = FXCollections.observableArrayList();
                System.out.println(t);
                for(int i=0;i< t;i++)
                {
                    JsonNode val= actualObj.get(i);

                    System.out.println(val.toString());
                    if(!val.get("item").asText().equals(""))
                        lti.add(new Ticket(val.get("item").asText(),val.get("voucherid").asText(),val.get("valeurItem").get("prixResto").asText(),val.get("valeurItem").get("prixKollere").asText()));
                }


                achatController.chargerlist(lti);
                setNode(achat);
            }
            else KollereUtils.showAlert("Impossible de charger la liste des items","Chargement Item","erreur");


    }

    @FXML
    void loadInscription(ActionEvent event) {

        killprocess();
        KollereUtils.READ = true;
        t = KollereUtils.lectureCarte();
        t.start();
        System.out.println("DANS INSCRIPTIP");
        KollereUtils.IDCARTE="";

        setNode(inscription);

    }
    @FXML

    public void valider(){
     //   System.out.println("Vous avez saisi comme login ===>" +login.getText()+"\nEt comme mot de passe====>"+mdp.getText());


    }

    void loadLogin() {
        KollereUtils.IDCARTE="";
        killprocess();
        String url ="http://services.ajit.sn/ws/resto/disconnection?token="+KollereUtils.TOKEN;
        JsonNode actualObj = KollereUtils.callWebservice(url);
         if(actualObj.get("code").asText().equals("0"))
        {
            try {
                btndcnx.getScene().getWindow().hide();
                Parent parent = FXMLLoader.load(getClass().getResource("/views/login.fxml"));
                Stage stage = new Stage();
                stage.initStyle(StageStyle.UNDECORATED);
                stage.setOnCloseRequest((e)->{
                    if(KollereUtils.TOKEN!=null)
                        e.consume();
                });
                KollereUtils.TOKEN=null;
                stage.setTitle("KOLLERE");
                stage.setScene(new Scene(parent,800,550));
                stage.setResizable(false);
                stage.show();

                KollereUtils.setStageIcon(stage);
            } catch (IOException ex) {
                //Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        else{
                KollereUtils.showAlert("Impossible de se deconnecter. Veuillez réessayer","Déconnexion","erreur");
         }

    }
    @FXML
    private void exit(ActionEvent event) {

        KollereUtils.exitApp();
    }

    public void killprocess() {
        if (t != null && t.getState().name().equals("RUNNABLE")) {
            t.interrupt();

        }
        KollereUtils.READ = false;
        KollereUtils.IDCARTE ="";
    }


}

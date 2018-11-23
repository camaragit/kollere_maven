package modules.login;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXSpinner;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.codehaus.jackson.JsonNode;
import utils.KollereUtils;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable {
    @FXML
    private JFXTextField login;

    @FXML
    private JFXPasswordField motdp;
    @FXML
    private JFXSpinner loadercnx;

    @FXML
    private Label erreurlogin;
    @FXML
    private JFXButton btnexit;
    @FXML
    void connexion(ActionEvent event) {
  /*      try {
            KollereUtils.getMacadress();
        } catch (Exception e) {
            e.printStackTrace();
        }*/
        erreurlogin.setText("");
        if(login.getText().equals("")){
            erreurlogin.setText("Veuillez saisir votre identifiant !");
        }
        else if(motdp.getText().equals("")){
            erreurlogin.setText("Veuillez saisir votre mot de passe !");
        }
        else{

           /* if(KollereUtils.netIsAvailable())
            {*/
           String url ="http://services.ajit.sn/ws/resto/connection?pin="+motdp.getText()+"&imei="+login.getText();
          // System.out.println(url);
                JsonNode actualObj = KollereUtils.callWebservice(url);//mapper.readTree(val);

                if(actualObj!=null)
                {
                    if(!actualObj.get("code").asText().equals("0"))
                    {
                        erreurlogin.setText(actualObj.get("message").asText()+" !");
                    }
                    //Recuperer le solde puis se connecter
                    else{
                        String urlsolde = "http://services.ajit.sn/ws/resto/ticketconsultation?token="+actualObj.get("token").asText()+"&nfcid=2181e5b5&voucherid=1";
                        JsonNode soldeobj = KollereUtils.callWebservice(urlsolde);
                        if(soldeobj==null)
                        {
                            erreurlogin.setText("Impossible de recuperer le solde");
                            return;
                        }
                        if(!soldeobj.get("code").asText().equals("0"))
                        {
                            erreurlogin.setText(soldeobj.get("message").asText()+" !");
                            return;
                        }
                        else{

                            KollereUtils.TITRE_BOUTIQUE.setValue(actualObj.get("nomresto").asText().toUpperCase());
                            KollereUtils.AGENTID = actualObj.get("agentid").asText();

                            String urlcash ="http://services.ajit.sn/ws/resto/bilan?agentid="+KollereUtils.AGENTID+"&jour=jour&mois=mois&annee=annee";

                            JsonNode cash = KollereUtils.callWebservice(urlcash);
                            if(cash==null)
                            {
                                erreurlogin.setText("Impossible de recuperer le cash agent");
                                return;
                            }
                            if(!cash.get("code").asText().equals("0"))
                            {
                                erreurlogin.setText(cash.get("message").asText()+" !");
                            }
                            //Si Maxifood
                            if(KollereUtils.AGENTID.equals("778"))
                            {
                                KollereUtils.SOLDE.setValue("SOLDE :"+cash.get("solde").asText()+" F CFA | CASH : "+cash.get("montantRecu").asText());

                            }
                            else KollereUtils.SOLDE.setValue("SOLDE :"+KollereUtils.NUMBER_FORMAT.format(soldeobj.get("montantRestant").asInt())+" F CFA | CASH : "+cash.get("montantRecu").asText());

                            KollereUtils.TOKEN =actualObj.get("token").asText();
                            KollereUtils.AGENT_BOUTIQUE.setValue(actualObj.get("prenomagent").asText());
                            erreurlogin.getScene().getWindow().hide();
                            loadMain();
                        }

                    }

                    loadercnx.setVisible(false);
                }
                else erreurlogin.setText("Erreur non traitée veuillez contacter le support !");



        }

    }

    @FXML
    void quitter(ActionEvent event) {

        KollereUtils.exitApp();
    }
    @FXML
    void reduire(ActionEvent event) {
        Stage stage= (Stage) btnexit.getScene().getWindow();
        stage.setIconified(true);

    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        KollereUtils.TOKEN=null;
        System.out.println("controlleur Login");


    }

    void loadMain() {
        try {
            Parent parent = FXMLLoader.load(getClass().getResource("/views/Dashboard.fxml"));
            Stage stage = new Stage(StageStyle.UNDECORATED);
            stage.setTitle("KOLLERE");
            stage.setScene(new Scene(parent,1065,727));
            stage.setResizable(false);
            stage.setOnCloseRequest((e)->{
                if(KollereUtils.TOKEN!=null)
                {
                    KollereUtils.showAlert("Vous devez vous déconnecter d'abord !!","Fermeture","erreur");
                    e.consume();
                }

            });
            stage.show();
            KollereUtils.setStageIcon(stage);
        } catch (IOException ex) {
            KollereUtils.showAlert(ex.getMessage(),"Main loading","erreur");
        }
    }

}

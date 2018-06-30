package modules.carte.inscription;

import com.jfoenix.controls.JFXTextField;
import com.jfoenix.validation.RequiredFieldValidator;

import de.jensd.fx.glyphs.GlyphsBuilder;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import nfctools.NfcTools;
import org.codehaus.jackson.JsonNode;
import utils.KollereUtils;

import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

public class InscriptionController implements Initializable {
    private static final String EM1 = "1em";
    private static final String ERROR = "error";
    @FXML
    private AnchorPane inscription;

    @FXML
    private AnchorPane holderAnchor;

    @FXML
    private JFXTextField txtFname;

    @FXML
    private JFXTextField txtLname;

    @FXML
    private JFXTextField txtMobile;

    @FXML
    private JFXTextField txtEmail;
    @FXML
    private FontAwesomeIconView warntel;

    @FXML
    private FontAwesomeIconView warpnom;

    @FXML
    private FontAwesomeIconView warnname;

    @FXML
    private FontAwesomeIconView warnmail;
    @FXML
    private Label lmail;

    @FXML
    private Label ltel;

     @FXML
    private Label lpname;
    @FXML
    private Label lname;



    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("controlleur inscription");

    txtFname.focusedProperty().addListener((arg,old,nouv)->{
        warpnom.setVisible(false);
        lpname.setText("");
        if(!nouv){
            if(txtFname.getText().equals(""))
            {
                lpname.setText("Veuillez renseigner le prénom du client");
                warpnom.setVisible(true);
            }


        }
    });
        txtEmail.focusedProperty().addListener((arg0, oldValue, newValue) -> {
            lmail.setText("");
            warnmail.setVisible(false);
            if (!newValue) {
                if(!txtEmail.getText().matches("^[a-zA-Z0-9_!#$%&’*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$")){
                  lmail.setText("Veuillez renseigner un format de mail valide");
                  warnmail.setVisible(true);
                }

            }
        });
        txtLname.focusedProperty().addListener((arg,old,nouv)->{
            warnname.setVisible(false);
            lname.setText("");
            if(!nouv){
                if(txtLname.getText().equals(""))
                {
                    lname.setText("Veuillez renseigner le nom du client");
                    warnname.setVisible(true);
                }


            }
        });
        txtMobile.focusedProperty().addListener((arg,old,nouv)->{
            warntel.setVisible(false);
            ltel.setText("");
            if(!nouv){

                if(txtMobile.getText().equals("") || !KollereUtils.verifTel(txtMobile.getText()))
                {
                    ltel.setText("Veuiller renseigner un format numero de telephone valide");
                    warntel.setVisible(true);
                }


            }
        });

    }


    @FXML
    void inscrire(ActionEvent event) {
        lname.setText(""); ltel.setText(""); ltel.setText(""); lmail.setText("");
        warntel.setVisible(false); warnname.setVisible(false); warpnom.setVisible(false); warnmail.setVisible(false);

            if(txtFname.getText().equals(""))
        {
            lpname.setText("Veuillez renseigner le prénom du client");
            warpnom.setVisible(true);
        } else {
                if(txtLname.getText().equals(""))
                {
                    lname.setText("Veuillez renseigner le nom du client");
                    warnname.setVisible(true);
                }
                else{
                    if(txtMobile.getText().equals("") || !KollereUtils.verifTel(txtMobile.getText()))
                    {
                        ltel.setText("Veuiller revoir le format du numero de telephone");
                        warntel.setVisible(true);
                    }
                    else{
                        if(!txtEmail.getText().matches("^[a-zA-Z0-9_!#$%&’*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$")){
                            lmail.setText("Veuillez renseigner un format de mail valide");
                            warnmail.setVisible(true);
                        }
                        else{
                            System.out.println("carte pas encore recuperée");
                            if(KollereUtils.IDCARTE.equals("")) {
                                if(KollereUtils.lireidnfc())
                                {
                                    inscription();
                                }


                            }
                            else {
                                System.out.println("carte deja recuperée");
                                inscription();
                            }


                        }
                    }
                }
            }


    }

    public  void inscription()
    {
        try {
            String url = "http://services.ajit.sn/ws/resto/inscription?nfcid="+KollereUtils.IDCARTE+"&nom="+URLEncoder.encode(txtLname.getText(),"UTF-8")+"&prenom="+URLEncoder.encode(txtFname.getText(),"UTF-8")+"&email="+txtEmail.getText()+"&numtel="+txtMobile.getText();
            JsonNode actualObj = KollereUtils.callWebservice(url);
            if(actualObj!=null)
            {
                if(actualObj.get("code").asText().equals("0"))
                {
                    KollereUtils.showAlert(actualObj.get("message").asText(),"Inscription","infos");
                    reset();
                }
                else KollereUtils.showAlert(actualObj.get("message").asText(),"Inscription","erreur");

            }
            else KollereUtils.showAlert("Erreur Inconnue.Veuillez contacter le support","Inscription","erreur");



        } catch (UnsupportedEncodingException e) {
            KollereUtils.showAlert(e.getMessage(),"Encodage","erreur");
            //e.printStackTrace();
        }
    }
    public void reset(){
        KollereUtils.IDCARTE="";
        txtMobile.setText("");
        txtEmail.setText("");
        txtLname.setText("");
        txtFname.setText("");
        warnmail.setVisible(false);
        warnname.setVisible(false);
        warntel.setVisible(false);
        warpnom.setVisible(false);
        lmail.setText("");
        lname.setText("");
        lpname.setText("");
        ltel.setText("");

    }

}

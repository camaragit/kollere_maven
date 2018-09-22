package modules.carte.inscription;

import com.jfoenix.controls.JFXRadioButton;
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
import javafx.scene.control.Spinner;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
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
    private Label lmail;

    @FXML
    private Label ltel;

     @FXML
    private Label lpname;
    @FXML
    private Label lname;
    @FXML
    private Label ladresse;
    @FXML
    private Label lprofession;


    @FXML
    private JFXRadioButton feminin;
    final ToggleGroup group = new ToggleGroup();
    @FXML
    private JFXRadioButton masculin;

    @FXML
    private JFXTextField adresse;

    @FXML
    private JFXTextField profession;

    @FXML
    private Spinner<Integer> age;
    String genre ="";



    @Override
    public void initialize(URL location, ResourceBundle resources) {

        System.out.println("controlleur inscription");
        masculin.setToggleGroup(group);
        feminin.setToggleGroup(group);
        masculin.setSelectedColor(Color.valueOf("#999900"));
        feminin.setSelectedColor(Color.valueOf("#999900"));
        txtFname.focusedProperty().addListener((arg,old,nouv)->{
        lpname.setText("");
        if(!nouv){
            if(txtFname.getText().equals(""))
            {
                lpname.setText("Veuillez renseigner le prénom du client");
                //warpnom.setVisible(true);
            }


        }
    });
        txtEmail.focusedProperty().addListener((arg0, oldValue, newValue) -> {
            lmail.setText("");
            //warnmail.setVisible(false);
            if (!newValue) {
                if(!txtEmail.getText().matches("^[a-zA-Z0-9_!#$%&’*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$")){
                  lmail.setText("Veuillez renseigner un format de mail valide");
                  //warnmail.setVisible(true);
                }

            }
        });
        txtLname.focusedProperty().addListener((arg,old,nouv)->{
            //warnname.setVisible(false);
            lname.setText("");
            if(!nouv){
                if(txtLname.getText().equals(""))
                {
                    lname.setText("Veuillez renseigner le nom du client");
                    //warnname.setVisible(true);
                }


            }
        });
        txtMobile.focusedProperty().addListener((arg,old,nouv)->{
            //warntel.setVisible(false);
            ltel.setText("");
            if(!nouv){

                if(txtMobile.getText().equals("") || !KollereUtils.verifTel(txtMobile.getText()))
                {
                    ltel.setText("Veuiller renseigner un format numero de telephone valide");
                    //warntel.setVisible(true);
                }


            }
        });

        adresse.focusedProperty().addListener((arg,old,nouv)->{
            ladresse.setText("");
            if(!nouv){
                if(adresse.getText().equals(""))
                    ladresse.setText("Veuillez renseigner l'adresse");
            }
        });
        profession.focusedProperty().addListener((arg,old,nouv)->{
            lprofession.setText("");
            if(!nouv){
                if(profession.getText().equals(""))
                    lprofession.setText("Veuillez renseigner la profession");
            }
        });
        age.focusedProperty().addListener((arg,old,nouv)->{

        });
        group.selectedToggleProperty().addListener((arg,old,nouv)->{
            JFXRadioButton selected = (JFXRadioButton) nouv;
            if(nouv!=null)
            genre = selected.getText().equals("Masculin") ? "M" :"F";

        });

    }


    @FXML
    void inscrire(ActionEvent event) {

        lname.setText(""); ltel.setText(""); ltel.setText(""); lmail.setText("");
        //warntel.setVisible(false); warnname.setVisible(false); warpnom.setVisible(false); warnmail.setVisible(false);

            if(txtFname.getText().equals(""))
        {
            lpname.setText("Veuillez renseigner le prénom du client");
            //warpnom.setVisible(true);
        } else {
                if(txtLname.getText().equals(""))
                {
                    lname.setText("Veuillez renseigner le nom du client");
                    //warnname.setVisible(true);
                }
                else{
                    if(txtMobile.getText().equals("") || !KollereUtils.verifTel(txtMobile.getText()))
                    {
                        ltel.setText("Veuiller revoir le format du numero de telephone");
                        //warntel.setVisible(true);
                    }
                    else{
                        if(!txtEmail.getText().matches("^[a-zA-Z0-9_!#$%&’*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$")){
                            lmail.setText("Veuillez renseigner un format de mail valide");
                            //warnmail.setVisible(true);
                        }
                        else{
                            if(adresse.getText().equals(""))
                                ladresse.setText("Veuillez renseigner l'adresse");
                            else
                                if(genre.equals(""))
                                    lmail.setText("Veuillez renseigner le sexe");
                            else
                                if(profession.getText().equals(""))
                                    lprofession.setText("Veuillez renseigner la profession");
                            else
                                if(KollereUtils.IDCARTE.equals("")){
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

            url +="&genre="+genre+"&age="+age.getValue()+"&quartier="+URLEncoder.encode(adresse.getText(),"UTF-8")+"&profession="+URLEncoder.encode(profession.getText(),"UTF-8");
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
        //warnmail.setVisible(false);
        //warnname.setVisible(false);
        //warntel.setVisible(false);
        //warpnom.setVisible(false);
        lmail.setText("");
        lname.setText("");
        lpname.setText("");
        ltel.setText("");
        profession.setText("");
        adresse.setText("");
        age.getEditor().setText("1");
        feminin.setSelected(false);
        masculin.setSelected(false);
    }

}

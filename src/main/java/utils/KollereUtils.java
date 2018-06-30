package utils;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.util.Duration;
import nfctools.NfcTools;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.controlsfx.control.Notifications;

import javax.smartcardio.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;
import java.io.IOException;
import java.io.PrintStream;
import java.net.*;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

public class KollereUtils {
    public static final String IMAGE_LOC = "/images/logo_KOLLERE.png";
    public static  String TOKEN;
    public static  String AGENTID;
    public static  Boolean READ=false;
    public static PrintStream OUTPUTFILE;
    public static final NumberFormat NUMBER_FORMAT = NumberFormat.getInstance(java.util.Locale.FRENCH);
    //public static  String SOLDE;
    public static  String TICKET;
    public static  String IDCARTE="";
    public static  String CODETICKET="";
    public static int CODEPANIER;
    public static  StringProperty TITRE_BOUTIQUE = new SimpleStringProperty();
    public static  StringProperty AGENT_BOUTIQUE = new SimpleStringProperty();
    public static StringProperty SOLDE = new SimpleStringProperty();
    public static void setStageIcon(Stage stage) {
        stage.getIcons().add(new Image(IMAGE_LOC));
    }

    public static void notificate(String message, String title,String type) {

        Notifications infos = Notifications.create()
                .title(title).darkStyle().graphic(new ImageView(IMAGE_LOC)).hideAfter(Duration.seconds(10))
                .text(message).position(Pos.CENTER);
        Notifications erreur = Notifications.create()
                .title(title).hideAfter(Duration.seconds(10))
                .text(message).position(Pos.CENTER);
        if(type.equals("erreur"))
            erreur.showError();
        else
            infos.showInformation();

        //infos.show();


    }

    public static JsonNode callWebservice(String url) {
        try {

          //  System.setOut(KollereUtils.OUTPUTFILE);
            SimpleDateFormat d = new SimpleDateFormat("EEEE dd MMMM yyyy HH:mm:ss");
            Date date = new Date();

            System.out.println("********************Appel de "+url+"********************* à "+d.format(date));
            Client client = Client.create(new DefaultClientConfig());

            URI uri = UriBuilder.fromUri(url).build();
            //WebResource.Builder webResource = client.resource(uri).post(WebResource.class).header("","").accept(MediaType.APPLICATION_JSON);
            //ClientResponse clientResponse = client.resource(uri).post(ClientResponse.class);
            ClientResponse clientResponse = client.resource(uri).header("type_requete","DESKTOP").post(ClientResponse.class);
            String val = clientResponse.getEntity(String.class);
            //String val = webResource.get(String.class);
            System.out.println("********* Reponse ======>"+val+"***********************");
            ObjectMapper mapper = new ObjectMapper();
            JsonNode actualObj = mapper.readTree(val);
            return actualObj;
        } catch (Exception e) {
            //System.setOut(KollereUtils.OUTPUTFILE);
            System.out.println("erreur "+e.getMessage());
            return null;

        }
    }

    public static void showAlert(String message,String title,String type){
        Alert a = new Alert(type.equals("infos")? Alert.AlertType.INFORMATION: Alert.AlertType.ERROR);
        a.setTitle(title);
        a.setHeaderText("KOLLERE");

        a.setHeaderText("");
        a.setContentText(message);
        Stage stage = (Stage) a.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image(KollereUtils.IMAGE_LOC));
        a.showAndWait();

       // Optional<ButtonType> result =
       // System.out.println(result.get());

    }
    public static boolean netIsAvailable() {
        try {
            final URL url = new URL("http://www.google.com");
            final URLConnection conn = url.openConnection();
            conn.connect();
            return true;
        } catch (MalformedURLException e) {
            return false;
        } catch (IOException e) {
            return false;
        }
    }
    public static void getMacadress() throws Exception{
        InetAddress address = InetAddress.getLocalHost();

        NetworkInterface ni = NetworkInterface.getByInetAddress(address);
        byte[] mac = ni.getHardwareAddress();


        for (int i = 0; i < mac.length; i++) {
            System.out.format("%02X%s", mac[i], (i < mac.length - 1) ? "-" : "");
        }
    }
    public static void exitApp(){
        if(TOKEN!=null){
            String url ="http://services.ajit.sn/ws/resto/disconnection?token="+TOKEN;
            JsonNode actualObj = KollereUtils.callWebservice(url);
            if(actualObj!=null)
            {
                if(actualObj.get("code").asText().equals("0")) {
                    Platform.exit();
                    System.exit(0);
                }
                else{
                    showAlert("Déconnexion impossible. Veuillez réssayer","Fermeture","erreur");
                }
            }
            else {
                showAlert("Erreur inconnue\n Veuillez contacter le support","Fermeture","erreur");

            }

        }
        else {
            Platform.exit();
            System.exit(0);

        }


    }
    public static Thread lectureCarte(){
      return   new Thread(()->{
            try{

                while (true && KollereUtils.READ) {
                    TerminalFactory factory = TerminalFactory.getDefault();
                    CardTerminals terminals = factory.terminals();
                    for (CardTerminal terminal : terminals.list(CardTerminals.State.CARD_INSERTION)) {
                        Card card;
                        card = terminal.connect("*");
                        CardChannel channel = card.getBasicChannel();
                        // Send test command
                        ResponseAPDU response = channel.transmit(new CommandAPDU( new byte[] { (byte) 0xFF, (byte) 0xCA, (byte) 0x00, (byte) 0x00, (byte) 0x00 }));
                        System.out.println("Response: " + response.toString());
                        if (response.getSW1() == 0x63 && response.getSW2() == 0x00)  System.out.println("Echec de la commande");
                        System.out.println("carte id==>"+NfcTools.bin2hex(response.getData()));
                        System.out.println("carte sans "+response.getData());
                        String idcrt = NfcTools.bin2hex(response.getData());
                        String c =response.getData().toString();
                        System.out.println("id trouve"+idcrt);

                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                KollereUtils.IDCARTE = idcrt;
                              //  IDCARTE =idcrt;
                                notificate("ID carte recuperé avec succès ","Id Carte","info");
                            }
                        });

                    }
                    terminals.waitForChange();
                }

            }
            catch (Exception c){

                if(c.getMessage().equals("list() failed") || c.getMessage().equals("connect() failed"))
                    //erreur.setText("Impossible de se connecter au lecteur.Reconnectez l'appareil et assurez vous que les voyants sonts allumés puis relancer l'application");
             System.out.println(c.getMessage());

            }

        });
    }

    public static Boolean verifTel(String telephone){
        String recup = telephone.replaceAll(" ","");
        System.out.println(recup);
        List<String> tab = Arrays.asList("77", "78", "76", "70");
        return recup.length()==9 && tab.contains(recup.substring(0,2)) ;
    }


    public static Boolean lireidnfc(){
     //   Map<Integer,String> retour = new HashMap<>();
        try {
            TerminalFactory factory = TerminalFactory.getDefault();
            List<CardTerminal> terminals = factory.terminals().list();
           // System.out.println("Terminals: " + terminals.get(0));
            CardTerminal terminal = terminals.get(0);

            // Connect with the card
            Card card = terminal.connect("*");
           // System.out.println("Card: " + card);
            CardChannel channel = card.getBasicChannel();


            // Send test command
            ResponseAPDU response = channel.transmit(new CommandAPDU( new byte[] { (byte) 0xFF, (byte) 0xCA, (byte) 0x00, (byte) 0x00, (byte) 0x00 }));
            System.out.println("Response: " + response.toString());
            if (response.getSW1() == 0x63 && response.getSW2() == 0x00){
                System.out.println("Echec de la commande");
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        showAlert("Verifiez que le lecteur est actif","Lecture Carte","erreur");
                    }
                });
                return false;
            }

                String idcrt = NfcTools.bin2hex(response.getData());
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        IDCARTE =idcrt;
                        notificate("L'id de la carte est récuperé avec succès ","Id Carte","info");
                    }
                });

          ///  Platform.ru
         //   retour.put(0,bin2hex(response.getData()));
            return true;

        }
        catch (Exception e){
            if(e.getMessage().equals("No card present"))
            {

                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        showAlert("Pas de carte sur le lecteur!","Lecture Carte","erreur");
                    }
                });
               return false;
            }
            else if(e.getMessage().equals("list() failed")) {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        showAlert("Veuillez inserer un lecteur nfc !","Lecture Carte","erreur");
                    }
                });
                return false;


            }
            else {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        showAlert(e.getMessage(),"Lecture Carte","erreur");
                    }
                });
                return false;

            }
        }
    }

}

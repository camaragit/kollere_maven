package nfctools;


import javax.smartcardio.*;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Dame on 15/01/2018.
 */


public class NfcTools {


   public    static String bin2hex(byte[] data) {
        return String.format("%0" + (data.length * 2) + "X", new BigInteger(1,data));
    }
    public static Map<Integer,String> lireidnfc(){
        Map<Integer,String> retour = new HashMap<>();
        try {
            TerminalFactory factory = TerminalFactory.getDefault();
            List<CardTerminal> terminals = factory.terminals().list();
            System.out.println("Terminals: " + terminals.get(0));
            CardTerminal terminal = terminals.get(0);

            // Connect with the card
            Card card = terminal.connect("*");
            System.out.println("Card: " + card);
            CardChannel channel = card.getBasicChannel();


            // Send test command
            ResponseAPDU response = channel.transmit(new CommandAPDU( new byte[] { (byte) 0xFF, (byte) 0xCA, (byte) 0x00, (byte) 0x00, (byte) 0x00 }));
            System.out.println("Response: " + response.toString());
            if (response.getSW1() == 0x63 && response.getSW2() == 0x00)  System.out.println("Echec de la commande");
            retour.put(0,bin2hex(response.getData()));
            return retour;

        }
        catch (Exception e){
            if(e.getMessage().equals("No card present"))
            {
                System.out.println("Pas de carte sur le lecteur!");
                retour.put(1,("Pas de carte sur le lecteur!"));
            }
            else if(e.getMessage().equals("list() failed")) {
                System.out.println("Veuillez inserer un lecteur nfc !");
                retour.put(1,("Veuillez inserer un lecteur nfc !"));
            }
            else {
                System.out.println(e.getMessage());
                retour.put(1,e.getMessage());
            }
            return retour;
        }
    }
}

package modules.carte.inscription;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Client {
    private String nom;
    private String prenom;
    private String email;
    private String telephone;
    private String nfcid;

}

package Controllers;

import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import static javafx.fxml.FXMLLoader.*;

public class MenuControlleur implements Initializable {

    @FXML
    private VBox menu;

    @FXML
    private JFXButton btnHome;

    @FXML
    void loadticket(ActionEvent event) throws IOException {

        Pane pane = load(getClass().getResource("ticket.fxml"));
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("DashBoard.fxml"));
       Parent root = (Parent) loader.load();
        DashBoardController dashBoardController =loader.getController();
        //System.out.println(dashBoardController);
       // dashBoardController.setNode(pane);
        //menu.getChildren().setAll(root);

       // btnHome.getScene().getWindow().hide();
       // Stage st =new Stage();
      // st.setScene(new Scene(root));
    //    st.show();
        //dashBoardController.getMainvbox().getChildren().clear();
        //loader.setLocation(this.class.getResource("view/RootLayout.fxml"));
       // ctr.getMainvbox().getChildren().removeAll();
       // ctr.getMainvbox().getChildren().add(pane);

    }

    public void initialize(URL location, ResourceBundle resources) {

    }

}

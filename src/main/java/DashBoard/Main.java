package DashBoard;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import utils.KollereUtils;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{

        Parent root = FXMLLoader.load(getClass().getResource("/views/login.fxml"));
        primaryStage.setTitle("KOLLERE");
        //primaryStage.getIcons().add(new Image("/images/logo_KOLLERE.png"));
        KollereUtils.setStageIcon(primaryStage);
        primaryStage.setResizable(false);
        primaryStage.initStyle(StageStyle.UNDECORATED);
        primaryStage.setOnCloseRequest((e)->{
            if(KollereUtils.TOKEN!=null)
                e.consume();
        });
        primaryStage.setScene(new Scene(root, 800, 550));
        primaryStage.show();
    }


    @Override
    public void stop() throws Exception {
        super.stop();

        System.out.println("fermeture main");
    }

    public static void main(String[] args) {
/*        try {
            File file = new File("A.txt");
            if(!file.exists())
                file.createNewFile();
            KollereUtils.OUTPUTFILE = new PrintStream(new FileOutputStream("traces.txt", true));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }*/
        launch(args);
    }
}

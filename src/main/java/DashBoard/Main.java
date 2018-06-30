package DashBoard;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import utils.KollereUtils;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.apache.commons.io.FilenameUtils;

public class Main extends Application {
    Stage stage;
/*    @Override
    public void start(Stage stage) {
        Scene scene = new Scene(new Group());
        stage.setTitle("Label With Image Sample");
        stage.setWidth(400);
        stage.setHeight(180);

        HBox hbox = new HBox();
        //Replace the image you want to put up
        String path = "."+File.separator+"src"+File.separator+"main"+File.separator+"resources"+File.separator+"images"+File.separator+"familles";//"/home/ubuntu/eclipse with liferay/Desktop/imagetest/";

        File folder = new File(path);
        File[] listOfFiles = folder.listFiles();

      *//*  for (final File file : listOfFiles) {
            ImageView imageView;
            imageView = createImageView(file);
            tile.getChildren().addAll(imageView);
        }*//*
      File imageFile = listOfFiles[0];
        Image image = null;
        try {
            image = new Image(new FileInputStream(imageFile));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        //Image image = new Image(getClass().getResourceAsStream("a.png"));
        Label label = new Label("Demo Label");
        label.setGraphic(new ImageView(image));
        hbox.setSpacing(10);
        hbox.getChildren().add((label));
        ((Group) scene.getRoot()).getChildren().add(hbox);

        stage.setScene(scene);
        stage.show();
    }*/

/*    @Override
    public void start(Stage primaryStage) throws Exception {
        stage = primaryStage;
        ScrollPane root = new ScrollPane();
        TilePane tile = new TilePane();
       root.setStyle("-fx-background-color: blue;");
        tile.setPadding(new Insets(15, 15, 15, 15));
        tile.setHgap(15);

        String path = "."+File.separator+"src"+File.separator+"main"+File.separator+"resources"+File.separator+"images"+File.separator+"familles";//"/home/ubuntu/eclipse with liferay/Desktop/imagetest/";

        File folder = new File(path);
        File[] listOfFiles = folder.listFiles();

        for (final File file : listOfFiles) {

            ImageView imageView;
            imageView = createImageView(file);
            Image image = new Image(new FileInputStream(file));
                   Label label = new Label("Demo Label");
                   label.setGraphic(new ImageView(image));
            String fileNameWithOutExt = FilenameUtils.removeExtension(file.getName());
            Text inftx = new Text(fileNameWithOutExt);
            inftx.setFont(new Font("Times New Roman", 20));
            inftx.setFill(Color.WHITE);
            Label flow = new Label();
            flow.setTextFill(Color.WHITE);
            flow.setFont(new Font("Times New Roman", 20));
            flow.setStyle("-fx-background-color: #999000; -fx-border-radius: 20px; -fx-border-color: #999000; ");

            flow.setText(fileNameWithOutExt);
            //inftx.setStyle("-rtfx-background-color:red;");
         //   inftx.setStyle("");

            StackPane pane = new StackPane();

            pane.getChildren().add(imageView);
            pane.getChildren().add(flow);
            //pane.getChildren().add(label);

            pane.setAlignment(Pos.BOTTOM_CENTER);
           // tile.getChildren().addAll(imageView);
            tile.getChildren().addAll(pane);
          //  tile.getChildren().addAll(label);
        }


        root.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER); // Horizontal
        root.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED); // Vertical scroll bar
        root.setFitToWidth(true);
        root.setContent(tile);

        primaryStage.setWidth(Screen.getPrimary().getVisualBounds().getWidth());
        primaryStage.setHeight(Screen.getPrimary().getVisualBounds()
                .getHeight());
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();

    }*/

    private ImageView createImageView(final File imageFile) {
        // DEFAULT_THUMBNAIL_WIDTH is a constant you need to define
        // The last two arguments are: preserveRatio, and use smooth (slower)
        // resizing

        ImageView imageView = null;
        try {
            final Image image = new Image(new FileInputStream(imageFile), 150, 0, true,
                    true);
            imageView = new ImageView(image);
            imageView.setFitWidth(150);
            imageView.setOnMouseClicked(new EventHandler<MouseEvent>() {

                @Override
                public void handle(MouseEvent mouseEvent) {
                    System.out.println("OKKKKK");

/*                    if(mouseEvent.getButton().equals(MouseButton.PRIMARY)){

                        if(mouseEvent.getClickCount() == 1){
                            try {
                                BorderPane borderPane = new BorderPane();
                                ImageView imageView = new ImageView();
                                Image image = new Image(new FileInputStream(imageFile));
                                imageView.setImage(image);
                                imageView.setStyle("-fx-background-color: BLACK");
                                imageView.setFitHeight(stage.getHeight() - 10);
                                imageView.setPreserveRatio(true);
                                imageView.setSmooth(true);
                                imageView.setCache(true);
                                borderPane.setCenter(imageView);
                                borderPane.setStyle("-fx-background-color: BLACK");
                                Stage newStage = new Stage();
                                newStage.setWidth(stage.getWidth());
                                newStage.setHeight(stage.getHeight());
                                newStage.setTitle(imageFile.getName());
                                Scene scene = new Scene(borderPane, Color.BLACK);
                                newStage.setScene(scene);
                                newStage.show();
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            }

                        }
                    }*/
                }
            });
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }
        return imageView;
    }

  /*  public static void main(String[] args) {
        launch(args);
    }*/






    @Override
    public void start(Stage primaryStage) throws Exception{
        System.out.println("repertoire ===>"+System.getProperty("user.dir")+File.separator+"src"+File.separator+"main"+File.separator+"resources"+File.separator+"images");
      //  String path = System.getProperty("user.dir")+File.separator+"src"+File.separator+"main"+File.separator+"resources"+File.separator+"images";
        String path = "."+File.separator+"src"+File.separator+"main"+File.separator+"resources"+File.separator+"images";
        System.out.println(path);


        try{
            File folder = new File(path);

            File[] listOfFiles = folder.listFiles();
            System.out.println(folder.canRead());

            for (final File file : listOfFiles) {
                System.out.println(file.getName());
                // ImageView imageView;
                // imageView = createImageView(file);
                //  tile.getChildren().addAll(imageView);
            }
        }
        catch (Exception e){
            System.out.println(e.getMessage());

        }


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
/*       try {
            File file = new File("A.txt");
            if(!file.exists())
                file.createNewFile();
            //KollereUtils.OUTPUTFILE = new PrintStream(new FileOutputStream("traces.txt", true));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }*/
        launch(args);
    }
}

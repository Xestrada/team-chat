package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        System.out.println(com.sun.javafx.runtime.VersionInfo.getRuntimeVersion());
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        root.setId("pane");
        primaryStage.setTitle("Chat Client");

        Scene scene = new Scene(root, 400, 400);
        scene.getStylesheets().addAll(this.getClass().getResource("style.css").toExternalForm());

        primaryStage.setScene(scene);
        primaryStage.show();

    }


    public static void main(String[] args) {
        launch(args);
    }
}

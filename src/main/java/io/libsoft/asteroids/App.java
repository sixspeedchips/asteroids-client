package io.libsoft.asteroids;

import io.libsoft.asteroids.client.Client;
import io.libsoft.asteroids.controller.DrawController;
import io.libsoft.asteroids.model.InternalModel;
import java.util.Random;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Bounds;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {


  private static Random random;


  private DrawController controller;


  public static void main(String[] args) {
    launch(args);
  }


  /**
   * Loads FXML layout, resource bundle, and icon used by application.
   *
   * @param stage primary application window.
   * @throws Exception if a required resource cannot be loaded.
   */
  @Override
  public void start(Stage stage) throws Exception {


    FXMLLoader fxmlLoader = new FXMLLoader(App.class.getClassLoader().getResource("fighter.fxml"));



    Parent root = fxmlLoader.load();


    controller = fxmlLoader.getController();
    controller.setModel(InternalModel.getInstance());

    Scene scene = new Scene(root);
    stage.setScene(scene);
    stage.show();
//    stage.setMaximized(true);
//    stage.sizeToScene();
    stage.setWidth(500);
    stage.setHeight(500);
    stage.setOnCloseRequest(event -> {
      System.exit(2);
    });

    Client client = new Client("localhost", 10000, InternalModel.getInstance());
    client.start();

  }


  @Override
  public void stop() throws Exception {
    controller.stop();
    super.stop();
  }

  private void setStageSize(Stage stage, Parent root) {
    Bounds bounds = root.getLayoutBounds();
    stage.setMinWidth(root.minWidth(-1) + stage.getWidth() - bounds.getWidth());
    stage.setMinHeight(root.minHeight(-1) + stage.getHeight() - bounds.getHeight());
  }


}




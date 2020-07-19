package io.libsoft.asteroids.controller;


import io.libsoft.asteroids.model.InternalModel;
import io.libsoft.asteroids.view.ModelViewer;
import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class DrawController {

  @FXML
  public Label myId;
  @FXML
  public VBox root;
  @FXML
  public Label tickSpeed;
  @FXML
  private ModelViewer modelViewer;

  private GFXUpdater updater;
  private boolean running;
  private InternalModel model;

  @FXML
  private void initialize() {
//    modelViewer.widthProperty().bind(root.widthProperty().subtract(25));
//    modelViewer.heightProperty().bind(root.heightProperty().subtract(50));
  }


  public void setModel(InternalModel model){
    this.model = model;
    updater = new GFXUpdater();
    updater.start();


    setListeners();
  }

  private void setListeners() {
    model.uuidProperty().addListener((observable, oldValue, newValue) -> {
      Platform.runLater(() -> {
        myId.setText(newValue.toString());
      });
    });



  }


  public void stop() {
  }

  private void updateView() {
    modelViewer.draw();
    tickSpeed.setText(String.format("%.2f", InternalModel.getInstance().getTickSpeed()));
  }


  private class GFXUpdater extends AnimationTimer {

    @Override
    public void handle(long now) {
      updateView();
    }


  }

}

package io.libsoft.asteroids.controller;


import io.libsoft.asteroids.model.InternalModel;
import io.libsoft.asteroids.view.ModelViewer;
import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class DrawController {

  @FXML
  public Label myId;
  @FXML
  private ModelViewer modelViewer;

  private GFXUpdater updater;
  private boolean running;
  private InternalModel model;

  @FXML
  private void initialize() {


  }


  public void setModel(InternalModel model){
    this.model = model;
    modelViewer = new ModelViewer();
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
  }


  private class GFXUpdater extends AnimationTimer {

    @Override
    public void handle(long now) {
      updateView();
    }


  }

}

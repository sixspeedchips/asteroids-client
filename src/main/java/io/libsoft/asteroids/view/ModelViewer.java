package io.libsoft.asteroids.view;


import io.libsoft.asteroids.model.InternalModel;
import io.libsoft.messenger.GameState;
import io.libsoft.messenger.PEntity;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;


public class ModelViewer extends Canvas {

  private final GraphicsContext gc;

  public ModelViewer() {
    gc = getGraphicsContext2D();
    setOnMouseDragged(event -> {
      System.out.printf("%f, %f\n", event.getX(), event.getY());
    });
    setHeight(500);
    setWidth(500);

  }



  /**
   * Renders the model to the screen via a jfx canvas
   */
  public void draw() {
    gc.setFill(Color.LIGHTSLATEGRAY);
    gc.fillRect(0, 0, getWidth(), getHeight());
    GameState gs = InternalModel.getInstance().getGameState();
    if (gs != null) {
      for (PEntity pEntity : gs.getPEntities()) {
        gc.setFill(Color.RED);
        gc.fillOval(pEntity.getX(), pEntity.getY(), 12, 12);
      }
    }
//    }
//    System.out.println(InternalModel.getInstance().getGameState());
  }


}

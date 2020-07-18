package io.libsoft.asteroids.view;


import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;


public class ModelViewer extends Canvas {

  private final GraphicsContext gc;

  public ModelViewer() {
    gc = getGraphicsContext2D();
    setWidth(500);
    setHeight(500);
    setOnMouseDragged(event -> {
      System.out.printf("%f, %f\n",event.getX(), event.getY());
    });
  }

  private void setListeners() {

  }


  /**
   * Renders the model to the screen via a jfx canvas
   */
  public void draw() {
    gc.setFill(Color.BLACK);
    gc.fillOval(0,0, getWidth(), getHeight());

  }


}

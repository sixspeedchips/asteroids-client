package io.libsoft.asteroids.view;


import io.libsoft.asteroids.model.InternalModel;
import io.libsoft.messenger.Entity;
import java.util.LinkedList;
import java.util.List;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;


public class ModelViewer extends Canvas {

  private final GraphicsContext gc;
  private final Double SCALE = 40D;
  private double cMass = 10;
  private final Color BACKGROUND = Color.LIGHTGREY;
  private List<Entity> lastDrawnEntities;

  public ModelViewer() {
    gc = getGraphicsContext2D();

    setHeight(1000);
    setWidth(1000);
    gc.setFill(BACKGROUND);
    gc.fillRect(0, 0, getWidth(), getHeight());
  }


  /**
   * Renders the model to the screen via a jfx canvas
   */
  public void draw() {
    List<Entity> currentEntities = InternalModel.getInstance().getEntities();
    gc.setFill(BACKGROUND);
    gc.fillRect(0, 0, getWidth(), getHeight());
//    if (lastDrawnEntities != null){
//      gc.setFill(BACKGROUND);
//      lastDrawnEntities.forEach(this::drawEntity);
//    }

    if (currentEntities != null) {
      for (Entity currentEntity : currentEntities) {
        gc.setFill(InternalModel.getInstance().getUUID().equals(currentEntity.getUuid()) ? Color.GREEN:Color.RED);
        drawEntity(currentEntity);
        gc.setFill(BACKGROUND);

      }
      lastDrawnEntities = currentEntities;
    }
//    }
//    System.out.println(InternalModel.getInstance().getGameState());
  }

  private void drawEntity(Entity entity){
    double x = entity.getPosition().getX();
    double y = entity.getPosition().getY();
    double[] xPts = new double[6];
    double[] yPts = new double[6];

    xPts[0] = x + 2 * SCALE * Math.cos(entity.getTheta());
    yPts[0] = y + 2 * SCALE * Math.sin(entity.getTheta());

    xPts[1] = x + .5* SCALE * Math.cos(entity.getTheta() + 3 * Math.PI / 4);
    yPts[1] = y + .5*SCALE * Math.sin(entity.getTheta() + 3 * Math.PI / 4);

    xPts[2] = x + .5*SCALE * Math.cos(entity.getTheta() + 5 * Math.PI / 4);
    yPts[2] = y + .5*SCALE * Math.sin(entity.getTheta() + 5 * Math.PI / 4);

    gc.fillPolygon(xPts, yPts, 3);
    gc.setFill(Color.CHARTREUSE);
    gc.fillOval(x-cMass/2, y-cMass/2, cMass, cMass);
  }


}

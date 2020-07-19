package io.libsoft.asteroids.controller;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class Controller implements Controls {

  private final Scene scene;
  private final List<KeyCode> keysPressed = new LinkedList<>();
  private final Map<KeyCode, BooleanProperty> commands = new HashMap<>();

  {
    commands.put(KeyCode.W, new SimpleBooleanProperty());
    commands.put(KeyCode.A, new SimpleBooleanProperty());
    commands.put(KeyCode.S, new SimpleBooleanProperty());
    commands.put(KeyCode.D, new SimpleBooleanProperty());
    commands.put(KeyCode.SPACE, new SimpleBooleanProperty());
  }


  public Controller(Scene scene) {
    this.scene = scene;
    scene.setOnKeyPressed(this::keyDown);
    scene.setOnKeyReleased(this::keyUp);

  }

  private void keyDown(KeyEvent event) {
    if (commands.get(event.getCode()) != null) {
      commands.get(event.getCode()).setValue(Boolean.TRUE);
    }
  }

  private void keyUp(KeyEvent event) {
    if (commands.get(event.getCode()) != null) {
      commands.get(event.getCode()).setValue(Boolean.FALSE);
    }
  }


  @Override
  public List<KeyCode> getCurrentKeys() {

    for (Entry<KeyCode, BooleanProperty> booleanPropertyEntry : commands.entrySet()) {
      if (booleanPropertyEntry.getValue().get()) {
        keysPressed.add(booleanPropertyEntry.getKey());
      }
    }
    return keysPressed;
  }
}

package io.libsoft.asteroids.client;

import io.libsoft.asteroids.controller.Controls;
import io.libsoft.asteroids.model.InternalModel;
import io.libsoft.messenger.Entity;
import io.libsoft.messenger.Message;
import io.libsoft.messenger.MessageType;
import io.libsoft.messenger.jsonmessages.SetName;
import io.libsoft.messenger.service.GsonService;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ConnectException;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import javafx.scene.input.KeyCode;

public class Client implements Runnable {

  private final ScheduledExecutorService es = Executors.newScheduledThreadPool(1);
  private final String host;
  private final int port;
  private final InternalModel model;
  private final Controls controls;

  private ObjectInputStream ois;
  private ObjectOutputStream oos;
  private Socket socket;
  private UUID uuid;
  private ScheduledFuture<?> connectionService;
  private boolean running;

  public Client(String host, int port, InternalModel model, Controls controls) {
    this.host = host;
    this.port = port;
    this.model = model;
    this.controls = controls;
    setListeners();
    connect();

  }

  void sendCommands() {
    List<KeyCode> kc = controls.getCurrentKeys();
    if (kc.isEmpty()) {
      return;
    }
    Message m = Message
        .build()
        .messageType(MessageType.CONTROL)
        .payload(GsonService.getInstance().toJson(kc))
        .sign(uuid);
    sendMessage(m);
    kc.clear();
  }


  private void setListeners() {

  }

  private void connect() {
    try {
      System.out.println("Attempting to connect to server...");
      socket = new Socket(host, port);
      oos = new ObjectOutputStream(socket.getOutputStream());
      ois = new ObjectInputStream(socket.getInputStream());
      System.out.println("Connected successfully.");
      if (connectionService != null) {
        connectionService.cancel(true);
        this.start();
      }
    } catch (ConnectException e) {
    } catch (IOException e) {
      e.printStackTrace();
    }
  }


  public void sendMessage(Message message) {
    try {
      oos.writeObject(message);
    } catch (IOException e) {
      e.printStackTrace();
    }


  }

  @Override
  public void run() {
    if (uuid == null) {
      setName(model.getUsername());
    }

    running = true;
    while (running) {
      Message message = null;
      try {
        message = (Message) ois.readObject();
//        System.out.println(GsonService.getInstance().toJson(message));
        // accepts the new UUID and replies with a subscription request
        // to the the current game state
        if (uuid == null && message.getMessageType() == MessageType.ASSIGN_UUID) {
          uuid = message.getMessageUUID();
          model.setUUID(uuid);
          Message r = Message.build().messageType(MessageType.ACCEPTED_UUID).sign(model.getUUID());
          sendMessage(r);
          Message m = Message.build().messageType(MessageType.SUBSCRIBE).sign(model.getUUID());
          sendMessage(m);
          es.scheduleAtFixedRate(this::sendCommands, 0, 16, TimeUnit.MILLISECONDS);
        } else if (message.getMessageType() == MessageType.GAME_STATE) {
          model.timestampUpdate(System.nanoTime());

          List<Entity> entities = new LinkedList<>();
          for (String e : message.getGameState().getPEntities()) {
            entities.add(GsonService.getAnnotater().fromJson(e, Entity.class));
          }
          System.out.println(GsonService.getPprinter().toJson(entities));
          model.setEntityState(entities);
        }
      } catch (IOException e) {
//        e.printStackTrace();
        System.out.println("Connection closed by server.");
        running = false;
        try {
          socket.close();
        } catch (IOException ioException) {
          ioException.printStackTrace();
        }
        connectionService = es.scheduleAtFixedRate(this::connect, 0, 5, TimeUnit.SECONDS);
      } catch (ClassNotFoundException e) {
        e.printStackTrace();
      }


    }

  }


  public void start() {
    Thread t = new Thread(this);
    t.start();
  }

  public void setName(String name) {

    SetName payload = new SetName();
    payload.setUsername(name);
    Message m = Message
        .build()
        .messageType(MessageType.SET_NAME)
        .payload(payload.toString())
        .sign(uuid);

    sendMessage(m);
  }
}

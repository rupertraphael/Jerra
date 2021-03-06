package jerra.control;

import java.util.Set;
import java.util.HashSet;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.KeyEvent;
import javafx.util.Duration;

import jerra.core.Rect;
import jerra.core.Vector;
import jerra.entity.AmbientShooterSpawner;
import jerra.entity.Bullet;
import jerra.entity.DefaultEntity;
import jerra.entity.Gun;
import jerra.entity.ShooterEntity;
import jerra.entity.Player;

import jerra.presence.ActivePresence;
import jerra.presence.DefaultPresence;
import jerra.presence.WanderPresence;
import jerra.room.Room;
import jerra.view.GraphicView;
import jerra.view.TextView;

/**
 * TextController
 */
public class GraphicController implements Controller {

    private Room room;
    private Canvas canvas;

    private TextView textView;
    private GraphicView view;

    private Set<String> heldKeys;
    private Set<String> lastKeys;
    private Set<String> newKeys;

    public GraphicController(Room room, Canvas canvas) {
        this.room = room;
        this.canvas = canvas;

        this.heldKeys = new HashSet<String>();
        this.lastKeys = new HashSet<String>();
        this.newKeys = new HashSet<String>();

    }

    public void start() {
        Vector zero = new Vector(0, 0);
        Vector block = new Vector(25, 25);

        this.room.spawnEntity(new DefaultEntity(new DefaultPresence(new Rect(new Vector(100, 0), block), zero)));
		this.room.spawnEntity(new DefaultEntity(new DefaultPresence(new Rect(new Vector(200, 0), block), zero)));
		this.room.spawnEntity(new DefaultEntity(new DefaultPresence(new Rect(new Vector(0, 100), block), zero)));
		this.room.spawnEntity(new DefaultEntity(new DefaultPresence(new Rect(new Vector(0, 200), block), zero)));
        this.room.spawnEntity(new DefaultEntity(new DefaultPresence(new Rect(new Vector(125, 125), block), zero)));

        
        ShooterEntity shooter = new ShooterEntity(
            new WanderPresence(new Rect(new Vector(300, 300), block), new Vector(3, 3), 25),
            new Gun(
                new Bullet(new Rect(zero, block), new Vector(30, 30), 7, 'E'),
                40
            ),
            'E'
        );
        this.room.spawnShooter(shooter);

        this.room.spawnShooterSpawner(new AmbientShooterSpawner(
            // new DefaultEntity(new WanderPresence(new Rect(new Vector(0, 0), block), new Vector(3, 3), 25)),
            shooter,
            new Vector(300, 300), 
            300, 
            100
        ));

        this.room.spawnShooter(
            new Player(
                new ActivePresence(
                    new Rect(
                        new Vector(0, 0), block
                    ), 
                    new Vector(5, 5), "up", "down", "left", "right"
                ),
                new Gun(
                    new Bullet(
                        new Rect(
                            new Vector(0, 0), new Vector(10, 10)
                        ), new Vector(15, 15), 100, 'P'
                    ),
                    10
                ),
                'P',
                new Vector(1, 0)
            )
        );
    
        this.view = new GraphicView(this.room, this.canvas);
        view.render();

        this.textView = new TextView(this.room);
        textView.render();

        // Handle key events
        this.canvas.getScene().setOnKeyPressed(event -> this.handleKeyPress(event));
        this.canvas.getScene().setOnKeyReleased(event -> this.handleKeyRelease(event));

        // Create game loop
        Timeline gameLoop = new Timeline();
        gameLoop.setCycleCount(Timeline.INDEFINITE);
        KeyFrame frame = new KeyFrame(new Duration(20), (event) -> this.update());
        gameLoop.getKeyFrames().add(frame);
        gameLoop.play();

    }

    private void handleKeyPress(KeyEvent key) {     

        this.heldKeys.add(key.getCode().toString());

    }

    private void handleKeyRelease(KeyEvent key) {

        this.heldKeys.remove(key.getCode().toString());

    }

    /**
     * Calculates various keysets, such as new key presses
     */
    private void computeKeys() {

        // Find new keys by removing old ones
        this.newKeys = new HashSet<String>(this.heldKeys);
        this.newKeys.removeAll(this.lastKeys);

        // Reset last keys to current press set
        this.lastKeys = new HashSet<String>(this.heldKeys);

    }

    private void queueKeys() {

        // Check held key events; note: will include new presses
        for (String key: this.heldKeys) {
            switch(key) {
                case "W":
                    this.room.queue("up");
                    break;
                case "S":
                    this.room.queue("down");
                    break;
                case "A":
                    this.room.queue("left");
                    break;
                case "D":
                    this.room.queue("right");
                    break;
                default:
                    break;
            }
        }

        // Check through keys that specifically appeared this update,
        // i.e. newly pressed
        for (String key: this.newKeys) {
            switch (key) {
                case "SPACE":
                    this.room.queue("shoot");
                    break;
                case "UP":
                    this.room.queue("upSecondary");
                    this.room.queue("shoot");
                    break;
                case "DOWN":
                    this.room.queue("downSecondary");
                    this.room.queue("shoot");
                    break;
                case "LEFT":
                    this.room.queue("leftSecondary");
                    this.room.queue("shoot");
                    break;
                case "RIGHT":
                    this.room.queue("rightSecondary");
                    this.room.queue("shoot");
                    break;
                default:
                    break;
            }
        }
    }

    private void update() {

        // Compute key sets
        this.computeKeys();

        // Queue key driven commands into the room
        this.queueKeys();

        // Update the room
        this.room.update();

        // Render the views
        this.textView.render();

        this.view.render();

        // Clear the room command queue
        this.room.clearQueue();

    }

}

package sokoban.view;

import sokoban.controller.EventListener;
import sokoban.model.Direction;
import sokoban.model.GameObject;
import sokoban.model.GameObjects;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Set;

import static java.awt.event.KeyEvent.*;

public class Field extends JPanel {
    private View view;
    private EventListener eventListener;

    public Field(View view) {
        this.view = view;
        KeyHandler keyHandler = new KeyHandler();
        addKeyListener(keyHandler);
        setFocusable(true);
    }

    public void paint(Graphics g) {
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, getWidth(), getHeight());
        GameObjects gameObjects = view.getGameObjects();
        Set<GameObject> set = gameObjects.getAll();
        for (GameObject entry : set) {
            entry.draw(g);
        }
    }

    public class KeyHandler extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            switch (e.getKeyCode()) {
                case VK_LEFT:
                    eventListener.move(Direction.LEFT);
                    break;
                case VK_RIGHT:
                    eventListener.move(Direction.RIGHT);
                    break;
                case VK_DOWN:
                    eventListener.move(Direction.DOWN);
                    break;
                case VK_UP:
                    eventListener.move(Direction.UP);
                    break;
                case VK_R:
                    eventListener.restart();
                    break;
            }
        }
    }

    public void setEventListener(EventListener eventListener) {
        this.eventListener = eventListener;
    }
}

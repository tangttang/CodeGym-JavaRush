package sokoban.view;

import sokoban.controller.Controller;
import sokoban.controller.EventListener;
import sokoban.model.GameObjects;

import javax.swing.*;

public class View extends JFrame {
    private Controller controller;
    private Field field;

    public View(Controller controller) {
        this.controller = controller;
    }

    public void init() {
        field = new Field(this);
        setTitle("Sokoban");
        add(field);
        setSize(500, 300);
        setVisible(true);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    public void completed(int level) {
        update();
        JOptionPane.showMessageDialog(null, "Level " + level + " completed", "Congratulations", JOptionPane.INFORMATION_MESSAGE);
        controller.startNextLevel();
    }

    public void update() {
        field.repaint();
    }

    public GameObjects getGameObjects() {
        return controller.getGameObjects();
    }

    public void setEventListener(EventListener eventListener) {
        this.field.setEventListener(eventListener);
    }

}

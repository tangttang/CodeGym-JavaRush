package sokoban.controller;

import sokoban.model.Direction;
import sokoban.model.GameObjects;
import sokoban.model.Model;
import sokoban.view.View;

public class Controller implements EventListener {
    private View view;
    private Model model;

    public Controller() {
        this.view = new View(this);
        this.model = new Model();
        view.init();
        view.setEventListener(this);
        model.restart();
        model.setEventListener(this);
    }

    public GameObjects getGameObjects() {
        return model.getGameObjects();
    }

    @Override
    public void move(Direction direction) {
        model.move(direction);
        view.update();
    }

    @Override
    public void restart() {
        model.restart();
        view.update();
    }

    @Override
    public void startNextLevel() {
        model.startNextLevel();
        view.update();
    }

    @Override
    public void levelCompleted(int level) {
        view.completed(level);
    }

    public static void main(String[] args) {
        Controller controller = new Controller();
    }
}

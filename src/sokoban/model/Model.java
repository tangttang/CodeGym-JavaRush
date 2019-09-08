package sokoban.model;


import sokoban.controller.EventListener;

import java.nio.file.FileSystems;
import java.util.Set;

public class Model {
    private EventListener eventListener;
    public static final int FIELD_CELL_SIZE = 20;
    private GameObjects gameObjects;
    private int currentLevel = 1;
    private LevelLoader levelLoader = new LevelLoader(FileSystems.getDefault().getPath("src", "sokoban", "res", "levels.txt"));

    public void restartLevel(int level) {
        this.gameObjects = levelLoader.getLevel(level);
    }

    public void move(Direction direction) {
        Player player = gameObjects.getPlayer();

        if (checkWallCollision(player, direction) || checkBoxCollisionAndMoveIfAvailable(direction))
            return;

        moveBox(player, direction);
        checkCompletion();

    }

    public boolean checkWallCollision(CollisionObject gameObject, Direction direction) {
        for (Wall wall : gameObjects.getWalls()) {
            if (gameObject.isCollision(wall, direction))
                return true;
        }
        return false;
    }

    public boolean checkBoxCollisionAndMoveIfAvailable(Direction direction) {
        Player player = gameObjects.getPlayer();
        Set<GameObject> gameObjectSet = gameObjects.getAll();
        Set<Box> boxSet = gameObjects.getBoxes();
        int thisX = player.getX();
        int thisY = player.getY();
        switch (direction) {
            case LEFT:
                thisX -= FIELD_CELL_SIZE;
                break;
            case RIGHT:
                thisX += FIELD_CELL_SIZE;
                break;
            case UP:
                thisY -= FIELD_CELL_SIZE;
                break;
            case DOWN:
                thisY += FIELD_CELL_SIZE;
                break;
        }

        GameObject obstacle = null;
        for (GameObject gameObject : gameObjectSet) {
            if ((thisX == gameObject.getX())
                    && (thisY == gameObject.getY())
                    && !(gameObject instanceof Player)
                    && !(gameObject instanceof Home)) {
                obstacle = gameObject;
            }
        }

        if (obstacle == null) {
            return false;
        }

        if (obstacle instanceof Box) {
            Box boxObstacle = (Box) obstacle;
            if (checkWallCollision(boxObstacle, direction)) {
                return true;
            }

            for (Box box : boxSet) {
                if (boxObstacle.isCollision(box, direction)) {
                    return true;
                }
            }

            moveBox(boxObstacle, direction);
        }
        return false;
    }

    private void moveBox(Movable object, Direction direction) {
        switch (direction) {
            case RIGHT:
                object.move(FIELD_CELL_SIZE, 0);
                break;
            case LEFT:
                object.move(-FIELD_CELL_SIZE, 0);
                break;
            case UP:
                object.move(0, -FIELD_CELL_SIZE);
                break;
            case DOWN:
                object.move(0, FIELD_CELL_SIZE);
        }
    }

    public void checkCompletion() {
        boolean isFailed = false;
        for (Home home : gameObjects.getHomes()) {
            boolean boxInPlace = false;
            for (Box box : gameObjects.getBoxes()) {
                if (box.getX() == home.getX() && box.getY() == home.getY()) {
                    boxInPlace = true;
                }
            }
            if (!boxInPlace)
                isFailed = true;
        }
        if (!isFailed) {
            eventListener.levelCompleted(currentLevel);
        }
    }

    public void restart() {
        restartLevel(currentLevel);
    }

    public void startNextLevel() {
        this.currentLevel++;
        restart();
    }

    public GameObjects getGameObjects() {
        return gameObjects;
    }

    public void setEventListener(EventListener eventListener) {
        this.eventListener = eventListener;
    }
}

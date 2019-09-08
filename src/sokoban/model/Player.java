package sokoban.model;

import java.awt.*;

public class Player extends CollisionObject implements Movable {

    public Player(int x, int y) {
        super(x, y);
    }

    @Override
    public void move(int x, int y) {
        this.setX(this.getX() + x);
        this.setY(this.getY() + y);
    }

    @Override
    public boolean isCollision(GameObject gameObject, Direction direction) {
        return super.isCollision(gameObject, direction);
    }

    @Override
    public void draw(Graphics graphics) {
        int x = getX() - getWidth() / 2;
        int y = getY() - getHeight() / 2;
        graphics.setColor(Color.YELLOW);
        graphics.fillOval(x, y, getHeight(), getWidth());
    }
}

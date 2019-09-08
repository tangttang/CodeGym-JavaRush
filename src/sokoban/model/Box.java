package sokoban.model;

import java.awt.*;

public class Box extends CollisionObject implements Movable {

    public Box(int x, int y) {
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
        graphics.setColor(Color.ORANGE);
        int x = getX() - getWidth() / 2;
        int y = getY() - getHeight() / 2;
        graphics.drawRect(x, y, getWidth(), getHeight());
        graphics.drawLine(x, y, x + getWidth(), y + getHeight());
        graphics.drawLine(x, y + getHeight(), x + getWidth(), y);
    }
}

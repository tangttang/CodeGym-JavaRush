package sokoban.model;

import java.awt.*;

public class Wall extends CollisionObject {

    public Wall(int x, int y) {
        super(x, y);
    }

    @Override
    public void draw(Graphics graphics) {
        graphics.setColor(new Color(180, 120, 80));
        int x = getX() - getWidth() / 2;
        int y = getY() - getHeight() / 2;
        graphics.drawRect(x, y, getWidth(), getHeight());
        graphics.fillRect(x, y, getWidth(), getHeight());
        graphics.setColor(Color.white);

        graphics.drawLine(x, y, x + getWidth(), y);
        graphics.drawLine(x, y + getHeight() / 2, x + getWidth(), y + getHeight() / 2);
        graphics.drawLine(x, y + getHeight(), x + getWidth(), y + getHeight());
        graphics.drawLine(x + getWidth() / 2, y, x + getWidth() / 2, y + getHeight() / 2);
    }
}

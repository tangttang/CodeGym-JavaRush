package sokoban.model;

public abstract class CollisionObject extends GameObject {

    public CollisionObject(int x, int y) {
        super(x, y);
    }

    public boolean isCollision(GameObject gameObject, Direction direction) {
        switch (direction) {
            case UP:
                return gameObject.getX() == this.getX() && getY() - Model.FIELD_CELL_SIZE == gameObject.getY();
            case DOWN:
                return gameObject.getX() == this.getX() && getY() + Model.FIELD_CELL_SIZE == gameObject.getY();
            case LEFT:
                return getX() - Model.FIELD_CELL_SIZE == gameObject.getX() && gameObject.getY() == this.getY();
            case RIGHT:
                return getX() + Model.FIELD_CELL_SIZE == gameObject.getX() && gameObject.getY() == this.getY();
            default:
                return false;
        }
    }
}

package game2048;

import java.util.*;

public class Model {
    private static final int FIELD_WIDTH = 4;
    private Tile[][] gameTiles;
    int score;
    int maxTile;
    private Stack<Tile[][]> previousStates = new Stack<>();
    private Stack<Integer> previousScores = new Stack<>();
    private boolean isSaveNeeded = true;

    public Model() {
        resetGameTiles();
    }

    void randomMove() {
        int direction = ((int) (Math.random() * 100)) % 4;
        switch (direction) {
            case 0:
                left();
                break;
            case 1:
                right();
                break;
            case 2:
                up();
                break;
            case 3:
                down();
                break;
        }
    }

    void autoMove() {
        PriorityQueue<MoveEfficiency> efficiencies = new PriorityQueue<>(4, Collections.reverseOrder());
        efficiencies.offer(getMoveEfficiency(this::left));
        efficiencies.offer(getMoveEfficiency(this::right));
        efficiencies.offer(getMoveEfficiency(this::up));
        efficiencies.offer(getMoveEfficiency(this::down));
        efficiencies.peek().getMove().move();
    }

    boolean hasBoardChanged() {
        int previousStatesWeight = 0;
        int gameTilesWeight = 0;
        for (int i = 0; i < FIELD_WIDTH; i++) {
            for (int j = 0; j < FIELD_WIDTH; j++) {
                previousStatesWeight += previousStates.peek()[i][j].value;
                gameTilesWeight += this.gameTiles[i][j].value;
            }
        }
        return previousStatesWeight != gameTilesWeight;
    }

    MoveEfficiency getMoveEfficiency(Move move) {
        MoveEfficiency moveEfficiency;
        move.move();
        if (hasBoardChanged()) {
            moveEfficiency = new MoveEfficiency(getEmptyTiles().size(), this.score, move);
        } else {
            moveEfficiency = new MoveEfficiency(-1, 0, move);
        }
        rollback();
        return moveEfficiency;
    }

    private void saveState(Tile[][] tiles) {
        Tile[][] temp = new Tile[FIELD_WIDTH][FIELD_WIDTH];
        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles[i].length; j++) {
                temp[i][j] = new Tile(tiles[i][j].value);
            }
        }
        previousStates.push(temp);
        previousScores.push(score);
        isSaveNeeded = false;
    }

    public void rollback() {
        if (!previousScores.isEmpty() && !previousStates.isEmpty()) {
            gameTiles = previousStates.pop();
            score = previousScores.pop();
        }

    }

    void resetGameTiles() {
        this.score = 0;
        this.maxTile = 0;
        this.gameTiles = new Tile[FIELD_WIDTH][FIELD_WIDTH];
        for (int i = 0; i < gameTiles.length; i++) {
            for (int j = 0; j < gameTiles[i].length; j++) {
                gameTiles[i][j] = new Tile();
            }
        }
        addTile();
        addTile();
    }

    private void addTile() {
        if (!getEmptyTiles().isEmpty())
            getEmptyTiles().get((int) (Math.random() * getEmptyTiles().size())).value = Math.random() < 0.9 ? 2 : 4;
    }


    private List<Tile> getEmptyTiles() {
        ArrayList<Tile> list = new ArrayList<>();
        for (Tile[] tiles : gameTiles) {
            for (Tile tile : tiles) {
                if (tile.value == 0) {
                    list.add(tile);
                }
            }
        }
        return list;
    }

    private boolean compressTiles(Tile[] tiles) {
        boolean isChanged = false;
        for (int i = 0; i < tiles.length; i++) {
            if (tiles[i].value == 0 && i < tiles.length - 1 && tiles[i + 1].value != 0) {
                Tile temp = tiles[i];
                tiles[i] = tiles[i + 1];
                tiles[i + 1] = temp;
                i = -1;
                isChanged = true;
            }
        }
        return isChanged;
    }

    public boolean canMove() {
        boolean isChanged = false;
        if (!getEmptyTiles().isEmpty()) {
            isChanged = true;
        } else {
            for (int i = 0; i < FIELD_WIDTH - 1; i++) {
                for (int j = 0; j < FIELD_WIDTH; j++) {
                    if (j < FIELD_WIDTH - 1 && gameTiles[i][j].value == gameTiles[i][j + 1].value || gameTiles[i][j].value == gameTiles[i + 1][j].value) {
                        isChanged = true;
                        break;
                    }
                }
            }
        }
        return isChanged;
    }

    public Tile[][] getGameTiles() {
        return gameTiles;
    }

    private boolean mergeTiles(Tile[] tiles) {
        boolean isChanged = false;
        for (int i = 0; i < tiles.length - 1; i++) {
            if (tiles[i].value != 0 && (tiles[i].value == tiles[i + 1].value)) {
                if (tiles[i].value + tiles[i + 1].value > maxTile) {
                    maxTile = tiles[i].value + tiles[i + 1].value;
                }
                tiles[i].value += tiles[i + 1].value;
                score += tiles[i].value;
                tiles[i + 1].value = 0;
                isChanged = true;
                compressTiles(tiles);
            }
        }
        return isChanged;
    }

    void left() {
        if (this.isSaveNeeded)
            saveState(this.gameTiles);
        boolean isChanged = false;
        for (int i = 0; i < FIELD_WIDTH; i++) {
            if (compressTiles(gameTiles[i]) | mergeTiles(gameTiles[i])) {
                isChanged = true;
            }
        }
        this.isSaveNeeded = true;
        if (isChanged) {
            addTile();
        }
    }

    void right() {
        saveState(this.gameTiles);
        rotate();
        rotate();
        left();
        rotate();
        rotate();
    }

    void up() {
        saveState(this.gameTiles);
        rotate();
        left();
        rotate();
        rotate();
        rotate();
    }

    void down() {
        saveState(this.gameTiles);
        rotate();
        rotate();
        rotate();
        left();
        rotate();
    }

    private void rotate() {
        for (int x = 0; x < FIELD_WIDTH / 2; x++) {
            // Consider elements in group of 4 in
            // current square
            for (int y = x; y < FIELD_WIDTH - x - 1; y++) {
                // store current cell in temp variable
                Tile temp = gameTiles[x][y];

                // move values from right to top
                gameTiles[x][y] = gameTiles[y][FIELD_WIDTH - 1 - x];

                // move values from bottom to right
                gameTiles[y][FIELD_WIDTH - 1 - x] = gameTiles[FIELD_WIDTH - 1 - x][FIELD_WIDTH - 1 - y];

                // move values from left to bottom
                gameTiles[FIELD_WIDTH - 1 - x][FIELD_WIDTH - 1 - y] = gameTiles[FIELD_WIDTH - 1 - y][x];

                // assign temp to left
                gameTiles[FIELD_WIDTH - 1 - y][x] = temp;
            }
        }
    }
}

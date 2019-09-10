package game2048;

public class MoveEfficiency implements Comparable<MoveEfficiency> {
    private int numberOfEmptyTiles;
    private int score;
    private Move move;

    public MoveEfficiency(int numberOfEmptyTiles, int score, Move move) {
        this.numberOfEmptyTiles = numberOfEmptyTiles;
        this.score = score;
        this.move = move;
    }

    @Override
    public int compareTo(MoveEfficiency o) {
        if (this == o)
            return 0;
        else if (this.numberOfEmptyTiles < o.numberOfEmptyTiles) {
            return -1;
        } else if (this.numberOfEmptyTiles > o.numberOfEmptyTiles) {
            return 1;
        } else {
            if (this.score < o.score) {
                return -1;
            } else if (this.score > o.score) {
                return 1;
            } else {
                return 0;
            }
        }
    }

    public Move getMove() {
        return move;
    }
}

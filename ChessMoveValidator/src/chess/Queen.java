package chess;

public class Queen implements ChessPiece {

    private final boolean white;

    public Queen(boolean white) {
        this.white = white;
    }

    @Override
    public boolean isWhite() {
        return white;
    }

    @Override
    public String getSymbol() {
        return white ? "♕" : "♛";
    }

    @Override
    public boolean isValidMove(
            int startRow,
            int startCol,
            int endRow,
            int endCol) {

        int rowDiff = Math.abs(endRow - startRow);
        int colDiff = Math.abs(endCol - startCol);

        // Straight OR diagonal
        return startRow == endRow ||
                startCol == endCol ||
                rowDiff == colDiff;
    }
}
package chess;

public class King implements ChessPiece {

    private final boolean white;

    public King(boolean white) {
        this.white = white;
    }

    @Override
    public boolean isWhite() {
        return white;
    }

    @Override
    public String getSymbol() {
        return white ? "♔" : "♚";
    }

    @Override
    public boolean isValidMove(
            int startRow,
            int startCol,
            int endRow,
            int endCol) {

        int rowDiff = Math.abs(endRow - startRow);
        int colDiff = Math.abs(endCol - startCol);

        return rowDiff <= 1 &&
                colDiff <= 1 &&
                !(rowDiff == 0 && colDiff == 0);
    }
}
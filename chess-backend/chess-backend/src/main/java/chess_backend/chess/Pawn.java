package chess_backend.chess;

public class Pawn implements ChessPiece {

    private final boolean white;

    public Pawn(boolean white) {
        this.white = white;
    }

    @Override
    public boolean isWhite() {
        return white;
    }

    @Override
    public String getSymbol() {
        return white ? "♙" : "♟";
    }

    @Override
    public boolean isValidMove(
            int startRow,
            int startCol,
            int endRow,
            int endCol) {

        int direction = white ? -1 : 1;

        int rowDiff = endRow - startRow;
        int colDiff = Math.abs(endCol - startCol);

        // One step
        if (colDiff == 0 &&
                rowDiff == direction) {

            return true;
        }

        // Two steps from starting position
        if (colDiff == 0 &&
                rowDiff == 2 * direction) {

            if (white && startRow == 6) {
                return true;
            }

            if (!white && startRow == 1) {
                return true;
            }
        }

        // Diagonal capture
        return colDiff == 1 &&
                rowDiff == direction;
    }
}
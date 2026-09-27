package chess;

public class Rook implements ChessPiece {

    private final boolean white;

    public Rook(boolean white) {
        this.white = white;
    }

    @Override
    public boolean isWhite() {
        return white;
    }

    @Override
    public String getSymbol() {
        return white ? "♖" : "♜";
    }

    @Override
    public boolean isValidMove(
            int startRow,
            int startCol,
            int endRow,
            int endCol) {

        return startRow == endRow ||
                startCol == endCol;
    }
}
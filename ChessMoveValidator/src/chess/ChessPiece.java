package chess;

public interface ChessPiece {

    boolean isWhite();

    String getSymbol();

    boolean isValidMove(
            int startRow,
            int startCol,
            int endRow,
            int endCol
    );
}
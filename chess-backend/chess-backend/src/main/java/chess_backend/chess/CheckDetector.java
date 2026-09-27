package chess_backend.chess;

public class CheckDetector {

    public boolean isKingInCheck(
            Board board,
            boolean whiteKing) {

        int[] kingPosition =
                findKing(board, whiteKing);

        if (kingPosition == null) {
            return false;
        }

        int kingRow = kingPosition[0];
        int kingCol = kingPosition[1];

        for (int row = 0; row < 8; row++) {

            for (int col = 0; col < 8; col++) {

                ChessPiece piece =
                        board.getPiece(row, col);

                if (piece == null) {
                    continue;
                }

                // Same color piece king ko attack nahi kar raha
                if (piece.isWhite() == whiteKing) {
                    continue;
                }

                if (canAttack(
                        board,
                        piece,
                        row,
                        col,
                        kingRow,
                        kingCol)) {

                    return true;
                }
            }
        }

        return false;
    }

    // ==========================================
    // FIND KING
    // ==========================================

    private int[] findKing(
            Board board,
            boolean whiteKing) {

        for (int row = 0; row < 8; row++) {

            for (int col = 0; col < 8; col++) {

                ChessPiece piece =
                        board.getPiece(row, col);

                if (piece instanceof King &&
                        piece.isWhite() == whiteKing) {

                    return new int[]{
                            row,
                            col
                    };
                }
            }
        }

        return null;
    }

    // ==========================================
    // CAN ATTACK
    // ==========================================

    private boolean canAttack(
            Board board,
            ChessPiece piece,
            int startRow,
            int startCol,
            int endRow,
            int endCol) {

        int rowDiff =
                Math.abs(endRow - startRow);

        int colDiff =
                Math.abs(endCol - startCol);

        // Pawn
        if (piece instanceof Pawn) {

            int direction =
                    piece.isWhite() ? -1 : 1;

            return rowDiff == 1 &&
                    colDiff == 1 &&
                    endRow - startRow == direction;
        }

        // Knight
        if (piece instanceof Knight) {

            return (rowDiff == 2 &&
                    colDiff == 1) ||
                    (rowDiff == 1 &&
                            colDiff == 2);
        }

        // King
        if (piece instanceof King) {

            return rowDiff <= 1 &&
                    colDiff <= 1 &&
                    !(rowDiff == 0 &&
                            colDiff == 0);
        }

        // Queen / Rook / Bishop
        if (!piece.isValidMove(
                startRow,
                startCol,
                endRow,
                endCol)) {

            return false;
        }

        return board.isPathClear(
                startRow,
                startCol,
                endRow,
                endCol
        );
    }
}
package chess;

public class CheckDetector {

    // ==========================================
    // KING CHECK
    // ==========================================

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

                // Apne pieces ignore karo
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
    // CHECKMATE
    // ==========================================

    public boolean isCheckmate(
            Board board,
            boolean whiteKing) {

        // King check me nahi hai
        if (!isKingInCheck(board, whiteKing)) {
            return false;
        }

        // Apne har piece ko check karo
        for (int startRow = 0; startRow < 8; startRow++) {

            for (int startCol = 0; startCol < 8; startCol++) {

                ChessPiece piece =
                        board.getPiece(startRow, startCol);

                if (piece == null) {
                    continue;
                }

                // Sirf current player's pieces
                if (piece.isWhite() != whiteKing) {
                    continue;
                }

                // Har possible destination try karo
                for (int endRow = 0; endRow < 8; endRow++) {

                    for (int endCol = 0; endCol < 8; endCol++) {

                        if (isSafeMove(
                                board,
                                startRow,
                                startCol,
                                endRow,
                                endCol,
                                whiteKing)) {

                            // Ek bhi safe move mil gaya
                            // to CHECKMATE nahi hai
                            return false;
                        }
                    }
                }
            }
        }

        // Check me hai + koi safe move nahi
        return true;
    }


    // ==========================================
    // CHECK WHETHER MOVE IS SAFE
    // ==========================================

    private boolean isSafeMove(
            Board board,
            int startRow,
            int startCol,
            int endRow,
            int endCol,
            boolean whiteKing) {

        ChessPiece movingPiece =
                board.getPiece(startRow, startCol);

        if (movingPiece == null) {
            return false;
        }

        // Apna piece hona chahiye
        if (movingPiece.isWhite() != whiteKing) {
            return false;
        }

        // Same position
        if (startRow == endRow &&
                startCol == endCol) {
            return false;
        }

        // Destination par apna piece nahi hona chahiye
        ChessPiece destinationPiece =
                board.getPiece(endRow, endCol);

        if (destinationPiece != null &&
                destinationPiece.isWhite() == whiteKing) {
            return false;
        }

        // Piece ka normal move valid hai ya nahi
        if (!movingPiece.isValidMove(
                startRow,
                startCol,
                endRow,
                endCol)) {

            return false;
        }

        // Path clear hona chahiye
        if (!(movingPiece instanceof Knight) &&
                !(movingPiece instanceof King)) {

            if (!board.isPathClear(
                    startRow,
                    startCol,
                    endRow,
                    endCol)) {

                return false;
            }
        }

        /*
         * IMPORTANT:
         * Ab temporary move karenge.
         */

        ChessPiece capturedPiece =
                board.getPiece(endRow, endCol);

        // Move
        board.setPiece(
                endRow,
                endCol,
                movingPiece
        );

        board.setPiece(
                startRow,
                startCol,
                null
        );

        // Move ke baad King check me hai?
        boolean stillInCheck =
                isKingInCheck(board, whiteKing);

        // Board ko original position par wapas lao
        board.setPiece(
                startRow,
                startCol,
                movingPiece
        );

        board.setPiece(
                endRow,
                endCol,
                capturedPiece
        );

        // King check me nahi hai = safe move
        return !stillInCheck;
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
    // CAN PIECE ATTACK?
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


        // ==========================================
        // PAWN
        // ==========================================

        if (piece instanceof Pawn) {

            int direction =
                    piece.isWhite() ? -1 : 1;

            return rowDiff == 1 &&
                    colDiff == 1 &&
                    endRow - startRow == direction;
        }


        // ==========================================
        // KNIGHT
        // ==========================================

        if (piece instanceof Knight) {

            return (rowDiff == 2 && colDiff == 1) ||
                    (rowDiff == 1 && colDiff == 2);
        }


        // ==========================================
        // KING
        // ==========================================

        if (piece instanceof King) {

            return rowDiff <= 1 &&
                    colDiff <= 1 &&
                    !(rowDiff == 0 && colDiff == 0);
        }


        // ==========================================
        // QUEEN / ROOK / BISHOP
        // ==========================================

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
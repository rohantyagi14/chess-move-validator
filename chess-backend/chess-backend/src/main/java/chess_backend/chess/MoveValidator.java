package chess_backend.chess;

public class MoveValidator {

    public boolean validateMove(
            Board board,
            int startRow,
            int startCol,
            int endRow,
            int endCol,
            boolean whiteTurn) {

        if (!board.isInsideBoard(startRow, startCol) ||
                !board.isInsideBoard(endRow, endCol)) {

            return false;
        }

        ChessPiece piece =
                board.getPiece(startRow, startCol);

        if (piece == null) {
            return false;
        }

        if (piece.isWhite() != whiteTurn) {
            return false;
        }

        if (startRow == endRow &&
                startCol == endCol) {

            return false;
        }

        ChessPiece destination =
                board.getPiece(endRow, endCol);

        // Apne hi piece ko capture nahi kar sakte
        if (destination != null &&
                destination.isWhite() == piece.isWhite()) {

            return false;
        }

        // Castling
        if (piece instanceof King &&
                Math.abs(endCol - startCol) == 2 &&
                startRow == endRow) {

            return validateCastling(
                    board,
                    startRow,
                    startCol,
                    endRow,
                    endCol,
                    whiteTurn
            );
        }

        boolean basicMoveValid;

        // Pawn
        if (piece instanceof Pawn) {

            basicMoveValid =
                    validatePawnMove(
                            board,
                            startRow,
                            startCol,
                            endRow,
                            endCol,
                            piece,
                            destination
                    );

        } else {

            basicMoveValid =
                    piece.isValidMove(
                            startRow,
                            startCol,
                            endRow,
                            endCol
                    );

            // Knight ke liye path check nahi hota
            if (basicMoveValid &&
                    !(piece instanceof Knight)) {

                if (!board.isPathClear(
                        startRow,
                        startCol,
                        endRow,
                        endCol)) {

                    return false;
                }
            }
        }

        if (!basicMoveValid) {
            return false;
        }

        // Temporary move
        board.movePieceTemporary(
                startRow,
                startCol,
                endRow,
                endCol
        );

        CheckDetector checkDetector =
                new CheckDetector();

        boolean kingInCheck =
                checkDetector.isKingInCheck(
                        board,
                        whiteTurn
                );

        // Move undo
        board.movePieceTemporary(
                endRow,
                endCol,
                startRow,
                startCol
        );

        if (destination != null) {

            board.setPiece(
                    endRow,
                    endCol,
                    destination
            );
        }

        return !kingInCheck;
    }

    // ==========================================
    // CASTLING
    // ==========================================

    private boolean validateCastling(
            Board board,
            int startRow,
            int startCol,
            int endRow,
            int endCol,
            boolean whiteTurn) {

        if (startCol != 4) {
            return false;
        }

        CheckDetector checkDetector =
                new CheckDetector();

        // Check me castle nahi kar sakte
        if (checkDetector.isKingInCheck(
                board,
                whiteTurn)) {

            return false;
        }

        // ======================================
        // KING SIDE
        // ======================================

        if (endCol == 6) {

            if (whiteTurn && startRow == 7) {

                if (board.hasWhiteKingMoved() ||
                        board.hasWhiteRightRookMoved()) {

                    return false;
                }

                ChessPiece rook =
                        board.getPiece(7, 7);

                if (!(rook instanceof Rook) ||
                        !rook.isWhite()) {

                    return false;
                }

                if (board.getPiece(7, 5) != null ||
                        board.getPiece(7, 6) != null) {

                    return false;
                }

                if (isSquareAttacked(
                        board, 7, 5, false) ||
                        isSquareAttacked(
                                board, 7, 6, false)) {

                    return false;
                }

                return true;
            }

            if (!whiteTurn && startRow == 0) {

                if (board.hasBlackKingMoved() ||
                        board.hasBlackRightRookMoved()) {

                    return false;
                }

                ChessPiece rook =
                        board.getPiece(0, 7);

                if (!(rook instanceof Rook) ||
                        rook.isWhite()) {

                    return false;
                }

                if (board.getPiece(0, 5) != null ||
                        board.getPiece(0, 6) != null) {

                    return false;
                }

                if (isSquareAttacked(
                        board, 0, 5, true) ||
                        isSquareAttacked(
                                board, 0, 6, true)) {

                    return false;
                }

                return true;
            }
        }

        // ======================================
        // QUEEN SIDE
        // ======================================

        if (endCol == 2) {

            if (whiteTurn && startRow == 7) {

                if (board.hasWhiteKingMoved() ||
                        board.hasWhiteLeftRookMoved()) {

                    return false;
                }

                ChessPiece rook =
                        board.getPiece(7, 0);

                if (!(rook instanceof Rook) ||
                        !rook.isWhite()) {

                    return false;
                }

                if (board.getPiece(7, 1) != null ||
                        board.getPiece(7, 2) != null ||
                        board.getPiece(7, 3) != null) {

                    return false;
                }

                if (isSquareAttacked(
                        board, 7, 3, false) ||
                        isSquareAttacked(
                                board, 7, 2, false)) {

                    return false;
                }

                return true;
            }

            if (!whiteTurn && startRow == 0) {

                if (board.hasBlackKingMoved() ||
                        board.hasBlackLeftRookMoved()) {

                    return false;
                }

                ChessPiece rook =
                        board.getPiece(0, 0);

                if (!(rook instanceof Rook) ||
                        rook.isWhite()) {

                    return false;
                }

                if (board.getPiece(0, 1) != null ||
                        board.getPiece(0, 2) != null ||
                        board.getPiece(0, 3) != null) {

                    return false;
                }

                if (isSquareAttacked(
                        board, 0, 3, true) ||
                        isSquareAttacked(
                                board, 0, 2, true)) {

                    return false;
                }

                return true;
            }
        }

        return false;
    }

    // ==========================================
    // SQUARE ATTACKED
    // ==========================================

    private boolean isSquareAttacked(
            Board board,
            int row,
            int col,
            boolean byWhite) {

        for (int r = 0; r < 8; r++) {

            for (int c = 0; c < 8; c++) {

                ChessPiece piece =
                        board.getPiece(r, c);

                if (piece == null) {
                    continue;
                }

                if (piece.isWhite() != byWhite) {
                    continue;
                }

                int rowDiff =
                        Math.abs(row - r);

                int colDiff =
                        Math.abs(col - c);

                if (piece instanceof Pawn) {

                    int direction =
                            piece.isWhite() ? -1 : 1;

                    if (row - r == direction &&
                            colDiff == 1) {

                        return true;
                    }
                }

                else if (piece instanceof Knight) {

                    if ((rowDiff == 2 &&
                            colDiff == 1) ||
                            (rowDiff == 1 &&
                                    colDiff == 2)) {

                        return true;
                    }
                }

                else if (piece instanceof King) {

                    if (rowDiff <= 1 &&
                            colDiff <= 1 &&
                            !(rowDiff == 0 &&
                                    colDiff == 0)) {

                        return true;
                    }
                }

                else {

                    if (piece.isValidMove(
                            r,
                            c,
                            row,
                            col) &&
                            board.isPathClear(
                                    r,
                                    c,
                                    row,
                                    col)) {

                        return true;
                    }
                }
            }
        }

        return false;
    }

    // ==========================================
    // PAWN MOVE
    // ==========================================

    private boolean validatePawnMove(
            Board board,
            int startRow,
            int startCol,
            int endRow,
            int endCol,
            ChessPiece piece,
            ChessPiece destination) {

        int direction =
                piece.isWhite() ? -1 : 1;

        int rowDiff =
                endRow - startRow;

        int colDiff =
                Math.abs(endCol - startCol);

        // One step
        if (colDiff == 0 &&
                rowDiff == direction) {

            return destination == null;
        }

        // Two steps
        if (colDiff == 0 &&
                rowDiff == 2 * direction) {

            boolean startingPosition =
                    (piece.isWhite() && startRow == 6) ||
                            (!piece.isWhite() && startRow == 1);

            if (!startingPosition) {
                return false;
            }

            if (destination != null) {
                return false;
            }

            int middleRow =
                    startRow + direction;

            return board.getPiece(
                    middleRow,
                    startCol
            ) == null;
        }

        // Diagonal capture
        if (colDiff == 1 &&
                rowDiff == direction) {

            return destination != null &&
                    destination.isWhite()
                            != piece.isWhite();
        }

        return false;
    }
}
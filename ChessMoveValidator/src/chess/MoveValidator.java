package chess;

public class MoveValidator {

    public boolean validateMove(
            Board board,
            int startRow,
            int startCol,
            int endRow,
            int endCol,
            boolean whiteTurn) {

        // ==========================================
        // 1. BOARD LIMIT
        // ==========================================

        if (!board.isInsideBoard(startRow, startCol) ||
                !board.isInsideBoard(endRow, endCol)) {

            return false;
        }

        // ==========================================
        // 2. STARTING PIECE
        // ==========================================

        ChessPiece piece =
                board.getPiece(startRow, startCol);

        if (piece == null) {

            System.out.println(
                    "There is no piece at starting position."
            );

            return false;
        }

        // ==========================================
        // 3. TURN CHECK
        // ==========================================

        if (piece.isWhite() != whiteTurn) {

            System.out.println(
                    "It is "
                            + (whiteTurn ? "White" : "Black")
                            + "'s turn."
            );

            return false;
        }

        // ==========================================
        // 4. SAME POSITION
        // ==========================================

        if (startRow == endRow &&
                startCol == endCol) {

            return false;
        }

        // ==========================================
        // 5. DESTINATION
        // ==========================================

        ChessPiece destination =
                board.getPiece(endRow, endCol);

        // Apne piece ko capture nahi kar sakte
        if (destination != null &&
                destination.isWhite() == piece.isWhite()) {

            System.out.println(
                    "You cannot capture your own piece."
            );

            return false;
        }

        // ==========================================
        // 6. CASTLING
        // ==========================================

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

        // ==========================================
        // 7. BASIC MOVE VALIDATION
        // ==========================================

        boolean basicMoveValid;

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

            // Knight jump kar sakta hai
            if (basicMoveValid &&
                    !(piece instanceof Knight)) {

                if (!board.isPathClear(
                        startRow,
                        startCol,
                        endRow,
                        endCol)) {

                    System.out.println(
                            "Path is blocked."
                    );

                    basicMoveValid = false;
                }
            }
        }

        if (!basicMoveValid) {

            System.out.println(
                    "Invalid movement for "
                            + piece.getClass().getSimpleName()
            );

            return false;
        }

        // ==========================================
        // 8. TEMPORARILY MAKE THE MOVE
        // ==========================================

        board.movePieceTemporary(
                startRow,
                startCol,
                endRow,
                endCol
        );

        // ==========================================
        // 9. CHECK WHETHER OUR KING IS NOW IN CHECK
        // ==========================================

        CheckDetector checkDetector =
                new CheckDetector();

        boolean kingInCheck =
                checkDetector.isKingInCheck(
                        board,
                        whiteTurn
                );

        // ==========================================
        // 10. UNDO TEMPORARY MOVE
        // ==========================================

        board.movePieceTemporary(
                endRow,
                endCol,
                startRow,
                startCol
        );

        // Restore captured piece
        if (destination != null) {

            board.setPiece(
                    endRow,
                    endCol,
                    destination
            );
        }

        // ==========================================
        // 11. KING SAFETY
        // ==========================================

        if (kingInCheck) {

            System.out.println();
            System.out.println(
                    "❌ Illegal move!"
            );

            System.out.println(
                    "Your King would be in check."
            );

            return false;
        }

        return true;
    }


    // ==========================================
    // CASTLING VALIDATION
    // ==========================================

    private boolean validateCastling(
            Board board,
            int startRow,
            int startCol,
            int endRow,
            int endCol,
            boolean whiteTurn) {

        // ==========================================
        // KING MUST START FROM e1/e8
        // ==========================================

        if (startCol != 4) {
            return false;
        }

        // ==========================================
        // KING MUST NOT ALREADY BE IN CHECK
        // ==========================================

        CheckDetector checkDetector =
                new CheckDetector();

        if (checkDetector.isKingInCheck(
                board,
                whiteTurn)) {

            System.out.println(
                    "❌ Cannot castle while King is in check."
            );

            return false;
        }

        // ==========================================
        // KINGSIDE CASTLING
        // ==========================================

        if (endCol == 6) {

            // White
            if (whiteTurn && startRow == 7) {

                if (board.hasWhiteKingMoved()) {
                    return false;
                }

                if (board.hasWhiteRightRookMoved()) {
                    return false;
                }

                ChessPiece rook =
                        board.getPiece(7, 7);

                if (!(rook instanceof Rook) ||
                        !rook.isWhite()) {

                    return false;
                }

                // f1 and g1 empty
                if (board.getPiece(7, 5) != null ||
                        board.getPiece(7, 6) != null) {

                    System.out.println(
                            "❌ Path is blocked."
                    );

                    return false;
                }

                // King cannot cross attacked square f1
                if (isSquareAttacked(
                        board,
                        7,
                        5,
                        false)) {

                    System.out.println(
                            "❌ King cannot pass through check."
                    );

                    return false;
                }

                // King cannot land on attacked square g1
                if (isSquareAttacked(
                        board,
                        7,
                        6,
                        false)) {

                    System.out.println(
                            "❌ King cannot castle into check."
                    );

                    return false;
                }

                return true;
            }

            // Black
            if (!whiteTurn && startRow == 0) {

                if (board.hasBlackKingMoved()) {
                    return false;
                }

                if (board.hasBlackRightRookMoved()) {
                    return false;
                }

                ChessPiece rook =
                        board.getPiece(0, 7);

                if (!(rook instanceof Rook) ||
                        rook.isWhite()) {

                    return false;
                }

                // f8 and g8 empty
                if (board.getPiece(0, 5) != null ||
                        board.getPiece(0, 6) != null) {

                    System.out.println(
                            "❌ Path is blocked."
                    );

                    return false;
                }

                // f8 attacked?
                if (isSquareAttacked(
                        board,
                        0,
                        5,
                        true)) {

                    System.out.println(
                            "❌ King cannot pass through check."
                    );

                    return false;
                }

                // g8 attacked?
                if (isSquareAttacked(
                        board,
                        0,
                        6,
                        true)) {

                    System.out.println(
                            "❌ King cannot castle into check."
                    );

                    return false;
                }

                return true;
            }
        }

        // ==========================================
        // QUEENSIDE CASTLING
        // ==========================================

        if (endCol == 2) {

            // White
            if (whiteTurn && startRow == 7) {

                if (board.hasWhiteKingMoved()) {
                    return false;
                }

                if (board.hasWhiteLeftRookMoved()) {
                    return false;
                }

                ChessPiece rook =
                        board.getPiece(7, 0);

                if (!(rook instanceof Rook) ||
                        !rook.isWhite()) {

                    return false;
                }

                // b1, c1, d1 empty
                if (board.getPiece(7, 1) != null ||
                        board.getPiece(7, 2) != null ||
                        board.getPiece(7, 3) != null) {

                    System.out.println(
                            "❌ Path is blocked."
                    );

                    return false;
                }

                // d1 attacked?
                if (isSquareAttacked(
                        board,
                        7,
                        3,
                        false)) {

                    System.out.println(
                            "❌ King cannot pass through check."
                    );

                    return false;
                }

                // c1 attacked?
                if (isSquareAttacked(
                        board,
                        7,
                        2,
                        false)) {

                    System.out.println(
                            "❌ King cannot castle into check."
                    );

                    return false;
                }

                return true;
            }

            // Black
            if (!whiteTurn && startRow == 0) {

                if (board.hasBlackKingMoved()) {
                    return false;
                }

                if (board.hasBlackLeftRookMoved()) {
                    return false;
                }

                ChessPiece rook =
                        board.getPiece(0, 0);

                if (!(rook instanceof Rook) ||
                        rook.isWhite()) {

                    return false;
                }

                // b8, c8, d8 empty
                if (board.getPiece(0, 1) != null ||
                        board.getPiece(0, 2) != null ||
                        board.getPiece(0, 3) != null) {

                    System.out.println(
                            "❌ Path is blocked."
                    );

                    return false;
                }

                // d8 attacked?
                if (isSquareAttacked(
                        board,
                        0,
                        3,
                        true)) {

                    System.out.println(
                            "❌ King cannot pass through check."
                    );

                    return false;
                }

                // c8 attacked?
                if (isSquareAttacked(
                        board,
                        0,
                        2,
                        true)) {

                    System.out.println(
                            "❌ King cannot castle into check."
                    );

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

        CheckDetector detector =
                new CheckDetector();

        // Temporary King position check
        ChessPiece original =
                board.getPiece(row, col);

        boolean attacked = false;

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

                // Pawn
                if (piece instanceof Pawn) {

                    int direction =
                            piece.isWhite() ? -1 : 1;

                    if (row - r == direction &&
                            colDiff == 1) {

                        attacked = true;
                    }
                }

                // Knight
                else if (piece instanceof Knight) {

                    if ((rowDiff == 2 &&
                            colDiff == 1) ||
                            (rowDiff == 1 &&
                                    colDiff == 2)) {

                        attacked = true;
                    }
                }

                // King
                else if (piece instanceof King) {

                    if (rowDiff <= 1 &&
                            colDiff <= 1 &&
                            !(rowDiff == 0 &&
                                    colDiff == 0)) {

                        attacked = true;
                    }
                }

                // Other pieces
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

                        attacked = true;
                    }
                }

                if (attacked) {
                    return true;
                }
            }
        }

        return false;
    }


    // ==========================================
    // PAWN VALIDATION
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

        // ==========================================
        // ONE STEP FORWARD
        // ==========================================

        if (colDiff == 0 &&
                rowDiff == direction) {

            return destination == null;
        }

        // ==========================================
        // TWO STEP FROM START
        // ==========================================

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

            if (board.getPiece(
                    middleRow,
                    startCol
            ) != null) {

                return false;
            }

            return true;
        }

        // ==========================================
        // DIAGONAL CAPTURE
        // ==========================================

        if (colDiff == 1 &&
                rowDiff == direction) {

            return destination != null &&
                    destination.isWhite()
                            != piece.isWhite();
        }

        return false;
    }
}
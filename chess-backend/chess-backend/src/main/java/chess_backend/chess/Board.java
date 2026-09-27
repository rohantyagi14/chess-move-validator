package chess_backend.chess;

public class Board {

    private final ChessPiece[][] board;

    // =========================
    // CASTLING RIGHTS
    // =========================

    private boolean whiteKingMoved = false;
    private boolean blackKingMoved = false;

    private boolean whiteLeftRookMoved = false;
    private boolean whiteRightRookMoved = false;

    private boolean blackLeftRookMoved = false;
    private boolean blackRightRookMoved = false;

    // =========================
    // CONSTRUCTOR
    // =========================

    public Board() {
        board = new ChessPiece[8][8];
        setupBoard();
    }

    // =========================
    // SETUP BOARD
    // =========================

    private void setupBoard() {

        // Black pieces
        board[0][0] = new Rook(false);
        board[0][1] = new Knight(false);
        board[0][2] = new Bishop(false);
        board[0][3] = new Queen(false);
        board[0][4] = new King(false);
        board[0][5] = new Bishop(false);
        board[0][6] = new Knight(false);
        board[0][7] = new Rook(false);

        // Black pawns
        for (int col = 0; col < 8; col++) {
            board[1][col] = new Pawn(false);
        }

        // White pawns
        for (int col = 0; col < 8; col++) {
            board[6][col] = new Pawn(true);
        }

        // White pieces
        board[7][0] = new Rook(true);
        board[7][1] = new Knight(true);
        board[7][2] = new Bishop(true);
        board[7][3] = new Queen(true);
        board[7][4] = new King(true);
        board[7][5] = new Bishop(true);
        board[7][6] = new Knight(true);
        board[7][7] = new Rook(true);
    }

    // =========================
    // GET PIECE
    // =========================

    public ChessPiece getPiece(int row, int col) {

        if (!isInsideBoard(row, col)) {
            return null;
        }

        return board[row][col];
    }

    // =========================
    // SET PIECE
    // =========================

    public void setPiece(
            int row,
            int col,
            ChessPiece piece) {

        if (!isInsideBoard(row, col)) {
            return;
        }

        board[row][col] = piece;
    }

    // =========================
    // CHECK BOARD LIMIT
    // =========================

    public boolean isInsideBoard(
            int row,
            int col) {

        return row >= 0 &&
                row < 8 &&
                col >= 0 &&
                col < 8;
    }

    // =========================
    // PATH CLEAR
    // =========================

    public boolean isPathClear(
            int startRow,
            int startCol,
            int endRow,
            int endCol) {

        int rowDirection =
                Integer.compare(endRow, startRow);

        int colDirection =
                Integer.compare(endCol, startCol);

        int row =
                startRow + rowDirection;

        int col =
                startCol + colDirection;

        while (row != endRow ||
                col != endCol) {

            if (board[row][col] != null) {
                return false;
            }

            row += rowDirection;
            col += colDirection;
        }

        return true;
    }

    // =========================
    // MOVE PIECE
    // =========================

    public void movePiece(
            int startRow,
            int startCol,
            int endRow,
            int endCol) {

        ChessPiece piece =
                board[startRow][startCol];

        if (piece == null) {
            return;
        }

        // =========================
        // CASTLING
        // =========================

        if (piece instanceof King &&
                startRow == endRow &&
                Math.abs(endCol - startCol) == 2) {

            // =========================
            // KING SIDE CASTLING
            // =========================

            if (endCol > startCol) {

                // Move King
                board[endRow][endCol] = piece;
                board[startRow][startCol] = null;

                // Move Rook
                ChessPiece rook =
                        board[startRow][7];

                board[startRow][5] = rook;
                board[startRow][7] = null;
            }

            // =========================
            // QUEEN SIDE CASTLING
            // =========================

            else {

                // Move King
                board[endRow][endCol] = piece;
                board[startRow][startCol] = null;

                // Move Rook
                ChessPiece rook =
                        board[startRow][0];

                board[startRow][3] = rook;
                board[startRow][0] = null;
            }

            updateCastlingRights(
                    piece,
                    startRow,
                    startCol,
                    endRow,
                    endCol
            );

            return;
        }

        // =========================
        // NORMAL MOVE
        // =========================

        board[endRow][endCol] =
                board[startRow][startCol];

        board[startRow][startCol] = null;

        updateCastlingRights(
                piece,
                startRow,
                startCol,
                endRow,
                endCol
        );
    }

    // =========================
    // TEMPORARY MOVE
    // Used by MoveValidator
    // =========================

    public void movePieceTemporary(
            int startRow,
            int startCol,
            int endRow,
            int endCol) {

        ChessPiece piece =
                board[startRow][startCol];

        board[endRow][endCol] = piece;
        board[startRow][startCol] = null;
    }

    // =========================
    // CASTLING RIGHTS UPDATE
    // =========================

    private void updateCastlingRights(
            ChessPiece piece,
            int startRow,
            int startCol,
            int endRow,
            int endCol) {

        // =========================
        // KING MOVED
        // =========================

        if (piece instanceof King) {

            if (piece.isWhite()) {
                whiteKingMoved = true;
            } else {
                blackKingMoved = true;
            }
        }

        // =========================
        // ROOK MOVED
        // =========================

        if (piece instanceof Rook) {

            if (piece.isWhite()) {

                // White left rook: a1
                if (startRow == 7 &&
                        startCol == 0) {

                    whiteLeftRookMoved = true;
                }

                // White right rook: h1
                if (startRow == 7 &&
                        startCol == 7) {

                    whiteRightRookMoved = true;
                }

            } else {

                // Black left rook: a8
                if (startRow == 0 &&
                        startCol == 0) {

                    blackLeftRookMoved = true;
                }

                // Black right rook: h8
                if (startRow == 0 &&
                        startCol == 7) {

                    blackRightRookMoved = true;
                }
            }
        }
    }

    // =========================
    // CASTLING GETTERS
    // =========================

    public boolean hasWhiteKingMoved() {
        return whiteKingMoved;
    }

    public boolean hasBlackKingMoved() {
        return blackKingMoved;
    }

    public boolean hasWhiteLeftRookMoved() {
        return whiteLeftRookMoved;
    }

    public boolean hasWhiteRightRookMoved() {
        return whiteRightRookMoved;
    }

    public boolean hasBlackLeftRookMoved() {
        return blackLeftRookMoved;
    }

    public boolean hasBlackRightRookMoved() {
        return blackRightRookMoved;
    }

    // =========================
    // PAWN PROMOTION
    // =========================

    public boolean promotePawn(
            int row,
            int col,
            String promotionPiece) {

        if (!isInsideBoard(row, col)) {
            return false;
        }

        ChessPiece piece =
                board[row][col];

        if (!(piece instanceof Pawn)) {
            return false;
        }

        // White pawn must reach row 0
        boolean whitePromotion =
                piece.isWhite() &&
                        row == 0;

        // Black pawn must reach row 7
        boolean blackPromotion =
                !piece.isWhite() &&
                        row == 7;

        if (!whitePromotion &&
                !blackPromotion) {

            return false;
        }

        if (promotionPiece == null ||
                promotionPiece.isEmpty()) {

            return false;
        }

        boolean white =
                piece.isWhite();

        ChessPiece newPiece;

        switch (
                promotionPiece.toUpperCase()
        ) {

            case "Q":
                newPiece =
                        new Queen(white);
                break;

            case "R":
                newPiece =
                        new Rook(white);
                break;

            case "B":
                newPiece =
                        new Bishop(white);
                break;

            case "N":
                newPiece =
                        new Knight(white);
                break;

            default:
                return false;
        }

        board[row][col] = newPiece;

        return true;
    }

    // =========================
    // PRINT BOARD
    // =========================

    public String printBoard() {

        StringBuilder result =
                new StringBuilder();

        for (int row = 0; row < 8; row++) {

            result.append(
                    8 - row
            ).append(" ");

            for (int col = 0; col < 8; col++) {

                ChessPiece piece =
                        board[row][col];

                if (piece == null) {

                    result.append(". ");

                } else {

                    result.append(
                            piece.getSymbol()
                    ).append(" ");
                }
            }

            result.append("\n");
        }

        result.append(
                "  a b c d e f g h"
        );

        return result.toString();
    }
}
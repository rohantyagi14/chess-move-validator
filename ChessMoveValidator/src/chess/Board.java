package chess;

public class Board {

    private final ChessPiece[][] board;

    // ==========================================
    // CASTLING RIGHTS
    // ==========================================

    private boolean whiteKingMoved = false;
    private boolean blackKingMoved = false;

    private boolean whiteLeftRookMoved = false;
    private boolean whiteRightRookMoved = false;

    private boolean blackLeftRookMoved = false;
    private boolean blackRightRookMoved = false;


    public Board() {

        board = new ChessPiece[8][8];

        setupBoard();
    }


    // ==========================================
    // SETUP
    // ==========================================

    private void setupBoard() {

        // ======================================
        // BLACK BACK ROW
        // ======================================

        board[0][0] = new Rook(false);
        board[0][1] = new Knight(false);
        board[0][2] = new Bishop(false);
        board[0][3] = new Queen(false);
        board[0][4] = new King(false);
        board[0][5] = new Bishop(false);
        board[0][6] = new Knight(false);
        board[0][7] = new Rook(false);


        // ======================================
        // BLACK PAWNS
        // ======================================

        for (int col = 0; col < 8; col++) {

            board[1][col] = new Pawn(false);
        }


        // ======================================
        // WHITE PAWNS
        // ======================================

        for (int col = 0; col < 8; col++) {

            board[6][col] = new Pawn(true);
        }


        // ======================================
        // WHITE BACK ROW
        // ======================================

        board[7][0] = new Rook(true);
        board[7][1] = new Knight(true);
        board[7][2] = new Bishop(true);
        board[7][3] = new Queen(true);
        board[7][4] = new King(true);
        board[7][5] = new Bishop(true);
        board[7][6] = new Knight(true);
        board[7][7] = new Rook(true);
    }


    // ==========================================
    // GET PIECE
    // ==========================================

    public ChessPiece getPiece(
            int row,
            int col) {

        if (!isInsideBoard(row, col)) {

            return null;
        }

        return board[row][col];
    }


    // ==========================================
    // SET PIECE
    // ==========================================

    public void setPiece(
            int row,
            int col,
            ChessPiece piece) {

        if (isInsideBoard(row, col)) {

            board[row][col] = piece;
        }
    }


    // ==========================================
    // BOARD CHECK
    // ==========================================

    public boolean isInsideBoard(
            int row,
            int col) {

        return row >= 0 &&
                row < 8 &&
                col >= 0 &&
                col < 8;
    }


    // ==========================================
    // PATH CHECK
    // ==========================================

    public boolean isPathClear(
            int startRow,
            int startCol,
            int endRow,
            int endCol) {

        int rowStep =
                Integer.compare(
                        endRow,
                        startRow
                );

        int colStep =
                Integer.compare(
                        endCol,
                        startCol
                );

        int currentRow =
                startRow + rowStep;

        int currentCol =
                startCol + colStep;

        while (currentRow != endRow ||
                currentCol != endCol) {

            if (board[currentRow][currentCol] != null) {

                return false;
            }

            currentRow += rowStep;
            currentCol += colStep;
        }

        return true;
    }


    // ==========================================
    // MOVE PIECE
    // ==========================================

    public void movePiece(
            int startRow,
            int startCol,
            int endRow,
            int endCol) {

        ChessPiece piece =
                board[startRow][startCol];

        board[endRow][endCol] = piece;

        board[startRow][startCol] = null;

        // Actual move hone par
        // castling rights update karo

        updateCastlingRights(
                startRow,
                startCol,
                endRow,
                endCol,
                piece
        );
    }


    // ==========================================
    // TEMPORARY MOVE
    // ==========================================
    // Check validation ke time use hoga.
    // Isse castling rights change nahi hongi.

    public void movePieceTemporary(
            int startRow,
            int startCol,
            int endRow,
            int endCol) {

        board[endRow][endCol] =
                board[startRow][startCol];

        board[startRow][startCol] = null;
    }


    // ==========================================
    // UPDATE CASTLING RIGHTS
    // ==========================================

    private void updateCastlingRights(
            int startRow,
            int startCol,
            int endRow,
            int endCol,
            ChessPiece piece) {

        if (piece == null) {

            return;
        }


        // ======================================
        // WHITE KING
        // ======================================

        if (piece instanceof King &&
                piece.isWhite()) {

            whiteKingMoved = true;
        }


        // ======================================
        // BLACK KING
        // ======================================

        if (piece instanceof King &&
                !piece.isWhite()) {

            blackKingMoved = true;
        }


        // ======================================
        // WHITE ROOKS
        // ======================================

        if (piece instanceof Rook &&
                piece.isWhite()) {

            // a1
            if (startRow == 7 &&
                    startCol == 0) {

                whiteLeftRookMoved = true;
            }

            // h1
            if (startRow == 7 &&
                    startCol == 7) {

                whiteRightRookMoved = true;
            }
        }


        // ======================================
        // BLACK ROOKS
        // ======================================

        if (piece instanceof Rook &&
                !piece.isWhite()) {

            // a8
            if (startRow == 0 &&
                    startCol == 0) {

                blackLeftRookMoved = true;
            }

            // h8
            if (startRow == 0 &&
                    startCol == 7) {

                blackRightRookMoved = true;
            }
        }
    }


    // ==========================================
    // WHITE KING
    // ==========================================

    public boolean hasWhiteKingMoved() {

        return whiteKingMoved;
    }


    // ==========================================
    // BLACK KING
    // ==========================================

    public boolean hasBlackKingMoved() {

        return blackKingMoved;
    }


    // ==========================================
    // WHITE LEFT ROOK
    // ==========================================

    public boolean hasWhiteLeftRookMoved() {

        return whiteLeftRookMoved;
    }


    // ==========================================
    // WHITE RIGHT ROOK
    // ==========================================

    public boolean hasWhiteRightRookMoved() {

        return whiteRightRookMoved;
    }


    // ==========================================
    // BLACK LEFT ROOK
    // ==========================================

    public boolean hasBlackLeftRookMoved() {

        return blackLeftRookMoved;
    }


    // ==========================================
    // BLACK RIGHT ROOK
    // ==========================================

    public boolean hasBlackRightRookMoved() {

        return blackRightRookMoved;
    }


    // ==========================================
    // PRINT BOARD
    // ==========================================

    public void printBoard() {

        System.out.println();

        System.out.println(
                "    a   b   c   d   e   f   g   h"
        );

        System.out.println(
                "  +---+---+---+---+---+---+---+---+"
        );

        for (int row = 0; row < 8; row++) {

            System.out.print(
                    (8 - row) + " |"
            );

            for (int col = 0; col < 8; col++) {

                if (board[row][col] == null) {

                    System.out.print("   |");

                } else {

                    System.out.print(
                            " "
                                    + board[row][col]
                                    .getSymbol()
                                    + " |"
                    );
                }
            }

            System.out.println(
                    " " + (8 - row)
            );

            System.out.println(
                    "  +---+---+---+---+---+---+---+---+"
            );
        }

        System.out.println(
                "    a   b   c   d   e   f   g   h"
        );

        System.out.println();
    }
}
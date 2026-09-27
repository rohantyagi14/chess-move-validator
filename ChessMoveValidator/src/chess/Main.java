package chess;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        Board board = new Board();

        MoveValidator validator =
                new MoveValidator();

        CheckDetector checkDetector =
                new CheckDetector();

        PromotionManager promotionManager =
                new PromotionManager();

        boolean whiteTurn = true;

        boolean gameOver = false;

        System.out.println();
        System.out.println("==================================");
        System.out.println("          JAVA CHESS GAME");
        System.out.println("==================================");
        System.out.println();

        while (!gameOver) {

            // ======================================
            // PRINT BOARD
            // ======================================

            board.printBoard();

            System.out.println();

            System.out.println(
                    whiteTurn
                            ? "White's turn"
                            : "Black's turn"
            );

            System.out.print(
                    "Enter move (example: e2 e4): "
            );

            String input =
                    scanner.nextLine();

            // ======================================
            // EXIT
            // ======================================

            if (input.equalsIgnoreCase("exit")) {

                System.out.println("Game ended.");

                break;
            }

            // ======================================
            // INPUT
            // ======================================

            String[] positions =
                    input.trim().split("\\s+");

            if (positions.length != 2) {

                System.out.println(
                        "❌ Invalid input!"
                );

                continue;
            }

            // ======================================
            // CONVERT POSITIONS
            // ======================================

            int[] start =
                    convertPosition(positions[0]);

            int[] end =
                    convertPosition(positions[1]);

            if (start == null || end == null) {

                System.out.println(
                        "❌ Invalid chess position!"
                );

                continue;
            }

            int startRow = start[0];
            int startCol = start[1];

            int endRow = end[0];
            int endCol = end[1];

            // ======================================
            // GET PIECE
            // ======================================

            ChessPiece piece =
                    board.getPiece(
                            startRow,
                            startCol
                    );

            if (piece == null) {

                System.out.println(
                        "❌ There is no piece at "
                                + positions[0]
                );

                continue;
            }

            // ======================================
            // CHECK PIECE COLOR
            // ======================================

            if (piece.isWhite() != whiteTurn) {

                System.out.println(
                        "❌ It's "
                                + (whiteTurn
                                ? "White's"
                                : "Black's")
                                + " turn!"
                );

                continue;
            }

            // ======================================
            // CAPTURED PIECE
            // ======================================

            ChessPiece captured =
                    board.getPiece(
                            endRow,
                            endCol
                    );

            // ======================================
            // CHECK CASTLING
            // ======================================

            boolean isCastling =
                    piece instanceof King &&
                            Math.abs(endCol - startCol) == 2;

            // ======================================
            // VALIDATE MOVE
            // ======================================

            boolean valid =
                    validator.validateMove(
                            board,
                            startRow,
                            startCol,
                            endRow,
                            endCol,
                            whiteTurn
                    );

            if (!valid) {

                System.out.println(
                        "❌ Move rejected!"
                );

                continue;
            }

            // ======================================
            // ACTUAL KING / NORMAL MOVE
            // ======================================

            board.movePiece(
                    startRow,
                    startCol,
                    endRow,
                    endCol
            );

            // ======================================
            // CASTLING - MOVE ROOK
            // ======================================

            if (isCastling) {

                // ==================================
                // KINGSIDE
                // King e1 -> g1
                // Rook h1 -> f1
                // ==================================

                if (endCol == 6) {

                    board.movePiece(
                            startRow,
                            7,
                            endRow,
                            5
                    );

                    System.out.println(
                            "♜ Castling performed!"
                    );
                }

                // ==================================
                // QUEENSIDE
                // King e1 -> c1
                // Rook a1 -> d1
                // ==================================

                else if (endCol == 2) {

                    board.movePiece(
                            startRow,
                            0,
                            endRow,
                            3
                    );

                    System.out.println(
                            "♜ Castling performed!"
                    );
                }
            }

            // ======================================
            // PAWN PROMOTION
            // ======================================

            ChessPiece movedPiece =
                    board.getPiece(
                            endRow,
                            endCol
                    );

            if (movedPiece instanceof Pawn) {

                // ==================================
                // WHITE PAWN -> RANK 8
                // ==================================

                if (movedPiece.isWhite() &&
                        endRow == 0) {

                    ChessPiece promotedPiece =
                            promotionManager.promote(
                                    true,
                                    scanner
                            );

                    board.setPiece(
                            endRow,
                            endCol,
                            promotedPiece
                    );

                    System.out.println(
                            "♙ Pawn promoted to "
                                    + promotedPiece
                                    .getClass()
                                    .getSimpleName()
                    );
                }

                // ==================================
                // BLACK PAWN -> RANK 1
                // ==================================

                else if (!movedPiece.isWhite() &&
                        endRow == 7) {

                    ChessPiece promotedPiece =
                            promotionManager.promote(
                                    false,
                                    scanner
                            );

                    board.setPiece(
                            endRow,
                            endCol,
                            promotedPiece
                    );

                    System.out.println(
                            "♟ Pawn promoted to "
                                    + promotedPiece
                                    .getClass()
                                    .getSimpleName()
                    );
                }
            }

            // ======================================
            // CAPTURE MESSAGE
            // ======================================

            if (captured != null) {

                System.out.println(
                        "⚔ Captured "
                                + captured.getSymbol()
                );
            }

            System.out.println(
                    "✅ Move successful!"
            );

            // ======================================
            // CHANGE TURN
            // ======================================

            whiteTurn = !whiteTurn;

            // ======================================
            // CHECK
            // ======================================

            boolean check =
                    checkDetector.isKingInCheck(
                            board,
                            whiteTurn
                    );

            if (check) {

                System.out.println();

                System.out.println(
                        "⚠️ "
                                + (whiteTurn
                                ? "WHITE"
                                : "BLACK")
                                + " KING IS IN CHECK!"
                );

                // ==================================
                // CHECKMATE
                // ==================================

                boolean checkmate =
                        checkDetector.isCheckmate(
                                board,
                                whiteTurn
                        );

                if (checkmate) {

                    System.out.println();

                    System.out.println(
                            "=================================="
                    );

                    System.out.println(
                            "          ♚ CHECKMATE! ♚"
                    );

                    System.out.println(
                            (whiteTurn
                                    ? "Black"
                                    : "White")
                                    + " wins!"
                    );

                    System.out.println(
                            "=================================="
                    );

                    // Final board
                    System.out.println();

                    board.printBoard();

                    gameOver = true;

                    continue;
                }

                System.out.println();
            }
        }

        scanner.close();

        System.out.println();
        System.out.println("Chess game finished.");
    }

    // ==========================================
    // CONVERT POSITION
    // ==========================================

    private static int[] convertPosition(
            String position) {

        if (position == null ||
                position.length() != 2) {

            return null;
        }

        char file =
                Character.toLowerCase(
                        position.charAt(0)
                );

        char rank =
                position.charAt(1);

        if (file < 'a' ||
                file > 'h') {

            return null;
        }

        if (rank < '1' ||
                rank > '8') {

            return null;
        }

        int col =
                file - 'a';

        int row =
                8 - (rank - '0');

        return new int[]{
                row,
                col
        };
    }
}
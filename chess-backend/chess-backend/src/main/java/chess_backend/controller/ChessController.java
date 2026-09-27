package chess_backend.controller;

import chess_backend.chess.Board;
import chess_backend.chess.ChessPiece;
import chess_backend.chess.CheckDetector;
import chess_backend.chess.CheckmateDetector;
import chess_backend.chess.MoveValidator;
import chess_backend.chess.StalemateDetector;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/")
@CrossOrigin
public class ChessController {

    private Board board;
    private final MoveValidator validator;
    private boolean whiteTurn = true;

    public ChessController() {
        board = new Board();
        validator = new MoveValidator();
    }

    // =========================
    // GET BOARD
    // =========================
    @GetMapping("/board")
    public String getBoard() {
        return board.printBoard();
    }

    // =========================
    // MOVE PIECE
    // =========================
    @PostMapping("/move")
    public String move(
            @RequestParam String from,
            @RequestParam String to,
            @RequestParam(defaultValue = "Q") String promotion) {

        int[] start = convertPosition(from);
        int[] end = convertPosition(to);

        if (start == null || end == null) {
            return "Invalid position";
        }

        ChessPiece piece =
                board.getPiece(start[0], start[1]);

        if (piece == null) {
            return "No piece at " + from;
        }

        if (piece.isWhite() != whiteTurn) {
            return "Not your turn";
        }

        boolean valid =
                validator.validateMove(
                        board,
                        start[0],
                        start[1],
                        end[0],
                        end[1],
                        whiteTurn
                );

        if (!valid) {
            return "Invalid move";
        }

        // Move piece
        board.movePiece(
                start[0],
                start[1],
                end[0],
                end[1]
        );

        // =========================
        // PAWN PROMOTION
        // =========================
        ChessPiece movedPiece =
                board.getPiece(end[0], end[1]);

        if (movedPiece instanceof chess_backend.chess.Pawn) {

            boolean reachedLastRank =
                    (movedPiece.isWhite() && end[0] == 0) ||
                            (!movedPiece.isWhite() && end[0] == 7);

            if (reachedLastRank) {

                boolean promoted =
                        board.promotePawn(
                                end[0],
                                end[1],
                                promotion
                        );

                if (!promoted) {
                    return "Invalid promotion piece. Use Q, R, B or N.";
                }
            }
        }

        // Change turn
        whiteTurn = !whiteTurn;

        return "Move successful: " + from + " -> " + to;
    }

    // =========================
    // LEGAL MOVES
    // =========================
    @GetMapping("/legal-moves")
    public String legalMoves(
            @RequestParam String position) {

        int[] start = convertPosition(position);

        if (start == null) {
            return "";
        }

        ChessPiece piece =
                board.getPiece(start[0], start[1]);

        if (piece == null) {
            return "";
        }

        if (piece.isWhite() != whiteTurn) {
            return "";
        }

        StringBuilder moves =
                new StringBuilder();

        for (int row = 0; row < 8; row++) {

            for (int col = 0; col < 8; col++) {

                if (validator.validateMove(
                        board,
                        start[0],
                        start[1],
                        row,
                        col,
                        whiteTurn)) {

                    if (moves.length() > 0) {
                        moves.append(",");
                    }

                    moves.append(
                            convertToPosition(row, col)
                    );
                }
            }
        }

        return moves.toString();
    }

    // =========================
    // CHECK STATUS
    // =========================
    @GetMapping("/check-status")
    public String checkStatus() {

        CheckDetector checkDetector =
                new CheckDetector();

        boolean whiteInCheck =
                checkDetector.isKingInCheck(
                        board,
                        true
                );

        boolean blackInCheck =
                checkDetector.isKingInCheck(
                        board,
                        false
                );

        if (whiteInCheck) {
            return "WHITE_CHECK";
        }

        if (blackInCheck) {
            return "BLACK_CHECK";
        }

        return "NO_CHECK";
    }

    // =========================
    // GAME STATUS
    // =========================
    @GetMapping("/game-status")
    public String gameStatus() {

        CheckDetector checkDetector =
                new CheckDetector();

        CheckmateDetector checkmateDetector =
                new CheckmateDetector();

        StalemateDetector stalemateDetector =
                new StalemateDetector();

        boolean whiteInCheck =
                checkDetector.isKingInCheck(
                        board,
                        true
                );

        boolean blackInCheck =
                checkDetector.isKingInCheck(
                        board,
                        false
                );

        if (whiteInCheck &&
                checkmateDetector.isCheckmate(
                        board,
                        true)) {

            return "WHITE_CHECKMATE";
        }

        if (blackInCheck &&
                checkmateDetector.isCheckmate(
                        board,
                        false)) {

            return "BLACK_CHECKMATE";
        }

        if (!whiteInCheck &&
                stalemateDetector.isStalemate(
                        board,
                        true)) {

            return "STALEMATE";
        }

        if (!blackInCheck &&
                stalemateDetector.isStalemate(
                        board,
                        false)) {

            return "STALEMATE";
        }

        if (whiteInCheck) {
            return "WHITE_CHECK";
        }

        if (blackInCheck) {
            return "BLACK_CHECK";
        }

        return "NO_CHECK";
    }

    // =========================
    // RESET GAME
    // =========================
    @PostMapping("/reset")
    public String resetGame() {

        board = new Board();
        whiteTurn = true;

        return "Game reset successfully";
    }

    // =========================
    // HELLO
    // =========================
    @GetMapping("/hello")
    public String hello() {
        return "Chess Backend is running!";
    }

    // =========================
    // CONVERT e2 -> [6,4]
    // =========================
    private int[] convertPosition(String position) {

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

        if (file < 'a' || file > 'h') {
            return null;
        }

        if (rank < '1' || rank > '8') {
            return null;
        }

        int col = file - 'a';
        int row = 8 - (rank - '0');

        return new int[]{row, col};
    }

    // =========================
    // [6,4] -> e2
    // =========================
    private String convertToPosition(
            int row,
            int col) {

        char file =
                (char) ('a' + col);

        char rank =
                (char) ('8' - row);

        return "" + file + rank;
    }
}
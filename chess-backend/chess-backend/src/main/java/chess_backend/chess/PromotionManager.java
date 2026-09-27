package chess_backend.chess;

public class PromotionManager {

    public ChessPiece promote(boolean white, String choice) {

        switch (choice) {

            case "1":
                return new Queen(white);

            case "2":
                return new Rook(white);

            case "3":
                return new Bishop(white);

            case "4":
                return new Knight(white);

            default:
                // Default Queen
                return new Queen(white);
        }
    }
}
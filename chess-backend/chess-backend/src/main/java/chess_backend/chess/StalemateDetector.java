package chess_backend.chess;

public class StalemateDetector {

    private final CheckDetector checkDetector;
    private final MoveValidator moveValidator;

    public StalemateDetector() {
        checkDetector = new CheckDetector();
        moveValidator = new MoveValidator();
    }

    public boolean isStalemate(
            Board board,
            boolean whiteTurn) {

        // Agar king check me hai,
        // to stalemate nahi ho sakta
        if (checkDetector.isKingInCheck(
                board,
                whiteTurn)) {

            return false;
        }

        // Current player ke saare pieces check karo
        for (int startRow = 0; startRow < 8; startRow++) {

            for (int startCol = 0; startCol < 8; startCol++) {

                ChessPiece piece =
                        board.getPiece(startRow, startCol);

                if (piece == null) {
                    continue;
                }

                // Sirf current player ke pieces
                if (piece.isWhite() != whiteTurn) {
                    continue;
                }

                // Har possible destination check karo
                for (int endRow = 0; endRow < 8; endRow++) {

                    for (int endCol = 0; endCol < 8; endCol++) {

                        boolean validMove =
                                moveValidator.validateMove(
                                        board,
                                        startRow,
                                        startCol,
                                        endRow,
                                        endCol,
                                        whiteTurn
                                );

                        // Ek bhi legal move mila
                        // to stalemate nahi hai
                        if (validMove) {
                            return false;
                        }
                    }
                }
            }
        }

        // Check me nahi hai + koi legal move nahi hai
        // = STALEMATE
        return true;
    }
}
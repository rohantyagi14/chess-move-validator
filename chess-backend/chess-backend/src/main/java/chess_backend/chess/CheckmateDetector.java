package chess_backend.chess;

public class CheckmateDetector {

    private final CheckDetector checkDetector;
    private final MoveValidator moveValidator;

    public CheckmateDetector() {
        checkDetector = new CheckDetector();
        moveValidator = new MoveValidator();
    }

    public boolean isCheckmate(
            Board board,
            boolean whiteKing) {

        // Step 1: King check me hi nahi hai
        // to checkmate possible nahi hai
        if (!checkDetector.isKingInCheck(
                board,
                whiteKing)) {

            return false;
        }

        // Step 2: Us side ke saare pieces check karo
        for (int startRow = 0; startRow < 8; startRow++) {

            for (int startCol = 0; startCol < 8; startCol++) {

                ChessPiece piece =
                        board.getPiece(startRow, startCol);

                if (piece == null) {
                    continue;
                }

                // Sirf checked side ke pieces
                if (piece.isWhite() != whiteKing) {
                    continue;
                }

                // Step 3: Har possible destination try karo
                for (int endRow = 0; endRow < 8; endRow++) {

                    for (int endCol = 0; endCol < 8; endCol++) {

                        boolean validMove =
                                moveValidator.validateMove(
                                        board,
                                        startRow,
                                        startCol,
                                        endRow,
                                        endCol,
                                        whiteKing
                                );

                        // Ek bhi legal move mil gaya
                        // to checkmate nahi hai
                        if (validMove) {
                            return false;
                        }
                    }
                }
            }
        }

        // King check me hai aur koi legal move nahi hai
        return true;
    }
}
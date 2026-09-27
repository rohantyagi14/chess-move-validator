package chess;

import java.util.Scanner;

public class PromotionManager {

    public ChessPiece promote(
            boolean white,
            Scanner scanner) {

        System.out.println();
        System.out.println("==================================");
        System.out.println("          PAWN PROMOTION");
        System.out.println("==================================");

        System.out.println("Choose a piece:");
        System.out.println("1. Queen");
        System.out.println("2. Rook");
        System.out.println("3. Bishop");
        System.out.println("4. Knight");

        System.out.print("Enter choice: ");

        String choice = scanner.nextLine();

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
                System.out.println(
                        "❌ Invalid choice! Queen selected."
                );

                return new Queen(white);
        }
    }
}
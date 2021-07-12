package main;
import java.awt.Color;
import java.util.ArrayList;

public class algorithm {
	
	static ArrayList<Color> legalMoveColors = new ArrayList<Color>();
	static Color tempColor;

	// Updates all pieces' legal moves.
	public static void updateAllPieces() {
		
		// Sets hasMoved.
		for (int i = 0; i < chessboard.LAN_BOARD.size(); i++) 
			if(chessboard.LAN_BOARD.get(i).originalLocation != chessboard.LAN_BOARD.get(i).getLocation())
				chessboard.LAN_BOARD.get(i).hasMoved = true;

		// Resets legal moves and updates them for each piece.		
		for (int i = 0; i < chessboard.LAN_BOARD.size(); i++) {
			chessboard.LAN_BOARD.get(i).resetLegalMoves();
			chessboard.LAN_BOARD.get(i).updateLegalMoves(chessboard.checkPiece);
		}
	}
	
	
	// Piece by piece, displays all legal moves by highlighting in yellow.
	public static void displayAllPossibleMoves() {
		
		Color tempColor;
		ArrayList<Color> legalMoveColors = new ArrayList<Color>();

		int sum = 0;
		for (int i = 0; i < chessboard.LAN_BOARD.size(); i++) {
			sum += chessboard.LAN_BOARD.get(i).getLegalMoves().size();
			System.out.printf("%d had %d moves: ", chessboard.LAN_BOARD.get(i).getLocation(), chessboard.LAN_BOARD.get(i).getLegalMoves().size());

			for (int j = 0; j < chessboard.LAN_BOARD.get(i).getLegalMoves().size(); j++) {
				System.out.printf("%d ", chessboard.LAN_BOARD.get(i).getLegalMoves().get(j));				
			}
			System.out.printf("%n");
			
			if (chessboard.squareArray.get(chessboard.LAN_BOARD.get(i).getLocation()).getBackground() == Color.DARK_GRAY)
				tempColor = Color.DARK_GRAY;
			else
				tempColor = Color.WHITE;
			
			main.chessboard.squareArray.get(main.chessboard.LAN_BOARD.get(i).getLocation()).setBackground(Color.RED);
			
			for (int j = 0; j < chessboard.LAN_BOARD.get(i).getLegalMoves().size(); j++) {
				
				if(chessboard.squareArray.get(chessboard.LAN_BOARD.get(i).getLegalMoves().get(j)).getBackground() == Color.DARK_GRAY)
					legalMoveColors.add(Color.DARK_GRAY);
				else
					legalMoveColors.add(Color.WHITE);
				chessboard.squareArray.get(chessboard.LAN_BOARD.get(i).getLegalMoves().get(j)).setBackground(Color.YELLOW);
			}

//			Scanner input = new Scanner(System.in);  // Create a Scanner object
//    		System.out.println("Enter anything to continue..");
//    		String userName = input.nextLine();  // Read user input
//    
    		if (tempColor == Color.DARK_GRAY)
				chessboard.squareArray.get(chessboard.LAN_BOARD.get(i).getLocation()).setBackground(Color.DARK_GRAY);
			else
				chessboard.squareArray.get(chessboard.LAN_BOARD.get(i).getLocation()).setBackground(Color.WHITE);

	 		for (int j = 0; j < legalMoveColors.size(); j++) {
				if (legalMoveColors.get(j) == Color.DARK_GRAY) {
					main.chessboard.squareArray.get(chessboard.LAN_BOARD.get(i).getLegalMoves().get(j)).setBackground(Color.DARK_GRAY);
				} else if (legalMoveColors.get(j) != null) {
					main.chessboard.squareArray.get(chessboard.LAN_BOARD.get(i).getLegalMoves().get(j)).setBackground(Color.WHITE);
				}	
			}
			legalMoveColors.clear();
		}
		System.out.printf("Total possible Legal Moves = %d", sum);
	}
}

package main;

import java.awt.*;
import javax.swing.*;
import java.util.ArrayList;

public class chessboard implements Runnable {

	static JFrame window = new JFrame("Surchessake");
	static JPanel screen = new JPanel();
	static JPanel board = new JPanel();
	
	static Cursor currentCursorType = Cursor.getDefaultCursor();
	static PGN pgnMoves;
	
	static ArrayList<Piece> LAN_BOARD = new ArrayList<Piece>();
	static ArrayList<JPanel> squareArray = new ArrayList<JPanel>();
	static ArrayList<JLabel> pieceIconArray = new ArrayList<JLabel>();
	static int checkPiece[] = new int[64];
	
	public static void updateCheckPiece(ArrayList<Piece> pieceArray) {
		
		for (int i = 0; i < 64; i++) {
			checkPiece[i] = -1;
		}
			
		for (int i = 0; i < pieceArray.size(); i++) {
			checkPiece[pieceArray.get(i).getLocation()] = pieceArray.get(i).getType();
		}
		
	}
	
	// Function to display the Java JFrame window with the chessboard layers
	public static void displayBoard() {

		// The JFrame window to display the game
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setSize(640, 640);
		window.setLayout(null);
		
		// The JPanel screen that sits in the JFrame window and contains the board
		screen.setSize(640, 640);
		screen.setLayout(null);
		screen.setBackground(Color.LIGHT_GRAY);
		
		// The JPanel that displays the chess board and sits in the screen
		board.setSize(512, 512);
		board.setLocation(64, 44);
		board.setBackground(Color.BLACK);
		board.setLayout(new GridLayout(8, 8, 0, 0));

		int squareIndex = 0;
		int flag = 0;

		// For loop to add Mouse Listeners to each Square, 64 of which are added to the Chessboard
		for(int i = 0; i < 8; i++) { 
			for(int j = 0; j < 8; j++) {
				
				JPanel square = new JPanel();

				// Beautiful ASCII fleing.
				char letter = (char) (j + 65);
				char number = (char) (8 - i + 48);

				square.setSize(64,64);
				
				// Initializing MouseListeners for each square.
				Mouse mouseListener = new Mouse();
				mouseListener.setName(Character.toString(letter) + Character.toString(number));
				mouseListener.setLocation(squareIndex);
				square.addMouseListener(mouseListener);

				if (flag == 0){
					square.setBackground(Color.WHITE);
					 flag = 1;
				}else {
					square.setBackground(Color.DARK_GRAY);
					 flag = 0;
				}
				
				JLabel piece = new JLabel();
				
				// If the square is the location of a piece, get correct icon.
				for(int k=0; k<LAN_BOARD.size(); k++) {
					if(squareIndex == LAN_BOARD.get(k).getLocation())
						piece.setIcon(new ImageIcon(LAN_BOARD.get(k).getImgsrc()));
				}

				// Add the piece to the array and square.
				pieceIconArray.add(piece);
				piece.setVisible(true);
				piece.setSize(64,64);
				square.add(piece);

				// add square to the arraylist
				squareArray.add(square);

				square.setVisible(true);
				square.setLayout(null);
				board.add(square);

				squareIndex++;

			} // For each column of the board.
		
			// Flags for alternating square color.
			if (flag == 0)
				flag = 1;
			else 
				flag = 0;
				
		} // For each row of the board.	

		// Set visibility.
		board.setVisible(true);
		screen.setVisible(true);
		window.setVisible(true);
		
		// Add board to screen, and screen to window.
		screen.add(board);
		window.add(screen);
		
	} // displayBoard()
	
	// Live Piece Drag
	public void run() {
		try{
			
			// Update check piece array and all piece legal moves.
			updateCheckPiece(LAN_BOARD);
			algorithm.updateAllPieces();

			// New panel for picking up a piece.
			JPanel panelInHand = new JPanel();
			panelInHand.setOpaque(false);
			panelInHand.setSize(64,64);
			panelInHand.setLayout(null);
			
			// New label to put on new paenl.
			JLabel labelnHand = new JLabel();
			labelnHand.setSize(64,64);
			labelnHand.setVisible(true);

			// Add the label to the panel.
			panelInHand.add(labelnHand);

			// Make window glass pane.
			window.setGlassPane(panelInHand);
			window.getGlassPane().setSize(64,64);
			
			// Start off not holding a piece, and able to mess with the board.
			boolean holdPiece = false;
			boolean messWithTheBoard = true;
			
			while(true) {
				Mouse.currentPointerLocation = MouseInfo.getPointerInfo().getLocation();
				
				// If the mouse is on a square with a piece.
				if(Piece.isPiece(Mouse.currentSquare, LAN_BOARD) && holdPiece == false && messWithTheBoard == true) {
					if(Mouse.clicked == true) {
						labelnHand.setIcon(new ImageIcon(LAN_BOARD.get(Piece.getPieceIndex(Mouse.originalSquare, LAN_BOARD)).getImgsrc()));
						holdPiece = true;
						messWithTheBoard = false;
						StringBuilder transparent = new StringBuilder(LAN_BOARD.get(Piece.getPieceIndex(Mouse.originalSquare, LAN_BOARD)).getImgsrc());
        				transparent.insert(7, 't');
						pieceIconArray.get(Mouse.originalSquare).setIcon(new ImageIcon(transparent.toString()));
					}
				}
				
				// While holding a piece (you can't mess with the board while holding the piece)
				if(currentCursorType == Cursor.getPredefinedCursor(12) && holdPiece == true && messWithTheBoard == false) {
					window.setCursor(currentCursorType);
					panelInHand.setLocation(Mouse.currentPointerLocation.x - 32, Mouse.currentPointerLocation.y - 64);
					panelInHand.setVisible(true);
					messWithTheBoard = false;
				}
				
				// Dropping a piece.
				if(currentCursorType == Cursor.getDefaultCursor() && messWithTheBoard == false && holdPiece == true) {
					
					// If the square to drop on is a legal move.
					if (LAN_BOARD.get(Piece.getPieceIndex(Mouse.originalSquare, LAN_BOARD)).getLegalMoves().contains(Mouse.currentSquare)) {
						System.out.println("That was a legal move!");

						// Sets the icon for the square moved to and removes the icon from the original square.
						pieceIconArray.get(Mouse.currentSquare).setIcon(new ImageIcon(LAN_BOARD.get(Piece.getPieceIndex(Mouse.originalSquare, LAN_BOARD)).getImgsrc()));
						panelInHand.setVisible(false);
						
						// Update Piece ArrayList
						if(Mouse.currentSquare != Mouse.originalSquare) {
							
							PGN.updatePGN(LAN_BOARD.get(Piece.getPieceIndex(Mouse.originalSquare, LAN_BOARD)), Mouse.currentSquare);
							updateCheckPiece(LAN_BOARD);

							if (Piece.isPiece(Mouse.currentSquare, LAN_BOARD) && Mouse.clicked == false) {
								LAN_BOARD.remove(Piece.getPieceIndex(Mouse.currentSquare, LAN_BOARD));
							}
							pieceIconArray.get(Mouse.originalSquare).setIcon(null);	
							LAN_BOARD.get(Piece.getPieceIndex(Mouse.originalSquare, LAN_BOARD)).setLocation(Mouse.currentSquare);
						}
						
						// Display update board with current pieces
						updateCheckPiece(LAN_BOARD);
						algorithm.updateAllPieces();
						

					} else { // If the square to drop on is NOT a legal move
						System.out.println("That was an illegal move!");
						pieceIconArray.get(Mouse.originalSquare).setIcon(new ImageIcon(LAN_BOARD.get(Piece.getPieceIndex(Mouse.originalSquare, LAN_BOARD)).getImgsrc()));
						panelInHand.setVisible(false);
					}
						
					// After dropping a pice, we are no longer holding one and can mess with the board again.
					holdPiece = false;					
					messWithTheBoard = true;

					// Displays all possible moves for each piece.
//					algorithm.displayAllPossibleMoves();
					
				} // Dropping a piece.

			} // while(true)

		} catch (Exception e) {} // Try catch in run().

	} // run()

} // chessboard
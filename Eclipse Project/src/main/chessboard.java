package main;

import java.awt.*;
import java.text.SimpleDateFormat;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Calendar;

public class chessboard implements Runnable {

	static JFrame applicationWindow = new JFrame("Surchessake");
	static JPanel playArea = new JPanel();
	static JPanel chessboard = new JPanel();
	static JPanel statsBoard = new JPanel();
		
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
	
	public static void displayTimerAndMoves() {
		
		JPanel whiteClockPanel = new JPanel();
		JPanel blackClockPanel = new JPanel();
		JPanel movesPanel = new JPanel();

		workers.GameClock whiteTimer = new workers.GameClock(whiteClockPanel);
		workers.GameClock blackTimer = new workers.GameClock(blackClockPanel);

//		workers.GameClock whiteClock = new workers.GameClock();
//		workers.GameClock blackClock = new workers.GameClock();
//		whiteClockPanel.add(whiteClock);
//		blackClockPanel.add(blackClock);

		//SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm:ss a");
		//JLabel timeLabel = new JLabel();
		//String time = timeFormat.format(Calendar.getInstance().getTime());
		//timeLabel.setText(time);
		//timeLabel.setVisible(true);
		//statsBoard.add(timeLabel);
		
		
		whiteClockPanel.setPreferredSize(new Dimension(128, 64));
		whiteClockPanel.setLocation(0, 0);
		whiteClockPanel.setBackground(Color.red);
		whiteClockPanel.setVisible(true);
		
		movesPanel.setPreferredSize(new Dimension(208, 256));
		movesPanel.setLocation(64, 0);
		movesPanel.setBackground(Color.green);
		movesPanel.setVisible(true);
		
		blackClockPanel.setPreferredSize(new Dimension(128, 64));
		blackClockPanel.setLocation(320, 0);
		blackClockPanel.setBackground(Color.blue);
		blackClockPanel.setVisible(true);
		
		statsBoard.setSize(208, 384);
		statsBoard.setLocation(600, 108);
		statsBoard.setBackground(Color.white);
		statsBoard.setVisible(true);
		statsBoard.setLayout(new BorderLayout());
		
		statsBoard.add(whiteClockPanel, BorderLayout.PAGE_START);
		statsBoard.add(movesPanel, BorderLayout.CENTER);
		statsBoard.add(blackClockPanel, BorderLayout.PAGE_END);
	}
	
	// Function to display the Java JFrame applicationWindow with the chesschessboard layers
	public static void displayChessboard() {

		// The JFrame applicationWindow to display the game
		applicationWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		applicationWindow.setSize(868, 635);
		applicationWindow.setLayout(null);
		
		// The JPanel playArea that sits in the JFrame applicationWindow and contains the chessboard
		playArea.setSize(856, 600);
		playArea.setLayout(null);
		playArea.setBackground(Color.pink);
		playArea.add(statsBoard);
		
		// The JPanel that displays the chess chessboard and sits in the playArea
		chessboard.setSize(512, 512);
		chessboard.setLocation(44, 44);
		chessboard.setBackground(null);
		chessboard.setLayout(new GridLayout(8, 8, 0, 0));
		
		// Timer and Move Module
		displayTimerAndMoves();
		
		
		int squareIndex = 0;
		int flag = 0;

		// For loop to add Mouse Listeners to each Square, 64 of which are added to the Chesschessboard
		for(int i = 0; i < 8; i++) { 
			for(int j = 0; j < 8; j++) {
				
				JPanel square = new JPanel();

				// Beautiful ASCII fleing.
				char letter = (char) (j + 97);
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
						piece.setIcon(new ImageIcon(LAN_BOARD.get(Piece.getPieceIndex(squareIndex, LAN_BOARD)).getImgsrc()));
				}

				// Add the piece to the array and square.
				piece.setHorizontalAlignment(SwingConstants.CENTER);
		        piece.setVerticalAlignment(SwingConstants.CENTER);
		        pieceIconArray.add(piece);
				piece.setVisible(true);
				piece.setSize(64,64);
				square.add(piece);

				// add square to the arraylist
				squareArray.add(square);

				square.setVisible(true);
				square.setLayout(null);
				chessboard.add(square);

				squareIndex++;

			} // For each column of the chessboard.
		
			// Flags for alternating square color.
			if (flag == 0)
				flag = 1;
			else 
				flag = 0;
				
		} // For each row of the chessboard.	



		// Set visibility.
		chessboard.setVisible(true);
		playArea.setVisible(true);
		applicationWindow.setVisible(true);
		
		// Add chessboard to playArea, and playArea to applicationWindow.
		playArea.add(chessboard);
		applicationWindow.add(playArea);
		
	} // displaychessboard()
	
	// Live Piece Drag
	public void run() {
		try{
			
			displayChessboard();
			// Update check piece array and all piece legal moves.
			updateCheckPiece(LAN_BOARD);
			algorithm.updateAllPieces();

			// New panel for picking up a piece.
			JPanel panelInHand = new JPanel();
			panelInHand.setOpaque(false);
			panelInHand.setSize(64,64);
			panelInHand.setLayout(null);
			panelInHand.setAlignmentX(SwingConstants.CENTER);
			panelInHand.setAlignmentY(SwingConstants.CENTER);
	        
			// New label to put on new paenl.
			JLabel labelInHand = new JLabel();
			labelInHand.setSize(64,64);
			labelInHand.setVisible(true);
			labelInHand.setHorizontalAlignment(SwingConstants.CENTER);
			labelInHand.setVerticalAlignment(SwingConstants.CENTER);
	        
			// Add the label to the panel.
			panelInHand.add(labelInHand);

			// Make applicationWindow glass pane.
			applicationWindow.setGlassPane(panelInHand);
			applicationWindow.getGlassPane().setSize(64,64);
			
			// Start off not holding a piece, and able to mess with the chessboard.
			boolean holdPiece = false;
			boolean messWithThechessboard = true;
			
			while(true) {
				Mouse.currentPointerLocation = MouseInfo.getPointerInfo().getLocation();
				
				// If the mouse is on a square with a piece.
				if(Piece.isPiece(Mouse.currentSquare, LAN_BOARD) && holdPiece == false && messWithThechessboard == true) {
					if(Mouse.clicked == true && LAN_BOARD.get(Piece.getPieceIndex(Mouse.originalSquare, LAN_BOARD)).getColor() == PGN.turnOrder) {
						labelInHand.setIcon(new ImageIcon(LAN_BOARD.get(Piece.getPieceIndex(Mouse.originalSquare, LAN_BOARD)).getImgsrc()));
						holdPiece = true;
						messWithThechessboard = false;
						StringBuilder transparent = new StringBuilder(LAN_BOARD.get(Piece.getPieceIndex(Mouse.originalSquare, LAN_BOARD)).getImgsrc());
        				transparent.insert(7, 't');
        				pieceIconArray.get(Mouse.originalSquare).setIcon(new ImageIcon(transparent.toString()));
					}
				}
				
				// While holding a piece (you can't mess with the chessboard while holding the piece)
				if(currentCursorType == Cursor.getPredefinedCursor(12) && holdPiece == true && messWithThechessboard == false) {
					applicationWindow.setCursor(currentCursorType);
					panelInHand.setLocation(Mouse.currentPointerLocation.x - 32, Mouse.currentPointerLocation.y - 64);
					panelInHand.setVisible(true);
					
					
					// Legal Moves for the current piece are displayed with a yellow/red dot on top
					for(int i = 0; i < LAN_BOARD.get(Piece.getPieceIndex(Mouse.originalSquare, LAN_BOARD)).getLegalMoves().size(); i++)
					//	if(pieceIconArray.get(LAN_BOARD.get(Piece.getPieceIndex(Mouse.originalSquare, LAN_BOARD)).getLegalMoves().get(i)).getIcon() == null)
							if (!Piece.isPiece(LAN_BOARD.get(Piece.getPieceIndex(Mouse.originalSquare, LAN_BOARD)).getLegalMoves().get(i), LAN_BOARD))
								pieceIconArray.get(LAN_BOARD.get(Piece.getPieceIndex(Mouse.originalSquare, LAN_BOARD)).getLegalMoves().get(i))
//								.setIcon(new ImageIcon("C:/Users/surya/OneDrive/Desktop/Projects/Surchessake/Eclipse Project/images/legalmove.png"));
								.setIcon(new ImageIcon("images/legalmove.png"));
							else {
							pieceIconArray.get(LAN_BOARD.get(Piece.getPieceIndex(Mouse.originalSquare, LAN_BOARD)).getLegalMoves().get(i))
//								.setIcon(new ImageIcon("C:/Users/AMD/Desktop/Surchessake/surchessake/Eclipse Project/images/legalcapture.png"));
								.setIcon(new ImageIcon("images/ct" + Integer.toString(LAN_BOARD.get(Piece.getPieceIndex(LAN_BOARD.get(Piece.getPieceIndex(Mouse.originalSquare, LAN_BOARD)).getLegalMoves().get(i), LAN_BOARD)).getType()) + ".png"));
								//System.out.println("images/ct" + Integer.toString(LAN_BOARD.get().getType()) + ".png");
								
							}
							
					
					messWithThechessboard = false;
				}
				
				// Dropping a piece.
				if(currentCursorType == Cursor.getDefaultCursor() && messWithThechessboard == false && holdPiece == true) {
					
					// Legal Moves for the current piece are reset back to original
					for(int i = 0; i < LAN_BOARD.get(Piece.getPieceIndex(Mouse.originalSquare, LAN_BOARD)).getLegalMoves().size(); i++)
						if(checkPiece[LAN_BOARD.get(Piece.getPieceIndex(Mouse.originalSquare, LAN_BOARD)).getLegalMoves().get(i)] == -1)
							pieceIconArray.get(LAN_BOARD.get(Piece.getPieceIndex(Mouse.originalSquare, LAN_BOARD)).getLegalMoves().get(i)).setIcon(null);
						else {
							pieceIconArray.get(LAN_BOARD.get(Piece.getPieceIndex(Mouse.originalSquare, LAN_BOARD)).getLegalMoves().get(i))
//								.setIcon(new ImageIcon("C:/Users/AMD/Desktop/Surchessake/surchessake/Eclipse Project/images/" + Integer.toString(LAN_BOARD.get(Piece.getPieceIndex(LAN_BOARD.get(Piece.getPieceIndex(Mouse.originalSquare, LAN_BOARD)).getLegalMoves().get(i), LAN_BOARD)).getType()) + ".png"));
								.setIcon(new ImageIcon("images/" + Integer.toString(LAN_BOARD.get(Piece.getPieceIndex(LAN_BOARD.get(Piece.getPieceIndex(Mouse.originalSquare, LAN_BOARD)).getLegalMoves().get(i), LAN_BOARD)).getType()) + ".png"));
								
							}
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
						
						// Display update chessboard with current pieces
						updateCheckPiece(LAN_BOARD);
						algorithm.updateAllPieces();
						Piece.isInCheck(1);
						
						// Not yet implemented, but last move played displayer -
//						squareArray.get(Mouse.originalSquare).setBackground(new Color(102, 102, 255));
//						squareArray.get(Mouse.newSquare).setBackground(new Color(153, 153, 255));
						
						

					} else { // If the square to drop on is NOT a legal move
						System.out.println("That was an illegal move!");
						pieceIconArray.get(Mouse.originalSquare).setIcon(new ImageIcon(LAN_BOARD.get(Piece.getPieceIndex(Mouse.originalSquare, LAN_BOARD)).getImgsrc()));
						panelInHand.setVisible(false);
					}
						
					// After dropping a pice, we are no longer holding one and can mess with the chessboard again.
					holdPiece = false;					
					messWithThechessboard = true;

					// Displays all possible moves for each piece.
//					algorithm.displayAllPossibleMoves();
					
				} // Dropping a piece.

			} // while(true)

		} catch (Exception e) {} // Try catch in run().

	} // run()

} // chessboard
package main;

import piece.*;

import java.awt.*;
import javax.swing.*;
import java.util.ArrayList;

public class chessboard implements Runnable {

	public static String SETUP_BOARD = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";

	static JFrame window = new JFrame("Surchessake");
	static JPanel screen = new JPanel();
	static JPanel board = new JPanel();
	
	static Cursor currentCursorType = Cursor.getDefaultCursor();

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
		
		for (int i = 0; i < 64; i++){
			if(checkPiece[i] > -1 && checkPiece[i] < 10)
					System.out.printf("  %d",checkPiece[i]);
			else
				System.out.printf(" %d",checkPiece[i]);	
			if((i+1)%8 == 0)
				System.out.printf("%n");
				
		}
	}
	
	
	public static boolean isPiece(int location) {
		boolean pieceFound = false;

		for (int i = 0; i < LAN_BOARD.size(); i++) {
			if (location == LAN_BOARD.get(i).getLocation()) 
				pieceFound = true;
		}
		return pieceFound;
	}

	public static int getPieceIndex(int location) {
		int l = -1;
		if(location > 63 || location < 0)
			return 69;
		else {
			for (int i = 0; i < LAN_BOARD.size(); i++) {
				if (location == LAN_BOARD.get(i).getLocation()) 
					l = i;
			}
			return l;
		}
	}
	
	// Function to read a FEN String and create Pieces in the back-end
	public static void readLAN(String LAN) {
		
		char pString[] = LAN.toCharArray();
		int pIndex = 0;
		
		// Looping through each character in a FEN String until just the position is reconstructed
		for (int i = 0; i < LAN.length() && pString[i] != ' '; i++) {

			// Reading piece type
			if(!Character.isDigit(pString[i]) && pString[i]!= '/') {
				
				Piece newPiece = new Piece();
				switch(pString[i]) {
					case 'P':
					case 'p':
						newPiece = new pawn();
						break;
					case 'R':
					case 'r':
						newPiece = new rook();
						break;
					case 'N':
					case 'n':
						newPiece = new knight();
						break;
					case 'B':
					case 'b':
						newPiece = new bishop();
						break;
					case 'Q':
					case 'q':
						newPiece = new queen();
						break;
					case 'K':
					case 'k':
						newPiece = new king();
						break;
				}
				
				
				// type.Piece p = new type.Piece();
				newPiece.importValues(pString[i], pIndex);				
				
				// Adding the Piece to the Piece Array
				LAN_BOARD.add(newPiece);
				pIndex++;
			}
			// Reading piece location
			else if(Character.isDigit(pString[i]) && pString[i]!= '/')
				pIndex += (pString[i] - 48);

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

				char letter = (char) (j + 65);
				char number = (char) (8 - i + 48);

				square.setSize(64,64);
				
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
				
				for(int k=0; k<LAN_BOARD.size(); k++) {
					if(squareIndex == LAN_BOARD.get(k).getLocation())
						piece.setIcon(new ImageIcon(LAN_BOARD.get(k).getImgsrc()));
				}

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
			}
		
			if (flag == 0)
				flag = 1;
			else 
				flag = 0;
				
		}		

		board.setVisible(true);
		screen.setVisible(true);
		window.setVisible(true);
		
		screen.add(board);
		window.add(screen);
//		boardInfo(LAN_BOARD);
		
	}
	
	public static void boardInfo(ArrayList<Piece> board) {

		for (int i = 0; i < board.size(); i++) {
			board.get(i).displayPI();
		}
	}
	
	// Live Piece Drag
	public void run() {
		try{
			
			updateCheckPiece(LAN_BOARD);
			algorithm.displayAllPossibleMoves(LAN_BOARD);
			
			JPanel panelInHand = new JPanel();
			panelInHand.setOpaque(false);
			panelInHand.setSize(64,64);
			panelInHand.setLayout(null);
			
			JLabel labelnHand = new JLabel();
			labelnHand.setSize(64,64);
			labelnHand.setVisible(true);

			panelInHand.add(labelnHand);

			window.setGlassPane(panelInHand);
			window.getGlassPane().setSize(64,64);
			
			boolean holdPiece = false;
			boolean messWithTheBoard = true;
			
			while(true) {
				Mouse.currentPointerLocation = MouseInfo.getPointerInfo().getLocation();
				
				// if the mouse is on a square with a piece
				if(isPiece(Mouse.currentSquare) && holdPiece == false && messWithTheBoard == true) {
					if(Mouse.clicked == true) {
						labelnHand.setIcon(new ImageIcon(LAN_BOARD.get(getPieceIndex(Mouse.originalSquare)).getImgsrc()));
						System.out.print("true");
						holdPiece = true;
						messWithTheBoard = false;
						// t + image source = transparent piece image
						StringBuilder transparent = new StringBuilder(LAN_BOARD.get(getPieceIndex(Mouse.originalSquare)).getImgsrc());
        				transparent.insert(7, 't');
						pieceIconArray.get(Mouse.originalSquare).setIcon(new ImageIcon(transparent.toString()));
//						pieceIconArray.get(Mouse.originalSquare)
					}
				}
				
				// While holding a piece
				// You can't mess with the board while holding the piece
				if(currentCursorType == Cursor.getPredefinedCursor(12) && holdPiece == true && messWithTheBoard == false) {
					window.setCursor(currentCursorType);
					panelInHand.setLocation(Mouse.currentPointerLocation.x - 32, Mouse.currentPointerLocation.y - 64);
					panelInHand.setVisible(true);
					messWithTheBoard = false;
				}
				
				// Dropping a piece.
				if(currentCursorType == Cursor.getDefaultCursor() && messWithTheBoard == false && holdPiece == true) {
					
					// Sets the icon for the square moved to and removes the icon from the original square.
					pieceIconArray.get(Mouse.currentSquare).setIcon(new ImageIcon(LAN_BOARD.get(getPieceIndex(Mouse.originalSquare)).getImgsrc()));
					panelInHand.setVisible(false);
					
					//isPiece(Mouse.currentSquare) && Mouse.clicked == false
					
					// Update Piece ArrayList (LAN_BOARD
					if(Mouse.currentSquare != Mouse.originalSquare) {
						updateCheckPiece(LAN_BOARD);
						if (isPiece(Mouse.currentSquare) && Mouse.clicked == false) {
							LAN_BOARD.remove(getPieceIndex(Mouse.currentSquare));
						}
						pieceIconArray.get(Mouse.originalSquare).setIcon(null);	
						LAN_BOARD.get(getPieceIndex(Mouse.originalSquare)).setLocation(Mouse.currentSquare);
					}
					
					// Display update board with current pieces
					updateCheckPiece(LAN_BOARD);
						
					// After dropping a pice, we are no longer holding one and can mess with the board again.
					holdPiece = false;					
					messWithTheBoard = true;
				}
				
		
			}
		} catch (Exception e) {}
	}

}
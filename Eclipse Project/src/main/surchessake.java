package main;

public class surchessake {

	public static void main(String[] args) {
		
		chessboard.readLAN(chessboard.SETUP_BOARD);
		chessboard.displayBoard();

		
		//Mouse and Piece Movement Thread
		Thread chessboardThread = new Thread(new chessboard());
		
		//Timer Thread
//		Thread timer = new Thread(new update());

		chessboardThread.start();
//		timer.start();
		
	}
	
}

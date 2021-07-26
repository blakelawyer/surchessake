package main;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class surchessake {
	
	static JFrame applicationWindow = new JFrame("Surchessake");
	static JPanel playArea = new JPanel();
	static JButton newGame = new JButton("New Game");
	
	static Thread chessboardThread = new Thread(new chessboard());
	
	public static void main(String[] args) {

		// The JFrame applicationWindow to display the game
		applicationWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		applicationWindow.setSize(868, 635);
		applicationWindow.setLayout(null);
		applicationWindow.add(playArea);
		applicationWindow.setVisible(true);
		
		// The JPanel playArea that sits in the JFrame applicationWindow and contains the chessboard
		playArea.setSize(856, 600);
		playArea.setLayout(null);
		playArea.setBackground(Color.pink);
		playArea.add(workers.displayStatsBoard()); // Displays Stats Board on the play area
		playArea.add(newGame);		
		playArea.setVisible(true);
		
		// New Game Button
		newGame.setLocation(600, 44);
		newGame.setSize(128,64);
		newGame.setVisible(true);
		newGame.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("New Game button pressed!");

				PGN.Tags.clear();
				PGN.Moves.clear();
				PGN.turnOrder = 0; 
				PGN.moveNumber = 1;
				PGN.capture = false;

				main.chessboard.LAN_BOARD.clear();
				main.chessboard.squareArray.clear();
				main.chessboard.pieceIconArray.clear();
				reader.readLAN(reader.SETUP_BOARD);

				playArea.removeAll();
				playArea.repaint();
				playArea.add(workers.displayChessboard());
				
				surchessake.chessboardThread.start();
				playArea.revalidate();
				applicationWindow.revalidate();
			}
		});
		surchessake.playArea.revalidate();
		surchessake.applicationWindow.revalidate();
	}
	
}

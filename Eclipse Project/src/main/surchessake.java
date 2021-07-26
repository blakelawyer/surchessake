package main;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.BorderLayout;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class surchessake {
	
	static JFrame applicationWindow = new JFrame("Surchessake");
	static JButton newGame = new JButton("New Game");
	
	static Thread chessboardThread = new Thread(new chessboard());
	
	public static void main(String[] args) {
		
		GridBagConstraints gbc = new GridBagConstraints();
		
		// The JFrame applicationWindow to display the game
		applicationWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		applicationWindow.setPreferredSize(new Dimension(896, 640));
		applicationWindow.setLayout(new GridBagLayout());
		applicationWindow.setResizable(false);
		
		applicationWindow.setVisible(true);
		
		
		newGame.setPreferredSize(new Dimension(64, 64));
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

				applicationWindow.repaint();

				gbc.gridx = 0;
				gbc.gridy = 0;
				gbc.gridheight = 8;
				gbc.gridwidth = 8;

				gbc.insets = new Insets(0, 0, 0, 64);

				applicationWindow.add(workers.displayChessboard(), gbc);
				
				
				surchessake.chessboardThread.start();
			}
		});
		
		gbc.gridx = 10;
		gbc.gridy = 1;
		gbc.gridheight = 1;
		gbc.gridwidth = 1;
		
		applicationWindow.add(newGame, gbc);
		surchessake.applicationWindow.revalidate();
		applicationWindow.pack();
	}
	
}

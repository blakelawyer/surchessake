package main;

import java.awt.Cursor;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.event.*;

public class Mouse implements MouseListener {
	
	String name;
	int location;
	
	static boolean clicked = false;
	static int onChessboard;
	static int currentSquare;
	static int originalSquare;
	
	
	static Point currentPointerLocation = MouseInfo.getPointerInfo().getLocation();

	
	public String getName() { return name; }
	public void setName(String n) {	name = n; }

	public int getLocation() { return location; }
	public void setLocation(int i) { location = i; }
	
	
	@Override
	public void mouseClicked(MouseEvent e) {
//		System.out.printf("Mouse was clicked on %s%n", name);
		
		// Original Square
		
		if (onChessboard == 1) { // Mouse is still on the chessboard.

				//chessboard.spaceArray.get(originalSquare).setBackground(Color.BLUE);
				// Only implemented to check legal moves, not for check legal as well as keep the 
				// piece held so that we may move it to the newly pressed blue square.
		}
		

		
	}

	@Override
	public void mousePressed(MouseEvent e) {

		clicked = true;
		originalSquare = location;		
		
		
		// Sets the current cursor to a hand cursor		
		chessboard.currentCursorType = Cursor.getPredefinedCursor(12);
		chessboard.screen.setCursor(chessboard.currentCursorType);
		
		// Testing X, Y coordinates of the cursor O(1)
		//System.out.printf("( %d , %d )%n", (int) currentPointerLocation.getX(), (int) currentPointerLocation.getY());
		
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		
		// New Square
		if (onChessboard == 1) { // Mouse is still on the chessboard.

			clicked = false;
			//chessboard.spaceArray.get(originalSquare).setBackground(Color.YELLOW);
			//chessboard.spaceArray.get(currentSquare).setBackground(Color.RED);
		}
		

		chessboard.currentCursorType = Cursor.getDefaultCursor();
		chessboard.screen.setCursor(chessboard.currentCursorType);
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		onChessboard++;
		currentSquare = location;
		//System.out.printf("Mouse has entered %s%n", name);
	}

	@Override
	public void mouseExited(MouseEvent e) {
		onChessboard--;
		//System.out.printf("Mouse has exited %s%n", name);
	}
}

package main;

import java.util.ArrayList;

public class algorithm {
	
	public static void displayAllPossibleMoves(ArrayList<main.Piece> pieceArray) {
		
		int sum = 0;
		for (int i = 0; i < pieceArray.size(); i++){
			pieceArray.get(i).updateLegalMoves(chessboard.checkPiece);
			sum += pieceArray.get(i).getLegalMoves().size();
			System.out.printf("%d had %d moves:! ", pieceArray.get(i).getLocation(), pieceArray.get(i).getLegalMoves().size());
//			for(int j = 0; j < pieceArray.get(i).getLegalMoves().size(); j++)
//				System.out.printf("%d ",pieceArray.get(i).getLegalMoves().get(j));
			System.out.printf("%n");
		}
		System.out.println(sum);
		
	}

}

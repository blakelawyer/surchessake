package piece;
import java.util.ArrayList;

public class rook extends main.Piece {
	
	public ArrayList<Integer> legalMoves = new ArrayList<Integer>();
	
	@Override
	public ArrayList<Integer> getLegalMoves() {
		super.getLegalMoves();
		return legalMoves;
	}
	@Override
	public void updateLegalMoves(int[] indexedPieceArray) {	
		super.updateLegalMoves(indexedPieceArray);
		
	}
}

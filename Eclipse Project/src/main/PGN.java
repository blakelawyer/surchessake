package main;

import java.util.ArrayList;

import javax.swing.text.AbstractDocument.Content;

public class PGN {
	
	static ArrayList<Tag> Tags = new ArrayList<Tag>();
	static ArrayList<Move> Moves = new ArrayList<Move>();
	
	static class Move {
		int moveNumber;
		String algebraicNotationWhite;
		String algebraicNotationBlack;
		
		Move(int m, String anw, String anb) {
			moveNumber = m;
			algebraicNotationWhite = anw;
			algebraicNotationBlack = anb;
		}
		
		// Getters
		public int getMoveNumber() { return moveNumber; }
		public String getAlgebraicNotationWhite() { return algebraicNotationWhite; }
		public String getAlgebraicNotationBlack() { return algebraicNotationBlack; }
		
	}
	
	static class Tag {
		String header;
		String description;
		
		Tag(String h, String d) {
			header = h;
			description = d;
		}
		
		// Getters
		public String getTagHeader() { return header; }
		public String getTagDescription() { return description; }
		
	}

	// Test PGN: [Location "Surya's Apartment"]
	
	public static void readPGN(String givenPGN) {
		char charPGN[] = givenPGN.toCharArray();
		for(int i = 0; i < charPGN.length; i++) {
			
			if(charPGN[i] == '[') {
				String header = "";
				String description = "";
				i++; // Gets inside the starting bracket.
				do { 
					header += Character.toString(charPGN[i]);
					i++;
				} while(charPGN[i]!=' ');
				if(charPGN[i] != '"')
					i++; // Gets inside the double quotes.
				i++;
				do {
					description += Character.toString(charPGN[i]);
					i++;
					if(charPGN[i] == '"')
						i++;
				} while(charPGN[i]!=']');
				Tags.add(new Tag(header, description));
			}
			
			int moveNumber;
			String algebraicNotationWhite = "";
			String algebraicNotationBlack = "";
			if(Character.isDigit(charPGN[i])) {
				moveNumber = charPGN[i] - 48;
				if(Character.isDigit(charPGN[i+1]) && !Character.isDigit(charPGN[i+2])) {
					moveNumber *= 10;
					moveNumber += charPGN[i+1] - 48;
					i++;
				}
				if(Character.isDigit(charPGN[i+2])) {
					moveNumber *= 10;
					moveNumber += charPGN[i+2] - 48;
					i+=2;
				}
								
				
				i+=3; // From move number to first character of move.

				do {
					algebraicNotationWhite += Character.toString(charPGN[i]);
					i++;
				} while (charPGN[i]!= ' ');
				i++;
				do {
					if(charPGN[i] == '{') {
						do { i++; } while(charPGN[i] != '}');
						i+=2;
					}
					algebraicNotationBlack += Character.toString(charPGN[i]);
					i++;
				} while (charPGN[i]!= ' ' && i + 1 < charPGN.length);
				
				Moves.add(new Move(moveNumber, algebraicNotationWhite, algebraicNotationBlack));
			}
		}
		Moves.get(Moves.size() - 1).algebraicNotationBlack += charPGN[charPGN.length - 1];
	}
	
	public static String writePGN() {
		
		String PGNFormat = "";
		
		for(int i = 0; i < Tags.size(); i++) {
			PGNFormat += "[" + Tags.get(i).header + " \"" + Tags.get(i).description + "\"]\r\n";
		}
		PGNFormat += "\r\n";
		for(int i = 0; i < Moves.size(); i++) {
			PGNFormat += Moves.get(i).moveNumber + ". " + Moves.get(i).algebraicNotationWhite + " " + Moves.get(i).algebraicNotationBlack + " ";
		}
		return PGNFormat;
	}
}

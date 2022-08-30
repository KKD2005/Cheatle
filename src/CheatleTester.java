import java.io.FileNotFoundException;

public class CheatleTester {
	public static void main(String[] args) throws FileNotFoundException, BadDictionaryException {

	Cheatle game = new Cheatle("WordleDictionary.txt", "WordleSolutionList.txt");
	game.beginGame();
	
	System.out.println (game.makeGuess("bluey"));
	System.out.println (game.getAlphabet());
	System.out.println (game.getNumRemaining());
	System.out.println (game.getWordsRemaining());
	System.out.println (game.correctPlaceLetters());
	System.out.println (game.wrongPlaceLetters());
	System.out.println (game.eliminatedLetters());
	System.out.println (game.unguessedLetters());
	game.restart();
	System.out.println (game.getAlphabet());
	System.out.println (game.getNumRemaining());
	System.out.println (game.getWordsRemaining());
	System.out.println (game.correctPlaceLetters());
	System.out.println (game.wrongPlaceLetters());
	System.out.println (game.eliminatedLetters());
	System.out.println (game.unguessedLetters());
	}
}

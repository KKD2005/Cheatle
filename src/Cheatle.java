import java.io.File;
import java.util.*;
import java.io.FileNotFoundException;

public class Cheatle {
	
	TreeSet <String>allowable;
	HashSet <String>allSolutions = new HashSet<String>();
	HashSet<String> dictionary = new HashSet<String>();
	TreeSet <Character> characters = new TreeSet <Character>();
	TreeSet <Character> rightCharacters;
	TreeSet <Character> wrongCharacters;
	TreeSet <Character> misplacedCharacters;
	int length;
	StringBuilder alphabet = new StringBuilder ("");
	int guesses = 0;


	//Reads the dictionaryFile and puts all the allowed guesses into a data structure,
	//and also reads the solutionFile and puts all the possible solutions into a data structure,
	//also adding all the possible solutions to the allowed guesses.
	//Throws a BadDictionaryException if not every word in the dictionary & solutions are of the same length 
	public Cheatle(String dictionaryFile, String solutionFile) throws FileNotFoundException, BadDictionaryException {
		
		Scanner scanner1 = new Scanner (new File (dictionaryFile));
		if (scanner1.hasNext()) {
			String word = scanner1.next();
			length = word.length();
			dictionary.add(word);
			for (int i = 0; i<length; i++) {
				characters.add(word.charAt(i));
			}
			
		}
		while (scanner1.hasNext()) {
			String word = scanner1.next();
			if (word.length() !=length) {
				throw new BadDictionaryException();
			}
			dictionary.add(word);
			for (int i = 0; i<length; i++) {
				characters.add(word.charAt(i));
			}
		}
		scanner1.close();
		Scanner scanner2 = new Scanner (new File (solutionFile));
		
		while (scanner2.hasNext()) {
			String word = scanner2.next();
			if (word.length() !=length) {
				throw new BadDictionaryException();
			}
			dictionary.add(word);
			allSolutions.add(word);
			for (int i = 0; i<length; i++) {
				characters.add(word.charAt(i));
			}
		}
		scanner2.close();
for (Character ch:characters) {
			alphabet.append(ch);
		}
		
	}


	//Returns the length of the words in the list of words
	public int getWordLength() {
		return length;
	}

	//Returns the complete alphabet of chars that are used in any word in the solution list,
	//in order as a String
	//LOOK FOR FORMATTING
	public String getAlphabet() {
		
		return alphabet.toString();
	}


	//Begins a game of Cheatle, initializing any private instance variables necessary for
	// a single game.
	public void beginGame() {
		characters = new TreeSet <Character>();
		String letters = alphabet.toString();
		for (int i = 0; i<letters.length();i++) {
			characters.add(letters.charAt(i));
		}
		allowable = new TreeSet <String>();
		for (String word:allSolutions) {
			allowable.add(word);
		}
		rightCharacters = new TreeSet <Character>();
		misplacedCharacters = new TreeSet<Character>();
		wrongCharacters = new TreeSet <Character>();
		guesses=0;
	}

	//Checks to see if the guess is in the dictionary of words.
	//Does NOT check to see whether the guess is one of the REMAINING words.
	public boolean isAllowable(String guess) {
		return dictionary.contains(guess);
	}

	//Given a guess, returns a String of '*', '?', and '!'
	//that gives feedback about the corresponding letters in that guess:
	// * means that letter is not in the word
	// ? means that letter is in the word, but not in that place
	// ! means that letter is in that exact place in the word
	// Because this is CHEATLE, not Wordle, you are to return the feedback
	// that leaves the LARGEST possible number of words remaining!!!
	//makeGuess should also UPDATE the list of remaining words
	// and update the list of letters where we KNOW where they are,
	// the list of letters that are definitely in the word but we don't know where they are,
	// the list of letters that are not in the word,
	// and the list of letters that are still possibilities
	public String makeGuess(String guess) {
		HashMap <String, Integer >solutionsReturn = new HashMap<String, Integer>();

		guesses++;
		solutionsReturn = new HashMap<String, Integer>();
		for (String word:allowable) {
			String feedback = feedback(guess,word);
			solutionsReturn.putIfAbsent(feedback, 0);
			int occurances=solutionsReturn.get(feedback);
			solutionsReturn.replace(feedback, occurances+1);
		}
		Set <String> keys = solutionsReturn.keySet();
		int max = 0;
		String bestFeedback="";
		for (String s:keys) {
			if (solutionsReturn.get(s)>max) {
				max =solutionsReturn.get(s);
				bestFeedback = s;
			} else if (solutionsReturn.get(s)==1&&solutionsReturn.get(s)==max) {
				if (isWinningFeedback(s)) {
					if (max==0) {
						max =solutionsReturn.get(s);
						bestFeedback = s;
					}
				} else {
					max =solutionsReturn.get(s);
					bestFeedback = s;
				}
				
			} 
			
		}
		
		TreeSet <String>newAllowable = new TreeSet <String>();

		for (String s:allowable) {
			String feedback = feedback(guess,s);
			if (feedback.equals(bestFeedback)==true) {
				newAllowable.add(s);
			}
		}
		allowable=newAllowable;
		for (int i=0; i<length; i++) {
			
			if (bestFeedback.charAt(i)=='!') {
				characters.remove (guess.charAt(i));
				wrongCharacters.remove(guess.charAt(i));
				misplacedCharacters.remove(guess.charAt(i));
				rightCharacters.add(guess.charAt(i));
			}else if (bestFeedback.charAt(i)=='*') {
				characters.remove (guess.charAt(i));
				wrongCharacters.add(guess.charAt(i));

			} else {
				characters.remove (guess.charAt(i));
				wrongCharacters.remove(guess.charAt(i));
			if (rightCharacters.contains(guess.charAt(i))==false) {
				misplacedCharacters.add(guess.charAt(i));
			}

			}
		}
		return bestFeedback;
		
	}
	public String feedback (String guess, String answer) {
		String feedback = new String ("");
		
		Character [] guessChar = new Character[length];
		Character []answerChar = new Character[length];
		Character []feedbackChar = new Character[length];
		for (int i=0; i<length; i++) {
			guessChar [i]=guess.charAt(i);
			answerChar[i] = answer.charAt(i);
		}
		for (int i=0; i<length ; i++) {
			if (guessChar[i]==answerChar[i]) {
				feedbackChar[i]='!';
				guessChar[i]=null;
				answerChar [i]=null;
				
			}
		}
		for (int i=0;i<length;i++) {
			if (guessChar[i]!=null) {
				boolean found=false;
				for (int j=0; j<length &&found==false; j++) {
					if (guessChar[i]==answerChar[j]) {
						found=true;
						guessChar[i]=null;
						answerChar[j]=null;
					}
				}
				if (found==true) {
					feedbackChar[i]='?';
				} else {
					feedbackChar[i]='*';
				}
			} 
		}
		for (int i=0;i<length;i++) {
			feedback = feedback+feedbackChar[i];
		}
		return feedback;
		
		
	}
	

	//Returns a String of all letters that have received a ! feedback
	// IN ORDER
	public String correctPlaceLetters() {
		StringBuilder correct = new StringBuilder ("");
		for (Character ch:rightCharacters) {
			correct.append(ch);
		}
		return correct.toString();
	}

	//Returns a String of all letters that have received a ? feedback
	// IN ORDER
	public String wrongPlaceLetters() {
		StringBuilder misplaced = new StringBuilder ("");
		for (Character ch:misplacedCharacters) {
			misplaced.append(ch);
		}
		return misplaced.toString();
	}

	//Returns a String of all letters that have received a * feedback
	// IN ORDER
	public String eliminatedLetters() {
		StringBuilder wrong = new StringBuilder ("");
		for (Character ch:wrongCharacters) {
			wrong.append(ch);
		}
		return wrong.toString();
	}

	//Returns a String of all unguessed letters
	public String unguessedLetters() {
		StringBuilder chars = new StringBuilder ("");
		for (Character ch:characters) {
			chars.append(ch);
		}
		return chars.toString();
	}


	//Returns true if the feedback string is the winning one,
	//i.e. if it is all !s
	public boolean isWinningFeedback(String feedback) {
		if (feedback.length()!=length) {
			return false;
		}
		for (int i=0; i<feedback.length(); i++) {
			if (feedback.charAt(i)!='!') {
				return false;
			}
		}
		return true;
	}

	//Returns a String of all the remaining possible words, with one word per line,
	// IN ORDER
	public String getWordsRemaining() {
		StringBuilder remaining = new StringBuilder ("");
		for (String s:allowable) {
			remaining.append(s);
			remaining.append("\n");
		}
		return remaining.toString();
	}
	
	//Returns the number of possible words remaining
	public int getNumRemaining() {
		return allowable.size();
	}

	//Returns the number of guesses made in this game
	public int numberOfGuessesMade() {
		return guesses;
	}

	//Ends the current game and starts a new game.
	public void restart() {
		beginGame();
	}

}

/* WORD LADDER Main.java
 * EE422C Project 3 submission by
 * <Ashvin Roharia>
 * <ar34426>
 * <16475>
 * <Minkoo Park>
 * <mp32454>
 * <16480>
 * Slip days used: <0>
 * Git URL: https://github.com/aroharia/hw3
 * Fall 2016
 */

package assignment3;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Scanner;
import java.util.Set;
import java.util.Stack;

public class Main {
	
	// static variables and constants only here.
	public static Scanner kb;	// input Scanner for commands
	public static ArrayList<String> start_end_wordList; //array list containing start word and end word
	public static String startWord; //first word for the word ladder
	public static String endWord; //last word for the word ladder
	public static Set<String> dictionaryWords; //all the words in the given dictionary
	
	public static void main(String[] args) throws Exception {
		
		
		PrintStream ps;	// output file
		// If arguments are specified, read/write from/to files instead of Std IO.
		if (args.length != 0) {
			kb = new Scanner(new File(args[0]));
			ps = new PrintStream(new File(args[1]));
			System.setOut(ps);			// redirect output to ps
		} else {
			kb = new Scanner(System.in);// default from Stdin
			ps = System.out;			// default to Stdout
		}
		
		// Reads user input and sets startWord and endWord
		initialize();
		
		//Ashvin is working on currently
		ArrayList<String> testingBFS = getWordLadderBFS(startWord, endWord);
		
		//TO DO
		printLadder(testingBFS);
		
		//TO DO
		//ArrayList<String> testingDFS = getWordLadderDFS(startWord, endWord);
		
		//TO DO
		//printLadder(testingDFS);

	}
	
	public static void initialize() {
		// initialize your static variables or constants here.
		// We will call this method before running our JUNIT tests.  So call it 
		// only once at the start of main.
		dictionaryWords = makeDictionary();
		start_end_wordList = parse(kb);
		startWord = start_end_wordList.get(0).toString();
		endWord = start_end_wordList.get(1).toString();
		
		
	}
	
	/**
	 * @param keyboard Scanner connected to System.in
	 * @return ArrayList of 2 Strings containing start word and end word. 
	 * If command is /quit, return empty ArrayList. 
	 */
	public static ArrayList<String> parse(Scanner keyboard) {
		System.out.println("Enter two words here:");
		String input = keyboard.nextLine();
		
		//Check if "/quit" was entered
		if (input.equals("/quit"))
			return null;
		
		//ArrayList<String> start_end_wordList = (ArrayList<String>) Arrays.asList(input.split(" "));
		ArrayList<String> start_end_wordList = new ArrayList<String>(Arrays.asList(input.split(" ")));
		return start_end_wordList;
	}
	/**
	 * Finds the word ladder of 2 words through DFS using recursion
	 * @param start is the first word as a String
	 * @param end is the last word as a String
	 * @return ArrayList of the ladder or null if it doesn't exist
	 */	
	public static ArrayList<String> getWordLadderDFS(String start, String end) {
		
		// Returned list should be ordered start to end.  Include start and end.
		// Return empty list if no ladder.
		// TODO some code
		Set<String> dict = makeDictionary();
		// TODO more code
		
		return null; // replace this line later with real return
	}
	
	/**
	 * Finds the word ladder of 2 words through breadth first search
	 * @param start is the first word as a String
	 * @param end is the last word as a String
	 * @return ArrayList of the ladder or null if it doesn't exist
	 */
    public static ArrayList<String> getWordLadderBFS(String start, String end) {
		// dict holds all the words in the given dictionary
		Set<String> dict = makeDictionary();
		
		// to make it comparable to dictionary words
		start = start.toUpperCase();
    	end = end.toUpperCase();
    	
		// no word ladder if first/last word are not in dictionary
		if (!(dict.contains(start) && dict.contains(end)))
			return null;
    	
    	// no word ladder if first word = last word
    	if (start.equals(end))
			return null;
		
		// Queue
		Queue<String> queue = new LinkedList<String>();
		queue.add(start);
		
		//  map
		Map<String,String> parentWordMap = new HashMap<String,String>(dict.size());
		parentWordMap.put(start, null);

		// discovered set
		Set<String> discovered = new HashSet<String>(dict.size());
		discovered.add(start);
		
		StartSearch : // label for outer loop to break to
		// while there exists more undiscovered words
		while (!queue.isEmpty()) {
			String current = queue.remove();
			// goes through the word letter by letter (gets length by checking how long the dictionary words are)
			for (int i=0; i<dict.iterator().next().length(); i++)
				// checks all letters A-Z
				for (char j='A'; j<='Z'; j++)
					// if the letter being 
					if (j ==current.charAt(i))
						continue;
					else {
						// possible new word
						String next = current.substring(0,i) + String.valueOf(j) + current.substring(i+1,dict.iterator().next().length());
						// if not already discovered
						if (!discovered.contains(next)){
							// if the newly generated word is the end word, ladder has been found
							if (next.equals(end)) {
								parentWordMap.put(end,current);
								break StartSearch; //breaks out of the outer loop to leave search
							// if the newly generated word exists, add to queue
							} else if (dict.contains(next)){
								queue.add(next);
								discovered.add(next);
								parentWordMap.put(next,current);
							}
						}
					}
		}
		
		// return null if ladder does not exist
		if (!parentWordMap.containsKey(end))
			return null;

		// stack to get the ladder (in a reversed order)
		Stack<String> reversedLadder = new Stack<String>();
		reversedLadder.add(end);
		String current = end;
		while ((current = parentWordMap.get(current)) != null){
			reversedLadder.add(current);
		}
		
		// create a new array list to reverse ladder into
		ArrayList<String> finalLadder = new ArrayList<String>(reversedLadder.size());
		while (!reversedLadder.isEmpty()){
			finalLadder.add(reversedLadder.pop().toLowerCase());
		}
		
		return finalLadder;
	}
    
	/**
	 * Takes all the words from a dictionary text file and converts into a Set<String>
	 * @return Set of all the words in the given dictionary
	 */
	public static Set<String>  makeDictionary () {
		Set<String> words = new HashSet<String>();
		Scanner infile = null;
		try {
			infile = new Scanner (new File("five_letter_words.txt"));
		} catch (FileNotFoundException e) {
			System.out.println("Dictionary File not Found!");
			e.printStackTrace();
			System.exit(1);
		}
		while (infile.hasNext()) {
			words.add(infile.next().toUpperCase());
		}
		return words;
	}
	
	/**
	 * Prints the word ladder in the appropriate format
	 * @param ladder is the ArrayList of words in the ladder, or null if it doens't exist
	 */
	public static void printLadder(ArrayList<String> ladder) {
		if (ladder == null){ // if ladder does not exist
			System.out.println("no word ladder can be found between " + startWord + " and " + endWord + ".");
		}
		else { // else if ladder is found, print the message
			System.out.println("a " + (ladder.size()-2) + "-rung word ladder exists between " + startWord + " and " + endWord);
			// print all the words in the ladder
			for(int i = 0; i < ladder.size(); i++){
				System.out.println(ladder.get(i));
			}
		}
	}
	// TODO
	// Other private static methods here
	
	/**
	 * returns true if two words differ by only one character
	 * @param String a first word of the pair to be checked 
	 * @param String b second word of the pair to be checked
	 */
	private static boolean differentByOne(String a, String b) {
		// do not consider if words have different length
        assert a.length() == b.length();
        // compute the number of different characters in two words
        int diff = 0;
        for (int i = 0; i < a.length(); i++) {
            if (a.charAt(i) != b.charAt(i)) {
            	diff++;            	
            }
            if (diff > 1){
            	return false;   
            }
        }
        return true;
    }

	/**
	 * for a given node, finds all of its neighbors
	 * which differ by one character from input word
	 * @param String start is the start word
	 * @param Set<String> dict is the dictionary of words
	 */
	private static ArrayList<String> findNeighbors(String start, Set<String> dict) {
		ArrayList<String> Neighbors = new ArrayList<String>();
		// for all the words in the dictionary, add neighboring nodes
		for (String word : dict) {
			if (differentByOne(start, word))  { 
				Neighbors.add(word);}
		}
		// Neighbor is a list of all the words that differ by one character
		return Neighbors;
	}
}

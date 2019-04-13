package Assig4;
import java.util.ArrayList;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;
import java.util.regex.Pattern;

/**
 * The program will spell check each word and then write a new file with the name of the suffix ‘_chk’.
 * @author weichen
 */
public class SpellChecker {
	String fileName;

	//read user file into array list 
	public ArrayList<String> filetoAl() {
		ArrayList<String> myfile = new ArrayList<String>();
		boolean wrong = false;
		do{
			FileChecker checkFile = new FileChecker();
			FileChecker newfile = checkFile.getuserFile();
			File afile = newfile.myfile;
			fileName = newfile.fileName + "_chk.txt";
			try {
				Scanner userFile1 = new Scanner(afile);
				while(userFile1.hasNext()) {
					myfile.add(userFile1.next());
				}
				wrong = true;
			} catch (FileNotFoundException e) {
				System.out.println("No such file or directory, please enter again!");
			}}while(!wrong);
		return myfile;
	}


	//check whether this word should be ignored: all upper case, includes number and special char
	public boolean ignoreWord(String word) {
		String regexUpper = "[A-Z]+";
		String regexNoword = ".*([\\_]|[\\W]|[\\d]).*";
		Pattern pattern1 = Pattern.compile(regexUpper);
		Pattern pattern2 = Pattern.compile(regexNoword);	
		boolean ignoreWord = pattern1 .matcher(word).matches() || pattern2 .matcher(word).matches();
		return ignoreWord;
	}

	// check whether this word is spelled correctly, we assume all upper case to be correct.
	public boolean spellCorrect(String word) {
		WordRecommender newrecom = new WordRecommender("engDictionary");
		ArrayList<String> listOfWords = newrecom.listOfWords;
		if(!ignoreWord(word) && !listOfWords.contains(word)) {
			System.out.println("The word '" + word + "' is misspelled.");
			System.out.println();
			return false;
		}
		return true;	
	}

	// take the solution to write new file
	public void writenewfile(String word , String fileName ) {	
		try {
			FileWriter  fw = new FileWriter(fileName, true);
			PrintWriter pw = new PrintWriter(fw);
			pw.print(word + " ");
			pw.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// If this word is not correct, we promote user to choose words from WordRecommender.
	public void check() {
		ArrayList<String> myfile = new ArrayList<String>();
		myfile = filetoAl();
		String word;

		for(int i = 0; i < myfile.size(); i ++) {
			word = myfile.get(i);
			ArrayList<String> topNlist = new ArrayList<String>();
			if(spellCorrect(word) != true) {
				WordRecommender newrecom = new WordRecommender("engDictionary");
				topNlist = newrecom.getWordSuggestions(word, 3, 0.7, 3);
				word = newrecom.theuserInput(word, 3, topNlist, "r", "a", "t" );	
			}	
			writenewfile(word, fileName);
		}
		System.out.println();
		System.out.println("Spell Checker is done! Please check the new file!");
	}

	public String getFileName() {
		return fileName;
	}

}

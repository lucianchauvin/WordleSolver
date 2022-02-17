import java.util.*;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.text.DecimalFormat;
import java.math.RoundingMode;
import java.io.*;
import java.util.*;
import java.util.List;

import javax.sound.midi.Soundbank;

import java.net.*;
import java.io.*;

class Main {
  public static void main(String[] args) throws IOException {
    ArrayList<String> wordbank = new ArrayList<String>();
    try (BufferedReader br = new BufferedReader(new FileReader("wordbank.txt"))) {
      String sCurrentLine;
      while ((sCurrentLine = br.readLine()) != null) {
        wordbank.add(sCurrentLine);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    Scanner myObj = new Scanner(System.in);
    System.out.println(getOnlineScore("hello"));
    while(true){
      System.out.println("--------------------------------------------------");

      System.out.println("Enter known letters at their positions in this format 'f----', where the dashes are unknown (green): ");
      String n = myObj.nextLine();
      System.out.println("Enter known letters but not positions at wrong position in format 'f----' (yellow): ");
      String k = myObj.nextLine();
      System.out.println("Enter letters not in word: ");
      String h = myObj.nextLine();
      wordbank = returnPossibles(wordbank, n, k, h);

    }
    
  }
  public static ArrayList<String> wordsContainingLetter(ArrayList<String> wordbank, char l){
      ArrayList<String> newwordbank = new ArrayList<String>();
      for(int x = 0; x < wordbank.size(); x++){
        boolean inword = false;
        for(int y = 0; y < wordbank.get(x).length(); y++){
          if(wordbank.get(x).charAt(y) == l){
            inword = true;
          }
       }
       if(!inword){
         newwordbank.add(wordbank.get(x));
       }
      }
    return newwordbank;
  }  
  public static ArrayList<String> wordsContainingLetters(ArrayList<String> wordbank, ArrayList<String> idk){
      ArrayList<String> newwordbank = new ArrayList<String>();
      for(int x = 0; x < wordbank.size(); x++){
        boolean inword = true;
        for(String s: idk){
          if(!wordbank.get(x).contains(s)){
            inword = false;
          }
        }
       if(inword){
         newwordbank.add(wordbank.get(x));
       }
      }
    return newwordbank;
  } 
  public static int getOnlineScore(String word) throws IOException{
    URL yahoo = new URL("https://api.phrasefinder.io/search?corpus=eng-us&query=" + word + "&topk=1&format=tsv");
    URLConnection yc = yahoo.openConnection();
    BufferedReader in = new BufferedReader(
                            new InputStreamReader(
                            yc.getInputStream()));
    String inputLine = in.readLine();
    if(inputLine != null){
      String[] currencies = inputLine.split("\t");
      return Integer.parseInt(currencies[1]);
    } else {
      return 0;
    }
  }
  public static ArrayList<String> wordsNOTContainingLetter(ArrayList<String> wordbank, char l){
      ArrayList<String> newwordbank = new ArrayList<String>();
      for(int x = 0; x < wordbank.size(); x++){
        boolean inword = false;
        for(int y = 0; y < wordbank.get(x).length(); y++){
          if(wordbank.get(x).charAt(y) == l){
            inword = true;
          }
       }
       if(inword){
         newwordbank.add(wordbank.get(x));
       }
      }
    return newwordbank;
  } 
  public static ArrayList<String> wordsContainingLetterAtIndex(ArrayList<String> wordbank, char l, int index){
      ArrayList<String> newwordbank = new ArrayList<String>();
      for(int x = 0; x < wordbank.size(); x++){
        boolean inword = false;
        for(int y = 0; y < wordbank.get(x).length(); y++){
          if(wordbank.get(x).charAt(y) == l && y == index){
            inword = true;
          }
       }
       if(inword){
         newwordbank.add(wordbank.get(x));
       }
      }
    return newwordbank;
  }
  public static ArrayList<String> wordsNOTContainingLetterAtIndex(ArrayList<String> wordbank, char l, int index){
      ArrayList<String> newwordbank = new ArrayList<String>();
      for(int x = 0; x < wordbank.size(); x++){
        boolean inword = false;
        for(int y = 0; y < wordbank.get(x).length(); y++){
          if(wordbank.get(x).charAt(y) == l && y == index){
            inword = true;
          }
       }
       if(!inword){
         newwordbank.add(wordbank.get(x));
       }
      }
    return newwordbank;
  }  
  public static double frequencyOfLetterInAllWords(ArrayList<String> wordbank, char l){
      double chars = 0;
      double wantedchars = 0;
      for(int x = 0; x < wordbank.size(); x++){
        boolean inword = false;
        for(int y = 0; y < wordbank.get(x).length(); y++){
          chars += 1;
          if(wordbank.get(x).charAt(y) == l){
            wantedchars += 1;
          }
       }
      }
    return (wantedchars/chars)*100;
  }
  public static double frequencyOfLetterInAllWordsAtPosition(ArrayList<String> wordbank, char l, int index){
      double chars = 0;
      double wantedchars = 0;
      for(int x = 0; x < wordbank.size(); x++){
        for(int y = 0; y < wordbank.get(x).length(); y++){
          chars += 1;
          }
       }
    return (((double) wordsContainingLetterAtIndex(wordbank, l, index).size())/((double)wordbank.size()))*100;
  }
  public static void rankWordBank(ArrayList<String> wordbank){
    DecimalFormat df = new DecimalFormat("##.##");
    df.setRoundingMode(RoundingMode.DOWN);
    char[] alph = "abcdefghijklmnopqrstuvwxyz".toCharArray();
    for(char x : alph){
      System.out.println("Frequency of '" + x + "' at all positions: " + df.format(frequencyOfLetterInAllWords(wordbank, x)) +"%");
      for(int y = 0; y <5; y++){
        System.out.println("Frequency of '" + x + "' at index " + y + ": "  + df.format(frequencyOfLetterInAllWordsAtPosition(wordbank, x, y)) +"%");
      }
    }
  }

  public static double score(ArrayList<String> wordbank, String s) throws IOException{
    wordbank = removeDupes(wordbank);
    double score = 0.0;
    for(char c: s.toCharArray()){
      int count = 0;
      for(int i=0; i < s.length(); i++){    
        if(s.charAt(i) == c)
            count++;
      }
      if(count >= 2){
        score -= frequencyOfLetterInAllWords(wordbank, c)*1.2;
      }
      score += frequencyOfLetterInAllWords(wordbank, c);
    }
    if(wordbank.size() < 150){
      return score*getOnlineScore(s);
    }
    return score;
  }
  public static ArrayList<Pair> reverseArrayList(ArrayList<Pair> alist)
    {
        // Arraylist for storing reversed elements
        // this.revArrayList = alist;
        for (int i = 0; i < alist.size() / 2; i++) {
            Pair temp = alist.get(i);
            alist.set(i, alist.get(alist.size() - i - 1));
            alist.set(alist.size() - i - 1, temp);
        }
 
        // Return the reversed arraylist
        return alist;
    }
  public static ArrayList<String> removeDupes(ArrayList<String> alist){
    ArrayList<String> newlist = new ArrayList<String>();
    newlist.add(alist.get(0));
    for(String s: alist){
      for(String a: newlist){
        if(!s.equals(a)){
          newlist.add(s);
          break;
        }
      }
    }
    return newlist;
  }
  public static ArrayList<Pair> removeDupes2(ArrayList<Pair> alist){
    ArrayList<Pair> newlist = new ArrayList<Pair>();
    newlist.add(alist.get(0));
    for(Pair s: alist){
      for(Pair a: newlist){
        if(!s.getWord().equals(a.getWord())){
          newlist.add(s);
          break;
        }
      }
    }
    return newlist;
  }
  public static ArrayList<String> returnPossibles(ArrayList<String> wordbank, String knownLetterAndPossition, String knownLetter, String wrongletter) throws IOException{
    ArrayList<String> myREALwords = new ArrayList<String>();
    ArrayList<String> mywords = new ArrayList<String>();
    ArrayList<String> myNOTwords = new ArrayList<String>();
    ArrayList<String> removedWrong = new ArrayList<String>();
    ArrayList<String> myREALACTUALwords = new ArrayList<String>();
    ArrayList<Pair> scores = new ArrayList<Pair>();

    ArrayList<Integer> pos = new ArrayList<Integer>();
    ArrayList<Character> posletters = new ArrayList<Character>();
    ArrayList<Integer> posnot = new ArrayList<Integer>();
    ArrayList<Character> posnotletters = new ArrayList<Character>();
    ArrayList<String> lettersunknown = new ArrayList<String>();
    ArrayList<String> wrongletters = new ArrayList<String>();

    for(char c: knownLetter.toCharArray()){
      if(c != '-'){
        lettersunknown.add(Character.toString(c));
      }
    }
    for(char c: wrongletter.toCharArray()){
      wrongletters.add(Character.toString(c));
    }
    for(int i = 0; i < knownLetterAndPossition.length(); i++){
      if(knownLetterAndPossition.charAt(i) != '-'){
        pos.add(i);
        posletters.add(knownLetterAndPossition.charAt(i));
      }
    }
    for(int i = 0; i < knownLetter.length(); i++){
      if(knownLetter.charAt(i) != '-'){
        posnot.add(i);
        posnotletters.add(knownLetter.charAt(i));
      }
    }
    if(pos.size() > 0){
      for(int x = 0; x < pos.size(); x++){
        for(String s: wordsContainingLetterAtIndex(wordbank, posletters.get(x), pos.get(x))){
          mywords.add(s);
        }
      }
      for(int x = 0; x < pos.size(); x++){
        for(String s: wordsNOTContainingLetterAtIndex(wordbank, posletters.get(x), pos.get(x))){
          myNOTwords.add(s);
        }
      }
    for(String s: mywords){
      boolean ugh = false;
      for(String p: myNOTwords){
        if(s.equals(p)){
          ugh = true;
        }
      }
      if(!ugh){
        myREALwords.add(s);
      }
    }
    }else{
      for(String s: wordsContainingLetters(wordbank, lettersunknown)){
        myREALwords.add(s);
      }
    }

    if(posnot.size() > 0){
      ArrayList<String> temp = new ArrayList<String>();

      for(String s: myREALwords){
        boolean ugh = false;
        for(int x = 0; x < posnot.size(); x++){
          if(s.charAt(posnot.get(x)) == posnotletters.get(x)){
            ugh = true;
          }
        }
        if(!ugh){
          temp.add(s);
        }
      }
      myREALwords = temp;
    }

    if(lettersunknown.size() != 0){
      for(String a: lettersunknown){
        for(String s: myREALwords){
          if(s.contains(a)){
            myREALACTUALwords.add(s);
          }
        }
      }
    }else{
      myREALACTUALwords = myREALwords;
    }
    for(String s: myREALACTUALwords){
      boolean ugh = true;
      for(String c: wrongletters){
        for(char l: s.toCharArray()){
          if(l == c.charAt(0)){
            ugh = false;
          }
        }
      }
      if(ugh){
        removedWrong.add(s);
      }
    }
    removedWrong = removeDupes(removedWrong);
    for(String a: removedWrong){
      scores.add(new Pair(a, score(wordbank, a)));
    }
    Collections.sort(scores);
    System.out.println(scores);
    scores = reverseArrayList(scores);
    double best = 0;
    String bestword = "";
    for(Pair p: scores){
      if(p.getValue() > best){
        best = p.getValue();
        bestword = p.getWord();
      }
    }
    System.out.println("Highest scored guess: " + bestword + " with a score of: " + best);


    return removedWrong;
  }
}
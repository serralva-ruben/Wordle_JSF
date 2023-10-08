package lu.uni;

import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.event.ValueChangeEvent;
import jakarta.inject.Named;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.ArrayList;


@SessionScoped
@Named("wordleBean")
public class WordleBean implements Serializable {
  
  private static ArrayList<String> answerList = new ArrayList<>();
  private static ArrayList<String> wordList = new ArrayList<>();
  private static ArrayList<Attempt> attempts = new ArrayList<>();

  private int currentAttempt = 0;
  private String inputText = "";

  private String randomWord;

  private boolean showInvalidWordWarning = false;
  private boolean won = false;


  public WordleBean() {
    //read words from file
    try {
      //read answerList
      InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("/answerlist.txt");
      if(inputStream != null){
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        String line;
        while((line = reader.readLine()) != null){
          answerList.add(line);
        }
        reader.close();
      }
      inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("/wordlist.txt");
      if(inputStream != null){
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        String line;
        while((line = reader.readLine()) != null){
          wordList.add(line);
        }
        reader.close();
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    //choose random word
    this.randomWord = answerList.get((int)(answerList.size()*Math.random()));
    //initialize the attempts array with 6 new attempts
    for(int i=0; i<6;i++){attempts.add(new Attempt());}
  }

  public ArrayList<String> getWordList(){
    return answerList;
  }

  public ArrayList<Attempt> getAttempts(){
    return attempts;
  }

  public int getCurrentAttempt(){return currentAttempt;}

  public void setWord(){
    showInvalidWordWarning = false;
    if(currentAttempt<6 && inputText.length()==5 && wordList.contains(inputText)){
      attempts.get(currentAttempt).setWord(inputText);
      char[] randomWordChars = randomWord.toCharArray();
      char[] inputChars = inputText.toCharArray();
      //optimise better??
      //problem with repeated letters, one becomes yellow
      for(int i=0; i< inputChars.length; i++){
        for(int j=0;  j< randomWordChars.length; j++){
          if(inputChars[i]==randomWordChars[j] && i==j) attempts.get(currentAttempt).getWord().get(i).setColor("GREEN");
          else if(inputChars[i]==randomWordChars[j] && attempts.get(currentAttempt).getWord().get(i).getColor().equals("LIGHTGREY")) attempts.get(currentAttempt).getWord().get(i).setColor("YELLOW");
        }
      }
      currentAttempt++;
      if(inputText.equals(randomWord)) won = true;
    }
    else if(currentAttempt<6) showInvalidWordWarning = true;
  }

  public void setInputText(String s){inputText = s;}
  public String getInputText(){return inputText;}

  public void inputTextChangeListener(ValueChangeEvent event) {
    inputText = event.getNewValue().toString();
  }

  public void reset(){
    inputText="";
    this.randomWord = answerList.get((int)(answerList.size()*Math.random()));
    for(int i=0; i<6;i++){attempts.set(i, new Attempt());}
    currentAttempt = 0;
    showInvalidWordWarning = false;
    won = false;
  }

  public boolean getWarning() {return showInvalidWordWarning;}
  public boolean getWon() {return won;}
  public String getAnswer() {return randomWord;}

  public boolean shouldShowAnswerOutput() {
    return getCurrentAttempt() == 6 && !getWon();
  }

  public boolean isInputDisabled() {
    return getCurrentAttempt() >= 6 || getWon();
  }
}

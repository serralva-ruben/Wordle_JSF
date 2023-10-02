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
  
  private static ArrayList<String> wordList = new ArrayList<>();
  private static ArrayList<Attempt> attempts = new ArrayList<>();

  private int currentAttempt = 0;
  private String inputText = "";

  private String randomWord;

  public WordleBean() {
    //read words from file
    try {
      InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("/answers.txt");
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
    this.randomWord = wordList.get((int)(wordList.size()*Math.random()));
    //initialize the attempts array with 6 new attempts
    for(int i=0; i<6;i++){attempts.add(new Attempt());}
  }

  public ArrayList<String> getWordList(){
    return wordList;
  }

  public ArrayList<Attempt> getAttempts(){
    return attempts;
  }

  public int getCurrentAttempt(){return currentAttempt;}

  public void setWord(String w){
    if(currentAttempt<6){
      attempts.get(currentAttempt).setWord(w);
      currentAttempt++;
    }
  }

  public void setWord(){
    if(currentAttempt<6 && inputText.length()==5){
      attempts.get(currentAttempt).setWord(inputText);
      //change color
      char[] randomWordChars = randomWord.toCharArray();
      char[] inputChars = inputText.toCharArray();
      //optimise better??
      for(int i=0; i< inputChars.length; i++){
        for(int j=0;  j< randomWordChars.length; j++){
          if(inputChars[i]==randomWordChars[j] && i==j) attempts.get(currentAttempt).getWord().get(i).setColor("GREEN");
          else if(inputChars[i]==randomWordChars[j]) attempts.get(currentAttempt).getWord().get(i).setColor("YELLOW");
        }
      }
      currentAttempt++;
    }
  }

  public void setInputText(String s){inputText = s;}
  public String getInputText(){return inputText;}

  public void inputTextChangeListener(ValueChangeEvent event) {
    inputText = event.getNewValue().toString();
  }

  public void reset(){
    inputText="";
    this.randomWord = wordList.get((int)(wordList.size()*Math.random()));
    for(int i=0; i<6;i++){attempts.set(i, new Attempt());}
    currentAttempt = 0;
  }
}

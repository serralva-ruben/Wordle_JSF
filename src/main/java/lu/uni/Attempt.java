package lu.uni;

import java.util.ArrayList;

public class Attempt {
    
    private ArrayList<Letter> word = new ArrayList<>();

    //Initialize the attempt with 5 empty chars inside the word arraylist
    public Attempt(){
        for(int i=0; i<5; i++){
            word.add(new Letter());
        }
    }

    public ArrayList<Letter> getWord(){
        return this.word;
    }

    //sets the word of the attempt
    public void setWord(String w){
        char[] wordChars = w.toCharArray();
        for(int i=0; i<word.size();i++){
            word.get(i).setChar(wordChars[i]);
        }
    }
}

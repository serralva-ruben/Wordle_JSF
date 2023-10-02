package lu.uni;

public class Letter {
    private Character c;
    private String color;

    public Letter(){
        this.c = ' ';
        this.color = "LIGHTGREY";
    }

    public String getColor(){return color;}

    public void setColor(String c){this.color = c;}

    public Character getChar(){return this.c;}

    public void setChar(Character c){this.c = c;}

}

import java.util.LinkedList;

/**
 * Created by nate on 3/9/17.
 */
public class WordModel {
    private final String myWord;
    private int myFrequency;
    private int myWinMin;
    private int myWinMax;

    private LinkedList<WordModel> myNextWords;

    public WordModel(String theWord)
        throws NullPointerException
    {
        if(theWord == null) {
            throw new NullPointerException("Hey! I don't like null values");
        }
        myWord = theWord;
        myFrequency = 1;
        myWinMin = 0;
        myWinMax = 0;

        myNextWords = new LinkedList<WordModel>();
    }
    public String getWord() {
        return myWord;
    }


}

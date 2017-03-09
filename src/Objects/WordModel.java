package Objects;

import java.util.LinkedList;

/**
 * Created by nate on 3/9/17.
 */
public class WordModel {

    private final String myWord;

    private boolean myTrained;

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

        myTrained = false;

        myNextWords = new LinkedList<WordModel>();
    }
    public String getWord() {
        return myWord;
    }
    public int setWinVals(int min)
        throws IllegalArgumentException
    {
        if(min < 0) {
            throw new IllegalArgumentException("I don't like negatives");
        }
        myWinMin = min;
        myWinMax = myWinMin + myFrequency;

        return myWinMax;
    }

    public boolean isWinner(int lottoTicket) {
        return ((lottoTicket >= myWinMin) && (lottoTicket < myWinMax));
    }

    public void incrementFreq() {
        myFrequency++;
    }
    // Adds the word after the passed word.
    // This is private because this is just to help out addNextTwoWords
    // Returns a reference to the wordmodel that was either found or added.
    private WordModel addNextWord(String word)
        throws NullPointerException
    {
        if(word == null) {
            throw new NullPointerException("Null values ain't cool yo.");
        }
        // Check if we already have this word.
        WordModel wm = null;
        boolean found = false;
        for(WordModel w : myNextWords) {
            if(w.getWord().compareTo(word) == 0) {
                w.incrementFreq();
                found = true;
                wm = w;
            }
        }
        // If the list doesn't contain the word, add it.
        if(!found) {
            wm = new WordModel(word);
            myNextWords.add(wm);
        }
        return wm;
    }
    public void addNextTwoWords(String nextWord, String wordAfterNext)
        throws NullPointerException
    {
        if(nextWord == null || wordAfterNext == null) {
            throw new NullPointerException("Null values ain't coo holmes.");
        }
        // Get the reference to the next word in our list.
        WordModel next = addNextWord(nextWord);
        // Add wordAfterNext to that Objects.WordModel.
        next.addNextWord(wordAfterNext);
    }

    public String getNextWord()
    {
        // Exit if we haven't finished training.
        if(!myTrained) {
            System.out.println("WOAH THERE BUCKAROO, WE AIN'T DONE YET.");
            return null;
        }

        return "Null";
    }
    public void finishTraining() {
        myTrained = true;
        int lowWin = 0;
        // Recurse into all nextwords and finish their training.
        for(WordModel w : myNextWords) {
            lowWin = w.setWinVals(lowWin);
            w.finishTraining();
        }
    }
}

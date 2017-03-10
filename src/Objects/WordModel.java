package Objects;

import java.time.Instant;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Random;

/**
 * Class to handle storage of a word and all words that follow from it.
 * @author Nathanael Toporek
 */
public class WordModel {

    private final String            myWord;

    private boolean                 isTrained;
    private int                     myFrequency;
    private int                     myWinMin;
    private int                     myWinMax;

    private Random                  myRNG;
    private LinkedList<WordModel>   myNextWords;

    /**
     * Constructs a new WordModel object containing the word passed.
     * @param theWord the word that this WordModel will represent.
     * @throws NullPointerException if theWord == null
     */
    public WordModel(String theWord)
        throws NullPointerException
    {
        if(theWord == null) {
            throw new NullPointerException("Hey! I don't like null values");
        }
        myWord = theWord;

        isTrained = false;

        myFrequency = 1;
        myWinMin = 0;
        myWinMax = 0;

        myRNG = new Random(Instant.now().toEpochMilli());

        myNextWords = new LinkedList<>();
    }

    /**
     * Returns the word that this WordModel represeents.
     * @return myWord
     */
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

    /**
     * Returns the maximum winning value. This is used
     * when we select a random word in a lottery-esque fashion.
     * @return myWinMax
     */
    public int getWinMax() {
        return myWinMax;
    }

    /**
     * Returns whether or not this word is a winner given a ticket value
     * @param lottoTicket The ticket value for random word choice.
     * @return (lottoTicket >= myWinMin) && (lottoTicket < myWinMax)
     */
    public boolean isWinner(int lottoTicket) {
        return ((lottoTicket >= myWinMin) && (lottoTicket < myWinMax));
    }

    public void incrementFreq() {
        myFrequency++;
    }
    public void addNextTwoWords(String nextWord, String wordAfterNext)
        throws NullPointerException, IllegalStateException
    {
        // Exit if either argument is null or we've finished training.
        if(nextWord == null || wordAfterNext == null) {
            throw new NullPointerException("Null values ain't coo holmes.");
        } else if(this.isTrained) {
            throw new IllegalStateException("We're already trained! * Eats glue *");
        }
        // Get the reference to the next word in our list.
        WordModel next = addNextWord(nextWord);
        // Add wordAfterNext to that Objects.WordModel.
        next.addNextWord(wordAfterNext);
    }

    /**
     * Returns a random next word in a lottery-esque fashion.
     * @return Random word in myNextWords
     * @throws IllegalStateException If !this.isTrained
     */
    public String getNextWord()
        throws IllegalStateException
    {
        // Exit if we haven't finished training.
        if(!this.isTrained) {
            throw new IllegalStateException("WOAH THERE BUCKAROO, WE AIN'T DONE YET.");
        }
        int winner = Math.abs(myRNG.nextInt() % myNextWords.getLast().getWinMax());
        Iterator<WordModel> itr = myNextWords.iterator();
        String winningWord = null;
        boolean found = false;
        while(itr.hasNext() && !found) {
            WordModel wm = itr.next();
            if(wm.isWinner(winner)) {
                winningWord = wm.getWord();
                found = true;
            }
        }
        return winningWord;
    }

    /**
     * Given the next word after this one, select the word after that.
     * @param nextWord The word after this one.
     * @return A random word after nextWord.
     * @throws NullPointerException if nextWord == null
     * @throws IllegalArgumentException if nextWord is not found
     * @throws IllegalStateException if !this.isTrained
     */
    public String getWordAfterNext(String nextWord)
        throws NullPointerException, IllegalArgumentException, IllegalStateException
    {
        // Fail if the user passes in a null value.
        if(nextWord == null) {
            throw new NullPointerException("I don't like nulls.");
        } else if(!this.isTrained) {
            throw new IllegalStateException("I'm not done training bruh.");
        }
        // Try to find the next word.
        WordModel next = null;
        boolean found = false;
        Iterator<WordModel> itr = myNextWords.iterator();
        while(itr.hasNext() && !found) {
            WordModel wm = itr.next();

            System.out.printf("TARGET: %s CURRENT: %s\n", nextWord, wm.getWord());
            if(wm.getWord().equals(nextWord)) {
                next = wm;
                found = true;
            }
        }
        // Throw an exception if we didn't find the word.
        if(!found) {
            throw new IllegalArgumentException("Word not found.");
        }
        String wordAfterNext = next.getNextWord();
        return wordAfterNext;
    }

    /**
     * Finishes the traning for this WordModel.
     * Will recurse into each of myNextWords and finish their training as well.
     * Sets this.isTrained = true;
     * Assigns winning values to each word.
     */
    public void finishTraining() {
        this.isTrained = true;
        int lowWin = 0;
        // Recurse into all nextwords and finish their training.
        // Also set win values for each value.
        for(WordModel w : myNextWords) {
            lowWin = w.setWinVals(lowWin);
            w.finishTraining();
        }
    }

    // Adds the word after the passed word.
    // This is private because this is just to help out addNextTwoWords
    // Returns a reference to the wordmodel that was either found or added.
    private WordModel addNextWord(String word) {
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
}

package Objects;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;

/**
 * Created by nate on 3/9/17.
 */
public class Trainer {

    private boolean isTrained;

    private Hashtable<String, WordModel> myTable;

    public Trainer() {
        isTrained = false;
        myTable = new Hashtable<>();
    }

    private WordModel addWord(String word)
        throws NullPointerException, IllegalStateException
    {
        if(word == null) {
            throw new NullPointerException("Null word found. NOPE!");
        } else if(this.isTrained) {
            throw new IllegalStateException("BUT I ALREADY GOT TRAINED. * eats glue *");
        }

        WordModel wm = myTable.get(word);
        // Add the word if it our trainer doesn't know it.
        if(wm == null) {
            wm = new WordModel(word);
            myTable.put(word, wm);
        } else { // Increment frequency if it does.
            wm.incrementFreq();
        }
        return wm;
    }

    private void addNextTwoWords(String currentWord, String nextWord, String wordAfterNext)
        throws NullPointerException, IllegalStateException
    {
        if(currentWord == null || nextWord == null || wordAfterNext == null) {
            throw new NullPointerException("Null values. I. DO. NOT. LIKE. THEM.");
        } else if(this.isTrained) {
            throw new IllegalStateException("BUT I ALREADY GOT TRAINED. * eats glue *");
        }
        WordModel curr = this.addWord(currentWord);
        curr.addNextTwoWords(nextWord, wordAfterNext);
    }

    private void train(Scanner fin) {
        String currentWord = null, nextWord = null, wordAfterNext = null;
        if(fin.hasNext()) {
            currentWord = fin.next();
        }
        if(fin.hasNext()) {
            nextWord = fin.next();
        }
        if(fin.hasNext()) {
            wordAfterNext = fin.next();
            try {
                this.addNextTwoWords(currentWord, nextWord, wordAfterNext);
            } catch(Exception e) {
                System.out.println(e.getMessage());
                e.printStackTrace();
            }
        }
        while(fin.hasNext()) {
            currentWord = nextWord;
            nextWord = wordAfterNext;
            wordAfterNext = fin.next();

            try {
                this.addNextTwoWords(currentWord, nextWord, wordAfterNext);
            } catch(Exception e) {
                System.out.println(e.getMessage());
                e.printStackTrace();
            }
        }
    }

    public void trainOnTexts(ArrayList<File> texts)
        throws NullPointerException, IllegalStateException
    {
        if(texts == null) {
            throw new NullPointerException("I don't like nulls bruh.");
        } else if(this.isTrained) {
            throw new IllegalStateException("But I'm already trained! * eats glue *");
        }
        for(File text : texts) {
            Scanner fin = null;
            try {
                fin = new Scanner(new BufferedReader(new FileReader(text)));
            } catch(FileNotFoundException fnfe) {
                fnfe.printStackTrace();
            }
            if(fin != null) {
                this.train(fin);
                fin.close();
            }
        }
    }
    public void finishTraining() {
        this.isTrained = true;
        int lowWin = 0;
        // We need to ensure that our keyset is always sorted so that
        // when we finish training we can always access the end of the hashmap
        ArrayList<String> keys = new ArrayList<>(myTable.keySet());
        Collections.sort(keys);

        for(String key : keys) {
            WordModel wm = myTable.get(key);
            // Set winvals for each WordModel in our table.
            lowWin = wm.setWinVals(lowWin);
            // Finish training for each wordmodel.
            wm.finishTraining();
        }
    }
}

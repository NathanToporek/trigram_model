package Objects;

import java.io.*;
import java.time.Instant;
import java.util.*;

/**
 * Class to train a lot of WordModels using a supplied list of text files.
 * @author Nathanael Toporek
 */
public class Trainer {

    private static final int WORDS_PER_LINE = 15;

    private boolean isTrained;

    private Hashtable<String, WordModel> myTable;

    /**
     * Constructor for this Trainer.
     * Initializes isTrained to false
     * Makes myTable a new HashTable
     */
    public Trainer() {
        isTrained = false;
        myTable = new Hashtable<>();
    }

    /**
     * Given a list of text files, train an each of them.
     * @param texts User-supplied list of text files.
     * @throws NullPointerException If texts == null
     * @throws IllegalStateException If this.isTrained
     */
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
                System.out.printf("TRAINING ON TEXT FILE: %s\n", text.getAbsoluteFile());
                this.train(fin);
                fin.close();
            }
        }
    }

    /**
     * Finishes the training for this trainer.
     * Sets this.isTrained to true.
     * initializes all of the winning values for all of its WordModels,
     * and tells all WordModels to finish their training.
     */
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

    /**
     * Generates a story of length provided by the user.
     * @param length The length of the story
     * @param fout The output stream that we'll print the story to.
     * @throws IllegalStateException If !this.isTrained
     * @throws IllegalArgumentException If length < 3
     * @throws NullPointerException If fout == null
     */
    public void generateStory(int length, PrintStream fout)
        throws IllegalStateException, IllegalArgumentException, NullPointerException
    {
        if(!this.isTrained) {
            throw new IllegalStateException("BUT I ALREADY GOT TRAINED. * eats glue *");
        } else if(length < 3) {
            throw new IllegalArgumentException("Please let me express myself, good sir/madam.");
        } else if(fout == null) {
            throw new NullPointerException("I CAN'T WRITE TO THE VOID.");
        }
        WordModel currentWord = this.getFirstWord();
        String nextWord = myTable.get(currentWord.getWord()).getNextWord();
        for(int i = 0; i < length; i++) {
            if((i % WORDS_PER_LINE) == 0) {
                fout.print("\r\n");
            }
            // Output current word.
            fout.printf("%s ", currentWord.getWord());
            // Get the third word out.
            String wordAfterNext = currentWord.getWordAfterNext(nextWord);
            // Move along toodlooo.
            currentWord = myTable.get(nextWord);
            nextWord = wordAfterNext;
        }
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
    private WordModel getFirstWord() {
        ArrayList<String> keys = new ArrayList<>(myTable.keySet());
        Collections.sort(keys);

        int modulo = myTable.get(keys.get(keys.size() - 1)).getWinMax();
        // Seed a new random number generator.
        Random rng = new Random(Instant.now().toEpochMilli());
        int rand = Math.abs(rng.nextInt() % modulo);

        Iterator<String> itr = keys.iterator();
        boolean found = false;
        WordModel wm = null;
        while(itr.hasNext() && !found) {
            wm = myTable.get(itr.next());
            found = wm.isWinner(rand);
        }
        return wm;
    }
}

package Driver;

import Objects.Trainer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Class to train an AI and generate a story
 * @author Nathanael Toporek
 */
public class Driver {
    /**
     * Main method that the user interacts with
     * @param args Commandline args passed into the program.
     */
    public static void main(String ... args) {
        String pathToTrainingMaterials = null;
        String outputPath = null;
        Scanner stdin = new Scanner(System.in);
        Path trainingDir = null;
        ArrayList<File> texts = new ArrayList<>();
        Trainer AI = new Trainer();

        do {
            System.out.print("Enter the complete pathname to the directory of your training materials: ");
            try {
                pathToTrainingMaterials = stdin.next();
            } catch (Exception e) {
                System.out.println(e);
            }
        } while(pathToTrainingMaterials == null);

        if(pathToTrainingMaterials.charAt(pathToTrainingMaterials.length() - 1) != '/') {
            pathToTrainingMaterials += '/';
        }

        trainingDir = Paths.get(pathToTrainingMaterials);
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(trainingDir)) {
            for (Path file: stream) {
                texts.add(new File(pathToTrainingMaterials + file.getFileName()));
            }
            AI.trainOnTexts(texts);
            AI.finishTraining();
        } catch(Exception e) {
            System.out.println(e);
        }

        do {
            System.out.print("\nPlease enter the full pathname to your output file: ");
            outputPath = stdin.next();
        } while(outputPath == null);

        PrintStream fout = null;
        try {
            fout = new PrintStream(new FileOutputStream(outputPath));
            AI.generateStory(1000, fout);
        } catch(Exception e) {
            e.printStackTrace();
            System.out.println(e);
            System.out.println(e.getMessage());
        } finally {
            if(fout != null) {
                fout.close();
            }
        }

        stdin.close();
    }
}

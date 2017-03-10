package Driver;

import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created by nate on 3/9/17.
 */
public class Driver {

    public static void main(String ... args) {

        Path trainingDir = Paths.get("/home/nate/eclipse_WS/nat96_trigram/training_texts");

        try (DirectoryStream<Path> stream = Files.newDirectoryStream(trainingDir)) {
            for (Path file: stream) {
                System.out.println(file.getFileName());
            }
        } catch(Exception e) {
            System.out.println(e);
        }
    }
}

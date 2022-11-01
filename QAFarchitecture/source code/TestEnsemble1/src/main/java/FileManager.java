

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

public class FileManager {

    public static void saveFile(String filename, ArrayList<String> content) {

        String filenameResult = filename;

        try {
            File myObj = new File(filenameResult);
            if (!myObj.exists())
                myObj.createNewFile();

            FileWriter myWriter = new FileWriter(filenameResult);
            for (String line : content
            ) {
                myWriter.write(line+"\r");
            }
            myWriter.close();
            System.out.println("Successfully wrote to the file.");


        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    public static ArrayList<String> readAllLines(String filename) {
        ArrayList<String> list = new ArrayList<>();
        Path filePath = Paths.get(filename);
        Charset charset = StandardCharsets.UTF_8;

        try (BufferedReader bufferedReader = Files.newBufferedReader(filePath, charset)) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                list.add(line);
            }
        } catch (IOException ex) {
            System.out.format("I/O error: %s%n", ex);
        }
        return list;
    }
}


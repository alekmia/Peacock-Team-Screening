import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.zip.GZIPInputStream;

public class GZIPReader {
    public static void readGZ(String filename) {
        try (GZIPInputStream gz = new GZIPInputStream(new FileInputStream(String.format("%s.gz", filename)));
             Reader decoder = new InputStreamReader(gz, StandardCharsets.UTF_8);
             BufferedReader reader = new BufferedReader(decoder);
             BufferedWriter writer = new BufferedWriter(new FileWriter(filename))
        ) {
            String a;
            while ((a = reader.readLine()) != null) {
                writer.write(a + System.lineSeparator());
                System.out.println(a);
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        readGZ("lng-4.txt");
    }
}

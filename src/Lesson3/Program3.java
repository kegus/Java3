package Lesson3;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Program3 {
    public static void main(String[] args) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("demo.txt"))) {
            for (int i = 0; i < 3; i++) {
                writer.write("Java\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        try (BufferedReader reader = new BufferedReader(new FileReader("demo.txt"))) {
            String str;
            int i = 0;
            while ((str = reader.readLine()) != null) {
                System.out.println(++i + ". " + str);
            }
            reader. mark(3);
            reader.reset();
            i = 10;
            while ((str = reader.readLine()) != null) {
                System.out.println(++i + ". " + str);
            }
            Files.lines(Paths.get("demo.txt"), StandardCharsets.UTF_8).forEach(System.out::println);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //new SimpleDateFormat("yyyy_MM_dd_hh_mm_ss").format(new Date());
        System.out.println(new SimpleDateFormat("yyyy_MM_dd_hh_mm_ss").format(new Date()));
        System.out.println("Ok");
    }
}

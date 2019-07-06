package FileLoadingAndStoring;

import Main.JobApplicationSystem;

import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class PreviousLoginDateLoaderAndStorer {

    /**
     * Loads the previous login date saved in "PreviousLoginDate.txt' in memory.
     * @throws IOException
     */
    public void loadPreviousLoginDate() throws IOException {
        try (BufferedReader fileReader = new BufferedReader(new FileReader("PreviousLoginDate.txt"))) {
            String dateString = fileReader.readLine().trim();
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate date = LocalDate.parse(dateString, dtf);
            JobApplicationSystem.setPreviousLoginDate(date);
        } catch (FileNotFoundException fnfe) {
            new FileWriter("PreviousLoginDate.txt");
        }
    }

    /**
     * Stores the previous login date in 'PreviousLoginDate.txt'.
     * @throws IOException
     */
    public void storePreviousLoginDate() throws IOException {
        try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("PreviousLoginDate.txt")))) {
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String dateString = JobApplicationSystem.getToday().format(dtf);
            out.println(dateString);
        }
    }
}

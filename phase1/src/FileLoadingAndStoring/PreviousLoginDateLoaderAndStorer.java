package FileLoadingAndStoring;

import Main.JobApplicationSystem;

import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

class PreviousLoginDateLoaderAndStorer {

    /**
     * Loads the previous login date saved in "PreviousLoginDate.txt' in memory.
     */
    void loadPreviousLoginDate(JobApplicationSystem jobApplicationSystem) {
        try (BufferedReader fileReader = new BufferedReader(new FileReader("phase1/files/PreviousLoginDate.txt"))) {
            String dateString = fileReader.readLine().trim();
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate date = LocalDate.parse(dateString, dtf);
            jobApplicationSystem.setPreviousLoginDate(date);
        } catch (FileNotFoundException fnfe) {
            try {
                new FileWriter("phase1/files/PreviousLoginDate.txt");
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    /**
     * Stores the previous login date in 'PreviousLoginDate.txt'.
     *
     */
    void storePreviousLoginDate(JobApplicationSystem jobApplicationSystem) {
        try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("PreviousLoginDate.txt")))) {
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String dateString = jobApplicationSystem.getToday().format(dtf);
            out.println(dateString);
        }
        catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
}

package com.teamb13.teamcreater.utils;

import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;

import com.aspose.cells.Workbook;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import com.teamb13.teamcreater.module.CsvData;

public class CsvUtils {

    private static final String OUT_NAME = "temp.csv";

    private static final String[] HEADER = {
            "ID",
            "Start time",
            "Completion time",
            "Email",
            "Name",
            "Your name  (FirstName LastName)",
            "Your student ID", "Your workshop class",
            "If you prefer to be in a team with particular students list " +
                    "their student IDs separated by commas (max. 6 student IDs):",
            "What project option do you prefer?",
            "What technologies/languages would you prefer to use in project?",
            "Timezone in which the team should preferably work",
            "Preferred week days for team work?",
            "Preferred day/night times for team-work"
    };

    private static void checkCol(ArrayList<String> col) throws Exception {
        for (int i = 0; i < HEADER.length; ++i) {
            int diff = Math.abs(col.get(i).compareToIgnoreCase(HEADER[i]));
            if (col.get(i).isEmpty() || col.get(i).isBlank() || diff > 5) {
                throw new Exception("Incorrect column format.");
            }
        }
    }

    public static CsvData readCsv(String filename) throws Exception {
        assert filename != null;

        try (var reader = new CSVReader(new FileReader(filename))) {
            ArrayList<ArrayList<String>> rows = new ArrayList<>();
            String[] s = reader.readNext();

            // If not match the number of cols.
            if (s.length != 14) {
                throw new IOException("File format incorrect. Check your columns.");
            }

            // Add header.
            ArrayList<String> header = new ArrayList<>(Arrays.stream(s).map(String::trim).toList());
            checkCol(header);

            int numOfRow = 0;

            while ((s = reader.readNext()) != null) {
                if (s[0].startsWith("Evaluation Only")) {
                    break;
                }

                ArrayList<String> row = new ArrayList<>(Arrays.stream(s).map(String::trim).toList());

                if (row.size() != header.size()) {
                    throw new IOException("File format incorrect. Check your data cells.");
                }

                rows.add(row);
                ++numOfRow;
            }

            // Catch this.
            if (numOfRow > 421) {
                throw new RuntimeException("Cannot more than 421 rows.");
            }

            return new CsvData(header, rows);
        } catch (CsvValidationException e) {
            throw new IOException(e);
        }
    }

    public static CsvData readXlsx(String filename) throws Exception {
        Workbook workbook = new Workbook(filename);
        workbook.save(OUT_NAME);
        var ret = readCsv(OUT_NAME);
        Files.delete(Path.of(OUT_NAME));
        return ret;
    }
}

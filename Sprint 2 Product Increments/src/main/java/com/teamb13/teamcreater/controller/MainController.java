package com.teamb13.teamcreater.controller;

import com.teamb13.teamcreater.module.CsvData;
import com.teamb13.teamcreater.module.Team;
import com.teamb13.teamcreater.utils.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.text.Font;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class MainController {

    Map<String, List<Team>> teams = Map.of();
    @FXML
    private Button createTeamBtn;

    @FXML
    private TextArea displayArea;

    @FXML
    private Button uploadBtn;

    private CsvData csvData;

    @FXML
    void onUploadBtnClicked(ActionEvent event) {
        File userChosenFile = null;
        try {
            userChosenFile = FileDialogCreator.getFile(
                    "Select csv or xlsx file",
                    uploadBtn.getScene().getWindow(),
                    PathUtils.getCurrentPathFileObject(),
                    FileDialogCreator.PreDefinedFormat.SUPPORTED_FILES
            );

            // If they select a file.
            if (userChosenFile != null) {
                if (userChosenFile.getName().endsWith(".xlsx")) {
                    csvData = CsvUtils.readXlsx(userChosenFile.getAbsolutePath());
                } else {
                    csvData = CsvUtils.readCsv(userChosenFile.getAbsolutePath());
                }

                MessageDialogCreator.information("Success", "Success",
                        String.format("Read data success from %s.", userChosenFile.getName()));
                // Enable button.
                createTeamBtn.setDisable(false);
                displayArea.setText("No Teams be created.");
            }
        } catch (Exception e) {
            MessageDialogCreator.error(
                    "Error",
                    null, String.format("Load data from \"%s\" failed.\n%s",
                            userChosenFile != null ? userChosenFile.getName() : "Unknown file",
                            e.getMessage()));
        }

        event.consume();
    }

    private void printTeams() {
        List<String> sortList = Arrays.stream(
                        teams.keySet().toArray(new String[0])
        ).sorted().toList();

        StringBuilder sb = new StringBuilder();
        sb.append("All teams: \n\n");
        for (var workShopName : sortList) {
            sb.append(String.format("Workshop: %s\n\n", workShopName));

            for (var team : teams.get(workShopName)) {
                sb.append(String.format("\tTeam name: %s\n", team.getName()));

                for (var student : team.getStudents()) {
                    sb.append(String.format("%-15s\t%-25s\t%10s\t%-40s\t%-50s\n",
                            student.studentID,
                            student.name,
                            student.workShops,
                            student.preferStudents.isEmpty() ? "NONE" : student.preferStudents,
                            student.preferProject));
                }

                sb.append("\n");
            }

            sb.append("\n");
        }

        displayArea.setText(sb.toString());
    }

    @FXML
    void onCreateTeamBtnClicked(ActionEvent event) {
        try {
            teams = TeamCreateUtils.createTeams(csvData);

            int size = 0;
            for (var pair : teams.entrySet()) {
                size += pair.getValue().size();
            }

            printTeams();

            MessageDialogCreator.information(
                    "Success",
                    "Create team successful.",
                    String.format("The total number of teams is %d.", size)
            );
        } catch (Exception e) {
            MessageDialogCreator.error("Error", "Create team failed.", e.getMessage());
        }

        event.consume();
    }

    @FXML
    void initialize() {
        try {
            var stream
                    = Objects.requireNonNull(this.getClass().getResource("font.ttf")).openStream();
            Font font = Font.loadFont(stream, 15);
            displayArea.setFont(font);
            stream.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
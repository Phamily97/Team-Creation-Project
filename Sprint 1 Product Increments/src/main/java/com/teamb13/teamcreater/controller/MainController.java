package com.teamb13.teamcreater.controller;

import com.teamb13.teamcreater.module.CsvData;
import com.teamb13.teamcreater.module.Team;
import com.teamb13.teamcreater.utils.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

import java.io.File;
import java.util.List;
import java.util.Map;

public class MainController {

    Map<String, List<Team>> teams = Map.of();
    @FXML
    private Button createTeamBtn;

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
        System.out.println("All teams: \n");
        for (var workShopAndTeam : teams.entrySet()) {
            System.out.printf("Workshop: %s\n", workShopAndTeam.getKey());

            for (var team : workShopAndTeam.getValue()) {
                System.out.printf("     Team name: %s\n", team.getName());

                for (var student : team.getStudents()) {
                    System.out.printf("             %-10s %s\n", student.studentID, student.name);
                }
            }

        }
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
}
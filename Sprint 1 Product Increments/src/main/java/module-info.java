open module com.teamb13.teamcreater {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;

    requires org.apache.poi.poi;
    requires org.apache.poi.ooxml;
    requires aspose.cells;
    requires org.jetbrains.annotations;

    requires com.opencsv;

    exports com.teamb13.teamcreater;
    exports com.teamb13.teamcreater.controller;
    exports com.teamb13.teamcreater.utils;
    exports com.teamb13.teamcreater.module;
}
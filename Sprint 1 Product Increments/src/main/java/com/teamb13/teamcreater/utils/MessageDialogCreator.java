package com.teamb13.teamcreater.utils;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Objects;
import java.util.Optional;

public final class MessageDialogCreator {

    private static final ButtonType[] EMPTY_BUTTONS = {};

    private MessageDialogCreator() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    @SuppressWarnings("unused")
    public static Optional<ButtonType> defaultDialog(@Nullable String title,
                                                     @Nullable String headerText,
                                                     @Nullable String content,
                                                     ButtonType @Nullable ... buttons) {
        return showDialog(AlertType.NONE, title, headerText, content, buttons);
    }

    public static Optional<ButtonType> defaultInformation(@Nullable String content) {
        return showDialog(AlertType.INFORMATION,
                "Information", null, content, EMPTY_BUTTONS);
    }

    public static Optional<ButtonType> information(@Nullable String title,
                                                   @Nullable String headerText,
                                                   @Nullable String content,
                                                   ButtonType @Nullable ... buttons) {
        return showDialog(AlertType.INFORMATION, title, headerText, content, buttons);
    }

    public static Optional<ButtonType> defaultWarning(@Nullable String content) {
        return showDialog(AlertType.WARNING, "Warning", null, content, EMPTY_BUTTONS);
    }

    public static Optional<ButtonType> warning(@Nullable String title,
                                               @Nullable String headerText,
                                               @Nullable String content,
                                               ButtonType @Nullable ... buttons) {
        return showDialog(AlertType.WARNING, title, headerText, content, buttons);
    }

    public static Optional<ButtonType> defaultConfirmation(@Nullable String content) {
        return showDialog(AlertType.CONFIRMATION, "Are you sure?", null, content, EMPTY_BUTTONS);
    }

    public static Optional<ButtonType> confirmation(@Nullable String title,
                                                    @Nullable String headerText,
                                                    @Nullable String content,
                                                    ButtonType @Nullable ... buttons) {
        return showDialog(AlertType.CONFIRMATION, title, headerText, content, buttons);
    }

    public static Optional<ButtonType> defaultError(@Nullable String content) {
        return showDialog(AlertType.ERROR, "Error", null, content, EMPTY_BUTTONS);
    }

    public static Optional<ButtonType> error(@Nullable String title,
                                             @Nullable String headerText,
                                             @Nullable String content,
                                             ButtonType @Nullable ... buttons) {
        return showDialog(AlertType.ERROR, title, headerText, content, buttons);
    }

    private static Optional<ButtonType> showDialog(@NotNull AlertType alertType,
                                                   @Nullable String title,
                                                   @Nullable String headerText,
                                                   @Nullable String content,
                                                   ButtonType @Nullable ... buttons) {
        final var alert = new Alert(alertType, content, buttons);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        return alert.showAndWait();
    }

    public static Optional<ButtonType> throwable(@NotNull Throwable throwable) {
        return throwable("Throwable", "Throwable object thrown.", throwable);
    }

    public static Optional<ButtonType> throwable(@Nullable String title,
                                                 @Nullable String headerText,
                                                 @NotNull Throwable throwable,
                                                 ButtonType @Nullable ... buttons) {
        Objects.requireNonNull(throwable);

        final var alert = new Alert(AlertType.ERROR, throwable.getMessage(), buttons);
        alert.setTitle(title);
        alert.setHeaderText(headerText);

        final var stringWriter = new StringWriter();
        final var printWriter = new PrintWriter(stringWriter);
        throwable.printStackTrace(printWriter);

        final String exceptionText = stringWriter.toString();

        final var label = new Label("The exception stacktrace was:");

        TextArea textArea = new TextArea(exceptionText);
        textArea.setEditable(false);
        textArea.setWrapText(false);

        textArea.setMaxWidth(Double.MAX_VALUE);
        textArea.setMaxHeight(Double.MAX_VALUE);
        GridPane.setVgrow(textArea, Priority.ALWAYS);
        GridPane.setHgrow(textArea, Priority.ALWAYS);

        final var expContent = new GridPane();
        expContent.setMaxWidth(Double.MAX_VALUE);
        expContent.add(label, 0, 0);
        expContent.setHgap(50);
        expContent.add(textArea, 0, 1);

        alert.getDialogPane().setExpandableContent(expContent);

        alert.setHeight(480);
        alert.setWidth(640);

        return alert.showAndWait();
    }
}

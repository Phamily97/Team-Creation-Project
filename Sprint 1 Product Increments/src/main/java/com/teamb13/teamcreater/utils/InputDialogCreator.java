package com.teamb13.teamcreater.utils;

import javafx.scene.control.TextInputDialog;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.Optional;

public final class InputDialogCreator {

    private final static String EMPTY = "";

    private InputDialogCreator() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public static Optional<String> getText(@Nullable String title,
                                           @Nullable String headerText,
                                           @Nullable String content) {
        return getText(title, headerText, content, EMPTY);
    }

    public static Optional<String> getText(@Nullable String title,
                                           @Nullable String headerText,
                                           @Nullable String content,
                                           @Nullable String defaultText) {
        final var textInputDialog = new TextInputDialog(Objects.requireNonNullElse(defaultText, EMPTY));
        textInputDialog.setTitle(title);
        textInputDialog.setHeaderText(headerText);
        textInputDialog.setContentText(content);

        return textInputDialog.showAndWait();
    }
}

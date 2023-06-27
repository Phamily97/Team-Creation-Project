package com.teamb13.teamcreater.utils;

import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.Objects;

public final class FileDialogCreator {

    private FileDialogCreator() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public enum PreDefinedFormat {
        EMPTY(new FileChooser.ExtensionFilter[0]),
        SUPPORTED_FILES(new FileChooser.ExtensionFilter[]{
                new FileChooser.ExtensionFilter(
                        "Supported file",
                        "*.xlsx", "*.csv")});

        @NotNull
        private final FileChooser.ExtensionFilter[] extensionFilters;

        PreDefinedFormat(@NotNull FileChooser.ExtensionFilter[] extensionFilter) {
            this.extensionFilters = extensionFilter;
        }
    }

    @SuppressWarnings("unused")
    public static @Nullable File defaultGetFile(@Nullable String title, @Nullable Window window) {
        return getFile(title, window, null, PreDefinedFormat.EMPTY);
    }

    public static @Nullable File getFile(@Nullable String title,
                                         @Nullable Window window,
                                         @Nullable File path,
                                         @Nullable PreDefinedFormat preDefinedFormat) {
        return getFile(title, window, path, Objects.requireNonNullElse(preDefinedFormat,
                PreDefinedFormat.EMPTY).extensionFilters);
    }

    private static @Nullable File getFile(@Nullable String title,
                                          @Nullable Window window,
                                          @Nullable File path,
                                          FileChooser.ExtensionFilter @Nullable ... extensionFilters) {
        final var fileChooser = new FileChooser();
        fileChooser.setTitle(title);
        fileChooser.setInitialDirectory(path);
        if (extensionFilters != null) {
            fileChooser.getExtensionFilters().addAll(extensionFilters);
        }

        return fileChooser.showOpenDialog(window != null ? window : new Stage());
    }
}

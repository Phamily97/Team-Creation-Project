package com.teamb13.teamcreater.utils;

import java.io.File;
import java.nio.file.Path;

public final class PathUtils {

    private PathUtils() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public static Path getCurrentPath() {
        return getCurrentPathFileObject().toPath();
    }

    public static File getCurrentPathFileObject() {
        return new File(System.getProperty("user.dir"));
    }
}

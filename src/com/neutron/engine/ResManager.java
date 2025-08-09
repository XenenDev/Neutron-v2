package com.neutron.engine;

import com.neutron.engine.func.SoundClip;

import javax.imageio.ImageIO;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ResManager {
    private static final Map<String, Object> resources = new HashMap<>(); //lazy load system
    private static final List<String> invalidPaths = new ArrayList<>();

    public static void prepare(String path) {
        File f = new File(path);

        // Check if the file exists and is readable
        if (!f.exists() || !f.isFile() || !f.canRead()) {
            handleInvalidPath(path);
            return;
        }

        // Determine file type and load accordingly
        if (isImageFile(f)) {
            BufferedImage image = img(path);
            if (image != null) {
                resources.put(f.getAbsolutePath(), image);
            }
        } else if (isSoundFile(f)) {
            SoundClip sound = sound(path);
            if (sound != null) {
                resources.put(f.getAbsolutePath(), sound);
            }
        } else {
            System.err.println("Unsupported file type: " + path);
        }
    }


    public static void unload(String path) {
        resources.remove(path);
    }

    public static void clear() {
        resources.clear();
    }

    public static void prepareList(String... paths) {
        for (String path : paths) {
            prepare(path);
        }
    }

    public static void unloadList(String... paths) {
        for (String path : paths) {
            unload(path);
        }
    }

    public static BufferedImage img(String path) {
        File f = new File(path);

        // Check if the file has a valid image extension
        if (!isImageFile(f)) {
            System.err.println("Invalid image file type: " + path);
            return null;
        }

        Object o = resources.get(f.getAbsolutePath());
        BufferedImage image = null;
        if (o instanceof BufferedImage)
            image = (BufferedImage) o;

        if (image == null) {
            try {
                image = ImageIO.read(f);
            } catch (IOException e) {
                handleInvalidPath(path);
            }

            if (image == null) {
                System.err.println("The file was not the appropriate image file type.");
            } else {
                resources.put(f.getAbsolutePath(), image);
            }
        }

        return image;
    }

    public static SoundClip sound(String path) {
        File f = new File(path);

        // Check if the file has a valid sound extension
        if (!isSoundFile(f)) {
            System.err.println("Invalid sound file type: " + path);
            return null;
        }

        Object o = resources.get(f.getAbsolutePath());
        SoundClip sound = null;
        if (o instanceof SoundClip)
            sound = (SoundClip) o;

        if (sound == null) {
            try {
                sound = new SoundClip(f.getAbsolutePath());
            } catch (IOException e) {
                handleInvalidPath(path);
            } catch (UnsupportedAudioFileException | LineUnavailableException e) {
                throw new RuntimeException(e);
            }

            if (sound == null) {
                System.err.println("File exists but isn't the correct type (not a sound file).");
            } else {
                resources.put(f.getAbsolutePath(), sound);
            }
        }

        return sound;
    }

    // Helper methods to check file extensions
    private static boolean isImageFile(File file) {
        String name = file.getName().toLowerCase();
        return name.endsWith(".png") || name.endsWith(".jpg") || name.endsWith(".jpeg") || name.endsWith(".bmp") || name.endsWith(".gif");
    }

    private static boolean isSoundFile(File file) {
        String name = file.getName().toLowerCase();
        return name.endsWith(".wav") || name.endsWith(".mp3") || name.endsWith(".ogg") || name.endsWith(".flac") || name.endsWith(".aac");
    }


    private static void handleInvalidPath(String path) {
        if (!invalidPaths.contains(path)) {
            invalidPaths.add(path);
            if (!(new File(path).exists())) {
                System.err.println("Path doesn't exist: '" + path + "'");
            } else if (new File(path).isDirectory()) {
                System.err.println("Path is to a directory, not a file: '" + path + "'");
            } else {
                System.err.println("The path: '" + path + "' either doesn't exist, can't be read or is a directory.");
            }
        }
    }

}

package com.neutron.engine;

import com.neutron.engine.func.ResourceType;
import javax.sound.sampled.*;
import java.io.*;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ResourceManager {
    // Thread-safety lock for synchronous multi-threaded access
    private static final Object lock = new Object();

    // ID → handle (int)
    private static final Map<Long, Integer> idMap = new HashMap<>();

    // handle → object
    private static final Map<Integer, Object> handleMap = new HashMap<>();

    // path → handle (deduplication)
    private static final Map<String, Integer> pathMap = new HashMap<>();

    private static final List<String> invalidPaths = new ArrayList<>();
    private static int nextHandle = 1; // simple counter for unique handles

    public static ResourceType load(String path, long resourceId) {
        synchronized (lock) {
            File f = new File(path);

            if (!f.exists() || !f.isFile() || !f.canRead()) {
                handleInvalidPath(path);
                return null;
            }

            // Check if already loaded by path
            Integer handle = pathMap.get(path);
            Object resource;

            if (handle != null) {
                // Already loaded, reuse
                resource = handleMap.get(handle);
            } else {
                // Load new resource
                if (isImageFile(f)) {
                    resource = img(path);
                    if (resource == null) return null;
                } else if (isSoundFile(f)) {
                    resource = sound(path);
                    if (resource == null) return null;
                } else {
                    System.err.println("Unsupported file type: " + path);
                    return null;
                }

                // Assign new handle
                handle = nextHandle++;
                handleMap.put(handle, resource);
                pathMap.put(path, handle);
            }

            // Map ID → handle
            idMap.put(resourceId, handle);

            return (resource instanceof BufferedImage) ? ResourceType.IMAGE : ResourceType.SOUND;
        }
    }

    public static Object fetch(long id) {
        synchronized (lock) {
            Integer handle = idMap.get(id);
            return (handle != null) ? handleMap.get(handle) : null;
        }
    }

    public static void unload(long id, boolean fullUnload) {
        synchronized (lock) {
            Integer handle = idMap.remove(id);
            if (handle == null) return;

            if (fullUnload) {
                // Remove the object entirely
                Object obj = handleMap.remove(handle);

                // Also remove from pathMap (reverse lookup)
                pathMap.values().removeIf(h -> h.equals(handle));
            }
            // If fullUnload == false, the object stays cached in handleMap/pathMap
            // so other IDs or future loads can reuse it.
        }
    }

    public static void clear() {
        synchronized (lock) {
            idMap.clear();
            handleMap.clear();
            pathMap.clear();
            nextHandle = 1;
        }
    }

    // --- Loaders ---
    static BufferedImage img(String path) {
        try {
            return ImageIO.read(new File(path));
        } catch (IOException e) {
            handleInvalidPath(path);
            return null;
        }
    }

    private static Sound sound(String path) {
        try {
            return new Sound(path);
        } catch (IOException e) {
            handleInvalidPath(path);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    // --- File type checks ---
    private static boolean isImageFile(File file) {
        String name = file.getName().toLowerCase();
        return name.endsWith(".png") || name.endsWith(".jpg") || name.endsWith(".jpeg")
                || name.endsWith(".bmp") || name.endsWith(".gif");
    }

    private static boolean isSoundFile(File file) {
        String name = file.getName().toLowerCase();
        return name.endsWith(".wav") || name.endsWith(".mp3") || name.endsWith(".ogg")
                || name.endsWith(".flac") || name.endsWith(".aac");
    }

    private static void handleInvalidPath(String path) {
        synchronized (invalidPaths) {
            if (!invalidPaths.contains(path)) {
                invalidPaths.add(path);
                File f = new File(path);
                if (!f.exists()) {
                    System.err.println("Path doesn't exist: '" + path + "'");
                } else if (f.isDirectory()) {
                    System.err.println("Path is to a directory, not a file: '" + path + "'");
                } else {
                    System.err.println("The path: '" + path + "' either doesn't exist, can't be read or is a directory.");
                }
            }
        }
    }

    public static class Sound {
        private final Clip clip;
        private final AudioFormat format;
        private final byte[] rawData; // keep PCM data here

        public Sound(String path) throws Exception {
            try (AudioInputStream ais = AudioSystem.getAudioInputStream(new File(path))) {
                this.format = ais.getFormat();

                // Read raw PCM bytes
                this.rawData = ais.readAllBytes();

                // Open clip with the same stream
                this.clip = AudioSystem.getClip();
                ByteArrayInputStream bais = new ByteArrayInputStream(rawData);
                AudioInputStream newStream = new AudioInputStream(bais, format, rawData.length / format.getFrameSize());
                this.clip.open(newStream);
            }
        }

        public Clip getClip() { return clip; }
        public AudioFormat getFormat() { return format; }
        public byte[] getRawData() { return rawData; }

        public AudioInputStream getFormatStream() {
            ByteArrayInputStream bais = new ByteArrayInputStream(rawData);
            return new AudioInputStream(bais, format, rawData.length / format.getFrameSize());
        }

    }

}


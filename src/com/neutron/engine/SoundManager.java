package com.neutron.engine;

import com.neutron.engine.func.AudioEffect;
import com.neutron.engine.func.Resource;
import com.neutron.engine.func.ResourceType;

import javax.sound.sampled.*;
import java.util.*;

public class SoundManager {
    private static float mainVolume = 1.0f; // normalized [0.0, 1.0]
    private static final List<SoundInstance> activeSounds = new ArrayList<>();

    // --- Volume control ---
    public static void setMainVolume(float volume) {
        SoundManager.mainVolume = Math.max(0f, Math.min(1f, volume));
    }

    public static float getMainVolume() {
        return SoundManager.mainVolume;
    }

    // --- Play sound with optional tags ---
    public static void play(Resource resource, float volume, AudioEffect effect, String... tags) {
        if (resource == null) return;

        if (resource.getType() != ResourceType.SOUND) {
            System.err.println("Resource is not an audio type: " + resource.getPath());
            return;
        }

        Object obj = resource.get();
        if (!(obj instanceof ResourceManager.Sound sound)) {
            System.err.println("Resource type SOUND but object is not Sound");
            return;
        }

        try {
            // Always overlap: create a fresh Clip
            Clip clip = AudioSystem.getClip();
            clip.open(sound.getFormatStream()); // assume Sound can provide a new AudioInputStream

            // Apply volume
            float effectiveVolume = Math.max(0f, Math.min(1f, mainVolume * volume));
            applyVolume(clip, effectiveVolume);

            // Apply effect
            if (effect != null) {
                effect.apply(sound); // effect modifies PCM before playback
            }

            // Track instance with tags
            SoundInstance instance = new SoundInstance(clip, new HashSet<>(Arrays.asList(tags)));
            activeSounds.add(instance);

            // Auto-remove when finished
            clip.addLineListener(ev -> {
                if (ev.getType() == LineEvent.Type.STOP) {
                    clip.close();
                    activeSounds.remove(instance);
                }
            });

            clip.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // --- Stop sounds by tag ---
    public static void stopByTag(String tag) {
        for (SoundInstance instance : new ArrayList<>(activeSounds)) {
            if (instance.tags.contains(tag)) {
                instance.clip.stop();
                instance.clip.close();
                activeSounds.remove(instance);
            }
        }
    }

    public static void stopByTags(String... tags) {
        Set<String> tagSet = new HashSet<>(Arrays.asList(tags));
        for (SoundInstance instance : new ArrayList<>(activeSounds)) {
            if (!Collections.disjoint(instance.tags, tagSet)) {
                instance.clip.stop();
                instance.clip.close();
                activeSounds.remove(instance);
            }
        }
    }

    // --- Stop all sounds ---
    public static void stopAll() {
        for (SoundInstance instance : new ArrayList<>(activeSounds)) {
            instance.clip.stop();
            instance.clip.close();
        }
        activeSounds.clear();
    }

    // --- Volume helper ---
    private static void applyVolume(Clip clip, float normalizedVolume) {
        try {
            FloatControl gain = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            float v = (normalizedVolume <= 0f) ? 0.0001f : normalizedVolume;
            float dB = (float) (20.0 * Math.log10(v));
            dB = Math.max(gain.getMinimum(), Math.min(gain.getMaximum(), dB));
            gain.setValue(dB);
        } catch (IllegalArgumentException ignored) {
            // MASTER_GAIN not supported
        }
    }

    // --- Inner helper class ---
    private static class SoundInstance {
        final Clip clip;
        final Set<String> tags;

        SoundInstance(Clip clip, Set<String> tags) {
            this.clip = clip;
            this.tags = tags;
        }
    }
}
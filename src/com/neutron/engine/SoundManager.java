package com.neutron.engine;

import com.neutron.engine.func.AudioEffect;
import com.neutron.engine.func.Resource;
import com.neutron.engine.func.ResourceType;

import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;

public class SoundManager {
    private static float mainVolume = 1.0f; // normalized [0.0, 1.0]

    public static void setMainVolume(float volume) {
        SoundManager.mainVolume = Math.max(0f, Math.min(1f, volume));
    }

    public static float getMainVolume() { return SoundManager.mainVolume; }

    public static void play(Resource resource, float volume, AudioEffect effect) {
        if (resource == null) return;

        // Only handle audio resources
        if (resource.getType() != ResourceType.SOUND) {
            System.err.println("Resource is not an audio type: " + resource.getPath());
            return;
        }

        // Resource.get() returns the underlying object (Sound)
        Object obj = resource.get();
        if (!(obj instanceof ResourceManager.Sound sound)) {
            System.err.println("Resource type AUDIO but object is not Sound");
            return;
        }

        Clip clip = sound.getClip();

        if (clip.isRunning()) clip.stop();
        clip.setFramePosition(0);

        // Apply volume
        float effectiveVolume = Math.max(0f, Math.min(1f, mainVolume * volume));
        applyVolume(clip, effectiveVolume);

        // Apply effect (stub)
        if (effect != null) {
            effect.apply(sound);
        }

        clip.start();
    }

    private static void applyVolume(Clip clip, float normalizedVolume) {
        try {
            FloatControl gain = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            float v = (normalizedVolume <= 0f) ? 0.0001f : normalizedVolume;
            float dB = (float) (20.0 * Math.log10(v));
            dB = Math.max(gain.getMinimum(), Math.min(gain.getMaximum(), dB));
            gain.setValue(dB);
        } catch (IllegalArgumentException ignored) {
            // MASTER_GAIN not supported, ignore
        }
    }
}
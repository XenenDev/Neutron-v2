package com.neutron.engine.func;

import com.neutron.engine.ResourceManager.Sound;

import javax.sound.sampled.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import javax.sound.sampled.AudioFormat;
/**
 * A configurable audio effect that can apply gain, echo, and stretch. TODO!
 */
public class AudioEffect {
    private final double gain;       // volume multiplier
    private final int echoDelayMs;   // echo delay in ms
    private final double echoDecay;  // echo decay factor
    private final double stretch;    // playback stretch factor

    public AudioEffect(double gain, int echoDelayMs, double echoDecay, double stretch) {
        this.gain = gain;
        this.echoDelayMs = echoDelayMs;
        this.echoDecay = echoDecay;
        this.stretch = stretch;
    }

    public void apply(Sound sound) {
        try {
            byte[] processed = sound.getRawData();

            if (gain != 1.0) processed = AudioEffects.applyGain(processed, (float) gain, sound.getFormat());
            if (echoDelayMs > 0 && echoDecay > 0) processed = AudioEffects.applyEcho(processed, sound.getFormat(), echoDelayMs, (float) echoDecay);
            if (stretch != 1.0) processed = AudioEffects.stretch(processed, (float) stretch);

            // Reload processed audio into clip
            Clip clip = sound.getClip();
            clip.close();
            ByteArrayInputStream bais = new ByteArrayInputStream(processed);
            AudioInputStream ais = new AudioInputStream(bais, sound.getFormat(), processed.length / sound.getFormat().getFrameSize());
            clip.open(ais);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void reloadClip(Clip clip, byte[] data, AudioFormat format) throws LineUnavailableException {
        clip.close();
        ByteArrayInputStream bais = new ByteArrayInputStream(data);
        AudioInputStream ais = new AudioInputStream(bais, format, data.length / format.getFrameSize());
        try {
            clip.open(ais);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static class AudioEffects {

        public static byte[] applyGain(byte[] audioData, float gain, AudioFormat format) {
            if (gain <= 0f) gain = 0.0001f;
            int bytesPerSample = format.getSampleSizeInBits() / 8;
            boolean bigEndian = format.isBigEndian();
            boolean signed = format.getEncoding() == AudioFormat.Encoding.PCM_SIGNED;

            byte[] out = new byte[audioData.length];

            for (int i = 0; i < audioData.length; i += bytesPerSample) {
                int sample = 0;

                if (bytesPerSample == 2) {
                    if (bigEndian) {
                        sample = (short) ((audioData[i] << 8) | (audioData[i + 1] & 0xFF));
                    } else {
                        sample = (short) ((audioData[i + 1] << 8) | (audioData[i] & 0xFF));
                    }
                } else if (bytesPerSample == 1) {
                    sample = signed ? audioData[i] : (audioData[i] & 0xFF) - 128;
                }

                sample = (int) (sample * gain);
                if (sample > Short.MAX_VALUE) sample = Short.MAX_VALUE;
                if (sample < Short.MIN_VALUE) sample = Short.MIN_VALUE;

                if (bytesPerSample == 2) {
                    if (bigEndian) {
                        out[i] = (byte) ((sample >> 8) & 0xFF);
                        out[i + 1] = (byte) (sample & 0xFF);
                    } else {
                        out[i] = (byte) (sample & 0xFF);
                        out[i + 1] = (byte) ((sample >> 8) & 0xFF);
                    }
                } else if (bytesPerSample == 1) {
                    out[i] = (byte) (signed ? sample : sample + 128);
                }
            }

            return out;
        }

        public static byte[] applyEcho(byte[] audioData, AudioFormat format, int delayMs, float decay) {
            int bytesPerSample = format.getSampleSizeInBits() / 8;
            int channels = format.getChannels();
            int frameSize = bytesPerSample * channels;
            int delayFrames = (int) ((delayMs / 1000.0) * format.getFrameRate());

            byte[] out = audioData.clone();

            for (int i = delayFrames * frameSize; i < audioData.length; i += frameSize) {
                for (int b = 0; b < bytesPerSample; b++) {
                    int delayedIndex = i - delayFrames * frameSize + b;
                    int currentIndex = i + b;

                    if (currentIndex < out.length && delayedIndex >= 0) {
                        int delayed = out[delayedIndex];
                        int current = out[currentIndex];
                        int mixed = (int) (current + delayed * decay);

                        if (mixed > 127) mixed = 127;
                        if (mixed < -128) mixed = -128;

                        out[currentIndex] = (byte) mixed;
                    }
                }
            }

            return out;
        }

        public static byte[] stretch(byte[] audioData, float factor) {
            if (factor <= 0f) throw new IllegalArgumentException("Stretch factor must be > 0");

            ByteArrayOutputStream out = new ByteArrayOutputStream();

            for (int i = 0; i < audioData.length; i++) {
                out.write(audioData[i]);
                if (factor > 1f) {
                    for (int j = 1; j < (int) factor; j++) {
                        out.write(audioData[i]);
                    }
                } else {
                    i += (int) (1 / factor) - 1;
                }
            }

            return out.toByteArray();
        }
    }
}

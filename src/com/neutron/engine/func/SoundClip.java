package com.neutron.engine.func;

import javax.sound.sampled.*;
import java.io.*;

public class SoundClip {

    private final Clip clip;
    private final FloatControl gainControl;
    public SoundClip(String path) throws IOException, UnsupportedAudioFileException, LineUnavailableException {
            InputStream audioSource = new FileInputStream(path);
            InputStream bufferedIn = new BufferedInputStream(audioSource);
            AudioInputStream ais = AudioSystem.getAudioInputStream(bufferedIn);
            AudioFormat baseFormat = ais.getFormat();
            AudioFormat decodeFormat = new AudioFormat(
                    AudioFormat.Encoding.PCM_SIGNED,
                    baseFormat.getSampleRate(),
                    16,
                    baseFormat.getChannels(),
                    baseFormat.getChannels() * 2,
                    baseFormat.getSampleRate(),
                    false
            );

            AudioInputStream dais = AudioSystem.getAudioInputStream(decodeFormat, ais);

            clip = AudioSystem.getClip();
            clip.open(dais);

            gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
    }

    public void play() {
        if (clip == null) return;
        stop();
        clip.setFramePosition(0);
        while (!clip.isRunning()) {
            clip.start();
        }
    }

    public void stop() {
        if (clip.isRunning()) clip.stop();
    }

    public void close() {
        stop();
        clip.drain();
        clip.close();
    }

    public void loop(int count) {
        clip.loop(count); //-1 for continuous loop
        play();
    }

    public void setVolume(float value) {
        gainControl.setValue(value);
    }

    public boolean isRunning() {
        return clip.isRunning();
    }
}

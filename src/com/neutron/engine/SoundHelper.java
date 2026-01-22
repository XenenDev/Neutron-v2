package com.neutron.engine;

import com.neutron.engine.func.AudioEffect;
import com.neutron.engine.func.Resource;

import java.util.*;
import java.util.function.Supplier;

/**
 * Internal helper class for managing sound rules.
 * Users should not interact with this directly - use the SoundEmitter interface instead.
 */
public class SoundHelper {

    private static final Map<Long, List<SoundRule>> objectRules = new HashMap<>();
    private static final Map<String, Boolean> ruleStates = new HashMap<>();

    /**
     * Represents a sound rule with a condition and playback settings
     */
    public static class SoundRule {
        final Resource sound;
        final String tag;
        final Supplier<Boolean> condition;
        final float volume;
        final boolean onlyOnChange;
        final AudioEffect effect;

        public SoundRule(Resource sound, Supplier<Boolean> condition, float volume,
                         String tag, boolean onlyOnChange, AudioEffect effect) {
            this.sound = sound;
            this.condition = condition;
            this.volume = volume;
            this.tag = tag;
            this.onlyOnChange = onlyOnChange;
            this.effect = effect;
        }

        public SoundRule(Resource sound, Supplier<Boolean> condition, float volume,
                         String tag, boolean onlyOnChange) {
            this(sound, condition, volume, tag, onlyOnChange, null);
        }
    }

    /**
     * Add a sound rule for a GameObject
     *
     * @param objectId     The GameObject's unique ID
     * @param sound        The sound resource to play
     * @param condition    Supplier that returns true when sound should play
     * @param volume       Volume level (0.0 to 1.0)
     * @param tag          Unique tag for this sound (used to stop/track it)
     * @param onlyOnChange If true, plays once when condition becomes true. If false, loops while condition is true
     * @param effect       Optional audio effect to apply
     */
    public static void addRule(long objectId, Resource sound, Supplier<Boolean> condition,
                               float volume, String tag, boolean onlyOnChange, AudioEffect effect) {
        objectRules.computeIfAbsent(objectId, k -> new ArrayList<>())
                .add(new SoundRule(sound, condition, volume, tag, onlyOnChange, effect));
    }

    /**
     * Add a sound rule without audio effect
     */
    public static void addRule(long objectId, Resource sound, Supplier<Boolean> condition,
                               float volume, String tag, boolean onlyOnChange) {
        addRule(objectId, sound, condition, volume, tag, onlyOnChange, null);
    }

    /**
     * Update all sound rules for a specific GameObject. Called by ObjectHandler during update cycle.
     *
     * @param objectId The GameObject's unique ID
     */
    public static void update(long objectId) {
        List<SoundRule> rules = objectRules.get(objectId);
        if (rules == null) return;

        for (SoundRule rule : rules) {
            boolean currentState = rule.condition.get();
            String stateKey = objectId + ":" + rule.tag;
            Boolean previousState = ruleStates.get(stateKey);

            if (rule.onlyOnChange) {
                // Play only when condition transitions from false to true
                if (currentState && (previousState == null || !previousState)) {
                    SoundManager.play(rule.sound, rule.volume, rule.effect, rule.tag);
                }
            } else {
                // Continuous playback while condition is true
                if (currentState && (previousState == null || !previousState)) {
                    // Condition just became true - start playing
                    SoundManager.play(rule.sound, rule.volume, rule.effect, rule.tag);
                } else if (!currentState && previousState != null && previousState) {
                    // Condition just became false - stop playing
                    SoundManager.stopByTag(rule.tag);
                }
            }

            ruleStates.put(stateKey, currentState);
        }
    }

    /**
     * Remove all rules and state for a specific GameObject. Called when the object is deleted.
     *
     * @param objectId The GameObject's unique ID
     */
    public static void cleanup(long objectId) {
        List<SoundRule> rules = objectRules.remove(objectId);
        if (rules != null) {
            // Stop all sounds associated with this object and clean up state
            for (SoundRule rule : rules) {
                String uniqueTag = objectId + ":" + rule.tag;
                SoundManager.stopByTag(uniqueTag);
                ruleStates.remove(objectId + ":" + rule.tag);
            }
        }
    }// Use object-specific tag to avoid conflicts between different objects
            String uniqueTag = objectId + ":" + rule.tag;

            if (rule.onlyOnChange) {
                // Play only when condition transitions from false to true
                if (currentState && (previousState == null || !previousState)) {
                    SoundManager.play(rule.sound, rule.volume, rule.effect, uniqueTag);
                }
            } else {
                // Continuous playback while condition is true
                if (currentState && (previousState == null || !previousState)) {
                    // Condition just became true - start playing
                    SoundManager.play(rule.sound, rule.volume, rule.effect, uniqueTag);
                } else if (!currentState && previousState != null && previousState) {
                    // Condition just became false - stop playing
                    SoundManager.stopByTag(uniqueT
     */
    public static int getRegisteredObjectCount() {
        return objectRules.size();
    }
}

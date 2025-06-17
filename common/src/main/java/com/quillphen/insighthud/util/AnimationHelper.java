package com.quillphen.insighthud.util;

import net.minecraft.util.Mth;
import java.util.HashMap;
import java.util.Map;

/**
 * Enhanced helper class for smooth animations in the HUD with multiple animation types
 */
public class AnimationHelper {
    
    private static final Map<String, Float> animationValues = new HashMap<>();
    private static final Map<String, Long> animationStartTimes = new HashMap<>();
    private static final Map<String, Float> targetValues = new HashMap<>();
    private static final Map<String, Float> startValues = new HashMap<>();
    
    /**
     * Smoothly animates a value towards a target with linear interpolation
     */
    public static float animate(String key, float target, float speed, float partialTick) {
        float current = animationValues.getOrDefault(key, target);
        float newValue = Mth.lerp(speed, current, target);
        animationValues.put(key, newValue);
        return newValue;
    }
    
    /**
     * Animates a value with easing over a specific duration
     */
    public static float animateEased(String key, float target, long duration, EaseType easing) {
        long currentTime = System.currentTimeMillis();
        
        if (!targetValues.containsKey(key) || !targetValues.get(key).equals(target)) {
            targetValues.put(key, target);
            animationStartTimes.put(key, currentTime);
            startValues.put(key, animationValues.getOrDefault(key, 0.0f));
        }
        
        long startTime = animationStartTimes.getOrDefault(key, currentTime);
        float progress = Math.min(1.0f, (currentTime - startTime) / (float) duration);
        
        if (progress >= 1.0f) {
            animationValues.put(key, target);
            return target;
        }
        
        float start = startValues.getOrDefault(key, 0.0f);
        float easedProgress = applyEasing(progress, easing);
        float current = Mth.lerp(easedProgress, start, target);
        
        animationValues.put(key, current);
        return current;
    }
    
    /**
     * Creates a pulsing animation with customizable frequency and amplitude
     */
    public static float pulse(String key, float baseValue, float amplitude, double frequency) {
        long time = System.currentTimeMillis();
        float pulseValue = (float) Math.sin(time * frequency) * amplitude;
        return baseValue + pulseValue;
    }
    
    /**
     * Creates a smooth breathing effect
     */
    public static float breathe(String key, float baseValue, float amplitude, double frequency) {
        long time = System.currentTimeMillis();
        float breatheValue = (float) (Math.sin(time * frequency) * 0.5 + 0.5) * amplitude;
        return baseValue + breatheValue;
    }
    
    /**
     * Fade in/out animation with smooth transitions
     */
    public static float fade(String key, boolean visible, float speed) {
        float target = visible ? 1.0f : 0.0f;
        return animate(key, target, speed, 1.0f);
    }
    
    /**
     * Slide animation for UI elements
     */
    public static float slide(String key, float target, float speed, EaseType easing) {
        return animateEased(key, target, (long)(1000 / speed), easing);
    }
    
    /**
     * Scale animation with bounce effect
     */
    public static float scale(String key, float target, boolean bounce) {
        EaseType easing = bounce ? EaseType.BOUNCE : EaseType.EASE_OUT;
        return animateEased(key, target, 300, easing);
    }
    
    /**
     * Color animation between two colors
     */
    public static int animateColor(String key, int fromColor, int toColor, float duration) {
        float progress = animateEased(key, 1.0f, (long)(duration * 1000), EaseType.EASE_IN_OUT);
        return lerpColor(fromColor, toColor, progress);
    }
    
    /**
     * Spring animation with overshoot
     */
    public static float spring(String key, float target, float tension, float friction) {
        long currentTime = System.currentTimeMillis();
        
        if (!targetValues.containsKey(key) || !targetValues.get(key).equals(target)) {
            targetValues.put(key, target);
            animationStartTimes.put(key, currentTime);
            startValues.put(key, animationValues.getOrDefault(key, 0.0f));
        }
        
        long startTime = animationStartTimes.getOrDefault(key, currentTime);
        float t = (currentTime - startTime) / 1000.0f; // Convert to seconds
        
        float start = startValues.getOrDefault(key, 0.0f);
        float distance = target - start;
        
        // Spring physics calculation
        float omega = (float) Math.sqrt(tension);
        float zeta = friction / (2 * (float) Math.sqrt(tension));
        
        float current;
        if (zeta < 1) {
            // Underdamped
            float omegaD = omega * (float) Math.sqrt(1 - zeta * zeta);
            current = target - distance * (float) Math.exp(-zeta * omega * t) * 
                      (float) Math.cos(omegaD * t);
        } else {
            // Overdamped or critically damped
            current = target - distance * (float) Math.exp(-omega * t);
        }
        
        animationValues.put(key, current);
        return current;
    }
    
    /**
     * Wobble effect for emphasis
     */
    public static float wobble(String key, float baseValue, float amplitude, double frequency, float decay) {
        long currentTime = System.currentTimeMillis();
        
        if (!animationStartTimes.containsKey(key)) {
            animationStartTimes.put(key, currentTime);
        }
        
        long startTime = animationStartTimes.get(key);
        float time = (currentTime - startTime) / 1000.0f;
        
        float decayFactor = (float) Math.exp(-decay * time);
        float wobbleValue = (float) Math.sin(time * frequency) * amplitude * decayFactor;
        
        return baseValue + wobbleValue;
    }
    
    /**
     * Reset animation to start from beginning
     */
    public static void reset(String key) {
        animationValues.remove(key);
        animationStartTimes.remove(key);
        targetValues.remove(key);
        startValues.remove(key);
    }
    
    /**
     * Get current animation value without updating
     */
    public static float getCurrentValue(String key) {
        return animationValues.getOrDefault(key, 0.0f);
    }
    
    /**
     * Check if animation is complete
     */
    public static boolean isComplete(String key, float target, float threshold) {
        float current = animationValues.getOrDefault(key, target);
        return Math.abs(current - target) < threshold;
    }
    
    private static float applyEasing(float progress, EaseType easing) {
        return switch (easing) {
            case LINEAR -> progress;
            case EASE_IN -> progress * progress;
            case EASE_OUT -> 1.0f - (1.0f - progress) * (1.0f - progress);
            case EASE_IN_OUT -> progress < 0.5f ? 
                2.0f * progress * progress : 
                1.0f - 2.0f * (1.0f - progress) * (1.0f - progress);
            case EASE_IN_CUBIC -> progress * progress * progress;
            case EASE_OUT_CUBIC -> 1.0f - (float) Math.pow(1.0f - progress, 3);
            case EASE_IN_OUT_CUBIC -> progress < 0.5f ?
                4.0f * progress * progress * progress :
                1.0f - (float) Math.pow(-2.0f * progress + 2.0f, 3) / 2.0f;
            case BOUNCE -> {
                float n1 = 7.5625f;
                float d1 = 2.75f;
                
                if (progress < 1.0f / d1) {
                    yield n1 * progress * progress;
                } else if (progress < 2.0f / d1) {
                    progress -= 1.5f / d1;
                    yield n1 * progress * progress + 0.75f;
                } else if (progress < 2.5f / d1) {
                    progress -= 2.25f / d1;
                    yield n1 * progress * progress + 0.9375f;
                } else {
                    progress -= 2.625f / d1;
                    yield n1 * progress * progress + 0.984375f;
                }
            }
            case ELASTIC -> {
                float c4 = (2.0f * (float) Math.PI) / 3.0f;
                if (progress == 0) yield 0.0f;
                else if (progress == 1) yield 1.0f;
                else yield (float) Math.pow(2, -10 * progress) * 
                         (float) Math.sin((progress * 10 - 0.75) * c4) + 1;
            }
            case BACK -> {
                float c1 = 1.70158f;
                float c3 = c1 + 1;
                yield c3 * progress * progress * progress - c1 * progress * progress;
            }
        };
    }
    
    private static int lerpColor(int color1, int color2, float t) {
        int r1 = (color1 >> 16) & 0xFF;
        int g1 = (color1 >> 8) & 0xFF;
        int b1 = color1 & 0xFF;
        int a1 = (color1 >> 24) & 0xFF;
        
        int r2 = (color2 >> 16) & 0xFF;
        int g2 = (color2 >> 8) & 0xFF;
        int b2 = color2 & 0xFF;
        int a2 = (color2 >> 24) & 0xFF;
        
        int r = (int) Mth.lerp(t, r1, r2);
        int g = (int) Mth.lerp(t, g1, g2);
        int b = (int) Mth.lerp(t, b1, b2);
        int a = (int) Mth.lerp(t, a1, a2);
        
        return (a << 24) | (r << 16) | (g << 8) | b;
    }
    
    public enum EaseType {
        LINEAR,
        EASE_IN,
        EASE_OUT,
        EASE_IN_OUT,
        EASE_IN_CUBIC,
        EASE_OUT_CUBIC,
        EASE_IN_OUT_CUBIC,
        BOUNCE,
        ELASTIC,
        BACK
    }
}

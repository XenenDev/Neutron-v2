package com.neutron.engine;

import com.neutron.engine.func.GraphicsFidelity;
import com.neutron.engine.func.Shader;

import java.awt.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.IntUnaryOperator;

public class Renderer {

    private final BufferStrategy bufferStrategy;
    public final Graphics2D graphics;

    private boolean useScreenCoordinates;
    private double scale = 1;
    private int cameraX = 0, cameraY = 0;

    private final BufferedImage lightmap; //TODO

    public final int WIDTH, HEIGHT;
    public final int CENTER_X, CENTER_Y;


    public Renderer(Window window, GraphicsFidelity gq) {
        Canvas canvas = window.getCanvas();
        canvas.createBufferStrategy(2);

        bufferStrategy = canvas.getBufferStrategy();
        graphics = (Graphics2D) bufferStrategy.getDrawGraphics();

        this.setGraphicsFidelity(gq);

        graphics.setBackground(Color.BLACK);
        useScreenCoordinates = false;
        this.setCameraZoom(this.scale);

        this.HEIGHT = canvas.getHeight();
        this.WIDTH = canvas.getWidth();
        this.CENTER_X = this.WIDTH / 2;
        this.CENTER_Y = this.HEIGHT / 2;

        lightmap = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB); //TODO
        int[] pixels = new int[WIDTH * HEIGHT];
        Arrays.fill(pixels, 0xFF000000); // Fill with opaque black

        lightmap.setRGB(0, 0, WIDTH, HEIGHT, pixels, 0, WIDTH);
    }

    public void setGraphicsFidelity(GraphicsFidelity gq) {
        graphics.setRenderingHints(new HashMap<>());
        if (gq.useAAForTextOnly()) {
            graphics.addRenderingHints(new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF));
            graphics.addRenderingHints(new RenderingHints(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON));
        } else {
            if (gq.useSubPixelFontRendering()) {
                graphics.addRenderingHints((Map<?, ?>) Toolkit.getDefaultToolkit().getDesktopProperty("awt.font.desktophints"));
                //graphics.addRenderingHints(new RenderingHints(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB));
            }

            if (gq.useGlobalAA()) {
                graphics.addRenderingHints(new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON));
            } else {
                graphics.addRenderingHints(new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF));
            }
        }

        if (gq.useQualityRendering()) {
            graphics.addRenderingHints(new RenderingHints(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY));
        } else {
            graphics.addRenderingHints(new RenderingHints(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_SPEED));
        }

        if (gq.useBilinearSampling()) {
            graphics.addRenderingHints(new RenderingHints(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR));
        } else {
            graphics.addRenderingHints(new RenderingHints(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR));
        }
    }

    public void clear() {
        double s = this.getCameraZoom();
        this.setCameraZoom(1);
        graphics.clearRect(0, 0, WIDTH, HEIGHT);
        this.setCameraZoom(s);
    }


    public void show() {
        bufferStrategy.show();
    }

    public void fillRect(int x, int y, int w, int h, Color color) {
        graphics.setColor(color);
        if (!useScreenCoordinates) {graphics.fillRect(x-cameraX, y-cameraY, w, h);}
        else {graphics.fillRect(x, y, w, h);}
    }

    public void fillSquare(int x, int y, int length, Color color) {
        graphics.setColor(color);
        if (!useScreenCoordinates) {graphics.fillRect(x-cameraX, y-cameraY, length, length);}
        else {graphics.fillRect(x, y, length, length);}
    }

    public void fillOval(int x, int y, int w, int h, Color color) {
        graphics.setColor(color);
        if (!useScreenCoordinates) {graphics.fillOval(x-cameraX, y-cameraY, w, h);}
        else {graphics.fillOval(x, y, w, h);}
    }

    public void fillCircle(int x, int y, int radius, Color color) {
        graphics.setColor(color);
        if (!useScreenCoordinates) {graphics.fillOval(x-cameraX, y-cameraY, radius, radius);}
        else {graphics.fillOval(x, y, radius, radius);}
    }

    public void drawRect(int x, int y, int w, int h, Color color) {
        graphics.setColor(color);
        if (!useScreenCoordinates) {graphics.drawRect(x-cameraX, y-cameraY, w, h);}
        else {graphics.drawRect(x, y, w, h);}
    }

    public void drawSquare(int x, int y, int length, Color color) {
        graphics.setColor(color);
        if (!useScreenCoordinates) {graphics.drawRect(x-cameraX, y-cameraY, length, length);}
        else {graphics.drawRect(x, y, length, length);}
    }

    public void drawOval(int x, int y, int w, int h, Color color) {
        graphics.setColor(color);
        if (!useScreenCoordinates) {graphics.drawOval(x-cameraX, y-cameraY, w, h);}
        else {graphics.drawOval(x, y, w, h);}
    }

    public void drawCircle(int x, int y, int radius, Color color) {
        graphics.setColor(color);
        if (!useScreenCoordinates) {graphics.drawOval(x-cameraX, y-cameraY, radius, radius);}
        else {graphics.drawOval(x, y, radius, radius);}
    }

    public void drawLine(int x1, int y1, int x2, int y2, Color color) {
        graphics.setColor(color);
        if (!useScreenCoordinates) {graphics.drawLine(x1+cameraX, y1+cameraX, x2+cameraY, y2+cameraY);}
        else {graphics.drawLine(x1, y1, x2, y2);}
    }

    public void drawImage(Image img, int x, int y) {
        if (!useScreenCoordinates) {graphics.drawImage(img, x-cameraX, y-cameraY, null);}
        else {graphics.drawImage(img, x, y, null);}
    }

    public void drawImage(Image img, int x, int y, Color bgColor) {
        if (!useScreenCoordinates) {graphics.drawImage(img, x-cameraX, y-cameraY, bgColor, null);}
        else {graphics.drawImage(img, x, y, bgColor, null);}
    }

    public void drawImage(Image img, int x, int y, int w, int h) {
        if (!useScreenCoordinates) {graphics.drawImage(img, x-cameraX, y-cameraY, w, h, null);}
        else {graphics.drawImage(img, x, y, w, h, null);}
    }

    public void drawImage(Image img, int x, int y, int w, int h, Color bgColor) {
        if (!useScreenCoordinates) {graphics.drawImage(img, x-cameraX, y-cameraY, w, h, bgColor, null);}
        else {graphics.drawImage(img, x, y, w, h, bgColor, null);}
    }

    public void drawImage(Image img, int x, int y, float scale) {
        if (!useScreenCoordinates) {
            graphics.drawImage(
                    img,
                    x-cameraX,
                    y-cameraY,
                    (int) (img.getWidth(null) * scale),
                    (int) (img.getHeight(null) * scale),
                    null
            );
        } else {
            graphics.drawImage(
                    img,
                    x,
                    y,
                    (int) (img.getWidth(null) * scale),
                    (int) (img.getHeight(null) * scale),
                    null
            );
        }
    }

    public void drawImage(Image img, int x, int y, float scale, Color bgColor) {
        if (!useScreenCoordinates) {
            graphics.drawImage(
                    img,
                    x-cameraX,
                    y-cameraY,
                    (int) (img.getWidth(null) * scale),
                    (int) (img.getHeight(null) * scale),
                    bgColor,
                    null
            );
        } else {
            graphics.drawImage(
                    img,
                    x,
                    y,
                    (int) (img.getWidth(null) * scale),
                    (int) (img.getHeight(null) * scale),
                    bgColor,
                    null
            );
        }
    }

    public void drawText(Object string, int x, int y, Color color) {
        graphics.setColor(color);
        if (!useScreenCoordinates) {graphics.drawString(String.valueOf(string), x-cameraX, y-cameraY);}
        else {graphics.drawString(String.valueOf(string), x, y);}
    }

    public void shade(int x, int y, int w, int h, Shader shader) {
        int[] pixels = new int[w * h];

        for (int px = 0; px < w; px++) {
            for (int py = 0; py < h; py++) {
                pixels[py * w + px] = shader.shade(px, py, px/((float)w-1), py/((float)h-1), w, h).getRGB();
            }
        }

        BufferedImage image = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        image.setRGB(0, 0, w, h, pixels, 0, w);
        this.drawImage(image, x, y);
    }

    public void shade(int x, int y, int w, int h, float scale, Shader shader) {
        int[] pixels = new int[w * h];

        for (int px = 0; px < w; px++) {
            for (int py = 0; py < h; py++) {
                pixels[py * w + px] = shader.shade(px, py, px/((float)w-1), py/((float)h-1), w, h).getRGB();
            }
        }

        BufferedImage image = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        image.setRGB(0, 0, w, h, pixels, 0, w);
        this.drawImage(image, (int) (x*scale), (int) (y*scale));
    }

    public void shade(int x, int y, int w, int h, float scaleY, float scaleX, Shader shader) {
        int[] pixels = new int[w * h];

        for (int px = 0; px < w; px++) {
            for (int py = 0; py < h; py++) {
                pixels[py * w + px] = shader.shade(px, py, px/((float)w-1), py/((float)h-1), w, h).getRGB();
            }
        }

        BufferedImage image = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        image.setRGB(0, 0, w, h, pixels, 0, w);

        this.drawImage(image, x, y, (int) (w*scaleX), (int) (h*scaleY));
    }

    public void setAlpha(float alpha) {
        graphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
    }

    public void drawLightmap() {
        this.drawImage(lightmap, 0, 0);
        //TODO: Should clear the lightmap and reset it every frame.
    } //TODO

    private void blendLightImage(BufferedImage image) {
        int width = lightmap.getWidth();
        int height = lightmap.getHeight();

        // Loop through every pixel of the lightmap and the image
        for (int px = 0; px < width; px++) {
            for (int py = 0; py < height; py++) {
                // Get the color of both images
                Color lightColor = new Color(image.getRGB(px, py), true);
                Color lightmapColor = new Color(lightmap.getRGB(px, py), true);

                // Get the RGB values of both colors
                int rLight = lightColor.getRed();
                int gLight = lightColor.getGreen();
                int bLight = lightColor.getBlue();
                int aLight = lightColor.getAlpha();

                int rLightmap = lightmapColor.getRed();
                int gLightmap = lightmapColor.getGreen();
                int bLightmap = lightmapColor.getBlue();
                int aLightmap = lightmapColor.getAlpha();

                // Multiply the color values to combine the light and the lightmap
                int rResult = Math.min(255, (rLight * rLightmap) / 255);
                int gResult = Math.min(255, (gLight * gLightmap) / 255);
                int bResult = Math.min(255, (bLight * bLightmap) / 255);
                int aResult = Math.min(255, (aLight * aLightmap) / 255);

                // Set the combined color in the lightmap
                Color resultColor = new Color(rResult, gResult, bResult, aResult);
                lightmap.setRGB(px, py, resultColor.getRGB());
            }
        }
    } //TODO

    //TODO
    public void shadeLight(int x, int y, float brightness, Color color, IntUnaryOperator falloff, float cutoff) {
        int[] pixels = new int[WIDTH * HEIGHT];

        // Loop through every pixel on the screen
        for (int px = 0; px < WIDTH; px++) {
            for (int py = 0; py < HEIGHT; py++) {
                // Calculate distance from the current pixel to the light source
                double dx = px - x;
                double dy = py - y;
                double distance = Math.sqrt(dx * dx + dy * dy);

                // Check if the pixel is beyond the cutoff distance
                if (distance > cutoff) {
                    // Set the pixel to be fully transparent if it's beyond the cutoff
                    pixels[py * WIDTH + px] = new Color(0, 0, 0, 0).getRGB();
                } else {
                    // Apply the falloff function to get the brightness for this pixel
                    int falloffBrightness = falloff.applyAsInt((int) distance);

                    // Calculate the final alpha value (opacity) based on the brightness and distance
                    int alpha = (int) (brightness * falloffBrightness);

                    // Ensure the alpha value is clamped between 0 and 255
                    alpha = Math.min(Math.max(alpha, 0), 255);

                    // Combine the RGB values with the computed alpha
                    pixels[py * WIDTH + px] = new Color(0, 0, 0, alpha).getRGB();
                }
            }
        }

        // Create a BufferedImage to store the result
        BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB);
        image.setRGB(0, 0, WIDTH, HEIGHT, pixels, 0, WIDTH);
        drawImage(image, x, y);
        blendLightImage(image);
    }


    public void setFont(Font f) {
        graphics.setFont(f);
    }

    public void setLineWidth(int w) {
        graphics.setStroke(new BasicStroke(w));
    }

    public void setCameraPos(int x, int y) {
        cameraX = x;
        cameraY = y;
    }

    public void moveCameraPos(int dx, int dy) {
        cameraX += dx;
        cameraY += dy;
    }

    public int getCameraX() {
        return cameraX;
    }

    public int getCameraY() {
        return cameraY;
    }

    public void setCameraZoom(double scale) {
        graphics.translate(this.CENTER_X, this.CENTER_Y);

        double adjustedScale = Math.abs(scale) / this.scale;
        this.scale = Math.abs(scale);
        graphics.scale(adjustedScale, adjustedScale);

        graphics.translate(-this.CENTER_X, -this.CENTER_Y);
    }

    public double getCameraZoom() {
        return this.scale;
    }

    public void setUseScreenCoordinates(boolean useScreenCoordinates) {
        this.useScreenCoordinates = useScreenCoordinates;
        graphics.translate(this.CENTER_X, this.CENTER_Y);

        if (useScreenCoordinates) {
            double adjustedScale = 1 / this.scale;
            graphics.scale(adjustedScale, adjustedScale);
        } else {
            graphics.scale(this.scale, this.scale);
        }

        graphics.translate(-this.CENTER_X, -this.CENTER_Y);
    }


    public boolean getUseScreenCoordinates() {
        return useScreenCoordinates;
    }


}

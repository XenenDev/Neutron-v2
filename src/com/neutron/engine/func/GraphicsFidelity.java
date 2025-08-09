package com.neutron.engine.func;

public interface GraphicsFidelity {

    //global Anti-Aliasing, including text rendering, however text rendering can be overridden by SupPixelFontSetting
    boolean useGlobalAA();

    //Use Fancy Sub-Pixel Render for Text, Overrides Global AA just for Text (EXPENSIVE)
    boolean useSubPixelFontRendering();

    //Use AA Only on TEXT ONLY, Overrides Sub-Pixel Font Rendering and Global AA. (Mostly Useless, set to false)
    boolean useAAForTextOnly();

    //Use Speed or Quality Render Preset
    boolean useQualityRendering();

    //Choose whether to use bilinear sampling for textures or nearest-neighbor sampling
    boolean useBilinearSampling();

}

package com.djpompilio.textcrusaders.lwjgl3;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.badlogic.gdx.graphics.glutils.HdpiMode;
import com.djpompilio.textcrusaders.textCrusaders;

/** Launches the desktop (LWJGL3) application. */
public class Lwjgl3Launcher {
    public static void main(String[] args) {
        if (StartupHelper.startNewJvmIfRequired()) return; // This handles macOS support and helps on Windows.
        createApplication();
    }

    private static Lwjgl3Application createApplication() {
        return new Lwjgl3Application(new textCrusaders(), getDefaultConfiguration());
    }

    private static Lwjgl3ApplicationConfiguration getDefaultConfiguration() {
        Lwjgl3ApplicationConfiguration configuration = new Lwjgl3ApplicationConfiguration();
        configuration.setTitle("Text Crusaders");
        //configuration.setOpenGLEmulation(GLEmulation.GL30, 3, 2);
        //// Vsync limits the frames per second to what your hardware can display, and helps eliminate
        //// screen tearing. This setting doesn't always work on Linux, so the line after is a safeguard.
        configuration.useVsync(true);
        //// Limits FPS to the refresh rate of the currently active monitor, plus 1 to try to match fractional
        //// refresh rates. The Vsync setting above should limit the actual FPS to match the monitor.
        configuration.setForegroundFPS(Lwjgl3ApplicationConfiguration.getDisplayMode().refreshRate + 1);
        //// If you remove the above line and set Vsync to false, you can get unlimited FPS, which can be
        //// useful for testing performance, but can also be very stressful to some hardware.
        //// You may also need to configure GPU drivers to fully disable Vsync; this can cause screen tearing.
        //configuration.setHdpiMode(HdpiMode.Logical);

        if (Lwjgl3ApplicationConfiguration.getDisplayMode().width <= 1920 || Lwjgl3ApplicationConfiguration.getDisplayMode().width >= 1280) { //1080p - 720p
            configuration.setWindowedMode(1280, 720);
        }
        else if (Lwjgl3ApplicationConfiguration.getDisplayMode().width <= 2560) { //1440p ++
            configuration.setWindowedMode(1920, 1080);
        }
        else {
            configuration.setWindowedMode(800, 600); //fallback
        }

        configuration.setResizable(true);
        configuration.setWindowSizeLimits(1280, 720, 7680, 4320);

        //configuration.setFullscreenMode(Lwjgl3ApplicationConfiguration.getDisplayMode());
        //// You can change these files; they are in lwjgl3/src/main/resources/ .
        //// They can also be loaded from the root of assets/ .
        configuration.setWindowIcon("libgdx128.png", "libgdx64.png", "libgdx32.png", "libgdx16.png");
        return configuration;
    }
}

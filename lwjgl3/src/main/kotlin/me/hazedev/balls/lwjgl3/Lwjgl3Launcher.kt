package me.hazedev.balls.lwjgl3

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration
import me.hazedev.balls.Assets
import me.hazedev.balls.BallRotation

fun main() {
    createApplication()
}

fun createApplication() = Lwjgl3Application(BallRotation(), getDefaultConfiguration())

fun getDefaultConfiguration() : Lwjgl3ApplicationConfiguration {
    val configuration = Lwjgl3ApplicationConfiguration()
    configuration.setTitle("BallRotation")
    configuration.setFullscreenMode(Lwjgl3ApplicationConfiguration.getDisplayMode())
    configuration.setWindowIcon(Assets.Paths.BALL)
    return configuration
}


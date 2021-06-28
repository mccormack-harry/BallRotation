package me.hazedev.balls

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.Interpolation
import de.eskalon.commons.core.ManagedGame
import de.eskalon.commons.screen.ManagedScreen
import de.eskalon.commons.screen.transition.ScreenTransition
import de.eskalon.commons.screen.transition.impl.BlendingTransition
import me.hazedev.balls.input.GameInput
import me.hazedev.balls.screen.GameScreen
import me.hazedev.balls.screen.MainMenuScreen
import me.hazedev.balls.screen.SplashScreen
import me.hazedev.balls.screen.transition.SimpleZoomTransition

class BallRotation : ManagedGame<ManagedScreen, ScreenTransition>() {

    lateinit var batch: SpriteBatch
    val assets by lazy { Assets() }

    override fun create() {
        super.create()
        batch = SpriteBatch()
        super.inputProcessor.addProcessor(GameInput(this))
        assets.loadAll()

        setupScreens()
        setupTransitions()
        screenManager.pushScreen(SplashScreen::class.qualifiedName, BlendingTransition::class.qualifiedName)
    }

    private fun setupScreens() {
        screenManager.addScreen(SplashScreen::class.qualifiedName, SplashScreen())
        screenManager.addScreen(MainMenuScreen::class.qualifiedName, MainMenuScreen())
        screenManager.addScreen(GameScreen::class.qualifiedName, GameScreen())
    }

    private fun setupTransitions() {
        screenManager.addScreenTransition(BlendingTransition::class.qualifiedName, BlendingTransition(batch, 1f, Interpolation.smooth))
        screenManager.addScreenTransition(SimpleZoomTransition::class.qualifiedName, SimpleZoomTransition(1f, Interpolation.smooth))
    }

    fun toggleFullscreen() {
        if (Gdx.graphics.isFullscreen) {
            Gdx.graphics.setWindowedMode(Gdx.graphics.width / 2, Gdx.graphics.height / 2)
        } else {
            Gdx.graphics.setFullscreenMode(Gdx.graphics.displayMode)
        }
    }

    fun exit() {
        Gdx.app.exit()
    }

}
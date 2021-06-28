package me.hazedev.balls.screen

import com.badlogic.gdx.Gdx
import de.eskalon.commons.screen.ManagedScreen
import me.hazedev.balls.BallRotation

open class ManagedScreenAdapter : ManagedScreen() {

    val game: BallRotation = Gdx.app.applicationListener as BallRotation

    override fun create() {
    }

    override fun render(delta: Float) {
    }

    override fun resize(width: Int, height: Int) {
    }

    override fun hide() {
    }

    override fun dispose() {
    }

}
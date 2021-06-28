package me.hazedev.balls.input

import com.badlogic.gdx.Input
import com.badlogic.gdx.InputAdapter
import me.hazedev.balls.BallRotation

class GameInput(val game: BallRotation) : InputAdapter() {

    override fun keyDown(keycode: Int): Boolean {
        when (keycode) {
            Input.Keys.F11 -> game.toggleFullscreen()
            Input.Keys.ESCAPE -> game.exit()
            else -> return false
        }
        return true
    }

}
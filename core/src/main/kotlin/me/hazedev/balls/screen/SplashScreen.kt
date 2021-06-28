package me.hazedev.balls.screen

import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.utils.viewport.ScreenViewport
import com.badlogic.gdx.utils.viewport.Viewport
import me.hazedev.balls.Assets
import me.hazedev.balls.screen.transition.SimpleZoomTransition
import kotlin.math.min

class SplashScreen : ManagedScreenAdapter() {

    lateinit var camera : OrthographicCamera
    lateinit var viewport : Viewport

    lateinit var ball: Sprite

    override fun create() {
        camera = OrthographicCamera()
        viewport = ScreenViewport(camera)
        ball = Sprite(game.assets.finishLoadingAsset<Texture>(Assets.BALL))
    }

    override fun render(delta: Float) {
        if (game.assets.update(17)) {
            if (!game.screenManager.inTransition())
                game.screenManager.pushScreen(
                    MainMenuScreen::class.qualifiedName,
                    SimpleZoomTransition::class.qualifiedName
                )
        }

        ball.rotate(90 * delta)

        viewport.apply()
        game.batch.projectionMatrix = camera.combined
        game.batch.begin()
        ball.draw(game.batch)
        game.batch.end()
    }

    override fun resize(width: Int, height: Int) {
        viewport.update(width, height)
        ball.setScale(min(width / ball.width, height / ball.height) * 0.25f)
    }

    override fun hide() {

    }

    override fun dispose() {

    }

}
package me.hazedev.balls.screen

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.utils.viewport.ScreenViewport
import com.badlogic.gdx.utils.viewport.Viewport
import de.eskalon.commons.screen.transition.impl.BlendingTransition
import me.hazedev.balls.Assets
import me.hazedev.balls.Level
import kotlin.math.min

class MainMenuScreen : ManagedScreenAdapter() {

    lateinit var camera : OrthographicCamera
    lateinit var viewport : Viewport
    lateinit var stage: Stage

    lateinit var title: Label
    lateinit var subtitle: Label

    var pushed = false

    override fun create() {
        camera = OrthographicCamera()
        viewport = ScreenViewport(camera)
        stage = Stage(viewport, game.batch)
        setupStage()
    }

    private fun setupStage() {
//        stage.isDebugAll = true

        val skin = game.assets.get(Assets.SKIN)

        val table = Table(skin)
        table.setFillParent(true)

        title = Label("Ball Rotation", Label.LabelStyle(game.assets.get(Assets.FONT_MEDIUM), Color.WHITE))
        subtitle = Label("Press any key to start!", Label.LabelStyle(game.assets.get(Assets.FONT_REGULAR), Color.LIGHT_GRAY))

        table.add(title).row()
        table.add(subtitle)
        stage.addActor(table)
    }

    override fun render(delta: Float) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.ANY_KEY)) {
            game.screenManager.pushScreen(GameScreen::class.qualifiedName, BlendingTransition::class.qualifiedName, Level.INTRODUCTION)
            pushed = true
        }
        stage.act(delta)
        stage.viewport.apply()
        stage.draw()
    }

    override fun resize(width: Int, height: Int) {
        viewport.update(width, height, true)
        val scale = min(width / 1920f, height / 1080f)
        println(scale)
        title.setFontScale(2 * scale)
        subtitle.setFontScale(scale)
    }

    override fun hide() {
        pushed = false
    }

}
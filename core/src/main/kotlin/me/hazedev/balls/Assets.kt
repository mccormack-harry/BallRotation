package me.hazedev.balls

import com.badlogic.gdx.assets.AssetDescriptor
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGeneratorLoader
import com.badlogic.gdx.graphics.g2d.freetype.FreetypeFontLoader
import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.maps.tiled.TmxMapLoader
import com.badlogic.gdx.scenes.scene2d.ui.Skin

class Assets : AssetManager() {

    object Paths {
        const val TEXTURE_DIR = "texture/"
        const val BALL = TEXTURE_DIR + "ball.png"
        const val ENEMY = TEXTURE_DIR + "enemy.png"
        const val WALL = TEXTURE_DIR + "wall.png"
        const val GOAL = TEXTURE_DIR + "goal.png"

        const val SKIN = "ui/" + "uiskin.json"

        const val FONT_DIR = "font/"
        const val FONT_LIGHT = FONT_DIR + "Quicksand-Light.ttf"
        const val FONT_REGULAR = FONT_DIR + "Quicksand-Regular.ttf"
        const val FONT_MEDIUM = FONT_DIR + "Quicksand-Medium.ttf"
        const val FONT_SEMI_BOLD = FONT_DIR + "Quicksand-SemiBold.ttf"
        const val FONT_BOLD = FONT_DIR + "Quicksand-Bold.ttf"
    }

    companion object {
        val BALL = AssetDescriptor(Paths.BALL, Texture::class.java)
        val ENEMY = AssetDescriptor(Paths.ENEMY, Texture::class.java)
        val WALL = AssetDescriptor(Paths.WALL, Texture::class.java)
        val GOAL = AssetDescriptor(Paths.GOAL, Texture::class.java)
        val SKIN = AssetDescriptor(Paths.SKIN, Skin::class.java)
        private val fontParameter = FreetypeFontLoader.FreeTypeFontLoaderParameter().apply { fontParameters.size = 64 }
        val FONT_LIGHT = AssetDescriptor(Paths.FONT_LIGHT, BitmapFont::class.java, fontParameter.apply { fontFileName = Paths.FONT_LIGHT })
        val FONT_REGULAR = AssetDescriptor(Paths.FONT_REGULAR, BitmapFont::class.java, fontParameter.apply { fontFileName = Paths.FONT_REGULAR })
        val FONT_MEDIUM = AssetDescriptor(Paths.FONT_MEDIUM, BitmapFont::class.java, fontParameter.apply { fontFileName = Paths.FONT_MEDIUM })
        val FONT_SEMI_BOLD = AssetDescriptor(Paths.FONT_SEMI_BOLD, BitmapFont::class.java, fontParameter.apply { fontFileName = Paths.FONT_SEMI_BOLD })
        val FONT_BOLD = AssetDescriptor(Paths.FONT_BOLD, BitmapFont::class.java, fontParameter.apply { fontFileName = Paths.FONT_BOLD })
    }

    init {
        setLoader(BitmapFont::class.java, ".ttf", FreetypeFontLoader(fileHandleResolver))
        setLoader(FreeTypeFontGenerator::class.java, FreeTypeFontGeneratorLoader(fileHandleResolver))
        setLoader(TiledMap::class.java, TmxMapLoader(fileHandleResolver))
    }

    fun loadAll() {
        load(SKIN)
        load(BALL)
        load(ENEMY)
        load(WALL)
        load(GOAL)
        load(FONT_LIGHT)
        load(FONT_REGULAR)
        load(FONT_MEDIUM)
        load(FONT_SEMI_BOLD)
        load(FONT_BOLD)
        for (level in Level.values()) {
            load(level.descriptor)
        }
    }

}
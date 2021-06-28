package me.hazedev.balls

import com.badlogic.gdx.assets.AssetDescriptor
import com.badlogic.gdx.maps.tiled.TiledMap

enum class Level(fileName: String) {

    INTRODUCTION("introduction.tmx");

    val displayName = name.lowercase().split('_').joinToString(" ") { it.replaceFirstChar(Char::uppercase) }
    val descriptor = AssetDescriptor("tiled/$fileName", TiledMap::class.java)

}
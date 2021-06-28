package me.hazedev.balls.screen

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.maps.objects.EllipseMapObject
import com.badlogic.gdx.maps.objects.RectangleMapObject
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.*
import com.badlogic.gdx.utils.Array
import com.badlogic.gdx.utils.viewport.FitViewport
import com.badlogic.gdx.utils.viewport.Viewport
import de.eskalon.commons.screen.transition.impl.BlendingTransition
import me.hazedev.balls.Assets
import me.hazedev.balls.Level
import kotlin.math.pow
import kotlin.math.sqrt

class GameScreen : ManagedScreenAdapter() {

    lateinit var debugRenderer: Box2DDebugRenderer

    lateinit var camera: OrthographicCamera
    lateinit var viewport: Viewport
    lateinit var world: World
    lateinit var level: Level
    lateinit var gravity: Vector2
    lateinit var bodies: Array<Body>
    lateinit var reachedGoal: Array<Body>
    lateinit var died: Array<Body>
    var players = 0
    var rotation = 0f
    var paused = false

    override fun create() {
        gravity = Vector2()
        bodies = Array()
        reachedGoal = Array()
        died = Array()
        debugRenderer = Box2DDebugRenderer()
    }

    override fun show() {
        camera = OrthographicCamera()
        viewport = FitViewport(0f, 0f, camera)
        initializeScreen()

        level = pushParams!![0] as Level
        // Interpret Level
        val map = game.assets.get(level.descriptor)
        val mapWidth = map.properties.get("width") as Int
        val mapHeight = map.properties.get("height") as Int
        val tileWidth = map.properties.get("tilewidth") as Int
        val tileHeight = map.properties.get("tileheight") as Int
        val width = mapWidth * tileWidth
        val height = mapHeight * tileHeight
        val worldSize = sqrt(width.toDouble().pow(2) + height.toDouble().pow(2)).toFloat()
        camera.position.set(width / 2f, height / 2f, 0f)
        viewport.setWorldSize(worldSize, worldSize)

        world = World(Vector2(), false).apply { setContactListener(ContactListener()) }
        for (mapLayer in map.layers) {
            when (mapLayer.name) {
                "walls" -> {
                    for (mapObject in mapLayer.objects) {
                        if (mapObject.isVisible && mapObject is RectangleMapObject) {
                            val shape = PolygonShape().apply {
                                val halfX = mapObject.rectangle.width / 2
                                val halfY = mapObject.rectangle.height / 2
                                setAsBox(halfX, halfY, Vector2(halfX, halfY), 0f)
                            }
                            val body = world.createBody(
                                BodyDef().apply {
                                    position.set(mapObject.rectangle.x, mapObject.rectangle.y)
                                }
                            ).apply {
                                createFixture(FixtureDef().apply {
                                    this.shape = shape
                                    this.friction = 0.99f
                                }).apply { userData = EntityType.WALL }
                            }
                            shape.dispose()
                            body.userData = Sprite(game.assets.get(Assets.WALL)).apply {
                                setSize(
                                    mapObject.rectangle.width,
                                    mapObject.rectangle.height
                                )
                            }
                        }
                    }
                }
                "players" -> {
                    for (mapObject in mapLayer.objects) {
                        if (mapObject.isVisible && mapObject is EllipseMapObject) {
                            val shape = CircleShape().apply {
                                this.radius = (mapObject.ellipse.width + mapObject.ellipse.height) / 4
                            }
                            val body = world.createBody(
                                BodyDef().apply {
                                    type = BodyDef.BodyType.DynamicBody
                                    position.set(
                                        mapObject.ellipse.x + shape.radius,
                                        mapObject.ellipse.y + shape.radius
                                    )
                                    linearDamping = 0.1f
                                    angularDamping = 0.1f
                                }).apply {
                                createFixture(FixtureDef().apply {
                                    this.shape = shape
                                    this.density = 10f
                                    this.friction = 0.99f
                                    this.restitution = 0.2f
                                }).apply { userData = EntityType.PLAYER }
                            }
                            shape.dispose()
                            body.userData = Sprite(game.assets.get(Assets.BALL)).apply {
                                val size = shape.radius * 2
                                setSize(size, size)
                                setOriginCenter()
                            }
                            players++
                        }
                    }
                }
                "enemies" -> {
                    for (mapObject in mapLayer.objects) {
                        if (mapObject.isVisible && mapObject is EllipseMapObject) {
                            val shape = CircleShape().apply {
                                this.radius = (mapObject.ellipse.width + mapObject.ellipse.height) / 4
                            }
                            val body = world.createBody(
                                BodyDef().apply {
                                    position.set(
                                        mapObject.ellipse.x + shape.radius,
                                        mapObject.ellipse.y + shape.radius
                                    )
                                }).apply {
                                createFixture(FixtureDef().apply {
                                    this.shape = shape
                                }).apply { userData = EntityType.ENEMY }
                            }
                            shape.dispose()
                            body.userData = Sprite(game.assets.get(Assets.ENEMY)).apply {
                                val size = shape.radius * 2
                                setSize(size, size)
                                setOriginCenter()
                            }
                        }
                    }
                }
                "goals" -> {
                    for (mapObject in mapLayer.objects) {
                        if (mapObject.isVisible && mapObject is EllipseMapObject) {
                            val shape = CircleShape().apply {
                                this.radius = (mapObject.ellipse.width + mapObject.ellipse.height) / 4
                            }
                            val body = world.createBody(
                                BodyDef().apply {
                                    position.set(
                                        mapObject.ellipse.x + shape.radius,
                                        mapObject.ellipse.y + shape.radius
                                    )
                                }).apply {
                                createFixture(FixtureDef().apply {
                                    this.shape = shape
                                }).apply { userData = EntityType.GOAL }
                            }
                            shape.dispose()
                            body.userData = Sprite(game.assets.get(Assets.GOAL)).apply {
                                val size = shape.radius * 2
                                setSize(size, size)
                                setOriginCenter()
                            }
                        }
                    }
                }
            }
        }
        resize(Gdx.graphics.width, Gdx.graphics.height)
    }

    private fun update(delta: Float) {
        if (reachedGoal.size > 0) {
            players -= reachedGoal.size
            reachedGoal.forEach(world::destroyBody)
            reachedGoal.clear()
        }
        if (died.size > 0) {
            players -= died.size
            died.forEach(world::destroyBody)
            died.clear()
        }
        if (players <= 0) {
            if (!game.screenManager.inTransition()) {
                game.screenManager.pushScreen(MainMenuScreen::class.qualifiedName, BlendingTransition::class.qualifiedName)
            }
        }
        world.gravity = gravity.set(0f, -30f).rotateDeg(rotation)
        world.step(delta, 6, 2)
        world.getBodies(bodies)
        for (body in bodies) {
            val sprite = body.userData
            if (sprite is Sprite) {
                sprite.rotation = body.angle * MathUtils.radDeg
                val position = body.position
                sprite.setOriginBasedPosition(position.x, position.y)
            }
        }
        val left = Gdx.input.isKeyPressed(Input.Keys.LEFT)
        val right = Gdx.input.isKeyPressed(Input.Keys.RIGHT)
        if (left && !right || !left && right) {
            val rotation = delta * (if (left) 1 else -1) * (if (Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT)) 50f else 30f)
            this.rotation -= rotation
            camera.rotate(rotation)
        }
    }

    override fun render(delta: Float) {
        if (!paused) update(delta)

        viewport.apply()
        game.batch.projectionMatrix = camera.combined
//        debugRenderer.render(world, camera.combined)
        game.batch.begin()
        for (body in bodies) {
            val sprite = body.userData
            if (sprite is Sprite) {
                sprite.draw(game.batch)
            }
        }
        game.batch.end()
    }

    override fun hide() {
        players = 0
        rotation = 0f
        world.dispose()
        bodies.clear()
    }

    override fun resize(width: Int, height: Int) {
        viewport.update(width, height)
    }

    override fun pause() {
        paused = true
    }

    override fun resume() {
        paused = false
    }

    inner class ContactListener: com.badlogic.gdx.physics.box2d.ContactListener {
        override fun beginContact(contact: Contact) {
            val player: Fixture
            val other: Fixture
            when {
                contact.fixtureA.userData == EntityType.PLAYER -> {
                    player = contact.fixtureA
                    other = contact.fixtureB
                }
                contact.fixtureB.userData == EntityType.PLAYER -> {
                    player = contact.fixtureB
                    other = contact.fixtureA
                }
                else -> return
            }
            if (other.userData == EntityType.ENEMY) {
                died.add(player.body)
            } else if (other.userData == EntityType.GOAL) {
                reachedGoal.add(player.body)
            }
        }

        override fun endContact(contact: Contact) {}

        override fun preSolve(contact: Contact, oldManifold: Manifold) {}

        override fun postSolve(contact: Contact, impulse: ContactImpulse) {}

    }

    enum class EntityType {
        PLAYER,ENEMY,GOAL,WALL;
    }

}
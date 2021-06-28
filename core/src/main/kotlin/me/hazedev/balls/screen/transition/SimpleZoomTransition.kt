package me.hazedev.balls.screen.transition

import com.badlogic.gdx.math.Interpolation
import de.eskalon.commons.screen.transition.impl.GLTransitionsShaderTransition

class SimpleZoomTransition(duration: Float, interpolation: Interpolation? = null) : GLTransitionsShaderTransition(duration, interpolation) {

    init {
        compileGLTransition("// Author: 0gust1\n" +
                "// License: MIT\n" +
                "\n" +
                "float zoom_quickness = 0.8;\n" +
                "float nQuick = clamp(zoom_quickness,0.2,1.0);\n" +
                "\n" +
                "vec2 zoom(vec2 uv, float amount) {\n" +
                "  return 0.5 + ((uv - 0.5) * (1.0-amount));\t\n" +
                "}\n" +
                "\n" +
                "vec4 transition (vec2 uv) {\n" +
                "  return mix(\n" +
                "    getFromColor(zoom(uv, smoothstep(0.0, nQuick, progress))),\n" +
                "    getToColor(uv),\n" +
                "   smoothstep(nQuick-0.2, 1.0, progress)\n" +
                "  );\n" +
                "}")
    }

}
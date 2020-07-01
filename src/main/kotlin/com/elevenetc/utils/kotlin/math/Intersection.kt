package com.elevenetc.utils.kotlin.math

class Intersection {
    var point: Point? = null
    var edge: Edge? = null
    var side: Edge.Side? = null
    var outColor = 0
    var hasOutColor = false
    fun exists(): Boolean {
        return point != null
    }
}
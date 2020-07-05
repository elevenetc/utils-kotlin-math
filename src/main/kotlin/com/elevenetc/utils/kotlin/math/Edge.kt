package com.elevenetc.utils.kotlin.math

import com.elevenetc.utils.kotlin.math.Vector.Direction.*


class Edge : Vector() {

    private val left = Side()
    private val right = Side()

    override fun set(x1: Float, y1: Float, x2: Float, y2: Float) {
        super.set(x1, y1, x2, y2)
        //Recalculate normals
        if (left.hasNormal()) left.normal = leftNormal
        if (right.hasNormal()) right.normal = rightNormal
    }

    fun leftSide(): Side {
        return left
    }

    fun rightSide(): Side {
        return right
    }

    fun setLeftNormal() {
        left.normal = leftNormal
    }

    fun setType(type: Side.Type?) {
        left.type = type
        right.type = type
    }

    fun setBehaviour(behaviour: Side.Behaviour?) {
        left.behaviour = behaviour
        right.behaviour = behaviour
    }

    fun setRightNormal() {
        right.normal = rightNormal
    }

    val rightNormal: Normal
        get() = getNormal(-50.0f)

    val leftNormal: Normal
        get() = getNormal(50.0f)

    private fun getNormal(lengthAndDirection: Float): Normal {
        val normal = Normal()
        val direction = direction()
        normal.x1 = (x1 + x2) / 2.0f
        normal.y1 = (y1 + y2) / 2.0f
        if (direction === N) {
            normal.x2 = normal.x1 - lengthAndDirection
            normal.y2 = normal.y1
        } else if (direction === E) {
            normal.x2 = normal.x1
            normal.y2 = normal.y1 - lengthAndDirection
        } else if (direction === S) {
            normal.x2 = normal.x1 + lengthAndDirection
            normal.y2 = normal.y1
        } else if (direction == W) {
            normal.x2 = normal.x1
            normal.y2 = normal.y1 + lengthAndDirection
        } else {
            val s = -1 / slope()
            val b: Float = -s * normal.x1 + normal.y1
            val normalSide = if (direction === NE) {
                normal.x1 - lengthAndDirection
            } else if (direction === SE) {
                normal.x1 + lengthAndDirection
            } else if (direction === SW) {
                normal.x1 + lengthAndDirection
            } else {
                normal.x1 - lengthAndDirection
            }
            normal.x2 = normalSide
            normal.y2 = s * normalSide + b
        }
        return normal
    }

    class Side {
        val isInside: Boolean
            get() = type == Type.IN

        val isOutside: Boolean
            get() = type == Type.OUT

        val isTransparent: Boolean
            get() = behaviour == Behaviour.TRANSPARENT

        var normal: Normal? = null
        var color = 0
        var type: Type? = null
        var behaviour: Behaviour? = null

        fun normal(): Normal? {
            return normal
        }

        fun hasNormal(): Boolean {
            return normal != null
        }

        fun color(): Int {
            return color
        }

        enum class Type {
            IN, OUT
        }

        enum class Behaviour {
            TRANSPARENT, OPAQUE
        }
    }
}
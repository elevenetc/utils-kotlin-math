package com.elevenetc.utils.kotlin.math

open class Segment {
    var name: String? = null
    var x1 = 0.0f
    var y1 = 0.0f
    var x2 = 0.0f
    var y2 = 0.0f

    constructor(x1: Float, y1: Float, x2: Float, y2: Float) {
        set(x1, y1, x2, y2)
    }

    constructor(start: RaySegment, x2: Float, y2: Float) {
        set(start.x1, start.y1, x2, y2)
    }

    constructor(start: Point, end: Point) {
        set(start.x, start.y, end.x, end.y)
    }

    constructor() {}

    fun translate(dx: Float, dy: Float) {
        x1 += dx
        x2 += dx
        y1 += dy
        y2 += dy
    }

    fun translateEnd(dx2: Float, dy2: Float) {
        x2 += dx2
        y2 += dy2
    }

    fun translateTo(intersection: Point) {
        val xDiff = intersection.x - x1
        val yDiff = intersection.y - y1
        translate(xDiff, yDiff)
    }

    open operator fun set(x1: Float, y1: Float, x2: Float, y2: Float) {
        this.x1 = x1
        this.y1 = y1
        this.x2 = x2
        this.y2 = y2
    }

    fun normalized(): Segment {
        val length = length()
        val origX = x2 - x1
        val origY = y2 - y1
        //TODO: cache inst
        return Segment(0.0f, 0.0f, origX / length, origY / length)
    }

    fun length(): Float {
        //TODO: cache/optimize
        val xSide = x2 - x1
        val ySide = y2 - y1
        return kotlin.math.sqrt(xSide * xSide + ySide * ySide)
    }

    fun slope(): Float {
        //TODO: cache/optimize
        return (y2 - y1) / (x2 - x1)
    }

    //TODO: cache/optimize
    val isHorizontal: Boolean
        get() =//TODO: cache/optimize
            y1 == y2

    //TODO: cache/optimize
    val isVertical: Boolean
        get() =//TODO: cache/optimize
            x1 == x2

    /**
     * @return direction in android Canvas coordinates:
     * N
     * NW(-1:-1) |  (1:-1)NE
     * |
     * W---------|------- E
     * |
     * SW(-1:1)  |  (1:1)SE
     * S
     */
    fun direction(): Direction {
        //TODO: cache/optimize
        return if (y1 == y2) {
            if (x1 < x2) {
                Direction.E
            } else {
                Direction.W
            }
        } else if (x1 == x2) {
            if (y1 < y2) {
                Direction.S
            } else {
                Direction.N
            }
        } else if (x1 < x2 && y1 < y2) {
            Direction.SE
        } else if (x1 < x2 && y1 > y2) {
            Direction.NE
        } else if (x1 > x2 && y1 > y2) {
            Direction.NW
        } else {
            Direction.SW
        }
    }

    enum class Direction {
        N, E, S, W, NE, SE, SW, NW
    }
}
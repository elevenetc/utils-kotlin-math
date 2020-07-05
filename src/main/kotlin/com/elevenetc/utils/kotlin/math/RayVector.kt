package com.elevenetc.utils.kotlin.math

class RayVector : Vector {

    var startAlpha = 0f
    var endAlpha = 0f
    var color = 0

    //Decay values
    var start = 0.0
    var end = 0.0

    constructor(vector: Vector) : this(vector.x1, vector.y1, vector.x2, vector.y2)
    constructor(x1: Float, y1: Float, x2: Float, y2: Float) : super(x1, y1, x2, y2)
    constructor(start: RayVector, x2: Float, y2: Float) : super(start.x1, start.y1, x2, y2)
    constructor(start: Point, end: Point) : super(start.x, start.y, end.x, end.y)

    constructor() {}

    override fun toString(): String {
        return "RaySegment{" +
                "x1=" + x1 +
                ", y1=" + y1 +
                ", x2=" + x2 +
                ", y2=" + y2 +
                '}'
    }

    fun copy(): RayVector {
        val copy = RayVector(x1, y1, x2, y2)
        copy.color = color
        copy.start = start
        copy.end = end
        copy.startAlpha = startAlpha
        copy.endAlpha = endAlpha
        return copy
    }
}
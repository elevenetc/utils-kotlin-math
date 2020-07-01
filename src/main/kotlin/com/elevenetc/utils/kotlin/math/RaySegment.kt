package com.elevenetc.utils.kotlin.math

class RaySegment : Segment {
    var startAlpha = 0f
    var endAlpha = 0f
    var color = 0

    //Decay values
    var start = 0.0
    var end = 0.0

    constructor(segment: Segment) : this(segment.x1, segment.y1, segment.x2, segment.y2)
    constructor(x1: Float, y1: Float, x2: Float, y2: Float) : super(x1, y1, x2, y2)
    constructor(start: RaySegment, x2: Float, y2: Float) : super(start.x1, start.y1, x2, y2)
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

    fun copy(): RaySegment {
        val copy = RaySegment(x1, y1, x2, y2)
        copy.color = color
        copy.start = start
        copy.end = end
        copy.startAlpha = startAlpha
        copy.endAlpha = endAlpha
        return copy
    }
}
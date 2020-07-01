package com.elevenetc.utils.kotlin.math

interface ElevenMath {
    fun rotate(
        segment: Segment?,
        cx: Float,
        cy: Float,
        degrees: Float
    )

    fun rotate(segment: Segment?, degrees: Float)
    fun dotProduct(
        a: Segment?,
        b: Segment?
    ): Float

    fun dotProduct(
        x1: Float, y1: Float, x2: Float, y2: Float,
        x3: Float, y3: Float, x4: Float, y4: Float
    ): Float

    fun angleBetween(
        a: Segment?,
        b: Segment?
    ): Float

    fun angleBetween(
        ax1: Float, ay1: Float, ax2: Float, ay2: Float,
        bx1: Float, by1: Float, bx2: Float, by2: Float
    ): Float

    fun isReflectedByNormalAndIntersection(
        ray: Segment?,
        edge: Edge?
    ): Intersection?

    fun getIntersectionByNormal(ray: Segment?, edge: Edge?): Intersection?
    fun distance(x1: Float, y1: Float, x2: Float, y2: Float): Float

    fun getClosestIntersection(ray: RaySegment?, scene: Scene?): Intersection?
    fun getIntersection(a: RaySegment?, b: Edge?): Point?
    fun getIntersection(
        x1: Float, y1: Float, x2: Float, y2: Float,
        b: RaySegment?
    ): Point

    fun getIntersectionPointWithEndsCheck(
        a: Segment?,
        b: Segment?
    ): Point?

    fun getIntersection(
        x1: Float, y1: Float, x2: Float, y2: Float,
        x3: Float, y3: Float, x4: Float, y4: Float
    ): Point

    fun hasIntersection(
        x1: Float, y1: Float, x2: Float, y2: Float,
        x3: Float, y3: Float, x4: Float, y4: Float
    ): Boolean

    companion object {
        fun create(): ElevenMath {
            return ElevenMathImpl()
        }
    }
}
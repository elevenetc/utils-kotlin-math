package com.elevenets.utils.kotlin.math

interface Math {
    fun rotate(
        segment: Segment?,
        cx: Double,
        cy: Double,
        degrees: Double
    )

    fun rotate(segment: Segment?, degrees: Double)
    fun dotProduct(
        a: Segment?,
        b: Segment?
    ): Double

    fun dotProduct(
        x1: Double, y1: Double, x2: Double, y2: Double,
        x3: Double, y3: Double, x4: Double, y4: Double
    ): Double

    fun angleBetween(
        a: Segment?,
        b: Segment?
    ): Double

    fun angleBetween(
        ax1: Double, ay1: Double, ax2: Double, ay2: Double,
        bx1: Double, by1: Double, bx2: Double, by2: Double
    ): Double

    fun isReflectedByNormalAndIntersection(
        ray: Segment?,
        edge: Edge?
    ): Intersection?

    fun getIntersectionByNormal(ray: Segment?, edge: Edge?): Intersection?
    fun distance(x1: Double, y1: Double, x2: Double, y2: Double): Double

    fun getClosestIntersection(ray: RaySegment?, scene: Scene?): Intersection?
    fun getIntersection(a: RaySegment?, b: Edge?): Point?
    fun getIntersection(
        x1: Double, y1: Double, x2: Double, y2: Double,
        b: RaySegment?
    ): Point

    fun getIntersectionPointWithEndsCheck(
        a: Segment?,
        b: Segment?
    ): Point?

    fun getIntersection(
        x1: Double, y1: Double, x2: Double, y2: Double,
        x3: Double, y3: Double, x4: Double, y4: Double
    ): Point

    fun hasIntersection(
        x1: Double, y1: Double, x2: Double, y2: Double,
        x3: Double, y3: Double, x4: Double, y4: Double
    ): Boolean

    companion object {
        fun create(): Math {
            return MathImpl()
        }
    }
}
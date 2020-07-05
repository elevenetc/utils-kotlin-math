package com.elevenetc.utils.kotlin.math

interface ElevenMath {
    fun rotate(
        vector: Vector?,
        cx: Float,
        cy: Float,
        degrees: Float
    )

    fun rotate(vector: Vector?, degrees: Float)
    fun dotProduct(
        a: Vector?,
        b: Vector?
    ): Float

    fun dotProduct(
        x1: Float, y1: Float, x2: Float, y2: Float,
        x3: Float, y3: Float, x4: Float, y4: Float
    ): Float

    fun angleBetween(
        a: Vector?,
        b: Vector?
    ): Float

    fun angleBetween(
        ax1: Float, ay1: Float, ax2: Float, ay2: Float,
        bx1: Float, by1: Float, bx2: Float, by2: Float
    ): Float

    fun isReflectedByNormalAndIntersection(
        ray: Vector?,
        edge: Edge?
    ): Intersection?

    fun getIntersectionByNormal(ray: Vector?, edge: Edge?): Intersection?
    fun distance(x1: Float, y1: Float, x2: Float, y2: Float): Float

    fun getClosestIntersection(ray: RayVector?, scene: Scene?): Intersection?
    fun getIntersection(a: RayVector?, b: Edge?): Point?
    fun getIntersection(
        x1: Float, y1: Float, x2: Float, y2: Float,
        b: RayVector?
    ): Point

    fun getIntersectionPointWithEndsCheck(
        a: Vector?,
        b: Vector?
    ): Point?

    fun getIntersection(
        x1: Float, y1: Float, x2: Float, y2: Float,
        x3: Float, y3: Float, x4: Float, y4: Float
    ): Point

    fun hasIntersection(
        x1: Float, y1: Float, x2: Float, y2: Float,
        x3: Float, y3: Float, x4: Float, y4: Float
    ): Boolean

    fun toRadians(angle: Float): Float

    /**
     * Rotates point around circle with radius 1 with given [angle]
     * [point] size must be >= 2. First 2 values are filled with x and y respectively
     *
     *         y
     *         1
     *         |
     *         |
     * -1 ------------1 x
     *         |
     *         |
     *        -1
     *
     */
    fun rotatePoint(angle: Float, point: Array<Float>)

    fun round(value: Float, precision: Int): Float

    fun round(array: Array<Float>, precision: Int)

    companion object {
        fun create(): ElevenMath {
            return ElevenMathImpl()
        }
    }
}
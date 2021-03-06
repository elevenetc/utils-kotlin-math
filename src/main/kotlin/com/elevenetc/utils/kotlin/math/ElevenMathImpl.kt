package com.elevenetc.utils.kotlin.math

import java.lang.Math.PI
import java.lang.Math.toRadians
import java.util.*
import kotlin.math.*


internal class ElevenMathImpl : ElevenMath {

    private val FloatEqualityEps = 0.0001

    override fun rotate(
        vector: Vector?,
        cx: Float,
        cy: Float,
        degrees: Float
    ) {
        val rads = toRadians(degrees.toDouble())
        val cos = cos(rads).toFloat()
        val sin = sin(rads).toFloat()

        //move to origin
        vector!!.x1 -= cx
        vector!!.y1 -= cy
        vector!!.x2 -= cx
        vector!!.y2 -= cy

        //rotate
        val x1 = vector!!.x1 * cos - vector.y1 * sin
        val y1 = vector.x1 * sin + vector.y1 * cos
        val x2 = vector.x2 * cos - vector.y2 * sin
        val y2 = vector.x2 * sin + vector.y2 * cos

        //move back
        vector.x1 = x1 + cx
        vector.y1 = y1 + cy
        vector.x2 = x2 + cx
        vector.y2 = y2 + cy
    }

    override fun rotate(vector: Vector?, degrees: Float) {
        val rads = toRadians(degrees.toDouble()).toFloat()
        val cos = cos(rads)
        val sin = sin(rads)

        //move to origin
        vector!!.x2 -= vector!!.x1
        vector.y2 -= vector.y1

        //rotate
        val x = vector.x2 * cos - vector.y2 * sin
        val y = vector.x2 * sin + vector.y2 * cos

        //move back
        vector.x2 = x + vector.x1
        vector.y2 = y + vector.y1
    }

    override fun dotProduct(
        a: Vector?,
        b: Vector?
    ): Float {
        val aOrigin = toOrigin(a)
        val bOrigin = toOrigin(b)
        return aOrigin.x2 * bOrigin.x2 + aOrigin.y2 * bOrigin.y2
    }

    override fun dotProduct(
        x1: Float, y1: Float, x2: Float, y2: Float,
        x3: Float, y3: Float, x4: Float, y4: Float
    ): Float {
        //move to origin
        var x1 = x1
        var y1 = y1
        var x2 = x2
        var y2 = y2
        var x3 = x3
        var y3 = y3
        var x4 = x4
        var y4 = y4
        x2 = x2 - x1
        y2 = y2 - y1
        x1 = 0.0f
        y1 = 0.0f
        x4 = x4 - x3
        y4 = y4 - y3
        x3 = 0.0f
        y3 = 0.0f
        return x2 * x4 + y2 * y4
    }

    override fun angleBetween(
        a: Vector?,
        b: Vector?
    ): Float {
        return angleBetween(
            a!!.x1, a.y1, a.x2, a.y2,
            b!!.x1, b.y1, b.x2, b.y2
        )
    }

    override fun angleBetween(
        ax1: Float, ay1: Float, ax2: Float, ay2: Float,
        bx1: Float, by1: Float, bx2: Float, by2: Float
    ): Float {
        val angleA = atan2(ay1 - ay2, ax1 - ax2)
        val angleB = atan2(by1 - by2, bx1 - bx2)
        return ((angleA - angleB) * 180 / PI).toFloat()
    }

    override fun isReflectedByNormalAndIntersection(
        ray: Vector?,
        edge: Edge?
    ): Intersection? {
        val intersection = getIntersectionByNormal(ray, edge)
        if (intersection!!.edge == null) return intersection
        intersection.point = getIntersectionPointWithEndsCheck(ray, edge)
        return intersection
    }

    override fun getIntersectionByNormal(
        ray: Vector?,
        edge: Edge?
    ): Intersection? {

        //TODO: cache
        val result = Intersection()
        if (edge!!.leftSide().hasNormal()) {
            if (dotProduct(ray, edge.leftSide().normal()) < 0) {
                result.edge = edge
                result.side = edge.leftSide()
                if (edge.leftSide().isTransparent) {
                    result.hasOutColor = true
                    result.outColor = edge.leftSide().color()
                }
                return result
            }
        }
        if (edge.rightSide().hasNormal()) {
            if (dotProduct(ray, edge.rightSide().normal()) < 0) {
                result.edge = edge
                result.side = edge.rightSide()
                if (edge.rightSide().isTransparent) {
                    result.hasOutColor = true
                    result.outColor = edge.leftSide().color()
                }
                return result
            }
        }
        return result
    }

    override fun distance(
        x1: Float,
        y1: Float,
        x2: Float,
        y2: Float
    ): Float {
        var x1 = x1
        var y1 = y1
        x1 -= x2
        y1 -= y2
        return sqrt(x1 * x1 + y1 * y1)
    }

    override fun getClosestIntersection(ray: RayVector?, scene: Scene?): Intersection? {

        //TODO: use cached list
        val intersections: MutableList<Intersection?> = LinkedList()
        var intersection: Intersection? = null

        //get all intersection edges with array
        for (obj in scene!!.objects) for (edge in obj.edges) {
            val i = isReflectedByNormalAndIntersection(ray, edge)
            if (i!!.exists()) intersections.add(i)
        }
        if (intersections.isEmpty()) {
            return intersection
        } else if (intersections.size == 1) {
            return intersections[0]
        }
        var minDist = Float.MAX_VALUE

        //get all intersection points and take closest
        for (i in intersections) {
            val inter = getIntersection(ray, i!!.edge)
            val dist = distance(inter!!.x, inter.y, ray!!.x1, ray.y1)
            if (dist.compareTo(0) > 0 && dist.compareTo(minDist) < 0) {
                minDist = dist
                intersection = i
            }
        }
        return intersection
    }

    override fun getIntersection(
        a: RayVector?,
        b: Edge?
    ): Point? {
        val x1 = a!!.x1
        val y1 = a.y1
        val x2 = a.x2
        val y2 = a.y2
        val x3 = b!!.x1
        val y3 = b.y1
        val x4 = b.x2
        val y4 = b.y2
        return getIntersection(x1, y1, x2, y2, x3, y3, x4, y4)
    }

    override fun getIntersection(
        x1: Float, y1: Float, x2: Float, y2: Float,
        b: RayVector?
    ): Point {
        return getIntersection(x1, y1, x2, y2, b!!.x1, b.y1, b.x2, b.y2)
    }

    override fun getIntersectionPointWithEndsCheck(
        a: Vector?,
        b: Vector?
    ): Point? {
        val x1 = a!!.x1
        val y1 = a.y1
        val x2 = a.x2
        val y2 = a.y2
        val x3 = b!!.x1
        val y3 = b.y1
        val x4 = b.x2
        val y4 = b.y2
        var has = hasIntersection(x1, y1, x2, y2, x3, y3, x4, y4)
        var result: Point? = null
        if (has) {
            val intersection = getIntersection(x1, y1, x2, y2, x3, y3, x4, y4)
            if (pointAtEnds(intersection.x, intersection.y, x1, y1, x2, y2) || pointAtEnds(
                    intersection.x,
                    intersection.y,
                    x3,
                    y3,
                    x4,
                    y4
                )
            ) {
                has = false
            } else {
                result = intersection
            }
        }
        return result
    }

    override fun getIntersection(
        x1: Float, y1: Float, x2: Float, y2: Float,
        x3: Float, y3: Float, x4: Float, y4: Float
    ): Point {
        val d = (x1 - x2) * (y3 - y4) - (y1 - y2) * (x3 - x4)
        val x = ((x3 - x4) * (x1 * y2 - y1 * x2) - (x1 - x2) * (x3 * y4 - y3 * x4)) / d
        val y = ((y3 - y4) * (x1 * y2 - y1 * x2) - (y1 - y2) * (x3 * y4 - y3 * x4)) / d
        return Point(x, y)
    }

    override fun hasIntersection(
        x1: Float, y1: Float, x2: Float, y2: Float,
        x3: Float, y3: Float, x4: Float, y4: Float
    ): Boolean {
        return (relativeCCW(x1, y1, x2, y2, x3, y3) *
                relativeCCW(x1, y1, x2, y2, x4, y4) <= 0
                && relativeCCW(x3, y3, x4, y4, x1, y1) *
                relativeCCW(x3, y3, x4, y4, x2, y2) <= 0)
    }

    override fun toRadians(angle: Float): Float {
        return toRadians(angle.toDouble()).toFloat()
    }

    override fun rotatePoint(angle: Float, point: Array<Float>) {
        val r = 1f
        val rads = toRadians(angle)
        val x = r * cos(rads)
        val y = r * sin(rads)
        point[0] = x
        point[1] = y
    }

    override fun round(array: Array<Float>, precision: Int){
        array.forEachIndexed { index, value ->
            array[index] = round(value, precision)
        }
    }

    override fun round(value: Float, precision: Int): Float {
        val scale = 10.0.pow(precision.toDouble()).toInt()
        return ((value * scale).roundToInt().toDouble() / scale).toFloat()
    }

    private fun toOrigin(vector: Vector?): RayVector {
        val diffX = vector!!.x1
        val diffY = vector.y1

        //TODO: cache instance
        return RayVector(0.0f, 0.0f, vector.x2 - diffX, vector.y2 - diffY)
    }

    /**
     * From java.awt.geom.Java2D
     */
    private fun relativeCCW(
        x1: Float, y1: Float,
        x2: Float, y2: Float,
        px: Float, py: Float
    ): Int {
        var x2 = x2
        var y2 = y2
        var px = px
        var py = py
        x2 -= x1
        y2 -= y1
        px -= x1
        py -= y1
        var ccw = px * y2 - py * x2
        if (ccw == 0.0f) {
            ccw = px * x2 + py * y2
            if (ccw > 0.0) {
                px -= x2
                py -= y2
                ccw = px * x2 + py * y2
                if (ccw < 0.0f) {
                    ccw = 0.0f
                }
            }
        }
        return ccw.compareTo(0.0)
    }

    private fun pointAtEnds(
        px: Float,
        py: Float,
        x1: Float,
        y1: Float,
        x2: Float,
        y2: Float
    ): Boolean {
        return FloatEquals(px, x1) && FloatEquals(py, y1) || FloatEquals(px, x2) && FloatEquals(py, y2)
    }

    private fun FloatEquals(a: Float, b: Float): Boolean {
        val diff = kotlin.math.abs(a - b)
        return diff <= FloatEqualityEps
    }
}
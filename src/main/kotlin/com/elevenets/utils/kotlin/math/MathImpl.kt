package com.elevenets.utils.kotlin.math

import java.util.*
import kotlin.math.sqrt


internal class MathImpl : Math {

    private val doubleEqualityEps = 0.0001

    override fun rotate(
        segment: Segment?,
        cx: Double,
        cy: Double,
        degrees: Double
    ) {
        val rads = Math.toRadians(degrees)
        val cos = Math.cos(rads)
        val sin = Math.sin(rads)

        //move to origin
        segment!!.x1 -= cx
        segment!!.y1 -= cy
        segment!!.x2 -= cx
        segment!!.y2 -= cy

        //rotate
        val x1 = segment!!.x1 * cos - segment.y1 * sin
        val y1 = segment.x1 * sin + segment.y1 * cos
        val x2 = segment.x2 * cos - segment.y2 * sin
        val y2 = segment.x2 * sin + segment.y2 * cos

        //move back
        segment.x1 = x1 + cx
        segment.y1 = y1 + cy
        segment.x2 = x2 + cx
        segment.y2 = y2 + cy
    }

    override fun rotate(segment: Segment?, degrees: Double) {
        val rads = Math.toRadians(degrees)
        val cos = Math.cos(rads)
        val sin = Math.sin(rads)

        //move to origin
        segment!!.x2 -= segment!!.x1
        segment.y2 -= segment.y1

        //rotate
        val x = segment.x2 * cos - segment.y2 * sin
        val y = segment.x2 * sin + segment.y2 * cos

        //move back
        segment.x2 = x + segment.x1
        segment.y2 = y + segment.y1
    }

    override fun dotProduct(
        a: Segment?,
        b: Segment?
    ): Double {
        val aOrigin = toOrigin(a)
        val bOrigin = toOrigin(b)
        return aOrigin.x2 * bOrigin.x2 + aOrigin.y2 * bOrigin.y2
    }

    override fun dotProduct(
        x1: Double, y1: Double, x2: Double, y2: Double,
        x3: Double, y3: Double, x4: Double, y4: Double
    ): Double {
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
        x1 = 0.0
        y1 = 0.0
        x4 = x4 - x3
        y4 = y4 - y3
        x3 = 0.0
        y3 = 0.0
        return x2 * x4 + y2 * y4
    }

    override fun angleBetween(
        a: Segment?,
        b: Segment?
    ): Double {
        return angleBetween(
            a!!.x1, a.y1, a.x2, a.y2,
            b!!.x1, b.y1, b.x2, b.y2
        )
    }

    override fun angleBetween(
        ax1: Double, ay1: Double, ax2: Double, ay2: Double,
        bx1: Double, by1: Double, bx2: Double, by2: Double
    ): Double {
        val angleA = Math.atan2(ay1 - ay2, ax1 - ax2)
        val angleB = Math.atan2(by1 - by2, bx1 - bx2)
        return (angleB - angleA) * 180 / Math.PI
    }

    override fun isReflectedByNormalAndIntersection(
        ray: Segment?,
        edge: Edge?
    ): Intersection? {
        val intersection = getIntersectionByNormal(ray, edge)
        if (intersection!!.edge == null) return intersection
        intersection.point = getIntersectionPointWithEndsCheck(ray, edge)
        return intersection
    }

    override fun getIntersectionByNormal(
        ray: Segment?,
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
        x1: Double,
        y1: Double,
        x2: Double,
        y2: Double
    ): Double {
        var x1 = x1
        var y1 = y1
        x1 -= x2
        y1 -= y2
        return sqrt(x1 * x1 + y1 * y1)
    }

    override fun getClosestIntersection(ray: RaySegment?, scene: Scene?): Intersection? {

        //TODO: use cached list
        val intersections: MutableList<Intersection?> = LinkedList()
        var intersection:Intersection? = null

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
        var minDist = Double.MAX_VALUE

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
        a: RaySegment?,
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
        x1: Double, y1: Double, x2: Double, y2: Double,
        b: RaySegment?
    ): Point {
        return getIntersection(x1, y1, x2, y2, b!!.x1, b.y1, b.x2, b.y2)
    }

    override fun getIntersectionPointWithEndsCheck(
        a: Segment?,
        b: Segment?
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
        x1: Double, y1: Double, x2: Double, y2: Double,
        x3: Double, y3: Double, x4: Double, y4: Double
    ): Point {
        val d = (x1 - x2) * (y3 - y4) - (y1 - y2) * (x3 - x4)
        val x = ((x3 - x4) * (x1 * y2 - y1 * x2) - (x1 - x2) * (x3 * y4 - y3 * x4)) / d
        val y = ((y3 - y4) * (x1 * y2 - y1 * x2) - (y1 - y2) * (x3 * y4 - y3 * x4)) / d
        return Point(x, y)
    }

    override fun hasIntersection(
        x1: Double, y1: Double, x2: Double, y2: Double,
        x3: Double, y3: Double, x4: Double, y4: Double
    ): Boolean {
        return (relativeCCW(x1, y1, x2, y2, x3, y3) *
                relativeCCW(x1, y1, x2, y2, x4, y4) <= 0
                && relativeCCW(x3, y3, x4, y4, x1, y1) *
                relativeCCW(x3, y3, x4, y4, x2, y2) <= 0)
    }

    private fun toOrigin(segment: Segment?): RaySegment {
        val diffX = segment!!.x1
        val diffY = segment.y1

        //TODO: cache instance
        return RaySegment(0.0, 0.0, segment.x2 - diffX, segment.y2 - diffY)
    }

    /**
     * From java.awt.geom.Java2D
     */
    private fun relativeCCW(
        x1: Double, y1: Double,
        x2: Double, y2: Double,
        px: Double, py: Double
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
        if (ccw == 0.0) {
            ccw = px * x2 + py * y2
            if (ccw > 0.0) {
                px -= x2
                py -= y2
                ccw = px * x2 + py * y2
                if (ccw < 0.0) {
                    ccw = 0.0
                }
            }
        }
        return ccw.compareTo(0.0)
    }

    private fun pointAtEnds(
        px: Double,
        py: Double,
        x1: Double,
        y1: Double,
        x2: Double,
        y2: Double
    ): Boolean {
        return doubleEquals(px, x1) && doubleEquals(py, y1) || doubleEquals(px, x2) && doubleEquals(py, y2)
    }

    private fun doubleEquals(a: Double, b: Double): Boolean {
        val diff = Math.abs(a - b)
        return diff <= doubleEqualityEps
    }
}
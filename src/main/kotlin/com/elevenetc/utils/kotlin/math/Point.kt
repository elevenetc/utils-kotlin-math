package com.elevenetc.utils.kotlin.math

data class Point(val x: Float, val y: Float) {
    constructor(array: Array<Float>) : this(array[0], array[1])
}
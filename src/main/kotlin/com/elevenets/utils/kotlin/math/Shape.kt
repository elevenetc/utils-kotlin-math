package com.elevenets.utils.kotlin.math


class Shape(val edges: MutableList<Edge> = mutableListOf()) {

    fun translate(x: Double, y: Double) {
        for (edge in edges) {
            edge.translate(x, y)
        }
    }
}
package com.elevenetc.utils.kotlin.math


class Shape(val edges: MutableList<Edge> = mutableListOf()) {

    fun translate(x: Float, y: Float) {
        for (edge in edges) {
            edge.translate(x, y)
        }
    }
}
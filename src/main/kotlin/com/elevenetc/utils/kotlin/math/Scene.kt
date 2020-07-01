package com.elevenetc.utils.kotlin.math

import java.util.*


class Scene {

    val objects: MutableList<Shape> = LinkedList()

    fun add(shape: Shape) {
        objects.add(shape)
    }

    fun objects(): List<Shape> {
        return objects
    }
}
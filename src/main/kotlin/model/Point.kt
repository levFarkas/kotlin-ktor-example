package model

import org.jetbrains.exposed.sql.*

object Point : Table("point") {
    val pointId = integer("point_id")
    val x = float("x")
    val y = float("y")
}
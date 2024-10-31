package fr.gplassard.gol
package game

import game.CellState.{ALIVE, DEAD}

case class Point(x: Int, y: Int)
case class Bounds(maxX: Int, maxY: Int)

enum CellState {
  case ALIVE, DEAD
}

case class GameState(aliveCells: Set[Point], bounds: Bounds, frame: Int) {
  def cellState(p: Point): CellState = if (aliveCells.contains(p)) ALIVE else DEAD

  def neighbors(p: Point): Seq[Point] = Seq(
    (-1, -1),
    (-1, 0),
    (-1, 1),
    (0, -1),
    (0, 1),
    (1, -1),
    (1, 0),
    (1, 1),
  ).map((deltaX, deltaY) => p.copy(x = p.x + deltaX, y = p.y + deltaY))

  def nextCellState(p: Point): CellState = {
    val aliveNeighbors = neighbors(p).count(n => cellState(n) == ALIVE)
    aliveNeighbors match {
      case 3 => ALIVE
      case 2 if cellState(p) == ALIVE => ALIVE
      case _ => DEAD
    }
  }

  def nextState(): GameState = this.copy(
    aliveCells = (for {
      x <- 0 to this.bounds.maxX
      y <- 0 to this.bounds.maxY
      if nextCellState(Point(x, y)) == ALIVE
    } yield Point(x, y)).toSet,
    frame = frame + 1,
  )
}

object GameState {
  private def toad(startX: Int, startY: Int) = Seq(
    Point(startX + 0, startY + 2),
    Point(startX + 1, startY + 1),
    Point(startX + 1, startY + 2),
    Point(startX + 2, startY + 1),
    Point(startX + 2, startY + 2),
    Point(startX + 3, startY + 1),
  )

  private def glider(startX: Int, startY: Int) = Seq(
    Point(startX + 0, startY + 1),
    Point(startX + 1, startY + 2),
    Point(startX + 2, startY + 0),
    Point(startX + 2, startY + 1),
    Point(startX + 2, startY + 2),
  )

  val initial = GameState(
    aliveCells = Set(
      toad(1, 1),
      toad(6, 6),
      toad(11, 11),
      glider(10, 1)
    ).flatten,
    bounds = Bounds(100, 100),
    frame = 0,
  )
}

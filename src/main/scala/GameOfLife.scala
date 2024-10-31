package fr.gplassard.gol

import game.GameState

import scalafx.application.{JFXApp3, Platform}
import scalafx.beans.property.{IntegerProperty, ObjectProperty}
import scalafx.scene.{Node, Scene}
import scalafx.scene.paint.Color.{Black, LightGreen}
import scalafx.scene.shape.Rectangle
import scalafx.scene.text.Text

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

object GameOfLife extends JFXApp3 {

  def gameLoop(update: () => Unit): Unit =
    Future {
      update()
      Thread.sleep(100)
    }.flatMap(_ => Future(gameLoop(update)))

  def render(state: GameState): List[Node] =  state.aliveCells.map(c => new Rectangle {
    x = 10 * c.x
    y = 10 * c.y
    width = 10
    height = 10
    fill = Black
  }).toList :+ Text(560, 10, state.frame.toString)

  override def start(): Unit = {
    val state = ObjectProperty(GameState.initial)
    val frame = IntegerProperty(0)

    frame.onChange {
      state.update(state.value.nextState())
    }

    stage = new JFXApp3.PrimaryStage {
      title.value = "Game of Life"
      width = 600
      height = 600
      scene = new Scene {
        fill = LightGreen
        content = render(state.value)
        state.onChange(Platform.runLater {
          content = render(state.value)
        })
      }
    }
    gameLoop(() => frame.update(frame.value + 1))
  }
}

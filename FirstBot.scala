// Scalatron

sealed trait ServerToPlugin
case class Welcome(name: String, apocalypse: Int, round: Int) extends ServerToPlugin
case class React(generation: Int, name: String, time: Int, view: String, energy: Int, master: String, collision: Option[String]) extends ServerToPlugin
case class Goodbye(energy: Int) extends ServerToPlugin

sealed trait PluginToServer
case class Move(vertical: Int, horizontal: Int) extends PluginToServer
case class Spawn(vertical: Int, horizontal: Int, name: Option[String], energy: Int) extends PluginToServer
case class Set() extends PluginToServer
case class Explode(size: Int) extends PluginToServer // 2 < x < 10

sealed trait Debugging extends PluginToServer
case class Stay(text: String) extends Debugging // max 10 chars
case class Status(text: String) extends Debugging // max 20 chars
case class MarkCell(vertical: Int, horizontal: Int, color: String) extends Debugging
case class DrawLine(fromVert: Int, fromHorz: Int, toVert: Int, toHorz: Int, color: String) extends Debugging
case class Log(text: String) extends Debugging

/*
 The view of the field around you.
"?" cell whose content is occluded by a wall
"_" empty cell
"W" wall
"M" Bot (=master; yours, always in the center unless seen by a slave)
"m" Bot (=master; enemy, not you)
"S" Mini-bot (=slave, yours)
"s" Mini-bot (=slave; enemy's, not yours)
"P" Zugar (=good plant, food)
"p" Toxifera (=bad plant, poisonous)
"B" Fluppet (=good beast, food)
"b" Snorg (=bad beast, predator)
*/

object Bot {

  def respond(input: String): String = {

  }

}


class ControlFunctionFactory {
  def create = Bot().respond _
}

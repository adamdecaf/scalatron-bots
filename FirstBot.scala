// Scalatron

case class KeyValuePair(key: String, value: String)

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
sealed trait FieldOfView
case object EmptyCell extends FieldOfView
case object WallContent extends FieldOfView
case object Wall extends FieldOfView
case object YourMasterBot extends FieldOfView
case object EnemyMasterBot extends FieldOfView
case object YourSlaveBot extends FieldOfView
case object EnemySlaveBot extends FieldOfView
case object GoodPlant extends FieldOfView
case object BadPlant extends FieldOfView
case object GoodBeast extends FieldOfView
case object BadBeast extends FieldOfView

object Bot extends ParsingHelpers {

  def respond(input: String): String = {
    //"Log(text=" + buildReact(input) + ")"
    println(buildReact(input))
    ""
  }

}

trait ParsingHelpers {
  def buildReact(input: String): React =
    pullReact(input).get

  private def pullCommand(input: String, keyword: String): Option[String] = {
    if (input.contains(keyword)) {
      (0 to input.length).foreach {
        loc =>
          val actualLength = math.min(input.length, loc + keyword.length)
          val potentialKeyword = input.substring(loc, actualLength)
          println(potentialKeyword)
          if (potentialKeyword == keyword) {
            return Some(input.substring(loc).takeWhile(_ != ')'))
          }
      }
      None
    } else {
      None
    }
  }

  private def pullProperties(command: String): List[KeyValuePair] = {
    // Assume a string like:
    // prop=value,prop=value,...
    command.split(",").map {
      pair =>
        val split = pair.split("=")
        KeyValuePair(split(0), split(1))
    }.toList
  }

  private def pullProperty(commands: List[KeyValuePair], needle: String): Option[String] = {
    commands.foreach {
      command =>
        if (command.key.toLowerCase == needle.toLowerCase)
          return Some(command.value)
    }
    None
  }

  private def pullReact(input: String): Option[React] = {
    pullCommand(input, "React").map {
      command =>
        // Strip off "React(" and ")" from the command
        val trimmedCommand = command.drop(6).dropRight(1)
        val keyValuePairs = pullProperties(trimmedCommand)
        return Some(React(
          generation = pullProperty(keyValuePairs, "generation").getOrElse("-1").toInt,
          name = pullProperty(keyValuePairs, "name").getOrElse(""),
          time = pullProperty(keyValuePairs, "time").getOrElse("-1").toInt,
          view = pullProperty(keyValuePairs, "view").getOrElse(""),
          energy = pullProperty(keyValuePairs, "energy").getOrElse("-999999").toInt,
          master = "",
          collision = None
        ))
    }
  }

}

class ControlFunctionFactory {
  def create = Bot.respond _
}

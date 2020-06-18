package repository.rows

import play.api.libs.json.{Format, Json}

case class TaskRow(id: Long, name: String,description: Option[String])

object TaskRow {
  /**
    * Mapping to read/write a TaskRow out as a JSON value.
    */
  implicit val format: Format[TaskRow] = Json.format
}

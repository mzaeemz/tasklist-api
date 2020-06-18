package repository

import javax.inject.{Inject, Singleton}
import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile
import slick.lifted.ProvenShape
import repository.rows.TaskRow
import scala.concurrent.{ExecutionContext, Future}
@Singleton
class TaskRepository @Inject()(dbConfigProvider:
                               DatabaseConfigProvider)(
                                implicit ec: ExecutionContext
                              ) {
  private val dbConfig = dbConfigProvider.get[JdbcProfile]
  import dbConfig._
  import profile.api._
  private val table = TableQuery[TaskTable]
  def getAll: Future[Seq[TaskRow]] = db.run {
    table.result
  }
  def get(id: Long): Future[Option[TaskRow]] = db.run {
    table.filter(_.id === id).result.headOption
  }
  def add(taskRow: TaskRow): Future[Long] = db.run {
    table.returning(table.map(_.id)) += taskRow
  }
  def update(taskRow: TaskRow): Future[Int] = db.run {
    table.filter(_.id === taskRow.id).update(taskRow)
  }
  def delete(id: Long): Future[Int] = db.run {
    table.filter(_.id === id).delete
  }
  private class TaskTable(tag: Tag) extends Table[TaskRow](tag,
    "tasks") {
    def * : ProvenShape[TaskRow] =
      (id, name, desription) <> ((TaskRow.apply _).tupled,
        TaskRow.unapply)
    def id: Rep[Long] = column[Long]("id", O.PrimaryKey,
      O.AutoInc)
    def name: Rep[String] = column[String]("name", O.Length(100))
    def desription: Rep[Option[String]] =
      column[Option[String]]("description")
  }
}
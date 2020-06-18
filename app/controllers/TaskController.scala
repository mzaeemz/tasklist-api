package controllers
import repository._
import repository.rows.TaskRow
import javax.inject.Inject
import play.api.db.slick.DatabaseConfigProvider
import play.api.data.Form
import play.api.libs.json.Json
import play.api.mvc._
import play.db.NamedDatabase

import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success}

case class TaskFormInput(name: String, description: Option[String])

class TaskController @Inject()(@NamedDatabase("default") protected val dbConfigProvider: DatabaseConfigProvider) (cc: ControllerComponents) extends AbstractController(cc) {
  private val taskRepository: TaskRepository = new TaskRepository(dbConfigProvider)(this.defaultExecutionContext)
  private val form: Form[TaskFormInput] = {
    import play.api.data.Forms._
    Form(
      mapping(
        "name" -> text,
        "description" -> optional(text)
      )(TaskFormInput.apply)(TaskFormInput.unapply)
    )
  }

  def getAll: Action[AnyContent] = Action.async {
    taskRepository.getAll.map{
      posts => Ok(Json.toJson(posts))
    }(this.defaultExecutionContext)
  }
  def get(id: String): Action[AnyContent] = Action.async {
    taskRepository.get(id.toLong).map{
      post => Ok(Json.toJson(post))
    }(this.defaultExecutionContext)
  }
  def insert: Action[AnyContent] = Action.async { implicit request =>
    insertJsonPost()
  }
  def update(id: String): Action[AnyContent] = Action.async { implicit request =>
    updateJsonPost(id.toLong)
  }
  def delete(id: String): Action[AnyContent] = Action.async { implicit request =>
    taskRepository.delete(id.toLong).map{
      post => Ok(Json.toJson(post))
    }(this.defaultExecutionContext)
  }
  private def insertJsonPost[A]()(
    implicit request: Request[A]): Future[Result] = {
    def failure(badForm: Form[TaskFormInput]): Future[Result] = {
      Future.successful(BadRequest)
    }
    def success(input: TaskFormInput): Future[Result] = {
        val taskRow = TaskRow(0L,input.name,input.description)
        taskRepository.add(taskRow)
        Future.successful(Created(Json.toJson(taskRow)))
    }
    form.bindFromRequest().fold(failure,success)
  }
  private def updateJsonPost[A](id: Long)(
    implicit request: Request[A]): Future[Result] = {
    def failure(badForm: Form[TaskFormInput]): Future[Result] = {
      Future.successful(BadRequest)
    }

    def success(input: TaskFormInput): Future[Result] = {
      val taskRow = TaskRow(id,input.name,input.description)
      taskRepository.update(taskRow)
      Future.successful(Created(Json.toJson(taskRow)))
    }
    form.bindFromRequest().fold(failure,success)
  }
}

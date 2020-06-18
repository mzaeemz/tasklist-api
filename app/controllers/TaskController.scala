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

  def hello(name: String) = Action {
    Ok("Hello " + name + "!")
  }
  def getAll() = Action.async {
    taskRepository.getAll.map{
      posts => Ok(Json.toJson(posts))
    }(this.defaultExecutionContext)
  }
  def process: Action[AnyContent] = Action.async { implicit request =>
    processJsonPost()
  }
  private def processJsonPost[A]()(
    implicit request: Request[A]): Future[Result] = {
    def failure(badForm: Form[TaskFormInput]): Future[Result] = {
      Future.successful(Redirect(routes.TaskController.hello("input")).flashing("failure" -> s"BadForm!"))
    }

    def success(input: TaskFormInput): Future[Result] = {
        val taskRow = TaskRow(0L,input.name,input.description)
        taskRepository.add(taskRow)
        Future.successful(Created(Json.toJson(taskRow)))
    }
    form.bindFromRequest().fold(failure,success)
  }

}

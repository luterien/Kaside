package controllers

import play.api._
import play.api.mvc._
import views.html

class Files extends Controller {

  def upload = Action { implicit request =>
    Ok(html.upload())
  }

  def completeUpload = Action(parse.multipartFormData) { request =>
    request.body.file("exported_file").map { exported_file =>
      import java.io.File
      val filename = exported_file.filename
      exported_file.ref.moveTo(new File(s"C:\\Users\\asus\\AppData\\Local\\Temp\\$filename"))
      Ok("File uploaded")
    }.getOrElse {
      Redirect(routes.BookmarkApp.index).flashing(
      "error" -> "Missing file")
    }
  }

}

package controllers

import jp.t2v.lab.play2.auth.AuthElement
import play.api.mvc.Controller
import views.html
import models.{NormalUser, Administrator}

class Message extends Controller with AuthElement with AuthConfigImpl {
  def show(id: String) = StackAction(AuthorityKey -> NormalUser) { implicit request =>
    val user = loggedIn
    val title = s"message id: $id"
    Ok(html.message.index(title))
  }

  def index = StackAction(AuthorityKey -> Administrator) { implicit request =>
    val user = loggedIn
    val title = "all messages"
    Ok(html.message.index(title))
  }
}
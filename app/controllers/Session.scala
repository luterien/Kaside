package controllers

import jp.t2v.lab.play2.auth.LoginLogout
import models.{NormalUser, Account}
import play.api.data.Form
import play.api.data.Forms._
import play.api.mvc.Results._
import play.api.mvc.{Action, Controller}
import views.html

import scala.concurrent.Future
import play.api.libs.concurrent.Execution.Implicits.defaultContext

case class NewAccount(email: String, password: String)

class Session extends Controller with LoginLogout with AuthConfigImpl {

  val loginForm = Form {
    mapping("email" -> email, "password" -> text)(Account.authenticate)(_.map(u => (u.email, "")))
      .verifying("Invalid email or password", result => result.isDefined)
  }

  val registerForm = Form(
    mapping("email" -> email, "password" -> nonEmptyText)(NewAccount.apply)(NewAccount.unapply)
  )

  def login = Action { implicit request =>
    Ok(html.login(loginForm))
  }

  def register = Action { implicit request =>
    Ok(html.register(registerForm))
  }

  def logout = Action.async { implicit request =>
    gotoLogoutSucceeded.map(_.flashing(
      "success" -> "You've been logged out"
    ))
  }

  def addUser = Action.async { implicit request =>
    registerForm.bindFromRequest.fold(
      formWithErrors => Future.successful(BadRequest(html.register(formWithErrors))),
      userData => {
        Account.create(userData.email, userData.password, NormalUser)
        gotoLogoutSucceeded.map(_.flashing(
          "success" -> "Sign up successful"
        ))
      }
    )
  }

  def authenticate = Action.async { implicit request =>
    loginForm.bindFromRequest.fold(
      formWithErrors => Future.successful(BadRequest(html.login(formWithErrors))),
      user => gotoLoginSucceeded(user.get.id)
    )
  }
}
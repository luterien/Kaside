package controllers

import play.api.mvc.RequestHeader
import play.api.mvc.Results.{Redirect, Forbidden}

import jp.t2v.lab.play2.auth._
import models.{Account, Role, Administrator, NormalUser}
import scala.concurrent.{Future, ExecutionContext}
import scala.reflect.{ClassTag, classTag}

trait AuthConfigImpl extends AuthConfig {

  type Id = Int
  type User = Account
  type Authority = Role
  val idTag: ClassTag[Id] = classTag[Id]
  val sessionTimeoutInSeconds: Int = 3600

  def resolveUser(id: Id)(implicit ctx: ExecutionContext): Future[Option[User]] = Future.successful(Account.findById(id))

  def loginSucceeded(request: RequestHeader)(implicit ctx: ExecutionContext) =
    Future.successful(Redirect(routes.BookmarkApp.index))

  def logoutSucceeded(request: RequestHeader)(implicit ctx: ExecutionContext) =
    Future.successful(Redirect(routes.Session.login))

  def authenticationFailed(request: RequestHeader)(implicit ctx: ExecutionContext) = {
    println("authentication failed")
    Future.successful(Redirect(routes.Session.login))
  }

  override def authorizationFailed(request: RequestHeader, user: User, authority: Option[Authority])(implicit context: ExecutionContext) = {
    Future.successful(Forbidden("no permission"))
  }

  def authorize(user: User, authority: Authority)(implicit context: ExecutionContext): Future[Boolean] = Future.successful {
    (user.role, authority) match {
      case (Administrator, _)       => true
      case (NormalUser, NormalUser) => true
      case _                        => false
    }
  }

}

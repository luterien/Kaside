package controllers

import jp.t2v.lab.play2.auth.{AuthElement, LoginLogout}
import play.api._
import play.api.mvc._

import play.api.data._
import play.api.data.Forms._
import play.api.Play.current
import play.api.i18n.Messages.Implicits._
import models.{NormalUser, Bookmark}

/**
  * Created by asus on 19.2.2016.
  */

case class BookmarkData(title: String, url: String, tags: String)

class BookmarkApp extends Controller with AuthElement with AuthConfigImpl {

  val bookmarkForm = Form(
    mapping(
      "title" -> text,
      "url" -> text,
      "tags" -> text
    )(BookmarkData.apply)(BookmarkData.unapply)
  )

  def index = StackAction(AuthorityKey -> NormalUser) { implicit request =>
    Ok(views.html.index(Bookmark.findAll, bookmarkForm))
  }

  def filterBookmarks(tags: String) = Action {
    Ok(views.html.index(Bookmark.filterBookmarks(tags), bookmarkForm))
  }

  def add = Action {
    implicit request =>
      val data = bookmarkForm.bindFromRequest.get
      Bookmark.add(new Bookmark(data.title, data.url, data.tags))
      Redirect(routes.BookmarkApp.index)
  }

}

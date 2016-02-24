package models

import play.api.db.DB
import anorm._
import play.api.Play.current
import anorm.SqlParser.{str}
/**
  * Created by asus on 19.2.2016.
  */

case class Bookmark(title: String, url: String)

object Bookmark {

  val defaultParser = str("TITLE") ~ str("URL") map {
    case title ~ url  => Bookmark(title, url)
  }

  def findAll = DB.withConnection { implicit c =>
    SQL("SELECT * FROM BOOKMARKS;").executeQuery().list(defaultParser)
  }

  def findByTitle(title: String) = ""

  def add(bookmark: Bookmark){
    DB.withConnection { implicit c =>
      SQL("INSERT INTO BOOKMARKS(TITLE, URL) VALUES ({title}, {url});")
        .on('title -> bookmark.title, 'url -> bookmark.url).executeInsert()
    }
  }

}

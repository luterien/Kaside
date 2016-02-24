package models

import play.api.db.DB
import anorm._
import play.api.Play.current
import anorm.SqlParser.{str}
/**
  * Created by asus on 19.2.2016.
  */

case class Bookmark(title: String, url: String, tags: String)

object Bookmark {

  val defaultParser = str("title") ~ str("url") ~ str("tags") map {
    case title ~ url ~ tags => Bookmark(title, url, tags)
  }

  def findAll = DB.withConnection { implicit c =>
    SQL("SELECT * FROM Bookmarks;").executeQuery().list(defaultParser)
  }

  def findByTitle(title: String) = ""

  def add(bookmark: Bookmark){
    DB.withConnection { implicit c =>
      SQL("INSERT INTO BOOKMARKS(title, url, tags) VALUES ({title}, {url}, {tags});")
        .on('title -> bookmark.title, 'url -> bookmark.url, 'tags -> bookmark.tags)
        .executeInsert()
    }
  }

}

package models

import play.api.db.DB
import anorm._
import play.api.Play.current
import anorm.SqlParser._
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

  def findUserBookmarks(id: Int) = DB.withConnection { implicit c =>
    SQL("SELECT * FROM Bookmarks WHERE acc_id={acc_id};").
      on('acc_id -> id).executeQuery().list(defaultParser)
  }

  def filterBookmarks(tags: String) = DB.withConnection { implicit c =>
    SQL("SELECT * FROM Bookmarks;").executeQuery().list(defaultParser)
  }

  def findByTitle(title: String) = ""

  def add(b: Bookmark, id: Int){
    DB.withConnection { implicit c =>
      SQL("INSERT INTO BOOKMARKS(title, url, tags, acc_id) VALUES ({title}, {url}, {tags}, {acc_id});")
        .on('title -> b.title, 'url -> b.url, 'tags -> b.tags, 'acc_id -> id)
        .executeInsert()
    }
  }

}

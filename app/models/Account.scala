package models

import anorm.SqlParser._
import anorm._
import play.api.Play.current
import play.api.db.DB
import org.mindrot.jbcrypt.BCrypt

/**
  * Created by asus on 26.2.2016.
  */

case class Account(id: Int, email: String, password: String, role: Role)

object Account {

  val defaultParser = int("id") ~ str("email") ~ str("password") ~  str("role") map {
    case id ~ email ~ pass ~ role => Account(id, email, pass, NormalUser)
  }

  def authenticate(email: String, password: String): Option[Account] =
    findByEmail(email).filter { account => BCrypt.checkpw(password, account.password) }

  def findByEmail(email: String): Option[Account] = DB.withConnection { implicit c =>
    SQL("SELECT id, email, password, role FROM Accounts WHERE email={email}")
      .on('email -> email).executeQuery().singleOpt(defaultParser)
  }

  def findById(id: Int): Option[Account] = DB.withConnection { implicit c =>
    SQL("SELECT * FROM Accounts WHERE id={id}")
      .on('id -> id).executeQuery().singleOpt(defaultParser)
  }

  def create(email: String, password: String, role: Role) = DB.withConnection { implicit c =>
    val pw_hash = BCrypt.hashpw(password, BCrypt.gensalt())
    SQL("INSERT INTO Accounts (email, password, role) VALUES ({email}, {password}, {role});")
      .on('email -> email, 'password -> pw_hash, 'role -> role.toString)
      .executeInsert()
  }

  def findAll: Seq[Account] = DB.withConnection { implicit c =>
    SQL("SELECT * FROM Accounts;").executeQuery().list(defaultParser)
  }

}

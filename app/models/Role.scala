package models

/**
  * Created by asus on 27.2.2016.
  */
sealed trait Role

case object Administrator extends Role {
  override def toString = "administrator"
}

case object NormalUser extends Role {
  override def toString = "normaluser"
}
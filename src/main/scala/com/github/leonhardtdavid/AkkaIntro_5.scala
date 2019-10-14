package com.github.leonhardtdavid

import akka.actor._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class WrongAsyncActor extends Actor {

  private var something = 0

  override def receive: Receive = {
    case n: Int => Future {
      Thread.sleep(200)
      if (something < n) something = n
    }

    case _ => println(something)
  }

}

class AsyncActor extends Actor {

  private var something = 0

  override def receive: Receive = {
    case n: Int => Future {
      Thread.sleep(200)
      self ! n.toString
    }

    case s: String if s forall Character.isDigit =>
      val n = s.toInt
      if (something < n) something = n

    case _ => println(something)
  }

}

object AkkaIntro_5 extends App {

  val system: ActorSystem = ActorSystem("AkkaIntro_5")

  val actor: ActorRef = system.actorOf(Props[WrongAsyncActor], "WrongAsyncActor")
//  val actor: ActorRef = system.actorOf(Props[AsyncActor], "AsyncActor")

  actor ! 4
  actor ! 9
  Thread.sleep(300)
  actor ! "Print"

}

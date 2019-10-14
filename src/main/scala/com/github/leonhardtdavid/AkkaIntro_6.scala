package com.github.leonhardtdavid

import akka.actor._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class AsyncA(b: ActorRef, n: Int) extends Actor  {

  override def receive: Receive = {
    case "In" => b ! "In"
    case x    => println(s"on A-$n: $x")
  }

}

class AsyncB extends Actor  {

  override def receive: Receive = {
    case "In" =>
      val senderRef = sender()
      Future {
        Thread.sleep(200)
        senderRef ! "Tellme something"
      }
  }

}

object AkkaIntro_6 extends App {

  val system: ActorSystem = ActorSystem("AkkaIntro_6")

  val b: ActorRef = system.actorOf(Props[AsyncB], "AsyncBActor")
  val a1: ActorRef = system.actorOf(Props(new AsyncA(b, 1)), "AsyncAActor1")
  val a2: ActorRef = system.actorOf(Props(new AsyncA(b, 2)), "AsyncAActor2")

  a1 ! "In"
  a2 ! "In"

}

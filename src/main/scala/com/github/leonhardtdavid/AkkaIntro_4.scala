package com.github.leonhardtdavid

import akka.actor._

import scala.util.Random

class Maradona extends Actor {

  private val countriesOfTheWorld = 194

  private val children = (1 to countriesOfTheWorld * (Random.nextInt(10) + 1)) map { n =>
    context.actorOf(Props(new MaradonaChild(Random.nextBoolean())), s"child-$n")
  }

  override def receive: Receive = {
    case x => this.children.foreach(_ ! x)
  }

}

class MaradonaChild(avowed: Boolean) extends Actor {

  override def receive: Receive = {
    case _ => println(s"Am I avowed? $avowed")
  }

}

object AkkaIntro_4 extends App {

  val system: ActorSystem = ActorSystem("AkkaIntro_4")

  val maradona: ActorRef = system.actorOf(Props[Maradona], "Maradona")

  maradona ! "Print"

}

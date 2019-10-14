package com.github.leonhardtdavid

import akka.actor._

class ImprovedCounter extends Actor {

  import context._

  override def receive: Receive = this.behavior(0)

  private def behavior(acc: Int): Receive = {
    case "Inc"   => become(behavior(acc + 1))
    case "Dec"   => become(behavior(acc - 1))
    case "Print" => println(acc)
  }

}

object AkkaIntro_3 extends App {

  // Create the actor system
  val system: ActorSystem = ActorSystem("AkkaIntro_3")

  // Create the counter actor
  val counter: ActorRef = system.actorOf(Props[ImprovedCounter], "ImprovedCounter")

  counter ! "Print"
  counter ! "Inc"
  counter ! "Inc"
  counter ! "Dec"
  counter ! "Print"

}

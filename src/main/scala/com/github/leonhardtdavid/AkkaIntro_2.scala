package com.github.leonhardtdavid

import akka.actor._

class Counter extends Actor {

  private var acc = 0

  override def receive: Receive = {
    case "Inc"   => acc += 1
    case "Dec"   => acc -= 1
    case "Print" => println(acc)
  }

}

object AkkaIntro_2 extends App {

  // Create the actor system
  val system: ActorSystem = ActorSystem("AkkaIntro_2")

  // Create the counter actor
  val counter: ActorRef = system.actorOf(Props[Counter], "Counter")

  counter ! "Print"
  counter ! "Inc"
  counter ! "Inc"
  counter ! "Dec"
  counter ! "Print"

}

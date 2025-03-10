package com.github.leonhardtdavid

import akka.actor.{Actor, ActorRef, ActorSystem, Props}

class Greeter(message: String, printerActor: ActorRef) extends Actor {

  import Greeter._
  import Printer._
  import context._

  override def receive: Receive = this.behavior("")

  private def behavior(greeting: String): Receive = {
    case WhoToGreet(who) =>
      become(behavior(s"$message, $who"))

    case Greet =>
      printerActor ! Greeting(greeting)
  }

}

object Greeter {

  def props(message: String, printerActor: ActorRef): Props = Props(new Greeter(message, printerActor))

  final case class WhoToGreet(who: String)

  case object Greet

}

class Printer extends Actor {

  import Printer._

  override def receive: Receive = {
    case Greeting(greeting) =>
      println(s"Greeting received (from ${sender()}): $greeting")
  }

}

object Printer {

  def props: Props = Props[Printer]

  final case class Greeting(greeting: String)

}

object AkkaIntro_7 extends App {

  import Greeter._

  // Create the 'helloAkka' actor system
  val system: ActorSystem = ActorSystem("AkkaIntro_7")

  // Create the printer actor
  val printer: ActorRef = system.actorOf(Printer.props, "printerActor")

  // Create the 'greeter' actors
  val howdyGreeter: ActorRef = system.actorOf(Greeter.props("Howdy", printer), "howdyGreeter")
  val helloGreeter: ActorRef = system.actorOf(Greeter.props("Hello", printer), "helloGreeter")
  val goodDayGreeter: ActorRef = system.actorOf(Greeter.props("Good day", printer), "goodDayGreeter")

  howdyGreeter ! WhoToGreet("Akka")
  howdyGreeter ! Greet

  howdyGreeter ! WhoToGreet("Lightbend")
  howdyGreeter ! Greet

  helloGreeter ! WhoToGreet("Scala")
  helloGreeter ! Greet

  goodDayGreeter ! WhoToGreet("Play")
  goodDayGreeter ! Greet

}

package com.github.leonhardtdavid

import akka.actor._
import akka.pattern._
import akka.util.Timeout

import scala.concurrent.{ExecutionContext, Future}
import scala.concurrent.duration._

class Print extends Actor {

  override def receive: Receive = {
    case x => println(x)
  }

}

class Echo extends Actor {

  override def receive: Receive = {
    case x => sender() ! x
  }

}

class A(b: ActorRef) extends Actor  {

  override def receive: Receive = {
    case "In" => b ! "In"
    case x    => println(s"on A: $x")
  }

}

class B(echo: ActorRef) extends Actor  {

  override def receive: Receive = {
    case "In" => echo ! "Tellme something" // try changing to forward instead of tell (!)
    case x    => println(s"on B: $x")
  }

}

object AkkaIntro_1 extends App {

  // Create the actor system
  val system: ActorSystem = ActorSystem("AkkaIntro_1")

  // Create the printer actor
  val printer: ActorRef = system.actorOf(Props[Print], "printActor")

  printer ! "Hello World!"

  // Create the echo actor
  val echo: ActorRef = system.actorOf(Props[Echo], "echoActor")

  implicit val timeout: Timeout = Timeout(1.second)
  implicit val ec: ExecutionContext = system.dispatcher

  (echo ? "eco eco eco") foreach println

  val b: ActorRef = system.actorOf(Props(new B(echo)), "BActor")
  val a: ActorRef = system.actorOf(Props(new A(b)), "AActor")

  a ! "In"

  val future = Future {
    Thread.sleep(2000)
    "awake"
  }

  future.pipeTo(printer)

}

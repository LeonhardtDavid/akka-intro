package com.github.leonhardtdavid

import akka.actor._

import scala.concurrent.duration._
import scala.concurrent.{Await, ExecutionContext, Future}

class ActorWithStash extends Actor with ActorLogging with Stash {

  import context._

  override def receive: Receive = {
    case "INIT" =>
      log.info("Initializing...")
      Future {
        Thread.sleep(2000)
        self ! "OK"
      }

    case "OK" if sender() == self =>
      unstashAll()
      become({
        case msg => log.info("Message received: {}", msg)
      })

    case _ =>
      stash()
  }

}

object ActorWithStash {
  def props: Props = Props[ActorWithStash]
}

object AkkaIntro_13 extends App {

  val system: ActorSystem = ActorSystem("AkkaIntro_13")
  implicit val ec: ExecutionContext = system.dispatcher

  val ref = system.actorOf(ActorWithStash.props, "ActorWithStash")

  ref ! "INIT"
  ref ! "message1"
  ref ! "message2"

  Thread.sleep(3000)

  Await.ready(system.terminate(), 30.seconds)

}

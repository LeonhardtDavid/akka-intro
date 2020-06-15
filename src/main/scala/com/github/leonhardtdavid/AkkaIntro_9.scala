package com.github.leonhardtdavid

import java.util.UUID

import akka.actor._
import akka.routing.{Broadcast, RoundRobinPool}

class Routee extends Actor with ActorLogging {

  private val uuid = UUID.randomUUID().toString

  override def receive: Receive = {
    case msg => log.debug(s"$uuid ==>> $msg")
  }

}

object Routee {

  def routerProps: Props = RoundRobinPool(nrOfInstances = 2).props(Props[Routee])

}

object AkkaIntro_9 extends App {

  val system: ActorSystem = ActorSystem("AkkaIntro_9")

  val router = system.actorOf(Routee.routerProps, "RouterActor")

  router ! "First message"
  router ! "Second message"

  router ! Broadcast("Broadcast message")

}

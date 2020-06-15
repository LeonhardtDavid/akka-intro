package com.github.leonhardtdavid

import akka.actor.SupervisorStrategy.{Restart, Stop}
import akka.actor._
import akka.pattern.{BackoffOpts, BackoffSupervisor}

import scala.concurrent.duration._

class FooActor extends Actor {

  private val bar1 = context.actorOf(BarActor.props, "bar1")
  private val bar2 = context.actorOf(BarActor.props, "bar2") // Just to try AllForOneStrategy

  override def supervisorStrategy: SupervisorStrategy = OneForOneStrategy() {
    case _: NullPointerException => Restart
    case _                       => Stop
  }

  override def receive: Receive = {
    case msg => bar1 ! msg
  }

}

object FooActor {
  def props: Props = Props[FooActor]
}

class BarActor extends Actor with ActorLogging {

  override def receive: Receive = {
    case "fail" => throw new NullPointerException
    case _      => throw new RuntimeException
  }

  override def postRestart(reason: Throwable): Unit = {
    log.debug("FooActor restarted")
  }

  override def postStop(): Unit = {
    log.debug("FooActor stopped")
  }

}

object BarActor {
  def props: Props = Props[BarActor]
}

object AkkaIntro_8 extends App {

  val system: ActorSystem = ActorSystem("AkkaIntro_8")

  val fooActor: ActorRef = system.actorOf(FooActor.props, "FooActor")

  fooActor ! "fail"

  Thread.sleep(500)

  fooActor ! ""

//  val supervisorProps = BackoffSupervisor.props(
//    BackoffOpts
//      .onFailure(
//        childProps = BarActor.props,
//        childName = "BarActor-child",
//        minBackoff = 2.seconds,
//        maxBackoff = 6.seconds,
//        randomFactor = 0.2
//      )
//      .withAutoReset(4.seconds) // Must be between minBackoff and maxBackoff
//  )
//
//  val supervisor = system.actorOf(supervisorProps, "BarActorSupervisor")
//
//  supervisor ! "fail"
//
//  Thread.sleep(5000)

}

package com.github.leonhardtdavid

import akka.actor._

import scala.concurrent.duration._
import scala.concurrent.{Await, ExecutionContext}

class SillyActor extends Actor {

  override def receive: Receive = {
    case _ => println("Message received on SillyActor")
  }

}

object SillyActor {
  def props: Props = Props[SillyActor]
}

class WithSchedulerSillyActor extends Actor with Timers {

  timers.startTimerAtFixedRate("key", "message", 5.seconds)

  override def receive: Receive = {
    case _ => println("Message received on WithSchedulerSillyActor")
  }

}

object WithSchedulerSillyActor {
  def props: Props = Props[WithSchedulerSillyActor]
}

object AkkaIntro_11 extends App {

  val system: ActorSystem = ActorSystem("AkkaIntro_11")
  implicit val ec: ExecutionContext = system.dispatcher

  system.scheduler.scheduleOnce(5.seconds, () => {
    println("Scheduled Once - Runnable")
  })

  val ref = system.actorOf(SillyActor.props, "SillyActor")

  system.scheduler.scheduleOnce(10.seconds, ref, "silly message")

//  val ref = system.actorOf(WithSchedulerSillyActor.props, "WithSchedulerSillyActor")
//
//  ref ! "message"

  Thread.sleep(20000)

  Await.ready(system.terminate(), 30.seconds)

}

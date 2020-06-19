package com.github.leonhardtdavid

import akka.actor._
import akka.event.Logging.MDC
import akka.event.slf4j.Slf4jLogMarker
import akka.event.{Logging, LoggingAdapter, MarkerLoggingAdapter}
import net.logstash.logback.marker.Markers

import scala.collection.JavaConverters._
import scala.concurrent.duration._
import scala.concurrent.{Await, ExecutionContext}
import scala.util.Random

class SomeActor extends Actor with ActorLogging {

  //  val log: LoggingAdapter = Logging(context.system, this)

  override def receive: Receive = {
    case msg => log.info("Message received: {}", msg)
  }

}

object SomeActor {
  def props: Props = Props[SomeActor]
}

class SomeActorWithMDC extends Actor with DiagnosticActorLogging {

  override def mdc(currentMessage: Any): MDC = {
    Map("some_key" -> Random.nextInt())
  }

  override def receive: Receive = {
    case _ =>
      // log.clearMDC()
      log.mdc(log.mdc ++ Map("another_key" -> "another value"))
      log.info("Message received with MDC")
  }

}

object SomeActorWithMDC {
  def props: Props = Props[SomeActorWithMDC]
}

class SomeActorWithMarker extends Actor {

  private val log: MarkerLoggingAdapter = Logging.withMarker(context.system, this)

  private def marker(message: Any) = Slf4jLogMarker(
    Markers.appendEntries(Map("a_key" -> "a value", "a_message" -> message).asJava)
  )

  override def receive: Receive = {
    case msg => log.info(marker(msg), "Message received with Marker")
  }

}

object SomeActorWithMarker {
  def props: Props = Props[SomeActorWithMarker]
}

object AkkaIntro_12 extends App {

  val system: ActorSystem = ActorSystem("AkkaIntro_12")
  implicit val ec: ExecutionContext = system.dispatcher

  val ref = system.actorOf(SomeActorWithMarker.props, "SomeActor")

  ref ! 0

  Thread.sleep(2000)

  Await.ready(system.terminate(), 30.seconds)

}

package com.github.leonhardtdavid

import akka.actor._
import akka.pattern.CircuitBreaker

import scala.concurrent.Future
import scala.concurrent.duration._

class MaybeFailureActor extends Actor {

  private val breaker = CircuitBreaker(
    scheduler = context.system.scheduler,
    maxFailures = 2,
    callTimeout = 2.seconds, // It doesn't matter in this case
    resetTimeout = 10.seconds
  ).onOpen {
    self ! "OPEN"
  }.onHalfOpen {
    self ! "HALF"
  }.onClose {
    self ! "CLOSE"
  }

  override def receive: Receive = {
    case "OK" =>
      println("OK received")
      breaker.succeed()

    case "FAIL" =>
      println("FAIL received")
      breaker.fail()

    case "OPEN" =>
      println("Breaker has been opened")

    case "HALF" =>
      println("Breaker partially opened")

    case "CLOSE" =>
      println("Breaker has been closed")

    case "FUTURE_OK" =>
      println("FUTURE_OK received")
      breaker.withCircuitBreaker(Future.successful(""))

    case "FUTURE_FAIL" =>
      println("FUTURE_FAIL received")
      breaker.withCircuitBreaker(Future.failed(new RuntimeException))
  }

}

object MaybeFailureActor {
  def props: Props = Props[MaybeFailureActor]
}

object AkkaIntro_10 extends App {

  val system: ActorSystem = ActorSystem("AkkaIntro_10")

  val ref = system.actorOf(MaybeFailureActor.props, "MaybeFailureActor")

  val OK = "OK"
  val FAIL = "FAIL"

  ref ! OK
  ref ! FAIL
  ref ! FAIL

  Thread.sleep(11000)

  ref ! OK
  ref ! OK

  Thread.sleep(15000)

  ref ! FAIL
  ref ! FAIL

  Thread.sleep(11000)

  ref ! FAIL

}

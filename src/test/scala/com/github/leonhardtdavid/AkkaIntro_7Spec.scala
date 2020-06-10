package com.github.leonhardtdavid

import akka.actor.ActorSystem
import akka.testkit.{TestKit, TestProbe}
import com.github.leonhardtdavid.Greeter._
import com.github.leonhardtdavid.Printer._
import org.scalatest.BeforeAndAfterAll
import org.scalatest.matchers.must.Matchers
import org.scalatest.wordspec.AnyWordSpecLike

import scala.concurrent.duration._

class AkkaIntro_7Spec
  extends TestKit(ActorSystem("AkkaIntro_7Spec"))
    with Matchers
    with AnyWordSpecLike
    with BeforeAndAfterAll {

  override def afterAll: Unit = {
    shutdown(system)
  }

  "A Greeter Actor" should {
    "pass on a greeting message when instructed to" in {
      val testProbe = TestProbe()
      val helloGreetingMessage = "hello"

      val helloGreeter = system.actorOf(Greeter.props(helloGreetingMessage, testProbe.ref))

      val greetPerson = "Akka"
      helloGreeter ! WhoToGreet(greetPerson)
      helloGreeter ! Greet

      testProbe.expectMsg(500.millis, Greeting(helloGreetingMessage + ", " + greetPerson))
    }
  }

}

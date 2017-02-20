package com.marionete.pi

import scala.concurrent.duration._
import akka.actor.ActorSystem
import akka.testkit.{DefaultTimeout, ImplicitSender, TestActorRef, TestKit}
import org.scalatest.{BeforeAndAfter, BeforeAndAfterAll, Matchers, WordSpecLike}

/**
  * Created by agapito on 20/12/2016.
  */
class TestSlave extends TestKit(ActorSystem("TestSlave"))
  with Matchers
  with DefaultTimeout
  with ImplicitSender
  with WordSpecLike
  with BeforeAndAfterAll {

  override def afterAll {
    TestKit.shutdownActorSystem(ActorSystem("TestSlave"))
  }

  "a slave" must {

    "check that point is inside circle" in {
      val point = Point(0.5, 0.5)
      val actor = TestActorRef[Slave]
      within(500 millis){
        actor.underlyingActor.checkPoint(point) should be(true)
      }
    }

    "check that point is outside circle" in {
      val point = Point(1.0, 1.0)
      val actor = TestActorRef[Slave]
      within(500 millis){
        actor.underlyingActor.checkPoint(point) should be(false)
      }
    }

    val points:List[Point] = List(
      Point(0.1,0.1),
      Point(0.1,0.1),
      Point(0.1,0.1),
      Point(0.1,0.1),
      Point(0.1,0.1),
      Point(0.1,0.1),
      Point(0.1,0.1),
      Point(0.1,0.1),
      Point(0.1,0.1),

      Point(1.0,1.0),
      Point(1.0,1.0),
      Point(1.0,1.0)
    )

    "calculate pi" in {
      val actor = TestActorRef[Slave]
      within(500 millis){
        val inCircle: Double = points.count( actor.underlyingActor.checkPoint(_) ).toDouble
        val allPoints: Double = points.size.toDouble
        def piApprox: Double = 4.0 * inCircle / allPoints

        piApprox should be(3.0)

      }
    }

    "calculate pi using method" in {
      val actor = TestActorRef[Slave]
      within(500 millis) {
        actor.underlyingActor.calculatePi(points) should be(3.0)
      }
    }

    "generate a random list of points" in {
      val actor = TestActorRef[Slave]
      within(500 millis) {
        actor.underlyingActor.genPoints(10).size should be(10)
      }
    }

    "calculate Pi when it receives a message" in {
      val actor = TestActorRef[Slave]
      actor ! MsgCalcPi(10)
      expectMsgPF(500 millis){
        case MsgPi => true
        case _ => false
      }
    }
  }
}

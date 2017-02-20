package com.marionete.pi

import akka.actor.{Actor, ActorSystem, Props}

import scala.util.Random

/**
  * Created by agapito on 20/12/2016.
  */

@SerialVersionUID(1L)
class Slave extends Actor {

  var state: Double = 0.0

  def receive: PartialFunction[Any, Unit] = {
    case MsgCalcPi(n:Int) =>
      println(s"Got message MsgCalcPi($n)")
      if ( n <= 100000)
        state = calculatePi(genPoints(n))
      else
        state = -9999999.9999999
      println(s"Sending state $state")
      sender ! MsgPi(state)
    case _ =>
      println(s"Got it!")
  }

  def checkPoint(point: Point): Boolean = point.sqnorm < 1.0
  def calculatePi(points: List[Point]): Double = 4.0 * points.count(checkPoint).toDouble / points.size.toDouble
  def genPoints(n:Int): List[Point] = (1 to n).map{ _ =>
    val x = Random.nextDouble()*2.0 - 1.0
    val y = Random.nextDouble()*2.0 - 1.0
    Point(x,y)
  }.toList

}

@SerialVersionUID(1L)
sealed trait Msg
@SerialVersionUID(1L)
case class MsgCalcPi(n:Int) extends Msg
@SerialVersionUID(1L)
case class MsgPi(pi:Double) extends Msg
@SerialVersionUID(1L)
case class MsgStartPi(n:Int) extends Msg


object Main extends App {

  val nActors = 100
  val system = ActorSystem("SlavePi")
  1.to(nActors).foreach(n => system.actorOf(Props[Slave], name=s"SlaveDoFilipe$n"))
  //val slave = system.actorOf(Props[Slave], name="SlaveDoFilipe")

}
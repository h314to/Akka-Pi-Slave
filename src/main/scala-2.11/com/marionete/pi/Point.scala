package com.marionete.pi

/**
  * Created by agapito on 20/12/2016.
  */
case class Point(x:Double, y:Double) {
  def sqnorm: Double = (x*x) + (y*y)
}

// Union types

def passEither(n: Int | String): Unit = n match
  case i: Int    => println(s"Int: $i")
  case s: String => println(s"String: $s")

passEither(3)
passEither("Scala")

type ErrorOr[T] = T | "Error"

import java.io.File as JFile

def handleResource(file: ErrorOr[JFile]): Unit = file match
  case _: "Error" => println("Error")
  case f: JFile   => println(s"File: $f")

// Intersection types

trait Camera:
  def takePhoto(): Unit
  def use(): Unit = println("Using camera")

trait Phone:
  def call(): Unit
  def use(): Unit = println("Using phone")

def useDevice(device: Camera & Phone): Unit =
  device.takePhoto()
  device.call()
  device.use()

class SmartPhone extends Camera, Phone:
  def takePhoto(): Unit = println("Taking photo")
  def call(): Unit = println("Calling")

  override def use(): Unit = println("Using smart phone")

useDevice(new SmartPhone)

trait HostConfig
trait HostController:
  def get: Option[HostConfig]

trait PortConfig
trait PortController:
  def get: Option[PortConfig]

def getConfig(
    controller: HostController & PortController
): Option[HostConfig & PortConfig] = controller.get

organization := "net.scalapro"

name := "lift-portal"

version := "0.1"

scalaVersion := "2.12.2"

val liftVer = "3.1.0-RC1"

libraryDependencies +="net.liftweb"       %% "lift-webkit"        % liftVer       % "compile"

libraryDependencies += "javax.servlet" % "javax.servlet-api" % "3.0.1" % "provided"
libraryDependencies += "com.typesafe.slick" % "slick_2.12" % "3.2.0"
libraryDependencies += "org.postgresql" % "postgresql" % "9.4.1212.jre7"
libraryDependencies += "org.slf4j" % "slf4j-nop" % "1.6.4"

enablePlugins(TomcatPlugin)

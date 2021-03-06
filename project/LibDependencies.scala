import sbt._
import play.core.PlayVersion
import sbt.ModuleID

object LibDependencies {
  def apply(): Seq[ModuleID] = Seq(
    "com.typesafe.play"      %% "play"               % PlayVersion.current,
    "com.vladsch.flexmark"    % "flexmark-all"       % "0.35.10"           % Test,
    "org.jsoup"               % "jsoup"              % "1.13.1"            % Test,
    "org.scalatest"          %% "scalatest"          % "3.2.3"             % Test,
    "com.typesafe.play"      %% "play-test"          % PlayVersion.current % Test,
    "org.scalatestplus.play" %% "scalatestplus-play" % "5.1.0"             % Test
  )
}

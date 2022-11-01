import scala.sys.process._
import play.sbt.PlayImport.PlayKeys._

val libName = "play-frontend-pta"

val npmBuild = taskKey[Unit]("npm-build")
val npmTest  = taskKey[Unit]("npm-test")

lazy val root = Project(libName, file("."))
  .enablePlugins(PlayScala, SbtTwirl)
  .disablePlugins(PlayLayoutPlugin)
  .settings(
    name := libName,
    majorVersion := 0,
    scalaVersion := "2.13.8",
    libraryDependencies ++= LibDependencies(),
    TwirlKeys.templateImports := templateImports,
    npmTest := {
      val exitCode = ("npm ci" #&& "npm test").!
      if (exitCode != 0) {
        throw new MessageOnlyException("npm install and test failed")
      }
    },
    npmBuild := {
      val exitCode = ("npm ci" #&& "npm run build").!
      if (exitCode != 0) {
        throw new MessageOnlyException("npm install and build failed")
      }
    },
    Compile / packageBin := (Compile / packageBin).dependsOn(npmBuild).value,
    Test / test := (Test / test).dependsOn(npmBuild).value,
    isPublicArtefact := true,
    scalafmtOnCompile := true,
    sbt.Test / parallelExecution := false,
    playMonitoredFiles ++= (Compile / TwirlKeys.compileTemplates / sourceDirectories).value,
    scalacOptions ++= Seq(
      "-Werror",
      "-Wconf:cat=unused-imports&site=.*views\\.html.*:s",
      "-Wconf:cat=unused-imports&site=<empty>:s",
      "-Wconf:cat=unused&src=.*Routes\\.scala:s"
    )
  )

lazy val templateImports: Seq[String] = Seq(
  "_root_.play.twirl.api.Html",
  "_root_.play.twirl.api.HtmlFormat",
  "_root_.play.twirl.api.JavaScript",
  "_root_.play.twirl.api.Txt",
  "_root_.play.twirl.api.Xml",
  "play.api.mvc._",
  "play.api.data._",
  "play.api.i18n._",
  "play.api.templates.PlayMagic._",
  "_root_.play.twirl.api.TwirlFeatureImports._",
  "_root_.play.twirl.api.TwirlHelperImports._"
)

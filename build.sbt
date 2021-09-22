import scala.sys.process._
import play.sbt.PlayImport.PlayKeys._

val libName         = "play-frontend-pta"
val silencerVersion = "1.7.2"
val npmBuild        = taskKey[Unit]("npm-build")
val npmTest         = taskKey[Unit]("npm-test")

lazy val root = Project(libName, file("."))
  .enablePlugins(PlayScala, SbtTwirl)
  .disablePlugins(PlayLayoutPlugin)
  .settings(
    name := libName,
    majorVersion := 0,
    scalaVersion := "2.12.13",
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
    packageBin in Compile := (packageBin in Compile).dependsOn(npmBuild).value,
    test in Test := (test in Test).dependsOn(npmBuild).value,
    isPublicArtefact := true,
    // ***************
    // Use the silencer plugin to suppress warnings from unused imports in compiled twirl templates
    scalacOptions += "-P:silencer:pathFilters=views;routes",
    libraryDependencies ++= Seq(
      compilerPlugin("com.github.ghik" % "silencer-plugin" % silencerVersion cross CrossVersion.full),
      "com.github.ghik" % "silencer-lib" % silencerVersion % Provided cross CrossVersion.full
    ),
    parallelExecution in sbt.Test := false,
    playMonitoredFiles ++= (sourceDirectories in (Compile, TwirlKeys.compileTemplates)).value
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

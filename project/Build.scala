import sbt._
import Keys._
import PlayProject._

object ApplicationBuild extends Build {

    val appName         = "play2-boilerplate"
    val appVersion      = "1.0-SNAPSHOT"

    val appDependencies = Seq(
      "mysql" % "mysql-connector-java" % "5.1.21"
    )

    // Only compile Bootstrap's main files and any other
    // `*.less` file in the `stylesheets` directory.
    def customLessEntryPoints(base: File): PathFinder = (
      (base / "app" / "assets" / "stylesheets" / "bootstrap" * "bootstrap-2.1.1.less") +++
      (base / "app" / "assets" / "stylesheets" / "bootstrap" * "responsive-2.1.1.less") +++
      (base / "app" / "assets" / "stylesheets" / "bootstrap" * "font-awesome-ie7.less") +++
      (base / "app" / "assets" / "stylesheets" * "*.less")
    )

    val main = PlayProject(
      appName, appVersion, appDependencies, mainLang = SCALA
    ).settings(
      lessEntryPoints <<= baseDirectory(customLessEntryPoints),
      coffeescriptOptions := Seq("native", "/usr/local/bin/browserify")
    )

}

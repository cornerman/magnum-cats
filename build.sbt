Global / onChangedBuildSource := ReloadOnSourceChanges

inThisBuild(
  Seq(
    organization := "com.github.cornerman",
    licenses     := Seq("MIT License" -> url("https://opensource.org/licenses/MIT")),
    homepage     := Some(url("https://github.com/cornerman/magnum-cats")),
    scmInfo      := Some(
      ScmInfo(
        url("https://github.com/cornerman/magnum-cats"),
        "scm:git:git@github.com:cornerman/magnum-cats.git",
        Some("scm:git:git@github.com:cornerman/magnum-cats.git"),
      ),
    ),
    pomExtra     :=
      <developers>
        <developer>
        <id>jk</id>
        <name>Johannes Karoff</name>
        <url>https://github.com/cornerman</url>
        </developer>
    </developers>,
  ),
)

lazy val catsEffect = project
  .in(file("cats-effect"))
  .settings(
    name         := "magnum-cats-effect",
    scalaVersion := "3.4.1",
    libraryDependencies ++= Seq(
      "org.typelevel"   %% "cats-effect" % "3.5.4",
      "com.augustnagro" %% "magnum"      % "1.1.1",
    ),
  )

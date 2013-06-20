name := "geochat"

version := "1.0"

mainClass := Some("JettyLauncher")

seq(webSettings :_*)

port in container.Configuration := 8080

externalResolvers <<= resolvers map { rs =>
  Resolver.withDefaultResolvers(rs, mavenCentral=true, scalaTools = false)
}

/*
 * Dependency hell:
 * http://stackoverflow.com/questions/4235082/configuring-jetty-jsp-support-in-embedded-mode-in-maven-project
 * http://stackoverflow.com/questions/2151075/cannot-load-jstl-taglib-within-embedded-jetty-server
 * http://stackoverflow.com/questions/9889674/sbt-jetty-and-servlet-3-0
 *
 */
libraryDependencies ++= Seq(
  "javax.servlet" % "servlet-api" % "2.5" % "provided" withSources(),
  "org.eclipse.jetty" % "jetty-webapp" % "7.1.4.v20100610" % "container",
  "org.mortbay.jetty" % "jsp-2.1-glassfish" % "2.1.v20100127" % "container",
  "javax.servlet.jsp" % "jsp-api" % "2.1" % "container" withSources(),
  "javax.servlet" % "jstl" % "1.2",
  "org.springframework" % "spring-core" % "3.1.1.RELEASE" withSources(),
  "org.springframework" % "spring-beans" % "3.1.1.RELEASE" withSources(),
  "org.springframework" % "spring-aop" % "3.1.1.RELEASE" withSources(),
  "org.springframework" % "spring-context" % "3.1.1.RELEASE" withSources(),
  "org.springframework" % "spring-context-support" % "3.1.1.RELEASE" withSources(),
  "org.springframework" % "spring-tx" % "3.1.1.RELEASE" withSources(),
  "org.springframework" % "spring-jdbc" % "3.1.1.RELEASE" withSources(),
  "org.springframework" % "spring-web" % "3.1.1.RELEASE" withSources(),
  "org.springframework" % "spring-webmvc" % "3.1.1.RELEASE" withSources(),
  "org.slf4j" % "slf4j-api" % "1.6.4" withSources(),
  "org.slf4j" % "slf4j-log4j12" % "1.6.4" withSources(),
  "log4j" % "log4j" % "1.2.16" withSources(),
  "cglib" % "cglib" % "2.2.2" withSources(),
  "aopalliance" % "aopalliance" % "1.0" withSources(),
  "postgresql" % "postgresql" % "9.1-901-1.jdbc4",
  "org.aspectj" % "aspectjrt" % "1.6.12",
  "org.aspectj" % "aspectjweaver" % "1.6.12",
  "org.aspectj" % "aspectjtools" % "1.6.12"
)


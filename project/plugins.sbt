// The Play plugin
addSbtPlugin("com.typesafe.play" % "sbt-plugin" % "2.6.11")

// The Scala test plugin
addSbtPlugin("com.artima.supersafe" % "sbtplugin" % "1.1.3")

resolvers += "Artima Maven Repository" at "http://repo.artima.com/releases"

resolvers += Resolver.typesafeRepo("releases")

addSbtPlugin("org.lyranthe.sbt" % "partial-unification" % "1.1.0")

package loader

import play.api.{Application, ApplicationLoader}

class RestaurantFinderLoader extends ApplicationLoader {
  def load(context: ApplicationLoader.Context): Application = {
    val module = new RestaurantFinderModule(context)
    module.flywayPlayInitializer.onStart()
    module.application
  }
}

package loader

import play.api.{Application, ApplicationLoader}

class RestaurantFinderLoader extends ApplicationLoader {
  def load(context: ApplicationLoader.Context): Application = new RestaurantFinderModule(context).application
}

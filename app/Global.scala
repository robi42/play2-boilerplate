import play.api._

import utilities.Assets

object Global extends GlobalSettings {

  override def onStart(app: Application) {
    Assets.bundleJs
  }

}

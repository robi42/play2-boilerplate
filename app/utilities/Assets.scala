package utilities

import play.api.Play
import play.api.Play.current

import com.google.common.base.Charsets._
import com.google.common.io.Files

import io.Source
import java.io.{File, FileOutputStream}
import java.util.zip.GZIPOutputStream

object Assets {

  def envify(path: String) = {
    if (Play.isProd)
      path.replace(".js", ".min.js")
          .replace(".css", ".min.css")
    else path
  }

  def bundleJs = {
    val path = new File("./public/javascripts").getCanonicalPath
    val bundleConfFilename = new File("./conf/jsBundle").getCanonicalPath

    val devBundle = new File("%s/bundle.js" format path)
    val prodBundle = new File("%s/bundle.min.js" format path)
    Files write ("", devBundle, UTF_8)
    Files write ("", prodBundle, UTF_8)

    Source.fromFile(bundleConfFilename).getLines map { resourceName =>
      "%s/%s.min.js" format (path, resourceName)
    } foreach { filename =>
      Files append (Source.fromFile(filename).mkString, prodBundle, UTF_8)
    }
    gzip("%s.gz" format prodBundle.getCanonicalPath,
         Source.fromFile(prodBundle).mkString)

    Source.fromFile(bundleConfFilename).getLines map { resourceName =>
      "%s/%s.js" format (path, resourceName)
    } foreach { filename =>
      Files append (Source.fromFile(filename).mkString, devBundle, UTF_8)
    }
  }

  def gzip(filename: String, content: String) = {
    val gzipper = new GZIPOutputStream(new FileOutputStream(filename))
    gzipper write content.getBytes(UTF_8)
    gzipper.finish
    gzipper.close
  }

}

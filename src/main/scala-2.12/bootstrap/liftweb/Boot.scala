package bootstrap.liftweb

import net.liftweb.http.{ContentSecurityPolicy, ContentSourceRestriction, Html5Properties, LiftRules, Req, SecurityRules}
import net.liftweb.sitemap.{Menu, SiteMap}
import net.scalapro.liftportal.setup.DBSetup

class Boot {
  def boot {
    // where to search snippet
    LiftRules.addToPackages("net.scalapro.liftportal")

    // Build SiteMap
    val entries = List(
      Menu.i("Home") / "index",
      Menu.i("Edit Template") /"cms"/ "edit-template",
      Menu.i("Template") /"cms"/ "templates"
    )

    // Use HTML5 for rendering
    LiftRules.htmlProperties.default.set((r: Req) =>
      new Html5Properties(r.userAgent))

    LiftRules.early.append(_.setCharacterEncoding("UTF-8"))

    LiftRules.setSiteMap(SiteMap(entries: _*))


    LiftRules.securityRules = () => {
      SecurityRules(content = Some(ContentSecurityPolicy(
        scriptSources = List(ContentSourceRestriction.Self,
                    ContentSourceRestriction.UnsafeInline,
          ContentSourceRestriction.UnsafeEval
        ),
        styleSources = List(ContentSourceRestriction.Self,
          ContentSourceRestriction.UnsafeInline
        )
      )))
    }

    DBSetup.setup
  }
}

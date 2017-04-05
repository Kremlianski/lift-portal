package bootstrap.liftweb

import net.liftweb.common.Full
import net.liftweb.http.{ContentSecurityPolicy, ContentSourceRestriction, Html5Properties, LiftRules, Req, ResourceServer, SecurityRules}
import net.liftweb.sitemap.Loc.Hidden
import net.liftweb.sitemap.{**, Menu, SiteMap}
import net.scalapro.liftportal.setup.DBSetup
import net.scalapro.liftportal.view.TemplateView
import net.scalapro.liftportal.cms.tables.Widgets

class Boot {
  def boot {
    // where to search snippet
    LiftRules.addToPackages("net.scalapro.liftportal")

    // Build SiteMap
    val entries = List(
      Menu.i("Home") / "index",
      Menu.i("Edit Template") /"cms"/ "edit-template",
      Menu.i("Edit Container") /"cms"/ "edit-container",
      Menu.i("Templates") /"cms"/ "templates",
      Menu.i("Containers") /"cms"/ "containers",
      Menu.i("Template") /"cms"/ "template"/ ** >> Hidden

    )

    LiftRules.viewDispatch.append{
      case List("cms", "template", id) =>
        Left(() => Full(TemplateView.edit(id)))
    }

    // Use HTML5 for rendering
    LiftRules.htmlProperties.default.set((r: Req) =>
      new Html5Properties(r.userAgent))

    LiftRules.early.append(_.setCharacterEncoding("UTF-8"))

    LiftRules.setSiteMap(SiteMap(entries: _*))

    ResourceServer.allow {
      case "css" :: _ => true
      case "js" :: _ => true
      case "lib" :: _ => true
    }

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
    Widgets.register
    DBSetup.setup
  }
}

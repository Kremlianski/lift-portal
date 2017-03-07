package net.scalapro.liftportal.util


object Tags {
  implicit def string2Option(s: String) = Some(s)

  case class Td(title: String, className: Option[String] = None, href: Option[String] = None)

  def td(title: String, className: Option[String] = None, href: Option[String] = None) = {
    val t = Td(title, className, href)
    t match {
      case Td(x, None, None) => <td>
        {x}
      </td>
      case Td(x, None, Some(h)) => <td>
        <a href={h}>
          {x}
        </a>
      </td>
      case Td(x, Some(c), None) => <td class={c}>
        {x}
      </td>
      case Td(x, Some(c), Some(h)) => <td class={c}>
        <a href={h}>
          {x}
        </a>
      </td>
    }

  }
}

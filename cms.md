# CMS

## Structure

1. Template
2. Page
3. Container
4. Widget
5. Space


Static markup should be stored in DB as pieces of XML fragments
**Widgets** should be used for dynamic content.

**Templates** should combine static markup and **widgets**

**Pages** should combine static markup in form of **containers** and dynamic content
in form of **widgets**


**Spaces** is just boxes in containers, which can contain other elements (widgets and containers)


### Template
It consists from markup and contains spaces and a special container for page. Spaces contain widgets. A template includes general markup for a group of pages.


### Page
It is wrapped by a template. Technically it's a box for content. It includes containers and widgets. 


### Container
It's a fragment of XML markup ant it contains spaces. I is used to form page markup.


### Widget
It's used for dynamic content. Every widget should have parameters specified by user.

### Space 
Space is a snippet, that can contain containers and widgets. A user can place new element to a space by JavaScript interface.




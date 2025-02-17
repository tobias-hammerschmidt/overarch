Changelog
=========

Version 0.7.0
-------------
* make file watches work on Macs by using beholder instead of hawk 
* added direction rendering to relations in class and state machine views
* enhanced example models


Version 0.6.0
-------------
* refactored exports, distinguish between
  * exports of model data (to JSON, structurizr)
  * rendering of views (e.g. to PlantUML)
* changed command line options to reflect the refactoring
* added render-format 'all' to generate all formats in one go
* updated usage and design documentation and diagrams


Version 0.5.0
-------------
* fixed and enhanced class view rendering
* enhanced example models


Version 0.4.0
-------------
* added concept view for concept maps
* added graphviz export for concept view
* updated logical data model to incorporate enhancements
* enhanced example models
* updated and enhanced usage documentation
* updated design document


Version 0.3.0
-------------
* added first markdown export
* added concept model elements
* added glossary view (textual view)
* added logical data model for overarch
* enhanced documentation and examples


Version 0.2.0
-------------
* added sprite includes for :sprite entries
* added support for UML use case, state and class views/diagrams
* enhanced documentation


Version 0.1.0
-------------
* initial import
* data format specification
* data loading
* support for views
  * system landscape
  * system context
  * container
  * component
  * deployment
  * dynamic
* command line interface
  * exports to json, plantuml and structurizr
  * file system watch for exports on changes
  * print sprite mappings
  * infos about the loaded model
* json export
  * based on the individual EDN files 
* plantuml export
  * styling support
  * sprite support
* structurizr export *experimental*
  * export structurizr workspace with model and views


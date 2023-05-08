Overarch Architecture Description
=================================
A data driven description of a software architecture based on the C4 model.

Describe your model as data and specify/generate representations (e.g. diagrams) for your model.

Rationale
---------

C4 models are great to vizualize an architecture on different levels of detail with the various diagrams types. The value lies in an expressive description and visualization of an architecture with different views.

But but the models used for diagram generation with the existing tools are not models in the sense of generality. Especially if you describe your model in PlantUML files, these descriptions are mere textfiles.

The textfiles don't compose and you can't do anything else with these descriptions other than render them with PlantUML. The parsing process is opaque and you don't have access to the data of the model.

Also the model is complected with the diagrams, as layout and rendering information is part of the model description and vice versa.

If the model is described as plain *data* in an open format, it can be transformed into a graphical representation, e.g. into PlantUML textfiles.

The model data should be separated from information about these representations. They can be composed with these information and with other models. By doing so, the model may also be used in other ways, e.g. the generation of documentation, code or infrastructure.

Even if the model is specified as data, the format should be a text file (EDN, JSON) to be easily edited with text editors by the whole team and to be committed to version control, instead of being in some propriatary binary format.

The native format should be Extensible Data Notation (EDN) with representations in other formats like JSON. EDN is a textual format for data, which is human readable. It is directly readable into data structures in clojure code.

The data format should be open for extension. E.g. it should cope with additional attributes in the data structures.

The model should describe the architecture (the structure) of your system(s). The elements are based on the C4 model and are a hierarchical composition of the elements of the architecture.

Model references should be used to refer to model elements from representations (e.g. diagrams). To allow references to relations, the definition of a relation must have an id.

Model references may be enhanced with additional attributes that are specific to the usage context (e.g. a sprite attribute in the context of a diagram to be rendered by PlantUML)

### Related Projects

* [C4 PlantUML](https://github.com/plantuml-stdlib/C4-PlantUML)
  * C4 standard library for PlantUML
  * just diagram oriented
  * no separation between model and presentation
  * pure textual representation
  * needs a parser implementation

* [structurizr](https://structurizr.org/)
  * C4 modelling tools from Simon Brown, the inventor of C4 models
  * diagram oriented
  * separates model from views, but in the same files
  * unwieldy tooling (IMHO)

* [fc4-framework](https://github.com/FundingCircle/fc4-framework)
  * FC4 is a Docs as Code tool to create software architecture diagrams.
  * just diagram oriented
  * no separation between model and presentation
  * tied to structurizr for modelling and visualization

* [archinsight](https://github.com/lonely-lockley/archinsight)
  * Insight language is a DSL (Domain Specific Language)
  * to translate C4 Model description into DOT language,
  * that can be rendered by Graphviz.
  * Insight supports only the first two levels of C4.
  * pure textual representation
  * needs a parser implementation


Examples
--------

Example of a model definition

```
[{:el :person
  :id :banking/personal-customer
  :name "Personal Banking Customer"
  :desc "A customer of the bank, with personal banking accounts."}
 {:el :system
  :id :banking/internet-banking-system
  :name "Internet Banking System"
  :desc "Allows customers to view information about their bank accounts and make payments."
  :ct [{:el :container
        :id :banking/web-app
        :name "Web Application"
        :desc "Deliveres the static content and the internet banking single page application."
        :tech "Clojure and Luminus"}
       {:el :container
        :id :banking/single-page-app
        :name "Single-Page Application"
        :desc "Provides all of the internet banking functionality to customers via their web browser."
        :tech "ClojureScript and Re-Frame"}
       {:el :container
        :id :banking/mobile-app
        :name "Mobile App"
        :desc "Provides a limited subset of the internet banking functionality to customers via their mobile device."
        :tech "ClojureScript and Reagent"}
       {:el :container
        :id :banking/api-application
        :name "API Application"
        :desc "Provides internet banking functionality via a JSON/HTTPS API."
        :tech "Clojure and Liberator"}
       {:el :container
        :subtype :database
        :id :banking/database
        :name "Database"
        :desc "Stores the user registration information, hashed authentication credentials, access logs, etc."
        :tech "Datomic"}]}
 {:el :system
  :id :banking/mainframe-banking-system
  :external true
  :name "Mainframe Banking System"
  :desc "Stores all the core banking information about customers, accounts, transactions, etc."}
 {:el :system
  :id :banking/email-system
  :external true
  :name "E-mail System"
  :desc "The internal Microsoft Exchange email system."}

 ; Context diagram relations 
 {:el :rel
  :id :banking/personal-customer-uses-internet-banking-system
  :from :banking/personal-customer
  :to :banking/internet-banking-system
  :name "Views account balances and makes payments using"}
 {:el :rel
  :id :banking/internet-banking-system-uses-email-system
  :from :banking/internet-banking-system
  :to :banking/email-system
  :name "Sends e-mail using"}
 {:el :rel
  :id :banking/internet-banking-system-using-mainframe-banking-system
  :from :banking/internet-banking-system
  :to :banking/mainframe-banking-system
  :name "Gets account information from, and makes payments using"}
 {:el :rel
  :id :banking/email-system-sends-mail-to-personal-customer
  :from :banking/email-system
  :to :banking/personal-customer
  :name "Sends e-mail to"}] 
 ```

Example of a diagrams specification

```
[{:el :context-diagram
  :id :banking/system-context-view
  :title "System Context View of the Internet Banking System"
  :ct [; model elements
       {:ref :banking/personal-customer}
       {:ref :banking/email-system}
       {:ref :banking/mainframe-banking-system}
       {:ref :banking/internet-banking-system}
       
       ; relations
       {:ref :banking/personal-customer-uses-internet-banking-system :direction :down}
       {:ref :banking/internet-banking-system-uses-email-system :direction :right}
       {:ref :banking/internet-banking-system-using-mainframe-banking-system}
       {:ref :banking/email-system-sends-mail-to-personal-customer :direction :up}]}
 ]
 ```

PlantUML export of the System Context View
```
@startuml banking_systemContextView
!include https://raw.githubusercontent.com/plantuml-stdlib/C4-PlantUML/master/C4_Context.puml

title System Context View of the Internet Banking System
Person(banking_personalCustomer, "Personal Banking Customer", $descr="A customer of the bank, with personal banking accounts.")
System_Ext(banking_emailSystem, "E-mail System", $descr="The internal Microsoft Exchange email system.")
System_Ext(banking_mainframeBankingSystem, "Mainframe Banking System", $descr="Stores all the core banking information about customers, accounts, transactions, etc.")
System(banking_internetBankingSystem, "Internet Banking System", $descr="Allows customers to view information about their bank accounts and make payments.")

Rel_Down(banking_personalCustomer, banking_internetBankingSystem, "Views account balances and makes payments using")
Rel_Right(banking_internetBankingSystem, banking_emailSystem, "Sends e-mail using")
Rel(banking_internetBankingSystem, banking_mainframeBankingSystem, "Gets account information from, and makes payments using")
Rel_Up(banking_emailSystem, banking_personalCustomer, "Sends e-mail to")
@enduml

```

System Context View rendered with PlantUML
![System Context View rendered with PlantUML](/doc/banking_systemContextView.svg)

Usage
-----

Use a folder for all the data (e.g. models, diagram specifications).
Add EDN files for the model and the diagram specifications and other representations. All the EDN files in the folder will be loaded.

...


Copyright
---------
© 2023 Ludger Solbach

License
-------
Eclipse Public License 1.0


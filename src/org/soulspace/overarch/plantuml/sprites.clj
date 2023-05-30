(ns org.soulspace.overarch.plantuml.sprites
  (:require [clojure.string :as str]
            [clojure.java.io :as io]
            [charred.api :as csv]
            [org.soulspace.clj.java.file :as file]))

(comment
  ; include icon/sprite sets, if icons are used, e.g. 
  "!define DEVICONS https://raw.githubusercontent.com/tupadr3/plantuml-icon-font-sprites/master/devicons"
  "!define FONTAWESOME https://raw.githubusercontent.com/tupadr3/plantuml-icon-font-sprites/master/font-awesome-5"
  "!include DEVICONS/angular.puml
   !include DEVICONS/java.puml
   !include DEVICONS/msql_server.puml
   !include FONTAWESOME/users.puml
   ")

(def sprite-libraries
  "Definition of sprite libraries."
  {:azure {:name "azure"
           :local-prefix "azure"
           :local-imports ["AzureCommon"
                           "AzureC4Integration"]
           :remote-prefix "AZURE"
           :remote-url "https://raw.githubusercontent.com/azure/"
           :remote-imports ["AzureCommon"
                            "AzureC4Integration"]}
   :aws {:name "awslib"
         :local-prefix "awslib"
         :local-imports ["AWSCommon"
                         "AWSC4Integration"]
         :remote-prefix "AWS"
         :remote-url "https://raw.githubusercontent.com/awslib/"
         :remote-imports ["AWSCommon"
                          "AWSC4Integration"]}
   :devicons    {:name "devicons"
                 :local-prefix "devicons"
                 :remote-prefix "DEVICONS"
                 :remote-url "https://raw.githubusercontent.com/tupadr3/plantuml-icon-font-sprites/master/devicons"}
   :fontawesome {:name "fontawesome"
                 :local-prefix "fontawesome"
                 :remote-prefix "FONTAWESOME"
                 :remote-url "https://raw.githubusercontent.com/tupadr3/plantuml-icon-font-sprites/master/font-awesome-5"}})

(def tech->sprite
  "Map of technology names to sprite infos."
  {"Azure Batch AI"                    {:lib "azure"
                                        :path "AIMachineLearning"
                                        :name "AzureBatchAI"}
   "Azure Bot Service"                 {:lib "azure"
                                        :path "AIMachineLearning"
                                        :name "AzureBotService"}
   "Azure Cognitive Services"          {:lib "azure"
                                        :path "AIMachineLearning"
                                        :name "AzureCognitiveServices"}
   "Azure Machine Learning Service"    {:lib "azure"
                                        :path "AIMachineLearning"
                                        :name "AzureMachineLearningService"}
   "Azure Machine Learning Studio"     {:lib "azure"
                                        :path "AIMachineLearning"
                                        :name "AzureMachineLearningStudio"}
   "Microsoft Genomics"                {:lib "azure"
                                        :path "AIMachineLearning"
                                        :name "MicrosoftGenomics"}
   "Azure Analysis Services"           {:lib "azure"
                                        :path "Analytics"
                                        :name "AzureAnalysisServices"}
   "Azure Data Catalog"                {:lib "azure"
                                        :path "Analytics"
                                        :name "AzureDataCatalog"}
   "Azure Data Explorer"               {:lib "azure"
                                        :path "Analytics"
                                        :name "AzureDataExplorer"}
   "Azure Data Lake Analytics"         {:lib "azure"
                                        :path "Analytics"
                                        :name "AzureDataLakeAnalytics"}
   "Azure Databricks"                  {:lib "azure"
                                        :path "Analytics"
                                        :name "AzureDatabricks"}
   "Azure Event Hub"                   {:lib "azure"
                                        :path "Analytics"
                                        :name "AzureEventHub"}
   "Azure HDInsight"                   {:lib "azure"
                                        :path "Analytics"
                                        :name "AzureHDInsight"}
   "Azure Stream Analytics"            {:lib "azure"
                                        :path "Analytics"
                                        :name "AzureStreamAnalytics"}
   "Azure App Service"                 {:lib "azure"
                                        :path "Compute"
                                        :name "AzureAppService"}
   "Azure Batch"                       {:lib "azure"
                                        :path "Compute"
                                        :name "AzureBatch"}
   "Azure Function"                    {:lib "azure"
                                        :path "Compute"
                                        :name "AzureFunction"}
   "Azure Service Fabric"              {:lib "azure"
                                        :path "Compute"
                                        :name "AzureServiceFabric"}
   "Azure Virtual Machine"             {:lib "azure"
                                        :path "Compute"
                                        :name "AzureVirtualMachine"}
   "Azure Virtual Machine Scale Set"   {:lib "azure"
                                        :path "Compute"
                                        :name "AzureVirtualMachineScaleSet"}
   "Azure Container Instance"          {:lib "azure"
                                        :path "Containers"
                                        :name "AzureContainerInstance"}
   "Azure Container Registry"          {:lib "azure"
                                        :path "Containers"
                                        :name "AzureContainerRegistry"}
   "Azure Kubernetes Service"          {:lib "azure"
                                        :path "Containers"
                                        :name "AzureKubernetesService"}
   "Azure Service Fabric Mesh"         {:lib "azure"
                                        :path "Containers"
                                        :name "AzureServiceFabricMesh"}
   "Azure Web App For Containers"      {:lib "azure"
                                        :path "Containers"
                                        :name "AzureWebAppForContainers"}
   "Azure"                             {:lib "azure"
                                        :path "General"
                                        :name "Azure"}
   "App Configuration"                 {:lib "azure"
                                        :path "Management"
                                        :name "AppConfiguration"}
   "Azure Arc"                         {:lib "azure"
                                        :path "Management"
                                        :name "AzureArc"}
   "Azure Arc Machine"                 {:lib "azure"
                                        :path "Management"
                                        :name "AzureArcMachine"}
   "Azure Automation"                  {:lib "azure"
                                        :path "Management"
                                        :name "AzureAutomation"}
   "Azure Backup"                      {:lib "azure"
                                        :path "Management"
                                        :name "AzureBackup"}
   "Azure Blue Prints"                 {:lib "azure"
                                        :path "Management"
                                        :name "AzureBluePrints"}
   "Azure Compliance"                  {:lib "azure"
                                        :path "Management"
                                        :name "AzureCompliance"}
   "Azure Cost Alert"                  {:lib "azure"
                                        :path "Management"
                                        :name "AzureCostAlert"}
   "Azure Cost Analysis"               {:lib "azure"
                                        :path "Management"
                                        :name "AzureCostAnalysis"}
   "Azure Cost Budget"                 {:lib "azure"
                                        :path "Management"
                                        :name "AzureCostBudget"}
   "Azure Cost Management"             {:lib "azure"
                                        :path "Management"
                                        :name "AzureCostManagement"}
   "Azure Cost Management And Billing" {:lib "azure"
                                        :path "Management"
                                        :name "AzureCostManagementAndBilling"}
   "Azure Geo Recovery"                {:lib "azure"
                                        :path "Management"
                                        :name "AzureGeoRecovery"}
   "Azure Lighthouse"                  {:lib "azure"
                                        :path "Management"
                                        :name "AzureLighthose"}
   "Azure Log Analytics"               {:lib "azure"
                                        :path "Management"
                                        :name "AzureLogAnalytics"}
   "Azure Managed Application Center"  {:lib "azure"
                                        :path "Management"
                                        :name "AzureManagedApplicationCenter"}
   "Azure Managed Applications"        {:lib "azure"
                                        :path "Management"
                                        :name "AzureManagedApplications"}
   "Azure Management Group"            {:lib "azure"
                                        :path "Management"
                                        :name "AzureManagementGroups"}
   "Azure Management Portal"           {:lib "azure"
                                        :path "Management"
                                        :name "AzureManagementPortal"}
   "Azure Metrics"                     {:lib "azure"
                                        :path "Management"
                                        :name "AzureMetrics"}
   "Azure Monitor"                     {:lib "azure"
                                        :path "Management"
                                        :name "AzureMonitor"}
   "Azure Policy"                      {:lib "azure"
                                        :path "Management"
                                        :name "AzurePolicy"}
   "Azure Resource Group"              {:lib "azure"
                                        :path "Management"
                                        :name "AzureResourceGroups"}
   "Azure Scheduler"                   {:lib "azure"
                                        :path "Management"
                                        :name "AzureScheduler"}
   "Azure Site Recovery"               {:lib "azure"
                                        :path "Management"
                                        :name "AzureSiteRecovery"}
   "Azure Subscription"                {:lib "azure"
                                        :path "Management"
                                        :name "AzureSubscription"}
   "Azure Application Gateway"         {:lib "azure"
                                        :path "Networking"
                                        :name "AzureApplicationGateway"}
   "Azure DDoS Protection"             {:lib "azure"
                                        :path "Networking"
                                        :name "AzureAzureDDoSProtection"}
   "Azure DNS"                         {:lib "azure"
                                        :path "Networking"
                                        :name "AzureDNS"}
   "Azure Express Route"               {:lib "azure"
                                        :path "Networking"
                                        :name "AzureExpressRoute"}
   "Azure Front Door Service"          {:lib "azure"
                                        :path "Networking"
                                        :name "AzureFrontDoorService"}
   "Azure Load Balancer"               {:lib "azure"
                                        :path "Networking"
                                        :name "AzureLoadBalancer"}
   "Azure Traffic Manager"             {:lib "azure"
                                        :path "Networking"
                                        :name "AzureTrafficManager"}
   "Azure VPN Gateway"                 {:lib "azure"
                                        :path "Networking"
                                        :name "AzureVPNGateway"}
   "Azure Virtual Network"             {:lib "azure"
                                        :path "Networking"
                                        :name "AzureVirtualNetwork"}
   "Azure Virtual WAN"                 {:lib "azure"
                                        :path "Networking"
                                        :name "AzureVirtualWAN"}
   "Azure Key Vault"                   {:lib "azure"
                                        :path "Security"
                                        :name "AzureKeyVault"}
   "Azure Sentinel"                    {:lib "azure"
                                        :path "Security"
                                        :name "AzureSentinel"}
   "Azure Blob Storage"                {:lib "azure"
                                        :path "Storage"
                                        :name "AzureBlobStorage"}
   "Azure Data Box"                    {:lib "azure"
                                        :path "Storage"
                                        :name "AzureDataBox"}
   "Azure Data Lake Storage"           {:lib "azure"
                                        :path "Storage"
                                        :name "AzureDataLakeStorage"}
   "Azure File Storage"                {:lib "azure"
                                        :path "Storage"
                                        :name "AzureFileStorage"}
   "Azure Managed Disks"               {:lib "azure"
                                        :path "Storage"
                                        :name "AzureManagedDisks"}
   "Azure Net App Files"               {:lib "azure"
                                        :path "Storage"
                                        :name "AzureNetAppFiles"}
   "Azure Queue Storage"               {:lib "azure"
                                        :path "Storage"
                                        :name "AzureQueueStorage"}
   "Azure Stor Simple"                 {:lib "azure"
                                        :path "Storage"
                                        :name "AzureStorSimple"}
   "Azure Storage"                     {:lib "azure"
                                        :path "Storage"
                                        :name "AzureStorage"}})

(defn sprite?
  "Returns true if the icon-map contains an icon for the given technology."
  [tech]
  (tech->sprite tech))

(defn plantuml-imports
  "Returns a collection of vectors of all the PlantUML '*.puml' files in
   the given directory `dir` containing the path relative to the `dir`
   and the base name of the import file."
  [dir]
  (->> dir
       (file/all-files-by-extension "puml")
       (map (partial file/relative-path dir))
       (map (juxt file/parent-path file/base-name))
       ;(map file/base-name)
       ))
(defn write-csv
  "Writes the collection `coll` in CSV format to `file`."
  [file coll]
  (with-open [wrt (io/writer file)]
    (csv/write-csv wrt coll)))

(defn sprite-entry
  "Prepares sprite entry info from collection PlantUML imports."
  [x]
  (let [path-entries (str/split (first x) #"/")
        sprite-lib (first path-entries)
        sprite-path (str/join "/" (rest path-entries))
        sprite-name (last x)
        sprite-key (last x)]
    [sprite-key {:lib sprite-lib
                 :path sprite-path
                 :name sprite-name}]))

(comment
  (count "/home/soulman/devel/tmp/plantuml-stdlib")
  (write-csv "dev/stdlib.csv" (into [] (plantuml-imports "/home/soulman/devel/tmp/plantuml-stdlib")))

  (count (plantuml-imports "/home/soulman/devel/tmp/plantuml-stdlib/")) 

  (map sprite-entry (plantuml-imports "/home/soulman/devel/tmp/plantuml-stdlib/"))

  )
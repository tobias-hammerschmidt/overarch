;;;;
;;;; JSON export
;;;;
(ns org.soulspace.overarch.exports.json
  (:require [org.soulspace.clj.java.file :as file]
            [org.soulspace.overarch.export :as exp]
            [org.soulspace.overarch.io :as oio]))
(defn export-json
  "Exports the data files in the model directory, as specified by `options`,
   to JSON."
  [options]
  ; The export works on file level.
  ; Each EDN file is read and the data is written as JSON.
  (doseq [file (file/all-files-by-extension "edn" (:model-dir options))]
    (let [out-dir (str (:export-dir options) "/"
                       (file/parent-path file) "/")
          out-file (str out-dir (file/base-name file) ".json")]
      (->> file
           (oio/load-edn)
           (oio/write-json out-file)))))

(defmethod exp/export :json
  [format options]
  (export-json options))

(comment
  (export-json "models"))
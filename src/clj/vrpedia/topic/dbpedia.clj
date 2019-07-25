(ns vrpedia.topic.dbpedia
  (:require [vrpedia.sparql.query :as sparql]))

(defn- resolve-common [uri]
  (-> {:uri uri}
      (into (sparql/query-common uri))))

(defn fetch [topic-uri]
  (resolve-common topic-uri))

(comment
  (resolve-common "http://dbpedia.org/resource/History_of_architecture"))

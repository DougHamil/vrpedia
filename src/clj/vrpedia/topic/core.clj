(ns vrpedia.topic.core
  (:require [vrpedia.topic.search :as search]
            [vrpedia.topic.dbpedia :as dbpedia]
            [vrpedia.topic.wikipedia :as wikipedia]))

(defn resolve [topic-uri]
  (-> topic-uri
      (dbpedia/fetch)
      (wikipedia/fetch)))

(defn search [term]
  (when-let [topic-uri (search/search-wikipedia-for-term term)]
    (resolve topic-uri)))


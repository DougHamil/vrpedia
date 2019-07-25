(ns vrpedia.topic.search
  (:require [clj-http.client :as http]))

(def wikipedia-search-url "https://en.wikipedia.org/w/api.php?action=opensearch&limit=1&namespace=0&format=json&search=")
(def resource-path "http://dbpedia.org/resource/")

(defn name->resource [name] (str resource-path name))

(defn search-wikipedia-for-term [term]
  (let [url (str wikipedia-search-url term)]
    (as-> url u
      (http/get u {:accept :json :as :json})
      (:body u)
      (first (last u))
      (if (nil? u)
        u
        (name->resource (last (clojure.string/split u #"/")))))))

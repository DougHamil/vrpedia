(ns semantic-web-ws.dbpedia
    (:require [clj-http.client :as http]
              [clojure.set :as set]
              [semantic-web-ws.sparql.query :as sparql]
              [semantic-web-ws.util :as util]))

(def not-found-resource {:abstract "Not found" :label {:value "Not found"} :uri nil :image "/img/warning_clojure.png"})
(def wikipedia-search-url "https://en.wikipedia.org/w/api.php?action=opensearch&limit=1&namespace=0&format=json&search=")
(def resource-path "http://dbpedia.org/resource/")
(def display-types {"http://dbpedia.org/ontology/Place" "Place"
                    "http://dbpedia.org/ontology/City" "City"
                    })
(defn name->resource [name] (str resource-path name))
(defn name->ontology [name] (str "http://dbpedia.org/ontology/" name))
(defn query-wikipedia [query]
  (let [url (str wikipedia-search-url query)]
    (as-> url u
      (http/get u {:accept :json :as :json})
      (:body u)
      (first (last u))
      (if (nil? u)
        u
        (name->resource (last (clojure.string/split u #"/")))))))

(defn find-resource-uri [query]
  (let [uri (query-wikipedia query)]
    (if (nil? uri)
      nil
      (sparql/query-redirect uri))))

(defn type-matcher [types other-types]
  (if (coll? types)
    (first (set/intersection (set types) (set other-types)))
    (first (set/intersection (set [types]) (set other-types)))))

(defn res->display-types [res]
  (do
    (println (:types res))
    (assoc res :display-types (filter some? (map #(get display-types %) (:types res))))))

(defn resolver
  ([res body] (body (:uri res) res))
  ([res types body]
   (let [matched-type (type-matcher types (:types res))]
     (if (some? matched-type)
       (do
         (println (:uri res) "is a" matched-type)
         (body (:uri res) res))
       res))))

;;;;;;;;;;;;;
;; Resolvers
;;;;;;;;;;;;;
(defn resolve-common [uri res]
  (-> res
      (into (sparql/query-description uri))
      (update :abstract (partial util/ascii-text))
      (update :image (util/default-on-nil-arg util/resolve-redirects "img/warning_clojure.png"))
      (assoc :types (sparql/query-types uri))
      (res->display-types)))

(defn resolve-people [uri res]
  (let [people (sparql/query-people uri)
        resolved people] ;(pmap #(-> % (update :image (util/default-on-nil-arg util/resolve-redirects "img/warning_clojure.png"))) people)]
    (assoc res :people resolved)))

(defn resolve-population [uri res] (assoc res :population (sparql/query-population uri)))
(defn resolve-location [uri res] (assoc res :location (sparql/query-location uri)))
(defn resolve-references [uri res] (assoc res :references (sparql/query-references uri)))


(defn resolve-resource [uri]
  (if (nil? uri)
    not-found-resource
    (-> {:uri uri}
      (resolver resolve-common)
      (resolver "http:
//www.w3.org/2003/01/geo/wgs84_pos#SpatialThing" resolve-location)
      (resolver (name->ontology "PopulatedPlace") resolve-population)
      (resolver (name->ontology "PopulatedPlace") resolve-people)
      (resolver resolve-references))))

(def resolve-resource-memoized (memoize resolve-resource))

(defn find-and-resolve-resource [query]
  (resolve-resource (find-resource-uri query)))

(def find-and-resolve-resource-memoized (memoize find-and-resolve-resource))

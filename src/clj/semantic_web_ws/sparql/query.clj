(ns semantic-web-ws.sparql.query
  (:require [yesparql.core :refer :all]
            [semantic-web-ws.util :as util]))

(defqueries "sparql/query.sparql" {:connection "http://dbpedia.org/sparql"})

(defn bind-uris [m]
  (reduce (fn [acc [k v]] (assoc acc k (java.net.URI. v))) {} m))

(defn slurp-results [queryfn]
  (with-open [results (queryfn)]
    (into [] results)))

(defn query-thing [uri queryfn]
  (map util/keyword-map (slurp-results (fn [] (queryfn {:bindings (bind-uris {:thing uri})})))))

(defn query-thing-top [uri queryfn]
  (first (query-thing uri queryfn)))

(defn query-thing-single-column [uri queryfn]
  (map (fn [row] (last (first (util/keyword-map row))))
     (slurp-results (fn [] (queryfn {:bindings (bind-uris {:thing uri})})))))

(defn flatten-values [m]
  (into {}
    (map
      (fn [[key val]]
        (let [value (:value val)]
          (if (some? value)
            [key value]
            [key val])))
      m)))

(defn query-description [uri]
  (flatten-values (query-thing-top uri select-description)))

(defn query-redirect [uri]
  (let [result (query-thing-top uri select-redirects)]
    (if (nil? result) uri (:parent result))))

(defn query-types [uri] (query-thing-single-column uri select-types))

(defn query-population [uri] (flatten-values (query-thing-top uri select-population)))
(defn query-location [uri] (flatten-values (query-thing-top uri select-location)))
(defn query-people [uri] (map flatten-values (query-thing uri select-people)))


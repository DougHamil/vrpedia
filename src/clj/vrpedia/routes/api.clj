(ns vrpedia.routes.api
  (:require [ring.util.http-response :as response]
            [vrpedia.dbpedia :as dbpedia]
            [vrpedia.topic.core :as topic]
            [reitit.ring :as ring]
            [reitit.core :as r]))

(defn handle-entity-search [req]
  (let [term (get-in req [:query-params "term"])]
    (response/ok (topic/search term))))

(defn handle-entity-request [req]
  (response/ok (dbpedia/resolve-resource-memoized req)))

(def routes
  ["/api"
   ["/entity" {:get {:handler handle-entity-request}}]
   ["/entity/search" {:get {:parameters {:query {:query string?}}}
                      :handler handle-entity-search}]])



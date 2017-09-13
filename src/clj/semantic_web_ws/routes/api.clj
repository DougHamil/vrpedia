(ns semantic-web-ws.routes.api
  (:require [compojure.core :refer [defroutes GET]]
            [ring.util.http-response :as response]
            [clojure.java.io :as io]
            [semantic-web-ws.dbpedia :as dbpedia]
            [clj-http.client :as http]))


(defn entity-search [req]
  (let [query (get-in req [:params :query])]
    (response/ok (dbpedia/find-and-resolve-resource query))))

(defroutes api-routes
  (GET "/api/entity/search" [] entity-search))


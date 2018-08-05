(ns semantic-web-ws.routes.home
  (:require [semantic-web-ws.layout :as layout]
            [compojure.core :refer [defroutes GET]]
            [ring.util.http-response :as response]
            [clojure.java.io :as io]))

(defn home-page []
  (response/found "/index.html"))

(defroutes home-routes
  (GET "/" [] (home-page)))


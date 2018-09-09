(ns vrpedia.routes.home
  (:require [vrpedia.layout :as layout]
            [compojure.core :refer [defroutes GET]]
            [ring.util.http-response :as response]
            [clojure.java.io :as io]))

(defn home-page []
  (response/found "/index.html"))

(defroutes home-routes
  (GET "/" [] (home-page)))


(ns vrpedia.handler
  (:require [compojure.core :refer [routes wrap-routes]]
            [vrpedia.layout :refer [error-page]]
            [vrpedia.routes.home :refer [home-routes]]
            [vrpedia.routes.api :refer [api-routes]]
            [compojure.route :as route]
            [mount.core :as mount]
            [vrpedia.middleware :as middleware]))

(def app-routes
  (routes
    (-> #'home-routes
        (wrap-routes middleware/wrap-csrf)
        (wrap-routes middleware/wrap-formats))
    (-> #'api-routes
        (wrap-routes middleware/wrap-csrf)
        (wrap-routes middleware/wrap-formats))
    (route/not-found
      (:body
        (error-page {:status 404
                     :title "page not found"})))))


(defn app [] (middleware/wrap-base #'app-routes))

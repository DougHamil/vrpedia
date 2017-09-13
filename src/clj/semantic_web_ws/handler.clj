(ns semantic-web-ws.handler
  (:require [compojure.core :refer [routes wrap-routes]]
            [semantic-web-ws.layout :refer [error-page]]
            [semantic-web-ws.routes.home :refer [home-routes]]
            [semantic-web-ws.routes.api :refer [api-routes]]
            [compojure.route :as route]
            [semantic-web-ws.env :refer [defaults]]
            [mount.core :as mount]
            [semantic-web-ws.middleware :as middleware]))

(mount/defstate init-app
                :start ((or (:init defaults) identity))
                :stop  ((or (:stop defaults) identity)))

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

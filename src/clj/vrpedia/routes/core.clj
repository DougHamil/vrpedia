(ns vrpedia.routes.core
  (:require [reitit.ring :as ring]
            [ring.middleware.params :as params]
            [muuntaja.middleware :as mw]
            [vrpedia.routes.api :as api]))

(def app
  (ring/ring-handler
   (ring/router
    [api/routes]
    {:data {:middleware [mw/wrap-format
                         params/wrap-params]}})
   (ring/routes
    (ring/create-resource-handler {:path "/"}))
   (ring/create-default-handler)))


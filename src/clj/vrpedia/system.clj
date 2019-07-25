(ns vrpedia.system
  (:require [integrant.core :as ig]
            [ring.adapter.jetty :as jetty]
            [ring.middleware.reload :refer [wrap-reload]]
            [ring.middleware.stacktrace :refer [wrap-stacktrace]]
            [vrpedia.routes.core :as routes]))


(def config {:adapter/jetty {:port 8080
                             :handler #'routes/app}})

(defmethod ig/init-key :adapter/jetty [_ {:keys [handler port]}]
  (jetty/run-jetty (-> handler
                       (wrap-reload)
                       (wrap-stacktrace))
                   {:port port
                    :join? false}))

(defmethod ig/halt-key! :adapter/jetty [_ s]
  (.stop s))


(defn start []
  (ig/init config))

(defn halt! [system]
  (ig/halt! system))


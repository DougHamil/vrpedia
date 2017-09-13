(ns user
  (:require [mount.core :as mount]
            semantic-web-ws.core))

(defn start []
  (mount/start-without #'semantic-web-ws.core/repl-server))

(defn stop []
  (mount/stop-except #'semantic-web-ws.core/repl-server))

(defn restart []
  (stop)
  (start))



(ns user
  (:require [mount.core :as mount]
            [semantic-web-ws.core :as core]))

(defn start []
  (mount/start-without #'core/repl-server))

(defn stop []
  (mount/stop-except #'core/repl-server))

(defn restart []
  (stop)
  (start))

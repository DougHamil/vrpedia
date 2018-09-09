(ns user
  (:require [mount.core :as mount]
            [vrpedia.core :as core]))

(defn start []
  (mount/start-without #'core/repl-server))

(defn stop []
  (mount/stop-except #'core/repl-server))

(defn restart []
  (stop)
  (start))

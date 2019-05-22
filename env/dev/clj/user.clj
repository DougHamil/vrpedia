(ns user
  (:require [mount.core :as mount]
            [vrpedia.core :as core]))

(defn start []
  (mount/start))

(defn stop []
  (mount/stop))

(defn restart []
  (stop)
  (start))

(ns user
  (:require [vrpedia.system :as sys]))

(defonce system (atom nil))

(defn start []
  (when-let [s @system]
    (sys/halt! s)
    (reset! system nil))
  (reset! system (sys/start)))

(defn stop []
  (when-let [s @system]
    (sys/halt! s)
    (reset! system nil)))


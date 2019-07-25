(ns vrpedia.core
  (:require [vrpedia.system :as sys])
  (:gen-class))

(defonce system (atom nil))

(defn -main [& args]
  (let [s (sys/start)]
    (reset! system s)))

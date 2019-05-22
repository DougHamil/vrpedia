(ns vrpedia.core
  (:require [threeagent.alpha.core :as th]
            [vrpedia.state :refer [state]]
            [vrpedia.util :refer [map->str]]
            [vrpedia.subject :as subject]))

(defn root []
  [:object
   [:hemisphere-light {:intensity 0.5
                       :position [0 0 10]}]
   [:point-light {:intensity 1.0}]
   [:box {:position [0 5 -10]}]])

(defn init []
  (th/render root
             (.getElementById js/document "root"))
  (when (nil? (:subject @state))
    (subject/search-for-subject! "Boise")))


(defn ^:dev/after-load reload []
  (th/render root
             (.getElementById js/document "root")))


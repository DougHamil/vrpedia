(ns vrpedia.core
  (:require [threeagent.alpha.core :as th]
            [vrpedia.state :refer [state]]
            [vrpedia.util :refer [map->str]]
            [vrpedia.util.virtual-window]
            [vrpedia.subject :as subject]
            [vrpedia.component.core :as components]))

(defn root []
  [:object
   [:hemisphere-light {:intensity 0.5
                       :position [0 0 10]}]
   [:point-light {:intensity 1.0}]
   [:object {:position [0 2 -10]}
    [:object {:position [80 300 -300]
              :rotation [0 3 0]}
     [:text-box {:text "This is some test text for extra testing"
                 :id "test"}]]]])


(defn- init-scene []
  (let [ctx (th/render root
                       (.getElementById js/document "root"))
        renderer (.-renderer ctx)]
    ;; Enable VR
    (set! (.-enabled (.-vr renderer)) true)
    (.setClearColor renderer 0xCCDDFF)
    (.appendChild js/document.body (.createButton js/window.WEBVR renderer))))

(defn init []
  (components/init!)
  (init-scene)
  (comment
    (when (nil? (:subject @state))
      (subject/search-for-subject! "Boise"))))

(defn ^:dev/after-load reload []
  (init-scene))


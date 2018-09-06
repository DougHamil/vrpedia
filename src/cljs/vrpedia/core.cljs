(ns vrpedia.core
  (:require [reagent.core :as r]
            [reagent.session :as session]
            [reagent.impl.template :as reagent-template]
            [vrpedia.util :refer [map->str]]
            [vrpedia.subject :as subject :refer [subject-panel]]))

;; This is a horrible hack to prevent Reagent from camel-casing component name keywords
(defn register-component-name [comp-name]
  (aset reagent-template/custom-prop-name-cache comp-name comp-name))

(let [aframe (.-AFRAME js/window)
      components (js->clj (.-components aframe))]
  (doall (map register-component-name (keys components))))

(defn sky []
  [:a-entity {:sky (map->str {:color "#222"
                              :segments-height "1"
                              :segments-width "1"})}])

(defn user-controller []
  [:a-entity {:laser-controls ""
              :raycaster "objects:.collidable"
              :user-controller ""}])


(defn root []
  [:a-entity
   [sky]
   [user-controller]
   [subject-panel]])

(r/render [root]
          (.getElementById js/document "app"))

(defn on-reload [])

(when (nil? (session/get :subject))
  (subject/search-for-subject! "Boise"))

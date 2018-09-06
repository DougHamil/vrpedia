(ns vrpedia.subject
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require [vrpedia.util :refer [vec3 map->str world-pos obj->clj]]
            [cljs.core.async :refer [<!]]
            [cljs-http.client :as http]
            [reagent.core :as r]
            [reagent.session :as session]))


(def max-subjects 5)

(defn set-subject! [subject]
  (-> subject
      (update :abstract #(clojure.string/replace % #"[:;]" " "))
      (->> (session/put! :subject))))

(defn load-subject! [uri]
  (session/put! :subject nil)
  (go (let [response (<! (http/get (str "/api/entity?uri=" uri)))]
        (set-subject! (:body response)))))

(defn search-for-subject! [term]
  (go (let [response (<! (http/get (str "/api/entity/search?query=" term)))]
        (set-subject! (:body response)))))

(defn related-subject-button [label uri source-button-pos]
  [:a-entity {:on-click #(load-subject! uri)
              :text-button (map->str {:width 2
                                      :text label})}
   [:a-entity {:position (vec3 0 0 -0.1)
               :spline (map->str {:color 0x888888
                                  :offset (vec3 1 0 0)
                                  :mid (vec3 1.5 0 -1)
                                  :target (vec3 source-button-pos)})}]])

(defn related-subject-list [related-subjects source-button]
  (when related-subjects
    (let [source-button-pos (world-pos source-button)]
      [:a-entity {:position (vec3 -2.5 0 0)}
       [:a-entity {:layout (map->str {:type "line"
                                      :plane "yz"
                                      :margin -1.1})}
        (for [subject (take max-subjects related-subjects)]
          [related-subject-button
           (:conceptLabel subject)
           (:Concept subject)
           source-button-pos])]])))

(defn relationship-list []
  (let [select-subjects! (fn [rel evt]
                           (let [btn (-> evt .-target)
                                 relationships (session/get-in [:subject :references])]
                             (session/put! :selected-relationship-button btn)
                             (session/assoc-in! [:subject :selected-subjects] (rel relationships))))]
    (fn []
      [:a-entity
       [:a-entity {:layout (map->str {:type "line"
                                      :plane "yz"
                                      :margin -1.1})}
        (for [[relationship subjects] (take max-subjects(session/get-in [:subject :references]))]
          [:a-entity {:on-click #(select-subjects! relationship %)
                      :text-button (map->str {:width 2
                                              :text (name relationship)})}])]
       [:a-entity {:rotation (vec3 0 30 0)}
        [related-subject-list (session/get-in [:subject :selected-subjects]) (session/get :selected-relationship-button)]]])))

(defn subject-panel []
  [:a-entity
    (let [subject (session/get :subject)
          title-text (or (:label subject) "Loading...")
          abstract-text (or (:abstract subject) "Please wait")
          image-url (:image subject)]
      [:a-entity {:position (vec3 -3.5 4 -8)}
       [:a-entity {:position (vec3 0.1 0.5 0.01)
                   :text-background "paddingX:0.1"
                   :geometry "primitive:plane"
                   :text (map->str {:font "roboto"
                                    :anchor "left"
                                    :width 10
                                    :value title-text})}]
       [:a-entity.collidable {:position (vec3 0 0 0)
                              :grab-scroll ""
                              :text-texture (map->str {:font "roboto"
                                                       :width 12
                                                       :fontSize 64
                                                       :text abstract-text})}]
       (when image-url
         [:a-plane {:fit-texture (map->str {:use-factor true
                                            :pixels-per-unit 200
                                            :max-width 2.0})
                    :material (map->str {:src (str "url(" image-url ")")})
                    :super-anchor (vec3 -0.5 -0.5 0)}])
       [:a-entity {:position (vec3 -3 0 3)
                   :rotation (vec3 0 30 0)}
        [relationship-list]]])])

(ns vrpedia.core
  (:require [threeagent.alpha.core :as th]
            ["layout-bmfont-text" :as bmfont]
            [vrpedia.state :refer [state]]
            [vrpedia.util :refer [map->str]]
            [vrpedia.util.virtual-window]
            [vrpedia.subject :as subject]
            [vrpedia.component.core :as components]
            [cljs.core.async :refer [<!]])
  (:require-macros [cljs.core.async.macros :refer [go]]))

(defn get-text-height [font text width]
  (let [layout (bmfont #js {:font font
                            :text text
                            :width width
                            :align "left"})]
    (.-height layout)))

(defn to-page-chunks [font text width height]
  (let [words (clojure.string/split text #"\n")
        chunks
        (->> words
             (reduce
              (fn [acc c]
                (let [p (str (:page acc) "\n\n\t" c)
                      h (get-text-height font p width)]
                  (if (>= h height)
                    {:pages (conj (:pages acc)
                                  (:page acc))
                     :page (str "..." c)}
                    (assoc acc :page p))))
              {:pages []
               :page ""})
             (doall))]
    (conj (:pages chunks) (:page chunks))))

(defn section->pages [section font width height]
  (let [text (->> section
                  (map #(if (map? %)
                          (:text %)
                          %))
                  (clojure.string/join ""))
        pages (to-page-chunks font text width height)]
    pages))

(defn page [text font page-width page-height]
  (let [page-margin 50
        text-height (get-text-height font text page-width)]
    [:object
     [:plane {:width (+ page-width page-margin)
              :height (+ page-height page-margin)
              :position [(- (/ page-width 2.0)
                            (/ page-margin 2.0))
                         (+ (/ page-height 2.0)
                            page-margin)
                         -3]
              :rotation [0 0 3.14]
              :material {:color 0x222222}}]
     [:plane {:width (+ page-width (* 1.5 page-margin))
              :height (+ page-height (* 1.5 page-margin))
              :position [(- (/ page-width 2.0)
                            (/ page-margin 2.0))
                         (+ (/ page-height 2.0)
                            page-margin)
                         -5]
              :rotation [0 0 3.14]
              :material {:color 0xDEDEDE}}]
     [:text-box {:text text
                 :rotation [0 -3.14 3.14]
                 :position [0 (- page-height text-height) 0]
                 :color 0xEEEEFF
                 :font font
                 :font-texture @(th/cursor state [:font-texture])
                 :width page-width}]]))

(defn root []
  (let [t @(th/cursor state [:time])]
    [:object
     [:hemisphere-light {:intensity 1.0
                         :position [0 0 0]}]
     [:object {:position [0 2 -10]}
      [:object {:position [-60 100 -100]}
       (when-let [pages @(th/cursor state [:pages])]
         (let [font @(th/cursor state [:font])
               page-width 1500
               page-height 2500
               page-scale 0.04]
           [:object
            (for [[i p] (map-indexed vector pages)]
              (let [oi i
                    i (- i (mod t 20))]
                [:object {:position [(* 20 i)
                                     -100
                                     (* -8 (+ i (js/Math.sin ( + t i))))]
                          :rotation [0
                                     0
                                     (* 0.1 (js/Math.sin (+ t oi)))]
                          :scale [page-scale page-scale page-scale]}
                 [page p font page-width page-height]]))]))]]]))

(defn tick [delta-time]
  (swap! state update :time + delta-time))

(defn- init-scene []
  (let [ctx (th/render root
                       (.getElementById js/document "root")
                       {:on-before-render tick})
        renderer (.-renderer ctx)]
    ;; Enable VR
    (set! (.-enabled (.-vr renderer)) true)
    (.setClearColor renderer 0x8877B2)
    (.appendChild js/document.body (.createButton js/window.WEBVR renderer))))

(defn init []
  (components/init!)
  (init-scene)
  (when (nil? (:subject @state))
    (go (let [topic (<! (subject/search-for-subject! "Machine Learning"))
              font (:font @state)
              pages (mapcat #(section->pages % font 1500 2500) (:sections topic))]
          (swap! state assoc :topic topic)
          (swap! state assoc :pages pages)))))

(defn ^:dev/after-load reload []
  (init-scene))


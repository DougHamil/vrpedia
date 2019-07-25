(ns vrpedia.component.text-box
  (:require ["three" :as three]
            ["three-bmfont-text" :as bmfont])
  (:require-macros [threeagent.alpha.macros :refer [defcomponent]]))

(defonce ^:private ^:dynamic *font* nil)
(defonce ^:private ^:dynamic *font-image* nil)

(defonce ^:private canvases (js/Map.))

(defn- get-canvas [id]
  (if-let [c (.get canvases id)]
    c
    (do
      (let [c (js/document.createElement "canvas")]
        (.set canvases id c)
        (.appendChild js/document.body c)
        c))))

(defcomponent :text-box [{:keys [text id]}]
  (if (and *font* *font-image*)
    (let [geo (bmfont #js {:width 300
                           :align "right"
                           :text text
                           :font *font*})
          mat (three/MeshBasicMaterial. #js {:map *font-image*
                                             :transparent true
                                             :color 0xAAFFFFF})]
      (js/console.log geo)
      (js/console.log *font-image*)
      (three/Mesh. geo mat))
    (three/Object3D.)))


(defn init! []
  (-> (js/window.fetch "https://raw.githubusercontent.com/etiennepinchon/aframe-fonts/master/fonts/creepster/Creepster-Regular.json")
      (.then #(.json %))
      (.then (fn [f]
               (set! *font* f))))
  (let [texture-loader (three/TextureLoader.)]
    (.load texture-loader "https://raw.githubusercontent.com/etiennepinchon/aframe-fonts/master/fonts/creepster/Creepster-Regular.png"
           (fn [t]
             (set! *font-image* t)))))

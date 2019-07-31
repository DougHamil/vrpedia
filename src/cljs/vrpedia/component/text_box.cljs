(ns vrpedia.component.text-box
  (:require ["three" :as three]
            ["three-bmfont-text" :as bmfont]
            [vrpedia.state :refer [state]]
            [vrpedia.shaders.msdf :as msdf-shader])
  (:require-macros [threeagent.alpha.macros :refer [defcomponent]]))

(defonce blah-blalh nil)




  (def default-font "didactgothic/DidactGothic-Regular")

(defonce ^:private canvases (js/Map.))

(defn- get-canvas [id]
  (if-let [c (.get canvases id)]
    c
    (do
      (let [c (js/document.createElement "canvas")]
        (.set canvases id c)
        (.appendChild js/document.body c)
        c))))


(defcomponent :text-box [{:keys [text width color opacity font font-texture]}]
  (if (and font font-texture)
    (let [geo (bmfont #js {:width (or width 300)
                           :align "left"
                           :text text
                           :font font})
          color (three/Color. (or color 0xFFFFFF))
          opacity (or opacity 1.0)
          mat (three/RawShaderMaterial. (msdf-shader/create-shader opacity color font-texture))]
      (three/Mesh. geo mat))
    (three/Object3D.)))


(defn init! []
  (-> (js/window.fetch (str "https://raw.githubusercontent.com/etiennepinchon/aframe-fonts/master/fonts/"
                            default-font
                            ".json"))
      (.then #(.json %))
      (.then (fn [f]
               (swap! state assoc :font f))))
  (let [texture-loader (three/TextureLoader.)]
    (.load texture-loader (str "https://raw.githubusercontent.com/etiennepinchon/aframe-fonts/master/fonts/"
                               default-font
                               ".png")
           (fn [t]
             (swap! state assoc :font-texture t)))))



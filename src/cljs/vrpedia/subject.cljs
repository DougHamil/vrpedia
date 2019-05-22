(ns vrpedia.subject
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require [vrpedia.state :refer [state]]
            [cljs.core.async :refer [<!]]
            [cljs-http.client :as http]))


(def max-subjects 5)

(defn set-subject! [subject]
  (-> subject
      (update :abstract #(clojure.string/replace % #"[:;]" " "))
      (->>
       (swap! state assoc :subject))))

(defn load-subject! [uri]
  (swap! state assoc :subject nil)
  (go (let [response (<! (http/get (str "/api/entity?uri=" uri)))]
        (set-subject! (:body response)))))

(defn search-for-subject! [term]
  (go (let [response (<! (http/get (str "/api/entity/search?query=" term)))]
        (set-subject! (:body response)))))

(ns vrpedia.subject
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require [vrpedia.state :refer [state]]
            [cljs.core.async :refer [<! >! chan]]
            [cljs-http.client :as http]))

(defn set-subject! [subject]
  (swap! state assoc :topic subject))

(defn load-subject! [uri]
  (swap! state assoc :subject nil)
  (go (let [response (<! (http/get (str "/api/entity?uri=" uri)))]
        (set-subject! (:body response)))))

(defn search-for-subject! [term]
  (let [c (chan)]
    (go (let [response (<! (http/get (str "/api/entity/search?term=" term)))]
          (>! c (:body response))))
    c))


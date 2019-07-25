(ns vrpedia.topic.wikipedia
  (:require [clj-http.client :as http])
  (:import [org.jsoup Jsoup]))

(defn- fetch-page [url]
  (.get (Jsoup/connect url)))

(defn- get-headline-text [c]
  (-> c
      (.selectFirst ".mw-headline")
      (.text)))

(defn- valid-text? [c]
  (and
   (#{"p" "h2" "h3"} (.tagName c))))

(defn- parse-sections [doc]
  (let [parent (.selectFirst doc ".mw-parser-output")
        children (filter valid-text? (.children parent))]
    (loop [c (first children)
           rs (rest children)
           sections []
           active []]
      (if c
        (let [t (.tagName c)]
          (case t
            "h2" (recur (first rs)
                        (rest rs)
                        (conj sections active)
                        [(get-headline-text c)])
            "h3" (let [s-name (first active)]
                   (recur (first rs)
                          (rest rs)
                          sections
                          (conj active (str s-name " - " (get-headline-text c)))))
            (recur (first rs)
                   (rest rs)
                   sections
                   (conj active (.text c)))))
        {:sections (conj sections active)}))))

;; Scrapes data from wikipedia article
(defn- resolve [wiki-url]
  (let [doc (fetch-page wiki-url)
        sections (parse-sections doc)]
    (def dbg sections)
    {:sections (parse-sections doc)}))

(defn fetch [topic]
  (if-let [wiki-page (:topicOf topic)]
    (merge topic (resolve wiki-page))
    topic))

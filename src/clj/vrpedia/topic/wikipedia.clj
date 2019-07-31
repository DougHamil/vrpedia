(ns vrpedia.topic.wikipedia
  (:require [clj-http.client :as http]
            [hickory.core :as hickory]
            [hickory.select :as s])
  (:import [org.jsoup Jsoup]))

(defn- fetch-page [url]
  (-> (http/get url)
      (:body)
      (hickory/parse)
      (hickory/as-hickory)))

(defn- select-body [doc]
  (->>
   (s/select (s/child
              (s/tag :body)
              (s/id "content")
              (s/id "bodyContent")
              (s/id "mw-content-text")
              (s/class "mw-parser-output"))
             doc)
   (first)))

(defn- select-first-heading [doc]
  (->>
   (s/select (s/child
              (s/tag :body)
              (s/id "content")
              (s/id "firstHeading"))
             doc)
   (first)))

(defn- content->text [c]
  (let [map-fn (fn this-fn [ch]
                 (cond
                   (string? ch) ch
                   :else (clojure.string/join "" (map this-fn (:content ch)))))]
    (->> c
         (:content)
         (map map-fn)
         (clojure.string/join " "))))

(defn- extract-chunk [c]
  (cond
    (= :a (:tag c))
    {:type :link
     :href (:href (:attrs c))
     :text (content->text c)}

    (map? c)
    (content->text c)

    (string? c)
    c))

(defn- extract-section [c]
  (cond
    (map? c) (cond
               (= :h3 (:tag c)) {:type :subsection
                                 :text (content->text c)}
               (= :h2 (:tag c)) (do
                                  (def dbg c)
                                  {:type :section
                                   :text (content->text c)})
               (= :p (:tag c)) (flatten (map extract-chunk (:content c)))
               :else (content->text c))
    (string? c) c))

(defn- join-strings [s]
  (->> s
       (partition-by string?)
       (map (fn [c]
              (if (string? (first c))
                [(clojure.string/join "" c)]
                c)))
       (flatten)))

(defn- extract-sections [doc]
  (let [body (select-body doc)
        first-heading (clojure.string/join "" (:content (select-first-heading doc)))
        chunks (filter #(#{:p :h2 :h3} (get-in % [:tag]))
                       (:content body))
        sections (->> chunks
                      (map extract-section)
                      (flatten)
                      (cons {:type :section
                             :text first-heading}))]
    (->> sections
         (partition-by #(= :section (:type %)))
         (partition 2)
         (map (fn [[s b]]
                (concat s (join-strings b)))))))

;; Scrapes data from wikipedia article
(defn- resolve [wiki-url]
  (let [doc (fetch-page wiki-url)
        sections (extract-sections doc)]
    {:sections sections}))

(defn fetch [topic]
  (if-let [wiki-page (:topicOf topic)]
    (merge topic (resolve wiki-page))
    topic))

(comment
  (let [d (fetch-page "https://en.wikipedia.org/wiki/Boise,_Idaho")
        s (extract-sections d)]
    s))

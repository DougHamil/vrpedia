(ns semantic-web-ws.util
  (:require [clj-http.client :as http]))

(defn resolve-redirects [url]
  (if (or (nil? url) (= "/" (first url)))
    url
    (do
      (println "Resolving" url)
      (let [redirects (:trace-redirects (http/get url))]
        (if (empty? redirects) url (last redirects))))))

(defn default-on-nil-arg [f default-value]
  (fn [arg]
    (if (nil? arg)
      default-value
      (f arg))))

(defn keyword-map [m]
  (into {}
        (for [[k v] m]
          [(keyword k) v])))
(ns semantic-web-ws.util
  (:require [clj-http.client :as http]))
(import org.apache.lucene.analysis.miscellaneous.ASCIIFoldingFilter)

(defn resolve-redirects [url]
  (if (or (nil? url) (= "/" (first url)))
    url
    (do
      (println "Resolving" url)
      (let [redirects (:trace-redirects (http/get url {:throw-exceptions false}))]
        (if (empty? redirects) url (last redirects))))))

(defn default-on-nil-arg [f default-value]
  (fn [arg]
    (if (nil? arg)
      default-value
      (f arg))))

(defn ascii-text [s]
  (let [input-char-array (char-array s)
        output-char-array (make-array Character/TYPE (* 4 (count s)))]
    (do
      (ASCIIFoldingFilter/foldToASCII input-char-array 0 output-char-array 0 (count s))
      (apply str (seq output-char-array)))))

(defn keyword-map [m]
  (into {}
        (for [[k v] m]
          [(keyword k) v])))
(ns vrpedia.util)

(defn vec3
  ([m]
   (str (get m "x") " " (get m "y") " " (get m "z")))
  ([x y z]
   (str x " " y " " z)))

(defn map->str [m]
  (clojure.string/join "; " (map #(str (name (first %)) ":" (second %)) m)))

(defn obj->clj
  [obj]
  (if (goog.isObject obj)
    (-> (fn [result key]
          (let [v (goog.object/get obj key)]
            (if (= "function" (goog/typeOf v))
              result
              (assoc result key (obj->clj v)))))
        (reduce {} (.getKeys goog/object obj)))
    obj))

(defn log [arg]
  (.log js/console arg))

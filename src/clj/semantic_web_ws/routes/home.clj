(ns semantic-web-ws.routes.home
  (:require [semantic-web-ws.layout :as layout]
            [compojure.core :refer [defroutes GET]]
            [ring.util.http-response :as response]
            [clojure.java.io :as io]))

(defn home-page []
  (response/found "/index.html"))

(defn about-page []
  (layout/render "about.html"))

(defn test-page [] "Test2")

(defroutes home-routes
  (GET "/" [] (home-page))
  (GET "/test" [] (test-page))
  (GET "/about" [] (about-page)))


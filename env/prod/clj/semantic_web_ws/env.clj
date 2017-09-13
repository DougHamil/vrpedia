(ns semantic-web-ws.env
  (:require [clojure.tools.logging :as log]))

(def defaults
  {:init
   (fn []
     (log/info "\n-=[semantic-web-ws started successfully]=-"))
   :stop
   (fn []
     (log/info "\n-=[semantic-web-ws has shut down successfully]=-"))
   :middleware identity})

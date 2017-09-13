(ns semantic-web-ws.env
  (:require [selmer.parser :as parser]
            [clojure.tools.logging :as log]
            [semantic-web-ws.dev-middleware :refer [wrap-dev]]))

(def defaults
  {:init
   (fn []
     (parser/cache-off!)
     (log/info "\n-=[semantic-web-ws started successfully using the development profile]=-"))
   :stop
   (fn []
     (log/info "\n-=[semantic-web-ws has shut down successfully]=-"))
   :middleware wrap-dev})

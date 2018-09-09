(defproject vrpedia "0.1.0-SNAPSHOT"

  :description "FIXME: write description"
  :url "http://example.com/FIXME"

  :dependencies [[clj-http "3.7.0"]
                 [clj-time "0.14.0"]
                 [compojure "1.6.0"]
                 [cprop "0.1.11"]
                 [funcool/struct "1.1.0"]
                 [luminus-immutant "0.2.3"]
                 [luminus-nrepl "0.1.4"]
                 [luminus/ring-ttl-session "0.3.2"]
                 [markdown-clj "1.0.1"]
                 [metosin/muuntaja "0.6.0"]
                 [metosin/ring-http-response "0.9.0"]
                 [mount "0.1.11"]
                 [org.clojure/clojure "1.9.0"]
                 [org.clojure/tools.cli "0.3.5"]
                 [org.clojure/tools.logging "0.4.0"]
                 [org.webjars.bower/tether "1.4.0"]
                 [org.webjars/bootstrap "4.0.0-alpha.5"]
                 [org.webjars/font-awesome "4.7.0"]
                 [org.webjars/jquery "3.2.1"]
                 [ring-webjars "0.2.0"]
                 [ring/ring-core "1.6.2"]
                 [ring/ring-defaults "0.3.1"]
                 [selmer "1.11.1"]
                 [seymores/luhn58 "0.1.1"]
                 [yesparql "0.3.1"]
                 [org.apache.lucene/lucene-analyzers-common "6.6.1"]
                 ;; UI
                 [org.clojure/clojurescript "1.10.339"]
                 [figwheel-sidecar "0.5.16"]
                 [cljs-http "0.1.45"]
                 [reagent "0.8.1"]
                 [reagent-utils "0.3.1"]]

  :min-lein-version "2.0.0"

  :jvm-opts ["-server" "-Dconf=.lein-env"]
  :source-paths ["src/clj"]
  :test-paths ["test/clj"]
  :resource-paths ["resources"]
  :target-path "target/%s/"
  :main ^:skip-aot vrpedia.core

  :plugins [[lein-cprop "1.0.3"]
            [lein-cljsbuild "1.1.7"]
            [lein-immutant "2.1.0"]
            [lein-figwheel "0.5.16"]]

  :cljsbuild {:builds {:dev {:source-paths ["src/cljs"]
                             :figwheel {:on-jsload "vrpedia.core/on-reload"}
                             :compiler {:main "vrpedia.core"
                                        :asset-path "/js/out"
                                        :optimizations :none
                                        :source-map true
                                        :pretty-print true
                                        :output-to "resources/public/js/vrpedia.js"
                                        :output-dir "resources/public/js/out"}}}}
  :profiles
  {:uberjar {:omit-source true
             :aot :all
             :uberjar-name "vrpedia.jar"
             :source-paths ["env/prod/clj"]
             :resource-paths ["env/prod/resources"]}

   :dev           [:project/dev :profiles/dev]
   :test          [:project/dev :project/test :profiles/test]

   :project/dev  {:dependencies [[prone "1.1.4"]
                                 [ring/ring-mock "0.3.1"]
                                 [ring/ring-devel "1.6.2"]
                                 [pjstadig/humane-test-output "0.8.2"]]
                  :plugins      [[com.jakemccrary/lein-test-refresh "0.19.0"]]
                  :source-paths ["env/dev/clj"]
                  :resource-paths ["env/dev/resources"]
                  :repl-options {:init-ns user}
                  :injections [(require 'pjstadig.humane-test-output)
                               (pjstadig.humane-test-output/activate!)]}
   :project/test {:resource-paths ["env/test/resources"]}
   :profiles/dev {}
   :profiles/test {}})

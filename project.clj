(defproject vrpedia "0.1.0-SNAPSHOT"

  :dependencies [[clj-http "3.7.0"]
                 [clj-time "0.14.0"]
                 [org.jsoup/jsoup "1.12.1"]
                 [cprop "0.1.11"]
                 [integrant "0.7.0"]
                 [hickory "0.7.1"]
                 [funcool/struct "1.1.0"]
                 [markdown-clj "1.0.1"]
                 [metosin/muuntaja "0.6.4"]
                 [metosin/ring-http-response "0.9.0"]
                 [metosin/reitit "0.3.9"]
                 [com.taoensso/timbre "4.10.0"]
                 [org.clojure/clojure "1.10.1"]
                 [org.clojure/tools.cli "0.3.5"]
                 [org.clojure/tools.logging "0.4.0"]
                 [ring "1.7.1"]
                 [selmer "1.11.1"]
                 [seymores/luhn58 "0.1.1"]
                 [yesparql "0.3.2"]
                 [org.apache.lucene/lucene-analyzers-common "6.6.1"]]

  :min-lein-version "2.0.0"

  :jvm-opts ["-server" "-Dconf=.lein-env"]
  :source-paths ["src/clj"]
  :test-paths ["test/clj"]
  :resource-paths ["resources"]
  :target-path "target/%s/"
  :main ^:skip-aot vrpedia.core

  :plugins [[lein-immutant "2.1.0"]]

  :profiles
  {:uberjar {:omit-source true
             :aot :all
             :uberjar-name "vrpedia.jar"
             :source-paths ["env/prod/clj"]
             :resource-paths ["env/prod/resources"]}

   :dev           [:project/dev :profiles/dev]
   :test          [:project/dev :project/test :profiles/test]

   :project/dev  {:dependencies [[ring/ring-devel "1.6.2"]]
                  :source-paths ["env/dev/clj"]
                  :resource-paths ["env/dev/resources"]
                  :repl-options {:init-ns user}}

   :project/test {:resource-paths ["env/test/resources"]}
   :profiles/dev {}
   :profiles/test {}})

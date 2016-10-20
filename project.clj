(defproject esdh "0.1.0-SNAPSHOT"
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [org.clojure/clojurescript "1.9.229"]
                 [reagent "0.6.0"]
                 [binaryage/devtools "0.8.2"]
                 [re-frame "0.8.0"]
                 [org.clojure/core.async "0.2.391"]
                 [re-com "0.9.0"]
                 [clojurewerkz/ogre "3.0.0.0-beta1"]
                 [org.apache.tinkerpop/tinkergraph-gremlin "3.2.2"]
                 [com.datastax.cassandra/dse-driver "1.1.0"]
                 [org.apache.tinkerpop/gremlin-driver "3.2.2"]
             ;    [yada "1.1.41"]
              ;   [aleph "0.4.1"]
               ;  [bidi "2.0.13"]
                 [cljs-ajax "0.5.8"]
                ; [cheshire "5.5.0"]
                ; [org.freemarker/freemarker "2.3.23"]
                ; [com.itextpdf.tool/xmlworker "5.5.8"]
                 ]
  ;; :imports [[com.itextpdf.text Document DocumentException]
  ;;           [com.itextpdf.text.pdf PdfWriter]
  ;;           [com.itextpdf.tool.xml XMLWorkerHelper]]

  :plugins [[lein-cljsbuild "1.1.4"]]

  :min-lein-version "2.5.3"

  :source-paths ["src/clj"]

  :clean-targets ^{:protect false} ["resources/public/js/compiled" "target"]

  :figwheel {:css-dirs ["resources/public/css"]}

  :profiles
  {:dev
   {:dependencies []

    :plugins      [[lein-figwheel "0.5.7"]]
    }}

  :cljsbuild
  {:builds
   [{:id           "dev"
     :source-paths ["src/cljs"]
     :figwheel     {:on-jsload "esdh.core/mount-root"}
     :compiler     {:main                 esdh.core
                    :output-to            "resources/public/js/compiled/app.js"
                    :output-dir           "resources/public/js/compiled/out"
                    :asset-path           "js/compiled/out"
                    :source-map-timestamp true}}

    {:id           "min"
     :source-paths ["src/cljs"]
     :compiler     {:main            esdh.core
                    :output-to       "resources/public/js/compiled/app.js"
                    :optimizations   :advanced
                    :closure-defines {goog.DEBUG false}
                    :pretty-print    false}}

    ]}

  )

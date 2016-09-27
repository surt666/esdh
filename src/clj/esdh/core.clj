(ns esdh.core
  (:require [clojurewerkz.ogre.core :as q])
  (:import [org.apache.tinkerpop.gremlin.tinkergraph.structure TinkerGraph]
           [com.datastax.driver.dse DseCluster DseSession]
           [com.datastax.driver.dse.graph GraphOptions]))

(def conf {
           "vendor" "dse"
           "hostUri" "ws://localhost:8182/gremlin"  ;; URL of the gremlin server to connect to
           "gremlin.graph" "org.apache.tinkerpop.gremlin.tinkergraph.structure.TinkerGraph"
           "address" "localhost/127.0.0.1:8182"
           "graphName" "esdh"    ;; name of the graph to connect to
           "create" false ;; whether to create graphName if it does not exist
           })

;;{address=localhost/127.0.0.1:8182, hostUri=ws://localhost:8182/gremlin}

(def graph (q/open-graph conf))

(def g (q/traversal graph))

(def cluster (.. DseCluster (builder) (addContactPoint "127.0.0.1") (withGraphOptions (. (GraphOptions.) (setGraphName "esdh"))) (build)))

(def session (.connect cluster))

;;(.. session (executeGraph "g.V()") (one))

;; (.. session (executeGraph "g.V()") (one) (as Vertex) (property "ice-id") (value))

;; (.. session (executeGraph "g.V().has('sag','type','henvendelse')") (one) (as Vertex) (property "ice-id") (value))

(defn get_g_V_count
  "g.V().count()"
  []
  (q/traverse g
              (q/V)
              (q/count)))

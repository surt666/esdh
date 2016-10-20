(ns esdh.core
  (:require [clojurewerkz.ogre.core :as q])
  (:import [org.apache.tinkerpop.gremlin.tinkergraph.structure TinkerGraph]
           [com.datastax.driver.dse DseCluster DseSession]
           [com.datastax.driver.dse.graph GraphOptions]
           [com.datastax.driver.dse.graph GraphStatement  SimpleGraphStatement]))

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

;; g.V().has('ice-id','123').outE('har_bfe').has('tx', lt('2016-02-01')).has('fra',gt('2012-01-01')).has('til',lt('2014-01-01'))

;; (def s (SimpleGraphStatement. "v1 = g.addV(label,'notat','ice-id','345221','dokument',d1)"))
;; #'esdh.core/s
;; esdh.core> (. s (set "d1" (.getBytes "82828")))
;; #object[com.datastax.driver.dse.graph.SimpleGraphStatement 0x53fd1471 "com.datastax.driver.dse.graph.SimpleGraphStatement@53fd1471"]
;; esdh.core> s
;; #object[com.datastax.driver.dse.graph.SimpleGraphStatement 0x53fd1471 "com.datastax.driver.dse.graph.SimpleGraphStatement@53fd1471"]
;; esdh.core> (.. session (executeGraph s))
;; #object[com.datastax.driver.dse.graph.GraphResultSet 0x4ad703be "com.datastax.driver.dse.graph.GraphResultSet@4ad703be"]

(defn opret-notat [ice-id endelig? dokument oprettet]
  (let [s (SimpleGraphStatement. "g.addV(label,'notat','ice-id',i,'dokument',d,'endelig',e,'oprettet',o)")]
    (doto s (.set "e" endelig?) (.set "d" (.getBytes dokument)) (.set "o" oprettet) (.set "i" ice-id))
    (.. session (executeGraph s))))

(defn opret-dokument [ice-id endelig? dokument oprettet mime-type titel]
  "dokument er bytearray"
  (let [s (SimpleGraphStatement. "g.addV(label,'dokument','ice-id',i,'dokument',d,'endelig',e,'oprettet',o,'titel',t,'mime-type',m)")]
    (doto s (.set "e" endelig?) (.set "d" dokument) (.set "o" oprettet) (.set "i" ice-id) (.set "t" titel) (.set "m" mime-type))
    (.. session (executeGraph s))))

(defn opret-sag [ice-id myndighed type oprettet sagsbehandler status]
  "dokument er bytearray"
  (let [s (SimpleGraphStatement. "g.addV(label,'sag','ice-id',i,'myndighed',m,'type',t,'oprettet',o,'sagsbehandler',s)")]
    (doto s (.set "m" myndighed) (.set "t" type) (.set "o" oprettet) (.set "i" ice-id) (.set "s" sagsbehandler))
    (.. session (executeGraph s))))

(defn opret-akt [sags-id ice-id myndighed type oprettet sagsbehandler status]
  "dokument er bytearray"
  (let [s (SimpleGraphStatement. (str "v1 = g.addV(label,'akt','ice-id',i,'myndighed',m,'type',t,'oprettet',o,'sagsbehandler',s).next()\n"
                                      "v2 = g.V().has(label,'sag').has('ice-id',si).next()\n"
                                      "v1.addEdge('tilhører',v2)"))]
    (doto s (.set "m" myndighed) (.set "t" type) (.set "o" oprettet) (.set "i" ice-id) (.set "s" sagsbehandler) (.set "si" sags-id))
    (.. session (executeGraph s))))

(defn get_g_V_count
  "g.V().count()"
  []
  (q/traverse g
              (q/V)
              (q/count)))

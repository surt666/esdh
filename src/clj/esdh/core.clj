(ns esdh.core
  (:require [yada.yada :refer [listener handler resource as-resource]]
            [yada.multipart :refer [find-part part-string part-bytes part-content-type]]
            [yada.swagger :refer [swaggered swagger-spec swagger-spec-resource]]
            [bidi.bidi :refer [tag]]
            [clojure.pprint :refer [pprint]]
            [schema.core :as s]
            [esdh.graphdb :as g]
            [clj-time.core :as t]
            [clj-time.local :as l]
            [clj-uuid :as uuid])
  (:import [java.io ByteArrayInputStream ByteArrayOutputStream]
           [java.util Base64]
           [com.itextpdf.text Document DocumentException]
           [com.itextpdf.text.pdf PdfWriter]
           [com.itextpdf.tool.xml XMLWorkerHelper]))

(def sager
  (resource
   {:produces {:media-type #{"text/plain"
                             "text/html"
                             "application/edn;q=0.9"
                             "application/json;q=0.8"
                             "application/transit+json;q=0.7"}
               :charset "UTF-8"}
    :access-control {:allow-origin "*"}
    :methods

    {:get
     {:parameters {:query {(s/optional-key :id) String}}
      :response
      (fn [ctx]
        (let [id (get-in ctx [:parameters :query :id])]
          (g/find-sager)))}}}))

(def akter
  (resource
   {:produces {:media-type #{"text/plain"
                             "text/html"
                             "application/edn;q=0.9"
                             "application/json;q=0.8"
                             "application/transit+json;q=0.7"}
               :charset "UTF-8"}
    :access-control {:allow-origin "*"}
    :methods
    {:get
     {:parameters {:query {:sags-id String}}
      :response
      (fn [ctx]
        (let [sags-id (get-in ctx [:parameters :query :sags-id])]
          (g/find-akter sags-id)))}}}))

(defn decode [to-decode]
  (String. (.decode (Base64/getDecoder) to-decode)))

(def notat
  (resource
   {:produces {:media-type #{"application/edn;q=0.9"
                             "application/json;q=0.8"
                             "application/transit+json;q=0.7"}
               :charset "UTF-8"}
    :access-control {:allow-origin "*"}
    :methods
    {:get
     {:parameters {:query {:akt-id String}}
      :response
      (fn [ctx]
        (let [akt-id (get-in ctx [:parameters :query :akt-id])
              n (g/find-notat akt-id)
              n-as-s "blaaa"; (apply str (map char (:dokument n)))
              ]
          (prn "NS" n-as-s (type (:dokument n)) (:dokument n))
          (assoc n :dokument n-as-s)))}}}))

(def dokumenter
  (resource
   {:produces {:media-type #{"application/edn;q=0.9"
                             "application/json;q=0.8"
                             "application/transit+json;q=0.7"}
               :charset "UTF-8"}
    :access-control {:allow-origin "*"}
    :methods
    {:get
     {:parameters {:query {:akt-id String}}
      :response
      (fn [ctx]
        (let [akt-id (get-in ctx [:parameters :query :akt-id])
              d (g/find-dokumenter akt-id)]
          d))}}}))

(def open-dok
  (resource
   {:access-control {:allow-origin "*"}
    :produces {:media-type #{"application/pdf" "application/vnd.openxmlformats-officedocument.wordprocessingml.document" "text/html"}
               :charset "UTF-8"}
    :methods
    {:get
     {:parameters {:query {:dok-id String}}
      :response
      (fn [ctx]
        (let [dok-id (get-in ctx [:parameters :query :dok-id])
              d (g/find-dokument dok-id)]
          (decode (first (:dokument d)))))}}}))

(defn opret-sag [data]
  (let [res (g/opret-sag (uuid/v1) (:myndighed data) (:type data) (str (l/local-now)) (:sagsbehandler data))]
    (prn "RES2" res)))

(defn opret-akt [data]
  (prn "JUHU" data)
  (let [res (g/opret-akt (:sags-id data) (uuid/v1) (:myndighed data) (:type data) (str (l/local-now)) (:sagsbehandler data))]
    (prn "RES3" res)))

(defn opret-notat [data]
  (let [res (g/opret-notat (uuid/v1) false (:notat data) (str (l/local-now)) (:akt-id data))]
    (prn "RES" res)))

(def preview
  (resource
   {:consumes {:media-type #{"application/edn;q=0.9"
                             "application/json;q=0.8"
                             "application/transit+json;q=0.7"}}
    :produces {:media-type #{"application/pdf"}
               :charset "UTF-8"}
    :access-control {:allow-origin "*"
                     :allow-headers ["Content-Type"]
                     :allow-methods #{:post}}
    :methods {:post
              {:response (fn [ctx]
                           (let [body (:body ctx)
                                 dok (:data body)
                                 d (Document.)
                                 out (ByteArrayOutputStream.)
                                 writer (PdfWriter/getInstance d out)]
                             (prn "DATA" dok)
                             (.open d)
                             (.. (XMLWorkerHelper/getInstance) (parseXHtml writer d (ByteArrayInputStream. (.getBytes dok))))
                             (.close d)
                             (.toByteArray out) ))}}}))

(def command
  (resource
   {:consumes {:media-type #{"application/edn;q=0.9"
                             "application/json;q=0.8"
                             "application/transit+json;q=0.7"}}
    :produces {:media-type #{"application/pdf"
                             "application/vnd.openxmlformats-officedocument.wordprocessingml.document"
                             "text/plain"
                             "text/html"
                             "application/edn;q=0.9"
                             "application/json;q=0.8"
                             "application/transit+json;q=0.7"}
               :charset "UTF-8"}
    :access-control {:allow-origin "*"
                     :allow-headers ["Content-Type"]
                     :allow-methods #{:post}}
    :methods {:post
              {:response (fn [ctx]
                           (let [body (:body ctx)
                                 command (:command body)
                                 data (:data body)]
                           ;  (prn "C" command data)
                             (cond
                               (= command :preview) (preview data)
                               (= command :opret-sag) (opret-sag data)
                               (= command :opret-akt) (opret-akt data)
                               (= command :gem-notat) (opret-notat data))))}}}))

(def upload
  (resource
   {:consumes [{:media-type #{"multipart/form-data"}}]
    :access-control {:allow-origin "*"
                     :allow-headers ["Content-Type"]
                     :allow-methods #{:post}}
    :produces {:media-type #{"text/plain" "text/html" "application/edn;q=0.9" "application/json;q=0.8" "application/transit+json;q=0.7"}
               :charset "UTF-8"}
    :methods {:post
              {:response (fn [ctx]
                           (let [body (:body ctx)
                                 part (find-part ctx "akt-id")
                                 akt-id (part-string part)
                                 part2 (find-part ctx "files")
                                 bytes (part-bytes part2)
                                 content-type (:name (part-content-type part2))
                                 file-id (uuid/v1)
                                 _ (prn "U" content-type akt-id (String. bytes 0 50) file-id)
                                 filename (get (:params (:content-disposition (get (:body ctx) "files"))) "filename")]
                             (let [res (g/opret-dokument file-id akt-id true bytes (str (l/local-now)) content-type filename)]
                               (assoc (:response ctx) :file-id file-id))))}}}))

(defn routes []
  ["/"
   [["preview" preview]
    ["sager" sager]
    ["akter" akter]
    ["notat" notat]
    ["dokument" open-dok]
    ["dokumenter" dokumenter]
    ["command" command]
    ["upload" upload]]])

(def svr
  (listener
   (routes)
   {:port 3000}))

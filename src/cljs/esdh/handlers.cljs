(ns esdh.handlers
    (:require [re-frame.core :as re-frame]
              [esdh.db :as db]
              [ajax.core :refer [GET POST json-response-format raw-response-format]]))

(re-frame/reg-event-db
 :initialize-db
 (fn [_ _]
   db/default-db))

(re-frame/reg-event-db
 :sag-valgt
 (fn [db [_ sag]]
   (re-frame/dispatch [:find-akter (first (:ice-id sag))])
   (assoc db :sag sag)))

(re-frame/reg-event-db
 :akt-valgt
 (fn [db [_ akt]]
   (re-frame/dispatch [:find-notat (first (:ice-id akt))])
   (re-frame/dispatch [:find-dokumenter (first (:ice-id akt))])
   (assoc db :akt akt)))

(re-frame/reg-event-db
 :dokument-valgt
 (fn [db [_ dok]]
    (assoc db :dok dok)))

(re-frame/reg-event-db
 :save-notat
 (fn [db [_ notat]]
   (let [val (POST "http://localhost:3000/command"
                 {:params {:command :gem-notat
                           :data {:notat notat
                                  :akt-id (first (:ice-id (:akt db)))}}
                  :response-format :edn})]
     (prn "SAVE" val))
   (assoc db :notat val)))

(re-frame/reg-event-db
 :edit-dok
 (fn [db [_ edit?]]
   (assoc db :edit-dok edit?)))

;; (re-frame/reg-event-db
;;  :handle-upload
;;  (fn [db [_ resp]]
;;     (assoc db :upload-dok resp)))
(re-frame/reg-event-db
 :upload-dok
 (fn [db [_ upload?]]
   (prn "UP1")
   (assoc db :upload-dok upload?)))

(re-frame/reg-event-db
 :update-dok
 (fn [db [_ e]]
   (prn "UDD" e)
   (let [akt (:akt db)]
        (re-frame/dispatch [:find-dokumenter (first (:ice-id akt))])
        (assoc db :upload-dok false :loading? false))))

(re-frame/reg-event-db
 :error
 (fn [db [_ res]]
   (.log js/console (str "something bad happened: " res))
   (assoc db :upload-dok false :loading? false)))

;; (defn error-handler [{:keys [status status-text]}]
;;   (.log js/console (str "something bad happened: " status " " status-text)))

(re-frame/reg-event-db
 :upload
 (fn [db [_ input-element]]
   (prn "UP2")
   (let [form-data (let [f-d (js/FormData.)
                         files (.-files input-element)
                         name (.-name input-element)
                         file (aget files 0)]
                     ;; (doseq [file-key (.keys js/Object files)]
                     ;;   (.append f-d name (aget files file-key)))
                     (.append f-d name file)
                     (.append f-d "akt-id" (first (:ice-id (:akt db))))
                     f-d)]
     (POST "http://localhost:3000/upload"
           {:body form-data
            :handler #(re-frame/dispatch [:update-dok %1])
            :error-handler #(re-frame/dispatch [:error %1])})
     (assoc db :loading? true))))

(re-frame/reg-event-db
 :add-sag
 (fn [db [_ add?]]
   (assoc db :add-sag add?)))

(re-frame/reg-event-db
 :add-akt
 (fn [db [_ add?]]
   (assoc db :add-akt add?)))

(re-frame/reg-event-db
 :process-sager
 (fn [db [_ res]]
   (-> db
       (assoc :sager res))))

(re-frame/reg-event-db
 :find-sager
 (fn [db [_]]
   (GET
    "http://localhost:3000/sager"
    {:handler #(re-frame/dispatch [:process-sager %1])
     :response-format (json-response-format {:keywords? true})})
   db))

(re-frame/reg-event-db
 :process-akter
 (fn [db [_ res]]
   (prn "A" res)
   (-> db
       (assoc :akter res))))

(re-frame/reg-event-db
 :find-akter
 (fn [db [_ sags-id]]
   (GET
    "http://localhost:3000/akter"
    {:handler #(re-frame/dispatch [:process-akter %1])
     :params {:sags-id sags-id}
     :response-format (json-response-format {:keywords? true})})
   db))

(re-frame/reg-event-db
 :process-notat
 (fn [db [_ res]]
   (-> db
       (assoc :notat res))))

(re-frame/reg-event-db
 :find-notat
 (fn [db [_ akt-id]]
   (GET
    "http://localhost:3000/notat"
    {:handler #(re-frame/dispatch [:process-notat %1])
     :params {:akt-id akt-id}
     :response-format (json-response-format {:keywords? true})})
   db))

(re-frame/reg-event-db
 :process-dokumenter
 (fn [db [_ res]]
    (-> db
       (assoc :dokumenter res))))

(re-frame/reg-event-db
 :find-dokumenter
 (fn [db [_ akt-id]]
   (GET
    "http://localhost:3000/dokumenter"
    {:handler #(re-frame/dispatch [:process-dokumenter %1])
     :params {:akt-id akt-id}
     :response-format (json-response-format {:keywords? true})})
   db))

(re-frame/reg-event-db
 :add-sags-data
 (fn [db [_ myndighed type]]
   (prn "NT" myndighed)
   (POST "http://localhost:3000/command"
         {:params {:command :opret-sag
                   :data {:myndighed myndighed
                          :type type
                          :sagsbehandler "w19807"}}})
   (re-frame/dispatch [:find-sager])
   (assoc db :add-sag false)))

(re-frame/reg-event-db
 :add-akt-data
 (fn [db [_  myndighed type]]
   (prn "NT" myndighed (first (:ice-id (:sag db))))
   (POST "http://localhost:3000/command"
         {:params {:command :opret-akt
                   :data {:myndighed myndighed
                          :type type
                          :sags-id (first (:ice-id (:sag db)))
                          :sagsbehandler "w19807"}}})
   (re-frame/dispatch [:find-akter (first (:ice-id (:sag db)))])
   (assoc db :add-akt false)))

(re-frame/reg-event-db
 :preview
 (fn [db [_  dokument]]
   (let [x (POST "http://localhost:3000/preview"
                {:params {:data dokument}
                 :response-format (raw-response-format)})]
     (prn "PRE" x))
   db))

(re-frame/reg-event-db
 :search
 (fn [db [_ criteria type]]
   (prn "SEARCH" criteria type)
   db))

(re-frame/reg-event-db
 :send-akt
 (fn [db [_ send?]]
   (let [parter [{:id 1 :navn "Hans Jansen" :type :ejer} {:id 2 :navn "Georg G" :type :rep}]]
     (assoc db :parter parter :send-akt send?))))

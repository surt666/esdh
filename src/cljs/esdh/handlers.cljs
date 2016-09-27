(ns esdh.handlers
    (:require [re-frame.core :as re-frame]
              [esdh.db :as db]))

(re-frame/reg-event-db
 :initialize-db
 (fn [_ _]
   db/default-db))

(re-frame/reg-event-db
 :sag-valgt
 (fn [db [_ sag]]
   (println "HELLO " sag)
   (assoc db :sag sag)))

(re-frame/reg-event-db
 :akt-valgt
 (fn [db [_ akt]]
   (assoc db :akt akt)))

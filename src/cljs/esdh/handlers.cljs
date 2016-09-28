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
   (assoc db :sag sag)))

(re-frame/reg-event-db
 :akt-valgt
 (fn [db [_ akt]]
   (assoc db :akt akt)))

(re-frame/reg-event-db
 :dokument-valgt
 (fn [db [_ dok]]
   (assoc db :dok dok)))

(re-frame/reg-event-db
 :save
 (fn [db [_ type val]]
   (cond
     (= type "notat") (assoc db :akt (assoc (:akt db) :notat val))
     (= type "dok") (assoc db :sok val))))

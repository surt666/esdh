(ns esdh.subs
    (:require-macros [reagent.ratom :refer [reaction]])
    (:require [re-frame.core :as re-frame]))

(re-frame/reg-sub
 :sag
 (fn [db]
   (:sag db)))

(re-frame/reg-sub
 :akt
 (fn [db]
   (:akt db)))

(re-frame/reg-sub
 :dok
 (fn [db]
   (:dok db)))

(re-frame/reg-sub
 :notat
 (fn [db]
   (:notat db)))

(re-frame/reg-sub
 :dokumenter
 (fn [db]
   (:dokumenter db)))

(re-frame/reg-sub
 :sager
 (fn [db]
   (:sager db)))

(re-frame/reg-sub
 :edit-dok
 (fn [db]
   (:edit-dok db)))

(re-frame/reg-sub
 :upload-dok
 (fn [db]
   (:upload-dok db)))

(re-frame/reg-sub
 :akter
 (fn [db]
   (:akter db)))

(re-frame/reg-sub
 :add-sag
 (fn [db]
   (:add-sag db)))

(re-frame/reg-sub
 :add-akt
 (fn [db]
   (:add-akt db)))

(re-frame/reg-sub
 :send-akt
 (fn [db]
   (:send-akt db)))

(re-frame/reg-sub
 :parter
 (fn [db]
   (:parter db)))

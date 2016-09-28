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
 :sager
 (fn [db]
   (:sager db)))

(re-frame/reg-sub
 :edit
 (fn [db]
   (:edit db)))

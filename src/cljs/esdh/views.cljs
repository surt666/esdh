(ns esdh.views
  (:require-macros [reagent.ratom :refer [reaction]])
  (:require [re-frame.core :as re-frame]
            [re-com.core :as re-com]))

(defn sag []
  (let [sag (re-frame/subscribe [:sag])]
    (prn "SA " sag)
    (fn []
      [re-com/v-box
       :gap "10px"
       :children [[re-com/box :child [:h4 "Sag"]]
                  [re-com/h-box
                   :gap "10px"
                   :children [[re-com/label :label (str "Vurderingsejendomsid: " (:vur-ejd-id @sag))]
                              [re-com/label :label (str "Status: " (:status @sag))]
                              [re-com/label :label (str "Frist: " (:frist @sag))]]]
                  [re-com/h-box
                   :gap "10px"
                   :children [[re-com/label :label (str "Myndighed: " (:myndighed @sag))]
                              [re-com/label :label (str "Sagsbehandler: " (:sagsbehandler @sag))]
                              [re-com/label :label (str "Oprettet: " (:oprettet @sag))]]]]])))

(defn dokumenter []
  (let [akt (re-frame/subscribe [:akt])
        dokumenter (reaction (:dokumenter @akt))]
    (prn "D " dokumenter)
    (fn []
      [re-com/v-box
       :gap "10px"
       :children [[re-com/box :child [:h4 "Dokumenter"]]
                  [re-com/box :child [:table {:style {:height "160px" :display "block" :overflow-y "scroll"}} [:thead [:tr [:th "Dokument"] [:th "Titel"] [:th "Oprettet"] [:th "Rolle"]]] [:tbody (for [dok @dokumenter] [:tr [:td [re-com/hyperlink :label (:dokument-id dok)]] [:td (:titel dok)] [:td (:oprettet dok)] [:td (:rolle dok)]])]]]]])))

(defn notat []
  (let [akt (re-frame/subscribe [:akt])
        notat (reaction (:notat @akt))]
    (fn []
      [re-com/v-box
       :gap "10px"
       :children [[re-com/box :child [:h4 "Notat"]]
                  [re-com/box :child [:textarea {:defaultValue @notat}]]]])))

(defn dokument []
  (let [name (re-frame/subscribe [:name])]
    (fn []
      [re-com/v-box
       :gap "10px"
       :children [[re-com/box :child [:h4 "Dokument"]]
                  [re-com/box :child [:textarea {:defaultValue "blolaolrao"}]]]])))

(defn sager []
  (let [sager (re-frame/subscribe [:sager])]
    (fn []
      [re-com/v-box
       :gap "10px"
       :children [[re-com/box :child [:h4 "Sager"]]
                  [re-com/box :child [:table {:style {:height "760px" :display "block" :overflow-y "scroll"}}
                                      (vec (cons :tbody
                                                 (for [sag @sager]
                                                   [:tr [:td [re-com/hyperlink :label (:sags-id sag) :on-click #(re-frame/dispatch [:sag-valgt sag])]]])))]]]])))

(defn akter []
  (let [sag (re-frame/subscribe [:sag])]
    (fn []
      [re-com/v-box
       :gap "10px"
       :children [[re-com/box :child [:h4 "Akter"]]
                  [re-com/box :child [:table {:style {:height "760px" :display "block" :overflow-y "scroll"} :cellPadding "5px" :cellSpacing "5px"} [:tbody {:id "akter" :style {:display (if (nil? @sag) "none" "block")}} (for [akt (:akter @sag)] [:tr [:td [re-com/hyperlink :label (str (:sagsbehandler akt) "  " (:oprettet akt)) :on-click #(re-frame/dispatch [:akt-valgt akt])]]])]]]]])))

(defn main-panel []
  (fn []
    [re-com/h-box
     :height "100%"
     :width "100%"
     :gap "10px"
     :children [[re-com/box
                 :child [sager]
                 :height "800px"
                 :width "200px"
                ]
                [re-com/box
                 :child [akter]
                 :height "800px"
                 :width "200px"
                 ]
                [re-com/v-box
                 :height "800px"
                 :width "auto"
                 :gap "10px"
                 :children [[re-com/box
                             :child [sag]
                             :height "100px"
                             ]
                            [re-com/box
                             :child [notat]
                             :height "100px"
                             ]
                            [re-com/box
                             :child [dokumenter]
                             :height "200px"
                             ]
                            [re-com/box
                             :child [dokument]
                             :height "400px"
                             ]]]]]))
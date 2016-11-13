(ns esdh.views
  (:require-macros [reagent.ratom :refer [reaction]])
  (:require [re-frame.core :as re-frame]
            [re-com.core :as re-com]
            [reagent.core :as reagent]))

(defn sag []
  (let [sag (re-frame/subscribe [:sag])]
    (fn []
      [re-com/v-box
       :gap "10px"
       :children [[re-com/box :child [:h4 "Sag"]]
                  [re-com/h-box
                   :gap "10px"
                   :children [[re-com/label :label (str "Vurderingsejendomsid: " (first (:vur-ejd-id @sag)))]
                              [re-com/label :label (str "Status: " (first (:status @sag)))]
                              [re-com/label :label (str "Frist: " (first (:frist @sag)))]]]
                  [re-com/h-box
                   :gap "10px"
                   :children [[re-com/label :label (str "Myndighed: " (first (:myndighed @sag)))]
                              [re-com/label :label (str "Sagsbehandler: " (first (:sagsbehandler @sag)))]
                              [re-com/label :label (str "Oprettet: " (first (:oprettet @sag)))]]]]])))

(defn akt []
  (let [akt (re-frame/subscribe [:akt])]
    (fn []
      [re-com/v-box
       :gap "10px"
       :children [[re-com/h-box
                   :gap "10px"
                   :align :center
                   :children [[re-com/box :child [:h4 "Akt"]]
                              [re-com/box :child [re-com/hyperlink :label "Send akt" :on-click #(re-frame/dispatch [:send-akt true])]]]]
                  [re-com/h-box
                   :gap "10px"
                   :children [[re-com/label :label (str "Id: " (first (:ice-id @akt)))]
                              [re-com/label :label (str "Frist: " (first (:frist @akt)))]]]
                  [re-com/h-box
                   :gap "10px"
                   :children [[re-com/label :label (str "Myndighed: " (first (:myndighed @akt)))]
                              [re-com/label :label (str "Sagsbehandler: " (first (:sagsbehandler @akt)))]
                              [re-com/label :label (str "Oprettet: " (first (:oprettet @akt)))]]]]])))

(defn dokumenter []
  (let [dokumenter (re-frame/subscribe [:dokumenter])]
    (fn []
      [re-com/v-box
       :gap "10px"
       :children [[re-com/h-box :gap "10px" :align :center :children [[re-com/box :child [:h4 "Dokumenter"]]
                                                                      [re-com/box :child [re-com/hyperlink :label "Tilføj" :on-click #(re-frame/dispatch [:upload-dok true])]]]]
                  [re-com/box :child [:table {:style {:height "160px" :display "block" :overflow-y "scroll"}} [:thead [:tr [:th "Dokument"] [:th "Titel"] [:th "Oprettet"]]] [:tbody (for [dok @dokumenter] [:tr [:td [re-com/hyperlink :label (str (first (:ice-id dok))) :on-click #(re-frame/dispatch [:dokument-valgt dok])]] [:td (str (first (:titel dok)))] [:td (str (first (:oprettet dok)))] [:td [re-com/hyperlink :label "edit" :on-click #(re-frame/dispatch [:edit-dok true])]]])]]]]])))

(defn notat []
  (let [notat (re-frame/subscribe [:notat])]
    (fn []
      [re-com/v-box
       :gap "10px"
       :children [[re-com/h-box :gap "10px" :align :center :children [[re-com/box :child [:h4 "Notat"]]
                                                                      [re-com/box :child [re-com/hyperlink :label "edit" :on-click #(. js/CKEDITOR (replace "notat"))]]
                                                                      [re-com/box :child [re-com/hyperlink :label "preview" :on-click #(re-frame/dispatch [:preview (:dokument @notat)])]]]]
                  [re-com/box :child [:textarea {:id "notat" :rows 5 :cols 20 :value (:dokument @notat)}]]
                  [re-com/button :label "Gem" :on-click #(re-frame/dispatch [:save-notat (let [instances  (aget js/CKEDITOR "instances")
                                                                                               instance (aget instances "notat")]
                                                                                           (.getData instance))])]]])))

(defn dokument []
  (let [dok (re-frame/subscribe [:dok])]
    (fn []
      [re-com/v-box
       :gap "10px"
       :children [[re-com/h-box :gap "10px" :align :center :children [[re-com/box :child [:h4 "Dokument"]]
                                                                      [re-com/box :child [re-com/hyperlink :label "edit" :on-click #(. js/CKEDITOR (replace "doc")); :on-click #(re-frame/dispatch [:edit "doc"])
                                                                                                                               ]]]]
                  [re-com/box :child [:textarea {:id "doc" :readonly "readonly" :rows 5 :cols 20 :value (:rolle @dok)}]]
                  [re-com/button :label "Gem" :on-click #(re-frame/dispatch [:save @dok (aget (.getElementById js/document "doc") "value")])]]])))

(defn sager []
  (let [sager (re-frame/subscribe [:sager])
        criteria-sag ""]
    (fn []
      [re-com/v-box
       :gap "10px"
       :children [[re-com/h-box :gap "10px" :align :center :children [[re-com/box :child [:h4 "Sager"]]
                                                                      [re-com/box :child [re-com/hyperlink :label "Hent" :on-click #(re-frame/dispatch [:find-sager])]]
                                                                      [re-com/box :child [re-com/hyperlink :label "Tilføj" :on-click #(re-frame/dispatch [:add-sag true])]]]]
                  [re-com/h-box :gap "10px" :align :center :children [[re-com/box :child [re-com/input-text :model criteria-sag :on-change #(re-frame/dispatch [:search % :sag]) :status nil :change-on-blur? true :placeholder "Søg"]]]]
                  [re-com/box :child [:table {:style {:height "760px" :display "block" :overflow-y "scroll"}}
                                      (vec (cons :tbody
                                                 (for [sag @sager]
                                                   (do (prn "S" sag)
                                                    [:tr [:td [re-com/hyperlink :label (first (:ice-id sag)) :on-click #(re-frame/dispatch [:sag-valgt sag])]]]))))]]]])))

(defn akter []
  (let [sag (re-frame/subscribe [:sag])
        akter (re-frame/subscribe [:akter])
        criteria-akt ""]
    (fn []
      [re-com/v-box
       :gap "10px"
       :children [[re-com/h-box :gap "10px" :align :center :children [[re-com/box :child [:h4 "Akter"]][re-com/box :child [re-com/hyperlink :label "Tilføj" :on-click #(re-frame/dispatch [:add-akt true])]]]]
                  [re-com/h-box :gap "10px" :align :center :children [[re-com/box :child [re-com/input-text :model criteria-akt :on-change #(re-frame/dispatch [:search % :akt]) :status nil :change-on-blur? true :placeholder "Søg"]]]]
                  [re-com/box :child [:table {:style {:height "760px" :display "block" :overflow-y "scroll"} :cellPadding "5px" :cellSpacing "5px"} [:tbody {:id "akter" :style {:display (if (nil? @sag) "none" "block")}} (for [akt @akter] [:tr [:td [re-com/hyperlink :label (str (first (:sagsbehandler akt)) "  " (first (:oprettet akt))) :on-click #(re-frame/dispatch [:akt-valgt akt])]]])]]]]])))

(defn edit-dok-modal [dok]
  [re-com/v-box
   :gap "10px"
   :margin "20px"
   :children [[re-com/box :child [:h4 "test"]]
              [re-com/box :child [:textarea {:id "edit-dok" :rows 5 :cols 20 :value (:tekst dok) :onClick #(. js/CKEDITOR (replace "edit-dok"))}]]
              [re-com/button :label "Gem" :on-click #(re-frame/dispatch [:save "dok" (aget (.getElementById js/document "edit-dok") "value")])]]])

(defn upload-dok-modal []
  [re-com/v-box
   :gap "10px"
   :margin "20px"
   :children [[re-com/box :child [:h4 "Tilføj dokument"]]
              [re-com/box :child [:input {:type "file" :id "upload-dok" :name "files"}]]
              [re-com/button :label "Gem" :on-click #(re-frame/dispatch [:upload (.getElementById js/document "upload-dok")])]]])

(defn add-sag-modal []
  [re-com/v-box
   :gap "10px"
   :margin "20px"
   :children [[re-com/box :child [:h4 "Tilføj sag"]]
              [re-com/h-box :gap "10px" :children
               [[re-com/box :child [:label "Myndighed"]]
                [re-com/box :child [:input {:type "text" :id "myndighed"}]]
                [re-com/box :child [:label "Type"]]
                [re-com/box :child [:input {:type "text" :id "type"}]]]]
              [re-com/button :label "Gem" :on-click #(re-frame/dispatch [:add-sags-data (aget (.getElementById js/document "myndighed") "value") (aget (.getElementById js/document "type") "value")])]]])

(defn add-akt-modal []
  [re-com/v-box
   :gap "10px"
   :margin "20px"
   :children [[re-com/box :child [:h4 "Tilføj akt"]]
              [re-com/h-box :gap "10px" :children
               [[re-com/box :child [:label "Myndighed"]]
                [re-com/box :child [:input {:type "text" :id "myndighed"}]]
                [re-com/box :child [:label "Type"]]
                [re-com/box :child [:input {:type "text" :id "type"}]]]]
              [re-com/button :label "Gem" :on-click #(re-frame/dispatch [:add-akt-data (aget (.getElementById js/document "myndighed") "value") (aget (.getElementById js/document "type") "value")])]]])

(defn send-akt-modal []
  (let [akt (re-frame/subscribe [:akt])
        parter (re-frame/subscribe [:parter])
        selections (reagent/atom #{})]
    [re-com/v-box
           :gap "10px"
           :margin "20px"
           :children [[re-com/box :child [:h4 "Send akt"]]
                      [re-com/h-box :gap "10px" :children
                       [[re-com/box :child [:label "Parter"]]
                        [re-com/selection-list :choices @parter :model selections :on-change #(reset! selections %) :label-fn :navn  :multi-select? true :requiered? true]]]
                      [re-com/button :label "Gem" :on-click #(re-frame/dispatch [:send-akt-data @selections])]]]))

(defn main-panel []
  (let [edit-dok (re-frame/subscribe [:edit-dok])
        upload-dok (re-frame/subscribe [:upload-dok])
        dok (re-frame/subscribe [:dok])
        add-sag (re-frame/subscribe [:add-sag])
        add-akt (re-frame/subscribe [:add-akt])
        send-akt (re-frame/subscribe [:send-akt])]
    (fn []
      [re-com/h-box
       :height "100%"
       :width "100%"
       :gap "10px"
       :margin "20px"
       :children [[re-com/box
                   :child [sager]
                   :height "800px"
                   :width "300px"
                   ]
                  [re-com/box
                   :child [akter]
                   :height "800px"
                   :width "270px"
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
                               :child [akt]
                               :height "100px"
                               ]
                              [re-com/box
                               :child [notat]
                               :height "auto"
                               ]
                              [re-com/box
                               :child [dokumenter]
                               :height "200px"
                               ]
                              ;; [re-com/box
                              ;;  :child [dokument]
                              ;;  :height "auto"]
                              ]]
                  (when @edit-dok
                    [re-com/modal-panel
                     :backdrop-on-click #(re-frame/dispatch [:edit-dok false])
                     :child (edit-dok-modal @dok)])
                  (when @upload-dok
                    [re-com/modal-panel
                     :backdrop-on-click #(re-frame/dispatch [:upload-dok false])
                     :child (upload-dok-modal)])
                  (when @add-sag
                    [re-com/modal-panel
                     :backdrop-on-click #(re-frame/dispatch [:add-sag false])
                     :child (add-sag-modal)])
                  (when @add-akt
                    [re-com/modal-panel
                     :backdrop-on-click #(re-frame/dispatch [:add-akt false])
                     :child (add-akt-modal)])
                  (when @send-akt
                    [re-com/modal-panel
                     :backdrop-on-click #(re-frame/dispatch [:send-akt false])
                     :child (send-akt-modal)])]])))

(ns esdh.db)

(def default-db
  {:sag nil
   :akt nil
   :dok nil
   :notat ""
   :edit-dok false
   :upload-dok false
   :add-sag false
   :add-akt false
   :sager [{:ice-id [""]}]
   :akter [{:ice-id [""]}]
   :dokumenter [{:ice-id [""]}]})

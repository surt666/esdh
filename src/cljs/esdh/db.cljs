(ns esdh.db)

(def default-db
  {:name "re-frame"
   :sag nil
   :akt nil
   :dok nil
   :sager [{:sags-id "K-929292" :vur-ejd-id 9238923 :status "Afgjort" :frist "11/1/2014" :oprettet "23/12/2013" :sagsbehandler "w81919" :myndighed "SANST"
            :akter [{:notat "oehoteuoehu" :sagsbehandler "w19991" :oprettet "12/1/2015"
                     :dokumenter [{:dokument-id "821827" :titel "Henvendelse om bla" :oprettet "11/2/2014" :rolle  "Sagsbehandler"}
                                  {:dokument-id "821822" :titel "Henvendelse om bla1" :oprettet "11/2/2014" :rolle  "Sagsbehandler"}]}
                    {:notat "oehoteuoehu qeuoeuoe" :sagsbehandler "w19191" :oprettet "12/2/2015"
                     :dokumenter [{:dokument-id "821827" :titel "Henvendelse om bla" :oprettet "11/2/2014" :rolle  "Sagsbehandler"}
                                  {:dokument-id "821822" :titel "Henvendelse om bla1" :oprettet "11/2/2014" :rolle  "Sagsbehandler"}]}]}
           {:sags-id "K-929192" :vur-ejd-id 9238924 :status "Afgjort" :frist "11/1/2014" :oprettet "23/12/2013" :sagsbehandler "w81919" :myndighed "SANST"
            :akter [{:notat "oehoteuoehu" :sagsbehandler "w19991" :oprettet "12/1/2015"
                     :dokumenter [{:dokument-id "821827" :titel "Henvendelse om bla" :oprettet "11/2/2014" :rolle  "Sagsbehandler"}
                                  {:dokument-id "821822" :titel "Henvendelse om bla1" :oprettet "11/2/2014" :rolle  "Sagsbehandler"}]}
                    {:notat "oehoteuoehu qeuoeuoe" :sagsbehandler "w19191" :oprettet "12/2/2015"
                     :dokumenter [{:dokument-id "821827" :titel "Henvendelse om bla" :oprettet "11/2/2014" :rolle  "Sagsbehandler"}
                                  {:dokument-id "821822" :titel "Henvendelse om bla1" :oprettet "11/2/2014" :rolle  "Sagsbehandler"}]}]}
           {:sags-id "V-977292" :vur-ejd-id 9238925 :status "Afgjort" :frist "11/1/2014" :oprettet "23/12/2013" :sagsbehandler "w81919" :myndighed "SANST"
            :akter [{:notat "oehoteuoehu" :sagsbehandler "w19991" :oprettet "12/1/2015"
                     :dokumenter [{:dokument-id "821827" :titel "Henvendelse om bla" :oprettet "11/2/2014" :rolle  "Sagsbehandler"}
                                  {:dokument-id "821822" :titel "Henvendelse om bla1" :oprettet "11/2/2014" :rolle  "Sagsbehandler"}]}
                    {:notat "oehoteuoehu qeuoeuoe" :sagsbehandler "w19191" :oprettet "12/2/2015"
                     :dokumenter [{:dokument-id "821827" :titel "Henvendelse om bla" :oprettet "11/2/2014" :rolle  "Sagsbehandler"}
                                  {:dokument-id "821822" :titel "Henvendelse om bla1" :oprettet "11/2/2014" :rolle  "Sagsbehandler"}]}]}]})

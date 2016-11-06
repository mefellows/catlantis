(ns yimp.db
  (:require [schema.core :as s :include-macros true]))


(s/defschema MenuOption {:id   (s/maybe s/Str)
                         :name s/Str})

; Teacher database definition
(s/defschema Teacher {:id   (s/maybe s/Str)
                      :name s/Str})

; Student database definition
(s/defschema Student s/Any)
; (s/defschema Student {:id   (s/maybe s/Str)
;                       :name s/Str})

(def o s/optional-key)

(s/defschema Image {:id             s/Str
                    :url            s/Str
                    (o :source-url) s/Str
                    (o :sub-id)     s/Str
                    (o :created)    s/Str
                    (o :favorite?)  s/Bool})

(s/defschema User {:username s/Str})

(s/defschema IncidentQuery
  {:incident   (s/maybe s/Any)
   :loading? s/Bool})

(def schema {:menu               [MenuOption]
             :menu-selected      (s/maybe MenuOption)
             :incident-query     IncidentQuery
             :user               (s/maybe User)
             :students           [s/Any]
             :teachers           [s/Any]
             :classrooms         [s/Any]
             :incidents          [s/Any]
             :contacts           [s/Any]
             :locations          [s/Any]
             :actions            [s/Any]
             :summary            [s/Any]
             :preferences        [s/Any]
             :current-incident   s/Any
             :current-student    s/Any
             :current-preference s/Any
             :current-student-incidents   s/Any
             :current-student-classroom   s/Any
             :current-classroom  s/Any
             :sync               s/Bool
             :current-page       s/Keyword
             
             :android  {:drawer s/Any}
                        :shared   {:tab s/Str}
                        :data     s/Any
                        :error    s/Any
                        :loading? s/Bool             
             })

(def app-db
  {:menu               [{:id nil :name "All Categories"}]
   :menu-selected      {:id "1" :name "All"}
   :sync               false
   :incident-query     {:incident nil
                        :loading? false}
   :teachers           []
   :students           [{:ID "test" :FirstName "Matt"}, {:ID "test" :FirstName "Matt"}, {:ID "test" :FirstName "Foo"}]
   :classrooms         []
   :incidents          []
   :contacts           []
   :locations          []
   :actions            []
   :summary            []
   :preferences        []
   :current-incident   nil
   :current-preference nil
   :current-student    nil
   :current-student-incidents []
   :current-classroom    nil
   :current-student-classroom    nil
   :user               {:username "test"}
   :current-page       :incidents
   
   :android  {:drawer nil}
                :shared   {:tab "main"}
                :data     nil
                :error    nil
                :loading? false   
   })

   (set! js/Store (js/require "react-native-store"))

   (defn model [type]
     (.model js/Store (name type)))

   (defn load [type result-fn]
     (let [model (model type)]
       (-> (.find model)
           (.then result-fn))))

   (defn upsert! [type filter data]
     (let [model  (model type)
           filter (clj->js filter)
           data   (clj->js data)]
       (-> (.find model filter)
           (.then (fn [res]
                    (if res
                      (.update model data filter)
                      (.add model data)))))))

   (defn insert! [type data]
     (let [model (model type)]
       (.add model (clj->js data))))

   (defn remove! [type filter]
     (let [model (model type)]
       (.remove model)))

   (defn remove-all! [type]
     (let [model (model type)]
       (.destroy model)))

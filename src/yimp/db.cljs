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

(def schema {:menu        [MenuOption]
             :menu-selected (s/maybe MenuOption)
             :incident-query   IncidentQuery
             :user              (s/maybe User)
             :students          [s/Any]
             :classrooms        [s/Any]
             :incidents         [s/Any]
             :current-incident  s/Any
             :current-student   s/Any ;TODO: fold this into current student!
             :current-student-incidents   s/Any
             :current-classroom s/Any
             :teachers          [Teacher]
             :sync              s/Bool
             :current-page      s/Keyword})

(def app-db
  {:menu               [{:id nil :name "All Categories"}]
   :menu-selected      {:id "1" :name "All"}
   :sync               false
   :incident-query     {:incident nil
                        :loading? false}
   :teachers           []
   :students           [{:ID "test" :FirstName "Matt"}, {:ID "test" :FirstName "Matt"}, {:ID "test" :FirstName "Foo"}]
   :classrooms         [{:id 1 :first_name "Matt" :last_name "foo"},{:id 2 :first_name "Matt" :last_name "foo"}]
   :current-incident   nil
   :current-student    nil
   :current-student-incidents []
   :current-classroom    nil
   :incidents          []
   :user               {:username "test"}
   :current-page       :incidents})

  (ns yimp.db
  (:require [schema.core :as s :include-macros true]))


(s/defschema MenuOption {:id   (s/maybe s/Str)
                         :name s/Str})

; Teacher database definition
(s/defschema Teacher {:id   (s/maybe s/Str)
                      :name s/Str})

; User database definition
(s/defschema User {:username s/Str
                   :id s/Int
                   :password (s/maybe s/Str)})

; Student database definition
(s/defschema Student s/Any)

(def o s/optional-key)

(s/defschema Image {:id             s/Str
                    :url            s/Str
                    (o :source-url) s/Str
                    (o :sub-id)     s/Str
                    (o :created)    s/Str
                    (o :favorite?)  s/Bool})

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
             :authenticated      s/Bool
             :sync               s/Bool
             :current-page       s/Keyword})

(def app-db
  {:menu               [{:id nil :name "All Categories"}]
   :menu-selected      {:id "1" :name "All"}
   :sync               false
   :incident-query     {:incident nil
                        :loading? false}
   :teachers           []
   :students           []
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
   :current-classroom  nil
   :authenticated      false
   :current-student-classroom    nil
   :user               nil
   :current-page       :incidents})

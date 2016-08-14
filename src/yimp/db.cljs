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

(s/defschema ImagesQuery
  {:images   (s/maybe [Image])
   :loading? s/Bool})

(def per-page 5)

(def schema {:menu        [MenuOption]
             :menu-selected (s/maybe MenuOption)
             :images-query      (merge ImagesQuery
                                       {:category (s/maybe MenuOption)
                                        :per-page s/Num})
             :incident-query   ImagesQuery
             :image-selected    (s/maybe Image)
             :random-fact       s/Str
             :user              (s/maybe User)
             :students          [s/Any]
             :teachers          [Teacher]
             :sync              s/Bool
             :current-page      s/Keyword})

(def app-db
  {:menu               [{:id nil :name "All Categories"}]
   :menu-selected      {:id "1" :name "All"}
   :sync               false
   :images-query       {:images   nil
                        :per-page per-page
                        :loading? false
                        :category nil}
   :incident-query     {:images   nil
                        :loading? false}
   :image-selected     nil
   :random-fact        ""
   :teachers           []
   :students           [{:ID "test" :FirstName "Matt"}, {:ID "test" :FirstName "Matt"}, {:ID "test" :FirstName "Foo"}]
   :user               {:username "test"}
   :current-page       :home})

(ns yimp.subs
  (:require-macros [reagent.ratom :refer [reaction]])
  (:require [re-frame.core :refer [register-sub]]
            [yimp.api :as api]
            [yimp.config :as cfg]
            [re-frame.core :as rf]
            [print.foo :as pf :include-macros true]))

(register-sub        ;; a new subscription handler
 :students             ;; usage (subscribe [:students])
 (fn [db]
   ;; extracts the students property from the db
   (reaction (:students @db))))  ;; pulls out :students
   
(register-sub        ;; a new subscription handler
 :teachers             ;; usage (subscribe [:teachers])
 (fn [db]
   ;; extracts the teachers property from the db
   (reaction (:teachers @db))))  ;; pulls out :teachers

(register-sub        ;; a new subscription handler
 :incidents             ;; usage (subscribe [:incidents])
 (fn [db]
   ;; extracts the incidents property from the db
   (reaction (:incidents @db))))  ;; pulls out :incidents

(register-sub
  :current-page
  (fn [db _]
    (reaction
      (:current-page @db))))

(register-sub
  :menu-selected
  (fn [db _]
    (reaction
      (:menu-selected @db))))

(register-sub
  :incident
  (fn [db _]
    (reaction
      (let [{:keys [images loading?]} (:incidents-query @db)]
        [images loading?]))))

(register-sub
  :detail
  (fn [db _]
    (reaction
      (print "some reaction"))))

(register-sub
  :user
  (fn [db _]
    (reaction
      (:user @db))))

(register-sub
  :current-incident
  (fn [db _]
    (reaction
      (:current-incident @db))))

(register-sub
  :current-student
  (fn [db _]
    (reaction
      (:current-student @db))))

(register-sub
  :sync
  (fn [db _]
    (reaction
      (:sync @db))))

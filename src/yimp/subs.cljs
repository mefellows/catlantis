(ns yimp.subs
  (:require-macros [reagent.ratom :refer [reaction]])
  (:require [re-frame.core :refer [register-sub]]
            [yimp.api :as api]
            [yimp.config :as cfg]
            [re-frame.core :as rf]
            [print.foo :as pf :include-macros true]))

(register-sub
  :get-android-drawer
  (fn [db _]
    (reaction
      (get-in @db [:android :drawer]))))

(register-sub
  :get-shared-tab
  (fn [db _]
    (reaction
      (get-in @db [:shared :tab]))))

(register-sub
  :get-data
  (fn [db _]
    (reaction
      (get @db :data))))
                
(register-sub        ;; a new subscription handler
 :students             ;; usage (subscribe [:students])
 (fn [db]
   ;; extracts the students property from the db
   (reaction (:students @db))))  ;; pulls out :students

(register-sub        ;; a new subscription handler
 :preferences             ;; usage (subscribe [:preferences])
 (fn [db]
   ;; extracts the preferences property from the db
   (reaction (:preferences @db))))  ;; pulls out :preferences

(register-sub        ;; a new subscription handler
 :current-student-incidents             ;; usage (subscribe [::current-student-incidents])
 (fn [db]
   ;; extracts the :current-student-incidents property from the db
   (reaction (:current-student-incidents @db))))  ;; pulls out :students

(register-sub        ;; a new subscription handler
 :current-student-classroom             ;; usage (subscribe [::current-student-classroom])
 (fn [db]
   ;; extracts the :current-student-classroom property from the db
   (reaction (:current-student-classroom @db))))  ;; pulls out :students

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

(register-sub        ;; a new subscription handler
 :classes             ;; usage (subscribe [:classes])
 (fn [db]
   ;; extracts the classes property from the db
   (reaction (:classes @db))))  ;; pulls out :classes

(register-sub        ;; a new subscription handler
 :classrooms             ;; usage (subscribe [:classrooms])
 (fn [db]
   ;; extracts the classrooms property from the db
   (reaction (:classrooms @db))))  ;; pulls out :classrooms

(register-sub
  :current-page
  (fn [db _]
    (reaction
      (:current-page @db))))

(register-sub
  :current-classroom
  (fn [db _]
    (reaction
      (:current-classroom @db))))

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
  :current-preference
  (fn [db _]
    (reaction
      (:current-preference @db))))

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

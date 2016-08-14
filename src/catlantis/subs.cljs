(ns catlantis.subs
  (:require-macros [reagent.ratom :refer [reaction]])
  (:require [re-frame.core :refer [register-sub]]
            [catlantis.api :as api]
            [catlantis.config :as cfg]
            [re-frame.core :as rf]
            [print.foo :as pf :include-macros true]))

(register-sub
  :get-greeting
  (fn [db _]
    (reaction
      (get @db :greeting))))

(register-sub
  :categories
  (fn [db _]
    (reaction
      (let [categs (:categories @db)]
        categs))))

(register-sub        ;; a new subscription handler
 :students             ;; usage (subscribe [:students])
 (fn [db]
   ;; extracts the students property from the db
   (reaction (:students @db))))  ;; pulls out :students

(register-sub
  :current-page
  (fn [db _]
    (reaction
      (:current-page @db))))

(register-sub
  :category-selected
  (fn [db _]
    (reaction
      (:category-selected @db))))

(register-sub
  :images
  (fn [db [_ req-category]]
    (reaction
      (let [{:keys [images loading? category]} (:images-query @db)]
        (if (or loading?
                (and images
                     (= category req-category)))
          [images loading?]
          (do (rf/dispatch-sync [:images-load req-category (not= category req-category)])
              [nil loading?]))))))

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
      ('some reaction))))

(register-sub
  :user
  (fn [db _]
    (reaction
      (:user @db))))

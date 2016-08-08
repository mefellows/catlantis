(ns catlantis.subs
  (:require-macros [reagent.ratom :refer [reaction]])
  (:require [re-frame.core :refer [register-sub]]
            [catlantis.api :as api]
            [catlantis.config :as cfg]
            [re-frame.core :as rf]
            [ajax.core :refer [GET]]
            [print.foo :as pf :include-macros true]))

(register-sub
  :get-greeting
  (fn [db _]
    (reaction
      (get @db :greeting))))

; TODO: replace this with lookup to test API call?
(register-sub
  :categories2
  (fn [db _]
    (ajax.core/GET "http://localhost:8000/students")
    {:handler #(rf/dispatch [:process-students-res %1])
     :error-handler #(rf/dispatch-sync [:bad-response %1])}))


(register-sub
  :categories
  (fn [db _]
    (reaction
      (let [categs (:categories @db)]
        ;        (if (<= (count categs) 1)
        ;          (do (api/fetch! :categories cfg/default-catapi-params
        ;                          {:handler #(rf/dispatch [:categories-res %])})
        ;              nil)
        ;          categs)))))
        foo))))

(register-sub
  :categories3
  (fn [db _]
    (reaction
      (let [categs (:categories @db)]
        ;        (if (<= (count categs) 1)
        ;          (do (api/fetch! :categories cfg/default-catapi-params
        ;                          {:handler #(rf/dispatch [:categories-res %])})
        ;              nil)
        ;          categs)))))
        categs))))

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
  :favorites
  (fn [db _]
    (reaction
      (let [{:keys [images loading?]} (:favorites-query @db)]
        [images loading?]))))

(register-sub 
  :detail
  (fn [db _]
    (reaction 
      ("some reaction"))))

(register-sub
  :detail2
  (fn [db _]
    (reaction
      (let [{:keys [image-selected random-fact favorites-query]} @db]
        {:image-selected
         (assoc image-selected :favorite? (contains? (->> (:images favorites-query)
                                                          (map :id)
                                                          set) (:id image-selected)))
         :random-fact
         random-fact}))))

(register-sub
  :user
  (fn [db _]
    (reaction
      (:user @db))))

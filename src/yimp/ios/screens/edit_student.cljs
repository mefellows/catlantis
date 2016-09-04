(ns yimp.ios.screens.edit-student
  (:require [re-frame.core :as rf]
            [reagent.core :as r]
            [clojure.walk :refer [keywordize-keys]]
            [yimp.shared.styles :refer [styles]]
            [clojure.string :as str]
            [yimp.shared.ui :as ui]))

(defn valid-form? [props]
  (let [validation-result (.validate (-> props
                                         (aget "refs")
                                         (aget "form")))]
  (empty? (js->clj (aget validation-result "errors")))))

(defn on-submit [props student]
  (when (valid-form? props)
    (js/console.log (clj->js student))
    (let [value (keywordize-keys (:value student))
          date_of_birth (:date_of_birth value)
          end_time (:end_time value)
          students (into [] (map (fn [i] {:id (int i)}) (:students value)))
          updated (-> value
            (assoc :students students)
            (assoc :date_of_birth (.toISOString (new js/Date date_of_birth))))]
            (js/console.log "converted student object to: " (clj->js updated))
      (rf/dispatch [:save-student updated]))))

(def t (js/require "tcomb-form-native"))
(def Form (r/adapt-react-class (.-Form t.form)))
(def s (.-stylesheet (.-Form t.form)))
(def _ (js/require "lodash"))

; See https://github.com/gcanti/tcomb-form-native/blob/master/lib/stylesheets/bootstrap.js
; for more you can modify.
(def form-style
  (let [stylesheet (_.cloneDeep s)
  updated (-> stylesheet
    (.-controlLabel)
    (.-normal)
    (aset "color" "#444444"))]
    stylesheet))

(def text-area-style
  (let [stylesheet (_.cloneDeep form-style)
        updated (-> stylesheet
                    (.-textbox)
                    (.-normal)
                    (aset "height" 150))]
        stylesheet))

(defn extract-student-enum [student]
  (let [] {(:id student) (str (:first_name student) " " (:last_name student))}))

(defn Student []
  (let [students (rf/subscribe [:students])]
    (->> @students
         (filter #(let [] (> (:id %1) 0)))
         (mapv extract-student-enum)
         (flatten)
         (into {})
         (clj->js)
         (t.enums))))

 (def options
   {:stylesheet form-style
    :order [:first_name :last_name :date_of_birth]
    :editable false
    :fields {:id {:hidden true}
             :date_of_birth {:editable false}
             :first_name {:editable false}
             :last_name {:editable false}}})

(defn student [new?]
  (let [obj {:date_of_birth t.Date
             :first_name t.String
             :last_name t.String}]
    (if-not new?
      (t.struct (clj->js (assoc obj :id t.Number)))
      (t.struct (clj->js obj)))))

; TODO: Move state into GLOBAL app state, not confined to component
(def edit-student
  {:component
   (r/create-class
     {:component-will-mount
      (fn [this]
        (let [student (rf/subscribe [:current-student])
              date_of_birth (:date_of_birth @student)
              ; Convert string to Date objects, and extract student id's
              updated (-> @student
                  (assoc :date_of_birth (js->clj (if (nil? date_of_birth) (new js/Date) (new js/Date date_of_birth)))))]
              (r/set-state this {:value updated})))

      :reagent-render
      (fn [props]
        (this-as this
                 (let [{:keys [value]} (r/state this)]
                 (js/console.log "Updated model: " (clj->js value))
                    [ui/view {:style (:form-container styles)}
                     [ui/scroll-view
                      {:style (:scroll-container styles)}
                      [Form {:ref "form"
                             :type (student (nil? (:id value)))
                             :value value
                             :options options
                             :on-change #(r/set-state this {:value (js->clj %1)})}]
                      [ui/button {:on-press    #(on-submit this (r/state this))
                                  :style       (:submit-btn styles)
                                  :text-style  (:submit-btn-text styles)
                                  :is-disabled #(not (valid-form? props))}
                       "Submit"]]])))})
   :config
   {:screen            :edit-student
    :screen-type       :screen
    :title             "Student"
    :navigator-buttons {:left-buttons
                          [{:icon (js/require "./images/ic_chevron_left.png")
                            :id   :back}]}}
   :on-navigator-event-fn
   (fn [{:keys [id]}]
     (let [id (keyword id)]
       (case id
         :back (rf/dispatch [:nav/pop]))))})

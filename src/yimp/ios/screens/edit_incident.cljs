(ns yimp.ios.screens.edit-incident
  (:require [re-frame.core :as rf]
            [reagent.core :as r]
            [clojure.walk :refer [keywordize-keys]]
            [yimp.shared.styles :refer [styles]]
            [clojure.string :as str]
            [cljs-time.core :as time]
            [cljs-time.format :refer [formatter parse unparse]]
            [yimp.shared.ui :as ui]))

(defn valid-form? [props]
  (let [validation-result (.validate (-> props
                                         (aget "refs")
                                         (aget "form")))]
  (empty? (js->clj (aget validation-result "errors")))))

(defn on-submit [props incident]
  (when (valid-form? props)
    (let [value (keywordize-keys (:value incident))
          ; convert date to string objects
          start_time (:start_time value)
          end_time (:end_time value)
          updated (-> value
            (assoc :start_time (.toISOString (new js/Date start_time)))
            (assoc :end_time (.toISOString (new js/Date end_time))))]
            (js/console.log "converted dates to: " (clj->js updated))
      (rf/dispatch [:save-incident updated]))))

(def t (js/require "tcomb-form-native"))
(def Form (r/adapt-react-class (.-Form t.form)))
(defn extract-student-enum [student]
  (let [] {(int (:id student)) (str (:first_name student) " " (:last_name student))}))

; Turns String into a Date
; Expects format: 2016-08-18T20:41:42Z
(defn time-parser [val]
  (print "time-parser" val)
  (js/console.log  "time-parser" (clj->js val))
  (if-not (nil? val)
    (let []
      (js/console.log (clj->js (parse (formatter "yyyy-MM-dd'T'HH:mm:ssZ") val)))
      (clj->js (parse (formatter "yyyy-MM-dd'T'HH:mm:ssZ") val)))
    (clj->js (time/now))))


; Turn string into correct value!
(defn time-formatter [val]
  (js/console.log "time-formatter: " val)
  (if-not (nil? val)
    (let []
      (js/console.log (clj->js (unparse (formatter "yyyy-MM-dd'T'HH:mm:ssZ") val)))
      (clj->js (unparse (formatter "yyyy-MM-dd'T'HH:mm:ssZ") val)))
    (clj->js (time/now))))

(def date-transformer
  (->> {:format time-parser
        :parse time-formatter}))

(def options
  {:fields {:id {:hidden true}
            :start_time {:format #(str "a date")
                         :transformer date-transformer}
            :end_time {:format #(str "a date")
                         :transformer date-transformer}}})

(def Student
  (let [students (rf/subscribe [:students])]
    (->> @students
         (filter #(let [] (> (:id %1) 0)))
         (mapv extract-student-enum)
         (flatten)
         (into {})
         (clj->js)
         (t.enums))))

(defn incident [new?]
  (let [obj {:start_time (t.maybe t.Date)
             :end_time (t.maybe t.Date)
             :description (t.maybe t.String)
             :follow_up (t.maybe t.Boolean)
             :summary t.String
             :student Student
             :location (t.maybe t.String)}]
    (if-not new?
      (t.struct (clj->js (assoc obj :id t.Number)))
      (t.struct (clj->js obj)))))

; TODO: Move state into GLOBAL app state, not confined to component
(def edit-incident
  {:component
   (r/create-class
     {:component-will-mount
      (fn [this]        
        (let [incident (rf/subscribe [:current-incident])
              start_time (:start_time @incident)
              end_time (:end_time @incident)
          ; Convert string to Date objects
          updated (-> @incident
              (assoc :start_time (js->clj (if (nil? start_time) (new js/Date) (new js/Date start_time))))
              (assoc :end_time (js->clj (if (nil? end_time) (new js/Date) (new js/Date end_time)))))]
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
                             :type (incident (nil? (:id value)))
                              :value value
                              ; :value (merge {:student 1 :summary "test" } value {:start_time (clj->js (new js/Date "1995-12-17T03:24:00")) :end_time (clj->js (new js/Date "2995-12-17T03:24:00"))})
                              ; :value (merge {:student 1 :summary "test" } value {:start_time (clj->js (time/now)) :end_time (clj->js (time/now))})
                             :on-change #(r/set-state this {:value (js->clj %1)})
                             :options options}]
                      [ui/button {:on-press    #(on-submit this (r/state this))
                                  :style       (:submit-btn styles)
                                  :text-style  (:submit-btn-text styles)
                                  :is-disabled #(not (valid-form? props))}
                       "Submit"]]])))})
   :config
   {:screen            :edit-incident
    :screen-type       :screen
    :title             "Create/Edit Incident"
    :navigator-buttons {:left-buttons
                          [{:icon (js/require "./images/ic_chevron_left.png")
                            :id   :back}]}}
   :on-navigator-event-fn
   (fn [{:keys [id]}]
     (let [id (keyword id)]
       (case id
         :back (rf/dispatch [:nav/pop]))))})

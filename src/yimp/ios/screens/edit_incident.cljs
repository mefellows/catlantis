(ns yimp.ios.screens.edit-incident
  (:require [re-frame.core :as rf]
            [reagent.core :as r]
            [clojure.walk :refer [keywordize-keys]]
            [yimp.shared.styles :refer [styles]]
            [clojure.string :as str]
            [yimp.ios.screens.preferences :refer [filtered-preferences]]
            [yimp.shared.ui :as ui]))

(defn valid-form? [props]
  (let [validation-result (.validate (-> props
                                         (aget "refs")
                                         (aget "form")))]
  (empty? (js->clj (aget validation-result "errors")))))

(defn on-submit [props incident]
  (when (valid-form? props)
    (js/console.log (clj->js incident))
    (let [value (keywordize-keys (:value incident))
          start_time (:start_time value)
          end_time (:end_time value)
          students (into [] (map (fn [i] {:id (int i)}) (:students value)))
          updated (-> value
            (assoc :students students)
            (assoc :start_time (.toISOString (new js/Date start_time)))
            (assoc :end_time (.toISOString (new js/Date end_time))))]
            (js/console.log "converted incident object to: " (clj->js updated))
      (rf/dispatch [:save-incident updated])
      (rf/dispatch [:nav/push :incidents]))))

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

(def options
  {:stylesheet form-style
    :order [:summary :location :description :action_taken :students :follow_up :start_time :end_time]
    :fields {:id {:hidden true}
    :students {:item {:label " "
                      :order "asc"}}
    :description {:stylesheet text-area-style
                  :multiline true}}})

(defn extract-student-enum [student]
  (let [] {(:id student) (str (:first_name student) " " (:last_name student))}))

(defn extract-preference-enum [preference]
  (let [] {(:value preference) (str (:value preference))}))

(defn Student []
  (let [students (rf/subscribe [:students])]
    (->> @students
         (filter #(let [] (> (:id %1) 0)))
         (mapv extract-student-enum)
         (flatten)
         (into {})
         (clj->js)
         (t.enums))))

 (defn Preference [type current-value]
   (let [preferences (rf/subscribe [:preferences])
         val (if-not (nil? current-value)
              current-value
              "")]
     (->> (filtered-preferences @preferences type)
          (mapv extract-preference-enum)
          (flatten)
          (cons {(str val) (str val)})
          (into {})
          (clj->js)
          (t.enums))))
          
(defn incident [val]
  (let [new? (nil? (:id val))
        incident (rf/subscribe [:current-incident])
        obj {:start_time t.Date
             :end_time t.Date
             :summary (Preference "summary" (:summary @incident))
             :students (t.list (Student))
             :description (t.maybe t.String)
             :location (Preference "location" (:location @incident))
             :follow_up (t.maybe t.Boolean)
             :action_taken (Preference "action" (:action_taken @incident))}]
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
              end_time   (:end_time @incident)
              students   (:students @incident)
              id         (:id @incident)
              local_id   (:local_id @incident)
          ; Convert string to Date objects, and extract student id's
          updated (-> @incident
              (assoc :students (into [] (map #(:id %1) students)))
              (assoc :local_id (if (and (nil? id) (nil? local_id))
                                   (.now js/Date)
                                   (if-not (nil? local_id) local_id id)))
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
                             :type (incident value)
                             :value value
                             :options options
                             :on-change #(r/set-state this {:value (js->clj %1)})}]
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

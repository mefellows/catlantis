(ns yimp.ios.screens.edit-student
  (:require-macros [natal-shell.data-source :as ds])
  (:require [re-frame.core :as rf]
            [reagent.core :as r]
            [yimp.utils :as u]
            [clojure.walk :refer [keywordize-keys]]
            [yimp.shared.styles :refer [styles]]
            [clojure.string :as str]
            [yimp.shared.ui :as ui]
            [yimp.ios.components.incident-list :refer [incident-list render-incident-row list-view-ds footer]]))

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
                 (let [{:keys [value]} (r/state this)
                       student (rf/subscribe [:current-student])
                       incidents (rf/subscribe [:current-student-incidents])
                       loading (rf/subscribe [:sync])
                       date (new js/Date (:date_of_birth @student))]
                       [ui/scroll-view {:style (:first-item styles)}
                         [ui/view {:style (:readonly-form styles)}
                           [ui/view {:style (:readonly-container styles)}
                            [ui/text {:style (:readonly-label styles)}
                               "Name"]
                            [ui/text {:style (:readonly-value styles)}
                               (str (:first_name @student) " " (:last_name @student))]]
                           [ui/view {:style (:readonly-container styles)}
                            [ui/text {:style (:readonly-label styles)}
                               "Date of birth"]
                            [ui/text {:style (:readonly-value styles)}
                               (.toLocaleDateString date "en-GB")]]
                           [ui/view {:style (:readonly-container styles)}
                            [ui/text {:style (:readonly-label styles)}
                               "Classroom"]
                            [ui/text {:style (:readonly-value styles)}
                               "5/6M"]]
                            [ui/text {:style (:readonly-section-title styles)}
                               "Incidents"]]
                            [incident-list @incidents @loading]])))})
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

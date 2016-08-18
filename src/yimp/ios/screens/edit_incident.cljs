(ns yimp.ios.screens.edit-incident
  (:require [re-frame.core :as rf]
            [reagent.core :as r]
            [clojure.walk :refer [keywordize-keys]]
            [yimp.shared.styles :refer [styles]]
            [clojure.string :as str]
            [yimp.shared.ui :as ui]))

(defn invalid-form? []
  false)

(defn on-submit [incident ]
  (print "submitting incident: " incident)
  (rf/dispatch [:save-incident (keywordize-keys (:value incident))]))

(def t (js/require "tcomb-form-native"))
(def Form (r/adapt-react-class (.-Form t.form)))
(defn incident [new?]

(let [obj {:start_time (t.maybe t.Date)
           :end_time (t.maybe t.Date)
           :description (t.maybe t.String)
           :follow_up (t.maybe t.Boolean)
           :summary t.String
           :student t.Number
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
        (let [incident (rf/subscribe [:current-incident])]
          (r/set-state this {:value @incident})))
      :reagent-render
      (fn [props]
        (this-as this
                 (let [{:keys [value]} (r/state this)
                      ;  sbmt (partial on-submit {}
                      ;                (-> props :config :screen-type))
                                     ]
                    [ui/view {:style (:form-container styles)}
                     [ui/scroll-view
                      {:style (:scroll-container styles)}
                      [Form {:ref "form"
                             :type (incident (empty? value))
                             :value value
                             :on-change #(r/set-state this {:value (js->clj %1)})
                             :options {:fields {:id {:hidden true}}}}]
                      [ui/button {:on-press    #(on-submit (r/state this))
                                  :style       (:submit-btn styles)
                                  :text-style  (:submit-btn-text styles)
                                  :is-disabled (invalid-form?)}
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

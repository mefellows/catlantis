(ns yimp.ios.screens.edit-incident
  (:require [re-frame.core :as rf]
            [reagent.core :as r]
            [yimp.shared.styles :refer [styles]]
            [clojure.string :as str]
            [yimp.shared.ui :as ui]))

(defn invalid-form? []
  false)

(defn on-submit [incident screen-type]
  (print "submitting incident: " incident))
  ; (when-not (invalid-username? (:username user))
  ;   (rf/dispatch [:user-change user])
  ;   (if (= screen-type :modal)
  ;     (rf/dispatch [:nav/pop])
  ;     (rf/dispatch [:nav/push :home]))))

(def t (js/require "tcomb-form-native"))
(def Form (r/adapt-react-class (.-Form t.form)))
(defn incident [new?]

  (let [obj {:start_time (t.maybe t.String)
             :end_time (t.maybe t.String)
             :description (t.maybe t.String)
             :follow_up (t.maybe t.Boolean)
             :summary (t.maybe t.String)
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
                       sbmt (partial on-submit {}
                                     (-> props :config :screen-type))]
                    [ui/view {:style (:form-container styles)}
                     [ui/scroll-view
                      {:style (:scroll-container styles)}
                      [Form {:ref "form"
                             :type (incident (empty? value))
                             :value value
                             :options {:fields {:id {:hidden true}}}}]
                      [ui/button {:on-press    sbmt
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

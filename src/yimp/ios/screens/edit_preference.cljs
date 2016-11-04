(ns yimp.ios.screens.edit-preference
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

(defn on-submit [props preference]
  (when (valid-form? props)
   (js/console.log (clj->js preference))
   (let [value (keywordize-keys (:value preference))]
         (js/console.log "Keywordize keys!!")
         (rf/dispatch [:save-preference value])
         (rf/dispatch [:nav/push :preferences]))))

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

 (def options
   {:stylesheet form-style
    :order [:id :type :value]
    :fields {:id {:hidden true}}})

(defn Types []
  (->> [{:action "Action"} {:summary "Summary" } {:location "Location"} {:contact "Contact"}]
         (flatten)
         (into {})
         (clj->js)
         (t.enums)))

(defn preference [new?]
  (let [obj {:type (Types)
             :value t.String}]
    (if-not new?
      (t.struct (clj->js (assoc obj :id t.Number)))
      (t.struct (clj->js obj)))))

; TODO: Move state into GLOBAL app state, not confined to component
(def edit-preference
  {:component
   (r/create-class
     {:component-will-mount
      (fn [this]
        (let [preference (rf/subscribe [:current-preference])]
          (r/set-state this {:value @preference})))

      :reagent-render
      (fn [props]
        (this-as this
                 (let [{:keys [value]} (r/state this)]
                 (js/console.log "Value => " (clj->js value))
                    [ui/view {:style (:form-container styles)}
                     [ui/scroll-view
                      {:style (:scroll-container styles)}
                      [Form {:ref "form"
                             :type (preference (nil? (:id value)))
                             :value value
                             :options options
                             :on-change #(r/set-state this {:value (js->clj %1)})}]
                      [ui/button {
                        :on-press    #(on-submit this (r/state this))
                                  :style       (:submit-btn styles)
                                  :text-style  (:submit-btn-text styles)
                                  :is-disabled #(not (valid-form? props))}
                       "Submit"]]])))})
   :config
   {:screen            :edit-preference
    :screen-type       :screen
    :title             "Create/Edit Preference"
    :navigator-buttons {:left-buttons
                          [{:icon (js/require "./images/ic_chevron_left.png")
                            :id   :back}]}}
   :on-navigator-event-fn
   (fn [{:keys [id]}]
     (let [id (keyword id)]
       (case id
         :back (rf/dispatch [:nav/pop]))))})

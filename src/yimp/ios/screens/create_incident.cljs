(ns yimp.ios.screens.create-incident
  (:require [re-frame.core :as rf]
            [reagent.core :as r]
            [yimp.shared.styles :refer [styles]]
            [print.foo :as pf :include-macros true]
            [clojure.string :as str]
            [yimp.shared.ui :as ui]))

(defn invalid-username? [username]
  (not (re-matches #"^[A-Za-z0-9]+(?:[ _-][A-Za-z0-9]+)*$" username)))

(defn on-submit [user screen-type]
  (print "submit!"))
  ; (when-not (invalid-username? (:username user))
  ;   (rf/dispatch [:user-change user])
  ;   (if (= screen-type :modal)
  ;     (rf/dispatch [:nav/pop])
  ;     (rf/dispatch [:nav/push :home]))))

(def t (js/require "tcomb-form-native"))
(def Form (r/adapt-react-class (.-Form t.form)))
(def person
  (t.struct (clj->js {:name t.String
                      :surname (t.maybe t.String)
                      :date (t.maybe t.String)
                      :description (t.maybe t.String)
                      :somethingelse (t.maybe t.String)
                      :surname2 (t.maybe t.String)
                      :age t.Number
                      :rememberMe t.Boolean})))

; TODO: Move state into GLOBAL app state, not confined to component
(def create-incident
  {:component
   (r/create-class
     {:component-will-mount
      (fn [this]
        (let [user (rf/subscribe [:user])
              username (:username @user)]
          (r/set-state this {:username username}))
        (print "create incident component mounting"))
      :reagent-render
      (fn [props]
        (this-as this
                 (let [{:keys [username]} (r/state this)
                       sbmt (partial on-submit {:username username}
                                     (-> props :config :screen-type))]
                    [ui/view {:style (:form-container styles)}
                     [ui/scroll-view
                      {:style (:scroll-container styles)}
                      [Form {:ref "form"
                             :type person
                             :options {}}]
                      [ui/button {:on-press    sbmt
                                  :style       (:submit-btn styles)
                                  :text-style  (:submit-btn-text styles)
                                  :is-disabled (invalid-username? username)}
                       "Submit"]]])))})
   :config
   {:screen            :create-incident
      :screen-type       :screen
      :title             "Create Incident"
      :navigator-buttons {:left-buttons
                          [{:icon (js/require "./images/ic_chevron_left.png")
                            :id   :back}]}}
   :on-navigator-event-fn
   (fn [{:keys [id]}]
     (let [id (keyword id)]
       (case id
         :back (rf/dispatch [:nav/pop]))))})

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
                   [ui/touchable-without-feedback
                    {:on-press #(ui/dismiss-keyboard)}
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
                       "Submit"]]]])))})
          
                
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
    
; (def create-incident
;   {:component
;    (r/create-class
;      {:component-will-mount
;        (fn [this]
;          (let [user (rf/subscribe [:user])
;                username (:username @user)]
;            (r/set-state this {:username username}))
;         (print "create incident component mounting"))
;       :reagent-render
;       (fn [props]
;         (this-as this
;           (let [{:keys [username]} (r/state this)
;                 sbmt (partial on-submit {:username username}
;                               (-> props :config :screen-type))]
;              [ui/touchable-without-feedback
;               {:on-press #(ui/dismiss-keyboard)}
;               [ui/view {:style (:form-container styles)}
;                [ui/text
;                  {:style 
;                   (:label-text styles)}
;                 "Some form element"]
;                [ui/text-input
;                 {:style                            (:input styles)
;                  :blur-on-submit                   true
;                  :on-change-text                   #(r/set-state this {:username (str/trim %)})
;                  :default-value                    username
;                  :placeholder                      "Username"
;                  :enables-return-key-automatically true
;                  :auto-correct                     false
;                  :on-submit-editing                sbmt
;                  :auto-capitalize                  :none}]
;                [ui/text
;                  {:style 
;                   (:label-text styles)}
;                 "Some form element2"]                
;                [ui/text-input
;                 {:style                            (:input styles)
;                  :blur-on-submit                   true
;                  :on-change-text                   #(r/set-state this {:password (str/trim %)})
;                  :keyboardType                     "email-address"
;                  :secure-text-entry                true
;                  :default-value                    ""
;                  :placeholder                      "Password"
;                  :enables-return-key-automatically true
;                  :auto-correct                     false
;                  :on-submit-editing                sbmt
;                  :auto-capitalize                  :none}]
;                [ui/button {:on-press    sbmt
;                            :style       (:submit-btn styles)
;                            :text-style  (:submit-btn-text styles)
;                            :is-disabled (invalid-username? username)}
;                 "Submit"]]])))})
;    :config
;    {:screen            :create-incident
;     :screen-type       :screen
;     :title             "Create Incident"
;     :navigator-buttons {:left-buttons
;                         [{:icon (js/require "./images/ic_chevron_left.png")
;                           :id   :back}]}}
;    :on-navigator-event-fn
;    (fn [{:keys [id]}]
;      (let [id (keyword id)]
;        (case id
;          :back (rf/dispatch [:nav/pop]))))})

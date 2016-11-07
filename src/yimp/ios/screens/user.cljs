(ns yimp.ios.screens.user
  (:require [re-frame.core :as rf]
            [reagent.core :as r]
            [yimp.shared.styles :refer [styles]]
            [clojure.walk :refer [keywordize-keys]]
            [print.foo :as pf :include-macros true]
            [yimp.shared.ui :as ui]
            [clojure.string :as str]))

(def t (js/require "tcomb-form-native"))
(def Form (r/adapt-react-class (.-Form t.form)))
(def s (.-stylesheet (.-Form t.form)))
(def _ (js/require "lodash"))

(defn valid-form? [props]
  (let [validation-result (.validate (-> props
    (aget "refs")
    (aget "form")))]
    (empty? (js->clj (aget validation-result "errors")))))

(defn on-submit [props user]
  (when (valid-form? props)
    (let [value (keywordize-keys (:value user))]
      (rf/dispatch [:login value]))))
    

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
    :order [:username :password]
    :fields {:password {:secure-text-entry true}}})

(defn extract-teacher-enum [teacher]
  (let [] {(:id teacher) (str (:first_name teacher) " " (:last_name teacher))}))

(defn Teachers []
  (let [teachers (rf/subscribe [:teachers])]
    (->> @teachers
         (filter #(let [] (> (:id %1) 0)))
         (mapv extract-teacher-enum)
         (flatten)
         (into {})
         (clj->js)
         (t.enums))))

(defn User []
  (let [obj {:username (Teachers)
             :password t.String}]
               (t.struct (clj->js obj))))

; TODO: Move state into GLOBAL app state, not confined to component
(def user
  {:component
   (r/create-class
     {:component-will-mount
      (fn [this]
        (let [user (rf/subscribe [:user])]
          (r/set-state this {:value @user})))

      :reagent-render
      (fn [props]
        (this-as this
                 (let [{:keys [value]} (r/state this)]
                    [ui/view {:style (:form-container styles)}
                     [ui/scroll-view
                      {:style (:scroll-container styles)}
                      [Form {:ref "form"
                             :type (User)
                             :value value                             
                             :options options
                             :on-change #(r/set-state this {:value (js->clj %1)})}]
                      [ui/button {
                        :on-press    #(on-submit this (r/state this))
                                  :style       (:submit-btn styles)
                                  :text-style  (:submit-btn-text styles)
                                  :is-disabled #(not (valid-form? props))}
                       "Login"]]])))})
   :config
   {:screen            :user
    :screen-type       :screen
    :title             "Login"
    :navigator-buttons {}}
   :on-navigator-event-fn
   (fn [{:keys [id]}]
     (let [id (keyword id)]
       (case id
         :back (rf/dispatch [:nav/pop]))))})


; (def user
;   {:component
;    (r/create-class
;      {:component-will-mount
;       (fn [this]
;         (let [user (rf/subscribe [:user])
;               username (:username @user)]
;           (r/set-state this {:username username})))
;       :reagent-render
;       (fn [props]
;         (this-as this
;           (let [{:keys [username]} (r/state this)
;                 sbmt (partial on-submit {:username username}
;                               (-> props :config :screen-type))]
;             [ui/image
;              {:source bg-img
;               :style  (:bg-img styles)}
;              [ui/touchable-without-feedback
;               {:on-press #(ui/dismiss-keyboard)}
;               [ui/view {:style (:login-container styles)}
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
;                 "Submit"]]]])))})
;                
;    :config
;    {:screen          :user
;     :screen-type     :screen
;     :title           "User"
;     :navigator-style {:nav-bar-hidden true}}})

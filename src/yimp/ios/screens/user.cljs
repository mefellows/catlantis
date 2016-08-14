(ns yimp.ios.screens.user
  (:require [re-frame.core :as rf]
            [reagent.core :as r]
            [yimp.shared.styles :refer [styles]]
            [print.foo :as pf :include-macros true]
            [yimp.shared.ui :as ui]
            [clojure.string :as str]))

(def bg-img (js/require "./images/login_bg1.jpg"))

(defn invalid-username? [username]
  (not (re-matches #"^[A-Za-z0-9]+(?:[ _-][A-Za-z0-9]+)*$" username)))

(defn on-submit [user screen-type]
  (when-not (invalid-username? (:username user))
    (rf/dispatch [:user-change user])
    (if (= screen-type :modal)
      (rf/dispatch [:nav/pop])
      (rf/dispatch [:nav/push :home]))))

(def user
  {:component
   (r/create-class
     {:component-will-mount
      (fn [this]
        (let [user (rf/subscribe [:user])
              username (:username @user)]
          (r/set-state this {:username username})))
      :reagent-render
      (fn [props]
        (this-as this
          (let [{:keys [username]} (r/state this)
                sbmt (partial on-submit {:username username}
                              (-> props :config :screen-type))]
            [ui/image
             {:source bg-img
              :style  (:bg-img styles)}
             [ui/touchable-without-feedback
              {:on-press #(ui/dismiss-keyboard)}
              [ui/view {:style (:login-container styles)}
               [ui/text-input
                {:style                            (:input styles)
                 :blur-on-submit                   true
                 :on-change-text                   #(r/set-state this {:username (str/trim %)})
                 :default-value                    username
                 :placeholder                      "Username"
                 :enables-return-key-automatically true
                 :auto-correct                     false
                 :on-submit-editing                sbmt
                 :auto-capitalize                  :none}]
               [ui/text-input
                {:style                            (:input styles)
                 :blur-on-submit                   true
                 :on-change-text                   #(r/set-state this {:password (str/trim %)})
                 :keyboardType                     "email-address"
                 :secure-text-entry                true
                 :default-value                    ""
                 :placeholder                      "Password"
                 :enables-return-key-automatically true
                 :auto-correct                     false
                 :on-submit-editing                sbmt
                 :auto-capitalize                  :none}]
               [ui/button {:on-press    sbmt
                           :style       (:submit-btn styles)
                           :text-style  (:submit-btn-text styles)
                           :is-disabled (invalid-username? username)}
                "Submit"]
               ]]])))})
   :config
   {:screen          :user
    :screen-type     :screen
    :title           "User"
    :navigator-style {:nav-bar-hidden true}}})

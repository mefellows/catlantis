(ns yimp.shared.ui
  (:require-macros [natal-shell.layout-animation :as la]
                   [natal-shell.dimensions :as dim])
  (:require [reagent.core :as r]
            [re-frame.core :as rf]
            [print.foo :as pf :include-macros true]
            [medley.core :as m]
            [yimp.utils :as u]
            [yimp.colors :refer [colors]]
            [yimp.config :as cfg]
            [camel-snake-kebab.core :as cs :include-macros true]))

(def ReactNative (js/require "react-native"))
; (set! js/window.React (js/require "react-native"))

(def text (r/adapt-react-class (.-Text ReactNative)))
(def view (r/adapt-react-class (.-View ReactNative)))
(def scroll-view (r/adapt-react-class (.-ScrollView ReactNative)))
(def image (r/adapt-react-class (.-Image ReactNative)))
(def refresh-control (r/adapt-react-class (.-RefreshControl ReactNative)))
(def touchable-highlight (r/adapt-react-class (.-TouchableHighlight ReactNative)))
(def touchable-opacity (r/adapt-react-class (.-TouchableOpacity ReactNative)))
(def touchable-without-feedback (r/adapt-react-class (.-TouchableWithoutFeedback ReactNative)))
(def list-view (r/adapt-react-class (.-ListView ReactNative)))
(def activity-indicator-ios (r/adapt-react-class (.-ActivityIndicatorIOS ReactNative)))
(def text-input (r/adapt-react-class (.-TextInput ReactNative)))
(def button (r/adapt-react-class (js/require "apsl-react-native-button")))
(def LinkingIOS (.-LinkingIOS ReactNative))
(def dismiss-keyboard (js/require "dismissKeyboard"))
(def keyboard-avoiding-view (r/adapt-react-class (.-KeyboardAvoidingView ReactNative)))
(def EStyleSheet (aget (js/require "react-native-extended-stylesheet") "default"))

(defn get-window
  ([] (get-window identity))
  ([f]
   (f (u/js->cljk (dim/get "window")))))

(defn build-stylesheet
  ([] (build-stylesheet {}))
  ([vals]
   (.build EStyleSheet (clj->js vals))))

(defn create-stylesheet [styles]
  (-> (m/map-vals #(u/apply-if map? (partial m/map-keys cs/->camelCase) %) styles)
      clj->js
      (->> (.create EStyleSheet))
      u/obj->hash-map))

(build-stylesheet)

(defn open-url [url]
  (.openURL LinkingIOS url))

(defn alert [title]
  (.alert (.-Alert ReactNative) title))

(defn anim-preset
  ([kw] (anim-preset kw {}))
  ([kw override]
   (-> (aget (la/presets) (name kw))
       (js->clj :keywordize-keys true)
       (merge override)
       clj->js)))

(def color colors)
(def add-icon (js/require "./images/ic_add.png"))
(def sync-icon (js/require "./images/ic_cached.png"))
(def close-icon (js/require "./images/ic_close.png"))
(def menu-icon (js/require "./images/ic_menu.png"))

(def navigator-buttons
  {:right-buttons
   [{:id   :create-incident
     :icon add-icon}]
   :left-buttons
   [{:icon menu-icon
     :id   :menu}
    {:id   :sync
     :icon sync-icon}]})

(defn navigator-events [{:keys [id]}]
  (let [id   (keyword id)]
    (case id
      :menu (rf/dispatch [:nav/toggle-drawer])
      :sync (rf/dispatch [:synchronise])
      :create-incident (rf/dispatch [:create-incident])
      :user (rf/dispatch [:nav/push id {:screen-type :modal}])
      (rf/dispatch [:nav/push id]))))

(defn navigator [screen title]
  {:screen            screen
   :screen-type       :screen
   :title             title
   :navigator-buttons navigator-buttons})

(defn create-screen [screen title render-fn]
  {:component
   (r/create-class
     {:reagent-render
      render-fn})
   :config
   (navigator screen title)
   :on-navigator-event-fn
   navigator-events})

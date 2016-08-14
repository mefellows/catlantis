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

(set! js/window.React (js/require "react-native"))

(def text (r/adapt-react-class (.-Text js/React)))
(def view (r/adapt-react-class (.-View js/React)))
(def scroll-view (r/adapt-react-class (.-ScrollView js/React)))
(def image (r/adapt-react-class (.-Image js/React)))
(def touchable-highlight (r/adapt-react-class (.-TouchableHighlight js/React)))
(def touchable-opacity (r/adapt-react-class (.-TouchableOpacity js/React)))
(def touchable-without-feedback (r/adapt-react-class (.-TouchableWithoutFeedback js/React)))
(def list-view (r/adapt-react-class (.-ListView js/React)))
(def activity-indicator-ios (r/adapt-react-class (.-ActivityIndicatorIOS js/React)))
(def text-input (r/adapt-react-class (.-TextInput js/React)))
(def list-item (r/adapt-react-class (js/require "react-native-listitem")))
(def image-progress (r/adapt-react-class (js/require "react-native-image-progress")))
(def keyboard-spacer (r/adapt-react-class (js/require "react-native-keyboard-spacer")))
(def button (r/adapt-react-class (js/require "apsl-react-native-button")))
(def LinkingIOS (.-LinkingIOS js/React))
(def dismiss-keyboard (js/require "dismissKeyboard"))
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
  (.alert (.-Alert js/React) title))

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

(defn navigator [screen]
  {:screen          screen
   :screen-type       :screen
   :title             cfg/app-name
   :navigator-buttons {:right-buttons
                       [{:id   :create-incident
                         :icon add-icon}]
                       :left-buttons
                       [{:icon menu-icon
                         :id   :menu}
                        {:id   :sync
                         :icon sync-icon}]}
})

(defn navigator-events [{:keys [id]}]
    (let [id (keyword id)]
      (case id
        :menu (rf/dispatch [:nav/toggle-drawer])
        :sync (rf/dispatch [:synchronise])
        :user (rf/dispatch [:nav/push id {:screen-type :modal}])
        (rf/dispatch [:nav/push id]))))
    
(defn create-screen [screen render-fn]
  {:component
   (r/create-class
     {:reagent-render
      render-fn})
   :config 
     (navigator screen)
   :on-navigator-event-fn
    navigator-events})    

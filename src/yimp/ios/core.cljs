(ns yimp.ios.core
  (:require [reagent.core :as r :refer [atom]]
            [re-frame.core :refer [subscribe dispatch dispatch-sync]]
            [yimp.subs]))

(def ReactNative (js/require "react-native"))

(def app-registry (.-AppRegistry ReactNative))
(def text (r/adapt-react-class (.-Text ReactNative)))
(def view (r/adapt-react-class (.-View ReactNative)))
(def image (r/adapt-react-class (.-Image ReactNative)))
(def touchable-highlight (r/adapt-react-class (.-TouchableHighlight ReactNative)))

(def logo-img (js/require "./images/cljs.png"))

(defn alert [title]
      (.alert (.-Alert ReactNative) title))

(defn app-root []
  ; (let [greeting (subscribe [:get-greeting])]
  (let [greeting "aoeu"]
    (fn []
      [view {:style {:flex-direction "column" :margin 40 :align-items "center"}}
       [text {:style {:font-size 30 :font-weight "100" :margin-bottom 20 :text-align "center"}} greeting]
       [image {:source logo-img
               :style  {:width 80 :height 80 :margin-bottom 30}}]
       [touchable-highlight {:style {:background-color "#999" :padding 10 :border-radius 5}
                             :on-press #(alert "HELLO!")}
        [text {:style {:color "white" :text-align "center" :font-weight "bold"}} "press me"]]])))

(defn init []
      ; (dispatch-sync [:initialize-db])
      (.registerComponent app-registry "yimp" #(r/reactify-component app-root)))


; (ns yimp.ios.core
;   (:require [print.foo :as pf :include-macros true]
;             [schema.core :as s :include-macros true]
;             [re-frame.core :as rf]
;             [yimp.db]
;             [yimp.handlers]
;             [yimp.subs]
;             [yimp.shared.navigation :as nav]
;             [yimp.ios.screens.home :refer [home]]
;             [yimp.ios.screens.teachers :refer [teachers]]
;             [yimp.ios.screens.students :refer [students]]
;             [yimp.ios.screens.incidents :refer [incidents]]
;             [yimp.ios.screens.detail :refer [detail]]
;             [yimp.ios.screens.menu :refer [menu]]
;             [yimp.ios.screens.edit-incident :refer [edit-incident]]
;             [yimp.ios.screens.user :refer [user]]
;             [yimp.shared.ui :as ui]))
; 
; (s/set-fn-validation! goog.DEBUG)
; (def nav-content-color (ui/color :orange800))
; 
; (defn app-root []
;   (let [page (rf/subscribe [:current-page])
;         u (rf/subscribe [:user])
;         username (:username @u)]
;     (nav/register-screen! home)
;     (nav/register-screen! teachers)
;     (nav/register-screen! students)
;     (nav/register-screen! incidents)
;     (nav/register-screen! detail)
;     (nav/register-screen! edit-incident)
;     (nav/register-screen! user)
;     (nav/register-reagent-component! :menu menu)
;     (nav/start-single-screen-app!
;       {:screen          (if (not= username "") @page :user)
;        :drawer          {:left                 {:screen :menu}
;                          :disable-open-gesture true}
;        :persist-state?  true
;        :animationType   :fade
;        :navigator-style {:nav-bar-blur         true
;                          :draw-under-nav-bar   true
;                          :nav-bar-button-color nav-content-color
;                         ;  :nav-bar-background-color "#efefef"
;                          :nav-bar-text-color   nav-content-color}})))
; 
; (defn init []
;   (rf/dispatch-sync [:initialize-db])
;   (app-root)
;   (rf/dispatch [:load-students])
;   (rf/dispatch [:load-incidents]))

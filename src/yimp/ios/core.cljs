(ns yimp.ios.core
  (:require [print.foo :as pf :include-macros true]
            [schema.core :as s :include-macros true]
            [re-frame.core :as rf]
            [yimp.db]
            [yimp.handlers]
            [yimp.subs]
            [yimp.shared.navigation :as nav]
            [yimp.ios.screens.home :refer [home]]
            [yimp.ios.screens.teachers :refer [teachers]]
            [yimp.ios.screens.students :refer [students]]
            [yimp.ios.screens.incidents :refer [incidents]]
            [yimp.ios.screens.detail :refer [detail]]
            [yimp.ios.screens.categories :refer [categories]]
            [yimp.ios.screens.create-incident :refer [create-incident]]
            [yimp.ios.screens.user :refer [user]]
            [yimp.shared.ui :as ui]))

(s/set-fn-validation! goog.DEBUG)
(def nav-content-color (ui/color :grey900))

(defn init-nav []
  (let [page (rf/subscribe [:current-page])
        u (rf/subscribe [:user])
        username (:username @u)]
    (nav/register-screen! home)
    (nav/register-screen! teachers)
    (nav/register-screen! students)
    (nav/register-screen! incidents)
    (nav/register-screen! detail)
    (nav/register-screen! create-incident)
    (nav/register-screen! user)
    (nav/register-reagent-component! :categories categories)
    (nav/start-single-screen-app!
      {:screen          (if (not= username "") @page :user)
       :drawer          {:left                 {:screen :categories}
                         :disable-open-gesture true}
       :persist-state?  true
       :animationType   :fade
       :navigator-style {:nav-bar-blur         true
                         :draw-under-nav-bar   true
                         :nav-bar-button-color nav-content-color
                         :nav-bar-background-color "#efefef"
                         :nav-bar-text-color   nav-content-color}})))

(defn init []
  (rf/dispatch-sync [:initialize-db])
  (init-nav)
  (rf/dispatch [:load-students]))

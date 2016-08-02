(ns catlantis.ios.core
  (:require [print.foo :as pf :include-macros true]
            [schema.core :as s :include-macros true]
            [re-frame.core :as rf]
            [catlantis.db]
            [catlantis.handlers]
            [catlantis.subs]
            [catlantis.shared.navigation :as nav]
            [catlantis.ios.screens.home :refer [home]]
            [catlantis.ios.screens.detail :refer [detail]]
            [catlantis.ios.screens.categories :refer [categories]]
            [catlantis.ios.screens.favorites :refer [favorites]]
            [catlantis.ios.screens.user :refer [user]]
            [catlantis.shared.ui :as ui]))

(s/set-fn-validation! goog.DEBUG)
(def nav-content-color (ui/color :deep-purple900))

(defn init-nav []
  (let [page (rf/subscribe [:current-page])
        u (rf/subscribe [:user])
        username (:username @u)]
    (nav/register-screen! home)
    (nav/register-screen! detail)
    (nav/register-screen! favorites)
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
                         :nav-bar-text-color   nav-content-color}})))

(defn init []
  (rf/dispatch-sync [:initialize-db])
  (init-nav))



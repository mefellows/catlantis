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
            [yimp.ios.screens.classrooms :refer [classrooms]]
            [yimp.ios.screens.students :refer [students]]
            [yimp.ios.screens.incidents :refer [incidents]]
            [yimp.ios.screens.detail :refer [detail]]
            [yimp.ios.screens.menu :refer [menu]]
            [yimp.ios.screens.edit-incident :refer [edit-incident]]
            [yimp.ios.screens.edit-student :refer [edit-student]]
            [yimp.ios.screens.edit-classroom :refer [edit-classroom]]
            [yimp.ios.screens.user :refer [user]]
            [yimp.shared.ui :as ui]))

(s/set-fn-validation! goog.DEBUG)
(def nav-content-color (ui/color :orange800))

(defn init-nav []
  (let [page (rf/subscribe [:current-page])
        u (rf/subscribe [:user])
        username (:username @u)]
    (nav/register-screen! home)
    (nav/register-screen! teachers)
    (nav/register-screen! students)
    (nav/register-screen! incidents)
    (nav/register-screen! classrooms)
    (nav/register-screen! detail)
    (nav/register-screen! edit-incident)
    (nav/register-screen! edit-student)
    (nav/register-screen! edit-classroom)
    (nav/register-screen! user)
    (nav/register-reagent-component! :menu menu)
    (nav/start-single-screen-app!
      {:screen          (if (not= username "") @page :user)
       :drawer          {:left                 {:screen :menu}
                         :disable-open-gesture true}
       :persist-state?  true
       :animationType   :fade
       :navigator-style {:nav-bar-blur         true
                         :draw-under-nav-bar   true
                         :nav-bar-button-color nav-content-color
                        ;  :nav-bar-background-color "#efefef"
                         :nav-bar-text-color   nav-content-color}})))

(defn init []
  (rf/dispatch-sync [:initialize-db])
  (init-nav)
  (rf/dispatch [:load-students])
  (rf/dispatch [:load-classrooms])
  (rf/dispatch [:load-teachers])
  (rf/dispatch [:load-incidents]))

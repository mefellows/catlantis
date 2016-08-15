(ns yimp.ios.components.student-list
  (:require-macros [natal-shell.data-source :as ds])
  (:require [yimp.shared.ui :as ui]
            [yimp.shared.styles :refer [styles]]
            [yimp.utils :as u]
            [re-frame.core :as rf]
            [reagent.core :as r]
            [print.foo :as pf :include-macros true]
            [yimp.config :as cfg]
            [yimp.config :refer [app-name]]))

(def list-view-ds (ds/data-source {:rowHasChanged #(not= %1 %2)}))

(defn render-student-row [{:keys [FirstName ID] :as student}]
   [ui/view
     [ui/text
      {:style (:image-text styles)}
      FirstName]])

(defn footer [loading?]
  (when loading?
    [ui/view
     {:style (:loading-wrap styles)}
     [ui/activity-indicator-ios
      {:style (:indicator styles)}]]))

(defn student-list [students]
  [ui/scroll-view
   {:style (:container styles)}
   [ui/list-view (merge
                   {:dataSource    (ds/clone-with-rows list-view-ds students)
                    :render-row    (comp r/as-element render-student-row u/js->cljk)
                    :style         (merge-with (:container styles) (:first-item styles))
                    :render-footer (comp r/as-element (partial footer true))}
                   {})]])

(ns yimp.ios.components.students-list
  (:require-macros [natal-shell.data-source :as ds])
  (:require [yimp.shared.ui :as ui]
            [yimp.utils :as u]
            [re-frame.core :as rf]
            [reagent.core :as r]
            [print.foo :as pf :include-macros true]
            [yimp.config :as cfg]
            [yimp.config :refer [app-name]]))

(declare styles)

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

(defn students-list [students]
   [ui/list-view (merge
                   {:dataSource    (ds/clone-with-rows list-view-ds students)
                    :render-row    (comp r/as-element render-student-row u/js->cljk)
                    :style         (:container styles)
                    :render-footer (comp r/as-element (partial footer true))}
                   {})])

(def star-icon (js/require "./images/star.png"))

(defn students-list2 []
  [ui/scroll-view
    {:style (:container styles)}
  [ui/view
   {:style (merge-with (:text-wrap styles) (:first-item styles))}
   [ui/text
    {:style (:image-text styles)}
    "Student 1"]]
  [ui/view
   {:style (:text-wrap styles)}
   [ui/text
    {:style (:image-text styles)}
    "Student 2"]]
  [ui/view
   {:style (:text-wrap styles)}
   [ui/text
    "Student 3"]]
  ])

 (def styles
   (ui/create-stylesheet
     {:container    {:flex             1
                     :background-color :transparent
                     :flex-direction   :row}
      :text         {:color "white" :text-align "center" :font-weight "bold"}
      :first-item   {:margin-top 100}
      :image-detail {:flex       1
                     :height     "60%"
                     :width      "100%"
                     :margin-top 20}
      :buttons-wrap {:flex-direction  "row"
                     :justify-content :space-between
                     :margin-top      0
                     :padding-left    20
                     :padding-right   20}
      :text-wrap    {:justify-content :center
                     :align-items     :center
                     :flex 1
                     :margin-top      20}
      :source-link  {:text-align :right
                     :color      (ui/color :grey400)
                     :width      "90%"
                     :height     20
                     :font-size  12}
      :image-text   {:text-align :center
                     :color      (ui/color :dark-black)
                     :width      "90%"
                     :height     "15%"}}))

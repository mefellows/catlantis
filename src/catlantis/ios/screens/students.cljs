(ns catlantis.ios.screens.students
  (:require [re-frame.core :as rf]
            [catlantis.shared.ui :as ui]
            [reagent.core :as r]
            [print.foo :as pf :include-macros true]
            [catlantis.config :refer [app-name]]
            [catlantis.config :as cfg]
            [catlantis.ios.components.students-list :refer [students-list]]))

(declare styles)

(rf/dispatch [:load-students])

(def star-icon (js/require "./images/star.png"))
(def close-icon (js/require "./images/close.png"))

(defn on-end-reached [ctg]
  (rf/dispatch [:images-load ctg]))

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

(def students
  {:component
   (r/create-class
     {:reagent-render
      (fn []
        (let [students (rf/subscribe [:students])]
          [students-list @students]))})
   :config
   {:screen            :students
    :screen-type       :screen
    :title             cfg/app-name
    :navigator-buttons {:right-buttons
                        [{:id   :favorites
                          :icon star-icon}]
                        :left-buttons
                        [{:icon (js/require "./images/navicon_menu.png")
                          :id   :menu}
                         {:icon (js/require "./images/user.png")
                          :id   :user}]}}
   :on-navigator-event-fn
   (fn [{:keys [id]}]
     (let [id (keyword id)]
       (case id
         :menu (rf/dispatch [:nav/toggle-drawer])
         :user (rf/dispatch [:nav/push id {:screen-type :modal}])
         (rf/dispatch [:nav/push id]))))})

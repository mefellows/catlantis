(ns yimp.shared.styles
  (:refer-clojure :exclude [pop!])
  (:require-macros [print.foo :as pf])
  (:require [yimp.shared.ui :as ui]))

(def styles
  (ui/create-stylesheet
    {:menu-container {:flex        0
                      :padding-top 40
                      :background-color "#ffffff"
                      :backface-visibility "hidden"}
     :list-item      {:flex 0}
     :list-item-text {:text-align :center
                      :color (ui/color :black)}
     :title          {:text-align     :center
                      :padding-bottom 20
                      :font-size      20
                      :font-weight    "500"}

     ; List View elements
     :listview-container  {:flex             1
                           ;  :background-color :transparent
                           :background-color (ui/color :white)
                           :flex-direction   :row
                           :border-top-width 1
                           :border-top-color "#efefef"
                           :height "90%"}
     :listview-row        {:flex 1
                           :flex-direction :row
                           :width      "100%"
                           :height     "10%"
                           :border-bottom-color "#efefef"
                           :border-bottom-width 1}
     :listview-rowcontent {:flex 9
                           :flex-direction :row
                           :align-items :center
                           :padding-left 20
}
     :listview-rowaction  {:flex 1
                           :flex-direction :row
                           :align-items :center}
     :button-forward      {:color (ui/color :black)
                           :flex       1
                           :resizeMode "cover"}
     :listview-btn        {:border-width     0
                           :align-self       :center}
     :listview-btn-text   {:color (ui/color :orange800)}
     :listview-row-footer {:flex 1
                           :flex-direction :column
                           :align-items :center
                           :padding 20
                           :width      "100%"
                           :height     "10%"}

     ; Other
     :container     {:flex             1
                     :background-color :transparent
                     :flex-direction   :row
                     :height 600}
     :scroll-container {:flex 8
                        :padding 20}
     :footer-container {:flex 2}
     :text         {:color "white" :text-align "center" :font-weight "bold"}
     :first-item   {:margin-top 60}
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
     :image-text   {:text-align :left
                    :color      (ui/color :dark-black)
                    :width      "90%"
                    :height     "15%"}
     :no-imgs-wrap {:flex        1
                    :padding-top 80
                    :align-items "center"}
     :bg-img       {:flex   1
                    :width  "100%"
                    :height "100%"}
     :form-container {:margin-top 50
                      :flex 1
                      :justify-content :center}
     :login-container {:background-color :transparent
                       :flex             1
                       :height           300
                       :justifyContent   :center}
     :label-text   {:color (ui/color :grey400)
                    :text-align "left"
                    :font-weight "normal"
                    :width       "75%"}
     :input        {:height           50
                    :background-color (ui/color :white)
                    :width            "75%"
                    :margin-bottom    5
                    :border-radius    6
                    :align-self       :center
                    :opacity          0.75}

     :submit-btn   {:background-color (ui/color :cyan300)
                    :border-width     0
                    :width            "75%"
                    :opacity          0.9
                    :align-self       :center}
:submit-btn-text {:color (ui/color :white)}}))

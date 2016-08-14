(ns catlantis.shared.styles
  (:refer-clojure :exclude [pop!])
  (:require-macros [print.foo :as pf])
  (:require [catlantis.shared.ui :as ui]))

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
                    :height     "15%"}
     :no-imgs-wrap {:flex        1
                    :padding-top 80
                    :align-items "center"}
     :bg-img       {:flex   1
                    :width  "100%"
                    :height "100%"}
     :login-container    {:background-color :transparent
                    :flex             1
                    :height           300
                    :justifyContent   :center}
     :input        {:height           50
                    :background-color (ui/color :white)
                    :width            "75%"
                    :margin-bottom    5
                    :border-radius    6
                    :align-self       :center
                    :opacity          0.75
                    }
     :submit-btn   {:background-color (ui/color :cyan300)
                    :border-width     0
                    :width            "75%"
                    :opacity          0.9
                    :align-self       :center}
     :submit-btn-text {:color (ui/color :white)}}))

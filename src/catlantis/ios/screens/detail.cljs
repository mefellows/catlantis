(ns catlantis.ios.screens.detail
  (:require [catlantis.shared.ui :as ui]
            [re-frame.core :as rf]
            [print.foo :as pf :include-macros true]
            [reagent.core :as r]))

(declare styles)

(def close-icon (js/require "./images/close.png"))
(def star-icon (js/require "./images/star.png"))
(def star-icon-full (js/require "./images/star_selected.png"))

(defn btn-icon [icon on-press tint-color]
  [ui/touchable-opacity
   {:on-press on-press
    :style    (:close-btn styles)}
   [ui/image
    {:source icon
     :style  {:tint-color (ui/color tint-color)}}]])

(def detail
  {:component
   (r/create-class
     {:reagent-render
      (fn []
          [ui/scroll-view
           {:style (:container styles)}
           [ui/view
            {:style (:buttons-wrap styles)}
            [ui/text
             {:style (:image-text styles)}
             "some text!"]]
           [ui/view
            {:style (:buttons-wrap styles)}
            [ui/text
             {:style (:image-text styles)}
             "some text!"]]
           [ui/view
            {:style (:buttons-wrap styles)}
            [ui/text
             {:style (:image-text styles)}
             "some text!"]]
           ])})
   :config
   {:screen            :detail
    :screen-type       :light-box
    :title             ""
    :navigator-buttons {:right-buttons []
                        :left-buttons  [{:icon close-icon
                                         :id   :close}]}
    :style             {:background-blur "dark"}}})

(def styles
  (ui/create-stylesheet
    {:container    {:flex             1
                    :background-color :transparent
                    :flex-direction   :column}
     :text         {:color "white" :text-align "center" :font-weight "bold"}
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

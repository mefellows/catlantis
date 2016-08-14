(ns catlantis.ios.screens.detail
  (:require [re-frame.core :as rf]
            [catlantis.shared.ui :as ui]
            [catlantis.shared.styles :refer [styles]]
            [print.foo :as pf :include-macros true]))

(def detail
  (ui/create-screen :detail
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
         ])))

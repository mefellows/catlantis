(ns catlantis.ios.screens.home
  (:require [re-frame.core :as rf]
            [catlantis.shared.ui :as ui]
            [catlantis.shared.styles :refer [styles]]
            [print.foo :as pf :include-macros true]))

(def home
  (ui/create-screen :home
    (fn []
        [ui/scroll-view
         {:style (:container styles)}
         [ui/view
          {:style (merge-with (:text-wrap styles) (:first-item styles))}
          [ui/text
           {:style (:image-text styles)}
           "first some text!"]]
         [ui/view
          {:style (:text-wrap styles)}
          [ui/text
           {:style (:image-text styles)}
           "some text!"]]
         [ui/view
          {:style (:text-wrap styles)}
          [ui/text
           {:style (:image-text styles)}
           "some last text!"]]
         ])))

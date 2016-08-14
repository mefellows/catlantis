(ns yimp.ios.screens.home
  (:require [re-frame.core :as rf]
            [yimp.shared.ui :as ui]
            [yimp.shared.styles :refer [styles]]
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

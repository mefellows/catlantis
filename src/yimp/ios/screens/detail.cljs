(ns yimp.ios.screens.detail
  (:require [re-frame.core :as rf]
            [yimp.shared.ui :as ui]
            [yimp.shared.styles :refer [styles]]
            [print.foo :as pf :include-macros true]))

(def detail
  (ui/create-screen :detail "Incident Detail"
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
         "some text!"]]])))
         

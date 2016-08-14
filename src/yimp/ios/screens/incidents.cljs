(ns yimp.ios.screens.incidents
  (:require [re-frame.core :as rf]
            [yimp.shared.ui :as ui]
            [yimp.shared.styles :refer [styles]]
            [print.foo :as pf :include-macros true]))

(def incidents
  (ui/create-screen :incidents
    (fn []
        [ui/scroll-view
         {:style (:container styles)}
         [ui/view
          {:style (merge-with (:text-wrap styles) (:first-item styles))}
          [ui/text
           {:style (:image-text styles)}
           "Incident 1"]]
         [ui/view
          {:style (:text-wrap styles)}
          [ui/text
           {:style (:image-text styles)}
           "Incident 2"]]
         [ui/view
          {:style (:text-wrap styles)}
          [ui/text
           {:style (:image-text styles)}
           "Incident 3"]]
         ])))

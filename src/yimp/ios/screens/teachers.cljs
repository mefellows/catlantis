(ns yimp.ios.screens.teachers
  (:require [re-frame.core :as rf]
            [yimp.shared.ui :as ui]
            [yimp.shared.styles :refer [styles]]
            [print.foo :as pf :include-macros true]))
            
(def teachers
  (ui/create-screen :teachers "Teachers"
    (fn []
        [ui/scroll-view
         {:style (:container styles)}
         [ui/view
          {:style (merge-with (:text-wrap styles) (:first-item styles))}
          [ui/text
           {:style (:image-text styles)}
           "Teacher 1"]]
         [ui/view
          {:style (:text-wrap styles)}
          [ui/text
           {:style (:image-text styles)}
           "Teacher 2"]]
         [ui/view
          {:style (:text-wrap styles)}
          [ui/text
           {:style (:image-text styles)}
           "Teacher 3"]]
         ])))

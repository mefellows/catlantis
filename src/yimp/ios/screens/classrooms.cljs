(ns yimp.ios.screens.classrooms
  (:require [re-frame.core :as rf]
            [yimp.shared.ui :as ui]
            [yimp.shared.styles :refer [styles]]
            [print.foo :as pf :include-macros true]
            [yimp.ios.components.classroom-list :refer [classroom-list]]))

(def classrooms
  (ui/create-screen :classrooms "Classrooms"
    (fn [props]
      (let [classrooms (rf/subscribe [:classrooms])
           loading (rf/subscribe [:sync])]
        [classroom-list @classrooms @loading]))))

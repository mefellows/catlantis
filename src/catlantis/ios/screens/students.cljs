(ns catlantis.ios.screens.students
  (:require [re-frame.core :as rf]
            [catlantis.shared.ui :as ui]
            [catlantis.shared.styles :refer [styles]]
            [print.foo :as pf :include-macros true]
            [catlantis.ios.components.students-list :refer [students-list]]))

(def students
  (ui/create-screen :students
    (fn []
      (let [students (rf/subscribe [:students])]
        [students-list @students]))))

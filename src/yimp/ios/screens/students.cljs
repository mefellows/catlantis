(ns yimp.ios.screens.students
  (:require [re-frame.core :as rf]
            [yimp.shared.ui :as ui]
            [yimp.shared.styles :refer [styles]]
            [print.foo :as pf :include-macros true]
            [yimp.ios.components.students-list :refer [students-list]]))

(def students
  (ui/create-screen :students
    (fn []
      (let [students (rf/subscribe [:students])]
        [students-list @students]))))

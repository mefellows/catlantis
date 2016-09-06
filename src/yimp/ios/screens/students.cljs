(ns yimp.ios.screens.students
  (:require [re-frame.core :as rf]
            [yimp.shared.ui :as ui]
            [yimp.shared.styles :refer [styles]]
            [print.foo :as pf :include-macros true]
            [yimp.ios.components.student-list :refer [student-list]]))

(def students
  (ui/create-screen :students "Students"
    (fn [props]
      (let [students (rf/subscribe [:students])
           loading (rf/subscribe [:sync])]
        [student-list (sort-by :first_name @students) @loading]))))

(ns yimp.ios.screens.preferences
  (:require [re-frame.core :as rf]
            [yimp.shared.ui :as ui]
            [yimp.shared.styles :refer [styles]]
            [print.foo :as pf :include-macros true]
            [yimp.ios.components.preference-list :refer [preference-list]]))

(def navigator-buttons
  {:right-buttons
   [{:id   :create-preference
     :icon ui/add-icon}]
   :left-buttons
   [{:icon ui/menu-icon
     :id   :menu}
    {:id   :sync
     :icon ui/sync-icon}]})

(def preferences
  (ui/create-screen :preferences "Preferences"
    (fn [props]
      (let [preferences (rf/subscribe [:preferences])
           loading (rf/subscribe [:sync])]
        [preference-list (sort-by :first_name @preferences) @loading])) navigator-buttons))

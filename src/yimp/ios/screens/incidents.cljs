(ns yimp.ios.screens.incidents
  (:require [re-frame.core :as rf]
            [yimp.shared.ui :as ui]
            [yimp.shared.styles :refer [styles]]
            [yimp.ios.components.incident-list :refer [incident-list]]))
(def incidents
  (ui/create-screen :incidents "Incidents"
    (fn [props]
      (let [incidents (rf/subscribe [:incidents])]
        [incident-list @incidents]))))

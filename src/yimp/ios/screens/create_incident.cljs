(ns yimp.ios.screens.create-incident
  (:require [re-frame.core :as rf]
            [reagent.core :as r]
            [yimp.shared.styles :refer [styles]]
            [print.foo :as pf :include-macros true]
            [yimp.ios.components.image-list :refer [image-list]]
            [yimp.shared.ui :as ui]))

(def create-incident
  {:component
   (r/create-class
     {:component-will-mount
      (fn []
        (rf/dispatch-sync [:incident-load]))
      :reagent-render
      (fn []
        (let [fav-query (rf/subscribe [:incident])
              [images loading?] @fav-query]
          (if (seq images)
            [image-list images loading?]
            (if loading?
              [ui/activity-indicator-ios {:animating true}]
              [ui/view
               {:style (:no-imgs-wrap styles)}
               [ui/text {}
                "You haven't added any image to incident yet"]]))))})
   :config
   {:screen            :create-incident
    :screen-type       :screen
    :title             "Create Incident"
    :navigator-buttons {:left-buttons
                        [{:icon (js/require "./images/ic_chevron_left.png")
                          :id   :back}]}}
   :on-navigator-event-fn
   (fn [{:keys [id]}]
     (let [id (keyword id)]
       (case id
         :back (rf/dispatch [:nav/pop]))))})

(ns yimp.ios.screens.preferences
  (:require [re-frame.core :as rf]
            [yimp.shared.ui :as ui]
            [yimp.shared.styles :refer [styles]]
            [clojure.walk :refer [keywordize-keys]]
            [print.foo :as pf :include-macros true]
            [yimp.ios.components.preference-list :refer [preference-list]]))

(def navigator-buttons
  (assoc ui/navigator-buttons :right-buttons [{:id :create-preference 
                                               :icon ui/add-icon}]))

(defn filtered-preferences [preferences action]
  (->> (keywordize-keys preferences)
    (remove #(not= (:type %1) action))))

(def preferences
  (ui/create-screen :preferences "Preferences"
    (fn [props]
      (let [preferences (rf/subscribe [:preferences])
           loading (rf/subscribe [:sync])]
           (js/console.log (clj->js @preferences))
           [ui/scroll-view {:style (:first-item styles)}
             [ui/view {:style (:readonly-form styles)}
              [ui/text {:style (:readonly-section-title styles)}
                 "Contacts"]]
                 [ui/view {:style (:readonly-container styles)}
              [ui/text {:style (:readonly-value styles)}
                 "The following people will be notified daily at 5pm of all yard incidents"]]                 
              [preference-list (filtered-preferences @preferences "contact") @loading]
             [ui/view {:style (:readonly-form styles)}
              [ui/text {:style (:readonly-section-title styles)}
                 "Summaries"]]
                 [ui/view {:style (:readonly-container styles)}
              [ui/text {:style (:readonly-value styles)}
                 "Incident summaries for pre-population in yard incidents"]]                 
              [preference-list (filtered-preferences @preferences "summary") @loading]
             [ui/view {:style (:readonly-form styles)}
              [ui/text {:style (:readonly-section-title styles)}
                 "Locations"]]
                 [ui/view {:style (:readonly-container styles)}
              [ui/text {:style (:readonly-value styles)}
                 "Locations for pre-population in yard incidents"]]                 
              [preference-list (filtered-preferences @preferences "location") @loading]
              [ui/view {:style (:readonly-form styles)}
               [ui/text {:style (:readonly-section-title styles)}
                  "Actions"]]
                  [ui/view {:style (:readonly-container styles)}
               [ui/text {:style (:readonly-value styles)}
                  "Action taken for pre-population in yard incidents"]]   
                  [preference-list (filtered-preferences @preferences "action") @loading]
              ])) navigator-buttons))

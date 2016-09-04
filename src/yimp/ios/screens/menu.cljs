(ns yimp.ios.screens.menu
  (:require [yimp.shared.ui :as ui]
            [reagent.core :as r]
            [re-frame.core :as rf]
            [clojure.string :as str]
            [yimp.shared.styles :refer [styles]]
            [yimp.shared.navigation :as nav]
            [print.foo :as pf :include-macros true]))

(defn on-menu-press [ctg]
  (let [{:keys [id name]} ctg
       id (keyword id)]
  (nav/set-title! (str/capitalize name))
  (rf/dispatch [:nav/push id {:screen-type :screen}])
  (rf/dispatch [:nav/toggle-drawer])))

(def menu-options
  [{:id "incidents" :name "Incidents"}
   {:id "classrooms" :name "Classrooms"}
   {:id "students" :name "Students"}])

(defn menu []
  (let [catgs menu-options
        {:keys [menu-container]} styles]
          [ui/view
           {:style menu-container}
           [ui/text {:style (:title styles)} "Menu"]
           (for [{:keys [id name] :as ctg} menu-options]
             [ui/list-item {:text           (if id name "All")
                            :style          (:list-item styles)
                            :style-text     (:list-item-text styles)
                            :on-press       (partial on-menu-press ctg)
                            :key            id
                            :underlay-color (ui/color :grey300)}])]))

(ns catlantis.ios.screens.categories
  (:require [catlantis.shared.ui :as ui]
            [reagent.core :as r]
            [re-frame.core :as rf]
            [clojure.string :as str]
            [catlantis.shared.navigation :as nav]
            [print.foo :as pf :include-macros true]))

(declare styles)

(defn on-menu-press [ctg]
  ; (rf/dispatch [:menu-select id])
  (let [{:keys [id name]} ctg
       id (keyword id)]
  (nav/set-title! (str/capitalize name))
  (rf/dispatch [:nav/push id {:screen-type :screen}])
  (rf/dispatch [:nav/toggle-drawer])))

(def menu-options
  [{:id "incidents" :name "Incidents"}
   {:id "teachers" :name "Teachers"}
   {:id "students" :name "Students"}])

(defn categories []
  (let [catgs menu-options
        {:keys [container]} styles]
          [ui/view
           {:style container}
           [ui/text {:style (:title styles)} "Menu"]
           (for [{:keys [id name] :as ctg} menu-options]
             [ui/list-item {:text           (if id name "All")
                            :style          (:list-item styles)
                            :style-text     (:list-item-text styles)
                            :on-press       (partial on-menu-press ctg)
                            :key            id
                            :underlay-color (ui/color :grey300)}])]))

(def styles
  (ui/create-stylesheet
    {:container      {:flex        0
                      :padding-top 40}
     :list-item      {:flex 0}
     :list-item-text {:text-align :center}
     :title          {:text-align     :center
                      :padding-bottom 20
                      :font-size      20
                      :font-weight    "500"}}))

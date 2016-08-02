(ns catlantis.ios.screens.categories
  (:require [catlantis.shared.ui :as ui]
            [reagent.core :as r]
            [re-frame.core :as rf]
            [print.foo :as pf :include-macros true]))

(declare styles)

(defn on-categ-press [ctg]
  (rf/dispatch [:category-select ctg])
  (rf/dispatch [:nav/toggle-drawer]))

(def menu-options 
  [{:id "1" :name "Incidents"}
   {:id "2" :name "Teachers"}
   {:id "3" :name "Students"}])

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
                            :on-press       (partial on-categ-press ctg)
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

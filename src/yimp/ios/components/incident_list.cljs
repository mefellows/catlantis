(ns yimp.ios.components.incident-list
  (:require-macros [natal-shell.data-source :as ds])
  (:require [yimp.shared.ui :as ui]
            [yimp.shared.styles :refer [styles]]
            [yimp.utils :as u]
            [re-frame.core :as rf]
            [reagent.core :as r]
            [print.foo :as pf :include-macros true]
            [yimp.config :as cfg]
            [yimp.config :refer [app-name]]))

(def list-view-ds (ds/data-source {:rowHasChanged #(not= %1 %2)}))

(defn render-incident-row [{:keys [Summary ID] :as incident}]
   [ui/view
      {:style (:listview-row styles)}
     [ui/view
        {:style (:listview-rowcontent styles)}
     [ui/text
      {:style (:image-text styles)}
      Summary]]
      [ui/image {;:style  (get-in styles [:listview :item :button-forward])
                 :style  (:button-forward styles)
                 :source (js/require "./images/ic_chevron_right.png")}]])

(defn footer [loading?]
  (when loading?
    [ui/view
     {:style (:listview-row styles)}
     [ui/activity-indicator-ios
      {:style (:indicator styles)}]]))

; TODO: add touchable highlight and click-through
(defn incident-list [incidents]
  [ui/scroll-view
   {:style (:listview-container styles)}
   [ui/list-view (merge
                   {:dataSource    (ds/clone-with-rows list-view-ds incidents)
                    :render-row    (comp r/as-element render-incident-row u/js->cljk)
                    :style         (merge-with (:container styles) (:first-item styles))
                    :render-footer (comp r/as-element (partial footer true))}
                   {})]])

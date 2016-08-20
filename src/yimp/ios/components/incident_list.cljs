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

(defn submit [id]
  (print "selected: " id )
  (rf/dispatch [:incident-load id] ))

(defn render-incident-row [{:keys [summary id] :as incident}]
  [ui/touchable-highlight {:style       (:listview-row styles)
                           :on-press    #(submit id)
                           :underlay-color "#efefef"
                           :active-opacity .9}
    [ui/view {:style       (:listview-row styles)}
      [ui/view {:style (:listview-rowcontent styles)}
          [ui/text {}
            summary]]
      [ui/view {:style (:listview-rowaction styles)}
        [ui/text {} " > "]]]])

(defn footer [loading?]
  (when loading?
    [ui/view
     {:style (:listview-row-footer styles)}
     [ui/activity-indicator-ios
      {:style (:indicator styles)}]]))

; (def refresh-control
;   (r/as-element [ui/refresh-control {:refreshing #(true)
;                                      :on-refresh #(print "refreshing on refresh")
;                                      :title "Loading incidents..."}]))

; TODO: add touchable highlight and click-through
(defn incident-list [incidents]
  [ui/scroll-view
   {:style (:listview-container styles)
    ;:refresh-control refresh-control}
   }

   [ui/list-view (merge
                   {:dataSource    (ds/clone-with-rows list-view-ds incidents)
                    :render-row    (comp r/as-element render-incident-row u/js->cljk)
                    :style         (merge-with (:container styles) (:first-item styles))
                    :render-footer (comp r/as-element (partial footer true))}
                   {})]])

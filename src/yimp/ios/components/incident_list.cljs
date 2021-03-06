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
  (let [date (new js/Date (:start_time incident))]
    [ui/touchable-highlight {:style       (:listview-row styles)
                             :on-press    #(submit id)
                             :underlay-color "#efefef"
                             :active-opacity .9}
      [ui/view {:style       (:listview-row styles)}
        [ui/view {:style (:listview-rowcontent styles)}
            [ui/text {:style (:listview-rowcontent-attribute styles)}
                (str (.getUTCDate date) "/" (+ 1(.getUTCMonth date)) " " (.getUTCFullYear date))]
            [ui/text {:style (:listview-rowcontent-inner styles)}
              summary]]
        [ui/view {:style (:listview-rowaction styles)}
          [ui/text {} " > "]]]]))

(defn footer [loading?]
  (when loading?
    [ui/view
     {:style (:listview-row-footer styles)}
     [ui/activity-indicator-ios
      {:style (:indicator styles)}]]))

; TODO: add touchable highlight and click-through
(defn incident-list [incidents loading?]
  (if (not-empty incidents)
    (let []
       [ui/list-view (merge
                       {:dataSource    (ds/clone-with-rows list-view-ds incidents)
                        :render-row    (comp r/as-element render-incident-row u/js->cljk)
                        :style         (merge-with (:container styles) {})
                        :render-footer (comp r/as-element (partial footer loading?))}
                       {})])
   [ui/scroll-view
    {:style (:container styles)}
    [ui/view
     {:style (:listview-rowcontent styles)}
     [ui/text {}
"No incidents huh? You must have a nice school!"]]]))

(defn incident-list-view [incidents loading?]
  (let []
    [ui/scroll-view {:style (merge-with (:listview-row styles) (:first-item styles))}
    [incident-list incidents loading?]]))

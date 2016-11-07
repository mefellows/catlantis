(ns yimp.android.core
  (:require [reagent.core :as r]
            [re-frame.core :refer [subscribe dispatch dispatch-sync]]
            [yimp.handlers]
            [yimp.subs]
            [yimp.android.shared.ui :as ui]
            [yimp.android.ui :as android-ui]
            [yimp.android.styles :as s]
            [yimp.android.scenes.root :refer [root-scene]]))

; (defn app-root []
;   [android-ui/navigator {:initial-route   {:name "main" :index 1}
;                          :style           (get-in s/styles [:app])
;                          :configure-scene (fn [_ _]
;                                             js/React.Navigator.SceneConfigs.FloatFromBottomAndroid)
;                          :render-scene    (fn [_ navigator]
;                                             (r/as-element [root-scene {:navigator navigator}]))}])

(defn init []
  (dispatch-sync [:initialize-schema])
  (dispatch [:load-from-db :city])
  (.registerComponent ui/app-registry "yimp" #(r/reactify-component root-scene)))

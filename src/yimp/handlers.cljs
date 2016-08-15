(ns yimp.handlers
  (:require
    [re-frame.core :refer [register-handler after]]
    [schema.core :as s :include-macros true]
    [print.foo :as pf :include-macros true]
    [yimp.db :refer [app-db schema]]
    [yimp.shared.navigation :as nav]
    [yimp.shared.ui :as ui]
    [re-frame.middleware :as mid]
    [ajax.core :refer [GET]]
    [yimp.config :as cfg]
    [clojure.string :as str]
    [yimp.api :as api]
    [re-frame.core :as rf]))

(enable-console-print!)

(defn log-ex
  [handler]
  (fn log-ex-handler
    [db v]
    (try
        (handler db v)        ;; call the handler with a wrapping try
        (catch :default e     ;; ooops
          (do
            (.error js/console e.stack)   ;; print a sane stacktrace
            (throw e))))))

(defn check-and-throw
  "throw an exception if db doesn't match the schema."
  [a-schema db]
  (when-let [problems (s/check a-schema db)]
    (throw (js/Error. (str "schema check failed: " problems)))))

(def validate-schema-mw
  (when goog.DEBUG
    (after (partial check-and-throw schema))))

(def basic-mw [#_mid/debug mid/trim-v validate-schema-mw])

(register-handler
  :initialize-db
  basic-mw
  (fn [_]
    app-db))

(register-handler
  :set-students
  basic-mw
  (fn [db [value]]
    (assoc db :students value)))

(register-handler
  :nav/push
  basic-mw
  (s/fn [db [screen-name config]]
    (nav/push-screen! screen-name config)
    db))

(register-handler
  :nav/pop
  basic-mw
  (s/fn [db]
    (nav/pop-screen!)
    db))

(register-handler
  :nav/toggle-drawer
  basic-mw
  (s/fn [db [config]]
    (nav/toggle-drawer! config)
    db))

(register-handler
  :menu-select
  basic-mw
  (s/fn [db [option]]
    (nav/set-title! (str/capitalize (:name option)))
    (assoc db :menu-selected (if (:id option) option nil))))

(def standard-middlewares  [basic-mw log-ex])

(register-handler
 :process-students-res
 (fn
   ;; store the response of fetching the phones list in the phones attribute of the db
   [db [_ response]]
   (print response)
   (assoc db :students response)))

(register-handler
  :load-students
  (s/fn [db _]
    (ajax.core/GET "http://localhost:8000/students"
    ; (ajax.core/GET "http://yimp.herokuapp.com/students"
     {
      :response-format :json
      :keywords? true
      :handler #(rf/dispatch [:process-students-res %1])
      :error-handler #(rf/dispatch-sync [:bad-response %1])})
   db)) ; <- DAH! Must return the state!!

(register-handler
  :incident-load
  basic-mw
  (s/fn [db]
    (let [query-params (cond-> (merge cfg/default-catapi-params
                                      {:sub-id (get-in db [:user :username])}))]
      (api/fetch! :favorites query-params
                  {:handler #(rf/dispatch [:incident-res (-> % :images)])})
      (assoc-in db [:incident-query :loading?] true))))

(register-handler
  :incident-res
  basic-mw
  (s/fn [db [images]]
    (let [images (if (map? images) [(:image images)] images)]
      (-> db
          (assoc-in [:incident-query :images] images)
          (assoc-in [:incident-query :loading?] false)))))

(register-handler
  :user-change
  basic-mw
  (s/fn [db [user]]
    (assoc db :user user)))

(register-handler
  :sync-complete
  basic-mw
  (s/fn [db [res]]
    (print res)
    (assoc db :sync false)))

; New Handlers
(register-handler
  :synchronise
  basic-mw
  (s/fn [db [_]]
    (ui/alert "Synchronising in background")
    (print "dispatch sync!")
    (ajax.core/POST "http://localhost:8000/sync"
    ; (ajax.core/POST "http://yimp.herokuapp.com/sync"
     {
      :response-format :json
      :keywords? true
      :handler #(rf/dispatch [:sync-complete %1])
      :error-handler #(rf/dispatch-sync [:bad-response %1])})
    ()
    (assoc db :sync true)))

(register-handler
  :bad-response
  (s/fn [db [_ _]])
  (print "error"))

  (register-handler
   :process-incidents-res
   (fn
     ;; store the response of fetching the phones list in the phones attribute of the db
     [db [_ response]]
     (print response)
     (assoc db :incidents response)))

  (register-handler
    :load-incidents
    (s/fn [db _]
      (ajax.core/GET "http://localhost:8000/incidents"
      ; (ajax.core/GET "http://yimp.herokuapp.com/incidents"
       {
        :response-format :json
        :keywords? true
        :handler #(rf/dispatch [:process-incidents-res %1])
        :error-handler #(rf/dispatch-sync [:bad-response %1])})
     db)) ; <- DAH! Must return the state!!

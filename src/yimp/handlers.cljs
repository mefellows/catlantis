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


(defn find-incident "Finds an incident in the given db by id" [db id]
  (let [incidents (:incidents db)
    incident (first
               (->> incidents
                    (filter
                      (fn [incident]
                        (= (:id incident) id)))))]
    (if-not (nil? incident)
      (let []
        (rf/dispatch [:nav/push :edit-incident])
        (assoc db :current-incident incident))
      nil)))

(defn update-incident "Finds and updates an incident in the db" [db incident]
  (let [incidents (:incidents db)]
    (->>
      incidents
      (mapv #(let []
        (if (= (:id %1) (:id incident)) incident %))))))
      ; (print "Just updated incidents to -> ")
      ; (assoc db :incidents))))

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

; Fetch a single incident from the local database (does not make API call)
(register-handler
  :incident-load
  basic-mw
  (s/fn [db [id]]
    (find-incident db id)))

(register-handler
  :incident-res
  basic-mw
  (s/fn [db [incident]]
    (let [incident (if (map? incident) [(:image incident)] incident)]
      (-> db
          (assoc-in [:incident-query :incident] incident)
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

(defn sync-records [records]
  (print "sync records: " records)
  (ajax.core/POST "http://localhost:8000/sync"
  ; (ajax.core/POST "http://yimp.herokuapp.com/sync"
   {
    :format (ajax.core/json-request-format)
    :response-format (ajax.core/json-response-format {:keywords? true})
    :params records
    :handler #(rf/dispatch [:sync-complete %1])
    :error-handler #(rf/dispatch-sync [:bad-response %1])})
)

; New Handlers
(register-handler
  :synchronise
  basic-mw
  (s/fn [db [_]]
    (ui/alert "Synchronising in background")
    (sync-records (let [incidents (:incidents db)]
      (->> incidents
        (filterv #(let []
            (= (:synchronised %1) false))))))
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

 (register-handler
   :create-incident
   (s/fn [db [_]]
      (rf/dispatch [:nav/push :edit-incident])
      (assoc db :current-incident {})))

 ; NOTE: handle updates -> currently only adds new.
 (register-handler
   :save-incident
   basic-mw
   (s/fn [db [incident]]
     (print "Saving local incident: " incident)
     (let [incidents (:incidents db)
           id (:id incident)
           updated-incident (assoc incident :synchronised false)]
       (if-not (nil? id)
         (let []
           ; find and update an incident
           (print "UPDATING")
            (->> updated-incident
                 (update-incident db)
                 (assoc db :incidents)))
         (let []
           ; find and update an incident
           (print "INSERTING")
           (->> updated-incident
             (conj incidents)
             (assoc db :incidents)))))))

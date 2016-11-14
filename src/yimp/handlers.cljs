(ns yimp.handlers
  (:require
    [re-frame.core :refer [register-handler after]]
    [schema.core :as s :include-macros true]
    [print.foo :as pf :include-macros true]
    [yimp.db :refer [app-db schema]]
    [yimp.config :refer [env]]
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

(defn find-student-classroom "Finds a classroom in the given db by a students' id" [db id]
  (let [classrooms (:classrooms db)
    classroom (first
               (->> classrooms
                    (filter
                      (fn [classroom]
                        (some #{id} (:students classroom))))))]
    (if-not (nil? classroom)
      (let []
        (assoc db :current-student-classroom classroom))
      db)))

(defn find-classroom "Finds a classroom in the given db by id" [db id]
  (let [classrooms (:classrooms db)
    classroom (first
               (->> classrooms
                    (filter
                      (fn [classroom]
                        (= (:id classroom) id)))))]
    (if-not (nil? classroom)
      (let []
        (rf/dispatch [:nav/push :edit-classroom])
        (assoc db :current-student-classroom classroom))
      nil)))


(defn find-student "Finds a student in the given db by id" [db id]
  (let [students (:students db)
        incidents (:incidents db)
        student-incidents (->> incidents
                               (filter
                                 (fn [incident]
                                   ; return true where student in (:students incident)
                                   (not (empty? (->> (:students incident)
                                                    (filter
                                                      (fn [student]
                                                        (= (:id student) id)))))))))

        student (first
                   (->> students
                        (filter
                          (fn [student]
                            (= (:id student) id)))))]
        (if-not (nil? student)
          (let []
            (rf/dispatch [:nav/push :edit-student])
            (-> db
              (assoc :current-student-incidents student-incidents)
              (assoc :current-student student)))
          nil)))

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

(defn find-preference "Finds an preference in the given db by id" [db id]
  (let [preferences (:preferences db)
    preference (first
               (->> preferences
                    (filter
                      (fn [preference]
                        (= (:id preference) id)))))]
    (if-not (nil? preference)
      (let []
        (rf/dispatch [:nav/push :edit-preference])
        (assoc db :current-preference preference))
      nil)))

(defn update-incident "Finds and updates an incident in the db (uses local-id)" [db incident]
  (let [incidents (:incidents db)]
    (->>
      incidents
      (mapv #(let []
        (if (= (:local_id %1) (:local_id incident)) incident %))))))

(register-handler
  :initialize-db
  basic-mw
  (fn [_]
    app-db))

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
   (if-not (nil? response)
    (assoc db :students response)
    (assoc db :students []))))

(register-handler
 :process-classrooms-res
 (fn
   ;; store the response of fetching the phones list in the phones attribute of the db
   [db [_ response]]
   (print response)
   (if-not (nil? response)
    (assoc db :classrooms response)
    (assoc db :classrooms []))))

(register-handler
  :load-students
  (s/fn [db _]
    (ajax.core/GET (str (:hostname env) "/students")
     {
      :response-format :json
      :keywords? true
      :handler #(rf/dispatch [:process-students-res %1])
      :error-handler #(rf/dispatch-sync [:bad-response %1])})
   db)) ; <- DAH! Must return the state!!

(register-handler
  :load-teachers
  (s/fn [db _]
    (ajax.core/GET (str (:hostname env) "/teachers")
     {
      :response-format :json
      :keywords? true
      :handler #(rf/dispatch [:process-teachers-res %1])
      :error-handler #(rf/dispatch-sync [:bad-response %1])})
   db)) ; <- DAH! Must return the state!!

(register-handler
  :load-classrooms
  (s/fn [db _]
    (ajax.core/GET (str (:hostname env) "/classes")
     {
      :response-format :json
      :keywords? true
      :handler #(rf/dispatch [:process-classrooms-res %1])
      :error-handler #(rf/dispatch-sync [:bad-response %1])})
   db)) ; <- DAH! Must return the state!!

(register-handler
 :process-teachers-res
 (fn
   ;; store the response of fetching the phones list in the phones attribute of the db
   [db [_ response]]
   (print response)
   (if-not (nil? response)
    (assoc db :teachers response)
    (assoc db :teachers []))))

(register-handler
  :load-teachers
  (s/fn [db _]
    (ajax.core/GET (str (:hostname env) "/teachers")
     {
      :response-format :json
      :keywords? true
      :handler #(rf/dispatch [:process-teachers-res %1])
      :error-handler #(rf/dispatch-sync [:bad-response %1])})
   db)) ; <- DAH! Must return the state!!

; Fetch a single incident from the local database (does not make API call)
(register-handler
  :incident-load
  basic-mw
  (s/fn [db [id]]
    (find-incident db id)))

; Fetch a single preference from the local database (does not make API call)
(register-handler
  :preference-load
  basic-mw
  (s/fn [db [id]]
    (find-preference db id)))

; Fetch a single student from the local database (does not make API call)
(register-handler
  :student-load
  basic-mw
  (s/fn [db [id]]
    (-> db
      (find-student id)
      (find-student-classroom id))))

(register-handler
  :incident-res
  basic-mw
  (s/fn [db [incident]]
    (let [incident (if (map? incident) [(:image incident)] incident)]
      (-> db
          (assoc-in [:incident-query :incident] incident)
          (assoc-in [:incident-query :loading?] false)))))

(register-handler
  :login
  basic-mw
  (s/fn [db [user]]
    (let [password (:password user)]
      (if (= password "gullynorth")
        (let []
          (rf/dispatch [:nav/push :incidents])
          (assoc db :user user))
        (let []
          (js/alert "Invalid password :(")
          db)
          ))))

; Sync all lookup lists. These are not managed as carefully as incidents
; so all they do is replace what is currently in storage.
(defn sync-config []
  (rf/dispatch [:load-students])
  (rf/dispatch [:load-incidents])
  (rf/dispatch [:load-classrooms])
  (rf/dispatch [:load-teachers])
  (rf/dispatch [:load-preferences]))

(register-handler
  :sync-complete
  basic-mw
  (s/fn [db [res records]]
    (print res)
    (sync-config)
    (when-not (nil? res)
      ; update all incidents -> no longer dirty!
      (merge (:incidents db) (->> records
        (map (fn [i]
          (assoc i :synchronised true))))))
    (assoc db :sync false)))

(register-handler
  :sync-fail
  basic-mw
  (s/fn [db [res]]
    (print res)
    (ui/alert (str "Synchronise failed: " res))
    (assoc db :sync false)))

(defn sync-records [records]
  (js/console.log "sync records: " (clj->js records))
  (ajax.core/POST (str (:hostname env) "/sync")
   {
    :format (ajax.core/json-request-format)
    :response-format (ajax.core/json-response-format {:keywords? true})
    :params (clj->js records)
    :handler #(rf/dispatch [:sync-complete %1 records])
    :error-handler #(rf/dispatch-sync [:sync-fail %1])}))

(defn save-preference [preference]
  (js/console.log "saving preference: " (clj->js preference))
  (let [method (if (nil? (:id preference))
                          ajax.core/POST
                          ajax.core/PUT)]
                          (method (str (:hostname env) "/preferences")
                          {
                            :format (ajax.core/json-request-format)
                            :response-format (ajax.core/json-response-format {:keywords? true})
                            :params (clj->js (assoc preference :school_id (:school-id env)))
                            :handler #(js/alert "Saved!")
                            :error-handler #(js/alert "Error saving!")})))

; New Handlers
(register-handler
  :save-preference
  basic-mw
  (s/fn [db [preference]]
    (save-preference preference)
    (assoc db :current-preference preference)))

; New Handlers
(register-handler
  :synchronise
  basic-mw
  (s/fn [db [_]]
    (ui/alert "Synchronising...")
    (sync-records (let [incidents (:incidents db)]
      (->> incidents
        (filterv #(let []
            (= (:synchronised %1) false))))))
    (assoc db :sync true)))

(register-handler
  :bad-response
  (s/fn [db [_ body]]
    (print "error: " body)
    db))

(register-handler
 :process-incidents-res
 (fn
   [db [_ response]]
   (print response)
   (assoc db :incidents response)))

(register-handler
 :process-preferences-res
 (fn
   [db [_ response]]
   (print response)
   (assoc db :preferences response)))

(register-handler
  :load-incidents
  (s/fn [db _]
    (ajax.core/GET (str (:hostname env) "/incidents")
     {
      :response-format :json
      :keywords? true
      :handler #(rf/dispatch [:process-incidents-res %1])
      :error-handler #(rf/dispatch-sync [:bad-response %1])})
   db)) ; <- DAH! Must return the state!!

(register-handler
  :load-preferences
  (s/fn [db _]
    (ajax.core/GET (str (:hostname env) "/school/" (:school-id env) "/preferences")
     {
      :response-format :json
      :keywords? true
      :handler #(rf/dispatch [:process-preferences-res %1])
      :error-handler #(rf/dispatch-sync [:bad-response %1])})
   db)) ; <- DAH! Must return the state!!

 (register-handler
   :create-incident
   (s/fn [db [_]]
      (rf/dispatch [:nav/push :edit-incident])
      (assoc db :current-incident {})))

 (register-handler
   :create-preference
   (s/fn [db [_]]
      (rf/dispatch [:nav/push :edit-preference])
      (assoc db :current-preference {})))

(defn get-incident-by-local-id "Finds an incident in the db by its local-id" [db local-id]
  (let [incidents (:incidents db)]
    (first (->>
      incidents
      (filter #(let []
        (= (:local_id %1) local-id)))))))

 ; NOTE: handle updates to unsynced records -> currently only adds new.
 (register-handler
   :save-incident
   basic-mw
   (s/fn [db [incident]]
     (print "Saving local incident: " incident)
     (let [incidents (:incidents db)
           id (:id incident)
           local-id (:local_id incident)
           updated-incident (assoc incident :synchronised false)]
       (if (nil? (get-incident-by-local-id db local-id))
         (let []
           ; Add a new incident locally.
           (print "INSERTING a new incident")
           (->> updated-incident
             (conj incidents)
             (assoc db :incidents)))
           (let []
             ; find and update an existing incident
             (print "UPDATING an existing incident")
              (->> updated-incident
                   (update-incident db)
                   (assoc db :incidents)))))))

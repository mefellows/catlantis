(ns yimp.test
  (:require [cljs.test :refer-macros [run-all-tests]]))

(enable-console-print!)

(defn ^:export run
  []
  (run-all-tests #"yimp.*-test"))

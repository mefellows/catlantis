(ns cljs.yimp.test
  (:require [cljs.test :refer-macros [run-all-tests run-tests]]))

(enable-console-print!)

(defn ^:export run
  []
  (print "Running ALL tests")
  ; (run-all-tests #"cljs.yimp.*-test"))
  (run-all-tests))

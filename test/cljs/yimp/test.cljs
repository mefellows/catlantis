(ns cljs.yimp.test
  (:require [cljs.test :refer-macros [run-all-tests deftest is]]))

(enable-console-print!)

(defn ^:export run []
  (run-all-tests #"cljs.yimp.*-test"))

(deftest do-i-work
  (is (= 1 1)))

; (ns cljs.yimp.core-test
;   (:require [clojure.test :refer :all]
;             ; [yimp.core :refer :all]
;             ))
;
; (deftest a-test
;   (testing "FIXME, I fail."
;     (is (= 0 1))))
(ns yimp.foo-test
  (:require [cljs.test :refer-macros [deftest is]]))

  (deftest do-i-work
    (is (= 1 2)))

(ns seajure-pbt.service-test-spec
  (:require [clojure.test :refer :all]
            [clojure.test.check.clojure-test :refer [defspec]]
            [clojure.test.check.properties :as prop]
            [clojure.spec.alpha :as s]))


(defspec register-user-test
  (prop/forall
    [user (s/gen :seajure-pbt.spec/user)]
    ,
    ,
    ,
    ,))
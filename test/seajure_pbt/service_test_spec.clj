(ns seajure-pbt.service-test-spec
  (:require [clojure.test :refer :all]
            [clojure.test.check.clojure-test :refer [defspec]]
            [clojure.test.check.properties :as prop]
            [clojure.spec.alpha :as s]
            [seajure-pbt.service :as service]
            [io.pedestal.http :as bootstrap]
            [io.pedestal.test :as ptest]
            [seajure-pbt.db.core :as db]
            [buddy.core.hash :as hash]
            [buddy.core.codecs :as codecs]))

(def service
  (::bootstrap/service-fn (bootstrap/create-servlet service/service)))

(def client
  (db/create-client))

(defspec register-user-test
  200
  (prop/for-all
    [username (s/gen :seajure-pbt.spec/username)
     email    (s/gen :seajure-pbt.spec/email)
     password (s/gen (s/and string?
                            (complement empty?)))]

    ;; Perform the POST Request
    (let [body     {:username username
                    :email    email
                    :password password}
          response (ptest/response-for service
                                       :post "/register"
                                       :headers {"Content-Type" "application/json"}
                                       :body (cheshire.core/generate-string body))]

      ;; Some sanity checks, ensure that we get a 200
      (is (= 200 (:status response)))

      ;; Perform our Property Check
      (let [result (db/get-user
                     client
                     email
                     password)]

        (and (= username (:username result))
             (= email (:email result))
             (not (nil? (:id result))))))))


(comment
  (run-tests 'seajure-pbt.service-test-spec))

{:result       false
 :seed         1562876866914
 :failing-size 0
 :num-tests    1, :fail ["5" "R@mg.2XXu" "OY"],
 :shrunk       {:total-nodes-visited 20,
                :depth               19,
                :result              false,
                :smallest            ["0" "0@0.0" "0"]},
 :test-var     "register-user-test"}

{:result true, :num-tests 200, :seed 1562877610675, :test-var "register-user-test"}

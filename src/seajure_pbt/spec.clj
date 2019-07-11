(ns seajure-pbt.spec
  (:require [clojure.spec.alpha :as s]
            [clojure.test.check.generators :as gen]
            [seajure-pbt.specs.internet :as si]))

(s/def ::username (s/and string?
                         (complement empty?)))
(s/def ::email ::si/email)
(s/def ::id uuid?)

(s/def ::user
  (s/keys :req [::username
                ::email
                ::id]))

(def user
  {::username "jvtrigueros"
   ::email    "j.trigueros@sap.com"
   ::id       #uuid"22186d2a-7c1f-496c-81dc-46b99bc51c63"})

(comment
  (s/explain ::user user))
;; => Success!

(comment
  (s/explain
    ::user {::username ""
            ::email    ""
            ::id       ""}))
;; => "" - failed: uuid? in: [:seajure-pbt.spec/id] at:
;; [:seajure-pbt.spec/id] spec: :seajure-pbt.spec/id


(s/fdef greet-user
  :args (s/cat :user ::user)
  :ret nil?)

(defn greet-user
  [user]
  (println "Hello, " (::username user)))

(comment
  (s/exercise-fn `greet-user))

(comment
  (s/exercise-fn `greet-user)

  ([(#:seajure-pbt.spec{:username "",
                        :email    "",
                        :id       #uuid"bdd39f8f-d91a-4fe9-86cc-8a0b6e6faf3e"}) nil],,,
   [(#:seajure-pbt.spec{:username "B",
                        :email    "",
                        :id       #uuid"7891ef52-4d16-44d7-9a01-485313cf5ca3"}) nil]
   [(#:seajure-pbt.spec{:username "KqvZJcL6",
                        :email    "pC",
                        :id       #uuid"08886ad2-c78b-4322-8cce-e704dc993065"}) nil]))

(require '[clojure.test.check.generators :as gen])

(gen/sample gen/string 10)
;; => ("" "ý" "J" "" "À" "Ç" "C=zÿÞ" "ÄÔÈUâO" "&" "õ")

(gen/generate gen/string 0)
;; => ""

(gen/generate gen/string 200)
;; => "aÅ§U)RMôåëdê $u3LjíøìÂ@mnØÄ²\r<>Äùm+ûÐ;®Hï½.[b¡b¤¦Ð»o[§»g'øcbÕ%YÆÆBs¤ ¼fYN¼û~âñYB)¿4ý%È-NBJHJIwWÛkü?<xí¥ Qþ\fç,o,=]Þa\ráÃCâL"

(gen/generate gen/uuid 0)
;; => #uuid"9704a7b4-ca79-4664-b080-5a6ca5664342"

(gen/generate gen/uuid 200)
;; => #uuid"d55067b6-d060-439a-b13f-2161ded73390"

(comment
  (sizing-sample gen/large-integer))
;; => {0   (-1 0 -1 -1 -1),
;;     5   (1 6 7 4 2),
;;     25  (-1 55798 23198 -11 6124159),
;;     200 (8371567737
;;          -393642130983883
;;          -56826587109
;;          114071285698586153
;;          5723723802814291)}

(comment
  (require '[clojure.test.check.generators])

  (require '[clojure.spec.gen.alpha]))

(comment
  (gen/generate (s/gen :seajure-pbt.spec/user)))
  ;; =>
  ;; #:seajure-pbt.spec{:username "G371o1Q639SS69amI9Y9yXZyY0x8",
  ;;                   :email "U4BT615c18f3uM50@ZaH3oY4970mswyWf74.36",
  ;;                   :id #uuid"c5cbd342-a36e-47c1-8f15-63c69e2e974b"})
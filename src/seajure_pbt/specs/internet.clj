(ns seajure-pbt.specs.internet
  "Source: https://gist.github.com/conan/2edca210999b96ad26d38c1ee96dfe40"
  (:require [clojure.spec.gen.alpha :as gen]
            [clojure.spec.alpha :as s]))

(def non-empty-string-alphanumeric
  "Generator for non-empty alphanumeric strings"
  (gen/such-that #(not= "" %)
                 (gen/string-alphanumeric)))

(def email-gen
  "Generator for email addresses"
  (gen/fmap
    (fn [[name host tld]]
      (str name "@" host "." tld))
    (gen/tuple
      non-empty-string-alphanumeric
      non-empty-string-alphanumeric
      non-empty-string-alphanumeric)))

(s/def ::email
  (s/with-gen
    #(re-matches #".+@.+\..+" %)
    (fn [] email-gen)))

(comment
  (gen/generate (s/gen ::email)))
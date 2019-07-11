(ns seajure-property-based-testing.db.core
  (:require [cognitect.aws.client.api :as aws]
            [buddy.core.hash :as hash]
            [buddy.core.codecs :refer [bytes->hex]])
  (:import (java.util UUID)))

(def ^:dynamic *table-name* "Users")

(defn create-table
  "Create DynamoDB Table"
  [client table-name]
  (aws/invoke client
              {:op      :CreateTable
               :request {:TableName             table-name
                         :KeySchema             [{:AttributeName "Email"
                                                  :KeyType       "HASH"}]
                         :AttributeDefinitions  [{:AttributeName "Email"
                                                  :AttributeType "S"}]
                         :ProvisionedThroughput {:ReadCapacityUnits  1
                                                 :WriteCapacityUnits 1}}}))

(defn insert-user
  "Insert a User to DynamoDB"
  [client user]
  (let [{:keys [username email password]} user
        id (str (UUID/randomUUID))]
    (aws/invoke client
                {:op      :PutItem
                 :request {:TableName :Users
                           :Item      {:Id       {:S (str (UUID/randomUUID))}
                                       :Username {:S username}
                                       :Email    {:S email}
                                       :Password {:S (-> password
                                                         hash/sha256
                                                         bytes->hex)}}}})
    (-> user
        (dissoc :password)
        (assoc :id id))))

(defn fetch-user-by-username-and-password
  "docstring"
  [arglist])


(comment
  (def ddb (aws/client {:api               :dynamodb
                        :endpoint-override {:protocol :http
                                            :hostname "localhost"
                                            :port     8000}})))

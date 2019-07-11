(ns seajure-pbt.db.core
  (:require [cognitect.aws.client.api :as aws]
            [buddy.core.hash :as hash]
            [buddy.core.codecs :refer [bytes->hex]])
  (:import (java.util UUID)))

(def ^:dynamic *table-name* :Users)

(defn create-client
  []
  (aws/client {:api               :dynamodb
               :endpoint-override {:protocol :http
                                   :hostname "localhost"
                                   :port     8000}}))

(defn destroy-client
  [client]
  (aws/stop client))

(defn create-table
  "Create DynamoDB Table"
  [client table-name]
  (aws/invoke client
              {:op      :CreateTable
               :request {:TableName             table-name
                         :KeySchema             [{:AttributeName "Email"
                                                  :KeyType       "HASH"}
                                                 {:AttributeName "Password"
                                                  :KeyType       "RANGE"}]
                         :AttributeDefinitions  [{:AttributeName "Email"
                                                  :AttributeType "S"}
                                                 {:AttributeName "Password"
                                                  :AttributeType "S"}]
                         :ProvisionedThroughput {:ReadCapacityUnits  1
                                                 :WriteCapacityUnits 1}}}))

(defn delete-table
  "Destroy DynamoDB Table"
  [client table-name]
  (aws/invoke client
              {:op      :DeleteTable
               :request {:TableName table-name}}))

(defn insert-user
  "Insert a User to DynamoDB"
  [client user]
  (let [{:keys [username email password]} user
        id (str (UUID/randomUUID))]
    (aws/invoke client
                {:op      :PutItem
                 :request {:TableName *table-name*
                           :Item      {:Id       {:S (str (UUID/randomUUID))}
                                       :Username {:S username}
                                       :Email    {:S email}
                                       :Password {:S (-> password
                                                         hash/sha256
                                                         bytes->hex)}}}})
    (-> user
        (dissoc :password)
        (assoc :id id))))

(defn get-user
  "Get a User with email and password"
  [client email password]
  (if-let [user (not-empty (aws/invoke client
                                       {:op      :GetItem
                                        :request {:TableName *table-name*
                                                  :Key       {:Email    {:S email}
                                                              :Password {:S (-> password
                                                                                hash/sha256
                                                                                bytes->hex)}}}}))]
    {:id       (get-in user [:Item :Id :S])
     :email    (get-in user [:Item :Email :S])
     :username (get-in user [:Item :Username :S])}))



(comment
  (def ddb (aws/client {:api               :dynamodb
                        :endpoint-override {:protocol :http
                                            :hostname "localhost"
                                            :port     8000}})))

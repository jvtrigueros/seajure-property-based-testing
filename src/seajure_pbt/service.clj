(ns seajure-pbt.service
  (:require [io.pedestal.http :as http]
            [io.pedestal.http.route :as route]
            [io.pedestal.http.body-params :as body-params]
            [ring.util.response :as ring-resp]
            [seajure-pbt.db.core :as db]))

(defn register
  "Register a user."
  [request]
  (let [{:keys [username email password]} (:json-params request)
        client (db/create-client)
        user   (db/insert-user client {:username username
                                       :email    email
                                       :password password})]
    (db/destroy-client client)
    (ring-resp/response {:result user})))

(defn about-page
  [request]
  (ring-resp/response (format "Clojure %s - served from %s"
                              (clojure-version)
                              (route/url-for ::about-page))))

(defn home-page
  [request]
  (ring-resp/response "Hello World!"))

;; Defines "/" and "/about" routes with their associated :get handlers.
;; The interceptors defined after the verb map (e.g., {:get home-page}
;; apply to / and its children (/about).
(def common-interceptors [(body-params/body-params) http/html-body])
(def api-interceptors [(body-params/body-params) http/json-body])

;; Tabular routes
(def routes #{["/" :get (conj common-interceptors `home-page)]
              ["/about" :get (conj common-interceptors `about-page)]
              ["/register" :post (conj api-interceptors `register)]})

;; Consumed by seajure-pbt.server/create-server
;; See http/default-interceptors for additional options you can configure
(def service {:env                     :prod
              ::http/routes            routes

              ;; Root for resource interceptor that is available by default.
              ::http/resource-path     "/public"

              ::http/type              :jetty
              ;;::http/host "localhost"
              ::http/port              8080
              ;; Options to pass to the container (Jetty)
              ::http/container-options {:h2c? true
                                        :h2?  false
                                        ;:keystore "test/hp/keystore.jks"
                                        ;:key-password "password"
                                        ;:ssl-port 8443
                                        :ssl? false}})


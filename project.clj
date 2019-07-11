(defproject seajure-pbt "0.0.1-SNAPSHOT"
  :description "Seajure Meetup Demo Application"
  :url "https://github.com/jvtrigueros/seajure-pbt"
  :license "UNLICENSED"
  :dependencies [[org.clojure/clojure "1.10.1"]
                 [io.pedestal/pedestal.service "0.5.7"]
                 [io.pedestal/pedestal.jetty "0.5.7"]
                 [buddy/buddy-core "1.6.0"]
                 [com.cognitect.aws/api "0.8.345"
                  :exclusions [org.eclipse.jetty/jetty-client
                               org.eclipse.jetty/jetty-http
                               org.eclipse.jetty/jetty-util]]
                 [com.cognitect.aws/endpoints "1.1.11.586"]
                 [com.cognitect.aws/dynamodb "726.2.484.0"]]
  :min-lein-version "2.0.0"
  :resource-paths ["config", "resources"]
  :profiles {:dev     {:aliases      {"run-dev" ["trampoline" "run" "-m" "seajure-pbt.server/run-dev"]}
                       :dependencies [[io.pedestal/pedestal.service-tools "0.5.7"]
                                      [org.clojure/test.check "0.9.0"]
                                      [http-kit "2.3.0"]]}
             :uberjar {:aot [seajure-pbt.server]}}
  :main ^{:skip-aot true} seajure-pbt.server)

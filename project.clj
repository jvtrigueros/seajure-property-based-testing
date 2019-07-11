(defproject seajure-property-based-testing "0.0.1-SNAPSHOT"
  :description "Seajure Meetup Demo Application"
  :url "https://github.com/jvtrigueros/seajure-property-based-testing"
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
  :profiles {:dev     {:aliases      {"run-dev" ["trampoline" "run" "-m" "seajure-property-based-testing.server/run-dev"]}
                       :dependencies [[io.pedestal/pedestal.service-tools "0.5.7"]]}
             :uberjar {:aot [seajure-property-based-testing.server]}}
  :main ^{:skip-aot true} seajure-property-based-testing.server)

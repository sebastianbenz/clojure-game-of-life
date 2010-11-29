(defproject simulator "1.0.0-SNAPSHOT"
  :description "Conway's game of life"
  :dependencies [[org.clojure/clojure "1.2.0"]
                 [org.clojure/clojure-contrib "1.2.0"]
				 [clj-oauth "1.2.8"]]
  :dev-dependencies [[com.stuartsierra/lazytest "1.1.2"]]
  :repositories {"stuartsierra-releases" "http://stuartsierra.com/maven2"}
  :main simulator.run)
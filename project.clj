(defproject heroes "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [clocop "0.2.0"]]
  :main heroes.core
  :profiles {:dev {:source-paths ["dev" "src" "test"]
                   :dependencies [[org.clojure/tools.namespace "0.2.11"]
                                  [proto-repl "0.1.2"]]}})

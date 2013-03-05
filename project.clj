(defproject dom-binding "0.0.1"
  :plugins [[lein-cljsbuild "0.3.0"] ]
  :dependencies [[org.clojure/clojure "1.5.0"] [jayq "2.3.0"]]
  :cljsbuild {
              :builds [{
                        ; The path to the top-level ClojureScript source directory:
                        :source-paths ["cljs"]
                        ; The standard ClojureScript compiler options:
                        ; (See the ClojureScript compiler documentation for details.)
                        :compiler {
                                   :output-to "dist.js"  ; default: target/cljsbuild-main.js
                                   :optimizations :whitespace
                                   :pretty-print true}}
                       {:id "test"
                        :source-paths ["cljs" "cljs-test"]
                        :compiler {
                                   :output-to "tests/dom-binding-test.js"
                                   :optimizations :whitespace
                                   :pretty-print true}}]})
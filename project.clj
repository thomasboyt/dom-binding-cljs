(defproject dom-binding "0.0.1"
  :plugins [[lein-cljsbuild "0.3.0"] ]
  :dependencies [[org.clojure/clojure "1.5.0"] [jayq "2.3.0"]]
  :cljsbuild {
              :builds [{:id "dist"
                        :source-paths ["src/dom-binding"]
                        :compiler {:output-to "dist/dom-binding.js"
                                   :optimizations :simple
                                   :pretty-print false}}
                       {:id "debug"
                        :source-paths ["src/dom-binding"]
                        :compiler {:output-to "dist/debug.js"
                                   :optimizations :whitespace
                                   :pretty-print true}}
                       {:id "test"
                        :source-paths ["src/dom-binding" "src/tests"]
                        :compiler {:output-to "tests/dom-binding-test.js"
                                   :optimizations :whitespace
                                   :pretty-print true}}]})
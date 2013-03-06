(ns qunit)

(defn module [description]
  (.module js/QUnit description))

(defn test [description test-fn]
  (.test js/QUnit description test-fn))

(defn equal [result expected description]
  (.equal js/QUnit result expected description))

(defn deep-equal [result expected description]
  (.deepEqual js/QUnit (clj->js result)
                       (clj->js expected)
                       description))
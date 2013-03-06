(ns dom-binding.test
  [:require [dom-binding.core :as dom-binding] 
            [jayq.core :as jayq]]
  [:use [qunit :only [module test equal deep-equal]]])

(.start js/QUnit)


(module "Utility Tests")
(test "Selector Test"
      (fn []
        ; (.log js/console (= js/NodeList (type (.querySelectorAll js/document ".multi-bind"))))

        (let [expected-element (.querySelectorAll js/document ".multi-bind")]
          (deep-equal (dom-binding/selector->dom-nodes :.multi-bind)
                      expected-element
                      "The keyword selector :.multi-bind returns the expected vector of nodes.")
          (deep-equal (dom-binding/selector->dom-nodes ".multi-bind")
                      expected-element
                      "The string selector '.multi-bind' returns the expected vector of nodes."))))

(test "NodeList Test"
      (fn []
        (let [node-list (.querySelectorAll js/document ".multi-bind")]
          (equal (seq? node-list)
                 true
                 "A NodeList is seqable.")
          (deep-equal (next (next (next node-list)))
                      (vector (.item node-list 3))
                      "A NodeList can be iterated over with (next).")
          (deep-equal (first node-list)
                      (.item node-list 0)
                      "The first item in the NodeList is findable.")
          (deep-equal (last node-list)
                      (.item node-list 3)
                      "The last item in the NodeList is findable.")
          (equal (second (map (fn [item] item) node-list))
                 (.item node-list 1)
                 "NodeLists can be mapped over.")
          (equal (second (map-indexed (fn [idx item] idx) node-list))
                 1
                 "NodeLists can be index-mapped over."))))

(module "Setting Properties")

(test "Constructor Test" 
      (fn []
        (let [bike-shed (dom-binding/create-dom-map {:color "purple"})]
          (deep-equal bike-shed
                      {:color {:content "purple"}}
                      "The constructor allows properties to be properly created and applied"))))

(test "Multiple Value Constructor Test"
      (fn []
        (let [bike-shed (dom-binding/create-dom-map {:color "purple" :foo "bar"})]
          (deep-equal bike-shed
                      {:color {:content "purple"}
                       :foo {:content "bar"}}))))

(test "Set Test" 
      (fn [] 
        (let [bike-shed (dom-binding/create-dom-map)]
          (deep-equal (dom-binding/assoc-property! bike-shed :color "purple")
                      {:color {:content "purple"}}
                      "The set function will return a map with a new property on it."))))

(test "Update Test"
      (fn []
        (let [bike-shed (dom-binding/create-dom-map {:color "purple"})]
          (deep-equal (dom-binding/assoc-property! bike-shed :color "red")
                      {:color {:content "red"}}
                      "The set function will return a changed map when updating an existing property."))))

(module "Binding Properties")

(test "add-dom-bind! adds a binding"
      (fn []
        (let [bike-shed (->
                          (dom-binding/create-dom-map {:color "purple"})
                          (dom-binding/add-dom-bind! :color :#bindable))
              binded-element (.querySelector js/document "#bindable")]
          (equal (get (first (get-in bike-shed [:color :bindings])) :element)
                 binded-element
                 "The add-dom-bind! function returns a bound dom-mapping."))))

(test "dom-update updates the DOM for a binding"
      (fn []
        (let [bike-shed (->
                          (dom-binding/create-dom-map {:color "purple"})
                          (dom-binding/add-dom-bind! :color :#bindable))
              binded-element (.querySelector js/document "#bindable")]
          (equal binded-element/innerHTML
                 "purple"
                 "#bindable is bound to the :color property and was set to purple."))))

(test "assoc-property! after a bind updates the DOM"
      (fn []
        (let [bike-shed (->
                          (dom-binding/create-dom-map {:color "purple"})
                          (dom-binding/add-dom-bind! :color :#bindable)
                          (dom-binding/assoc-property! :color "red"))
              binded-element (.querySelector js/document "#bindable")]
          (equal binded-element/innerHTML
                 "red"
                 "#bindable was changed to red after a set."))))

(module "Transforming")
(test "Content is transformed before being inserted into the DOM"
      (fn []
        (let [bike-shed (->
                          (dom-binding/create-dom-map {:color "purple"})
                          (dom-binding/add-dom-bind! 
                            :color :#bindable
                            :transform 
                             (fn [content]
                               (cond
                                 (= content "purple") "violet"))))
              binded-element (.querySelector js/document "#bindable")]
          (equal binded-element/innerHTML
                 "violet"
                 "#bindable's content was transformed from purple to violet."))))
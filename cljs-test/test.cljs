(ns dom-binding.test
  [:require [dom-binding.core :as dom-binding] 
            [jayq.core :as jayq]]
  [:use [qunit :only [module test equal deep-equal]]])

(.start js/QUnit)

(module "Setting Properties")

(test "Constructor Test" 
      (fn []
        (let [bike-shed (dom-binding/create-dom-binding {:color "purple"})]
          (deep-equal bike-shed
                      {:color {:content "purple"}}
                      "The constructor allows properties to be properly created and applied"))))

(test "Set Test" 
      (fn [] 
        (let [bike-shed (dom-binding/create-dom-binding)]
          (deep-equal (dom-binding/set-property bike-shed :color "purple")
                      {:color {:content "purple"}}
                      "The set function will return a map with a new property on it."))))

(test "Update Test"
      (fn []
        (let [bike-shed (dom-binding/create-dom-binding {:color "purple"})]
          (deep-equal (dom-binding/set-property bike-shed :color "red")
                      {:color {:content "red"}}
                      "The set function will return a changed map when updating an existing property."))))

(module "Binding Properties")

(test "Selector Test"
      (fn []
        (let [expected-element (.querySelectorAll js/document ".multi-bind")]
          (deep-equal (dom-binding/->dom-nodes :.multi-bind)
                      expected-element
                      "The keyword selector :.multi-bind returns the expectedvector of nodes.")
          (deep-equal (dom-binding/->dom-nodes ".multi-bind")
                      expected-element
                      "The string selector '.multi-bind' returns the expected vector of nodes."))))
          ; (deep-equal (dom-binding/->dom-nodes (jayq.core.$ :.multi-bind))
          ;             expected-element
          ;             "The jQuery nodes ($ :.multi-bind) returns the expected vector of nodes.")
          ; (deep-equal (dom-binding/->dom-nodes (.querySelectorAll 
          ;                                        js/document 
          ;                                        ".multi-bind"))
          ;             expected-element
          ;             "The NodeList returned from querySelectorAll '.multi-bind' returns the expected vector of nodes."))))

(test "Binding Test"
      (fn []
        (let [bike-shed (->
                          (dom-binding/create-dom-binding {:color "purple"})
                          (dom-binding/bind :color :#bindable))
              binded-element (.querySelector js/document clj->js "#bindable")]
          (equal (.innerHTML js/binded-element)
                 (clj->js "purple")
                 "#bindable is bound to the :color property"))))
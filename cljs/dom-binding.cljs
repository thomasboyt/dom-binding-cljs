(ns dom-binding.core)
  ;(:require [jayq.core :as jayq]))

; binding map that gets passed:
; {:color {:content "purple"
;          :bindings {
;                     :element <some dom node>
;                     :type :content or :class
;                     :transform fn [value] ()}}}

(defn create-dom-binding 
  ([] {})
  ; wrap incoming properties in a map
  ([properties] (zipmap (keys properties) 
                        (map (fn [value] {:content value}) (vals properties)))))
  
(defn set-property [dom-mapping property value]
  (let [new-mapping (assoc-in dom-mapping [property :content] value)]
    (doseq [binding (get-in dom-mapping [property :bindings])]
      (.log js/console "iterating over bindings"))
    new-mapping))


; (defn ->dom-nodes [selector]
;   ; possible types for selector:
;   ;   element node - how is this represented? too bad i don't have a working repl...
;   ;   element array - see above. should be a collection (.coll?) of element nodes?
;   ;   jquery node - check jayq
;   ;   jquery array of nodes - check jayq
;   ;   selector - string (string?) or keyword (keyword?)
;   (let [selector (js->clj selector)]
;     (cond
;       ; selector string or keyword
;       (or (string? selector) (keyword? selector)) 
;         (.querySelectorAll js/document (clj->js selector))
;       ; NodeList (no-op)
;       (= js/NodeList (type selector))
;         selector
;       ; assume jayq array. no easy way to check, really :I
;       (coll? selector)
;         )))
      
; temporary simplifed version that only takes a selector, not existing nodes      
(defn ->dom-nodes [selector]
  (.querySelectorAll js/document (clj->js selector)))

(defn bind [dom-mapping property selector 
            & {:keys [type transform]
            :or {type :content 
                 transform (fn [x] x)}}]
  (let [nodes (->dom-nodes selector)]
    {:element the-node :type type :transform transform}))
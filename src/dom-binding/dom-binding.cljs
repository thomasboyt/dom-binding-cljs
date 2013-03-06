(ns dom-binding.core)

; binding map that gets passed:
; {:color {:content "purple"
;          :bindings {
;                     :element <some dom node>
;                     :type :content or :class
;                     :transform fn [value] ()}}}

;; JavaScript's NodeList type is weird and not really an array, so Clojurescript doesn't add the seqable interface to it. Here's a manual implementation.
(extend-type js/NodeList
  ISeqable
  (-seq [coll] (when (.item coll 0) coll))
  
  ISeq
  (-first [coll] (.item coll 0))
  (-rest [coll] 
         (map (fn [i] (.item coll i))
              (range 1 coll/length))))

;; Constructor for the dom map
(defn create-dom-map 
  ([] {})
  ([properties] (into {} (map (fn [[k v]] [k {:content v}]) properties))))

;; Utility function: update the dom for a property given its content and binding map
(defn dom-update! [property-content bind-map]
    (let [el (clj->js (:element bind-map))
          type (:type bind-map)]
      (case type
        :content
          (set! el/innerHTML ((:transform bind-map) property-content))
        :class
          (.addClass el property-content))))

;; Associates a property on the map.  
(defn assoc-property! [dom-map property value]
  (doseq [binding-item (get-in dom-map [property :bindings])]
      (dom-update! value binding-item))
  (assoc-in dom-map [property :content] value))

;; Shorthand for retrieving a property
(defn get-property [dom-map property]
  (get-in dom-map [property :content]))

;; Utility function: converts a selector to individual DOM nodes.     
(defn selector->dom-nodes [selector]
  (.querySelectorAll js/document (clj->js selector)))

;; Adds a binding to the dom-map.
(defn add-dom-bind! [dom-map property selector 
                    & {:keys [type transform]
                       :or {type :content 
                            transform identity}}]
  (let [nodes (selector->dom-nodes selector)]
    (update-in dom-map [property :bindings]
               concat (doall (map
                               (fn [element] 
                                 (let [bind {:element element 
                                             :type type 
                                             :transform transform}]
                                   (dom-update! (get-in dom-map [property :content]) bind)
                                   bind))
                               nodes)))))

(ns firetalk.spec
  (:require [clojure.spec.alpha :as s])
  (:import  [com.google.cloud.firestore Firestore Query]))

(defn firestore? [x] (instance? Firestore x))
(defn query? [x] (instance? Query x))

(s/def ::where-clause
  (s/cat
    :oper #{'= 'in '> '>= '< '<=}
    :key (s/or :field string? :path (s/coll-of string?))
    :value any?))

(s/def ::limit (s/and int? pos?))

(s/def ::collection (s/or :string string? :path (s/coll-of string?)))

(s/def ::where (s/coll-of ::where-clause))

;; Example:
;; {:collection "users"
;;  :where [[= "name" "Ritchie"]
;;          [< "age" 10]]
;;  :limit 10}

(s/def ::query
  (s/keys
    :req [::where ::collection]
    :opt [::limit]))

(s/fdef parse-query
        :args ::query
        :ret (s/fspec
               :arg firestore?
               :ret query?))

(s/fdef execute!
  :args (s/cat :client firestore?
               :query ::query)
  :ret map?)

(s/def ::document
  (s/map-of
    (s/or :keyword keyword? :string string? :symbol symbol?)
    any?))

(s/def ::documents
  (s/coll-of ::document))

(s/def ::write
  (s/keys
    :req [::collection ::documents]))

(ns cljctools.mult.spec
  (:require
   [clojure.spec.alpha :as s]
   [cljctools.mult.protocols :as mult.protocols]
   [cljctools.socket.spec :as socket.spec]))

(s/def ::tab-id string?)
(s/def ::tab-title string?)

(s/def ::filepath string?)
(s/def ::ns-symbol symbol?)

(s/def ::channel #?(:clj #(instance? clojure.core.async.impl.channels.ManyToManyChannel %)
                    :cljs #(instance? cljs.core.async.impl.channels/ManyToManyChannel %)))
(s/def ::mult #?(:clj #(satisfies? clojure.core.async.Mult %)
                 :cljs #(satisfies? cljs.core.async/Mult %)))




(s/def ::on-tab-closed ifn?)
(s/def ::on-tab-message ifn?)

(s/def ::editor #(and
                  (satisfies? mult.protocols/Editor %)
                  (satisfies? mult.protocols/Release %)
                  #?(:clj (satisfies? clojure.lang.IDeref %))
                  #?(:cljs (satisfies? cljs.core/IDeref %))))

(s/def ::text-editor #(satisfies? mult.protocols/TextEditor %))

(s/def ::range (s/tuple int? int? int? int?))

(s/def ::tab #(and
               (satisfies? mult.protocols/Tab %)
               (satisfies? mult.protocols/Open %)
               (satisfies? mult.protocols/Close %)
               (satisfies? mult.protocols/Send %)
               (satisfies? mult.protocols/Active? %)
               (satisfies? mult.protocols/Release %)
               #?(:clj (satisfies? clojure.lang.IDeref %))
               #?(:cljs (satisfies? cljs.core/IDeref %))))

(s/def ::cljctools-mult #(and
                          (satisfies? mult.protocols/CljctoolsMult %)
                          #?(:clj (satisfies? clojure.lang.IDeref %))
                          #?(:cljs (satisfies? cljs.core/IDeref %))))

(s/def ::logical-repl #(and
                        (satisfies? mult.protocols/LogicalRepl %)
                        (satisfies? mult.protocols/Release %)
                        #?(:clj (satisfies? clojure.lang.IDeref %))
                        #?(:cljs (satisfies? cljs.core/IDeref %))))

(s/def ::code-string string?)
(s/def ::logical-repl-eval-opts (s/keys :req [::code-string
                                              ::ns-symbol]))


(s/def ::connection-meta-id keyword?)
(s/def ::repl-protocol #{:nrepl})
(s/def ::connection-opts-type #{::socket.spec/tcp-socket-opts
                                ::socket.spec/websocket-opts})
(s/def ::connection-opts (s/or
                          ::socket.spec/tcp-socket-opts ::socket.spec/tcp-socket-opts
                          ::socket.spec/websocket-opts ::socket.spec/websocket-opts))

(s/def ::connection-meta (s/keys :req [::connection-meta-id
                                       ::repl-protocol
                                       ::connection-opts
                                       ::connection-opts-type]))

(s/def ::connection-metas (s/coll-of ::connection-meta :into #{}))

(s/def ::logical-repl-meta-id keyword?)
(s/def ::logical-repl-type #{:shadow-cljs :nrepl})
(s/def ::runtime #{:cljs :clj})
(s/def ::shadow-build-key keyword?)
(s/def ::include-file? (s/or
                        ::ifn? ifn?
                        ::list? list?))

(s/def ::logical-repl-meta (s/keys :req [::logical-repl-meta-id
                                         ::connection-meta-id
                                         ::logical-repl-type
                                         ::runtime
                                         ::include-file?]
                                   :opt [::shadow-build-key]))

(s/def ::logical-repl-metas (s/coll-of ::logical-repl-meta :into #{}))

(s/def ::tab-meta-id keyword?)
(s/def ::logical-repl-meta-ids (s/coll-of ::logical-repl-meta-id :into #{}))

(s/def ::tab-meta (s/keys :req [::tab-meta-id
                                   ::logical-repl-meta-ids]))

(s/def ::open-tabs (s/coll-of ::tab-meta-id :into #{}))
(s/def ::active-tab ::tab-meta-id)

(s/def ::config (s/keys :req [::connection-metas
                              ::logical-repl-metas
                              ::tab-metas
                              ::open-tabs
                              ::active-tab]))

(s/def ::config-as-data ::config)

(s/def ::cmd| ::channel)
(s/def ::cmd #{::cmd-open
               ::cmd-ping
               ::cmd-eval})

(s/def ::op| ::channel)
(s/def ::op #{::op-ping
              ::op-eval
              ::op-update-ui-state
              ::op-did-change-active-text-editor})

(s/def ::eval-result (s/nilable string?))

(s/def ::op-value (s/keys :req-un [::op]
                          :opt []))


(s/def ::ui-state (s/keys :req []
                          :opt [::eval-result
                                ::ns-symbol
                                ::config-as-data
                                ::logical-repl-meta-id]))



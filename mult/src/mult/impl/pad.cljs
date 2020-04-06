(ns mult.impl.pad
  (:require
   [clojure.core.async :as a :refer [<! >!  chan go alt! take! put! offer! poll! alts! pub sub
                                     timeout close! to-chan go-loop sliding-buffer dropping-buffer
                                     pipeline pipeline-async]]
   [goog.string :refer [format]]
   [clojure.string :as string]
   ["fs" :as fs]
   ["path" :as path]
   ["net" :as net]
   ["bencode" :as bencode]
   [cljs.reader :refer [read-string]]
   [bencode-cljc.core :refer [serialize deserialize]]
   [mult.protocols.proc :refer [Proc]]))

(defn hello-fn []
  (.. vscode.window (showInformationMessage
                     (format "Hello World!!! %s" (type (chan 1)))
                     #_(str "Hello World!" (type (chan 1)))))
  #_(go
      (<! (timeout 3000))
      (put! (channels :ch/test|) {:op :show-info-msg})))

(comment

  (js/console.log 3)
  (js/console.log format)
  (go
    (<! (timeout 2000))
    (js/console.log (type format))
    (js/console.log (format "Hello World! %s" 123))

    (<! (timeout 1000))
    (js/console.log "done"))
  (hello-fn)
  ;;
  )

(comment

  (offer! (channels :ch/editor-ops|)  {:op :show-information-message
                                       :inforamtion-message "message via repl via channel"})

  ;;
  )

(comment

  (def data$ (atom nil))

  (defn on-data
    [buff]
    (println "; net/Socket data")
    (let [benstr (.toString buff)
          o (deserialize benstr)]
      (when (contains? o "value")
        (println o)
        (reset! data$ o))))

  (def ws (let [ws (net/Socket.)]
            (.connect ws #js {:port 5533 #_5511
                              :editor "localeditor"})
            (doto ws
              (.on "connect" (fn []
                               (println "; net/Socket connect")))
              (.on "ready" (fn []
                             (println "; net/Socket ready")))
              (.on "timeout" (fn []
                               (println "; net/Socket timeout")))
              (.on "close" (fn [hadError]
                             (println "; net/Socket close")
                             (println (format "hadError %s"  hadError))))
              (.on "data" (fn [buff] (on-data buff)))
              (.on "error" (fn [e]
                             (println "; net/Socket error")
                             (println e))))
            ws))

  (.write ws (str {:op "eval" :code "(+ 2 3)"}))
  (.write ws (str "error"))
  (dotimes [i 2]
    (.write ws (str {:op "eval" :code "(+ 2 3)"})))
  (dotimes [i 2]
    (.write ws (str "error")))


  (bencode/encode (str {:op "eval" :code "(+ 2 3)"}))
  (bencode/decode (bencode/encode (str {:op "eval" :code "(+ 2 3)"})))

  (.write ws (bencode/encode (str {:op "eval" :code "(+ 2 3)"})))


  (deserialize (serialize {:op "eval" :code "(+ 2 3)"}))

  ; clj only
  (binding [*ns* mult.vscode]
    [3 (type hello-fn)])

  (.write ws (serialize {:op "eval" :code "(+ 2 4)"}))

  (.write ws (serialize {:op "eval" :code "(do
                                           (in-ns 'abc.core)
                                           [(type foo) (foo)]
                                           )"}))



  ;;
  )
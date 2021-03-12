(ns cljctools.mult.protocols)

(defprotocol Release
  (release* [_]))

(defprotocol Active?
  (active?* [_]))

(defprotocol Send
  (send* [_ msg]))

(defprotocol Open
  (open* [_]))

(defprotocol Close
  (close* [_]))

(defprotocol CljctoolsMult
  #_IDeref)

(defprotocol Editor
  (show-notification* [_ text])
  (active-text-editor* [_])
  (create-tab* [_ opts])
  (read-mult-edn* [_])
  (init* [_] "creating editor is sync (for spec validation of return value), init* is async")
  #_Release
  #_IDeref)

(defprotocol TextEditor
  (text* [_] [_ range])
  (filepath* [_]))

(defprotocol Tab
  #_Open
  #_Close
  #_Send
  #_Active?
  #_Release
  #_IDeref)


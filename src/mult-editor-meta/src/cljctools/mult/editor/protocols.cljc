(ns cljctools.mult.editor.protocols)

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

(defprotocol Editor
  (show-notification* [_ text])
  (active-text-editor* [_])
  (create-tab* [_ opts])
  (read-mult-edn* [_])
  #_Release
  #_IDeref)

(defprotocol TextEditor
  (text* [_] [_ range])
  (filepath* [_])
  (selection* [_])
  (replace* [_ text]))

(defprotocol Tab
  #_Open
  #_Close
  #_Send
  #_Active?
  #_Release
  #_IDeref)
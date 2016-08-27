(ns yimp.config)

(def thecatapi-key "ODk2MzY")

(def app-name "YIMP")

(def default-catapi-params
  {:format  "xml"
   :size    "med"
   :api-key thecatapi-key})

(def env
  {:hostname "http://localhost:3000"})
  ; {:hostname "http://yimp.herokuapp.com"})

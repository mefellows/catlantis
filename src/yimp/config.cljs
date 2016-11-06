(ns yimp.config)

(def thecatapi-key "ODk2MzY")

(def app-name "YIMP")

(def default-catapi-params
  {:format  "xml"
   :size    "med"
   :api-key thecatapi-key})

(def env
  {:hostname "http://yimp.herokuapp.com"
 ; {:hostname "http://localhost:3000"
  :school-id 1})

  ;; http://openweathermap.org/api
  (def openweathermap-appid
    "c6c71a25563f331e7857e4f53ddd6a48")

  ;; https://datamarket.azure.com/dataset/bing/search
  (def bing-appid
    "TkhIUUl4cjJFSWlUSHY4ZW1sQmd4MCtkb1Foc1hlRk5aQTJKMlhWVHlvazpOSEhRSXhyMkVJaVRIdjhlbWxCZ3gwK2RvUWhzWGVGTlpBMkoyWFZUeW9r")

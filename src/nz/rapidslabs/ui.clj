(ns nz.rapidslabs.ui
  (:require [cheshire.core :as cheshire]
            [clojure.java.io :as io]
            [nz.rapidslabs.settings :as settings]
            [com.biffweb :as biff]
            [ring.middleware.anti-forgery :as csrf]
            [ring.util.response :as ring-response]
            [rum.core :as rum]))

(defn css-path []
  (if-some [last-modified (some-> (io/resource "public/css/main.css")
                                  ring-response/resource-data
                                  :last-modified
                                  (.getTime))]
    (str "/css/main.css?t=" last-modified)
    "/css/main.css"))

(defn js-path []
  (if-some [last-modified (some-> (io/resource "public/js/main.js")
                                  ring-response/resource-data
                                  :last-modified
                                  (.getTime))]
    (str "/js/main.js?t=" last-modified)
    "/js/main.js"))

(defn base [{:keys [::recaptcha] :as ctx} & body]
  (apply
   biff/base-html
   (-> ctx
       (merge #:base{:title settings/app-name
                     :lang "en-US"
                     :icon "/img/glider.png"
                     :description "For all your concreting needs in the Wellington region"
                     :image "https://www.rapidslabs.co.nz/images/256/152/header-logo1?h=0b781650"})
       (update :base/head (fn [head]
                            (concat [[:link {:rel "stylesheet" :href (css-path)}]
                                     [:script {:src (js-path)}]
                                     [:script {:src "https://unpkg.com/htmx.org@2.0.3"}]
                                     [:script {:src "https://unpkg.com/htmx.org@2.0.3/dist/ext/ws.js"}]
                                     [:script {:src "https://unpkg.com/hyperscript.org@0.9.12"}]
                                     (when recaptcha
                                       [:script {:src "https://www.google.com/recaptcha/api.js"
                                                 :async "async" :defer "defer"}])]
                                    head))))
   body))

(def phone
  [:svg.size-6 {:fill "currentColor" :viewBox "0 0 24 24"
                :path {:fill-rule "evenodd" :clip-rule "evenodd"
                       :d "M1.5 4.5a3 3 0 0 1 3-3h1.372c.86 0 1.61.586 1.819 1.42l1.105 4.423a1.875 1.875 0 0 1-.694 1.955l-1.293.97c-.135.101-.164.249-.126.352a11.285 11.285 0 0 0 6.697 6.697c.103.038.25.009.352-.126l.97-1.293a1.875 1.875 0 0 1 1.955-.694l4.423 1.105c.834.209 1.42.959 1.42 1.82V19.5a3 3 0 0 1-3 3h-2.25C8.552 22.5 1.5 15.448 1.5 6.75V4.5Z"}}])


(defn page [ctx & body]
  (base
   ctx
   [:.bg-zinc-100.flex.flex-col.flex-grow {:class "dark:bg-zinc-500"}
    [:.p-4.mx-auto.max-w-8xl.w-full
     (when (bound? #'csrf/*anti-forgery-token*)
       {:hx-headers (cheshire/generate-string
                     {:x-csrf-token csrf/*anti-forgery-token*})})

     body]
    [:.p-4.mx-auto.max-w-8xl.w-full
     [:.text-base.text-orange-500.mb-4
      "Under construction: If you need help, call Juppy " [:a {:href "tel:0275600679"} "027 560 0679" ] " or Nick 021 453 070"]]
    [:.flex-grow]
    [:.flex-grow]]))

(defn on-error [{:keys [status ex] :as ctx}]
  {:status status
   :headers {"content-type" "text/html"}
   :body (rum/render-static-markup
          (page
           ctx
           [:h1.text-lg.font-bold
            (if (= status 404)
              "Page not found."
              "Something went wrong.")]))})

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


(def tabs
  [:<>.border-b.border-stone-200.mb-4
   [:ul#tabs
    {:class '["hidden" "text-sm" "font-medium" "text-center" "text-gray-500" "rounded-lg" "shadow" "sm:flex" "dark:divide-stone-700" "dark:text-stone-400"]
     :role "tablist" }
    [:li
     {:class "w-full focus-within:z-10"}
     [:a
      { :href "#home",
       :class
       "inline-block w-full p-4 text-stone-900 bg-stone-100 border-r border-stone-200 dark:border-stone-700 rounded-s-lg focus:ring-4 focus:ring-blue-300 active focus:outline-none dark:bg-stone-700 dark:text-white"
       :data-tabs-toggle "#home"}
      "Home"]]
    [:li
     {:class "w-full focus-within:z-10"}
     [:a
      { :href "/about",
       :class
       "inline-block w-full p-4 bg-white border-r border-stone-200 dark:border-stone-700 hover:text-stone-700 hover:bg-stone-50 focus:ring-4 focus:ring-blue-300 focus:outline-none dark:hover:text-white dark:bg-stone-800 dark:hover:bg-stone-700"
       :data-tabs-toggle "#tabcontent"}
      "About Us"]]
    [:li
     {:class "w-full focus-within:z-10"}
     [:a
      { :href "#enquiry",
       :class
       "inline-block w-full p-4 bg-cyan border-r border-stone-200 dark:border-stone-700 hover:text-stone-700 hover:bg-stone-50 focus:ring-4 focus:ring-blue-300 focus:outline-none dark:hover:text-white dark:bg-stone-800 dark:hover:bg-stone-700"}
      "Quote / Enquiry"]]
    [:li
     {:class "w-full focus-within:z-10"}
     [:a
      {:href "#concrete",
       :class
       "inline-block w-full p-4 bg-cyan border-s-0 border-stone-200 dark:border-stone-700 rounded-e-lg hover:text-stone-700 hover:bg-stone-50 focus:ring-4 focus:outline-none focus:ring-blue-300 dark:hover:text-white dark:bg-stone-800 dark:hover:bg-stone-700"}
      "Concrete Stuff"]]
    [:li
     {:class "w-full focus-within:z-10"}
     [:a
      {:href "#contact",
       :class
       "inline-block w-full p-4 bg-cyan border-s-0 border-stone-200 dark:border-stone-700 rounded-e-lg hover:text-stone-700 hover:bg-stone-50 focus:ring-4 focus:outline-none focus:ring-blue-300 dark:hover:text-white dark:bg-stone-800 dark:hover:bg-stone-700"
       :data-tabs-toggle "#contact"}
      "Contact Us"]]
    ]
   [:#tabcontent
    [:#home
     {
      :hx-get     "/home",
      :hx-trigger "load delay:100ms",
      :hx-target  "#tabcontent",
      :hx-swap    "innerHTML"}]]
   ])

(defn page [ctx & body]
  (base
   ctx
   [:.bg-zinc-100.flex.flex-col.flex-grow
    [:.p-3.mx-auto.max-w-6xl.w-full
     (when (bound? #'csrf/*anti-forgery-token*)
       {:hx-headers (cheshire/generate-string
                      {:x-csrf-token csrf/*anti-forgery-token*})})

     body]
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

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

(defn twe-path []
  (if-some [last-modified (some-> (io/resource "public/js/tw-elements.umd.min.js")
                                  ring-response/resource-data
                                  :last-modified
                                  (.getTime))]
    (str "/js/tw-elements.umd.min.js?t=" last-modified)
    "/js/tw-elements.umd.min.js"))

(defn base [{:keys [::recaptcha] :as ctx} & body]
  (apply
   biff/base-html
   (-> ctx
       (merge #:base{:title settings/app-name
                     :lang "en-NZ"
                     :icon "/img/rapidslabs-logo.png"
                     :description "For all your concreting needs in the Wellington region"
                     :image "https://www.rapidslabs.co.nz/images/256/152/header-logo1?h=0b781650"})
       (update :base/head (fn [head]
                            (concat [[:link {:rel "stylesheet" :href (css-path)}]
                                     [:link {:ref "stylesheet" :href "https://cdn.jsdelivr.net/npm/tw-elements/css/tw-elements.min.css"}]
                                     [:script {:src (twe-path)}]
                                     [:script {:src (js-path) :type "module"}]
                                     ;;  [:script {:src "https://unpkg.com/htmx.org@2.0.3"}]
                                     ;;  [:script {:src "https://unpkg.com/hyperscript.org@0.9.12"}]
                                     (when recaptcha
                                       [:script {:src "https://www.google.com/recaptcha/api.js"
                                                 :async "async" :defer "defer"}])]
                                    head))))
   body))


(defn page [ctx & body]
  (base
   ctx
   ;;  (nav)
   [:.bg-orange-300.flex.flex-col {:class "dark:bg-orange-800"}
    [:.flex-grow]
    [:.p-3.mx-auto.max-w-screen-sm.w-full
     (when (bound? #'csrf/*anti-forgery-token*)
       {:hx-headers (cheshire/generate-string
                     {:x-csrf-token csrf/*anti-forgery-token*})})
     body]
    [:.flex-grow]
    [:.flex-grow]
    ]
   ))

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

(def phone-svg
  [:span
   {:class "me-3 [&>svg]:h-5 [&>svg]:w-5"}
   [:svg
    {:xmlns "http://www.w3.org/2000/svg",
     :fill "none",
     :viewBox "0 0 24 24",
     :stroke-width "1.5",
     :stroke "currentColor",
     :class "size-6"}
    [:path
     {:stroke-linecap "round",
      :stroke-linejoin "round",
      :d
      "M2.25 6.75c0 8.284 6.716 15 15 15h2.25a2.25 2.25 0 0 0 2.25-2.25v-1.372c0-.516-.351-.966-.852-1.091l-4.423-1.106c-.44-.11-.902.055-1.173.417l-.97 1.293c-.282.376-.769.542-1.21.38a12.035 12.035 0 0 1-7.143-7.143c-.162-.441.004-.928.38-1.21l1.293-.97c.363-.271.527-.734.417-1.173L6.963 3.102a1.125 1.125 0 0 0-1.091-.852H4.5A2.25 2.25 0 0 0 2.25 4.5v2.25Z"}]]])

(def mobile-svg
  [:span
       {:class "me-3 [&>svg]:h-5 [&>svg]:w-5 hover:text-cyan"}
       [:svg
        {:xmlns "http://www.w3.org/2000/svg",
         :fill "fill-stone-200",
         :viewBox "0 0 24 24",
         :stroke-width "1.5",
         :stroke "currentColor",
         :class "size-6"}
        [:path
         {:stroke-linecap "round",
          :stroke-linejoin "round",
          :d
          "M10.5 1.5H8.25A2.25 2.25 0 0 0 6 3.75v16.5a2.25 2.25 0 0 0 2.25 2.25h7.5A2.25 2.25 0 0 0 18 20.25V3.75a2.25 2.25 0 0 0-2.25-2.25H13.5m-3 0V3h3V1.5m-3 0h3m-3 18.75h3"}]]])

(def email-svg
  [:span
   {:class "me-3 [&>svg]:h-5 [&>svg]:w-5"}
   [:svg
    {:xmlns "http://www.w3.org/2000/svg",
     :viewBox "0 0 24 24",
     :fill "currentColor"}
    [:path
     {:d
      "M1.5 8.67v8.58a3 3 0 003 3h15a3 3 0 003-3V8.67l-8.928 5.493a3 3 0 01-3.144 0L1.5 8.67z"}]
    [:path
     {:d
      "M22.5 6.908V6.75a3 3 0 00-3-3h-15a3 3 0 00-3 3v.158l9.714 5.978a1.5 1.5 0 001.572 0L22.5 6.908z"}]]])

(def address-svg
  [:span
       {:class "me-3 [&>svg]:h-5 [&>svg]:w-5"}
       [:svg
        {:xmlns "http://www.w3.org/2000/svg",
         :viewBox "0 0 24 24",
         :fill "currentColor"}
        [:path
         {:d
          "M11.47 3.84a.75.75 0 011.06 0l8.69 8.69a.75.75 0 101.06-1.06l-8.689-8.69a2.25 2.25 0 00-3.182 0l-8.69 8.69a.75.75 0 001.061 1.06l8.69-8.69z"}]
        [:path
         {:d
          "M12 5.432l8.159 8.159c.03.03.06.058.091.086v6.198c0 1.035-.84 1.875-1.875 1.875H15a.75.75 0 01-.75-.75v-4.5a.75.75 0 00-.75-.75h-3a.75.75 0 00-.75.75V21a.75.75 0 01-.75.75H5.625a1.875 1.875 0 01-1.875-1.875v-6.198a2.29 2.29 0 00.091-.086L12 5.43z"}]]])

(def juppy-tel [:a {:href "tel:0275600679" :alt "027 560 0679"} " Juppy " ])
(def nick-tel [:a {:href "tel:021453070" :alt "021 453 070"} " Nick " ])
(def contact-us [:a {:href "mail:contactus@rapidslabs.co.nz" :alt "contactus@rapidslabs.co.nz"} "contactus@rapidslabs.co.nz"])

;; (defn contact-section []
;;   [:<>
;;    [:.mb-9.flex.justify-center.text-neutral-700
;;     {:class  "text-surface/75 dark:bg-neutral-700 dark:text-white/75 lg:text-left"}
;;    [:.flex.flex-col.items-center.justify-center
;;     {:class
;;      "border-b-2 border-neutral-200 p-6 dark:border-white/10 lg:justify-between"}
;;     [:.flex.flex-row
;;      [:p.mb-4.mx-4.flex.flex-row.justify-center.font-semibold.uppercase {:class "md:justify-start"}
;;       "Contact "]
;;      [:p.mb-4.flex.items-center.justify-center {:class "md:justify-start hover:text-cyan"}
;;       address-svg
;;       " 7A Kapuni Grove, Porirua "]
;;      [:p.mb-4.mx-4.flex.items-center.justify-center {:class "md:justify-start hover:text-cyan"}
;;       email-svg
;;       contact-us]
;;      [:p.mb-4.mx-4.flex.items-center.justify-center {:class "md:justify-start"}
;;       mobile-svg
;;       nick-tel]
;;      [:p.mb-4.mx-4.flex.items-center.justify-center {:class "md:justify-start"}
;;       mobile-svg
;;       juppy-tel]]]]]
;;   )

(defn contact-section []
  [:<>
   [:.mb-9.flex.justify-center.text-stone-700
    {:class "dark:text-stone-200"}
    [:.flex.flex-col.items-center.justify-center.border-b-2.border-stone-200.p-6
     {:class "dark:text-stone-700"}
     [:.flex.flex-row
      [:p.mb-4.mx-4.font-semibold.uppercase "Contact"]
      [:p.mb-4.flex.items-center.justify-center.hover:text-stone-500
       address-svg "7A Kapuni Grove, Porirua"]
      [:p.mb-4.mx-4.flex.items-center.justify-center
       {:class "hover:text-stone-500"}
       email-svg contact-us]
      [:p.mb-4.mx-4.flex.items-center.justify-center
       {:class "hover:text-stone-500"}
       mobile-svg nick-tel]
      [:p.mb-4.mx-4.flex.items-center.justify-center
       {:class "hover:text-stone-500"}
       mobile-svg juppy-tel]]]]])

(def phone
  [:svg.size-6 {:fill "currentColor" :viewBox "0 0 24 24"
                :path {:fill-rule "evenodd" :clip-rule "evenodd"
                       :d "M1.5 4.5a3 3 0 0 1 3-3h1.372c.86 0 1.61.586 1.819 1.42l1.105 4.423a1.875 1.875 0 0 1-.694 1.955l-1.293.97c-.135.101-.164.249-.126.352a11.285 11.285 0 0 0 6.697 6.697c.103.038.25.009.352-.126l.97-1.293a1.875 1.875 0 0 1 1.955-.694l4.423 1.105c.834.209 1.42.959 1.42 1.82V19.5a3 3 0 0 1-3 3h-2.25C8.552 22.5 1.5 15.448 1.5 6.75V4.5Z"}}])



(def nav
  [:nav.block.w-full.px-4.py-2.bg-neutral-100.bg-opacity-90.sticky.top-3.shadow
   {:class "lg:px-8 lg:py-3 backdrop-blur-lg backdrop-saturate-150 z-[9999]"
    :data-twe-navbar-ref ""
    }
   [:.mx-auto.flex.flex-wrap.items-center.justify-between.px-3
    {:class "md:container"}
    [:a {:href "/#",
         :class
         "mr-4 block cursor-pointer py-1.5 text-base text-slate-200 font-semibold"}
     [:img.object-contain.h-12.bg-stone-800 {:src "/img/rapidslabs-logo.webp"}]]
    [:div
     {:class "hidden lg:block"}
     [:ul.flex.flex-col.gap-2.mt-2.mb-4
      {:class "lg:mb-0 lg:mt-0 lg:flex-row lg:items-center lg:gap-6"}
      [:li.flex.items-center.p-1.text-sm.gap-x-2.text-slate-800
       [:a {:href "/services", :class "flex items-center"} "Services"]]
      [:li.flex.items-center.p-1.text-sm.gap-x-2.text-slate-800
       [:a {:href "/projects", :class "flex items-center"} "Projects"]]
      [:li.flex.items-center.p-1.text-sm.gap-x-2.text-slate-800
       [:a {:href "/products", :class "flex items-center"} "Products"]]
      [:li.flex.items-center.p-1.text-sm.gap-x-2.text-slate-800
       [:a {:href "/about", :class "flex items-center"} "About Us"]
       [:a
        {:class
         "mr-4 text-neutral-500 hover:text-neutral-700 focus:text-neutral-700 disabled:text-black/30 dark:text-neutral-200 dark:hover:text-neutral-300 dark:focus:text-neutral-300 [&.active]:text-black/90 dark:[&.active]:text-neutral-400",
         :href "/about"}
        [:span
         {:class "[&>svg]:w-5"}
         [:svg
          {:xmlns "http://www.w3.org/2000/svg",
           :viewBox "0 0 24 24",
           :fill "currentColor"}
          [:path
           {:fill-rule "evenodd",
            :d
            "M7.5 6a4.5 4.5 0 119 0 4.5 4.5 0 01-9 0zM3.751 20.105a8.25 8.25 0 0116.498 0 .75.75 0 01-.437.695A18.683 18.683 0 0112 22.5c-2.786 0-5.433-.608-7.812-1.7a.75.75 0 01-.437-.695z",
            :clip-rule "evenodd"}]]]]]
      ]]
    [:button
     {:class
      "relative ml-auto h-6 max-h-[40px] w-6 max-w-[40px] select-none rounded-lg text-center align-middle text-xs font-medium uppercase text-inherit transition-all hover:bg-transparent focus:bg-transparent active:bg-transparent disabled:pointer-events-none disabled:opacity-50 disabled:shadow-none lg:hidden",
      :type "button"
      :data-twe-collapse-init "",
      :data-twe-target "#navbarSupportedContent1",
      :aria-controls "navbarSupportedContent1",
      :aria-expanded "false",
      :aria-label "Toggle navigation"
      }
     [:span
      {:class
       "absolute transform -translate-x-1/2 -translate-y-1/2 top-1/2 left-1/2"}
      [:svg
       {:xmlns "http://www.w3.org/2000/svg",
        :class "w-6 h-6",
        :fill "none",
        :stroke "currentColor",
        :stroke-width "2"}
       [:path
        {:stroke-linecap "round",
         :stroke-linejoin "round",
         :d "M4 6h16M4 12h16M4 18h16"}]]]]]])

(def nav1
  [:nav.bg-stone-100.shadow-md.sticky.top-0.z-50
   {:class "dark:bg-stone-800"}
   [:.mx-auto.flex.w-full.flex-wrap.items-center.justify-between.px-6.py-4
    {:class "md:container"}
    ;; Hamburger button for mobile
    [:button.block.border-0.bg-transparent.px-2.text-stone-600
     {:type "button"
      :class "hover:text-stone-800 focus:outline-none focus:ring-0 dark:text-stone-300 dark:hover:text-stone-100 lg:hidden"
      :data-twe-collapse-init true
      :data-twe-target "#navbarSupportedContent1"
      :aria-controls "navbarSupportedContent1"
      :aria-expanded "false"
      :aria-label "Toggle navigation"}
     [:span {:class "[&>svg]:w-7"}
      [:svg.h-7.w-7 {:xmlns "http://www.w3.org/2000/svg"
                     :viewBox "0 0 24 24"
                     :fill "currentColor"}
       [:path {:fill-rule "evenodd"
               :d "M3 6.75A.75.75 0 013.75 6h16.5a.75.75 0 010 1.5H3.75A.75.75 0 013 6.75zM3 12a.75.75 0 01.75-.75h16.5a.75.75 0 010 1.5H3.75A.75.75 0 013 12zm0 5.25a.75.75 0 01.75-.75h16.5a.75.75 0 010 1.5H3.75a.75.75 0 01-.75-.75z"
               :clip-rule "evenodd"}]]]]

    ;; Collapsible container
    [:#navbarSupportedContent1.hidden.flex-grow.items-center
     {:class "lg:flex"}
     [:a.mr-4.cursor-pointer.text-stone-900
      {:href "/#"
       :class "dark:text-stone-200"}
      [:img.object-contain.h-12.bg-stone-800 {:src "/img/rapidslabs-logo.webp"}]]
     ;; Navigation links
     [:ul.flex.list-none.mr-auto.flex-col.pl-0
       {:class "lg:flex"}
      [:li [:a.text-stone-600
            {:class "hover:text-stone-800 focus:text-stone-800 dark:text-stone-200 dark:hover:text-stone-100 lg:px-2"
             :href "/services"} "Services"]]
      [:li [:a.text-stone-600
            {:class "hover:text-stone-800 focus:text-stone-800 dark:text-stone-200 dark:hover:text-stone-100 lg:px-2"
             :href "/projects"} "Projects"]]
      [:li [:a.text-stone-600
            {:class "hover:text-stone-800 focus:text-stone-800 dark:text-stone-200 dark:hover:text-stone-100 lg:px-2"
             :href "/products"} "Products"]]]]

    ;; Right-side elements
    [:div.relative.flex.items-center
     [:a.mr-4.text-stone-600
      {:class "hover:text-stone-800 focus:text-stone-800 dark:text-stone-200 dark:hover:text-stone-100"
       :href "/contact-us"}
      [:svg {:class "w-5" :xmlns "http://www.w3.org/2000/svg"
             :viewBox "0 0 24 24"
             :fill "currentColor"}
       [:path {:d "M1.5 8.67v8.58a3 3 0 003 3h15a3 3 0 003-3V8.67l-8.928 5.493a3 3 0 01-3.144 0L1.5 8.67z"}]
       [:path {:d "M22.5 6.908V6.75a3 3 0 00-3-3h-15a3 3 0 00-3 3v.158l9.714 5.978a1.5 1.5 0 001.572 0L22.5 6.908z"}]]]
     [:a.mr-4.text-stone-600
      {:class "hover:text-stone-800 focus:text-stone-800 dark:text-stone-200 dark:hover:text-stone-100"
       :href "/about"}
      [:svg {:class "w-5" :xmlns "http://www.w3.org/2000/svg"
             :viewBox "0 0 24 24"
             :fill "currentColor"}
       [:path {:fill-rule "evenodd"
               :d "M7.5 6a4.5 4.5 0 119 0 4.5 4.5 0 01-9 0zM3.751 20.105a8.25 8.25 0 0116.498 0 .75.75 0 01-.437.695A18.683 18.683 0 0112 22.5c-2.786 0-5.433-.608-7.812-1.7a.75.75 0 01-.437-.695z"
               :clip-rule "evenodd"}]]]]]])

(def nav2
  [:nav.bg-stone-100.shadow-md.sticky.top-0.z-50
   {:class
    "flex-no-wrap relative w-full min-h-[120px] max-h-[120px] items-center justify-between bg-neutral-100 py-2 shadow-md shadow-black/5 dark:bg-neutral-600 dark:shadow-black/10 lg:flex-wrap lg:justify-start lg:py-4",
    :data-twe-navbar-ref true}
   ;; need a container
   [:div
    {:class
     "md:container mx-auto flex w-full flex-wrap items-center justify-between px-6 py-4"}
    ;; Hamburger button for mobile
    [:button
     {:class
      "block border-0 bg-transparent px-2 text-neutral-500 hover:no-underline hover:shadow-none focus:no-underline focus:shadow-none focus:outline-none focus:ring-0 dark:text-neutral-200 lg:hidden",
      :type "button",
      :data-twe-collapse-init true,
      :data-twe-target "#navbarSupportedContent1",
      :aria-controls "navbarSupportedContent1",
      :aria-expanded "false",
      :aria-label "Toggle navigation"}
     (comment "Hamburger icon")
     [:span
      {:class "[&>svg]:w-7"}
      [:svg
       {:xmlns "http://www.w3.org/2000/svg",
        :viewBox "0 0 24 24",
        :fill "currentColor",
        :class "h-7 w-7"}
       [:path
        {:fill-rule "evenodd",
         :d
         "M3 6.75A.75.75 0 013.75 6h16.5a.75.75 0 010 1.5H3.75A.75.75 0 013 6.75zM3 12a.75.75 0 01.75-.75h16.5a.75.75 0 010 1.5H3.75A.75.75 0 013 12zm0 5.25a.75.75 0 01.75-.75h16.5a.75.75 0 010 1.5H3.75a.75.75 0 01-.75-.75z",
         :clip-rule "evenodd"}]]]]
    (comment "Collapsible navigation container")
    [:div
     {:class
      "!visible hidden flex-grow basis-[100%] items-center lg:!flex lg:basis-auto",
      :id "navbarSupportedContent1",
      :data-twe-collapse-item true
      }
     ;; rapidslabs logo
     [:a {:href "/",
          :class
          "mr-4 block cursor-pointer py-1.5 text-base text-slate-200 font-semibold lg:mb-0 lg:mt-0 lg:pr-4"}
      [:img.object-contain.h-12.bg-stone-800 {:src "/img/rapidslabs-logo.webp"}]]
     ;; [:a
     ;;  {:class
     ;;   "mb-4 mr-2 mt-3 flex items-center text-neutral-900 hover:text-neutral-900 focus:text-neutral-900 dark:text-neutral-200 dark:hover:text-neutral-400 dark:focus:text-neutral-400 lg:mb-0 lg:mt-0",
     ;;   :href "/#about"}
     ;;  [:img {:src "/img/rapidslabs-precast-logo.jpg"
     ;;         :style {:height "35px" :width "70px"}
     ;;         :alt "About"
     ;;         :loading "lazy"}]
     ;;  ]
     (comment "Left navigation links")
     [:ul
      {:class "list-style-none mr-auto flex flex-col pl-0 lg:flex-row",
       :data-twe-navbar-nav-ref true
       }
      [:li
        {:class "mb-4 lg:mb-0 lg:pr-4", :data-twe-nav-item-ref true}
       [:a
        {:class
         "text-stone-500 hover:text-stone-700 focus:text-stone-700 disabled:text-black/30 dark:text-stone-200 dark:hover:text-stone-300 dark:focus:text-stone-300 lg:px-2 [&.active]:text-black/90 dark:[&.active]:text-yestone-400"
         :href "/services" }
        "Services"]]
      [:li
       {:class "mb-4 lg:mb-0 lg:pr-4", :data-twe-nav-item-ref true}
       [:a
        {:class
         "text-stone-500 hover:text-stone-700 focus:text-stone-700 disabled:text-black/30 dark:text-stone-200 dark:hover:text-stone-300 dark:focus:text-stone-300 lg:px-2 [&.active]:text-black/90 dark:[&.active]:text-yestone-400"
         :href "/projects",
         :data-twe-nav-link-ref true
         }
        "Projects"]]
      [:li
       {:class "mb-4 lg:mb-0 lg:pr-4", :data-twe-nav-item-ref true}
       [:a
        {:class
         "text-stone-500 hover:text-stone-700 focus:text-stone-700 disabled:text-black/30 dark:text-stone-200 dark:hover:text-stone-300 dark:focus:text-stone-300 lg:px-2 [&.active]:text-black/90 dark:[&.active]:text-yestone-400"
         :href "/products",
         :data-twe-nav-link-ref true
         }
        "Products"]]
      ]]
    ;; Right Elements
    [:div
     {:class "relative flex items-center"}
     (comment "Envelope Icon")
     [:a
      {:class
       "mr-4 text-neutral-500 hover:text-neutral-700 focus:text-neutral-700 disabled:text-black/30 dark:text-neutral-200 dark:hover:text-neutral-300 dark:focus:text-neutral-300 [&.active]:text-black/90 dark:[&.active]:text-neutral-400",
       :href "/contact-us"}
      [:span
       {:class "[&>svg]:w-5"}
       [:svg
        {:xmlns "http://www.w3.org/2000/svg",
         :viewBox "0 0 24 24",
         :fill "currentColor"}
        [:path
         {:d
          "M1.5 8.67v8.58a3 3 0 003 3h15a3 3 0 003-3V8.67l-8.928 5.493a3 3 0 01-3.144 0L1.5 8.67z"}]
        [:path
         {:d
          "M22.5 6.908V6.75a3 3 0 00-3-3h-15a3 3 0 00-3 3v.158l9.714 5.978a1.5 1.5 0 001.572 0L22.5 6.908z"}]]]]
     (comment "User Icon")
     [:a
      {:class
       "mr-4 text-neutral-500 hover:text-neutral-700 focus:text-neutral-700 disabled:text-black/30 dark:text-neutral-200 dark:hover:text-neutral-300 dark:focus:text-neutral-300 [&.active]:text-black/90 dark:[&.active]:text-neutral-400",
       :href "/about"
       :alt "About Us" }
      [:span
       {:class "[&>svg]:w-5"}
       [:svg
        {:xmlns "http://www.w3.org/2000/svg",
         :viewBox "0 0 24 24",
         :fill "currentColor"}
        [:path
         {:fill-rule "evenodd",
          :d
          "M7.5 6a4.5 4.5 0 119 0 4.5 4.5 0 01-9 0zM3.751 20.105a8.25 8.25 0 0116.498 0 .75.75 0 01-.437.695A18.683 18.683 0 0112 22.5c-2.786 0-5.433-.608-7.812-1.7a.75.75 0 01-.437-.695z",
          :clip-rule "evenodd"}]]] ]]]])

 (def footer
   [:<>
    [:footer.text-white
     {:class
      "w-full items-center justify-between bg-neutral-100 pb-2 shadow-md shadow-black/5 dark:bg-stone-800 dark:shadow-black/10 lg:flex-wrap lg:justify-start lg:pb-4"
       :data-twe-sticky-position "bottom"
      }
     [:div
      {:class
       "md:container  min-h-[56px] max-h-[56px]  mx-auto flex w-full flex-wrap items-center justify-between px-3"}
      [:.flex-row
       {:class '["sm:px-6" "lg:px-8"]}
       (contact-section)]
      ]]]
   )


(def footer-tw
  [:footer
 {:class
  "bg-zinc-50 text-center text-surface/75 dark:bg-neutral-700 dark:text-white/75 lg:text-left"}
 (comment "Main")
 [:div
  {:class "mx-6 py-10 text-center md:text-left"}
  [:div
   {:class "grid-1 grid gap-8 md:grid-cols-2 lg:grid-cols-3"}
   (comment "Rapid Slabs section")
   [:div
    {:class ""}
    [:h6
     {:class
      "mb-4 flex items-center justify-center font-semibold uppercase md:justify-start"}
     [:span
      {:class "me-3 [&>svg]:h-4 [&>svg]:w-4"}
      [:svg
       {:xmlns "http://www.w3.org/2000/svg",
        :viewBox "0 0 24 24",
        :fill "currentColor"}
       [:path
        {:d
         "M12.378 1.602a.75.75 0 00-.756 0L3 6.632l9 5.25 9-5.25-8.622-5.03zM21.75 7.93l-9 5.25v9l8.628-5.032a.75.75 0 00.372-.648V7.93zM11.25 22.18v-9l-9-5.25v8.57a.75.75 0 00.372.648l8.628 5.033z"}]]]
     "Rapid Slabs"]
    [:p
     "@Rapid Slabs youve got a partner for the future\n        were commited to building strong fundations\n        competitive, comprehensive pricing! \n Quality and expertise ."]]
   (comment "Products section")
   [:div
    [:h6
     {:class
      "mb-4 flex justify-center font-semibold uppercase md:justify-start"}
     "Products"]
    [:p {:class "mb-4"} [:a {:href "/products"} "Speedfloor"]]
    [:p {:class "mb-4"} [:a {:href "/products"} "Cuploex Ecodome"]]
    [:p {:class "mb-4"} [:a {:href "/products"} "Firth Xpod"]]
    [:p [:a {:href "/products"} "Ribraft"]]]
   (comment "Contact section")
   [:div
    [:h6
     {:class
      "mb-4 flex justify-center font-semibold uppercase md:justify-start"}
     "Contact"]
    [:p
     {:class "mb-4 flex items-center justify-center md:justify-start"}
     address-svg "7A Kapuni Grove, Porirua"]
    [:p
     {:class "mb-4 flex items-center justify-center md:justify-start hover:text-stone-500"}
     email-svg contact-us]
    [:p
     {:class "mb-4 flex items-center justify-center md:justify-start hover:text-stone-500"}
     mobile-svg juppy-tel]
    [:p
     {:class "flex items-center justify-center md:justify-start hover:text-stone-500"}
     mobile-svg nick-tel]]]]
 (comment "Copyright section")
 [:div
  {:class "bg-black/5 p-6 text-center"}
  [:span "© 2024 Copyright:"]
  [:a
   {:class "font-semibold", :href "https://rapidslabs.co.nz/"}
   "Rapid Slabs"]]])


(def footer2
  [:footer.bg-stone-100.shadow-md.sticky.bottom-0
   {:class "md:container dark:shadow-black/10 dark:bg-stone-800"}
   [:.mx-auto.items-center.justify-between.px-3.py-4
    {:class "lg:flex-row sm:px-6 lg:px-8"}
    (contact-section)
    ]])

(defn app-page [ctx & body]
  (base
   ctx
   [:div.mx-auto.bg-stone-100.dark:bg-stone-900
    ;; resize default pages
    ;;{:class "max-w-[540px] sm:max-w-[604px] md:max-w-[720px] lg:max-w-[972px] xl:max-w-full xl:px-12 2xl:max-w-[1400px]"}
    nav2
    [:main.mx-auto.text-stone-800.px-4
     {:class "mt-[60px] md:container dark:text-stone-200 "

      }
     body
     ]
    footer-tw
    ]))

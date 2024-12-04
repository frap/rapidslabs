(ns nz.rapidslabs.app
  (:require [com.biffweb :as biff :refer [q]]
            [nz.rapidslabs.middleware :as mid]
            [clojure.string :as str]
            [nz.rapidslabs.ui :as ui]
            [clojure.string :as str]
            [nz.rapidslabs.settings :as settings]
            [rum.core :as rum]
            [xtdb.api :as xt]
            [ring.adapter.jetty9 :as jetty]
            [cheshire.core :as cheshire]))

(def rib-raft-imgs ["rib-raft-1c" "rib-raft-1d" "rib-raft-1f"])
(def cupolex-imgs ["slabs-cuploex-6" "slabs-cuploex4" "slabs-cuplolex5"])
(def speedfloor-imgs ["speedfloor-1" "speedfloor-image" "speedfloor"])

(defn image-component [img-names]
  [:<>
   [:.w-full.shadow.flex.flex-row.max-w-6xl
    (for [img img-names]
      [:img.mb-4.h-auto.w-auto.max-w-full.rounded-lg {:src (str "/img/" img ".webp")
                                                                       :class "basis-1/3 hover:basis-1/2 hover:shadow-lg hover.shadow-black/50"}
       ])]])

(def juppy-tel [:a {:href "tel:0275600679"} "027 560 0679" ])
(def nick-tel [:a {:href "tel:021453070"} "021 453 070" ])

(defn contact-us []
  [:<>
   [:div.mx-auto.flex.flex-row.px-4
    [:.text-2xl.font-semibold.text-stone-400.mb-4 "Contact Us"]
    [:mb-8
     [:.text-lg.font-semibold.text-stone-400.mb-2
      "Our Address"]
     [:p.text-stone-300
      "where we are"]]
    [:mb-8
     [:h3.text-lg.font-semibold.text-stone-400.mb-2
      "Phone"]
     [:p.text-stone-300
      juppy-tel]]
    [:mb-8
     [:.text-lg.font-semibold.text-stone-400.mb-2
      "Email"]
     [:p.text-stone-300
      "juppy@rapidslabs.co.nz"]]
    [:.relative.h-0.overflow-hidden.mb-6 {:style "padding-bottom: 56.25%;"}
     [:iframe.absolute.top-0.left-0.w-full.h-full
      {:src "https://www.google.com/maps/embed?pb=!1m18!1m12!1m3!1d3353.096656292789!2d-122.08217698485344!3d37.42205787981786!2m3!1f0!2f0!3f0!3m2!1i1024!2i768!4f13.1!3m3!1m2!1s0x808fbc7b4be10725%3A0xf59d178a87b32f52!2sGolden%20Gate%20Bridge!5e0!3m2!1sen!2sus!4v1610035984427!5m2!1sen!2sus"
       :frameborder 0 :style "border:0;" :allowfullscreen "" :aria-hidden "false" :tabindex 0}]]
    ]])

(defn home-page []
  (ui/app-page
   {}
   [:.flex-grow]
   [:.p-3.mx-auto.max-w-screen-sm.w-full
    "Under construction: If you need help, call Juppy " [:a {:href "tel:0275600679"} "027 560 0679" ] " or Nick 021 453"
    ]
   [:.flex-grow]
   [:.flex-grow]
  ))

(defn enquiry-page [_ctx]
  (ui/app-page
   {}
   [:<>
   (biff/form
    {:action "/app/set-foo"}
    [:label.block  "First Name: "
     [:span.font-mono "first name"]]
    [:.h-1]
    [:.flex
     [:input.w-md#first {:type "text" :name "first"}]
     [:.w-3]
     [:button.btn {:type "submit"} "Update"]]
    [:.h-1]
    [:.text-sm.text-stone-400
     "This demonstrates updating a value with a plain old form."])
   [:img.h-96.p-4 {:src "/img/slabs-truck.jpg"
                   :class "hover:object-scale-down"} ]])
  )

(def carousel
  [:<>
   [:.section
   [:.grid.gap-8.lg:grid-cols-3
    [:div
     {:id "carouselExampleCaptions",
      :class "relative",
      :data-twe-carousel-init "",
      :data-twe-carousel-slide ""}
     (comment "Carousel indicators")
     [:div
      {:class
       "absolute bottom-0 left-0 right-0 z-[2] mx-[15%] mb-4 flex list-none justify-center p-0",
       :data-twe-carousel-indicators ""}
      [:button
       {:type "button",
        :data-twe-target "#carouselExampleCaptions",
        :data-twe-slide-to "0",
        :data-twe-carousel-active "",
        :class
        "mx-[3px] box-content h-[3px] w-[30px] flex-initial cursor-pointer border-0 border-y-[10px] border-solid border-transparent bg-white bg-clip-padding p-0 -indent-[999px] opacity-50 transition-opacity duration-[600ms] ease-[cubic-bezier(0.25,0.1,0.25,1.0)] motion-reduce:transition-none",
        :aria-current "true",
        :aria-label "Slide 1"}]
      [:button
       {:type "button",
        :data-twe-target "#carouselExampleCaptions",
        :data-twe-slide-to "1",
        :class
        "mx-[3px] box-content h-[3px] w-[30px] flex-initial cursor-pointer border-0 border-y-[10px] border-solid border-transparent bg-white bg-clip-padding p-0 -indent-[999px] opacity-50 transition-opacity duration-[600ms] ease-[cubic-bezier(0.25,0.1,0.25,1.0)] motion-reduce:transition-none",
        :aria-label "Slide 2"}]
      [:button
       {:type "button",
        :data-twe-target "#carouselExampleCaptions",
        :data-twe-slide-to "2",
        :class
        "mx-[3px] box-content h-[3px] w-[30px] flex-initial cursor-pointer border-0 border-y-[10px] border-solid border-transparent bg-white bg-clip-padding p-0 -indent-[999px] opacity-50 transition-opacity duration-[600ms] ease-[cubic-bezier(0.25,0.1,0.25,1.0)] motion-reduce:transition-none",
        :aria-label "Slide 3"}]]
     (comment "Carousel items")
     [:div
      {:class
       "relative w-full overflow-hidden after:clear-both after:block after:content-['']"}
      (comment "First item")
      [:div
       {:class
        "relative float-left -me-[100%] w-full transition-transform duration-[600ms] ease-in-out motion-reduce:transition-none",
        :data-twe-carousel-active "",
        :data-twe-carousel-item "",
        :style "backface-visibility: hidden"}
       [:img
        {:src "https://tecdn.b-cdn.net/img/Photos/Slides/img%20(15).jpg",
         :class "block w-full",
         :alt "..."}]
       [:div
        {:class
         "absolute inset-x-[15%] bottom-5 hidden py-5 text-center text-white md:block"}
        [:h5 {:class "text-xl"} "First slide label"]
        [:p
         "Some representative placeholder content for the first slide."]]]
      (comment "Second item")
      [:div
       {:class
        "relative float-left -me-[100%] hidden w-full transition-transform duration-[600ms] ease-in-out motion-reduce:transition-none",
        :data-twe-carousel-item "",
        :style "backface-visibility: hidden"}
       [:img
        {:src "https://tecdn.b-cdn.net/img/Photos/Slides/img%20(22).jpg",
         :class "block w-full",
         :alt "..."}]
       [:div
        {:class
         "absolute inset-x-[15%] bottom-5 hidden py-5 text-center text-white md:block"}
        [:h5 {:class "text-xl"} "Second slide label"]
        [:p
         "Some representative placeholder content for the second slide."]]]
      (comment "Third item")
      [:div
       {:class
        "relative float-left -me-[100%] hidden w-full transition-transform duration-[600ms] ease-in-out motion-reduce:transition-none",
        :data-twe-carousel-item "",
        :style "backface-visibility: hidden"}
       [:img
        {:src "https://tecdn.b-cdn.net/img/Photos/Slides/img%20(23).jpg",
         :class "block w-full",
         :alt "..."}]
       [:div
        {:class
         "absolute inset-x-[15%] bottom-5 hidden py-5 text-center text-white md:block"}
        [:h5 {:class "text-xl"} "Third slide label"]
        [:p
         "Some representative placeholder content for the third slide."]]]]
     (comment "Carousel controls - prev item")
     [:button
      {:class
       "absolute bottom-0 left-0 top-0 z-[1] flex w-[15%] items-center justify-center border-0 bg-none p-0 text-center text-white opacity-50 transition-opacity duration-150 ease-[cubic-bezier(0.25,0.1,0.25,1.0)] hover:text-white hover:no-underline hover:opacity-90 hover:outline-none focus:text-white focus:no-underline focus:opacity-90 focus:outline-none motion-reduce:transition-none",
       :type "button",
       :data-twe-target "#carouselExampleCaptions",
       :data-twe-slide "prev"}
      [:span
       {:class "inline-block h-8 w-8"}
       [:svg
        {:xmlns "http://www.w3.org/2000/svg",
         :fill "none",
         :viewBox "0 0 24 24",
         :stroke-width "1.5",
         :stroke "currentColor",
         :class "h-6 w-6"}
        [:path
         {:stroke-linecap "round",
          :stroke-linejoin "round",
          :d "M15.75 19.5L8.25 12l7.5-7.5"}]]]
      [:span
       {:class
        "!absolute !-m-px !h-px !w-px !overflow-hidden !whitespace-nowrap !border-0 !p-0 ![clip:rect(0,0,0,0)]"}
       "Previous"]]
     (comment "Carousel controls - next item")
     [:button
      {:class
       "absolute bottom-0 right-0 top-0 z-[1] flex w-[15%] items-center justify-center border-0 bg-none p-0 text-center text-white opacity-50 transition-opacity duration-150 ease-[cubic-bezier(0.25,0.1,0.25,1.0)] hover:text-white hover:no-underline hover:opacity-90 hover:outline-none focus:text-white focus:no-underline focus:opacity-90 focus:outline-none motion-reduce:transition-none",
       :type "button",
       :data-twe-target "#carouselExampleCaptions",
       :data-twe-slide "next"}
      [:span
       {:class "inline-block h-8 w-8"}
       [:svg
        {:xmlns "http://www.w3.org/2000/svg",
         :fill "none",
         :viewBox "0 0 24 24",
         :stroke-width "1.5",
         :stroke "currentColor",
         :class "h-6 w-6"}
        [:path
         {:stroke-linecap "round",
          :stroke-linejoin "round",
          :d "M8.25 4.5l7.5 7.5-7.5 7.5"}]]]
      [:span
       {:class
        "!absolute !-m-px !h-px !w-px !overflow-hidden !whitespace-nowrap !border-0 !p-0 ![clip:rect(0,0,0,0)]"}
       "Next"]]]]]]
 )

(defn projects-page [_ctx]
  (ui/app-page
   {}
   [:<>
   [:.h-6]
   [:.flex.flex-row.px-4.mx-auto
    [:img.h-24.object-left.rounded-lg.shadow-none.transition-shadow.duration-300.ease-in-out.px-2 {:src "/img/cupolex-concrete-logo.webp"
                                                                                                   :class "hover:shadow-lg hover.shadow-black/50"}]
    [:.text-3xl.font-medium.text-stone-600.leading-tight.dark:text-stone-100
     "Wellington's Regional provider of Cupolex® foundations"]]
   [:.mb-4.text-stone-600.text-base.dark:text-stone-200
    "Rapid Slabs are the only Wellington provider of Cupolex®  Foundations - an up to date technology in concrete flooring."]
   [:.text-2xl.font-medium.text-stone-600.leading-tight.dark:text-stone-100
    "Latest innovations in concrete flooring"]
   [:.mb-4.text-stone-600.leading-loose.dark:text-stone-200
    "Cupolex®  is one of the latest innovations in concrete flooring that not only saves money but is environmentally friendly as well."]
   [:.text-2xl.font-medium.text-stone-600.leading-tight.dark:text-stone-100
    "How does Cupolex® work"]
   [:.mb-4.text-stone-600.leading-loose.dark:text-stone-200
    "Using 100% recycled plastic moulded dome forms as a foundation, concrete is poured over the plastic forms creating a structure for the concrete slab. The plastic foundation reduces the amount of contact the concrete has with the soil and restricts the amount of moisture seeping through the concrete"]
   [:.text-2xl.font-medium.text-stone-600.leading-tight.dark:text-stone-100
    "The advantages of using Cuploex®"]
   [:.mb-4.text-stone-600.leading-loose.dark:text-stone-200
    "The Cupolex®  system has the same structural integrity as other concrete foundations while using less concrete - no need for fill or gravel, and it controls moisture content in the concrete."]
   [:.mb-4.text-stone-600.leading-loose.dark:text-stone-200
    "The structure of the plastic foundation makes it easy to run cables and pipes through the concrete flooring after the floor has been laid."]
   [:.text-2xl.font-medium.text-stone-600.leading-tight.dark:text-stone-100
    "Stop concrete curing problems with Cupolex®"]
   [:div
    (image-component cupolex-imgs)]
   [:img.h-auto {:src "/img/slabs-cupolex-3.webp"
                 :class "hover:shadow-lg hover.shadow-black/50"}]
   ]))

(defn ribraft-page []
  [:<>
   [:.h-6]
   [:.text-2xl.font-medium.text-stone-600.leading-tight.dark:text-stone-100.dark:text-stone-200
    "Ribraft flooring - an innovative flooring solution on offer from Rapid Slabs"]
   [:.mb-4.text-stone-600.leading-loose {:class "dark:text-stone-200"}
    "Ribraft is a concrete flooring system that’s becoming increasingly popular for residential
 and commercial applications throughout New Zealand."]
   [:.text-2xl.font-medium.leading-tight.dark:text-stone-100
    "Concrete flooring system offering lots of advantages"]
   [:.mb-4.text-stone-600.leading-loose {:class "dark:text-stone-200"}
    "Quick to lay, durable, and seismically strong due to its construction method,
Ribraft offers many advantages over standard concrete foundations including the cleaner construction method,
with far less excavated material and waste."]
   ;;
   [:.text-2xl.font-medium.text-stone-600.leading-tight.dark:text-stone-100
    "Unique construction method uses concrete & polystyrene"]
   [:.mb-4.text-stone-600.leading-loose {:class "dark:text-stone-200"}
    "Ribraft flooring is a concrete flooring system created using polystyrene pods,
 plastic spacers, steel reinforcing pods and RaftMix concrete.
 Due to the construction method of the components simply fitting together and no need to dig footings, labour time and costs can be reduced considerably."]
   [:.text-2xl.font-medium.text-stone-600.leading-tight.dark:text-stone-100
    "Where can this flooring solution be used" ]
   [:.mb-4.text-stone-600.leading-loose {:class "dark:text-stone-200"}
    "This concrete and polystyrene flooring system can be used widely, from small buildings to large developments."
    (image-component rib-raft-imgs) ]])

(defn foundation-page [_ctx]
  (ui/app-page
   {}
   [:.mx-auto.pt-20.text-justify.text-white.flex.w-full.flex-wrap.px-3
    {:class
     "lg:container"}
    [:h1.text-xl.font-medium.leading-tight.dark:text-stone-100.mb-4 "Expert Solutions for Every Foundation Need" ]
    [:.mx-4.leading-loose
     "We know our foundations, and we understand that one size doesn’t fit all! At Rapid Slabs & Precast, we specialise in a wide range of foundation solutions tailored to meet the specific needs of every project. From conventional slabs to complex, engineered foundations, we’ll work with you to determine the best approach for your build, whether it’s a small residential job or a large commercial project. With an abundance of industry experience, our team has the expertise to deliver a foundation that’s right every time.
We’re proud to offer various advanced foundation systems that provide flexibility, strength, and efficiency, allowing us to adapt to unique site conditions and project demands. Foundation systems we work with include:"]
    [:ul.mx-4.list-disc.items-left.text-left
     [:li "Speedfloor"]
     [:li "Cupolex Ecodome"]
     [:li "Firth Xpod"]
     [:li "Quickset"]
     [:li "Armadillo System by Cresco"]
     [:li "Ribraft"]]
    [.]
    ]
   ))

(defn precast-page [_ctx]
  (ui/app-page
   {}
   [:.container.mx-auto
    [:text-3xl "Precast"]
    ;; carousel
    ]))


(defn about-page [_ctx]
  (ui/app-page
   {}
   [:.pt-20.text-white.dark:text-stone-100 {:class "md:flex"}
    [:.flex-col
     [:.text-2xl.font-medium.leading-tight
      "Create a solid foundation for your house or building"]
     [:p.mb-4.leading-loose
      "Rapid Slabs offer the right solutions for all your concrete floors and slabs requirements. Known for providing a quick, efficient, and quality job at the same time as being cost effective, Rapid Slabs are the team to contact for all your concrete foundation requirements. Call us today!"]
     [:.text-2xl.font-medium.leading-tight
      "Experienced team leaders with decades of experience"]
     [:.flex.py-2
      [:img.h-24.max-w-md.rounded-lg.shadow-none.transition-shadow.duration-300.ease-in-out.px-2 {:src "/img/daboys.jpg"
                                                                                                  :class "hover:shadow-lg hover.shadow-black/50"}]
      [:p.mb-4.text-base.text-stone-600.mb-4.leading-loose.dark:text-stone-200
       "Certified builders, Adam Jupp & Nick Jones, lead the Rapid Slabs team.
       With a combined over 50 years building experience, Adam & Nick  will
       ensure you receive a reliable and professional service and a quality
       concrete foundation suited to your needs."
       ]]
     [:.text-2xl.font-medium.leading-tight
      "Dedicated & skilled Rapid Slabs team"]
     [:.mb-4.leading-loose
      "You can expect a quality job from our dedicated team of 25 - all experts in the delivery and laying of concrete slabs, flooring & foundations."]
     [:.text-2xl.font-medium.text-stone-600.leading-tight
      "Versatile products to suit your job"]
     [:.mb-4.leading-loose
      "Choose from a range of high quality concrete & composite products to suit your job. Cupolex, Speed floor or Rib Raft flooring and foundations offer you the versatility so you can choose the best product for your needs."]
     [:.text-2xl.font-medium.leading-tight.dark:text-stone-100
      "Safety first"]
     [:.mb-4.leading-loose
      "Health and safety are of paramount importance at Rapid Slabs. As members of Sitesafe (who specialise in creating safe work environments), staff regularly attend safety courses and we operate site specific safety plans."]
     [:.text-2xl.font-medium.text-stone-600.leading-tight.dark:text-stone-100
      "Servicing the greater Wellington region"]
     [:.mb-4.leading-loose
      "Based in the Porirua region, our concrete slab laying services extend throughout the region: Wellington, Hutt Valley, Kapiti Coast and Levin."]
     [:h2.text-2xl.font-medium.leading-tight
      "Operating Hours"]
     [:.mb-4.leading-loose
      "On the job Monday to Saturday, 7am to 6pm,  we are available on Sunday by arrangement."]
     ;;(contact-us)
     ]]))



(def tab-names ["Home" "Cupolex®" "Speedfloor" "Ribraft Foundation" "Enquiry" "About Rapid Slabs"])

;; (defn tab-component [tab-names]
;;   (let [page-functions {"Home" home-page
;;                         "Cupolex®" cupolex-page
;;                         "Speedfloor" speedfloor-page
;;                         "Ribraft Foundation" ribraft-page
;;                         "Enquiry" enquiry-page
;;                         "About Rapid Slabs" about-page}]
;;   [:<>
;;    [:.w-full.test-sm:font-medium.text-center.rounded-lg.dark:bg-stone-400.dark:border-stone-700.flex
;;     [:img.object-cover.bg-stone-200 {:src "/img/rapidslabs-logo.webp"}]
;;     (for [id tab-names]
;;       [:button.inline-block.w-full.p-4.bg-stone-200.border-s-0.border-stone-300
;;        {:role  "tab"
;;         :name  "tab"
;;         :value id
;;         :class
;;         "focus-within:z-10 hover:text-stone-700 hover:bg-cyan-200 focus:ring-4 focus:ring-cyan-300 rounded-lg dark:hover:text-white dark:bg-stone-400 dark:hover:bg-stone-600 dark:border-stone-700"
;;         ;; Hyperscript goes in the :_ attribute.
;;         :_     "on click show .section in #tab-sections when its id is my value"} id])]
;;    [:.h-6]
;;    [:#tab-sections
;;     (for [id tab-names]
;;       (let [page-fn (get page-functions id)]
;;         [:.section
;;          {:id    id
;;           :style (when-not (= id "Home")
;;                  {:display "none"})}
;;       (when page-fn
;;              (page-fn))
;;          ]))]]))
>>>>>>> 3731d2a (multiple changes to app and ui)

(def ripple-button
  [:button
   {:type "button",
    :data-twe-ripple-init "",
    :class
    "inline-block rounded bg-primary px-6 pb-2 pt-2.5 text-xs font-medium uppercase leading-normal text-white shadow-primary-3 transition duration-150 ease-in-out hover:bg-primary-accent-300 hover:shadow-primary-2 focus:bg-primary-accent-300 focus:shadow-primary-2 focus:outline-none focus:ring-0 active:bg-primary-600 active:shadow-primary-2 motion-reduce:transition-none dark:shadow-black/30 dark:hover:shadow-dark-strong dark:focus:shadow-dark-strong dark:active:shadow-dark-strong"}
   "Ripple"])

(defn app [{:keys [session biff/db] :as ctx}]
  (let [{:user/keys [email firstname lastname]} (xt/entity db (:uid session))]
    (ui/app-page
     {}
     [:.h-full {:class "bg-[hsla(0,0%,98%,0.6)]"}
      [:.text-white.h-full.items-center.justify-center {:class "md:flex"}
       [:.flex-grow {:class "md:basis-1/3"}] ;; will grow and occupy available space in flex container
       [:.flex-col.flex-grow.text-center {:class "md:basis-2/3"}
        [:h1.mb-5.text-5xl.font-semibold "Something Exciting is coming!"]
        [:p.mb-7.text-2xl.font-medium "While we work on bringing you our new website, you can still get in touch with us at contactus@rapidslabs.co.nz
or call: Nick 021 453 070 or Adam 027 560 0679"]
        ripple-button
        ]

       ;;   [:.flex-grow {:class "md:basis-1/4"}]
       ]]

     ;; (tab-component tab-names)


     )))



(defn echo [{:keys [params]}]
  {:status 200
   :headers {"content-type" "application/json"}
   :body params})

(def module
  { ;; :static {"/about/" about-page }
   ;;  "/home"  home-page
   ;;  "/contact/" contact-page
   ;;       }
   :routes [
            ["/" {:get app}]
            ["/home" {:get home-page}]
            ["/about" {:get about-page}]
            ["/contact-us" {:get enquiry-page}]
            ["/foundations" {:get foundation-page}]
            ["/projects"   {:get projects-page}]
            {"/precast"    {:get precast-page}}
            ]})

(comment
  (macroexpand (rum/render-html contact-us))
  ;; => "nz.rapidslabs.app$contact_us@21054678"
  ;; => Execution error (ClassCastException) at rum.server-render/render-style! (server_render.clj:289).
  ;;    class java.lang.Character cannot be cast to class java.util.Map$Entry (java.lang.Character and java.util.Map$Entry are in module java.base of loader 'bootstrap')
  ;; => "<div class=\"w-full test-sm:font-medium text-center rounded-lg shadow sm:fle\"><div src=\"/img/rib-raft-1c.webp\" class=\"mb-4 h-auto max-w-full rounded-full\"></div><div src=\"/img/rib-raft-1d.webp\" class=\"mb-4 h-auto max-w-full rounded-full\"></div><div src=\"/img/rib-raft-1f.webp\" class=\"mb-4 h-auto max-w-full rounded-full\"></div></div>"

  ,)

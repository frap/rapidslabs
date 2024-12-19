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

(defn contactus []
  [:<>
   [:section
   {:id "contact", :class "bg-gray-100 p-8 rounded shadow-md"}
   [:h2
    {:class "text-3xl font-extrabold text-gray-900 mb-6"}
    "Contact Us"]
   [:p
    {:class "text-lg text-gray-600 mb-4"}
    "Top Quality, Reliable Pricing, No Corners Cut!"]
   [:p
    {:class "text-gray-600 mb-4"}
    "Email:"
    [:a
     {:href "mailto:contactus@rapidslabs.co.nz",
      :class "text-blue-600 hover:underline"}
     "contactus@rapidslabs.co.nz"]]
   [:a
    {:href "#",
     :class
     "mt-4 inline-block bg-blue-600 text-white px-6 py-3 rounded shadow hover:bg-blue-700"}
    "Get a Free Quote"]]]
  )

(defn enquiry-page [_ctx]
  (ui/app-page
   {}
   [:section#enquiry.mb-16
    [:h2.text-3xl.font-extrabold.mb-6.text-stone-900.dark:text-stone-100 "Enquiry Form"]
    [:div.bg-stone-100.p-6.shadow-md.rounded-lg.dark:bg-stone-800
     (biff/form
      {:class "dark:shadow-black/10"
       :action "/app/send-email"}
      ;; Name Label and Input
      [:div.mb-4
       [:label.block.text-stone-700.dark:text-stone-200 {:for "name"} "Your Name"]
       [:input.w-full.px-4.py-2.bg-stone-50.text-stone-900.rounded-md.shadow-inner.focus:outline-none.focus:ring-2.focus:ring-cyan-600.dark:bg-stone-700.dark:text-stone-100
        {:type "text" :name "first" :id "name" :placeholder "Enter your name"}]]

      [:div.mb-4
       [:label.block.text-stone-700.dark:text-stone-200 {:for "enquiry"} "Your Enquiry"]
       [:input.w-full.px-4.py-2.bg-stone-50.text-stone-900.rounded-md.shadow-inner.focus:outline-none.focus:ring-2.focus:ring-cyan-600.dark:bg-stone-700.dark:text-stone-100
        {:type "text" :name "enquiry" :id "enquiry" :placeholder "Enter your Enquiry"}]]
      ;; Submit Button
      [:div.flex.justify-between.items-center
       [:button.btn.px-4.py-2.bg-cyan-600.text-white.font-bold.rounded-lg.hover:bg-cyan-700.focus:outline-none.focus:ring-2.focus:ring-cyan-500.dark:bg-cyan-700.dark:hover:bg-cyan-800
        {:type "submit"} "Submit"]]

      ;; Additional Instructions
      [:p.text-sm.text-stone-600.mt-4.dark:text-stone-300
       "Please enter your name and any details in the comments section."])]]))


(defn carousel []
  [:div {:id "carouselExampleIndicators"
         :class "relative"
         :data-twe-carousel-init true
         :data-twe-ride "carousel"}

   ;; Carousel indicators
   [:div {:class "absolute bottom-0 left-0 right-0 z-[2] mx-[15%] mb-4 flex list-none justify-center p-0"
          :data-twe-carousel-indicators true}
    [:button {:type "button"
              :data-twe-target "#carouselExampleIndicators"
              :data-twe-slide-to "0"
              :data-twe-carousel-active true
              :class "mx-[3px] box-content h-[3px] w-[30px] flex-initial cursor-pointer border-0 border-y-[10px] border-solid border-transparent bg-white bg-clip-padding p-0 -indent-[999px] opacity-50 transition-opacity duration-[600ms] ease-[cubic-bezier(0.25,0.1,0.25,1.0)] motion-reduce:transition-none"
              :aria-current "true"
              :aria-label "Slide 1"}]
    [:button {:type "button"
              :data-twe-target "#carouselExampleIndicators"
              :data-twe-slide-to "1"
              :class "mx-[3px] box-content h-[3px] w-[30px] flex-initial cursor-pointer border-0 border-y-[10px] border-solid border-transparent bg-white bg-clip-padding p-0 -indent-[999px] opacity-50 transition-opacity duration-[600ms] ease-[cubic-bezier(0.25,0.1,0.25,1.0)] motion-reduce:transition-none"
              :aria-label "Slide 2"}]
    [:button {:type "button"
              :data-twe-target "#carouselExampleIndicators"
              :data-twe-slide-to "2"
              :class "mx-[3px] box-content h-[3px] w-[30px] flex-initial cursor-pointer border-0 border-y-[10px] border-solid border-transparent bg-white bg-clip-padding p-0 -indent-[999px] opacity-50 transition-opacity duration-[600ms] ease-[cubic-bezier(0.25,0.1,0.25,1.0)] motion-reduce:transition-none"
              :aria-label "Slide 3"}]]

   ;; Carousel items
   [:div {:class "relative w-full overflow-hidden after:clear-both after:block after:content-['']"}
    ;; First item
    [:div {:class "relative float-left -mr-[100%] w-full transition-transform duration-[600ms] ease-in-out motion-reduce:transition-none"
           :data-twe-carousel-item true
           :data-twe-carousel-active true}
     [:img {:src "/img/rapidslabs_juppy.jpg"
            :class "block w-full"
            :alt "Juppy working"}]]

    ;; Second item
    [:div {:class "relative float-left -mr-[100%] hidden w-full transition-transform duration-[600ms] ease-in-out motion-reduce:transition-none"
           :data-twe-carousel-item true}
     [:img {:src "/img/rapid-slabs-gallery-01a.webp"
            :class "block w-full"
            :alt "Camera"}]]

    ;; Third item
    [:div {:class "relative float-left -mr-[100%] hidden w-full transition-transform duration-[600ms] ease-in-out motion-reduce:transition-none"
           :data-twe-carousel-item true}
     [:img {:src "/img/rapid-slabs-skirting.jpg"
            :class "block w-full"
            :alt "Exotic Fruits"}]]]

   ;; Carousel controls - prev item
   [:button {:class "absolute bottom-0 left-0 top-0 z-[1] flex w-[15%] items-center justify-center border-0 bg-none p-0 text-center text-white opacity-50 transition-opacity duration-150 ease-[cubic-bezier(0.25,0.1,0.25,1.0)] hover:text-white hover:no-underline hover:opacity-90 hover:outline-none focus:text-white focus:no-underline focus:opacity-90 focus:outline-none motion-reduce:transition-none"
             :type "button"
             :data-twe-target "#carouselExampleIndicators"
             :data-twe-slide "prev"}
    [:span {:class "inline-block h-8 w-8"}
     [:svg {:xmlns "http://www.w3.org/2000/svg"
            :fill "none"
            :viewBox "0 0 24 24"
            :stroke-width "1.5"
            :stroke "currentColor"
            :class "h-6 w-6"}
      [:path {:stroke-linecap "round"
              :stroke-linejoin "round"
              :d "M15.75 19.5L8.25 12l7.5-7.5"}]]]
    [:span {:class "!absolute !-m-px !h-px !w-px !overflow-hidden !whitespace-nowrap !border-0 !p-0 ![clip:rect(0,0,0,0)]"} "Previous"]]

   ;; Carousel controls - next item
   [:button {:class "absolute bottom-0 right-0 top-0 z-[1] flex w-[15%] items-center justify-center border-0 bg-none p-0 text-center text-white opacity-50 transition-opacity duration-150 ease-[cubic-bezier(0.25,0.1,0.25,1.0)] hover:text-white hover:no-underline hover:opacity-90 hover:outline-none focus:text-white focus:no-underline focus:opacity-90 focus:outline-none motion-reduce:transition-none"
             :type "button"
             :data-twe-target "#carouselExampleIndicators"
             :data-twe-slide "next"}
    [:span {:class "inline-block h-8 w-8"}
     [:svg {:xmlns "http://www.w3.org/2000/svg"
            :fill "none"
            :viewBox "0 0 24 24"
            :stroke-width "1.5"
            :stroke "currentColor"
            :class "h-6 w-6"}
      [:path {:stroke-linecap "round"
              :stroke-linejoin "round"
              :d "M8.25 4.5l7.5 7.5-7.5 7.5"}]]]
    [:span {:class "!absolute !-m-px !h-px !w-px !overflow-hidden !whitespace-nowrap !border-0 !p-0 ![clip:rect(0,0,0,0)]"} "Next"]]]
  )

;; [:<>
;;     [:.h-6]
;;     [:.flex.flex-row.px-4.mx-auto
;;      [:img.h-24.object-left.rounded-lg.shadow-none.transition-shadow.duration-300.ease-in-out.px-2 {:src "/img/cupolex-concrete-logo.webp"
;;                                                                                                     :class "hover:shadow-lg hover.shadow-black/50"}]
;;      [:.text-3xl.font-medium.text-stone-600.leading-tight.dark:text-stone-100
;;       "Wellington's Regional provider of Cupolex® foundations"]]
;;     [:.mb-4.text-stone-600.text-base.dark:text-stone-200
;;      "Rapid Slabs are the only Wellington provider of Cupolex®  Foundations - an up to date technology in concrete flooring."]
;;     [:.text-2xl.font-medium.text-stone-600.leading-tight.dark:text-stone-100
;;      "Latest innovations in concrete flooring"]
;;     [:.mb-4.text-stone-600.leading-loose.dark:text-stone-200
;;      "Cupolex®  is one of the latest innovations in concrete flooring that not only saves money but is environmentally friendly as well."]
;;     [:.text-2xl.font-medium.text-stone-600.leading-tight.dark:text-stone-100
;;      "How does Cupolex® work"]
;;     [:.mb-4.text-stone-600.leading-loose.dark:text-stone-200
;;      "Using 100% recycled plastic moulded dome forms as a foundation, concrete is poured over the plastic forms creating a structure for the concrete slab. The plastic foundation reduces the amount of contact the concrete has with the soil and restricts the amount of moisture seeping through the concrete"]
;;     [:.text-2xl.font-medium.text-stone-600.leading-tight.dark:text-stone-100
;;      "The advantages of using Cuploex®"]
;;     [:.mb-4.text-stone-600.leading-loose.dark:text-stone-200
;;      "The Cupolex®  system has the same structural integrity as other concrete foundations while using less concrete - no need for fill or gravel, and it controls moisture content in the concrete."]
;;     [:.mb-4.text-stone-600.leading-loose.dark:text-stone-200
;;      "The structure of the plastic foundation makes it easy to run cables and pipes through the concrete flooring after the floor has been laid."]
;;     [:.text-2xl.font-medium.text-stone-600.leading-tight.dark:text-stone-100
;;      "Stop concrete curing problems with Cupolex®"]
;;     [:div
;;      (image-component cupolex-imgs)]
;;     [:img.h-auto {:src "/img/slabs-cupolex-3.webp"
;;                   :class "hover:shadow-lg hover.shadow-black/50"}]
;;     ]


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


(defn precast-modal []
  [:div#precast-modal.hidden.fixed.inset-0.z-50.flex.items-center.justify-center.bg-black.bg-opacity-50
   [:div.bg-white.rounded-lg.shadow-lg.p-6.max-w-3xl.w-full
    ;; Modal Header
    [:div.flex.items-center.justify-between.mb-4
     [:h3.text-xl.font-bold.text-stone-900 "Precast Services" ]
     [:button#close-modal.text-stone-400.hover:text-stone-600.focus:outline-none "✕"]]
    ;; Modal Content
    [:section.py-12.px-4.lg:px-16.bg-gray-50.text-gray-800
     [:div.max-w-7xl.mx-auto
      ;; Section Title
      [:h2.text-3xl.font-bold.mb-6.text-center "PRECAST"]
      [:h3.text-xl.font-semibold.mb-4.text-center "Efficient, Durable, and Tailored Precast for Every Project"]
      [:p.text-lg.leading-relaxed.mb-8.text-center "Quality Precast Solutions for Residential and Commercial Builds"]

      ;; Introductory Paragraph
      [:p.text-lg.leading-relaxed.mb-6
       "At Rapid Slabs & Precast, we are the only ones in Wellington providing high-quality, custom precast concrete solutions. Whether you’re constructing walls, beams, or intricate architectural features, our team can design, manufacture, and install precast panels that are specific to your design requirements. With our skilled team and onsite production processes, we’ll deliver results that not only meet specifications but also elevate the structural and aesthetic value of your project."]
      [:p.text-lg.leading-relaxed.mb-6
       "We offer a wide range of precast concrete options that allow for versatility, durability, less onsite disruptions, and faster installation times. Our services include both standard and custom panel designs, giving you the flexibility to incorporate precast concrete in any construction project—from residential homes to commercial buildings and complex infrastructure projects."]

      ;; Types of Precast Systems
      [:div.mb-10
       [:h3.text-2xl.font-semibold.mb-4 "Types of precast systems we work with include:"]
       [:ul.list-disc.pl-6.space-y-2
        [:li "Tilt-up panels"]
        [:li "Structural and architectural panels"]
        [:li "Load-bearing walls"]
        [:li "Sound barriers and retaining walls"]
        [:li "Fire walls"]
        [:li "Cladding panels"]]]

      ;; Why Build with Precast
      [:div.mb-10
       [:h3.text-2xl.font-semibold.mb-4 "Why build with concrete precast panels?"]
       [:p.text-lg.leading-relaxed.mb-6
        "Building with concrete precast panels offers numerous advantages, making it one of the most reliable and efficient materials in modern construction. Precast concrete is known for its exceptional durability, strength, and resistance to elements, allowing structures to withstand weather extremes, fire, and heavy loads with minimal maintenance. This resilience makes it an ideal choice for load-bearing walls, structural components, and protective barriers, providing not only safety but longevity to buildings."]
       [:p.text-lg.leading-relaxed.mb-6
        "One of the key benefits of precast panels is their versatility in design and application. Because panels are crafted offsite under controlled conditions, we achieve precise specifications, resulting in quality and consistent results. This approach not only allows for faster and more efficient onsite installation but also minimizes disruptions, helping projects stay on schedule. Precast concrete is also highly customizable, allowing you to create architectural features that add unique visual appeal without sacrificing strength."]
       [:p.text-lg.leading-relaxed
        "With excellent insulation properties, precast panels contribute to better thermal efficiency, making buildings more comfortable and energy-efficient. Additionally, concrete’s natural fire resistance offers added security and peace of mind. Our range of options—whether it’s tilt-up panels, sound barriers, or custom cladding—ensures we have a solution to meet the needs of any project, from residential homes to large-scale commercial buildings. At Rapid Slabs & Precast, our expertise in precast concrete can bring both technical excellence and aesthetic value to your build, making your vision a reality."]]

      ;; Closing Paragraph
      [:p.text-lg.leading-relaxed.text-center
       "From our first conversation through to the final installation, our team works closely with you to ensure that each precast element is designed and delivered to meet your timeline, budget, and expectations."]]]
    ;; Modal Footer
    [:div.flex.justify-end.mt-6
     [:button#close-modal
      {:class "px-4 py-2 bg-gray-200 text-gray-700 rounded hover:bg-gray-300 focus:outline-none"}
      "Close"]]]])

(defn foundations-modal []
  [:div#foundations-modal.hidden.fixed.inset-0.z-50.flex.items-center.justify-center.bg-black.bg-opacity-50
   [:div.bg-white.rounded-lg.shadow-lg.p-6.max-w-3xl.w-full
    [:div.flex.items-center.justify-between.mb-4
     [:h3.text-xl.font-bold.text-stone-900 "Foundation Services"]
     [:button#close-modal.text-stone-400.hover:text-stone-600.focus:outline-none "✕"]]
    [:div "(Content for foundations modal goes here)"]
    [:div.flex.justify-end.mt-6
     [:button#close-modal.px-4.py-2.bg-gray-200.text-gray-700.rounded
      {:class "hover:bg-gray-300 focus:outline-none"}
      "Close"]]]]
  )

(def images
  [[ "/img/slabs-crane.jpg" "Slabs Crane"]
   ["/img/rib-raft-1d.webp" "Ribraft"]
   ["/img/slabs-cupolex4.webp" "Cupolex"]])


(defn products-page [_ctx]
  (ui/app-page
   {}
   [:#products.mb-16
    [:h2.text-3xl.font-extrabold.text-stone-900.mb-6
     {:class "dark:text-stone-100"} "Expert Solutions for Every Foundation Need"]
    [:p.text-lg.text-stone-700.leading-relaxed.mb-6
     "We know our foundations, and we understand that one size doesn’t fit all! At Rapid Slabs & Precast, we specialise in a wide range of foundation solutions tailored to meet the specific needs of every project. From conventional slabs to complex, engineered foundations, we’ll work with you to determine the best approach for your build, whether it’s a small residential job or a large commercial project."]
     [:ul.list-disc.list-inside.text-stone-700.space-y-2
     [:li "Speedfloor"]
     [:li "Cupolex Ecodome"]
     [:li "Firth Xpod"]
     [:li "Quickset"]
     [:li "Armadillo System by Cresco"]
     [:li "Ribraft"]]

    [:div (carousel)]
    [:p.text-lg.text-stone-700.leading-relaxed.mt-6
     "From the initial consultation to project completion, we’re dedicated to working closely with you to ensure your foundation meets the highest standards of durability, precision, and reliability. Rapid Slabs & Precast is here to help lay the groundwork for your project’s success."]

    ]))

(defn projects-page [_ctx]
  (ui/app-page
   {}
   [:section#projects.mb-16
    [:h2.text-3xl.font-extrabold.text-stone-900.mb-6
     {:class "dark:text-stone-100"} "Rapid Slabs Projects"]
    [:p.text-lg.text-stone-700.leading-relaxed.mb-6
     "Reputation in our industry is key to our success. Here are some of the projects we’re proud to have been a part of:"]
    [:ul.list-disc.list-inside.text-stone-700.space-y-2
     [:li "Foundations – simple and complex examples"]
     [:li "Precast – the Works, Tesla"]
     [:li "Other projects – Morgan’s farm"]]
    ;;
    [:div (carousel)]
    ]))

(defn services-page [_ctx]
  (ui/app-page
   {}
   [:section#services.mb-16.bg-no-repeat.bg-cover.h-screen
    [:h2.text-3xl.font-extrabold.text-stone-900.mb-6
     {:class "dark:text-stone-100"} "Rapid Services"]
    [:div.grid.grid-cols-1.md:grid-cols-2.gap-8
     ;; Foundations Card
     [:div.bg-stone-100.p-6.shadow-md.rounded.dark:bg-stone-800
      {:class "dark:shadow-black/10"}
      [:h3.text-xl.font-bold.mb-4.text-stone-900.dark:text-stone-100 "Foundations"]
      [:p.text-stone-700.leading-relaxed.dark:text-stone-200
       "From conventional slabs to complex engineered foundations, we offer tailored solutions to meet your project's needs."]]
     ;; Precast Card
     [:div.bg-stone-100.p-6.shadow-md.rounded.dark:bg-stone-800
      {:class "dark:shadow-black/10"}
      [:h3.text-xl.font-bold.mb-4.text-stone-900.dark:text-stone-100 "Precast"]
      [:p.text-stone-700.leading-relaxed.mb-4.dark:text-stone-200
       "We provide high-quality, custom precast concrete solutions, including walls, beams, and intricate architectural features."]
      ;; Button to Trigger Modal
      [:button#open-modal
       {:class "px-4 py-2 bg-zinc-600 text-white rounded hover:bg-zinc-700 focus:outline-none dark:bg-cyan-700 dark:hover:bg-cyan-800"}
       "Learn More"]]]
     (precast-modal)
    ]
))

(defn about-page [_ctx]
  (ui/app-page
   {}
   [:section#about.mb-16.bg-no-repeat.bg-contain
    [:h2.text-3xl.font-extrabold.text-stone-900.mb-6
     {:class "dark:text-stone-100"}
     "Building Strong Foundations, Together"]
    ;; Introduction Paragraph
    [:p.text-lg.leading-relaxed.text-center.text-stone-700.mb-8
     "At Rapid Slabs & Precast, we see concrete as more than just a building material—it’s a key element in modern construction, and the future of construction. Serving the greater Wellington region and the lower North Island, we specialise in providing high-quality concrete foundations and custom precast panels for both residential and commercial projects."]
    ;; Our Story
    [:div.mb-10
     [:h3.text-2xl.font-semibold.text-stone-800.mb-4 "Our Story"]
     [:p.text-lg.leading-relaxed.text-stone-700.mb-6
      "Rapid Slabs & Precast is more than just a concrete company. Under the new ownership of Nick Jones and Adam Jupp, with over 60 years of combined industry experience, we saw the need for a construction service that goes beyond the basics. Our mission is to push what’s possible in the concrete industry. From simple residential builds to complex commercial developments, our focus is on delivering tailored solutions that are built to last."]
     [:p.text-lg.leading-relaxed.text-stone-700
      "We take pride in doing more than what’s expected, combining technical expertise with hands-on experience to create solutions that meet both the structural and aesthetic needs of each project. Whether it’s an intricate foundation or a custom precast panel, we thrive on the challenges that push us to innovate and elevate the standard of concrete construction."]]
    ;; Our Approach
    [:div.mb-10
     [:h3.text-2xl.font-semibold.text-stone-800.mb-4 "Our Approach"]
     [:p.text-lg.leading-relaxed.text-stone-700.mb-6
      "We know that not all slabs are the same, which is why we specialize in a wide variety of foundation systems, including RibRaft, Cupolex, Expod, QuickSet, Firth Xpod and engineered foundations. We offer solutions designed to suit the unique requirements of each project. But our services don’t stop at foundations—our expertise in precast panels allows us to provide end-to-end solutions for all aspects of concrete construction."]
     [:p.text-lg.leading-relaxed.text-stone-700
      "At the heart of our success is our commitment to strong relationships. We collaborate closely with our clients, suppliers, and contractors to ensure the smooth delivery of each project. By maintaining open communication and shared goals, we deliver on our promises, building trust and long-term partnerships along the way. Our clients know they can rely on us for high-quality work, competitive pricing, and a no-nonsense approach to getting the job done."]]
    ;; Why Work With Us?
    [:div.mb-10
     [:h3.text-2xl.font-semibold.text-stone-800.mb-4 "Why Work With Us?"]
     [:ul.list-disc.pl-6.space-y-2.text-stone-700
      [:li "Specialisation in the Complex: We thrive on challenges, from intricate concrete foundations to custom-designed precast panels. No job is too big or small for us, and we take pride in delivering results that meet the unique needs of each project."]
      [:li "Innovation and Expertise: We combine decades of hands-on experience with the latest design and construction techniques to create cutting-edge solutions. Our forward-thinking approach ensures that your project benefits from the most advanced methods available."]
      [:li "End-to-End Service: From the first consultation to the final installation, we offer complete project management. Our team is involved in every stage of the process, ensuring quality control and timely delivery every step of the way."]
      [:li "Building on Trust: Our reputation is built on trust, reliability, and collaboration. We’re not just focused on getting the job done—we care about the success of your project as much as you do. That’s why so many developers, builders, and property owners continue to work with us."]]]
    ;; Looking Ahead
    [:div.mb-10
     [:h3.text-2xl.font-semibold.text-stone-800.mb-4 "Looking Ahead"]
     [:p.text-lg.leading-relaxed.text-stone-700
      "The construction industry is constantly evolving, and so are we. As new technologies, materials, and methods emerge, we stay ahead of the curve to offer innovative solutions that push the boundaries of what’s possible with concrete. Our team is always looking for ways to improve and adapt, ensuring that we remain at the forefront of the industry and ready to meet the future head-on."]]
    ;; Conclusion
    [:div.text-center
     [:p.text-lg.leading-relaxed.text-stone-700.mb-6
      "At Rapid Slabs & Precast, we’re committed to building strong foundations—both in the physical structures we create and the relationships we form along the way. We’re ready to take on any challenge, and we’re excited to work with you on your next project."]
     [:a.bg-stone-800.text-white.px-6.py-3.rounded-lg.shadow-md.hover:bg-stone-900.transition
      {:href "#contact"} "Let’s Build Together"]]]))


(defn app [_ctx]
  (ui/app-page
   {}
   [:section#homepage.mb-16.bg-contain.bg-no-repeat.h-screen
    {:class "mt-[60px]"
     ;; :style {:background-image "url('/img/rapidslabs_juppy.jpg')"}
     }
    [:h2.text-3xl.font-extrabold.text-stone-800.mb-6
     "Rapid Slabs & Precast"]
   ;;   [:div (carousel)]
    [:p.text-xl.text-stone-700.leading-relaxed.mb-4
     "Foundations and Panels That Build the Future"]
    [:p.text-lg.text-stone-700
     "Building Strong Foundations and Custom Panels for Every Project."]
    [:a.mt-4.inline-block.bg-stone-800.text-white.px-6.py-3.rounded.shadow.hover:bg-stone-900
     {:href "#contact"}
     "Get a Free Quote"]
    [:p.text-xl.text-white.px-10.mb-10
      ;; {:class "mt-[400px]" }
     "Site under construction! Call Nick or Juppy on links below if you need help"]]))

   ;;  [  :main.bg-cover.bg-no-repeat.h-screen
   ;;     { :style {:margin-top "-56px"
   ;;             :margin-bottom "-56px"
   ;;              :background-image "url('/img/rapidslabs_juppy.jpg')"}}]

(def module
  { ;; :static {"/about/" about-page }
   :routes [
            ["/" {:get app}]
            ["/about" {:get about-page}]
            ["/contact-us" {:get enquiry-page}]
            ["/services" {:get services-page}]
            ["/projects"   {:get projects-page}]
            {"/products"    {:get products-page}}
            ]})

(comment
  (macroexpand (rum/render-html contact-us))
  ;; => "nz.rapidslabs.app$contact_us@21054678"
  ;; => Execution error (ClassCastException) at rum.server-render/render-style! (server_render.clj:289).
  ;;    class java.lang.Character cannot be cast to class java.util.Map$Entry (java.lang.Character and java.util.Map$Entry are in module java.base of loader 'bootstrap')
  ;; => "<div class=\"w-full test-sm:font-medium text-center rounded-lg shadow sm:fle\"><div src=\"/img/rib-raft-1c.webp\" class=\"mb-4 h-auto max-w-full rounded-full\"></div><div src=\"/img/rib-raft-1d.webp\" class=\"mb-4 h-auto max-w-full rounded-full\"></div><div src=\"/img/rib-raft-1f.webp\" class=\"mb-4 h-auto max-w-full rounded-full\"></div></div>"

  ,)

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
(defn image-component [img-names]
  [:<>
   (for [img img-names]
     [:.mb-4.h-auto.max-w-full.rounded-full {:src (str "/img/" img ".webp")}
      ])])

(defn home-page []
   [:<>
    [:.h-6]
    [:img.flex-col.rounded-lg.shadow-none.transition-shadow.duration-300.ease-in-out {:src "/img/rapidslabs-precast.jpg"
                                 :class "hover:object-scale-down hover:shadow-lg hover.shadow-black/50"}]
    [:p.mb-4.text-stone-600.dark:text-gray-400.text-base
     "Rapid Slabs are the experienced professionals for all concrete flooring, concrete slabs & concrete foundations for the greater Wellington region including the Hutt Valley, Porirua, Kapiti Coast and Levin.
     We offer Cupolex, Speedfloor and Ribraft systems. Talk to us today about your requirements."]]
  )

(defn contacts-page []
   [:<>
    [:.h-6]
  [:p.text-orange-500.dark:text-orange-400.text-md.p-4
   "Under construction obviously
 you need to call Juppy long " [:a {:href "tel:0275600679"} "027 560 0679" ]]
    [:img.object-fill.p-10 {:src "/img/slabs-truck.jpg"
                       :class "hover:object-scale-down"} ]]
  )

(defn enquiry-page []
  [:<>
  [:p.text-orange-500.mb-4.leading-loose "This is the Enquiry section however I have just copied the cupolex stuff" ]
   [:text-3xl.font-medium.leading-tight "Wellington region provider of Cupolex® foundations"]

   [:.mb-4.text-stone-600 "Rapid Slabs are the only Wellington provider of Cupolex®  Foundations - an up to date technology in concrete flooring."
    [:.text-3xl.font-medium.text-stone-600.leading-tight "Latest innovations in concrete flooring"]
    [:.mb-4.text-stone-600.leading-loose "Cupolex®  is one of the latest innovations in concrete flooring that not only saves money but is environmentally friendly as well.
"]
    [:.text-3xl.font-medium.leading-tight "How does Cupolex® work" ]

"


Using 100% recycled plastic moulded dome forms as a foundation, concrete is poured over the plastic forms creating a structure for the concrete slab. The plastic foundation reduces the amount of contact the concrete has with the soil and restricts the amount of moisture seeping through the concrete
The advantages of using Cuploex®

The Cupolex®  system has the same structural integrity as other concrete foundations while using less concrete - no need for fill or gravel, and it controls moisture content in the concrete.

The structure of the plastic foundation makes it easy to run cables and pipes through the concrete flooring after the floor has been laid.

Stop concrete curing problems with Cupolex® "]]
  )

(defn concrete-page []
  [:<>
   [:.h-6]
   [:.text-3xl.font-medium.leading-tight "Ribraft flooring - an innovative flooring solution on offer from Rapid Slabs"]
   [:.mb-4.text-stone-600.leading-loose "Ribraft is a concrete flooring system that’s becoming increasingly popular for residential
 and commercial applications throughout New Zealand."]
   [:.text-3xl.font-medium.leading-tight "Concrete flooring system offering lots of advantages"]
   [:.mb-4.text-stone-600.leading-loose "Quick to lay, durable, and seismically strong due to its construction method,
Ribraft offers many advantages over standard concrete foundations including the cleaner construction method,
with far less excavated material and waste."]
   (image-component rib-raft-imgs)
   [:.text-3xl.font-medium.leading-tight "Unique construction method uses concrete & polystyrene"]
   [:.mb-4.leading-loose "Ribraft flooring is a concrete flooring system created using polystyrene pods,
 plastic spacers, steel reinforcing pods and RaftMix concrete.
 Due to the construction method of the components simply fitting together and no need to dig footings, labour time and costs can be reduced considerably.
" ]
   [:.text-3xl.font-medium.leading-tight "Where can this flooring solution be used" ]
   [:.mb-4.leading-loose "This concrete and polystyrene flooring system can be used widely,
   from small buildings to large developments."]])

(defn about-page []
  [:<>
    [:.text-3xl.font-medium.text-stone-600.leading-tight "Create a solid foundation for your house or building"]
    [:p.mb-4.text-stone-600.dark:text-gray-400.mb-4.leading-loose
     "Rapid Slabs offer the right solutions for all your concrete floors and slabs requirements. Known for providing a quick, efficient, and quality job at the same time as being cost effective, Rapid Slabs are the team to contact for all your concrete foundation requirements. Call us today!"]
    [:.text-3xl.font-medium.text-stone-600.leading-tight
     "Experienced team leaders with decades of experience"]
    [:.flex-col.flex-grow
     [:img.h-auto.max-w-sm.rounded-lg.shadow-none.transition-shadow.duration-300.ease-in-out {:src "/img/daboys.jpg"
                        :class "hover:shadow-lg hover.shadow-black/50"}]
     [:p.mb-4.text-stone-600.mb-4.leading-loose
     "Certified builders, Adam Jupp & Nick Jones, lead the Rapid Slabs team.
       With a combined over 50 years building experience, Adam & Nick  will
       ensure you receive a reliable and professional service and a quality
       concrete foundation suited to your needs."
      ]
     ]


   ]
  ;; (ui/page
  ;;  {:base/title (str "About " settings/app-name)}
  ;; )
  )



(def tab-names ["Home" "Contacts" "Enquiry" "Concrete" "About"])

(defn tab-component [tab-names]
  (let [page-functions {"Home" home-page
                        "Contacts" contacts-page
                        "Enquiry" enquiry-page
                        "Concrete" concrete-page
                        "About" about-page}]
  [:<>
   [:.w-full.test-sm:font-medium.text-center.rounded-lg.shadow.sm:flex
    [:img.object-cover.bg-stone-200 {:src "/img/rapidslabs-logo.webp"}]
    (for [id tab-names]
      [:button.inline-block.w-full.p-4.bg-stone-200.border-s-0.border-stone-200
       {:role  "tab"
        :name  "tab"
        :value id
        :class
        "focus-within:z-10 dark:border-stone-700 hover:text-stone-700 hover:bg-cyan-200 focus:ring-4 focus:ring-blue-300 focus:outline-none dark:hover:text-white dark:bg-stone-800 dark:hover:bg-stone-700"
        ;; :checked (when (= id "home")
        ;;            true)
        ;; Hyperscript goes in the :_ attribute.
        :_     "on click show .section in #tab-sections when its id is my value"} id])]
   [:.h-6]
   [:#tab-sections
    (for [id tab-names]
      (let [page-fn (get page-functions id)]
        [:.section
         {:id    id
          :style (when-not (= id "Home")
                 {:display "none"})}
      (when page-fn
             (page-fn))
         ]))]]))

(def footer
  [:#footer.w-full.py-14.mx-auto.max-w-7xl.px-4
   {:class '["sm:px-6" "lg:px-8"]}
   [:p.text-gray-500.dark:text-gray-400.text-md "©"
    [:a.link {:href "https://tuatara.red"} "tuatara.red"] " 2024, All rights reserved."]]
  )

(defn app [{:keys [session biff/db] :as ctx}]
  (let [{:user/keys [email firstname lastname]} (xt/entity db (:uid session))]
       (ui/page
        {}
        [:<>
         [:.h-12]
         (tab-component tab-names)

       ;;  [:h-12]
       ;;  footer
         ]
        )))



(defn echo [{:keys [params]}]
  {:status 200
   :headers {"content-type" "application/json"}
   :body params})

(def module
  { ;;:static {"/about/" about-page
          ;;  "/home"  home-page
          ;;  "/contact/" contact-page
     ;;       }
   :routes ["/"
            ["" {:get app}]
            ;; ["/home" {:get home-page}]
            ;; ["/about" {:get about-page}]
            ;; ["/contact" {:get contact-page}]
            ]})

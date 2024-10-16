(ns nz.rapidslabs.app
  (:require [com.biffweb :as biff :refer [q]]
            [nz.rapidslabs.middleware :as mid]
            [nz.rapidslabs.ui :as ui]
            [clojure.string :as str]
            [nz.rapidslabs.settings :as settings]
            [rum.core :as rum]
            [xtdb.api :as xt]
            [ring.adapter.jetty9 :as jetty]
            [cheshire.core :as cheshire]))


(defn home-page []
   [:<>
    [:.h-6]
    [:p.text-stone-600.dark:text-gray-400.text-base
     "Rapid Slabs are the experienced professionals for all concrete flooring, concrete slabs & concrete foundations for the greater Wellington region including the Hutt Valley, Porirua, Kapiti Coast and Levin.
     We offer Cupolex, Speedfloor and Ribraft systems. Talk to us today about your requirements."]]
  )

(defn contacts-page []
   [:<>
    [:.h-6]
  [:p.text-orange-500.dark:text-orange-400.text-md
   "Under construction obviously

you need to call Juppy call 021 430 465"]]
  )

(defn enquiry-page []
  [:<>
  [:p.text-orange-500.dark:text-orange-400.text-md "This is the Enquiry section however I have just copied the cupolex stuff" ]
   [:text.2xl "Wellington region provider of Cupolex® foundations"]

   [:p.text-stone-600 "Rapid Slabs are the only Wellington provider of Cupolex®  Foundations - an up to date technology in concrete flooring.

Latest innovations in concrete flooring

Cupolex®  is one of the latest innovations in concrete flooring that not only saves money but is environmentally friendly as well.

How does Cupolex® work

Using 100% recycled plastic moulded dome forms as a foundation, concrete is poured over the plastic forms creating a structure for the concrete slab. The plastic foundation reduces the amount of contact the concrete has with the soil and restricts the amount of moisture seeping through the concrete
The advantages of using Cuploex®

The Cupolex®  system has the same structural integrity as other concrete foundations while using less concrete - no need for fill or gravel, and it controls moisture content in the concrete.

The structure of the plastic foundation makes it easy to run cables and pipes through the concrete flooring after the floor has been laid.

Stop concrete curing problems with Cupolex® "]]
  )

(defn concrete-page []
  [:<> "Ribraft flooring - an innovative flooring solution on offer from Rapid Slabs

Ribraft is a concrete flooring system that’s becoming increasingly popular for residential and commercial applications throughout New Zealand.


Concrete flooring system offering lots of advantages

Quick to lay, durable, and seismically strong due to its construction method, Ribraft offers many advantages over standard concrete foundations including the cleaner construction method, with far less excavated material and waste.


Unique construction method uses concrete & polystyrene

Ribraft flooring is a concrete flooring system created using polystyrene pods, plastic spacers, steel reinforcing pods and RaftMix concrete. Due to the construction method of the components simply fitting together and no need to dig footings, labour time and costs can be reduced considerably.


Where can this flooring solution be used

This concrete and polystyrene flooring system can be used widely, from small buildings to large developments."])

(defn about-page []
  (ui/page
   {:base/title (str "About " settings/app-name)}
   [:<>
    [:text-xl.font-bold "Create a solid foundation for your house or building"]
    [:p.text-gray-500.dark:text-gray-400.text-md
     "Rapid Slabs offer the right solutions for all your concrete floors and slabs requirements. Known for providing a quick, efficient, and quality job at the same time as being cost effective, Rapid Slabs are the team to contact for all your concrete foundation requirements. Call us today!"]
    [:text-lg.font-bold
     "Experienced team leader with decades of experience"]
    [:p.text-gray-500.dark:text-gray-400.text-md
     "Certified builder, Adam Jupp, leads the Rapid Slabs team. With 25 years in the building industry, Adam will ensure you receive a reliable and professional service and a quality concrete foundation suited to your needs."]

   ]))

(def tab-names ["Home" "Contacts" "Enquiry" "Concrete" "About"])

(defn tab-component [tab-names]
  (let [page-functions {"Home" home-page
                        "Contacts" contacts-page
                        "Enquiry" enquiry-page
                        "Concrete" concrete-page
                        "About" about-page}]
  [:<>
   [:.w-full.test-sm:font-medium.text-center.rounded-lg.shadow.sm:flex
    (for [id tab-names]
      [:button.inline-block.w-full.p-4.bg-stone.border-s-0.border-stone-200
       {:role  "tab"
        :name  "tab"
        :value id
        :class
        "focus-within:z-10 dark:border-stone-700 hover:text-stone-700 hover:bg-stone-50 focus:ring-4 focus:ring-blue-300 focus:outline-none dark:hover:text-white dark:bg-stone-800 dark:hover:bg-stone-700"
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

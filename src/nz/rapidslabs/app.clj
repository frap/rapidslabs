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
   [:.w-full.test-sm:font-medium.text-center.rounded-lg.shadow
    (for [img img-names]
      [:img.mb-4.h-auto.max-w-full.rounded-full {:src (str "/img/" img ".webp")}
       ])]])

(def juppy-tel [:a {:href "tel:0275600679"} "027 560 0679" ])
(defn contact-us []
  [:<>
   [:div.mx-auto.px4
    [:h2.text-2xl.font-semibold.text-stone-400.mb-4 "Contact Us"]
    [:mb-8
     [:h3.text-lg.font-semibold.text-stone-400.mb-2
      "Our Address"]
     [:p.text-stone-300
      "wher we are"]]
    [:mb-8
     [:h3.text-lg.font-semibold.text-stone-400.mb-2
      "Phone"]
     [:p.text-stone-300
      juppy-tel]]
    [:mb-8
     [:h3.text-lg.font-semibold.text-stone-400.mb-2
      "Email"]
     [:p.text-stone-300
      "juppy@rapidslabs.co.nz"]]
    [:.relative.h-0.overflow-hidden.mb-6 {:style "padding-bottom: 56.25%;"}
     [:iframe.absolute.top-0.left-0.w-full.h-full
      {:src "https://www.google.com/maps/embed?pb=!1m18!1m12!1m3!1d3353.096656292789!2d-122.08217698485344!3d37.42205787981786!2m3!1f0!2f0!3f0!3m2!1i1024!2i768!4f13.1!3m3!1m2!1s0x808fbc7b4be10725%3A0xf59d178a87b32f52!2sGolden%20Gate%20Bridge!5e0!3m2!1sen!2sus!4v1610035984427!5m2!1sen!2sus"
       :frameborder 0 :style "border:0;" :allowfullscreen "" :aria-hidden "false" :tabindex 0}]]
    ]])

(defn home-page []
  [:<>
   [:.h-6]
   [:img.flex-col.rounded-lg.shadow-none.transition-shadow.duration-300.ease-in-out
    {:src "/img/rapidslabs-precast.jpg"
     :class "hover:object-scale-down hover:shadow-lg hover.shadow-black/50"}]
   [:.h-6]
   [:p.mb-4.text-stone-600.text-base.dark:text-stone-200
    "Rapid Slabs are the experienced professionals for all concrete flooring, concrete slabs & concrete foundations for the greater Wellington region including the Hutt Valley, Porirua, Kapiti Coast and Levin.
     We offer Cupolex, Speedfloor and Ribraft systems. Talk to us today about your requirements."]]
  )

(defn enquiry-page []
   [:<>
    [:.h-6]
    [:p.text-orange-500.dark:text-orange-200.text-base.p-4
     "Under construction obviously you need to call Juppy " [:a {:href "tel:0275600679"} "027 560 0679" ]]
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
    [:img.h-48.p-4 {:src "/img/slabs-truck.jpg"
                    :class "hover:object-scale-down"} ]]
  )

(defn cupolex-page []
  [:<>
   [:.h-6]
   [:.text-3xl.font-medium.text-stone-600.leading-tight {:class "dark:text-stone-200"}
    "Wellington region provider of Cupolex® foundations"]
   [:.mb-4.text-stone-600.text-base {:class "dark:text-stone-200"}
    "Rapid Slabs are the only Wellington provider of Cupolex®  Foundations - an up to date technology in concrete flooring."]
   [:.text-3xl.font-medium.text-stone-600.leading-tight {:class "dark:text-stone-200"}
    "Latest innovations in concrete flooring"]
   [:.mb-4.text-stone-600.leading-loose {:class "dark:text-stone-200"}
    "Cupolex®  is one of the latest innovations in concrete flooring that not only saves money but is environmentally friendly as well."]
   [:.text-3xl.font-medium..text-stone-600.leading-tight {:class "dark:text-stone-200"}
    "How does Cupolex® work"]
   [:.mb-4.text-stone-600.leading-loose {:class "dark:text-stone-200"}
    "Using 100% recycled plastic moulded dome forms as a foundation, concrete is poured over the plastic forms creating a structure for the concrete slab. The plastic foundation reduces the amount of contact the concrete has with the soil and restricts the amount of moisture seeping through the concrete
The advantages of using Cuploex®
The Cupolex®  system has the same structural integrity as other concrete foundations while using less concrete - no need for fill or gravel, and it controls moisture content in the concrete.

The structure of the plastic foundation makes it easy to run cables and pipes through the concrete flooring after the floor has been laid.

Stop concrete curing problems with Cupolex® "]])

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
   [:.text-3xl.font-medium.text-stone-600.leading-tight.dark:text-stone-100
    "Where can this flooring solution be used" ]
   [:.mb-4.text-stone-600.leading-loose {:class "dark:text-stone-200"}
    "This concrete and polystyrene flooring system can be used widely, from small buildings to large developments."  (image-component rib-raft-imgs) ]])

(defn speedfloor-page []
  [:<>
   [:.h-6]
   [:.text-2xl.font-medium.text-stone-600.leading-tight.dark:text-stone-100
    "Speedfloor - a lightweight suspended concrete floor system"]
   [:div "test"]])

(defn about-page []
  [:<>
   [:.h-6]
   [:.text-2xl.font-medium.text-stone-600.leading-tight.dark:text-stone-100
    "Create a solid foundation for your house or building"]
   [:p.mb-4.text-stone-600.dark:text-gray-400.mb-4.leading-loose.dark:text-stone-100
    "Rapid Slabs offer the right solutions for all your concrete floors and slabs requirements. Known for providing a quick, efficient, and quality job at the same time as being cost effective, Rapid Slabs are the team to contact for all your concrete foundation requirements. Call us today!"]
   [:.text-2xl.font-medium.text-stone-600.leading-tight {:class "dark:text-stone-200"}
    "Experienced team leaders with decades of experience"]
   [:.flex.py-2
    [:img.h-24.max-w-sm.rounded-lg.shadow-none.transition-shadow.duration-300.ease-in-out.px-2 {:src "/img/daboys.jpg"
                                                                                                :class "hover:shadow-lg hover.shadow-black/50"}]
    [:p.mb-4.text-stone-600.mb-4.leading-loose {:class "dark:text-stone-200"}
     "Certified builders, Adam Jupp & Nick Jones, lead the Rapid Slabs team.
       With a combined over 50 years building experience, Adam & Nick  will
       ensure you receive a reliable and professional service and a quality
       concrete foundation suited to your needs."
     ]]
   ;;(contact-us)
   ]
  )


(def tab-names ["Home" "Cupolex®" "Speedfloor" "Ribraft Foundation" "Enquiry" "About Rapid Slabs"])

(defn tab-component [tab-names]
  (let [page-functions {"Home" home-page
                        "Cupolex®" cupolex-page
                        "Speedfloor" speedfloor-page
                        "Ribraft Foundation" ribraft-page
                        "Enquiry" enquiry-page
                        "About Rapid Slabs" about-page}]
  [:<>
   [:.w-full.test-sm:font-medium.text-center.rounded-lg.dark:bg-stone-400.dark:border-stone-700.flex
    [:img.object-cover.bg-stone-200 {:src "/img/rapidslabs-logo.webp"}]
    (for [id tab-names]
      [:button.inline-block.w-full.p-4.bg-stone-200.border-s-0.border-stone-300
       {:role  "tab"
        :name  "tab"
        :value id
        :class
        "focus-within:z-10 hover:text-stone-700 hover:bg-cyan-200 focus:ring-4 focus:ring-cyan-300 rounded-lg dark:hover:text-white dark:bg-stone-400 dark:hover:bg-stone-600 dark:border-stone-700"
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

(comment
  (macroexpand (rum/render-html (contact-us)))
  ;; => Execution error (ClassCastException) at rum.server-render/render-style! (server_render.clj:289).
  ;;    class java.lang.Character cannot be cast to class java.util.Map$Entry (java.lang.Character and java.util.Map$Entry are in module java.base of loader 'bootstrap')
  ;; => "<div class=\"w-full test-sm:font-medium text-center rounded-lg shadow sm:fle\"><div src=\"/img/rib-raft-1c.webp\" class=\"mb-4 h-auto max-w-full rounded-full\"></div><div src=\"/img/rib-raft-1d.webp\" class=\"mb-4 h-auto max-w-full rounded-full\"></div><div src=\"/img/rib-raft-1f.webp\" class=\"mb-4 h-auto max-w-full rounded-full\"></div></div>"

  ,)

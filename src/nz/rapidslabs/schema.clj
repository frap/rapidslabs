(ns nz.rapidslabs.schema)

(def schema
  {:user/id :uuid
   :user [:map {:closed true}
          [:xt/id                           :user/id]
          [:user/email                      :string]
          [:user/joined-at                  inst?]
          [:user/firstname                  :string]
          [:user/lastname                   :string]
          [:user/telephone {:optional true} :string]]

   :enquiry/id :uuid
   :enquiry [:map {:closed true}
         [:xt/id           :enquiry/id]
         [:enquiry/user    :user/id]
         [:enquiry/text    :string]
         [:enquiry/sent-at inst?]]})

(def module
  {:schema schema})

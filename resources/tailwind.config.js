module.exports = {
  content: [
    './src/**/*',
    // './resources/**/*',
    "./resources/public/node_modules/tw-elements/js/**/*.js"
  ],
  theme: {
    extend: {},
  },
  plugins: [
    require('@tailwindcss/forms'),
    require("tw-elements/plugin.cjs"),
  ],
  darkMode: "class"
}

module.exports = {
  content: [
    './src/**/*',
    './node_modules/tw-elements/js/**/*.js',
    "./resources/node_modules/tw-elements/js/**/*.js"
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

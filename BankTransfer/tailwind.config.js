// tailwind.config.js
/** @type {import('tailwindcss').Config} */
module.exports = {
  content: [
    "./src/**/*.{html,ts}",
  ],
  darkMode: 'class',
  theme: {
    extend: {
      colors: {
        'primary': '#1193d4',
        'secondary': '#28a745',
        'background-light': '#f6f7f8',
        'background-dark': '#101c22',
        'text-light': '#6c757d',
        'text-dark': '#adb5bd'
      },
      fontFamily: {
        'display': ['Manrope', 'sans-serif']
      },
      borderRadius: {
        'DEFAULT': '0.25rem',
        'lg': '0.5rem',
        'xl': '0.75rem',
        'full': '9999px'
      },
    },
  },
  plugins: [
    require('@tailwindcss/forms'),
  ],
}

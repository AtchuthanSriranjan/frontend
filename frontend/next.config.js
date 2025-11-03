/** @type {import('next').NextConfig} */
const nextConfig = {
  reactStrictMode: true,

  output: "standalone",

  experimental: {
    turbo: {
      rules: {},
    },
  },
};

module.exports = nextConfig;
